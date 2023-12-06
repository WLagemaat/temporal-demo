FROM eclipse-temurin:21-jre-alpine
MAINTAINER wlagemaat

WORKDIR /app
COPY ./target/rdw-*.jar /app/rdw-app.jar

CMD ["java", "-jar", "rdw-app.jar" ]

# docker build -t wlagemaat/rdw-app:1.0 .