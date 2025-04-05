package com.nexign.dmf.rss.rssvw.config;

import jakarta.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

//@Configuration
//@EnableWebMvc
//public class MVCConfig implements WebMvcConfigurer {
////    @Autowired
////    ServletContext srvl;
////    @Override
////    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//////        System.out.println("WebMvcConfigurer\n..");
////        registry.addResourceHandler("/resources/**")
////                .addResourceLocations("file:///E:/ansserg/jprojects/ms/web/html/")
////                .resourceChain(true)
////                .addResolver(new PathResourceResolver());
//////        System.out.println("Ok!");
////    }
//}
