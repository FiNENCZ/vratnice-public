package cz.diamo.vratnice_public.service;

import java.net.URI;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import cz.diamo.vratnice_public.configuration.ReCaptchaProperties;
import cz.diamo.vratnice_public.dto.ReCaptchaResponse;

@Service
public class ReCaptchaService {

    final static Logger logger = LogManager.getLogger(ReCaptchaService.class);

    @Autowired
    private ReCaptchaProperties reCaptchaProperties;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MessageSource messageSource;


    public ReCaptchaResponse verify(String response) {
        URI verifyURI = URI.create(String.format(reCaptchaProperties.getSite()+ "?secret=%s&response=%s",
            reCaptchaProperties.getSecret(), response));
        
        ReCaptchaResponse reCaptchaResponse = restTemplate.getForObject(verifyURI, ReCaptchaResponse.class);

   

        if (reCaptchaResponse == null) {
            ReCaptchaResponse newReCaptchaResponse = new ReCaptchaResponse();
            newReCaptchaResponse.setSuccess(false);
            newReCaptchaResponse.setErrorMessage(messageSource.getMessage("recaptcha.base-error", null, LocaleContextHolder.getLocale()));
            return newReCaptchaResponse;
        }

        logger.info(reCaptchaResponse);

        if (reCaptchaResponse.isSuccess() && (reCaptchaResponse.getScore() < reCaptchaProperties.getThreshold())) {
            reCaptchaResponse.setSuccess(false);
            reCaptchaResponse.setErrorMessage(messageSource.getMessage("recaptcha.low-score", null, LocaleContextHolder.getLocale()));
        }
        
        if (reCaptchaResponse.getErrorCodes() != null) {
            String firstError = reCaptchaResponse.getErrorCodes().stream().findFirst().orElse(null);

            if (firstError != null) {
                reCaptchaResponse.setErrorMessage(messageSource.getMessage("recaptcha." + firstError, null, LocaleContextHolder.getLocale()));
            }
        }

        return reCaptchaResponse;
    }
    
}
