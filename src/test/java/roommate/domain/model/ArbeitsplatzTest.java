package roommate.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roommate.domain.model.Arbeitsplatz;
import roommate.domain.model.Ausstattung;

import static org.assertj.core.api.Assertions.assertThat;

public class ArbeitsplatzTest {

    private Arbeitsplatz arbeitsplatz;
    @Test
    @DisplayName("Eine Ausstattung kann hinzugefügt werden")
    void test1(){
        arbeitsplatz = new Arbeitsplatz(1, "11.11.11");
        Ausstattung ausstattung = new Ausstattung("Monitor", "HDMI");

        arbeitsplatz.addAusstattung(ausstattung);

        assertThat(arbeitsplatz.ausstattungen()).contains(ausstattung);
    }
    @Test
    @DisplayName("Eine Ausstattung kann gelöscht werden")
    void test2(){
        arbeitsplatz = new Arbeitsplatz(1, "11.11.11");
        Ausstattung ausstattung1 = new Ausstattung("Monitor", "HDMI");
        Ausstattung ausstattung2 = new Ausstattung("Drucker", "Farbe");
        arbeitsplatz.addAusstattung(ausstattung1);
        arbeitsplatz.addAusstattung(ausstattung2);

        arbeitsplatz.deleteAusstattung("Monitor", "HDMI");

        assertThat(arbeitsplatz.ausstattungen()).containsOnly(ausstattung2);
    }
}
