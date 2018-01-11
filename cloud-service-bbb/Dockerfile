FROM java:8
VOLUME /tmp
ADD ./target/cloud-service-bbb.jar app.jar
RUN bash -c 'touch /app.jar'
EXPOSE 9002
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
