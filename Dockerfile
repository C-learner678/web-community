FROM openjdk:8
VOLUME /tmp
ADD web-community-0.1.jar web-community-0.1.jar
ENTRYPOINT ["java","-jar","/web-community-0.1.jar"]
EXPOSE 8888