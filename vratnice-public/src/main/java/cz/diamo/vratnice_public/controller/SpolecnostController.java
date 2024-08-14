package cz.diamo.vratnice_public.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import cz.diamo.vratnice_public.dto.SpolecnostDto;
import cz.diamo.vratnice_public.service.VratniceService;

import org.springframework.web.bind.annotation.GetMapping;



@RestController
public class SpolecnostController  extends BaseController  {
    final static Logger logger = LogManager.getLogger(RidicController.class);

    @Autowired
    private VratniceService vratniceService;

    @GetMapping("spolecnost/list")
    public ResponseEntity<List<SpolecnostDto>> list() {
        return ResponseEntity.ok(vratniceService.seznamSpolecnosti());
    }
}
