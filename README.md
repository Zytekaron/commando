# commando
### Version: 1.5.1
##### Requires Java 11+
##### base package renamed to `com.zytekaron` as of 1.5.0
[![](https://jitpack.io/v/com.zytekaron/commando.svg)](https://jitpack.io/#com.zytekaron/commando)

This is the command handler I use in my own JDA bots. Feel free to use it.

<br>

## Installation

### Gradle
```groovy
repositories {
    maven { url = 'https://jitpack.io' }
}
```
```groovy
depencencies {
    compile 'com.zytekaron:commando:1.5.1'
}
```

### Maven
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

```xml
<dependencies>
    <dependency>
        <groupId>com.zytekaron</groupId>
        <artifactId>commando</artifactId>
        <version>1.5.1</version>
    </dependency>
</dependencies>
```

<br>

## Usage Examples
[Main.java](src/test/java/com/zytekaron/commando/Main.java)
<br>
[PingCommand.java](src/test/java/com/zytekaron/commando/commands/PingCommand.java)
<br>
[EvalCommand.java](src/test/java/com/zytekaron/commando/commands/EvalCommand.java)

<br>

## License
<b>commando</b> is licensed under the [GNU Lesser General Public License Version 3](https://github.com/Zytekaron/commando/blob/master/LICENSE)