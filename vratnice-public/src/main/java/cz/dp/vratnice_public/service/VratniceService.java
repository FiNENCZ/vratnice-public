package cz.dp.vratnice_public.service;

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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import cz.dp.vratnice_public.annotation.TransactionalROE;
import cz.dp.vratnice_public.configuration.AppProperties;
import cz.dp.vratnice_public.dto.LokalitaDto;
import cz.dp.vratnice_public.dto.PovoleniVjezduVozidlaDto;
import cz.dp.vratnice_public.dto.RidicDto;
import cz.dp.vratnice_public.dto.SpolecnostDto;
import cz.dp.vratnice_public.dto.StatDto;
import cz.dp.vratnice_public.dto.VozidloTypDto;
import cz.dp.vratnice_public.dto.ZavodDto;
import cz.dp.vratnice_public.exception.BaseException;

import org.json.JSONException;
import org.json.JSONObject;

@Service
@TransactionalROE
public class VratniceService {


    final static Logger logger = LogManager.getLogger(VratniceService.class);

    @Autowired
    private RestOperations restVratnice;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private AppProperties appProperties;

    private HttpEntity<Void> createHttpEntityWithAuth() {
        String username = appProperties.getVratniceApiUsername();
        String password = appProperties.getVratniceApiPassword();

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(username, password);
        return new HttpEntity<>(headers);
    }

