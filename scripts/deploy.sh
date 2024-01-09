#!/bin/bash
source ../.env

cd /home/ubuntu

# Blue 를 기준으로 현재 동작하는 컨테이너의 id를 저장한다.
EXIST_BLUE=$(docker ps -f "name=dev-blue" -f "status=running" -q)

# blue가 실행되지 않는다면 (green 실행 중)
if [ -z "$EXIST_BLUE" ]; then
  # blue 컨테이너 배포 로그 생성
  echo "blue container deploy: $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> "/home/${EC2_USERNAME}/server/deploy.log"
  sudo docker run -d -p "${BLUE_PORT}:${SPRING_BASIC_PORT}" --rm --name dev-blue "${DOCKER_USERNAME}/${DOCKER_REPOSITORY_NAME}:${DOCKER_BLUE_NAME}"
  STOP_CONTAINER="green"
  START_CONTAINER="blue"
else
  echo "green container deploy: $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> "/home/${EC2_USERNAME}/server/deploy.log"
  sudo docker run -d -p "${GREEN_PORT}:${SPRING_BASIC_PORT}" --rm --name dev-green "${DOCKER_USERNAME}/${DOCKER_REPOSITORY_NAME}:${DOCKER_GREEN_NAME}"
  STOP_CONTAINER="blue"
  START_CONTAINER="green"
fi

sleep 10

# 새로운 컨테이너가 제대로 떴는지 확인
EXIST_AFTER=$(docker ps -f "name=dev-$START_CONTAINER" -f "status=running" -q)
# 해당 컨테이너가 정상적으로 실행되는 경우
if [ -n "$EXIST_AFTER" ]; then
  # 이전 컨테이너 종료
  sudo docker stop "dev-$STOP_CONTAINER"
fi