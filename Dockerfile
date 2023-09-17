# Etapa 1: Construir a aplicação
FROM windows11system/welcome-to-docker AS build
RUN apt-get update && apt-get install -y openjdk-17-jdk
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew bootJar --no-daemon

# Etapa 2: Criar a imagem final
FROM openjdk:17-jdk-slim
EXPOSE 8080
WORKDIR /app
COPY --from=build /app/build/libs/react-flutter-nativeAndroid-1.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
