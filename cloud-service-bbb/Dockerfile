FROM maven:3.5-jdk-8

LABEL "maintainer"="uvans5233@gmail.com"

#VOLUME /tmp
#ADD ./target/cloud-service-bbb.jar app.jar
#RUN bash -c 'touch /app.jar'

EXPOSE 7002

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
