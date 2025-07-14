
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
COPY application/pom.xml ./application/
COPY domain/pom.xml ./domain/
COPY infrastructure/pom.xml ./infrastructure/

RUN mvn dependency:go-offline

COPY . .

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

ARG JAR_FILE=application/target/*.jar

COPY --from=build /app/${JAR_FILE} app.jar

RUN addgroup --system appgroup && adduser --system --ingroup appgroup appuser

RUN chown -R appuser:appgroup /app


USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]