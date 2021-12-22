package com.qd.cjb.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

import static com.qd.cjb.api.controller.BatisController.USER_PATH;

/**
 * @version : 1.0
 * @auther : hjx
 * @Date : 2021/1/31
 * @Description : nuc.edu.hjx.config
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/META-INF/resources/")//映射swagger2
                .addResourceLocations("file:"+USER_PATH+"/");//映射本地静态资源
    }

    @Bean
    public Intercepator intercepator() {
        return new Intercepator();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(intercepator())
                .addPathPatterns("/user/queryuser")
                .addPathPatterns("/user/fans").addPathPatterns("/user/unfans")
                .addPathPatterns("/video/uploadVideo")
                .addPathPatterns("/video/like").addPathPatterns("/video/unlike");
    }
}
