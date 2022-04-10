# syntax=docker/dockerfile:1
FROM openjdk:11
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN apt-get update && apt-get install -y dos2unix
RUN dos2unix ./mvnw && apt-get --purge remove -y dos2unix && rm -rf /var/lib/apt/lists/*
RUN ./mvnw dependency:go-offline
COPY src ./src
CMD ["./mvnw", "spring-boot:run"]

