package cz.diamo.vratnice_public.entity;

import java.io.Serializable;

//import cz.diamo.vratnice_public.constants.Constants;
import cz.diamo.vratnice_public.enums.VozidloTypEnum;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class VozidloTyp implements Serializable {
    private static final long serialVersionUID = 1L;


    private Integer idVozidloTyp;


    private String nazevResx;


    private String nazev;

    public VozidloTyp(VozidloTypEnum value) {
        setIdVozidloTyp(value.getValue());
    }

    public VozidloTypEnum getVozidloTypEnum() {
        return VozidloTypEnum.getVozidloTypEnum(getIdVozidloTyp());
    }
}
