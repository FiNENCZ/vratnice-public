package cz.diamo.vratnice_public.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cz.diamo.vratnice_public.dto.ZavodDto;
import cz.diamo.vratnice_public.service.VratniceService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class ZavodController extends BaseController  {
    final static Logger logger = LogManager.getLogger(ZavodController.class);

    @Autowired
    private VratniceService vratniceService;

    @GetMapping("/zavod/list")
	public ResponseEntity<List<ZavodDto>> list(HttpServletRequest request,
			@RequestParam @Nullable Boolean aktivni, @RequestParam @Nullable String idZavodu) {

		return ResponseEntity.ok(vratniceService.seznamZavodu(aktivni, idZavodu));
	}


}
