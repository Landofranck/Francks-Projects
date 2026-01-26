FROM eclipse-temurin:22-jdk AS build
WORKDIR /build

COPY . .
RUN rm -rf target
RUN ./mvnw package -Dquarkus.package.type=uber-jar

FROM eclipse-temurin:22-jre
WORKDIR /app
COPY --from=build /build/target/*-runner.jar /app/app.jar
CMD ["java", "-jar", "/app/app.jar"]
