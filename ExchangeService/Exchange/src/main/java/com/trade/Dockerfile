FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY ExchangeService/Exchange/pom.xml .
RUN mvn dependency:go-offline -q
COPY ExchangeService/Exchange/src ./src
RUN mvn package -DskipTests -q

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "app.jar"]