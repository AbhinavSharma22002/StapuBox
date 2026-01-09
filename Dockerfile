# Stage 1: Build using Java 25
FROM eclipse-temurin:25-jdk AS build
WORKDIR /app

# Install Maven manually in the Java 25 environment
RUN apt-get update && apt-get install -y maven

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
# Build the JAR
RUN mvn clean package -DskipTests

# Stage 2: Runtime using Java 25
FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]