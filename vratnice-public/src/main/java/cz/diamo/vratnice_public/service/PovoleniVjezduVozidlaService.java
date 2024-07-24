package cz.diamo.vratnice_public.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;

import cz.diamo.vratnice_public.csvRepresentation.PovoleniVjezduVozidlaCsvRepresentation;
import cz.diamo.vratnice_public.csvRepresentation.RzTypVozidlaCsvRepresentation;
import cz.diamo.vratnice_public.dto.PovoleniVjezduVozidlaDto;
import cz.diamo.vratnice_public.dto.RidicDto;
import cz.diamo.vratnice_public.dto.RzTypVozidlaDto;
import cz.diamo.vratnice_public.dto.VozidloTypDto;
import cz.diamo.vratnice_public.dto.ZavodDto;
import cz.diamo.vratnice_public.exception.BaseException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;

@Service
public class PovoleniVjezduVozidlaService {

    @Autowired
    private VratniceService vratniceService;

    @Autowired
    private Validator validator;


    public PovoleniVjezduVozidlaDto create(PovoleniVjezduVozidlaDto povoleniVjezduVozidlaDto) {
        if (povoleniVjezduVozidlaDto.getRidic() != null) {
            RidicDto savedRidic =  vratniceService.saveRidic(povoleniVjezduVozidlaDto.getRidic());
            povoleniVjezduVozidlaDto.setRidic(savedRidic);
        }

        //TODO: zaslání na žádosti

        return povoleniVjezduVozidlaDto;
    }

    public Set<PovoleniVjezduVozidlaDto>  processPovoleniCsvData(MultipartFile file) throws IOException, ParseException, BaseException {
        Set<PovoleniVjezduVozidlaDto> povoleniVjezduVozidlas = parsePovoleniCsv(file);
        Set<PovoleniVjezduVozidlaDto> savedDtos = new HashSet<>();

        for (PovoleniVjezduVozidlaDto dto : povoleniVjezduVozidlas) {
            PovoleniVjezduVozidlaDto savedEntity = create(dto); 
            savedDtos.add(savedEntity);
        }
    

        return savedDtos;
    }
    

