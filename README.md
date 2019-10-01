# GlowingAPI v1.0.0
### Requires ProtocolLib https://www.spigotmc.org/resources/protocollib.1997/
An API for handling client side glowing effects.

GlowingAPI currently uses Jitpack https://jitpack.io/#TheLimeGlass/GlowingAPI/1.0.0
### Maven
In your `pom.xml` add:
```xml
<repositories>
  <repository>
      <id>jitpack.io</id>
      <url>https://jitpack.io</url>
  </repository>
</repositories>

<dependency>
    <groupId>com.github.TheLimeGlass</groupId>
    <artifactId>GlowingAPI</artifactId>
    <version>1.0.0</version>
</dependency>

```
### Gradle
In your `build.gradle` add: 
```groovy
repositories {
  maven {
    url 'https://jitpack.io'
  }
}

dependencies {
  compile 'com.github.TheLimeGlass:SkriptHub-Java-API:1.0.0'
}
```

Example of usage:
```java
public class ExamplePlugin extends JavaPlugin {

	private static ExamplePlugin instance;
	private GlowingAPI glowing;

	public void onEnable() {
		instance = this;
		glowing = new GlowingAPI(instance);
	}

	public static ExamplePlugin getInstance() {
		return instance;
	}

	public GlowingAPI getGlowingAPI() {
		return glowing;
	}

}

```
```java
ExamplePlugin instance = ExamplePlugin.getInstance();
GlowingAPI glowing = instance.getGlowingAPI();

glowing.setGlowing(entity, player);
glowing.stopGlowing(entity, player);

glowing.setTimedGlowing(5, TimeUnit.SECONDS, entity, player);
```
