package br.com.pointer.pointer_back.config;

import br.com.pointer.pointer_back.auth.TokenValidationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

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
}