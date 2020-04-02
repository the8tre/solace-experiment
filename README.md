# Solace experiments


## Build
```bash
mvn package
```

## Run
### Configure
Create a `src/main/resources/application-local.properties` file such as:

```javascript
solace.credentials.username=solace username
solace.credentials.password=solace password
solace.credentials.uri=solace uri (without user info)
```

## Run
```bash
mvn spring-boot:run
```
Application runs on http://localhost:8080

## Use

The application tries to bind 2 @JmsListener:
* Q/TestQueue
* T/TestTopic


Uri | Description | Example
--- | --- | ---
/jms/send/T/{topic}/{message} | Sends a message to the {topic} Topic | /jms/send/T/TestTopic/A%20Message
/jms/send/T/{topic}/{subTopic}/{message} | Sends a message to the {topic}/{subTopic} Topic | /jms/send/T/TestTopic/WithASubTopic/AnotherMessage
/jms/send/Q/{queue}/{message} | Sends a message to the {queue} Queue | /jms/send/Q/TestQueue/AQueuedMessage

