package walid.jahin.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // /uploads/** → 실제 uploads 폴더
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");

        // 정적 리소스(html, css, js)
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }
}
  