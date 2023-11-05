#FROM eclipse-temurin:21.0.1_12-jdk-jammy as builder

#
#WORKDIR /tmp
#RUN rm -rf ./*
#COPY .mvn/ .mvn
#COPY mvnw pom.xml ./
##RUN ./mvnw dependency:go-offline
#COPY ./src ./src
#RUN ./mvnw package -DskipTests -nsu -o

FROM eclipse-temurin:21-jre-alpine
MAINTAINER wlagemaat
EXPOSE 8082
COPY ./target/*.jar /opt/app/*.jar
ENTRYPOINT ["java", "-jar", "/opt/app/*.jar" ]