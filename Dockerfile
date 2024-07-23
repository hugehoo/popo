# Use an official OpenJDK runtime as a parent image, specifying the AMD64 architecture
FROM --platform=linux/amd64 openjdk:17-jdk

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file into the container
COPY build/libs/clova-*.jar app.jar

# Expose port 8080
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
