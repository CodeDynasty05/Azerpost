FROM gradle:8.6-jdk21 AS build
WORKDIR /workspace

COPY gradlew gradlew.bat build.gradle settings.gradle ./
COPY gradle ./gradle
COPY src ./src
RUN chmod +x gradlew \
 && ./gradlew clean bootJar --no-daemon

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

COPY --from=build /workspace/build/libs/*.jar app.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]