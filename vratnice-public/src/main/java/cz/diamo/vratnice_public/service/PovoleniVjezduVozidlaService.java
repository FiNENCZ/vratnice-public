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
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;

import cz.diamo.vratnice_public.csvRepresentation.PovoleniVjezduVozidlaCzechCsvRepresentation;
import cz.diamo.vratnice_public.csvRepresentation.PovoleniVjezduVozidlaEnglishCsvRepresentation;
import cz.diamo.vratnice_public.csvRepresentation.RzTypVozidlaCzechCsvRepresentation;
import cz.diamo.vratnice_public.csvRepresentation.RzTypVozidlaEnglishCsvRepresentation;
import cz.diamo.vratnice_public.dto.LokalitaDto;
import cz.diamo.vratnice_public.dto.PovoleniVjezduVozidlaDto;
import cz.diamo.vratnice_public.dto.RidicDto;
import cz.diamo.vratnice_public.dto.RzTypVozidlaDto;
import cz.diamo.vratnice_public.dto.SpolecnostDto;
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

    @Autowired
    private MessageSource messageSource;


    public PovoleniVjezduVozidlaDto create(PovoleniVjezduVozidlaDto povoleniVjezduVozidlaDto) {
        PovoleniVjezduVozidlaDto savedPovoleni =  vratniceService.savePovoleni(povoleniVjezduVozidlaDto);
        return savedPovoleni;
    }

    public Set<PovoleniVjezduVozidlaDto>  processPovoleniCsvData(MultipartFile file) throws IOException, ParseException, BaseException {
        Set<PovoleniVjezduVozidlaDto> povoleniVjezduVozidlaSet = new HashSet<>();

        String header = detectCsvHeader(file);
    
        if (header.contains("vehicleLicensePlates") && header.contains("vehicleTypes")) {
            povoleniVjezduVozidlaSet = parsePovoleniEnglishCsv(file);
        } else if (header.contains("rzVozidla") && header.contains("typVozidla")) {
            povoleniVjezduVozidlaSet = parsePovoleniCzechCsv(file);
        } else {
            throw new IllegalArgumentException(messageSource.getMessage("povoleni.vjezdu.vozidla.csv.invalid", null, LocaleContextHolder.getLocale()));
        }

        Set<PovoleniVjezduVozidlaDto> savedDtos = new HashSet<>();

        for (PovoleniVjezduVozidlaDto dto : povoleniVjezduVozidlaSet) {
            PovoleniVjezduVozidlaDto savedEntity = create(dto); 
            savedDtos.add(savedEntity);
        }
    

        return savedDtos;
    }
    

    private Set<PovoleniVjezduVozidlaDto> parsePovoleniCzechCsv(MultipartFile file) throws IOException, ParseException, BaseException {
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            Set<PovoleniVjezduVozidlaDto> povoleniVjezduVozidlaSet = new HashSet<>();
            HeaderColumnNameMappingStrategy<PovoleniVjezduVozidlaCzechCsvRepresentation> strategy =
                new HeaderColumnNameMappingStrategy<>();

            strategy.setType(PovoleniVjezduVozidlaCzechCsvRepresentation.class);
            CsvToBean<PovoleniVjezduVozidlaCzechCsvRepresentation> csvToBean = 
                new CsvToBeanBuilder<PovoleniVjezduVozidlaCzechCsvRepresentation>(reader)
                    .withMappingStrategy(strategy).withIgnoreEmptyLine(true).withIgnoreLeadingWhiteSpace(true).build();

            for (PovoleniVjezduVozidlaCzechCsvRepresentation csvLine : csvToBean.parse()) {
                PovoleniVjezduVozidlaDto povoleniVjezduVozidlaDto = new PovoleniVjezduVozidlaDto();

                // Basic fields
                povoleniVjezduVozidlaDto.setJmenoZadatele(csvLine.getJmenoZadatele());
                povoleniVjezduVozidlaDto.setPrijmeniZadatele(csvLine.getPrijmeniZadatele());
                povoleniVjezduVozidlaDto.setSpolecnostZadatele(vratniceService.saveSpolecnost(new SpolecnostDto(csvLine.getSpolecnostZadatele())));
                povoleniVjezduVozidlaDto.setIcoZadatele(csvLine.getIcoZadatele());
                povoleniVjezduVozidlaDto.setEmailZadatele(csvLine.getEmailZadatele());
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
                    if (existedRidic == null || existedRidic.getIdRidic() == null) {
                        ridicDto.setJmeno(csvLine.getRidic_jmeno());
                        ridicDto.setPrijmeni(csvLine.getRidic_prijmeni());
                        ridicDto.setCisloOp(csvLine.getRidic_cisloOp());
                        ridicDto.setSpolecnost(vratniceService.saveSpolecnost(new SpolecnostDto(csvLine.getRidic_firma())));
                    } else {
                        ridicDto = existedRidic;
                    }

                    povoleniVjezduVozidlaDto.setRidic(ridicDto);
                }

                povoleniVjezduVozidlaDto.setSpolecnostVozidla(vratniceService.saveSpolecnost(new SpolecnostDto(csvLine.getSpolecnostVozidla())));
                povoleniVjezduVozidlaDto.setDatumOd(parseDate(csvLine.getDatumOd()));
                povoleniVjezduVozidlaDto.setDatumDo(parseDate(csvLine.getDatumDo()));

                List<ZavodDto> listOfExistedZavod = vratniceService.seznamZavodu(true, null);

                String zavodNazev = csvLine.getZavodNazev();
                if (zavodNazev != null) {
                    for (ZavodDto zavodDto: listOfExistedZavod) {
                        if (zavodDto.getNazev().equals(zavodNazev)) {
                            povoleniVjezduVozidlaDto.setZavod(zavodDto);
                            break;
                        }
                    }
                    
                }

                List<LokalitaDto> listOfExistedLokalit = vratniceService.seznamLokalit(null);
            
                if (csvLine.getLokalitaNazvy() != null && csvLine.getLokalitaNazvy().length > 0) {
                    List<LokalitaDto> lokalitaList = new ArrayList<>();
                    for (String lokalitaNazev : csvLine.getLokalitaNazvy()) {
                        for (LokalitaDto lokalitaDto: listOfExistedLokalit) {
                            if (lokalitaDto.getNazev().equals(lokalitaNazev)) {
                                lokalitaList.add(lokalitaDto);
                            }
                        }
                    }
                    povoleniVjezduVozidlaDto.setLokality(lokalitaList);
                }

                povoleniVjezduVozidlaDto.setOpakovanyVjezd(csvLine.isOpakovanyVjezd());

                validate(povoleniVjezduVozidlaDto);

                // Add to set
                povoleniVjezduVozidlaSet.add(povoleniVjezduVozidlaDto);
            }
            return povoleniVjezduVozidlaSet;
        }
     }

     private Set<PovoleniVjezduVozidlaDto> parsePovoleniEnglishCsv(MultipartFile file) throws IOException, ParseException, BaseException {
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            Set<PovoleniVjezduVozidlaDto> povoleniVjezduVozidlaSet = new HashSet<>();
            HeaderColumnNameMappingStrategy<PovoleniVjezduVozidlaEnglishCsvRepresentation> strategy =
                new HeaderColumnNameMappingStrategy<>();

            strategy.setType(PovoleniVjezduVozidlaEnglishCsvRepresentation.class);
            CsvToBean<PovoleniVjezduVozidlaEnglishCsvRepresentation> csvToBean = 
                new CsvToBeanBuilder<PovoleniVjezduVozidlaEnglishCsvRepresentation>(reader)
                    .withMappingStrategy(strategy).withIgnoreEmptyLine(true).build();

            for (PovoleniVjezduVozidlaEnglishCsvRepresentation csvLine : csvToBean.parse()) {
                PovoleniVjezduVozidlaDto povoleniVjezduVozidlaDto = new PovoleniVjezduVozidlaDto();

                // Basic fields
                povoleniVjezduVozidlaDto.setJmenoZadatele(csvLine.getJmenoZadatele());
                povoleniVjezduVozidlaDto.setPrijmeniZadatele(csvLine.getPrijmeniZadatele());
                povoleniVjezduVozidlaDto.setSpolecnostZadatele(vratniceService.saveSpolecnost(new SpolecnostDto(csvLine.getSpolecnostZadatele())));
                povoleniVjezduVozidlaDto.setIcoZadatele(csvLine.getIcoZadatele());
                povoleniVjezduVozidlaDto.setEmailZadatele(csvLine.getEmailZadatele());
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
                        ridicDto.setSpolecnost(vratniceService.saveSpolecnost(new SpolecnostDto(csvLine.getRidic_firma())));
                    } else {
                        ridicDto = existedRidic;
                    }

                    povoleniVjezduVozidlaDto.setRidic(ridicDto);
                }

                povoleniVjezduVozidlaDto.setSpolecnostVozidla(vratniceService.saveSpolecnost(new SpolecnostDto(csvLine.getSpolecnostVozidla())));
                povoleniVjezduVozidlaDto.setDatumOd(parseDate(csvLine.getDatumOd()));
                povoleniVjezduVozidlaDto.setDatumDo(parseDate(csvLine.getDatumDo()));

                List<ZavodDto> listOfExistedZavod = vratniceService.seznamZavodu(true, null);

                String zavodNazev = csvLine.getZavodNazev();
                if (zavodNazev != null) {
                    for (ZavodDto zavodDto: listOfExistedZavod) {
                        if (zavodDto.getNazev().equals(zavodNazev)) {
                            povoleniVjezduVozidlaDto.setZavod(zavodDto);
                            break;
                        }
                    }
                    
                }

                List<LokalitaDto> listOfExistedLokalit = vratniceService.seznamLokalit(null);
            
                if (csvLine.getLokalitaNazvy() != null && csvLine.getLokalitaNazvy().length > 0) {
                    List<LokalitaDto> lokalitaList = new ArrayList<>();
                    for (String lokalitaNazev : csvLine.getLokalitaNazvy()) {
                        for (LokalitaDto lokalitaDto: listOfExistedLokalit) {
                            if (lokalitaDto.getNazev().equals(lokalitaNazev)) {
                                lokalitaList.add(lokalitaDto);
                            }
                        }
                    }
                    povoleniVjezduVozidlaDto.setLokality(lokalitaList);
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
        Set<RzTypVozidlaDto> rzTypVozidlaSet = new HashSet<>();

        String header = detectCsvHeader(file);

        if (header.contains("vehicleLicensePlates") && header.contains("vehicleTypes")) {
            rzTypVozidlaSet = parseRzTypVozidlaEnglishCsv(file);
        } else if (header.contains("rzVozidla") && header.contains("typVozidla")) {
            rzTypVozidlaSet = parseRzTypVozidlaCzechCsv(file);
        } else {
            throw new IllegalArgumentException(messageSource.getMessage("povoleni.vjezdu.vozidla.csv.invalid", null, LocaleContextHolder.getLocale()));
        }
    
    

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

    private Set<RzTypVozidlaDto> parseRzTypVozidlaCzechCsv(MultipartFile file) throws IOException, ParseException, BaseException {
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            Set<RzTypVozidlaDto> rzTypVozidlaSet = new HashSet<>();
            HeaderColumnNameMappingStrategy<RzTypVozidlaCzechCsvRepresentation> strategy =
                new HeaderColumnNameMappingStrategy<>();

            strategy.setType(RzTypVozidlaCzechCsvRepresentation.class);
            CsvToBean<RzTypVozidlaCzechCsvRepresentation> csvToBean = 
                new CsvToBeanBuilder<RzTypVozidlaCzechCsvRepresentation>(reader)
                    .withMappingStrategy(strategy).withIgnoreEmptyLine(true).withIgnoreLeadingWhiteSpace(true).build();

            for (RzTypVozidlaCzechCsvRepresentation csvLine : csvToBean.parse()) {
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

     private Set<RzTypVozidlaDto> parseRzTypVozidlaEnglishCsv(MultipartFile file) throws IOException, ParseException, BaseException {
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            Set<RzTypVozidlaDto> rzTypVozidlaSet = new HashSet<>();
            HeaderColumnNameMappingStrategy<RzTypVozidlaEnglishCsvRepresentation> strategy =
                new HeaderColumnNameMappingStrategy<>();

            strategy.setType(RzTypVozidlaEnglishCsvRepresentation.class);
            CsvToBean<RzTypVozidlaEnglishCsvRepresentation> csvToBean = 
                new CsvToBeanBuilder<RzTypVozidlaEnglishCsvRepresentation>(reader)
                    .withMappingStrategy(strategy).withIgnoreEmptyLine(true).withIgnoreLeadingWhiteSpace(true).build();

            for (RzTypVozidlaEnglishCsvRepresentation csvLine : csvToBean.parse()) {
                RzTypVozidlaDto rzTypVozidla = new RzTypVozidlaDto();
  
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

    private String detectCsvHeader(MultipartFile file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String headerLine = reader.readLine(); // Read the header line
            return headerLine != null ? headerLine : "";
        }
    }

}
