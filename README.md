# Bot Framework Spring Boot Starter

Bot Framework makes it easier to work with chat bot applications
based on Spring Boot architecture.

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

To start work with bot-framework you need implement ChatUser interface in your project.

The main goal of the project is to help you split up 
bot flow into independent separated handlers. Splitting logic 
is based on 'state' of user, so we can trigger different handlers for 
different user states. For example:

```
@Text(states = {"DEFAULT"})
public void handleText(ChatUserImpl user, @Text String text) {
log.info("Text handled: " + text);
//This method will be called for user with state 'DEFAULT'.
}
```

You can define a few states for one method, we will trigger it in case of any of them.

Or you can define method without states and it will be default handler if there 
are no other appropriate handlers.

To mark your class as handlers' container you need to put @ChatEventsProcessor on it.
(In case of IntentContainer you need to put @IntentProcessor)

There are next containers available at the moment:
    - TextContainer
    - PostbackContainer
    - LocationContainer
    - IntentContainer

They are entry points for your events.

BotFramework support additional parameters which you can pass to state(postback/intent) 
in the next way: STATE?string_param=param?int_param=1?double_param=2.5

You can receive those parameters in your handler in the next way:

`@Text
public void handleText(@param("string_param") String stringParam, 
                       @param("int_param") Integer intParam, 
                       @param("double_param") Double param) {
                   }`
                   
                   

Usage example:

Text:

`@ChatEventsProcessor
public void TextHandler {
    @Text(states={"STATE1", "STATE2"})
    public vodi handle(ChatUserImpl user, @Text String text, @Param("param") Integer param) {
    }
}`



Location:

`@ChatEventsProcessor
public void LocationHandler {
    @Location(states={"STATE1", "STATE2"})
    public vodi handle(ChatUserImpl user, @Location Coordinates coordinates) {
    }
}`



Postback:

`@ChatEventsProcessor
public void PostbackHandler {
    @Postback(value="POSTBACK", states={"STATE"})
    public vodi handle(ChatUserImpl user, @Param("id") Integer id) {
    }
}`



Intent:

`@IntentProcessor
public void IntentHandler {
    @Text(value="INTENT", states={"STATE1", "STATE2"})
    public vodi handle(ChatUserImpl user, @Param("id") Integer id) {
    }
}`