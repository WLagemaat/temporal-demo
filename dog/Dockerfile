FROM eclipse-temurin:21-jre-alpine
MAINTAINER wlagemaat

WORKDIR /app
COPY ./target/dog-*.jar /app/dog-app.jar

EXPOSE 8093

CMD ["java", "-jar", "dog-app.jar" ]

# docker build -t wlagemaat/dog-app:1.0 .