    private Set<PovoleniVjezduVozidlaDto> parsePovoleniCsv(MultipartFile file) throws IOException, ParseException, BaseException {
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            Set<PovoleniVjezduVozidlaDto> povoleniVjezduVozidlaSet = new HashSet<>();
            HeaderColumnNameMappingStrategy<PovoleniVjezduVozidlaCsvRepresentation> strategy =
                new HeaderColumnNameMappingStrategy<>();

            strategy.setType(PovoleniVjezduVozidlaCsvRepresentation.class);
            CsvToBean<PovoleniVjezduVozidlaCsvRepresentation> csvToBean = 
                new CsvToBeanBuilder<PovoleniVjezduVozidlaCsvRepresentation>(reader)
                    .withMappingStrategy(strategy).withIgnoreEmptyLine(true).withIgnoreLeadingWhiteSpace(true).build();

            for (PovoleniVjezduVozidlaCsvRepresentation csvLine : csvToBean.parse()) {
                PovoleniVjezduVozidlaDto povoleniVjezduVozidlaDto = new PovoleniVjezduVozidlaDto();

                // Basic fields
                povoleniVjezduVozidlaDto.setJmenoZadatele(csvLine.getJmenoZadatele());
                povoleniVjezduVozidlaDto.setPrijmeniZadatele(csvLine.getPrijmeniZadatele());
                povoleniVjezduVozidlaDto.setSpolecnostZadatele(csvLine.getSpolecnostZadatele());
                povoleniVjezduVozidlaDto.setIcoZadatele(csvLine.getIcoZadatele());
                povoleniVjezduVozidlaDto.setDuvodZadosti(csvLine.getDuvodZadosti());
                povoleniVjezduVozidlaDto.setZemeRegistraceVozidla(vratniceService.getStatByNazev(csvLine.getZemeRegistraceVozidla()));

                // Handle multiple values separated by '|'
                if (csvLine.getRzVozidla() != null && csvLine.getRzVozidla().length > 0) {
                    povoleniVjezduVozidlaDto.setRzVozidla(List.of(csvLine.getRzVozidla()));
                }

                if (csvLine.getTypVozidla() != null && csvLine.getTypVozidla().length > 0) {
                    List<VozidloTypDto> typVozidlaList = new ArrayList<>();
                    for (String typ : csvLine.getTypVozidla()) {
                        typVozidlaList.add(vratniceService.getVozidloTypByNazev(typ));
                    }
                    povoleniVjezduVozidlaDto.setTypVozidla(typVozidlaList);
                }

                if(csvLine.getRidic_jmeno() != null && csvLine.getRidic_prijmeni() != null && csvLine.getRidic_cisloOp() != null ) {
                    RidicDto ridicDto = new RidicDto();

                    RidicDto existedRidic = vratniceService.getRidicByCisloOp(csvLine.getRidic_cisloOp());
                    if (existedRidic == null) {
                        ridicDto.setJmeno(csvLine.getRidic_jmeno());
                        ridicDto.setPrijmeni(csvLine.getRidic_prijmeni());
                        ridicDto.setCisloOp(csvLine.getRidic_cisloOp());
                        ridicDto.setFirma(csvLine.getRidic_firma());
                    } else {
                        ridicDto = existedRidic;
                    }

                    povoleniVjezduVozidlaDto.setRidic(ridicDto);
                }

                povoleniVjezduVozidlaDto.setSpolecnostVozidla(csvLine.getSpolecnostVozidla());
                povoleniVjezduVozidlaDto.setDatumOd(parseDate(csvLine.getDatumOd()));
                povoleniVjezduVozidlaDto.setDatumDo(parseDate(csvLine.getDatumDo()));

                List<ZavodDto> listOfExistedZavods = vratniceService.seznamZavodu(null, null);
            
                if (csvLine.getZavod_nazvy() != null && csvLine.getZavod_nazvy().length > 0) {
                    List<ZavodDto> zavodyList = new ArrayList<>();
                    for (String zavod : csvLine.getZavod_nazvy()) {
                        for (ZavodDto zavodDto: listOfExistedZavods) {
                            if (zavodDto.getNazev() == zavod) {
                                zavodyList.add(zavodDto);
                            }
                        }
                    }
                    povoleniVjezduVozidlaDto.setZavod(zavodyList);
                }
                povoleniVjezduVozidlaDto.setOpakovanyVjezd(csvLine.isOpakovanyVjezd());

                validate(povoleniVjezduVozidlaDto);

                // Add to set
                povoleniVjezduVozidlaSet.add(povoleniVjezduVozidlaDto);
            }
            return povoleniVjezduVozidlaSet;
        }
     }


    public RzTypVozidlaDto processRzTypVozidlaCsvData(MultipartFile file) throws IOException, ParseException, BaseException {
        Set<RzTypVozidlaDto> rzTypVozidlaSet = parseRzTypVozidlaCsv(file);

        RzTypVozidlaDto aggregatedRzTypVozidlaDto = new RzTypVozidlaDto();
        List<String> aggregatedRzVozidla = new ArrayList<>();
        List<VozidloTypDto> aggregatedTypVozidla = new ArrayList<>();
    
        for (RzTypVozidlaDto rzTypVozidlaDto : rzTypVozidlaSet) {
            if (rzTypVozidlaDto.getRzVozidla() != null) {
                aggregatedRzVozidla.addAll(rzTypVozidlaDto.getRzVozidla());
            }
            if (rzTypVozidlaDto.getTypVozidla() != null) {
                aggregatedTypVozidla.addAll(rzTypVozidlaDto.getTypVozidla());
            }
        }
    
        aggregatedRzTypVozidlaDto.setRzVozidla(aggregatedRzVozidla);
        aggregatedRzTypVozidlaDto.setTypVozidla(aggregatedTypVozidla);
    
        // Můžete přidat aggregatedRzTypVozidlaDto do výsledné množiny, pokud to dává smysl
        rzTypVozidlaSet.add(aggregatedRzTypVozidlaDto);

    
        return aggregatedRzTypVozidlaDto;
    }

    private Set<RzTypVozidlaDto> parseRzTypVozidlaCsv(MultipartFile file) throws IOException, ParseException, BaseException {
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            Set<RzTypVozidlaDto> rzTypVozidlaSet = new HashSet<>();
            HeaderColumnNameMappingStrategy<RzTypVozidlaCsvRepresentation> strategy =
                new HeaderColumnNameMappingStrategy<>();

            strategy.setType(RzTypVozidlaCsvRepresentation.class);
            CsvToBean<RzTypVozidlaCsvRepresentation> csvToBean = 
                new CsvToBeanBuilder<RzTypVozidlaCsvRepresentation>(reader)
                    .withMappingStrategy(strategy).withIgnoreEmptyLine(true).withIgnoreLeadingWhiteSpace(true).build();

            for (RzTypVozidlaCsvRepresentation csvLine : csvToBean.parse()) {
                RzTypVozidlaDto rzTypVozidla = new RzTypVozidlaDto();


                // Handle multiple values separated by '|'
                if (csvLine.getRzVozidla() != null && csvLine.getRzVozidla().length > 0) {
                    rzTypVozidla.setRzVozidla(List.of(csvLine.getRzVozidla()));
                }

                if (csvLine.getTypVozidla() != null && csvLine.getTypVozidla().length > 0) {
                    List<VozidloTypDto> typVozidlaList = new ArrayList<>();
                    for (String typ : csvLine.getTypVozidla()) {
                        typVozidlaList.add(vratniceService.getVozidloTypByNazev(typ));
                    }
                    rzTypVozidla.setTypVozidla(typVozidlaList);
                }

                // Add to set
                rzTypVozidlaSet.add(rzTypVozidla);
            }
            return rzTypVozidlaSet;
        }
     }

     private Date parseDate(String dateStr) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.parse(dateStr);
    }

    private void validate(@Valid PovoleniVjezduVozidlaDto dto) {
        Set<ConstraintViolation<PovoleniVjezduVozidlaDto>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

}
