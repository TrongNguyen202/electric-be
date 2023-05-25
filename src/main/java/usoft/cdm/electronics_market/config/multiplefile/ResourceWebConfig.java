package usoft.cdm.electronics_market.config.multiplefile;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class ResourceWebConfig implements WebMvcConfigurer {
    private final Environment environment;

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        String location = environment.getProperty("app.file.storage.mapping");

        registry.addResourceHandler("/static/**").addResourceLocations(location);
    }
}
