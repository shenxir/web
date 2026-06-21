package org.example.lostfound.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 使用项目根目录的绝对路径
        String projectDir = System.getProperty("user.dir");
        String uploadPath = projectDir + File.separator + "picture" + File.separator;

        registry.addResourceHandler("/picture/**")
                .addResourceLocations("file:" + uploadPath);
    }
}
