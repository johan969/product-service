FROM eclipse-temurin:17-jre
WORKDIR /app
COPY target/product-service-1.0.0.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]