package com.nexign.dmf.rss.ocsdb.config;

import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataSize;
import org.springframework.web.WebApplicationInitializer;

@Component
public class AppInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext sc) throws ServletException {
//        AnnotationConfigWebApplicationContext root = new AnnotationConfigWebApplicationContext();
//        root.register((SecurityConfig.class));
//        sc.addListener((new ContextLoaderListener((root))));
//        sc.addFilter("securityFilter",new DelegatingFilterProxy("springSecurityFilterChain"));
    }

    @Bean
        //Метод для установления размера файла и размера запроса
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();

        factory.setMaxFileSize(DataSize.parse("10MB")); //Установить значение в 10MB
        factory.setMaxRequestSize(DataSize.parse("10MB")); //Установить значение в 10MB

        return factory.createMultipartConfig();
    }
}
