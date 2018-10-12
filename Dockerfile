FROM anapsix/alpine-java:8

RUN mkdir /application
RUN mkdir -p /application/log
COPY build/libs/notification-service-SNAPSHOT.jar /application/notification-service.jar
COPY application.yml /application/application.yml
WORKDIR /application

CMD ["java","-jar","notification-service.jar"]
EXPOSE 8017

