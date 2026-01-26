FROM eclipse-temurin:21-jdk
RUN apt-get update && apt-get install -y curl
COPY /build/libs/*.jar notification-service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "notification-service.jar"]