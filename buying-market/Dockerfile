FROM openjdk:11-jdk-slim as build
LABEL org.opencontainers.image.source https://github.com/RAF-SI-2021/Osiguranje-Back

WORKDIR /app
COPY pom.xml ./pom.xml
COPY src ./src

RUN apt-get update && apt-get install -y dos2unix
RUN dos2unix ./mvnw && apt-get --purge remove -y dos2unix && rm -rf /var/lib/apt/lists/*

RUN ./mvnw clean package

FROM openjdk:11-jre-slim as run
COPY --from=build /app/target/buying-market-0.0.1-SNAPSHOT.jar buying-market-0.0.1-SNAPSHOT.jar

CMD ["java", "-jar", "buying-market-0.0.1-SNAPSHOT.jar"]
