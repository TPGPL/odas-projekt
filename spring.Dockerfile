FROM eclipse-temurin:17
WORKDIR /app
COPY ./target/odas-projekt-0.0.1-SNAPSHOT.jar /app
CMD ["java", "-jar", "odas-projekt-0.0.1-SNAPSHOT.jar"]