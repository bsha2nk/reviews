FROM eclipse-temurin:17-jdk-alpine
COPY target/reviews-0.0.1-SNAPSHOT.jar reviews.jar
ENTRYPOINT ["java", "-jar", "reviews.jar"]