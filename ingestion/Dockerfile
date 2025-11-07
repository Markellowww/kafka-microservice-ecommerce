FROM maven:4.0.0-rc-4-amazoncorretto-21-al2023 AS build
WORKDIR /app

COPY pom.xml .
COPY ingestion/pom.xml ./ingestion/
COPY processing/pom.xml ./processing/
COPY fulfillment/pom.xml ./fulfillment/
COPY kafgen/pom.xml ./kafgen/

RUN mvn -f pom.xml clean package -DskipTests

COPY ./ingestion/src ./ingestion/src

RUN mvn -f ingestion/pom.xml clean package -DskipTests

FROM amazoncorretto:21-al2023
COPY --from=build /app/ingestion/target/ingestion-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]