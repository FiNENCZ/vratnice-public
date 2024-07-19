package cz.diamo.vratnice_public.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import cz.diamo.vratnice_public.annotation.TransactionalROE;
import cz.diamo.vratnice_public.controller.ZavodController;
import cz.diamo.vratnice_public.dto.LokalitaDto;
import cz.diamo.vratnice_public.dto.RidicDto;
import cz.diamo.vratnice_public.dto.StatDto;
import cz.diamo.vratnice_public.dto.VozidloTypDto;
import cz.diamo.vratnice_public.dto.ZavodDto;

@Service
@TransactionalROE
public class VratniceService {

    @Autowired
    private RestOperations restVratnice;

    @TransactionalROE
    public List<LokalitaDto> seznamLokalit(String idZavod) {
        String url = "/vratnice-public/lokalita/list";
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("idZavod", idZavod);

        try {
            LokalitaDto[] response = restVratnice.getForObject(builder.toUriString(), LokalitaDto[].class);
            return Arrays.asList(response);
        } catch (RestClientException e) {

            throw new RestClientException(String.format("Pri volání Vránice došlo k chybě./n%s", e));
        }
    }


    @TransactionalROE
    public List<ZavodDto> seznamZavodu(Boolean aktivni, String idZavod) {
        String url = "/vratnice-public/zavod/list";
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("aktivni", aktivni)
                .queryParam("idZavod", idZavod);

        try {
            ZavodDto[] response = restVratnice.getForObject(builder.toUriString(), ZavodDto[].class);
            return Arrays.asList(response);
        } catch (RestClientException e) {

            throw new RestClientException(String.format("Pri volání Vránice došlo k chybě./n%s", e));
        }
    }

    @TransactionalROE
    public List<VozidloTypDto> seznamVozidloTyp(Boolean withIZS) {
        String url = "/vratnice-public/vozidlo-typ/list";
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("withIZS", withIZS);

        try {
            VozidloTypDto[] response = restVratnice.getForObject(builder.toUriString(), VozidloTypDto[].class);
            return Arrays.asList(response);
        } catch (RestClientException e) {

            throw new RestClientException(String.format("Pri volání Vránice došlo k chybě./n%s", e));
        }
    }

    @TransactionalROE
    public RidicDto listRidicByCisloOp(String cisloOp) {
        String url = "/vratnice-public/ridic/list-by-cislo-op";
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("cisloOp", cisloOp);

        try {
            RidicDto response = restVratnice.getForObject(builder.toUriString(), RidicDto.class);
            return response;
        } catch (RestClientException e) {

            throw new RestClientException(String.format("Pri volání Vránice došlo k chybě./n%s", e));
        }
    }


    @TransactionalROE
    public List<StatDto> seznamStatu() {
        String url = "/vratnice-public/stat/list";
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);

        try {
            StatDto[] response = restVratnice.getForObject(builder.toUriString(), StatDto[].class);
            return Arrays.asList(response);
        } catch (RestClientException e) {

            throw new RestClientException(String.format("Pri volání Vránice došlo k chybě./n%s", e));
        }
    }

    @TransactionalROE
    public RidicDto saveRidic(RidicDto ridicDto) {
        String url = "/vratnice-public/ridic/save";
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);

        try {
            RidicDto response = restVratnice.postForObject(builder.toUriString(), ridicDto, RidicDto.class);
            return response;
        } catch (RestClientException e) {

            throw new RestClientException(String.format("Pri volání Vránice došlo k chybě./n%s", e));
        }
    }


}
