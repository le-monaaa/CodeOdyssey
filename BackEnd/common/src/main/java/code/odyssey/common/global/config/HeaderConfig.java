package code.odyssey.common.global.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

public class HeaderConfig {
    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            template.header("Content-Type", "application/json;charset=utf-8");
        };
    }
}
