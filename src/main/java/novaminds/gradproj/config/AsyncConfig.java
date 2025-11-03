package novaminds.gradproj.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * 비동기 작업을 위한 스레드 풀 설정
 * 주로 이메일 발송과 같은 I/O 작업에 사용
 */
@Configuration
@EnableAsync
public class AsyncConfig {

    /**
     * 이메일 발송 등 비동기 작업을 위한 스레드 풀 Executor
     *
     * @return ThreadPoolTaskExecutor 비동기 작업 실행을 위한 Executor
     */
    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // 코어 스레드 수: 기본적으로 유지할 스레드 개수
        executor.setCorePoolSize(2);

        // 최대 스레드 수: 부하가 높을 때 늘어날 수 있는 최대 스레드 개수
        executor.setMaxPoolSize(5);

        // 큐 용량: 대기 중인 작업을 저장할 큐의 크기
        executor.setQueueCapacity(100);

        // 스레드 이름 접두사
        executor.setThreadNamePrefix("async-email-");

        // 애플리케이션 종료 시 실행 중인 작업 완료 대기
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);

        // 초기화
        executor.initialize();

        return executor;
    }
}