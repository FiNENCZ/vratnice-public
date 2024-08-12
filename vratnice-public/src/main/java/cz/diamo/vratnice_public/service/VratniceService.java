package cz.diamo.vratnice_public.service;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import cz.diamo.vratnice_public.annotation.TransactionalROE;
import cz.diamo.vratnice_public.dto.LokalitaDto;
import cz.diamo.vratnice_public.dto.RidicDto;
import cz.diamo.vratnice_public.dto.SpolecnostDto;
import cz.diamo.vratnice_public.dto.StatDto;
import cz.diamo.vratnice_public.dto.VozidloTypDto;
import cz.diamo.vratnice_public.dto.ZavodDto;
import cz.diamo.vratnice_public.exception.BaseException;

@Service
@TransactionalROE
public class VratniceService {


    final static Logger logger = LogManager.getLogger(VratniceService.class);

    @Autowired
    private RestOperations restVratnice;


    @Autowired
    private MessageSource messageSource;

    @TransactionalROE
    public List<LokalitaDto> seznamLokalit(String idZavod) {
        String url = "/vratnice-public/lokalita/list";
        String lang = LocaleContextHolder.getLocale().getLanguage();

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("idZavod", idZavod)
                .queryParam("lang", lang);

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
        String lang = LocaleContextHolder.getLocale().getLanguage();
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("aktivni", aktivni)
                .queryParam("idZavod", idZavod)
                .queryParam("lang", lang);

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
        String lang = LocaleContextHolder.getLocale().getLanguage();
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("withIZS", withIZS)
                .queryParam("lang", lang);

        try {
            VozidloTypDto[] response = restVratnice.getForObject(builder.encode().toUriString(), VozidloTypDto[].class);
            return Arrays.asList(response);
        } catch (RestClientException e) {

            throw new RestClientException(String.format("Pri volání Vránice došlo k chybě./n%s", e));
        }
    }


    @TransactionalROE
    public VozidloTypDto getVozidloTypByNazev(String nazev) throws BaseException {
        try {
        HttpEntity<Void> requestEntity = new HttpEntity<Void>(null, null);
        String lang = LocaleContextHolder.getLocale().getLanguage();
        HashMap<String, String> params = new HashMap<>();
        params.put("nazev", nazev); 
        params.put("lang", lang);
        
        ResponseEntity<VozidloTypDto> result = restVratnice.exchange("/vratnice-public/vozidlo-typ/get-by-nazev?nazev={nazev}&lang={lang}", HttpMethod.GET, requestEntity, VozidloTypDto.class, params);
        
        if (result.getStatusCode().isError())
            throw new BaseException(String.format("Při volání Vrátnice došlo k chybě./n%s", result.getStatusCode()));
        return result.getBody();

        } catch (HttpClientErrorException | HttpServerErrorException he) {
            // Chybové hlášky od serveru
            String responseBody = new String(he.getResponseBodyAsByteArray(), StandardCharsets.UTF_8);
            try {
                JSONObject obj = new JSONObject(responseBody);
                String errorMessage = obj.optString("message", he.getMessage());
                logger.info("Chyba při volání REST API: {}", errorMessage);
                throw new BaseException(errorMessage);
            } catch (JSONException e) {
                logger.error("Chyba při zpracování odpovědi: {}", responseBody, e);
                throw new BaseException(he.getMessage());
            }
            
        } catch (BaseException be) {
            logger.error("BaseException: ", be);
            throw be;
            
        } catch (Exception e) {
            logger.error("Neočekávaná chyba: ", e);
            throw new BaseException(messageSource.getMessage("vratnice.nelze.spojit", null, LocaleContextHolder.getLocale()));
        }
    }

    @TransactionalROE
    public RidicDto getRidicByCisloOp(String cisloOp) {
        String url = "/vratnice-public/ridic/get-by-cislo-op";
        String lang = LocaleContextHolder.getLocale().getLanguage();
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("cisloOp", cisloOp)
                .queryParam("lang", lang);

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
        String lang = LocaleContextHolder.getLocale().getLanguage();
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
            .queryParam("lang", lang);;

        try {
            StatDto[] response = restVratnice.getForObject(builder.toUriString(), StatDto[].class);
            return Arrays.asList(response);
        } catch (RestClientException e) {

            throw new RestClientException(String.format("Pri volání Vránice došlo k chybě./n%s", e));
        }
    }

