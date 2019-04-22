# Руководство

## Сборка

Сборка командой:
* mvnw clean package

## Тесты

Запуск тестов командой:
* mvn test

## Запуск

После сборки приложения, из папки приложения выполнить:
* java -jar target\app.jar

Для переопределения адреса сервиса other, можно воспользоваться вариантами:
* java -jar target\app.jar --other.host="http://other-host"
* java -jar target\app.jar --other.timeout=5
* java -jar target\app.jar --spring.config.location=classpath:/another-location.properties
