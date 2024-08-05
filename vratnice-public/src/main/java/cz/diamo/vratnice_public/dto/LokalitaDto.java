package cz.diamo.vratnice_public.dto;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LokalitaDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    @NotBlank(message = "{lokalita.nazev.require}")
    @Size(max = 80, message = "{lokalita.nazev.max.80}")
    private String nazev;

    @NotNull(message = "{lokalita.zavod.require}")
    private ZavodDto zavod;
}
