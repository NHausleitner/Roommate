package roommate.domain.model;

import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

public record Ausstattung(String name, String spezifikation){

    public Ausstattung(String name, String spezifikation){
        this.name = POLICY.sanitize(name);
        this.spezifikation = POLICY.sanitize(spezifikation);
    }
    private static final PolicyFactory POLICY =
            Sanitizers.FORMATTING.
                    and(Sanitizers.LINKS)
                    .and(Sanitizers.BLOCKS);
}