    @TransactionalROE
    public StatDto getStatByNazev(String nazev) throws BaseException {
        try {
        HttpEntity<Void> requestEntity = new HttpEntity<Void>(null, null);
        String lang = LocaleContextHolder.getLocale().getLanguage();


        HashMap<String, String> params = new HashMap<>();
        params.put("nazev", nazev); 
        params.put("lang", lang);
        
        ResponseEntity<StatDto> result = restVratnice.exchange("/vratnice-public/stat/get-by-nazev?nazev={nazev}&lang={lang}", HttpMethod.GET, requestEntity, StatDto.class, params);
        if (result.getStatusCode().isError())
            throw new BaseException(String.format("Při volání Vrátnice došlo k chybě./n%s", result.getStatusCode()));
        return result.getBody();

        } catch (HttpClientErrorException | HttpServerErrorException he) {
            // Chybové hlášky od serveru
            String responseBody = new String(he.getResponseBodyAsByteArray(), StandardCharsets.UTF_8);
            try {
                JSONObject obj = new JSONObject(responseBody);
                String errorMessage = obj.optString("message", he.getMessage());
                logger.info("Chyba při volání REST API: {}", errorMessage);
                throw new BaseException(errorMessage);
            } catch (JSONException e) {
                logger.error("Chyba při zpracování odpovědi: {}", responseBody, e);
                throw new BaseException(he.getMessage());
            }
            
        } catch (BaseException be) {
            logger.error("BaseException: ", be);
            throw be;
            
        } catch (Exception e) {
            logger.error("Neočekávaná chyba: ", e);
            throw new BaseException(messageSource.getMessage("vratnice.nelze.spojit", null, LocaleContextHolder.getLocale()));
        }
    }

    @TransactionalROE
    public RidicDto saveRidic(RidicDto ridicDto) {
        String url = "/vratnice-public/ridic/save";
        String lang = LocaleContextHolder.getLocale().getLanguage();
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
            .queryParam("lang", lang);

        try {
            RidicDto response = restVratnice.postForObject(builder.toUriString(), ridicDto, RidicDto.class);
            return response;
        } catch (RestClientException e) {

            throw new RestClientException(String.format("Pri volání Vránice došlo k chybě./n%s", e));
        }
    }

    @TransactionalROE
    public List<SpolecnostDto> seznamSpolecnosti() {
        String url = "/vratnice-public/spolecnost/list";
        String lang = LocaleContextHolder.getLocale().getLanguage();

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("lang", lang);

        try {
            SpolecnostDto[] response = restVratnice.getForObject(builder.toUriString(), SpolecnostDto[].class);
            return Arrays.asList(response);
        } catch (RestClientException e) {

            throw new RestClientException(String.format("Pri volání Vránice došlo k chybě./n%s", e));
        }
    }

    @TransactionalROE
    public SpolecnostDto getSpolecnostByNazev(String nazev) throws BaseException {
        try {
        HttpEntity<Void> requestEntity = new HttpEntity<Void>(null, null);
        String lang = LocaleContextHolder.getLocale().getLanguage();
        HashMap<String, String> params = new HashMap<>();
        params.put("nazev", nazev); 
        params.put("lang", lang);
        
        ResponseEntity<SpolecnostDto> result = restVratnice.exchange("/vratnice-public/spolecnost/get-by-nazev?nazev={nazev}&lang={lang}", HttpMethod.GET, requestEntity, SpolecnostDto.class, params);
        
        if (result.getStatusCode().isError())
            throw new BaseException(String.format("Při volání Vrátnice došlo k chybě./n%s", result.getStatusCode()));
        return result.getBody();

        } catch (HttpClientErrorException | HttpServerErrorException he) {
            // Chybové hlášky od serveru
            String responseBody = new String(he.getResponseBodyAsByteArray(), StandardCharsets.UTF_8);
            try {
                JSONObject obj = new JSONObject(responseBody);
                String errorMessage = obj.optString("message", he.getMessage());
                logger.info("Chyba při volání REST API: {}", errorMessage);
                throw new BaseException(errorMessage);
            } catch (JSONException e) {
                logger.error("Chyba při zpracování odpovědi: {}", responseBody, e);
                throw new BaseException(he.getMessage());
            }
            
        } catch (BaseException be) {
            logger.error("BaseException: ", be);
            throw be;
            
        } catch (Exception e) {
            logger.error("Neočekávaná chyba: ", e);
            throw new BaseException(messageSource.getMessage("vratnice.nelze.spojit", null, LocaleContextHolder.getLocale()));
        }
    }

    @TransactionalROE
    public SpolecnostDto saveSpolecnost(SpolecnostDto spolecnost) {
        String url = "/vratnice-public/spolecnost/save";
        String lang = LocaleContextHolder.getLocale().getLanguage();
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
            .queryParam("lang", lang);

        try {
            SpolecnostDto response = restVratnice.postForObject(builder.toUriString(), spolecnost, SpolecnostDto.class);
            return response;
        } catch (RestClientException e) {

            throw new RestClientException(String.format("Pri volání Vránice došlo k chybě./n%s", e));
        }
    }


}
