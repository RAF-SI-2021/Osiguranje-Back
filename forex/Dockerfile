# syntax=docker/dockerfile:1
FROM openjdk:17
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x ./mvnw
RUN microdnf install dos2unix
RUN dos2unix ./mvnw && microdnf remove dos2unix
RUN ./mvnw dependency:go-offline
COPY src ./src
CMD ["./mvnw", "spring-boot:run"]
