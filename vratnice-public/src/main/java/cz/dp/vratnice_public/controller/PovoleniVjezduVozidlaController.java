package cz.dp.vratnice_public.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import cz.dp.vratnice_public.annotation.ValidReCaptcha;
import cz.dp.vratnice_public.dto.PovoleniVjezduVozidlaDto;
import cz.dp.vratnice_public.dto.RzTypVozidlaDto;
import cz.dp.vratnice_public.exception.BaseException;
import cz.dp.vratnice_public.exception.ReCaptchaException;
import cz.dp.vratnice_public.service.PovoleniVjezduVozidlaService;
import jakarta.validation.Valid;

@RestController
public class PovoleniVjezduVozidlaController extends BaseController{

    final static Logger logger = LogManager.getLogger(PovoleniVjezduVozidlaController.class);


    @Autowired
    private PovoleniVjezduVozidlaService povoleniVjezduVozidlaService;

    @PostMapping(value = "/povoleni-vjezdu-vozidla/povoleni-csv", consumes = {"multipart/form-data"})
    @ValidReCaptcha
    public ResponseEntity<Set<PovoleniVjezduVozidlaDto>> povoleniCsv(@RequestPart("file")MultipartFile file, 
                @RequestHeader("reCAPTCHA-Token") String recaptchaToken) throws IOException, ParseException, BaseException, ReCaptchaException{
        try {
            return ResponseEntity.ok(povoleniVjezduVozidlaService.processPovoleniCsvData(file));
        } 
        catch (ResponseStatusException re) {
            logger.error(re);
            throw re;        
        } catch (Exception e) {
            logger.error(e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
        }
    }


    @PostMapping(value = "/povoleni-vjezdu-vozidla/rz-typ-vozidla-csv", consumes = {"multipart/form-data"})
    @ValidReCaptcha
    public ResponseEntity<RzTypVozidlaDto> rzTypVozidlaCsv(@RequestPart("file")MultipartFile file,
             @RequestHeader("reCAPTCHA-Token") String recaptchaToken) throws IOException, ParseException, BaseException, ReCaptchaException {
        return ResponseEntity.ok(povoleniVjezduVozidlaService.processRzTypVozidlaCsvData(file));
  
       
    }

    @PostMapping("/povoleni-vjezdu-vozidla/save")
    @ValidReCaptcha
    public ResponseEntity<PovoleniVjezduVozidlaDto> save(@RequestBody @Valid PovoleniVjezduVozidlaDto povoleniVjezduVozidlaDto, 
                @RequestHeader("reCAPTCHA-Token") String recaptchaToken) throws ReCaptchaException {
        //PovoleniVjezduVozidla povoleniVjezduVozidla = povoleniVjezduVozidlaService.create(povoleniVjezduVozidlaDto);
        PovoleniVjezduVozidlaDto savePovoleniVjezduVozidlaDto = povoleniVjezduVozidlaService.create(povoleniVjezduVozidlaDto);

        return ResponseEntity.ok(savePovoleniVjezduVozidlaDto);
    }

}
