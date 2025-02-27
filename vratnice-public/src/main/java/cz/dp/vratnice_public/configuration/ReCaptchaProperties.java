package cz.dp.vratnice_public.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import lombok.Data;

@ConfigurationProperties(prefix = "google.recaptcha")
@Component
@Validated
@Data
public class ReCaptchaProperties {

    private String secret;

    private String site;

    private Float threshold;

}
