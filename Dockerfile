# Этап 1: Сборка
FROM maven:3.8.4-openjdk-17 AS build

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем файл pom.xml и загружаем зависимости
COPY pom.xml ./
RUN mvn dependency:go-offline

# Копируем исходный код и компилируем его
COPY src ./src
RUN mvn clean package -DskipTests

# Этап 2: Запуск
FROM openjdk:17-slim

# Копируем собранный .jar файл из этапа сборки
COPY --from=build /app/target/my-app.jar /my-app.jar

# Открываем порт для приложения (если нужно)
EXPOSE 8080

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "/my-app.jar"]