    @TransactionalROE
    public List<LokalitaDto> seznamLokalit(String idZavod) {
        String url = "/rest/lokalita/list";
        String lang = LocaleContextHolder.getLocale().getLanguage();

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("idZavod", idZavod)
                .queryParam("lang", lang);

        try {
            LokalitaDto[] response = restVratnice.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                createHttpEntityWithAuth(),
                LokalitaDto[].class
            ).getBody();
            return Arrays.asList(response);
        } catch (RestClientException e) {

            throw new RestClientException(String.format("Pri volání Vránice došlo k chybě./n%s", e));
        }
    }


    @TransactionalROE
    public List<ZavodDto> seznamZavodu(Boolean aktivni, String idZavod) {
        String url = "/rest/zavod/list";
        String lang = LocaleContextHolder.getLocale().getLanguage();
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("aktivni", aktivni)
                .queryParam("idZavod", idZavod)
                .queryParam("lang", lang);

        try {
            ZavodDto[] response = restVratnice.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                createHttpEntityWithAuth(),
                ZavodDto[].class
            ).getBody();
            return Arrays.asList(response);
        } catch (RestClientException e) {

            throw new RestClientException(String.format("Pri volání Vránice došlo k chybě./n%s", e));
        }
    }

    @TransactionalROE
    public List<VozidloTypDto> seznamVozidloTyp(Boolean withIZS) {
        String url = "/rest/vozidlo-typ/list";
        String lang = LocaleContextHolder.getLocale().getLanguage();
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("withIZS", withIZS)
                .queryParam("lang", lang);
        
    
        try {
            VozidloTypDto[] response = restVratnice.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    createHttpEntityWithAuth(),
                    VozidloTypDto[].class
            ).getBody();
            return Arrays.asList(response);
        } catch (RestClientException e) {

            throw new RestClientException(String.format("Pri volání Vránice došlo k chybě./n%s", e));
        }
    }


    @TransactionalROE
    public VozidloTypDto getVozidloTypByNazev(String nazev) throws BaseException {
        try {
        String lang = LocaleContextHolder.getLocale().getLanguage();
        HashMap<String, String> params = new HashMap<>();
        params.put("nazev", nazev); 
        params.put("lang", lang);

        ResponseEntity<VozidloTypDto> result = restVratnice.exchange(
            "/rest/vozidlo-typ/get-by-nazev?nazev={nazev}&lang={lang}",
            HttpMethod.GET,
            createHttpEntityWithAuth(),
            VozidloTypDto.class,
            params
        );
    
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
    public RidicDto getRidicByCisloOp(String cisloOp) throws BaseException {
        try {
            String lang = LocaleContextHolder.getLocale().getLanguage();
            HashMap<String, String> params = new HashMap<>();
            params.put("cisloOp", cisloOp); 
            params.put("lang", lang);

            ResponseEntity<RidicDto> result = restVratnice.exchange(
                    "/rest/ridic/get-by-cislo-op?cisloOp={cisloOp}&lang={lang}",
                    HttpMethod.GET,
                    createHttpEntityWithAuth(),
                    RidicDto.class,
                    params
            );

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
    public List<StatDto> seznamStatu() {
        String url = "/rest/stat/list";
        String lang = LocaleContextHolder.getLocale().getLanguage();
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
            .queryParam("lang", lang);;

        try {
            StatDto[] response = restVratnice.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                createHttpEntityWithAuth(),
                StatDto[].class
            ).getBody();
            return Arrays.asList(response);
        } catch (RestClientException e) {

            throw new RestClientException(String.format("Pri volání Vránice došlo k chybě./n%s", e));
        }
    }

    @TransactionalROE
    public StatDto getStatByNazev(String nazev) throws BaseException {
        try {
        String lang = LocaleContextHolder.getLocale().getLanguage();

        HashMap<String, String> params = new HashMap<>();
        params.put("nazev", nazev); 
        params.put("lang", lang);
        
        ResponseEntity<StatDto> result = restVratnice.exchange(
            "/rest/stat/get-by-nazev?nazev={nazev}&lang={lang}",
            HttpMethod.GET,
            createHttpEntityWithAuth(),
            StatDto.class,
            params
        );

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
        String url = "/rest/ridic/save";
        String lang = LocaleContextHolder.getLocale().getLanguage();
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
            .queryParam("lang", lang);

        try {
            HttpEntity<RidicDto> requestEntity = new HttpEntity<>(ridicDto, createHttpEntityWithAuth().getHeaders());

            ResponseEntity<RidicDto> responseEntity = restVratnice.exchange(
                    builder.toUriString(),
                    HttpMethod.POST,
                    requestEntity,
                    RidicDto.class
            );
    
            return responseEntity.getBody();
        } catch (RestClientException e) {

            throw new RestClientException(String.format("Pri volání Vránice došlo k chybě./n%s", e));
        }
    }

    @TransactionalROE
    public List<SpolecnostDto> seznamSpolecnosti() {
        String url = "/rest/spolecnost/list";
        String lang = LocaleContextHolder.getLocale().getLanguage();

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("lang", lang);

        try {
            SpolecnostDto[] response = restVratnice.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                createHttpEntityWithAuth(),
                SpolecnostDto[].class
            ).getBody();
            return Arrays.asList(response);
        } catch (RestClientException e) {

            throw new RestClientException(String.format("Pri volání Vránice došlo k chybě./n%s", e));
        }
    }

    @TransactionalROE
    public SpolecnostDto getSpolecnostByNazev(String nazev) throws BaseException {
        try {
            String lang = LocaleContextHolder.getLocale().getLanguage();
            HashMap<String, String> params = new HashMap<>();
            params.put("nazev", nazev); 
            params.put("lang", lang);
            
            ResponseEntity<SpolecnostDto> result = restVratnice.exchange(
                    "/rest/spolecnost/get-by-nazev?nazev={nazev}&lang={lang}",
                    HttpMethod.GET,
                    createHttpEntityWithAuth(),
                    SpolecnostDto.class,
                    params
            );

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
        String url = "/rest/spolecnost/save";
        String lang = LocaleContextHolder.getLocale().getLanguage();
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
            .queryParam("lang", lang);

        try {
            HttpEntity<SpolecnostDto> requestEntity = new HttpEntity<>(spolecnost, createHttpEntityWithAuth().getHeaders());

            ResponseEntity<SpolecnostDto> responseEntity = restVratnice.exchange(
                    builder.toUriString(),
                    HttpMethod.POST,
                    requestEntity,
                    SpolecnostDto.class
            );
    
            return responseEntity.getBody();
        } catch (RestClientException e) {

            throw new RestClientException(String.format("Pri volání Vránice došlo k chybě./n%s", e));
        }
    }

    @TransactionalROE
    public PovoleniVjezduVozidlaDto savePovoleni(PovoleniVjezduVozidlaDto povoleniVjezduVozidlaDto) {
        String url = "/rest/povoleni-vjezdu-vozidla/save";
        String lang = LocaleContextHolder.getLocale().getLanguage();
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
            .queryParam("lang", lang);

        try {
            HttpEntity<PovoleniVjezduVozidlaDto> requestEntity = new HttpEntity<>(povoleniVjezduVozidlaDto, createHttpEntityWithAuth().getHeaders());

            ResponseEntity<PovoleniVjezduVozidlaDto> responseEntity = restVratnice.exchange(
                    builder.toUriString(),
                    HttpMethod.POST,
                    requestEntity,
                    PovoleniVjezduVozidlaDto.class
            );
            return responseEntity.getBody();
        } catch (RestClientException e) {

            throw new RestClientException(String.format("Pri volání Vránice došlo k chybě./n%s", e));
        }
    }


}
