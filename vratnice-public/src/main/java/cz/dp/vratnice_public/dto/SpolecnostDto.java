package cz.dp.vratnice_public.dto;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SpolecnostDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @NotBlank(message = "{spolecnost.nazev.require}")
    @Size(max = 80, message = "{spolecnost.nazev.max.120}")
    private String nazev; 

    public SpolecnostDto(String nazev) {
        setNazev(nazev);
    }
}
