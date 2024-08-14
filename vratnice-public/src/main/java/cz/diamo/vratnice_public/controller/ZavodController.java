package cz.diamo.vratnice_public.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cz.diamo.vratnice_public.annotation.ValidReCaptcha;
import cz.diamo.vratnice_public.dto.ZavodDto;
import cz.diamo.vratnice_public.exception.ReCaptchaException;
import cz.diamo.vratnice_public.service.VratniceService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class ZavodController extends BaseController  {
    final static Logger logger = LogManager.getLogger(ZavodController.class);

    @Autowired
    private VratniceService vratniceService;

    @GetMapping("/zavod/list")
    @ValidReCaptcha
	public ResponseEntity<List<ZavodDto>> list(HttpServletRequest request, @RequestHeader("reCAPTCHA-Token") String recaptchaToken,
			@RequestParam @Nullable Boolean aktivni, @RequestParam @Nullable String idZavodu) throws ReCaptchaException {

		return ResponseEntity.ok(vratniceService.seznamZavodu(aktivni, idZavodu));
	}


}
