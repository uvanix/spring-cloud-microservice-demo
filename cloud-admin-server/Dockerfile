FROM maven:3.5-jdk-8

LABEL "maintainer"="uvans5233@gmail.com"

#VOLUME /tmp
#ADD ./target/cloud-admin-server.jar app.jar
#RUN bash -c 'touch /app.jar'

EXPOSE 9410

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
