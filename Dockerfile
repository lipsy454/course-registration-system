FROM eclipse-temurin:21-jdk-jammy
EXPOSE 8081
COPY target/course-0.0.1-SNAPSHOT.jar spring-basic.jar
ENTRYPOINT ["java","-jar","spring-basic.jar"]