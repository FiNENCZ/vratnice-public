package cz.diamo.vratnice_public.csvRepresentation;

import com.opencsv.bean.CsvBindByName;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RzTypVozidlaEnglishCsvRepresentation {

    @CsvBindByName(column = "vehicleLicensePlates")
    private String[] rzVozidla;

    @CsvBindByName(column = "vehicleTypes")
    private String[] typVozidla;

}
