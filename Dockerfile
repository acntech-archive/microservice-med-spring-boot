FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD web/target/employee-service.war employee-service.war
ENV JAVA_OPTS=""
ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /employee-service.war