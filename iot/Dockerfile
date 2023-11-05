FROM eclipse-temurin:21-jre-alpine
MAINTAINER wlagemaat
EXPOSE 8091
COPY ./target/*.jar iot.jar
ENTRYPOINT ["java", "-jar", "iot.jar" ]