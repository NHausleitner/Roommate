package roommate.application.service;

import org.springframework.stereotype.Service;
import roommate.domain.model.Arbeitsplatz;
import roommate.domain.model.Ausstattung;
import roommate.domain.service.ArbeitsplatzRepository;
import roommate.domain.service.NichtVorhandenException;

import java.util.List;

@Service
public class RoomMateService {

    private final ArbeitsplatzRepository repository;

    public RoomMateService(ArbeitsplatzRepository repository){
        this.repository = repository;
    }

    public List<Arbeitsplatz> alleArbeitsplaetze() {
        return repository.findAll().stream().sorted().toList();
    }

    public Arbeitsplatz arbeitsplatz(int id){
        return repository.findById(id).orElseThrow(NichtVorhandenException::new);
    }

    public synchronized int arbeitsplatzHinzufuegen(String raum){
        if (raum == null || raum.isBlank()) return -1;
        Arbeitsplatz arbeitsplatz = new Arbeitsplatz(null, raum);
        Arbeitsplatz savedArbeitsplatz = repository.save(arbeitsplatz);
        return savedArbeitsplatz.id();
        }

    public synchronized int ausstattungHinzufuegen(int id, String name, String spezifikation){
        if (name == null || name.isBlank()) return -1;
        Arbeitsplatz arbeitsplatz = arbeitsplatz(id);
        if(arbeitsplatz.ausstattungen().contains(new Ausstattung(name, spezifikation))){
            return -2;
        }
        arbeitsplatz.addAusstattung(new Ausstattung(name, spezifikation));
        repository.save(arbeitsplatz);
        return 1;
        }

    public synchronized void arbeitsplatzLoeschen(int arbeitsplatzId) {
        repository.deleteById(arbeitsplatzId);
    }

    public synchronized void loescheAusstattungNachArbeitsplatzId(Integer arbeitsplatzId, String ausstattungName, String ausstattungSpezifikation) {
        Arbeitsplatz arbeitsplatz = arbeitsplatz(arbeitsplatzId);
        arbeitsplatz.deleteAusstattung(ausstattungName, ausstattungSpezifikation);
        repository.save(arbeitsplatz);
    }
}
