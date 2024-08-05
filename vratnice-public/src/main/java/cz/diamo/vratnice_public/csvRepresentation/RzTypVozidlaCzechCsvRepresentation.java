package cz.diamo.vratnice_public.csvRepresentation;

import com.opencsv.bean.CsvBindByName;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RzTypVozidlaCzechCsvRepresentation {

    @CsvBindByName(column = "rzVozidla")
    private String[] rzVozidla;

    @CsvBindByName(column = "typVozidla")
    private String[] typVozidla;

}
