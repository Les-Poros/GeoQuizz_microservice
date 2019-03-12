FROM openjdk:8

RUN mkdir /app
WORKDIR /app

ADD ./target/backoffice-0.0.1-SNAPSHOT.jar /app/backoffice.jar

RUN mkdir /app/img

RUN bash -c 'touch /restservice.jar'

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/backoffice.jar"]