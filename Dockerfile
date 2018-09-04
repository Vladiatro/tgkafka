FROM openjdk:8-jdk-alpine
MAINTAINER admin@vladiator.biz
COPY target/tgkafka.jar tgkafka.jar
COPY telegram.properties telegram.properties
CMD ["java", "-jar", "tgkafka.jar", "--spring.config.location=telegram.properties"]
EXPOSE 8080