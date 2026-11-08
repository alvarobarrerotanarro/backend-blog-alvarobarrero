FROM maven:4.0.0-rc-7-eclipse-temurin-27-alpine as builder

WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline # Evita invalidar cache docker

COPY src ./src
RUN mvn clean package -DskipTests
RUN ls -lah ./target

FROM eclipse-temurin:27_35-jre-ubi10-minimal

WORKDIR /app
COPY --from=builder /app/target/backend-blog-alvarobarrero-0.0.1-SNAPSHOT.jar ./app.jar
EXPOSE 3000 
CMD ["java", "-jar", "./app.jar"]