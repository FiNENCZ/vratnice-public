package cz.dp.vratnice_public.dto;

import java.io.Serializable;

import cz.dp.vratnice_public.enums.ZadostStavEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ZadostStavDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String nazev;

    public ZadostStavDto(Integer enumValue) {
        setId(enumValue);
    }

    public ZadostStavEnum getStavEnum() {
        return ZadostStavEnum.getZadostStavEnum(getId());
    }
}

