# 第一阶段：拉依赖
FROM gradle:8.7.0-jdk21 AS dependencies

WORKDIR /app

COPY build.gradle settings.gradle ./
COPY gradle ./gradle

RUN gradle dependencies --no-daemon

# 第二阶段：正式构建
FROM dependencies AS builder

COPY src ./src

RUN gradle build --no-daemon

# 第三阶段：生成最终运行镜像
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8081

# 第一阶段：拉依赖
FROM gradle:8.7.0-jdk21 AS dependencies

WORKDIR /app

COPY build.gradle settings.gradle ./
COPY gradle ./gradle

RUN gradle dependencies --no-daemon

# 第二阶段：正式构建
FROM dependencies AS builder

COPY src ./src

RUN gradle build --no-daemon

# 第三阶段：生成最终运行镜像
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8081

CMD ["java", "-Xmx200m", "-Xms100m", "-XX:+UseSerialGC", "-jar", "app.jar"]
