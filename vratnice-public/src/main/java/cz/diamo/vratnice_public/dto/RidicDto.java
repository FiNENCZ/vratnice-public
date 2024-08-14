package cz.diamo.vratnice_public.dto;

import java.io.Serializable;
import java.util.Date;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RidicDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String idRidic;

    @NotBlank(message = "{ridic.jmeno.require}")
    @Size(max = 50, message = "{ridic.jmeno.max.50}")
    private String jmeno;

    @NotBlank(message = "{ridic.prijmeni.require}")
    @Size(max = 50, message = "{ridic.prijmeni.max.50}")
    private String prijmeni;

    @NotBlank(message = "{ridic.cisloOp.require}")
    @Digits(integer = 9, fraction = 0, message = "{ridic.cisloOp.pattern}")
    private String cisloOp;

    @Valid
    private SpolecnostDto spolecnost;

    private Date datumPouceni;

}
