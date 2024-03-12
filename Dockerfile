
FROM openjdk:8-jre-slim


MAINTAINER WDJ


ENV PARAMS=""


ENV TZ=PRC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone


ADD /chatbot-api-interfaces/target/chatbot-api.jar /chatbot-api.jar


ENTRYPOINT ["sh", "-c", "java -jar $JAVA_OPTS /chatbot-api.jar $PARAMS"]
