# syntax=docker/dockerfile:1
FROM openjdk:17
LABEL org.opencontainers.image.source https://github.com/RAF-SI-2021/Osiguranje-Back

WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN microdnf install dos2unix
RUN dos2unix ./mvnw && microdnf remove dos2unix
RUN ./mvnw dependency:go-offline
COPY src ./src
CMD ["./mvnw", "spring-boot:run"]
