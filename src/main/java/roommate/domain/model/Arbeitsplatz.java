package roommate.domain.model;

import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Arbeitsplatz implements Comparable<Arbeitsplatz>{

    @Id
    private final Integer id;
    private final String raum;
    private final List<Ausstattung> ausstattungen;

    @PersistenceCreator
    public Arbeitsplatz(Integer id, String raum, List<Ausstattung> ausstattungen){
        this.id = id;
        this.raum = POLICY.sanitize(raum);
        this.ausstattungen = ausstattungen;
    }

    public Arbeitsplatz(Integer id, String raum){
        this(id, raum, new ArrayList<>());
    }

    private static final PolicyFactory POLICY =
            Sanitizers.FORMATTING.
                    and(Sanitizers.LINKS)
                    .and(Sanitizers.BLOCKS);

    @Override
    public int compareTo(Arbeitsplatz other) {
        return id.compareTo(other.id);
    }

    public Integer id() {
        return id;
    }

    public String raum() {
        return raum;
    }

    public List<Ausstattung> ausstattungen() {
        return ausstattungen;
    }

    public void addAusstattung(Ausstattung ausstattung) {
        ausstattungen.add(ausstattung);
    }

    public void deleteAusstattung(String name, String spezifikation) {
        List<Ausstattung> ausstattungenZuEntfernen = new ArrayList<>();
        for (Ausstattung ausstattung : ausstattungen()) {
            if (Objects.equals(ausstattung.name(), name) && Objects.equals(ausstattung.spezifikation(), spezifikation)) {
                ausstattungenZuEntfernen.add(ausstattung);
            }
        }
        ausstattungen.removeAll(ausstattungenZuEntfernen);
    }

    @Override
    public String toString() {
        return "Begriff[" +
                "id=" + id + ", " +
                "raum=" + raum + ']';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Arbeitsplatz arbeitsplatz = (Arbeitsplatz) o;
        return Objects.equals(id, arbeitsplatz.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
