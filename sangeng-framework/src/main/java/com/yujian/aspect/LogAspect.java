package com.yujian.aspect;

import com.yujian.annotation.SystemLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
@Aspect
@Slf4j
public class LogAspect {

    @Pointcut("@annotation(com.yujian.annotation.SystemLog)")
    public void pt(){

    }
    
    @Around("pt()")
    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {

        Object ret;
        try {
            handleBefore(joinPoint);
            ret = joinPoint.proceed();
            handleAfter();
        } finally {
            //结束后换行
            log.info("=======End======" + System.lineSeparator());
            
        }

        return ret;
    }

    private void handleAfter() {
    }

    private void handleBefore(ProceedingJoinPoint joinPoint) {

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        //获取被增强方法上的注解对象
        SystemLog systemLog = getSytemLog(joinPoint);


        log.info("=======Start=======");
        // 打印请求 URL
        log.info("URL            : {}",request.getRequestURL());
        // 打印描述信息
        log.info("BusinessName   : {}", "");
        // 打印 Http method
        log.info("HTTP Method    : {}","" );
        // 打印调用 controller 的全路径以及执行方法
        log.info("Class Method   : {}.{}","" );
        // 打印请求的 IP
        log.info("IP             : {}","");
        // 打印请求入参
        log.info("Request Args   : {}","");
    }

    private SystemLog getSytemLog(ProceedingJoinPoint joinPoint) {
        return null;

    }


}
