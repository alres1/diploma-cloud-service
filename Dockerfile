FROM openjdk:18-jdk-alpine3.15

EXPOSE 8080

ADD target/diploma-cloud-service-0.0.1-SNAPSHOT.jar cloud-service.jar

ENTRYPOINT ["java", "-jar", "cloud-service.jar"]