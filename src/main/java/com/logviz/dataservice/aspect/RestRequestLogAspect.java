package com.logViz.dataservice.aspect;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class RestRequestLogAspect {

    @Getter
    @Setter
    private boolean restReqLoggingEnabled;

    @Around("@annotation(com.logViz.dataservice.aspect.LogRestApiFlow)")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        if(restReqLoggingEnabled) {
            log.info("Request Received. Module:{}, API:{}, Req:{}",
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
        }

        Object result = joinPoint.proceed();

        stopWatch.stop();
        if(restReqLoggingEnabled) {
            log.info("Request Returned: Module:{}, API:{}, RespTm:{} ms, isSuccess:{}, errorType:{}, Response:{}",
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), stopWatch.getTotalTimeMillis(), true, "No Error", result);
        }

        return result;
    }
}
