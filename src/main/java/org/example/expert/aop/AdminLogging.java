package org.example.expert.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AdminLogging {

    //- 요청한 사용자의 ID
    //- API 요청 시각
    //- API 요청 URL

    private final HttpServletRequest request;

    @Before("execution(* org.example.expert.domain.comment.controller.deleteComment*(..)) || " +
            "execution(* org.example.expert.domain.user.controller.changeUserRole*(..))")
    public void logAdminAccess(JoinPoint joinPoint) {



        // 요청 시각
        LocalDateTime requestTime = LocalDateTime.now();

        // 요청 URL
        String requestUrl = request.getRequestURL().toString();


        log.info("request time={}", requestTime);

    }

}
