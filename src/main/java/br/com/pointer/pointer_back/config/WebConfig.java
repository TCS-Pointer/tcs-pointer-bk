package br.com.pointer.pointer_back.config;

import br.com.pointer.pointer_back.auth.TokenValidationInterceptor;
import br.com.pointer.pointer_back.constant.SecurityConstants;
import br.com.pointer.pointer_back.interceptor.RequestLoggingInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final TokenValidationInterceptor tokenValidationInterceptor;
    private final RequestLoggingInterceptor requestLoggingInterceptor;

    public WebConfig(TokenValidationInterceptor tokenValidationInterceptor,
                    RequestLoggingInterceptor requestLoggingInterceptor) {
        this.tokenValidationInterceptor = tokenValidationInterceptor;
        this.requestLoggingInterceptor = requestLoggingInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestLoggingInterceptor)
                .addPathPatterns("/**")
                .order(1);
        registry.addInterceptor(tokenValidationInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(SecurityConstants.EXCLUDED_PATHS.toArray(new String[0]))
                .order(2);
    }
}