FROM ubuntu:latest AS build
RUN apt-get update
RUN apt-get install openjdk-17-jdk -y
COPY . .
RUN ./gradlew bootjar --no-deamon

FROM openjdk:17-jdk-slim
EXPOSE 8080
COPY --from=build /build/libs/react-flutter-nativeAndroid-1.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]