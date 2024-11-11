package unip.universityInParty.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class AsyncConfiguration implements AsyncConfigurer {
    @Bean(name = "emailAsyncExecutor")
    public Executor getAsyncExecutor() {
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5); // 기본 스레드 풀 크기(최소 5개)
        executor.setMaxPoolSize(25); // 최대 스레드 풀 크기
        executor.setQueueCapacity(30); // 작업 큐의 크기

        // 작업 거부 전략 : 스레드 풀에 여유가 없을 경우, 호출한 스레드가 실행
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setAwaitTerminationSeconds(60); // 스레드 종료 시 대기 시간 설정
        executor.setThreadNamePrefix("AsyncMail Thread-"); // 생성 스레드 이름 접두사
        return executor;
    }
}