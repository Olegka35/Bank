FROM eclipse-temurin:21-jdk
RUN apt-get update && apt-get install -y curl
COPY /build/libs/*.jar config-server.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "config-server.jar"]