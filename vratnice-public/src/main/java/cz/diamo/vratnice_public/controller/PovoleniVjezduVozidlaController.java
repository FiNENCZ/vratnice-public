package cz.diamo.vratnice_public.controller;

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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import cz.diamo.vratnice_public.dto.PovoleniVjezduVozidlaDto;
import cz.diamo.vratnice_public.dto.RzTypVozidlaDto;
import cz.diamo.vratnice_public.exception.BaseException;
import cz.diamo.vratnice_public.service.PovoleniVjezduVozidlaService;
import jakarta.validation.Valid;

@RestController
public class PovoleniVjezduVozidlaController extends BaseController{

    final static Logger logger = LogManager.getLogger(PovoleniVjezduVozidlaController.class);


    @Autowired
    private PovoleniVjezduVozidlaService povoleniVjezduVozidlaService;

    @PostMapping(value = "/povoleni-vjezdu-vozidla/povoleni-csv", consumes = {"multipart/form-data"})
    public ResponseEntity<Set<PovoleniVjezduVozidlaDto>> povoleniCsv(@RequestPart("file")MultipartFile file) throws IOException, ParseException, BaseException {
        try {
            return ResponseEntity.ok(povoleniVjezduVozidlaService.processPovoleniCsvData(file));
        } catch (ResponseStatusException re) {
            logger.error(re);
            throw re;        
        } catch (Exception e) {
            logger.error(e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
        }
    }


    @PostMapping(value = "/povoleni-vjezdu-vozidla/rz-typ-vozidla-csv", consumes = {"multipart/form-data"})
    public ResponseEntity<RzTypVozidlaDto> rzTypVozidlaCsv(@RequestPart("file")MultipartFile file) throws IOException, ParseException, BaseException {
        try {
            return ResponseEntity.ok(povoleniVjezduVozidlaService.processRzTypVozidlaCsvData(file));
        } catch (ResponseStatusException re) {
			logger.error(re);
			throw re;
		} catch (Exception e) {
			logger.error(e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
		}
       
    }

    @PostMapping("/povoleni-vjezdu-vozidla/save")
    public ResponseEntity<PovoleniVjezduVozidlaDto> save(@RequestBody @Valid PovoleniVjezduVozidlaDto povoleniVjezduVozidlaDto) {
        //PovoleniVjezduVozidla povoleniVjezduVozidla = povoleniVjezduVozidlaService.create(povoleniVjezduVozidlaDto);
        PovoleniVjezduVozidlaDto savePovoleniVjezduVozidlaDto = povoleniVjezduVozidlaService.create(povoleniVjezduVozidlaDto);

        return ResponseEntity.ok(savePovoleniVjezduVozidlaDto);
    }

}
