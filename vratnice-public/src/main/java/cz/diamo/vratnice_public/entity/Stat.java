package cz.diamo.vratnice_public.entity;

import java.io.Serializable;

//import cz.diamo.vratnice_public.constants.Constants;
import cz.diamo.vratnice_public.enums.StatEnum;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class Stat implements Serializable {
    private static final long serialVersionUID = 1L;


    private Integer idStat;

    private String nazevResx;

    private String nazev;

    public Stat(StatEnum value) {
        setIdStat(value.getValue());
    }

    public StatEnum getStatEnum() {
        return StatEnum.getStatEnum(getIdStat());
    }

}
