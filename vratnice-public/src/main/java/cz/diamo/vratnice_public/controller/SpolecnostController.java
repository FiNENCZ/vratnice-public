package cz.diamo.vratnice_public.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import cz.diamo.vratnice_public.dto.SpolecnostDto;
import cz.diamo.vratnice_public.exception.BaseException;
import cz.diamo.vratnice_public.service.VratniceService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
public class SpolecnostController  extends BaseController  {
    final static Logger logger = LogManager.getLogger(RidicController.class);

    @Autowired
    private VratniceService vratniceService;

    @GetMapping("spolecnost/list")
    public ResponseEntity<List<SpolecnostDto>> list() {
        return ResponseEntity.ok(vratniceService.seznamSpolecnosti());
    }

    @GetMapping("spolecnost/get-by-nazev")
    public ResponseEntity<SpolecnostDto> getByNazev(@RequestParam String nazev) throws BaseException {
        return ResponseEntity.ok(vratniceService.getSpolecnostByNazev(nazev));
    }

    @PostMapping("/spolecnost/save")
    public ResponseEntity<SpolecnostDto> save(@RequestBody @Valid SpolecnostDto spolecnostDto) {
        return ResponseEntity.ok(vratniceService.saveSpolecnost(spolecnostDto));
    }
    
    

}
