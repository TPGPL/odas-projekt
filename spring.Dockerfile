FROM eclipse-temurin:17
WORKDIR /app
COPY ./target/odas-projekt-1.0.jar /app
CMD ["java", "-jar", "odas-projekt-1.0.jar"]