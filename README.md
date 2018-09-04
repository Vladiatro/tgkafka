Telegram-Kafka
==============

A Spring Boot app which receives Protobuf messages from Kafka and sends them to Telegram.
Keeps chat ids using MongoDB.

Messages have the only field `content`, which is a string.
Default Kafka topic name is `telegram`, which is configurable in the `tgkafka.topic` property 
    of `telegram.properties`.

How to Run
----------

#### Create and Configure the Bot
* Proceed to [@BotFather](https://t.me/BotFather) and create a new bot.
* Duplicate `telegram.template.properties` where you will enter
 secret information:
  
      cp telegram.template.properties telegram.properties
  
  Note that `telegram.properties` is already in the `.gitignore` file.
 

* Insert bot's username and token as `tgkafka.bot.username` and `tgkafka.bot.token` parameters
  in the properties file.
  
* If you have Telegram blocked in your area, choose the appropriate proxy type
  in the `tgkafka.bot.proxy.type` property (e.g. SOCKS5) and enter proxy parameters
  in the lines below.

#### Build
    ./mvnw clean package
    docker-compose up

How to Use
----------
Go to your bot and start messaging with it by sending `/start` command.
It will register this chat in the database. Also you can add this bot 
to groups, and it will send messages there.

Now you have two simple options to send a message to Kafka:
* `/publish someText` command. You will see that the text came back:
  it had been put in and read from the broker.
* Special endpoint: `http://localhost:8080/sendMessage?text=someText`.
  This message should appear in Telegram chat.
  
If the system was unable to send the message (e.g. no chats registered, API errors occur),
it will put it back to Kafka with the same topic and wait for some time to read again.