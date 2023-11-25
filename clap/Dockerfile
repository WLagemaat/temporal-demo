FROM eclipse-temurin:21-jre-alpine
MAINTAINER wlagemaat

WORKDIR /app
COPY ./target/clap-*.jar /app/clap-app.jar

EXPOSE 8081

CMD ["java", "-jar", "clap-app.jar" ]

# docker build -t wlagemaat/clap-app:1.0 .