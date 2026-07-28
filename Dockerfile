# ===== Giai doan 1: build bang Maven =====
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
# Cache dependency truoc de build sau nhanh hon
COPY pom.xml .
RUN mvn -q -e -B dependency:go-offline
COPY src ./src
RUN mvn -q -e -B clean package -DskipTests

# ===== Giai doan 2: chay bang JRE gon nhe =====
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
# Cloud (Railway/Render) se dat bien PORT; ung dung da doc ${PORT} trong application.properties
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
