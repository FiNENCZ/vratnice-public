package cz.diamo.vratnice_public.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cz.diamo.vratnice_public.enums.ZadostStavEnum;

@Data
@NoArgsConstructor
public class PovoleniVjezduVozidlaDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String idPovoleniVjezduVozidla;

    @NotBlank(message = "{povoleni.vjezdu.vozidla.jmeno_zadatele.require}")
    @Size(max = 30, message = "{povoleni.vjezdu.vozidla.jmeno_zadatele.max.30}")
    private String jmenoZadatele;

    @NotBlank(message = "{povoleni.vjezdu.vozidla.prijmeni_zadatele.require}")
    @Size(max = 30, message = "{povoleni.vjezdu.vozidla.prijmeni_zadatele.max.30}")
    private String prijmeniZadatele;

    @Valid
    private SpolecnostDto spolecnostZadatele;

    private String icoZadatele;

    @Email(message = "{povoleni.vjezdu.vozidla.email.invalid}")
    @NotBlank(message = "{povoleni.vjezdu.vozidla.email.require}")
    private String emailZadatele;

    private String duvodZadosti;

    @NotNull(message = "{povoleni.vjezdu.vozidla.rz_vozidla.require}")
    @NotEmpty(message = "{povoleni.vjezdu.vozidla.rz_vozidla.require}")
    private List<String> rzVozidla;

    @NotNull(message = "{povoleni.vjezdu.vozidla.typ_vozidla.require}")
    @NotEmpty(message = "{povoleni.vjezdu.vozidla.typ_vozidla.require}")
    private List<VozidloTypDto> typVozidla;

    @NotNull(message = "{povoleni.vjezdu.vozidla.zeme_registrace_vozidla.require}")
    private StatDto zemeRegistraceVozidla;

    @Valid
    private RidicDto ridic;

    @Valid
    private SpolecnostDto spolecnostVozidla;

    @NotNull(message = "{povoleni.vjezdu.vozidla.datum_od.require}")
    private Date datumOd;

    @NotNull(message = "{povoleni.vjezdu.vozidla.datum_do.require}")
    private Date datumDo;

    @NotNull(message = "{povoleni.vjezdu.vozidla.zavod.require}")
    private ZavodDto zavod;

    @NotNull(message = "{povoleni.vjezdu.vozidla.lokalita.require}")
    @NotEmpty(message = "{povoleni.vjezdu.vozidla.lokalita.require}")
    private List<LokalitaDto> lokality;

    private Boolean opakovanyVjezd = false;

    private ZadostStavDto stav = new ZadostStavDto(ZadostStavEnum.PRIPRAVENO.getValue());

    @AssertTrue(message = "{povoleni.vjezdu.vozidla.rz_typ_vozidla.require}")
    private boolean isRzVozidlaTypVozidlaCountEqual() {
        if (rzVozidla == null || typVozidla == null) {
            return false;
        }
        return rzVozidla.size() == typVozidla.size();
    }
    
    @AssertTrue(message = "{povoleni.vjezdu.vozidla.datum_od_datum_do}")
    private boolean isDatumOdBeforeDatumDo() {
        if (datumOd == null || datumDo == null) {
            return true; // pokud jsou data null, nechceme aby validace selhala zde
        }
        return !datumDo.before(datumOd);
    }

    @AssertTrue(message = "{povoleni.vjezdu.vozidla.rz_vozidla.unique}")
    private boolean isRzVozidlaUnique() {
        if (rzVozidla == null) {
            return true; // pokud je seznam null, nechceme aby validace selhala zde
        }
        Set<String> uniqueRzVozidla = new HashSet<>(rzVozidla);
        return uniqueRzVozidla.size() == rzVozidla.size();
    }

    @AssertTrue(message = "{povoleni.vjezdu.vozidla.rz_vozidla.require}")
    private boolean isRzVozidlaElementCompleted() {
        if (rzVozidla == null) { 
            return true; // null není validace anotace, použití @NotNull to vyřeší
        }

        for (String element : rzVozidla) {
            if (element == null || element.trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @AssertTrue(message = "{povoleni.vjezdu.vozidla.lokalita.invalid}")
    private boolean isLokalityHaveSameZavod() {
        if (lokality == null || zavod == null) {
            return true; // null není validace anotace, použití @NotNull to vyřeší
        }
        for (LokalitaDto lokalita : lokality) {
            if (!zavod.getId().equals(lokalita.getZavod().getId())) {
                return false;
            }
        }
        return true;
    }

}
