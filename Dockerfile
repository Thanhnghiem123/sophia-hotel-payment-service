# -------- Base Image --------
FROM eclipse-temurin:17-jdk-alpine AS base
WORKDIR /app

# Copy Gradle wrapper & configs
COPY ./gradlew gradlew
COPY ./gradle gradle
COPY ./build.gradle settings.gradle ./
RUN chmod +x gradlew

# Cache dependencies
RUN ./gradlew dependencies --no-daemon

# -------- Build Stage --------
FROM base AS builder
# Copy the rest of the source code
COPY src ./src
# gradlew should already be executable and present from the base stage
# If build.gradle or settings.gradle changed, they are already updated from 'base'
RUN ./gradlew build --no-daemon -x test

# -------- Runtime Stage --------
FROM eclipse-temurin:17-jdk-alpine AS runtime
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8084
CMD ["java", "-jar", "app.jar"]
