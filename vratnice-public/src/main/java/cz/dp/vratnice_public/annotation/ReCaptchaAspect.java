package cz.dp.vratnice_public.annotation;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cz.dp.vratnice_public.dto.ReCaptchaResponse;
import cz.dp.vratnice_public.exception.ReCaptchaException;
import cz.dp.vratnice_public.service.ReCaptchaService;

@Aspect
@Component
public class ReCaptchaAspect {


    @Autowired
    private ReCaptchaService recaptchaService;


    @Pointcut("@annotation(ValidReCaptcha)")
    public void validReCaptcha() {}

    @Before("validReCaptcha() && args(entity, recaptchaToken, ..)")
    public void validateReCaptcha(JoinPoint joinPoint, Object entity, String recaptchaToken) throws ReCaptchaException {
        ReCaptchaResponse reCaptchaResponse = recaptchaService.verify(recaptchaToken);
        if (!reCaptchaResponse.isSuccess()) {
            throw new ReCaptchaException(reCaptchaResponse.getErrorMessage());
        }
    }
}