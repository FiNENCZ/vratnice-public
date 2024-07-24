package cz.diamo.vratnice_public.entity;

import java.io.Serializable;


//import cz.diamo.vratnice_public.constants.Constants;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Lokalita implements Serializable {

    private static final long serialVersionUID = 1L;

    private String idLokalita;
    
    private String nazev;

    private Zavod zavod;

    public Lokalita(String idLokalita) {
        setIdLokalita(idLokalita);
    }
}