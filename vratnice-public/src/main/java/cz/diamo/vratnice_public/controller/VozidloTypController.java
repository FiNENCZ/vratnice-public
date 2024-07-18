package cz.diamo.vratnice_public.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import cz.diamo.vratnice_public.dto.VozidloTypDto;


@RestController
public class VozidloTypController extends BaseController {
    final static Logger logger = LogManager.getLogger(VozidloTypController.class);

    @GetMapping("/vozidlo-typ/list")
    public ResponseEntity<List<VozidloTypDto>> list(@RequestParam @Nullable Boolean withIZS) {
        List<VozidloTypDto> result = new ArrayList<VozidloTypDto>();
        /*List<VozidloTyp> list = vozidloTypService.getList(withIZS);
        try {
            if (list != null && list.size() > 0) {
                for (VozidloTyp vozidloTyp : list) {
                    vozidloTyp.setNazev(resourcesComponent.getResources(LocaleContextHolder.getLocale(),vozidloTyp.getNazevResx()));
                    result.add(new VozidloTypDto(vozidloTyp));
                }
            } 
        }catch (Exception e) {
			logger.error(e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
		}*/

        return ResponseEntity.ok(result);
    }

}
