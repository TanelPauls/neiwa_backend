# ===== Build stage =====
FROM gradle:8.4-jdk21 AS build
ENV GRADLE_USER_HOME=/home/gradle/.gradle
WORKDIR /app

# Cache dependency layer
COPY build.gradle settings.gradle ./
COPY gradle gradle
RUN gradle dependencies --no-daemon --build-cache || true

# Copy source code
COPY . .
RUN gradle bootJar --no-daemon --build-cache

# ===== Runtime stage =====
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
