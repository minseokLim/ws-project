FROM    openjdk:8-jdk-alpine
RUN     apk update && apk upgrade && apk add netcat-openbsd && apk add tzdata
RUN     ln -snf /usr/share/zoneinfo/Asia/Seoul /etc/localtime
RUN     mkdir -p /usr/local/batch-service
ADD     @project.build.finalName@.jar /usr/local/batch-service/
ADD     run.sh run.sh
RUN     chmod +x run.sh
CMD     ./run.sh