FROM gradle:8.7.0-jdk21 AS dependencies

WORKDIR /app

COPY build.gradle .
COPY ../../settings.gradle .

RUN gradle build --no-daemon || return 0

FROM dependencies AS builder

COPY . .

RUN gradle build --no-daemon

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8081

CMD ["java", "-jar", "app.jar"]
