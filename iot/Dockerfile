FROM eclipse-temurin:21-jre-alpine
MAINTAINER wlagemaat

WORKDIR /app
COPY ./target/iot-*.jar /app/iot-app.jar

CMD ["java", "-jar", "iot-app.jar" ]

# docker build -t wlagemaat/iot-app:1.0 .