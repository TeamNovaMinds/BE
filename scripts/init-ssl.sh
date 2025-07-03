#!/bin/bash

echo "🔐 SSL 인증서 초기 설정 시작..."

DOMAIN="justfridge.p-e.kr"
EMAIL="kiroro0814@naver.com"  # ✅ 실제 이메일로 변경 필요

# ✅ 인증서가 이미 있는지 확인
if [ -f "/home/ubuntu/deployment/certbot-certs/live/$DOMAIN/fullchain.pem" ]; then
    echo "✅ SSL 인증서가 이미 존재합니다."

    # HTTPS 설정 적용
    cd /home/ubuntu/deployment
    rm -f nginx/conf.d/websites/justfridge.p-e.kr-initial.conf
    docker compose restart nginx

    echo "🎉 HTTPS 설정 완료!"
    exit 0
fi

# ✅ 초기 HTTP 전용 설정으로 시작
cd /home/ubuntu/deployment
cp nginx/conf.d/websites/justfridge.p-e.kr-initial.conf nginx/conf.d/websites/justfridge.p-e.kr.conf.tmp
mv nginx/conf.d/websites/justfridge.p-e.kr.conf nginx/conf.d/websites/justfridge.p-e.kr.conf.backup
mv nginx/conf.d/websites/justfridge.p-e.kr.conf.tmp nginx/conf.d/websites/justfridge.p-e.kr.conf

# ✅ Nginx 재시작 (HTTP 전용 모드)
docker compose restart nginx
sleep 10

# ✅ Certbot으로 인증서 발급
echo "📝 도메인 $DOMAIN에 대한 SSL 인증서 발급 중..."
docker compose exec -T certbot certbot certonly \
    --webroot \
    --webroot-path=/var/www/certbot \
    --email $EMAIL \
    --agree-tos \
    --no-eff-email \
    --force-renewal \
    -d $DOMAIN

if [ $? -eq 0 ]; then
    echo "✅ SSL 인증서 발급 성공!"

    # ✅ HTTPS 포함 설정으로 전환
    mv nginx/conf.d/websites/justfridge.p-e.kr.conf.backup nginx/conf.d/websites/justfridge.p-e.kr.conf
    rm -f nginx/conf.d/websites/justfridge.p-e.kr-initial.conf

    # ✅ Nginx 재시작 (HTTPS 모드)
    docker compose restart nginx

    echo "🎉 HTTPS 설정 완료!"
else
    echo "❌ SSL 인증서 발급 실패"

    # ✅ 원래 설정으로 복구
    mv nginx/conf.d/websites/justfridge.p-e.kr.conf.backup nginx/conf.d/websites/justfridge.p-e.kr.conf
    docker compose restart nginx

    exit 1
fi