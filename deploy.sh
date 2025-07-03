#!/bin/bash
echo "🚀 배포 시작..."

if [ ! -f .env ]; then
    echo "❌ .env 파일이 없습니다!"
    exit 1
fi

# ✅ 실제 ECR 로그인
echo "🔑 ECR 로그인 중..."
aws ecr get-login-password --region ap-northeast-2 | docker login --username AWS --password-stdin 600627338836.dkr.ecr.ap-northeast-2.amazonaws.com

echo "🔄 서비스 중지 중..."
docker compose down

echo "📥 최신 이미지 pull 중..."
docker compose pull

echo "🚀 서비스 시작 중..."
docker compose up -d

echo "✅ 배포 완료!"
echo "🌐 http://justfridge.p-e.kr"
echo "🔐 https://justfridge.p-e.kr (SSL 설정 후)"