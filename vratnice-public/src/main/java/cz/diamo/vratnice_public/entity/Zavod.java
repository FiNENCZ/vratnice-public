package cz.diamo.vratnice_public.entity;

import java.io.Serializable;
import java.sql.Timestamp;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Zavod implements Serializable {
    private static final long serialVersionUID = 1L;

    private String idZavod;

    private String sapId;

    private String nazev;


    private String barvaPozadi;


    private String barvaPisma;

    private String poznamka;

    private Boolean aktivita = true;

    private Timestamp casZmn;


    private String zmenuProvedl;

    public Zavod(String idZavod) {
        setIdZavod(idZavod);
    }
}