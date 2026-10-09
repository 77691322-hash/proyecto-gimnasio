package com.gimnasio;

import com.gimnasio.controller.SesionInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class GymApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(GymApplication.class, args);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SesionInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/login", "/logout", "/publicidad", "/contacto",
                        "/paquetes/catalogo", "/paquetes/solicitar",
                        "/css/**", "/js/**", "/img/**", "/error");
    }
}
