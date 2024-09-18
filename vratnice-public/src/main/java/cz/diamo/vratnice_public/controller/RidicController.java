package cz.diamo.vratnice_public.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cz.diamo.vratnice_public.annotation.ValidReCaptcha;
import cz.diamo.vratnice_public.dto.RidicDto;
import cz.diamo.vratnice_public.exception.BaseException;
import cz.diamo.vratnice_public.exception.ReCaptchaException;
import cz.diamo.vratnice_public.service.VratniceService;

@RestController
public class RidicController extends BaseController  {
    final static Logger logger = LogManager.getLogger(RidicController.class);

    @Autowired
    private VratniceService vratniceService;
/* 
    @GetMapping("/ridic/list")
    public ResponseEntity<List<RidicDto>> list() {
        return ResponseEntity.ok(vratniceService.seznam(idZavod));
    }*/

    @GetMapping("/ridic/list-by-cislo-op")
    @ValidReCaptcha
    public ResponseEntity<RidicDto> getRidicByCisloOp(@RequestParam String cisloOp, @RequestHeader("reCAPTCHA-Token") String recaptchaToken) throws ReCaptchaException, BaseException {
        RidicDto ridic = vratniceService.getRidicByCisloOp(cisloOp);
        if (ridic == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ridic);
    }



}
