FROM eclipse-temurin:21-jre-alpine
MAINTAINER wlagemaat

WORKDIR /app
COPY ./target/exe-*.jar /app/exe-app.jar

CMD ["java", "-jar", "exe-app.jar" ]

# docker build -t wlagemaat/exe-app:1.0 .