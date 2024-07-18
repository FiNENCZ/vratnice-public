package cz.diamo.vratnice_public.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import cz.diamo.vratnice_public.dto.RidicDto;

@RestController
public class RidicController extends BaseController  {
    final static Logger logger = LogManager.getLogger(RidicController.class);

    @GetMapping("/ridic/list")
    public ResponseEntity<List<RidicDto>> list() {
        List<RidicDto> ridicDtos = new ArrayList<RidicDto>();
        /* 
        List<RidicDto> ridicDtos = ridicService.list().stream()
            .map(RidicDto::new)
            .collect(Collectors.toList());
            */
        return ResponseEntity.ok(ridicDtos);
    }

}
