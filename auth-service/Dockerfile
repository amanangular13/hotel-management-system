# ---- Build stage ----
FROM maven:3.9.4-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copy pom.xml and download dependencies first (for caching)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source and build
COPY src ./src
RUN mvn clean package -DskipTests

# ---- Runtime stage ----
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Railway will inject PORT, default to 8080
ENV PORT=8081
EXPOSE 8081

ENTRYPOINT ["java", "-Dserver.port=${PORT}", "-jar", "app.jar"]