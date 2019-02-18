# OmniTrade API JVM Client

**OmniTrade API JVM Client**  is an open-source library that integrates the [OmniTrade](https://omnitrade.io/) API for the JVM, written in Kotlin. You can read the API documentation by visiting  [this link](https://omnitrade.io/documents/api_v2)

## Usage:

You can use by adding it to your dependencies.

Gradle:

```groovy
dependencies {
  implementation 'com.omnitrade:omnitrade-client:0.1.0'
}
```

Maven:

```xml
<dependency>
  <groupId>com.omnitrade</groupId>
  <artifactId>omnitrade-client</artifactId>
  <version>0.1.0</version>
</dependency>
```

## Usage example

1.  Instantiate the client using the builder:

Kotlin:
```kotlin
val client = OmnitradeClient.Builder
            .accessKey("YOUR ACCESS KEY")
            .secretKey("YOUR SECRET KEY")
            .timeout(60L)
            .build()
```

Java:
```java
final OmnitradeClient client = OmnitradeClient.Builder.INSTANCE
                        .setAccessKey("YOUR ACCESS KEY")
                        .setSecretKey("YOUR SECRET KEY")
                        .setTimeout(60L)
                        .build()
```

The `accessKey` and `secretKey` are only needed for the private API calls. If you build a client without the keys, you can set them up using the method `setupKeys`:

Kotlin:
```kotlin
val client = OmnitradeClient.Builder.build()
client.setupKeys("YOUR ACCESS KEY", "YOUR SECRET KEY")
```

Java:
```java
final OmnitradeClient client = OmnitradeClient.Builder
                        .INSTANCE.build()
client.setupKeys("YOUR ACCESS KEY", "YOUR SECRET KEY")
```

2.  Call the method that you need:

Kotlin:
```kotlin
val member = client.getMe().get()
```

Java:
```java
final Member member = client.getMe().get()
```

All methods return a `CompletableFuture<T>`, so they can be handled asynchronously.

## Available methods

### Public OmniTrade API Endpoints
- getMarkets();
- getOrderBook();
- getDepth();
- getOrderMarket();
- getTrades();
- getK();
- getKWithPendingTrade();

### Private OmniTrade API Endpoints
- getMe();
- getDeposit();
- getDeposits();
- getDepositAddress();
- getOrder();
- getOrders();
- createOrder();
- createMultipleOrders();
- deleteOrder();
- clearAllOrders();
- clearBottomOrder();
- getMyTrades();

## TODO

- Assure that all methods are working as intended and returning the proper model;
- Add tests for all methods, following the example of the ones that already exist, using Spek;
- Configure and setup Travis CI;
- Configure the remote maven repository to publish the lib using the maven-publish plugin;
- Create Javadoc for the lib methods, explaining which endpoints it targets and what information it delivers;
- Refactor the Client, so it will not become a one file monster.
