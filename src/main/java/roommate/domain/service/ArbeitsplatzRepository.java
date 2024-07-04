package roommate.domain.service;

import org.springframework.data.repository.CrudRepository;
import roommate.domain.model.Arbeitsplatz;

import java.util.Collection;
import java.util.Optional;

public interface ArbeitsplatzRepository extends CrudRepository<Arbeitsplatz, Integer> {
    Collection<Arbeitsplatz> findAll();
    Optional<Arbeitsplatz> findById(Integer id);
    Arbeitsplatz save(Arbeitsplatz arbeitsplatz);
    void deleteById(Integer id);
}
