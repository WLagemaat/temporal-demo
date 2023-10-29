FROM eclipse-temurin:21_35-jre-ubi9-minimal
MAINTAINER wlagemaat.nl
COPY target/mass-1.0.jar mass-1.0.jar
EXPOSE 8082
ENV APP_TEMPORAL_HOST localhost:7233
ENTRYPOINT ["java","-jar","/mass-1.0.jar"]