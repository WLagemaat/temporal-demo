FROM eclipse-temurin:21-jre-alpine
MAINTAINER wlagemaat
EXPOSE 8083
COPY ./target/*.jar dog.jar
ENTRYPOINT ["java", "-jar", "dog.jar" ]