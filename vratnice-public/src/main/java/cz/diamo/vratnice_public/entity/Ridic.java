package cz.diamo.vratnice_public.entity;

import java.io.Serializable;
import java.util.Date;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class Ridic implements Serializable {
    private static final long serialVersionUID = 1L;


    private String idRidic;

    private String jmeno;

    private String prijmeni;

    private String cisloOp;

    private String firma;

    private Date datumPouceni;

    public Ridic(String idRidic) {
        setIdRidic(idRidic);
    }
}
