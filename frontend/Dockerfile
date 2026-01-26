FROM eclipse-temurin:21-jdk
RUN apt-get update && apt-get install -y curl
COPY /build/libs/*.jar bank-frontend.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "bank-frontend.jar"]