package com.irum.orderservice.global.infrastructure.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.HttpHeaders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

//RequestInterceptor : 요정을 가로채서 header, body등을 변경할 수 있음
@Slf4j
@Component
public class FeignClientInterceptor implements RequestInterceptor {

    // feign 이 외부로 http 요청을 보내기 직전에 실행됨
    @Override
    public void apply(RequestTemplate template) {
        //Spring MVC가 현재 처리 중인 HTTP 요청의 request context를 꺼내옴
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();

            // Gateway가 넣어준 X-Member-Id 헤더 전달
            String memberIdHeader = request.getHeader("X-Member-Id");
            if (memberIdHeader != null) {
                log.debug("Feign Propagation: X-Member-Id = {}", memberIdHeader);
                //Feign 요청에 x-member-id를 추가 
                template.header("X-Member-Id", memberIdHeader);
            }
        }
    }
}
