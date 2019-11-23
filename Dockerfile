FROM openjdk:8-jdk-alpine
ADD target/movie-database-0.0.1-SNAPSHOT.jar .
EXPOSE 8080
RUN  java -jar movie-database-0.0.1-SNAPSHOT.jar