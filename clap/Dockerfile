FROM eclipse-temurin:21-jre-alpine
MAINTAINER wlagemaat
EXPOSE 8081
COPY ./target/*.jar clap.jar
ENTRYPOINT ["java", "-jar", "clap.jar" ]