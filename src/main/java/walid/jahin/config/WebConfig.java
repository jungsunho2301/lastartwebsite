package walid.jahin.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {

                // ✅ 업로드 경로 (artwork, artshop 포함)
                registry.addResourceHandler("/uploads/**")
                                .addResourceLocations("file:" + System.getProperty("user.dir") + "/uploads/");

                // ✅ 정적 리소스
                registry.addResourceHandler("/css/**")
                                .addResourceLocations("classpath:/static/css/");
                registry.addResourceHandler("/js/**")
                                .addResourceLocations("classpath:/static/js/");
        }
}
