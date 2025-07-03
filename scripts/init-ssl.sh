#!/bin/bash

echo "🔐 SSL 인증서 초기 설정 시작..."

DOMAIN="justfridge.p-e.kr"
EMAIL="kiroro0814@naver.com"

if [ ! "$(docker ps -q -f name=nginx-proxy)" ]; then
    echo "❌ nginx 컨테이너가 실행되지 않았습니다."
    exit 1
fi

if [ ! "$(docker ps -q -f name=certbot-ssl)" ]; then
    echo "❌ certbot 컨테이너가 실행되지 않았습니다."
    exit 1
fi

echo "📝 도메인 $DOMAIN에 대한 SSL 인증서 발급 중..."
docker-compose exec -T certbot certbot certonly \
    --webroot \
    --webroot-path=/var/www/certbot \
    --email $EMAIL \
    --agree-tos \
    --no-eff-email \
    --staging \
    -d $DOMAIN

if [ $? -eq 0 ]; then
    echo "✅ 테스트 인증서 발급 성공! 실제 인증서 발급 중..."
    docker-compose exec -T certbot certbot certonly \
        --webroot \
        --webroot-path=/var/www/certbot \
        --email $EMAIL \
        --agree-tos \
        --no-eff-email \
        --force-renewal \
        -d $DOMAIN

    docker-compose restart nginx
    echo "🎉 SSL 인증서 설정 완료!"
else
    echo "❌ SSL 인증서 발급 실패"
    exit 1
fi