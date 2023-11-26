FROM eclipse-temurin:21-jre-alpine
MAINTAINER wlagemaat

WORKDIR /app
COPY ./target/mass-*.jar /app/mass-app.jar

EXPOSE 8082

CMD ["java", "-jar", "mass-app.jar" ]

# docker build -t wlagemaat/mass-app:1.0 .