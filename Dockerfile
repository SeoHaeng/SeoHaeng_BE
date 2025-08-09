FROM openjdk:17-jdk-slim

COPY backend-0.0.1-SNAPSHOT.jar app.jar
COPY application.yml application.yml

ENTRYPOINT ["java","-jar","/app.jar"]
