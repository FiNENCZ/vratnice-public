package cz.dp.vratnice_public.csvRepresentation;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PovoleniVjezduVozidlaEnglishCsvRepresentation {

    @CsvBindByName(column = "applicantFirstName")
    private String jmenoZadatele;

    @CsvBindByName(column = "applicantLastName")
    private String prijmeniZadatele;

    @CsvBindByName(column = "applicantCompany")
    private String spolecnostZadatele;

    @CsvBindByName(column = "applicantICO")
    private String icoZadatele;

    @CsvBindByName(column = "applicantEmail")
    private String emailZadatele;

    @CsvBindByName(column = "requestReason")
    private String duvodZadosti;

    @CsvBindByName(column = "vehicleLicensePlates")
    private String[] rzVozidla;

    @CsvBindByName(column = "vehicleTypes")
    private String[] typVozidla;

    @CsvBindByName(column = "vehicleRegistrationCountry")
    private String zemeRegistraceVozidla;

    @CsvBindByName(column = "driverFirstName")
    private String ridic_jmeno;

    @CsvBindByName(column = "driverLastName")
    private String ridic_prijmeni;

    @CsvBindByName(column = "driverIDNumber")
    private String ridic_cisloOp;

    @CsvBindByName(column = "driverCompany")
    private String ridic_firma;

    @CsvBindByName(column = "vehicleCompany")
    private String spolecnostVozidla;

    @CsvBindByName(column = "startDate")
    private String datumOd;

    @CsvBindByName(column = "endDate")
    private String datumDo;

    @CsvBindByName(column = "plantName")
    private String zavodNazev;

    @CsvCustomBindByName(converter = LokalitaSplitConverter.class, column = "locationNames")
    private String[] lokalitaNazvy;

    @CsvBindByName(column = "repeatedEntry")
    private boolean opakovanyVjezd;
}
