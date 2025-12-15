package novaminds.gradproj.global.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import novaminds.gradproj.domain.member.service.security.jwt.JwtTokenProvider;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // websocket 연결 시 (CONNECT) 헤더의 jwt token 검증
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String jwtToken = accessor.getFirstNativeHeader("Authorization");

            if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
                jwtToken = jwtToken.substring(7);
            }

            log.info("WebSocket 연결 요청: token 존재 여부 = {}", jwtToken != null);

            // 유효성 검증 (예외 발생 시 연결 거부됨)
            // 실제 구현 시에는 예외 처리를 꼼꼼히 하거나, 유효하지 않으면 메시지를 null로 리턴하여 차단
            if (jwtToken == null || !jwtTokenProvider.validateToken(jwtToken)) {
                throw new IllegalArgumentException("유효하지 않은 웹소켓 연결 토큰입니다.");
            }
        }
        return message;
    }
}
