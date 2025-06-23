package br.com.pointer.pointer_back.config;

import br.com.pointer.pointer_back.auth.TokenValidationInterceptor;
<<<<<<< HEAD
import br.com.pointer.pointer_back.constant.SecurityConstants;
import br.com.pointer.pointer_back.interceptor.RequestLoggingInterceptor;
=======
import org.springframework.beans.factory.annotation.Autowired;
>>>>>>> 3c46f92a3eab74bba1b2fc31a3bd29ad2f03f3ce
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

<<<<<<< HEAD
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
=======
    @Autowired
    private TokenValidationInterceptor tokenValidationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenValidationInterceptor)
                .addPathPatterns("/**") // Aplica a todas as rotas
                .excludePathPatterns( // Exclui rotas públicas
                        "/token",
                        "/usuarios/criar",
                        "/usuarios/atualizar-senha",
                        "/error");
    }
>>>>>>> 3c46f92a3eab74bba1b2fc31a3bd29ad2f03f3ce
}