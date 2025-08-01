FROM eclipse-temurin:21-jre
VOLUME /tmp
COPY target/channelapi-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]