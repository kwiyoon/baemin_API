#!/bin/bash

# JAR 파일명을 deploy.log에 기록.
BUILD_JAR=$(ls /home/ec2-user/zzoni/build/libs/*.jar | grep -nv 'plain')
JAR_NAME=$(basename $BUILD_JAR)
echo "> build 파일명: $JAR_NAME" >> /home/ec2-user/zzoni/deploy.log

# 빌드된 JAR 파일을 /home/ec2-user/ 디렉토리로 복사
echo "> build 파일 복사" >> /home/ec2-user/zzoni/deploy.log
DEPLOY_PATH=/home/ec2-user/zzoni/
cp $BUILD_JAR $DEPLOY_PATH

echo "> 현재 실행중인 애플리케이션 pid 확인" >> /home/ec2-user/zzoni/deploy.log
CURRENT_PID=$(pgrep -f $JAR_NAME)

# 현재 실행 중인 애플리케이션 종료
if [ -z $CURRENT_PID ]
then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다." >> /home/ec2-user/zzoni/deploy.log
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

# 새로운 JAR 파일 배포 및 실행
DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
echo "> DEPLOY_JAR 배포"    >> /home/ec2-user/zzoni/deploy.log
nohup java -Dspring.profiles.active=dev -jar $DEPLOY_JAR >> /home/ec2-user/zzoni/deploy.log >/home/ec2-user/zzoni/deploy_err.log &
