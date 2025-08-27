package com.example.wewha.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
public class WebConfig {

    @Bean
    public CommonsRequestLoggingFilter loggingFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true); // 요청 본문(payload)을 로그에 포함
        filter.setMaxPayloadLength(10000); // 로그에 표시할 본문의 최대 길이
        filter.setIncludeHeaders(true); // 헤더 정보를 로그에 포함
        filter.setAfterMessagePrefix("REQUEST DATA : ");
        return filter;
    }
}
