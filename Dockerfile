FROM openjdk:8-jdk-alpine

ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
COPY gpu.jar /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]