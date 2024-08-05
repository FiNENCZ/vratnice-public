package cz.diamo.vratnice_public.dto;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ZavodDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @Size(max = 100, message = "{sapid.max.100}")
    private String sapId;

    @Size(max = 1000, message = "{nazev.max.1000}")
    @NotBlank(message = "{nazev.require}")
    private String nazev;

    private String barvaPozadi;

    private String barvaPisma;

    @NotNull(message = "{aktivita.require}")
    private Boolean aktivita;
}
