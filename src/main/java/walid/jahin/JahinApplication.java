package walid.jahin;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "walid.jahin")
@EnableJpaRepositories(basePackages = "walid.jahin.repository")
@EntityScan(basePackages = "walid.jahin.model")
public class JahinApplication {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(JahinApplication.class, args);

        if (ctx instanceof AnnotationConfigServletWebServerApplicationContext ac) {
            RequestMappingHandlerMapping mapping = ac.getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class);
            mapping.getHandlerMethods().forEach((k, v) -> {
                System.out.println("✅ Mapping: " + k + " → " + v);
            });
        }
    }

    @Bean
    public CommandLineRunner checkBeans(ApplicationContext ctx) {
        return args -> {
            System.out.println("📦 Registered Beans:");
            String[] beanNames = ctx.getBeanDefinitionNames();
            for (String name : beanNames) {
                if (name.toLowerCase().contains("artshop")) {
                    System.out.println("✅ Found: " + name);
                }
            }
        };
    }
}
