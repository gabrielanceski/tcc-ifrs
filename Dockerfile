FROM maven:3.9.9-eclipse-temurin-21 AS build

COPY src /app/src
COPY pom.xml /app

WORKDIR /app

RUN mvn clean install package -DskipTests

FROM eclipse-temurin:21-jre-alpine

ENV SERVER_PORT=${SERVER_PORT}
ARG JAR_FILE=/app/target/*.jar

EXPOSE ${SERVER_PORT}

WORKDIR /app

COPY --from=build ${JAR_FILE} app.jar

CMD ["java", "-jar", "app.jar"]