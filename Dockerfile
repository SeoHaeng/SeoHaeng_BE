FROM eclipse-temurin:17-jdk-jammy

COPY backend-0.0.1-SNAPSHOT.jar app.jar
COPY application.yml application.yml

ENTRYPOINT ["java","-jar","/app.jar"]
