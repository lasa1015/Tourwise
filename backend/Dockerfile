FROM gradle:8.7.0-jdk21 AS dependencies

WORKDIR /app

COPY build.gradle .
COPY settings.gradle .

# 只拉依赖，不 build
RUN gradle dependencies --no-daemon

FROM dependencies AS builder

COPY . .

# 这里改成跳过 test
RUN gradle build -x test --no-daemon

FROM eclipse-temurin:21-jre

WORKDIR /app

# 安装需要的库
RUN apt-get update && apt-get install -y libgomp1

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

CMD ["java", "-Xmx1g", "-Xms512m", "-XX:+UseSerialGC", "-jar", "app.jar"]


