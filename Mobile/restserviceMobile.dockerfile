FROM openjdk:8

RUN mkdir /app
WORKDIR /app

ADD ./target/mobile-0.0.1-SNAPSHOT.jar /app/mobile.jar
RUN bash -c 'touch /restservice.jar'


ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/mobile.jar"]