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

There are 3 available conatiners: PostbackContainer, TextContainer and LocationContainer.
Each container has two constructors. First - has one parameter "packageName", which is the root package where your processors are located.
The second has two parameters "packageName" and "spliterator", which is used to encode postback and state parameters into string.

Example:
```java
	@Bean
	public TextContainer<User> textContainer() {
		return new TextContainer<>("com.botscrew");
	}
```

Containers are used as an entry for your requests. They redirect your requests based on their contents and *ChatUsers'* object state.

2) Create classes annotated with @PostbackProcessor, @TextProcessor, @LocationProcessor

These classes are going to be picked up by containers when they are located inside of the package specified in container constructor.
Their methods are going to be used as processors for text messages submitted to the *TextContainer*.

>**Note**
> @TextProcessor, @PostbackProcessor and @ApiAiProcessor annotations already contain @Component annotation inside of themselves.

<h3>Processing text messages</h3>

To process text messages first create a bean with class TextContainer.

```java
    @Bean
	public TextContainer<User> textContainer() {
		return new TextContainer<>(packageWithTextPorcessorsName);
	}
```
Next, create beans annotated with @TextProcessor and methods inside of them that are annotated with @Text to which user messages are going to be delegated to:

```java
    @TextProcessor
    public class YourTextProcessor{
        @Text(states={"USER_STATE"})
        public void processChatUserMessage(ChatUser user, String message){

        }
    }
```

In order to submit a message to *TextContainer* use method *processText*

```java
   textContainer.processText(messageText, chatUser);
```

Each method with @Text annotation is used as a processor for requests. Each state can have only one processor, but one processor can be responsible for multiple states.
If multiple processing methods are responsible for the same *ChatUser*'s states, you will get a runtime DuplicatedActionException exception on
startup. If you do not specify any of the *ChatUser*'s states in your method it is going to be treated as a fallback method and is going to be used only when
some state does not have a corresponding processing method, the same way as with any other processing method there can only be one processing method with no
specified states.

Text processors' and methods' parameters can include message text and  the object of class that implements *ChatUser* interface.

<h3>Processing postback messages</h3>

Unlike text messages, Postbacks' payload is defined by us, instead of users. So, not only we can use these static values to map particular messages
to their processing methods but also define some postback parameters by including them as key value pairs of strings into postback value separated
with '?' by default or by spliterator defined inside of PostbackContainer constructor.

```java
    @Bean
    public PostbackContainer postbackConteiner(){
        return new PosbackContainer(packageWithPostbackPorcessorsName, spliterator);
    }
```

Just like TextContainer - PostbackContainer will use the package name defined inside of its constructor to find any classes annotated with
@PostbackProcessor annotation. And then it finds methods annotated with @Postback annotation inside those classes. These methods are going to be
called based on requests' postback value as well as *ChatUser* state. Here is how it works:

1. We generate a key that consists of received postback value and user state, missing values are replaced with DEFALT_POSTBACK and ALL_STATES.
2. First, we look for methods that match our key both in user state and postback value.
3. if none have matched, we look for methods with the key that consists of the default state and matching postback.
4. If none have matched, we look for methods with the key that consists of matching state and default postback.
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

Just like *ChatUser's* states - postbacks can contain parameters. If you don't need to include a state parameters map in your @Postback annotated method, you can simply include an
argument with class Map<String, String> and it is going to be automatically assigned a value of postback parameters. If you want only state parameters or both state parameters
and postback parameters, you will have to put appropriate annotations on each method argument.

<h3>Processing API AI, messages</h3>

API AI is a response we receive from user's input analyzing services like DialogFlow. Just like with others messages we first have to define a container:

```java
    @Bean
    public ApiAiContainer apiAiContainer(){
        return new ApiAiContainer(packageNameWithApiAiProcessorsName);
    }
```

ApiAiProcessors use *ChatUser*'s states and ApiAiResponse action value to map requests to appropriate processor methods.
Instead of postback value, we will look for an Action parameter. Action is a String corresponding to the user's intent identified by NLP service.

```java
    public void processApiAi(ChatUser user,
                            @ApiAiParameters Map<String, String> apiAiParams,
                            @Fullfillment Fulfilment fulfilment,
                            @StateParameters Map<String, String> stateParams){
        //do staff
    }
```

ApiAi parameters besides the *ChatUser* argument can include a map of ApiAiParameters with keys representing names of keywords found inside of user's input
by NLP service. Just like with postback processors - we have to include appropriate annotations when we use apiAiParameters with stateParameters
or only stateParameters. Fulfillment is an object that contains a proposed ApiAi service responses for particular intent.

<h3>Processing Location messages</h3>

Location messages are an unique case of postback messages.

```java
    @Bean
    public LocationContainer locationContainer(){
        return new LocationContainer(packageNameWithLocationProcessors);
    }
```

Locations are returned in the form of an object of the class *GeoCoordinates* that you can insert like an argument of your processor methods.

```java
    @Location(states={"SOME_STATE"})
    public void processLocationMessage(GeoCoordinates coordinates){

    }
```

The same way as text processors - location processors are guided only by the *ChatUser* state.






