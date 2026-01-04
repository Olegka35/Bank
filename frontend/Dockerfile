FROM eclipse-temurin:21-jdk-alpine AS build
COPY /build/libs/*.jar bank-frontend.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "bank-frontend.jar"]