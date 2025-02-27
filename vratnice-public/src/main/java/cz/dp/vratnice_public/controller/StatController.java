package cz.dp.vratnice_public.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import cz.dp.vratnice_public.annotation.ValidReCaptcha;
import cz.dp.vratnice_public.dto.StatDto;
import cz.dp.vratnice_public.exception.ReCaptchaException;
import cz.dp.vratnice_public.service.VratniceService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class StatController extends BaseController {
    final static Logger logger = LogManager.getLogger(StatController.class);

    @Autowired
    private VratniceService vratniceService;

    @GetMapping("/stat/list")
    @ValidReCaptcha
    public ResponseEntity<List<StatDto>> list(HttpServletRequest request, @RequestHeader("reCAPTCHA-Token") String recaptchaToken) throws ReCaptchaException {
        return ResponseEntity.ok(vratniceService.seznamStatu());
    }

}
