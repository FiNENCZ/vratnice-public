package cz.diamo.vratnice_public.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


//import cz.diamo.vratnice_public.constants.Constants;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class PovoleniVjezduVozidla implements Serializable{

    private static final long serialVersionUID = 1L;

    private String idPovoleniVjezduVozidla;


    private String jmenoZadatele;


    private String prijmeniZadatele;


    private String spolecnostZadatele;


    private String icoZadatele;


    private String duvodZadosti;


    private List<String> rzVozidla;


    private List<VozidloTyp> typVozidla;


    private Stat zemeRegistraceVozidla;


    private Ridic ridic;


    private String spolecnostVozidla;


    private Date datumOd;


    private Date datumDo;

    private List<Zavod> zavod;

    private Boolean opakovanyVjezd = false;

    private String stav = "vyžádáno";

    public PovoleniVjezduVozidla(String idPovoleniVjezduVozidla) {
        setIdPovoleniVjezduVozidla(idPovoleniVjezduVozidla);
    }




// Jméno žadatele (povinná položka)
// Příjmení žadatele (povinná položka)
// Společnost žadatele (povinná položka)
// IČO žadatele (nepovinná položka)
// Na základě čeho se žádá (nepovinná položka)
// RZ vozidla – je možné zadat RZ více vozidel a současně i typ vozidel (také možné importovat ze souboru CSV) 
// Typ vozidla (osobní, dodávka, nákladní, speciální)

// Země registrace vozidla (číselník)
// Řidič (nepovinné) – pokud bude řidič zadán, bude předvyplněn při identifikaci dané RZ s možností změny a bude možnost přiložit scan podepsaného prohlášení o dodržování pravidel pro vjezd.
// Společnost, které vozidlo patři (nepovinné)
// Období od - do (u jednorázového vjezdu budou obě data stejná)
// Lokalita (závod) – výběr z číselníku, i vícenásobný
// Důvod vjezdu (volný text)
// Opakovaný vjezd (koloběh) – zatržítko pro zvláštní druh opakovaného vjezdu
// Informaci, že žádost musí být odeslána 3 dny předem (její odeslání ale nebude blokováno, i když nebude termín dodržen)

}
