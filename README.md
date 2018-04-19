# Bot Framework Spring Boot Starter

Bot Framework makes it easier to work with chat bot applications
based on Spring Boot architecture.



##### 1. [*Getting started*] (https://gitlab.com/bots-crew/bot-framework/blob/boot-starter/README.md#getting-started-1)
##### 2. [*User*] (https://gitlab.com/bots-crew/bot-framework/blob/boot-starter/README.md#user)
##### 3. [*Handlers*] (https://gitlab.com/bots-crew/bot-framework/blob/boot-starter/README.md#handlers)
##### 4. [*Handlers example*] (https://gitlab.com/bots-crew/bot-framework/blob/boot-starter/README.md#handlers-example)
##### 5. [*Parameters*] (https://gitlab.com/bots-crew/bot-framework/blob/boot-starter/README.md#parameters)
##### 6. [*Containers usage example*] (https://gitlab.com/bots-crew/bot-framework/blob/boot-starter/README.md#container-usage-example)



### Getting started:

To include bot framework to your project 
you can add dependency to your build configuration

Add the next repository path:

`<repositories>
        <repository>
            <id>MyGet</id>
            <url>https://www.myget.org/F/bots-crew/maven</url>
        </repository>
    </repositories>`

Add the dependency:

`<dependency>
            <groupId>com.botscrew</groupId>
            <artifactId>bot-framework-spring-boot-starter</artifactId>
            <version>{bot-framework-version}</version>
        </dependency>`
        
### User

To start work with bot-framework you need implement `ChatUser` interface in your project.

The main goal of the project is to help you split up 
bot flow into independent separated handlers. Splitting logic 
is based on 'state' of user, so we can trigger different handlers for 
different user states. For example:

```java
@Text(states = {"DEFAULT"})
public void handleText(ChatUserImpl user, @Text String text) {
log.info("Text handled: " + text);
//This method will be called for user with state 'DEFAULT'.
}
```

You can define a few states for one method, we will trigger it in case of any of them.

Or you can define method without states and it will be default handler if there 
are no other appropriate handlers.

### Handlers

* We call 'handler' a method with specific annotation which will be called when some related action happens.

To mark your class as handlers' container you need to put @ChatEventsProcessor on it.
(In case of IntentContainer you need to put @IntentProcessor)

There are next containers available at the moment:

* TextContainer(mark handler with @Text annotation) 
* PostbackContainer(mark handler with @Postback annotation)
* LocationContainer(mark handler with @Location annotation)
* IntentContainer(mark handler with @Intent annotation)

They are entry points for your events.

* Text:

```java
@ChatEventsProcessor
public void TextHandler {
    @Text(states={"STATE1", "STATE2"})
    public void handle(ChatUserImpl user, @Text String text, @Param("param") Integer param) {
    }
}
```


* Location:

```java
@ChatEventsProcessor
public void LocationHandler {
    @Location(states={"STATE1", "STATE2"})
    public void handle(ChatUserImpl user, @Location Coordinates coordinates) {
    }
}
```


* Postback:

```java
@ChatEventsProcessor
public void PostbackHandler {
    @Postback(value="POSTBACK", states={"STATE"})
    public void handle(ChatUserImpl user, @Param("id") Integer id) {
    }
}
```


* Intent:

```java
@IntentProcessor
public void IntentHandler {
    @Intent(value="INTENT", states={"STATE1", "STATE2"})
    public void handle(ChatUserImpl user, @Param("id") Integer id) {
    }
}
```

* Referral:

```java
@ChatEventsProcessoor
public void ReferralHandler {
    @Referral(value="advertisement_ref", states={"STATE1", "STATE2"})
    public void handle(ChatUserImpl user, @Referral String fullRef, @Original Referral referral) {
        log.info(referral.getSource());
    }
}
```

* Read:

```java
@ChatEventsProcessoor
public void ReadHandler {
    @Read(states={})
    public void handle(ChatUserImpl user, @Original Read read) {
        log.info(read.getWatermark());
    }
}
```

* Echo:

```java
@ChatEventsProcessoor
public void EchoHandler {
    @Echo(states={})
    public void handle(ChatUserImpl user, @Original Message echoMessage) {
        log.info(echoMessage.getMid());
    }
}
```

* Delivery:

```java
@ChatEventsProcessoor
public void DeliveryHandler {
    @Delivery(states={})
    public void handle(ChatUserImpl user, @Original Delivery delivery) {
    }
}
```

### Parameters

BotFramework support additional parameters which you can pass to state(postback/intent) 
in the next way: `STATE?string_param=param?int_param=1?double_param=2.5`

You can receive those parameters in your handler in the next way:

```java
@Text
public void handleText(@Param("string_param") String stringParam, 
                       @Param("int_param") Integer intParam, 
                       @Param("double_param") Double param) {
                   }
```


### Container Usage Example

`Container` repesents an object where you can pass event data and it will find appropriate handler for it and trigger it with your data.

You can pass a set of arguments which you want to be able to get in target method.

`ArgumentKit` is an object which contains arguments available for passing to selected handler.
`SimpleArgumentKit` is default implementation for it.

`ArgumentKit` compare arguments by type and by name.
You cannot pass 2 arguments with the same name and 2 arguments with the same type(!WITHOUT names)

* LocationContainer

```java
@Autowired
private LocationContainer container;

public void processLocation(User user, Coordinates coordinates) {
    ArgumentKit kit = new SimpleArgumentKit();
    kit.put(ArgumentType.COORDINATES, new SimpleArgumentWrapper(coordinates));

    container.process(user, kit);
}
```

* TextContainer

```java
@Autowired
private TextContainer container;

public void processText(User user, String text) {
    ArgumentKit kit = new SimpleArgumentKit();
    kit.put(ArgumentType.TEXT, new SimpleArgumentWrapper(text));

    container.process(user, kit);
}
```

* PostbackContainer

```java
@Autowired
private PostbackContainer container;

public void processPostback(User user, String postback) {
    container.process(user, postback);
}
```

* IntentContainer

```java
@Autowired
private IntentContainer container;

public void processPostback(User user, String intent, String originalQuery) {
    ArgumentKit kit = new SimpleArgumentKit();
    kit.put(ArgumentType.TEXT, new SimpleArgumentWrapper(originalQuery));
    container.process(user, intent, kit);
}
```
