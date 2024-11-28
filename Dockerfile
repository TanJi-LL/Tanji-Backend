FROM openjdk:21

ENV TZ=Asia/Seoul

WORKDIR /app

COPY module-api/execute-app/build/libs/execute-app-0.0.1-SNAPSHOT.jar ./execute.jar

EXPOSE 8080
# 임시
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-Duser.timezone=Asia/Seoul", "-jar", "./execute.jar"]