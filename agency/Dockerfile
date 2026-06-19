FROM maven:3-eclipse-temurin-17-alpine AS build

WORKDIR /app

# Copy poms first for dependency caching
COPY pom.xml ./
COPY agency/pom.xml agency/pom.xml
RUN mvn dependency:go-offline -pl agency -am

# Copy sources
COPY agency/src agency/src

# Build
RUN mvn clean package -pl agency -am -DskipTests

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=build /app/agency/target/agency-*-exec.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
