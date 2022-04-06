# syntax=docker/dockerfile:1
FROM openjdk:16-oracle
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN microdnf install dos2unix
RUN dos2unix ./mvnw && microdnf remove dos2unix
RUN ./mvnw dependency:go-offline
COPY src ./src
RUN mkdir futures-api
COPY config.properties ./futures-api/config.properties
COPY data ./futures-api/data
CMD ["./mvnw", "spring-boot:run"]
