#!/bin/bash
echo "🚀 이전 버전의 애플리케이션을 중지합니다..."
cd /home/ubuntu/deployment

# docker-compose.yml 파일이 있으면 실행 중인 컨테이너를 중지하고 삭제합니다.
if [ -f docker-compose.yml ]; then
    docker-compose down --remove-orphans
    echo "✅ 기존 컨테이너가 중지 및 삭제되었습니다."
else
    echo "ℹ️ docker-compose.yml 파일이 없어 중지할 컨테이너가 없습니다."
fi