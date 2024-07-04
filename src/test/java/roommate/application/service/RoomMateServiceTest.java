package roommate.application.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.jdbc.Sql;
import roommate.application.service.RoomMateService;
import roommate.domain.model.Arbeitsplatz;
import roommate.domain.model.Ausstattung;
import roommate.domain.service.ArbeitsplatzRepository;
import roommate.domain.service.NichtVorhandenException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@JdbcTest
public class RoomMateServiceTest {
    ArbeitsplatzRepository repository = mock(ArbeitsplatzRepository.class);

    @Test
    @Sql("clear_data.sql")
    @DisplayName("Ein Arbeitsplatz kann erstellt werden")
    void test1(){
        RoomMateService service = new RoomMateService(repository);
        when(repository.save(any())).thenReturn(new Arbeitsplatz(1, "11.11.11"));

        service.arbeitsplatzHinzufuegen("22.22.22");

        ArgumentCaptor<Arbeitsplatz> captor = ArgumentCaptor.forClass(Arbeitsplatz.class);
        verify(repository).save(captor.capture());
        Arbeitsplatz savedArbeitsplatz = captor.getValue();

        assertThat(savedArbeitsplatz.raum()).isEqualTo("22.22.22");
    }

    @Test
    @Sql("clear_data.sql")
    @DisplayName("Ein Arbeitsplatz ohne Raum wird nicht erstellt")
    void test2(){
        RoomMateService service = new RoomMateService(repository);

        service.arbeitsplatzHinzufuegen("");

        verify(repository, never()).save(any());
    }
    @Test
    @Sql("clear_data.sql")
    @DisplayName("Ausstattungen können hinzugefügt werden")
    void test3(){

        Arbeitsplatz arbeitsplatz = new Arbeitsplatz(100, "11.11.11");
        when(repository.findById(anyInt())).thenReturn(Optional.of(arbeitsplatz));
        RoomMateService service = new RoomMateService(repository);
        service.ausstattungHinzufuegen(100, "Monitor", "HDMI");

        ArgumentCaptor<Arbeitsplatz> captor = ArgumentCaptor.forClass(Arbeitsplatz.class);
        verify(repository).save(captor.capture());
        Arbeitsplatz savedArbeitsplatz = captor.getValue();

        assertThat(savedArbeitsplatz.ausstattungen())
                .contains(new Ausstattung("Monitor", "HDMI"));
    }
    @Test
    @Sql("clear_data.sql")
    @DisplayName("Bei 2 identischen Ausstattungen wird nur eine hinzugefügt")
    void test4(){
        RoomMateService service = new RoomMateService(repository);
        Arbeitsplatz arbeitsplatz = new Arbeitsplatz(1, "11.11.11");
        when(repository.save(any())).thenReturn(arbeitsplatz);
        when(repository.findById(anyInt())).thenReturn(Optional.of(arbeitsplatz));
        service.arbeitsplatzHinzufuegen("11.11.11");

        service.ausstattungHinzufuegen(1,"Monitor","60hz");
        service.ausstattungHinzufuegen(1,"Monitor","60hz");

        verify(repository, times(2)).save(any());
    }
    @Test
    @Sql("clear_data.sql")
    @DisplayName("Bei 2 Ausstattungen mit gleichem Namen, aber unterschiedlichen Spezifikationen werden beide hinzugefügt")
    void test5(){
        RoomMateService service = new RoomMateService(repository);
        Arbeitsplatz arbeitsplatz = new Arbeitsplatz(1, "11.11.11");
        when(repository.save(any())).thenReturn(arbeitsplatz);
        when(repository.findById(anyInt())).thenReturn(Optional.of(arbeitsplatz));
        service.arbeitsplatzHinzufuegen("11.11.11");

        service.ausstattungHinzufuegen(1, "Monitor","60hz");
        service.ausstattungHinzufuegen(1, "Monitor","");

        assertThat(service.arbeitsplatz(1).ausstattungen()).contains(new Ausstattung("Monitor","60hz"), new Ausstattung("Monitor",""));
    }
    @Test
    @Sql("clear_data.sql")
    @DisplayName("Eine Ausstattung ohne Namen wird nicht hinzugefügt")
    void test6(){
        RoomMateService service = new RoomMateService(repository);
        Arbeitsplatz arbeitsplatz = new Arbeitsplatz(1, "11.11.11");
        when(repository.save(any())).thenReturn(arbeitsplatz);
        when(repository.findById(anyInt())).thenReturn(Optional.of(arbeitsplatz));
        service.arbeitsplatzHinzufuegen("11.11.11");

        service.ausstattungHinzufuegen(1, "", "test");

        assertThat(service.arbeitsplatz(1).ausstattungen()).isEmpty();
    }
    @Test
    @Sql("clear_data.sql")
    @DisplayName("Ein vorhandener Arbeitsplatz kann gefunden werden")
    void test7(){
        RoomMateService service = new RoomMateService(repository);
        Arbeitsplatz arbeitsplatz = new Arbeitsplatz(1, "11.11.11");
        when(repository.save(any())).thenReturn(arbeitsplatz);
        when(repository.findById(anyInt())).thenReturn(Optional.of(arbeitsplatz));
        service.arbeitsplatzHinzufuegen("11.11.11");

        Arbeitsplatz neuArbeitsplatz = service.arbeitsplatz(1);

        assertThat(neuArbeitsplatz).isEqualTo(arbeitsplatz);
    }
    @Test
    @Sql("clear_data.sql")
    @DisplayName("Ein nicht vorhandener Arbeitsplatz wird nicht gefunden")
    void test8(){
        RoomMateService service = new RoomMateService(repository);
        when(repository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(NichtVorhandenException.class, () -> service.arbeitsplatz(1));
    }
    @Test
    @Sql("clear_data.sql")
    @DisplayName("Ein vorhandener Arbeitsplatz kann gelöscht werden")
    void test9(){
        RoomMateService service = new RoomMateService(repository);
        Arbeitsplatz arbeitsplatz = new Arbeitsplatz(1, "11.11.11");
        when(repository.save(any())).thenReturn(arbeitsplatz);
        when(repository.findById(anyInt())).thenReturn(Optional.of(arbeitsplatz));
        service.arbeitsplatzHinzufuegen("11.11.11");

        service.arbeitsplatzLoeschen(arbeitsplatz.id());

        verify(repository, times(1)).deleteById(1);
    }
    @Test
    @Sql("clear_data.sql")
    @DisplayName("Lösche eine vorhandene Ausstattung von einem Arbeitsplatz")
    void test11(){
        RoomMateService service = new RoomMateService(repository);
        Arbeitsplatz arbeitsplatz = new Arbeitsplatz(1, "11.11.11");
        when(repository.save(any())).thenReturn(arbeitsplatz);
        when(repository.findById(anyInt())).thenReturn(Optional.of(arbeitsplatz));
        service.arbeitsplatzHinzufuegen("11.11.11");
        service.ausstattungHinzufuegen(1, "Monitor", "HDMI");

        service.loescheAusstattungNachArbeitsplatzId(arbeitsplatz.id(), "Monitor", "HDMI");

        assertThat(arbeitsplatz.ausstattungen().size()).isEqualTo(0);
    }

    @Test
    @Sql("clear_data.sql")
    @DisplayName("Wenn eine nicht vorhandene Ausstattung von einem Arbeitsplatz gelöscht wird, wird nichts gelöscht")
    void test12(){
        RoomMateService service = new RoomMateService(repository);
        Arbeitsplatz arbeitsplatz = new Arbeitsplatz(1, "11.11.11");
        when(repository.save(any())).thenReturn(arbeitsplatz);
        when(repository.findById(anyInt())).thenReturn(Optional.of(arbeitsplatz));
        service.arbeitsplatzHinzufuegen("11.11.11");
        service.ausstattungHinzufuegen(1, "Monitor", "HDMI");

        service.loescheAusstattungNachArbeitsplatzId(arbeitsplatz.id(), "Drucker", "Farbe");

        assertThat(arbeitsplatz.ausstattungen().size()).isEqualTo(1);
    }

}
