package cz.diamo.vratnice_public.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cz.diamo.vratnice_public.dto.ZavodDto;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class ZavodController extends BaseController  {
    final static Logger logger = LogManager.getLogger(ZavodController.class);

    @GetMapping("/zavod/list")
	public List<ZavodDto> list(HttpServletRequest request,
			@RequestParam @Nullable Boolean aktivni, @RequestParam @Nullable String idZavodu) {

		List<ZavodDto> result = new ArrayList<ZavodDto>();
		/*List<Zavod> list = zavodServices.getList(idZavodu, aktivni);
		
		if (list != null && list.size() > 0) {
			for (Zavod zavod : list) {
				result.add(new ZavodDto(zavod));
			}
		}*/

		return result;
	}


}
