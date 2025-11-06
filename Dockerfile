# Dockerfile для корневого модуля (не для запуска, только для сборки зависимостей)
FROM maven:3.9.11-amazoncorretto-21 AS DEPENDENCIES
WORKDIR /opt/app
COPY pom.xml .
RUN mvn -B -e dependency:go-offline -N