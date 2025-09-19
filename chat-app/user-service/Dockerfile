# Étape 1 : build
FROM maven:3.9.2-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Étape 2 : run
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/target/user-service-0.0.1-SNAPSHOT.jar ./user-service.jar

# Exposer le port configuré dans application.properties
EXPOSE 8081

# Lancer l'application
ENTRYPOINT ["java", "-jar", "user-service.jar"]
