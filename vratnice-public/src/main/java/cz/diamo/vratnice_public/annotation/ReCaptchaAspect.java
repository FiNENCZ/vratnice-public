package cz.diamo.vratnice_public.annotation;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cz.diamo.vratnice_public.dto.ReCaptchaResponse;
import cz.diamo.vratnice_public.exception.BaseException;
import cz.diamo.vratnice_public.service.ReCaptchaService;

@Aspect
@Component
public class ReCaptchaAspect {


    @Autowired
    private ReCaptchaService recaptchaService;


    @Pointcut("@annotation(ValidReCaptcha)")
    public void validReCaptcha() {}

    @Before("validReCaptcha() && args(entity, recaptchaToken, ..)")
    public void validateReCaptcha(JoinPoint joinPoint, Object entity, String recaptchaToken) throws BaseException {
        ReCaptchaResponse reCaptchaResponse = recaptchaService.verify(recaptchaToken);
        if (!reCaptchaResponse.isSuccess()) {
            throw new BaseException(reCaptchaResponse.getErrorMessage());
        }
    }
}