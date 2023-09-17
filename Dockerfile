# Stage 1: Build the application
FROM ubuntu:latest AS build
RUN apt-get update && apt-get install -y openjdk-17-jdk
WORKDIR /app
COPY . .
RUN ./gradlew bootJar --no-daemon

# Stage 2: Create the final image
FROM openjdk:17-jdk-slim
EXPOSE 8080
WORKDIR /app
COPY --from=build /app/build/libs/react-flutter-nativeAndroid-1.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
