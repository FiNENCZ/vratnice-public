package cz.diamo.vratnice_public.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import cz.diamo.vratnice_public.annotation.TransactionalROE;
import cz.diamo.vratnice_public.dto.LokalitaDto;
@Service
@TransactionalROE
public class VratniceService {

    @Autowired
    private RestOperations restVratnice;

    @TransactionalROE
    public List<LokalitaDto> seznamLokalit(String idZavod) {
        String url = "/api/lokalita/list";
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("idZavod", idZavod);

        LokalitaDto[] response = restVratnice.getForObject(builder.toUriString(), LokalitaDto[].class);
        return Arrays.asList(response);
    }


}
