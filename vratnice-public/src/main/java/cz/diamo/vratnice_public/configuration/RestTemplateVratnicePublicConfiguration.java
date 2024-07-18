package cz.diamo.vratnice_public.configuration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class RestTemplateVratnicePublicConfiguration {

    @Bean
    RestOperations restVratnice(RestTemplateBuilder restTemplateBuilder, AppProperties appProperties) {
        return restTemplateBuilder.uriTemplateHandler(new DefaultUriBuilderFactory(appProperties.getVratniceApiUrl()))
                .messageConverters(new MappingJackson2HttpMessageConverter()).build();
    }

}
