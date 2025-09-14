# ---------- build stage ----------
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q -B -DskipTests dependency:go-offline
COPY src ./src
RUN mvn -q -B -DskipTests package
RUN set -eux; JAR=$(ls target/*.jar | head -n1); mv "$JAR" /app/app.jar

# ---------- runtime stage ----------
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75.0"
RUN addgroup -S app && adduser -S app -G app
USER app
COPY --from=build /app/app.jar /app/app.jar

ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]