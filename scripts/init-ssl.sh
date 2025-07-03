#!/bin/bash

echo "🔐 SSL 인증서 초기 설정 시작..."

DOMAIN="justfridge.p-e.kr"
EMAIL="kiroro0814@naver.com"

# 인증서가 이미 있는지 확인
if docker compose exec -T nginx test -f /etc/letsencrypt/live/$DOMAIN/fullchain.pem 2>/dev/null; then
    echo "✅ SSL 인증서가 이미 존재합니다."
    echo "🎉 HTTPS 설정 완료!"
    exit 0
fi

cd /home/ubuntu/deployment

# 기존 HTTPS 설정을 백업합니다.
cp nginx/conf.d/websites/justfridge.p-e.kr.conf nginx/conf.d/websites/justfridge.p-e.kr.conf.backup

# 💡 수정된 부분: SSL 인증서 발급을 위해 초기 HTTP 설정을 복사합니다.
cp nginx/conf.d/websites-init/justfridge.p-e.kr-initial.conf nginx/conf.d/websites/justfridge.p-e.kr.conf

# Nginx와 Certbot을 초기 설정으로 실행합니다.
echo "🔄 인증서 발급을 위해 Nginx와 Certbot 컨테이너를 실행합니다..."
docker compose up -d nginx certbot
sleep 15 # 컨테이너가 완전히 시작될 때까지 충분히 대기합니다.

# Certbot으로 인증서 발급
echo "📝 도메인 $DOMAIN에 대한 SSL 인증서 발급을 시도합니다..."
docker compose exec -T certbot certbot certonly \
    --webroot \
    --webroot-path=/var/www/certbot \
    --email $EMAIL \
    --agree-tos \
    --no-eff-email \
    --non-interactive \
    -d $DOMAIN

# 인증서 발급 성공/실패에 따라 처리
if [ $? -eq 0 ]; then
    echo "✅ SSL 인증서 발급 성공!"
    # 백업해둔 최종 HTTPS 설정으로 복원합니다.
    cp nginx/conf.d/websites/justfridge.p-e.kr.conf.backup nginx/conf.d/websites/justfridge.p-e.kr.conf
    echo "🔄 Nginx를 HTTPS 모드로 재시작합니다..."
    docker compose restart nginx
    sleep 10
    echo "🎉 HTTPS 설정이 성공적으로 완료되었습니다!"
else
    echo "❌ SSL 인증서 발급에 실패했습니다."
    # 원래 설정으로 복구합니다.
    cp nginx/conf.d/websites/justfridge.p-e.kr.conf.backup nginx/conf.d/websites/justfridge.p-e.kr.conf
    docker compose restart nginx
    exit 1
fi