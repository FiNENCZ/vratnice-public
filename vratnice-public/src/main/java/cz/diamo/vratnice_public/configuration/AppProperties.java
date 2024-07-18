package cz.diamo.vratnice_public.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import lombok.Data;

@ConfigurationProperties(prefix = "app")
@Component
@Validated
@Data
public class AppProperties {

    private String vratniceApiUrl;

}
