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
		<version>1.1.0-rc</version>
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
2) Create classes with annotations @PostbackProcessor, @TextProcessor, @LocationProcessor
> **Note:**
   `PostbackContainer` has one more constructor with spliterator parameter
>
