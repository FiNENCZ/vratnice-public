package cz.dp.vratnice_public.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestController;

import cz.dp.vratnice_public.annotation.ValidReCaptcha;
import cz.dp.vratnice_public.dto.LokalitaDto;
import cz.dp.vratnice_public.exception.ReCaptchaException;
import cz.dp.vratnice_public.service.VratniceService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class LokalitaController extends BaseController  {
    final static Logger logger = LogManager.getLogger(LokalitaController.class);

    @Autowired
    private VratniceService vratniceService;

    @GetMapping("/lokalita/list")
    @ValidReCaptcha
    public ResponseEntity<List<LokalitaDto>> list(@RequestParam @Nullable String idZavod, @RequestHeader("reCAPTCHA-Token") String recaptchaToken) throws ReCaptchaException {
        return ResponseEntity.ok(vratniceService.seznamLokalit(idZavod));
    }
    

}
