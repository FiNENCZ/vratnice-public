package cz.diamo.vratnice_public.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestController;

import cz.diamo.vratnice_public.dto.LokalitaDto;
import cz.diamo.vratnice_public.service.VratniceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class LokalitaController {

    @Autowired
    private VratniceService vratniceService;

    @GetMapping("/lokalita/list")
    public ResponseEntity<List<LokalitaDto>> list(@RequestParam @Nullable String idZavod) {
        return ResponseEntity.ok(vratniceService.seznamLokalit(idZavod));
    }
    

}
