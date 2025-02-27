package cz.dp.vratnice_public.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import cz.dp.vratnice_public.annotation.ValidReCaptcha;
import cz.dp.vratnice_public.dto.SpolecnostDto;
import cz.dp.vratnice_public.exception.ReCaptchaException;
import cz.dp.vratnice_public.service.VratniceService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;



@RestController
public class SpolecnostController  extends BaseController  {
    final static Logger logger = LogManager.getLogger(RidicController.class);

    @Autowired
    private VratniceService vratniceService;

    @GetMapping("spolecnost/list")
    @ValidReCaptcha
    public ResponseEntity<List<SpolecnostDto>> list(@RequestHeader("reCAPTCHA-Token") String recaptchaToken) throws ReCaptchaException {
        return ResponseEntity.ok(vratniceService.seznamSpolecnosti());
    }
}
