# Use a lightweight OpenJDK 21 image
FROM eclipse-temurin:21-jdk-jammy

# Set the working directory
WORKDIR /app

# Copy the built jar file
COPY build/libs/*.jar app.jar

# Expose the port the app runs on
EXPOSE 8081

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"] 