#!/bin/bash

REPOSITORY=/home/ec2-user/app/step1
PROJECT_NAME=freelec-springboot-webservice

cd $REPOSITORY/$PROJECT_NAME/

echo "> Git Pull"
git pull

echo "> start the project buile"
./gradlew build

echo "> move to step1 directory"
cd $REPOSITORY

echo "> copy the build file"
cp $REPOSITORY/$PROJECT_NAME/build/libs/*.jar $REPOSITORY/

echo "> check the pid of the currently running application"
CURRENT_PID=$(pgrep -fl $PROJECT_NAME | grep jar | awk '{print $1}')

echo "> The pid of the currently running application : $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
        echo "> No application is currently running, so it will not be terminated."
else
        echo "> kill -15 $CURRENT_PID"
        kill -15 $CURRENT_PID
        sleep 5
fi

echo "> Deploy new application"
JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)

echo "> JAR Name: $JAR_NAME"
nohup java -jar \
    -Dspring.config.location=classpath:/application.properties,classpath:/application-real.properties,/home/ec2-user/app/application-oauth.properties,/home/ec2-user/app/application-real-db.properties \
    -Dspring.profiles.active=real \
    $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &