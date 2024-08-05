package cz.diamo.vratnice_public.dto;

import java.io.Serializable;

import cz.diamo.vratnice_public.enums.StatEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StatDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String nazev;

    public StatEnum getStatEnum() {
        return StatEnum.getStatEnum(getId());
    }
}
