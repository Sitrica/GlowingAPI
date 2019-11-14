# GlowingAPI v1.0.0
### Requires ProtocolLib https://www.spigotmc.org/resources/protocollib.1997/
An API for handling client side glowing effects.

# Compiling

### Maven
Maven requires setting up profiles and defining the token else where https://help.github.com/en/github/managing-packages-with-github-packages/configuring-apache-maven-for-use-with-github-packages

### Gradle
In your `build.gradle` add: 
```groovy
repositories {
	maven {
		url 'https://maven.pkg.github.com/Sitrica/GlowingAPI/'
		credentials {
			username = "Sitrica"
			password = project.findProperty("gpr.key") ?: System.getenv("GITHUB_PACKAGES_KEY")
		}
	}
}

dependencies {
	compile (group: 'com.sitrica', name: 'GlowingAPI', version: '1.0.0')
}
```
Getting a Github token:

1.) Go into your account settings on Github and create a personal token with the read:packages scope checked.

2.) Generate that key, and now go add a System Environment Variable named GITHUB_PACKAGES_KEY

3.) Restart computer or if using Chocolatey type `refreshenv`

Note: you can just directly put your token as the password, but we highly discourage that.

Example of usage:
```java
public class ExamplePlugin extends JavaPlugin {

	private static ExamplePlugin instance;
	private GlowingAPI glowing;

	@Override
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
