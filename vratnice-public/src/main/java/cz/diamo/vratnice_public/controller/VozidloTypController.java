package cz.diamo.vratnice_public.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cz.diamo.vratnice_public.dto.VozidloTypDto;
import cz.diamo.vratnice_public.exception.BaseException;
import cz.diamo.vratnice_public.service.VratniceService;
import jakarta.servlet.http.HttpServletRequest;


@RestController
public class VozidloTypController extends BaseController {
    final static Logger logger = LogManager.getLogger(VozidloTypController.class);

    @Autowired
    private VratniceService vratniceService;

    @GetMapping("/vozidlo-typ/list")
    public ResponseEntity<List<VozidloTypDto>> list(HttpServletRequest request, @RequestParam @Nullable Boolean withIZS ) {
        return ResponseEntity.ok(vratniceService.seznamVozidloTyp(withIZS));
    }

    @GetMapping("vozidlo-typ/get-by-nazev")
    public ResponseEntity<VozidloTypDto> getByNazev(@RequestParam String nazev) throws BaseException {
        logger.info(nazev);
        return ResponseEntity.ok(vratniceService.getVozidloTypByNazev(nazev));
    }
    

}
