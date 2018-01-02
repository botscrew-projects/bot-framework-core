<h2>Botscrew Bot Framework </h2>
<h3>Integration</h3>

To add this library to your project you need to update your pom.xml file:

1) add repository with this library:
```xml
	<repositories>
        <repository>
            <id>myMavenRepo.read</id>
            <url>https://mymavenrepo.com/repo/gSB19txzRY1QEt9uuoC9/</url>
        </repository>
    </repositories>
```

2) add library dependency:
```xml
    <dependency>
		<groupId>com.botscrew</groupId>
		<artifactId>bot-framework</artifactId>
		<version>1.1.0-rc2</version>
	</dependency>
```

After that, library is ready to use.

<h3>Usage</h3>

1) First you need to create beans for all needed containers.

There are 3 available conatiners: PostbackContainer, TextContainer, LocationContainer
Containers' constructors have one parameter "packageName", which is the root package where your processors is located.
Example:
```java
	@Bean
	public TextContainer<User> textContainer() {
		return new TextContainer<>("com.botscrew");
	}
```

Containers are a used as an entry for your requests they redirect your request based on their contents and *ChatUser`s* object state.

2) Create classes with annotations @PostbackProcessor, @TextProcessor, @LocationProcessor
> **Note:**
   `PostbackContainer` has one more constructor with spliterator parameter
>

    If the class is located inside of the package we have specified inside of appropriate container bean is annotated with one of this annotations,
its methods are going to be used as a as processors for text messages submitted to appropriate container.


<h3>Processing text messages</h3>

To process text messages first create a bean with class TextContainer.
```java
    @Bean
	public TextContainer<User> textContainer() {
		return new TextContainer<>(packageWithTextPorcessorsName);
	}
```
Next create a processors beans and methods inside of them to which our messages are going to be deligated to:

```java
    @TextProcessor
    public class YourTextProcessor{
        @Text(states={"USER_STATE"})
        public void processChatUserMessage(ChatUser user, String message){

        }
    }
```
    In order to submit a message to textContainer use method *processText*
```java
   textContainer.processText(messageText, chatUser);
```

    Each method with @Text annotation is used as processor for requests. Each state can have only one processor, but one processor can be responsible for multiple states.
If multiple processing methods are responsible for requests with the same states you will get a runtime DuplicatedActionException exeption on startup.
If you do not specify any of the *ChatUser* states in your method it is going to be treated as a fallback method and going to be used only when some state does not have a
corresponding processing method, the same way as with any other processing method there can only be one processing method with no specified states.
    Text processors` methods parameters can only include message text and object of class that implements *ChatUser* interface.


<h3>Processing postback messages</h3>

    Postbacks` payload is defined by us unlike text messages instead of users. So not only we can use this static values to map particular messages to their processing methods
but also define some postback parameters by including them as key value pairs of strings into postback value separated with '?' by default or spliterator defined inside of
PostbackContainer constructor.

```java
    @Bean
    public PostbackContainer postbackConteiner(){
        return new PosbackContainer(packageWithPostbackPorcessorsName, spliterator);
    }
```
    Just like TextContainer - PostbackContainer will use the package name defined inside of it`s constructor to find any classes annotated with @PostbackProcessor annotation.
And then find methods annotated with @Postback annotation inside those classes. These methods are going to be called based on requests`
postback value as well as user user state. Here is how it works:

1. We generate a key that consists of received postback value and user state, missing values are replaced with DEFALT_POSTBACK and ALL_STATES.
2. First we look for methods that matches our key both in user state and postback value.
3. if none have matched, we look for methods with key consisting of default state and matching postback.
4. If none have matched, we look for methods with key consisting of matching state and default postback.
5. if none have matched, we look for methods with both default keys and default postbacks.
6. If no methods have been found a IlligalArgumentException will be thrown.


```java
    @PostbackProcessor
    public class YourPostbackProcessor{

        @Postback(states = {"USER_STATE"}, postback = "POSTBACK")
        public void processPostback(ChatUser user, @PostbackParameters Map<String, String> postbackParams, @StateParameters Map<String, String> stateParameters){

        }

    }
```

    Just like *ChatUser`s* states - postbacks can contain parameters. If you don't need to include state parameters map in your @Postback annotated method you can simply include an
argument with class Map<String, String> and it is going to be automatically assigned a value of postback parameters, if you want only state parameters or both state parameters
and postback parameters you will have to put appropriate annotations on each method argument.

<h3>Processing API AI, messages</h3>

API AI is a response we recieve from user`s input analyzing services like DialogFlow. Just like with others messages we first have to define a container:
```java
    @Bean
    public ApiAiContainer apiAiContainer(){
        return new ApiAiContainer(packageNameWithApiAiProcessorsName);
    }
```
Similar to the with PostbackProcessors ApiAiProcessors has an additional parameter to match when looking for appropriate method to match user`s message.
Just instead of postback value we will look for Action parameter. Action is a String corresponding to the user intent identified by nlp service.

```java
    public void processApiAi(ChatUser user,
                            @ApiAiParameters Map<String, String> apiAiParams,
                            @Fullfillment Fulfilment fulfilment,
                            @StateParameters Map<String, String> stateParams){
        //do staff
    }
```
ApiAi parameters besides the *ChatUser* argument can include map of ApiAiParameters with keys representing names of keywords found inside of user input by nlp service.
Just like with postback processors we have to include appropriate annotations when we use apiAiParameters with stateParameters or only stateParameters.
Fulfillment is an object that contains a proposed by ApiAi service responses for particular intent.

<h3>Processing Location messages</h3>

Location messages are unique case of postback message.

```java
    @Bean
    public LocationContainer locationContainer(){
        return new LocationContainer(packageNameWithLocationProcessors);
    }
```

Locations are returned in the form of an object of the class *GeoCoordinates* that you can insert like an argument of yours processor methods.

```java
    @Location(states={"SOME_STATE"})
    public void processLocationMessage(GeoCoordinates coordinates){

    }
```

The same way as text processors - location processors are guided only by the *ChatUser* state.
