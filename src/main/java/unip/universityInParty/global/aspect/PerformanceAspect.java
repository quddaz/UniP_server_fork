package unip.universityInParty.global.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PerformanceAspect {

    private static final Logger logger = LoggerFactory.getLogger(PerformanceAspect.class);

    // 'unip.universityInParty' 패키지 아래의 모든 컨트롤러의 모든 메서드에 적용
    @Around("execution(* unip.universityInParty..controller..*(..))")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Object proceed = joinPoint.proceed(); // 타겟 메서드 실행

        long executionTime = System.currentTimeMillis() - start;

        logger.info("Method {} executed in {} ms", joinPoint.getSignature(), executionTime);
        return proceed;
    }
}