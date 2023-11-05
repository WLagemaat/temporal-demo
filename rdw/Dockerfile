FROM eclipse-temurin:21-jre-alpine
MAINTAINER wlagemaat
EXPOSE 8090
COPY ./target/*.jar rdw.jar
ENTRYPOINT ["java", "-jar", "rdw.jar" ]