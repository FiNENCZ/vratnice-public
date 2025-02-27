package cz.dp.vratnice_public.dto;

import java.io.Serializable;

import cz.dp.vratnice_public.enums.VozidloTypEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VozidloTypDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String nazev;


    public VozidloTypEnum getTypEnum() {
        return VozidloTypEnum.getVozidloTypEnum(getId());
    }

}
