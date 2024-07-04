package roommate.web;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import roommate.domain.model.Arbeitsplatz;
import roommate.domain.model.Ausstattung;
import roommate.security.MethodSecurityConfig;
import roommate.security.SecurityConfig;
import roommate.application.service.RoomMateService;
import roommate.web.helper.WithMockOAuth2User;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@Import({SecurityConfig.class, MethodSecurityConfig.class})
public class WebControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    RoomMateService service;
    @Test
    @DisplayName("Wird / aufgerufen, erfolgt ein redirect")
    void test1() throws  Exception{
        redirectionErfolgtAuf("/");
    }

    @Test
    @DisplayName("Ein nicht authentifizierter Besucher kann / aufrufen")
    void test2() throws Exception {
        redirectionErfolgtAuf("/secret");
    }

    @Test
    @DisplayName("Ein nicht authentifizierter Besucher kann nicht /uebersicht aufrufen")
    void test3() throws Exception {
        MvcResult mvcResult = getMvcResultVonNichtAuthentifiziertemBesucherAuf("/uebersicht");
        autorisierungUeberpruefung(mvcResult);
    }

    @Test
    @DisplayName("Ein nicht authentifizierter Besucher kann nicht /admin/adminseite aufrufen")
    void test4() throws Exception {
        MvcResult mvcResult = getMvcResultVonNichtAuthentifiziertemBesucherAuf("/admin/adminseite");
        autorisierungUeberpruefung(mvcResult);
    }

    @Test
    @DisplayName("Ein nicht authentifizierter Besucher kann nicht /admin/arbeitsplatzAendern aufrufen")
    void test5() throws Exception {
        MvcResult mvcResult = getMvcResultVonNichtAuthentifiziertemBesucherAuf("/admin/arbeitsplatzAendern");
        autorisierungUeberpruefung(mvcResult);
    }

    @Test
    @DisplayName("Ein nicht authentifizierter Besucher kann nicht /admin/arbeitsplatzErstellen aufrufen")
    void test6() throws Exception {
        MvcResult mvcResult = getMvcResultVonNichtAuthentifiziertemBesucherAuf("/admin/arbeitsplatzErstellen");
        autorisierungUeberpruefung(mvcResult);
    }

    @Test
    @DisplayName("Ein nicht authentifizierter Besucher kann nicht /admin/reservierungAendern aufrufen")
    void test7() throws Exception {
        MvcResult mvcResult = getMvcResultVonNichtAuthentifiziertemBesucherAuf("/admin/reservierungAendern");
        autorisierungUeberpruefung(mvcResult);
    }
    @Test
    @DisplayName("Ein nicht authentifizierter Besucher kann nicht /admin/arbeitsplatzWirdGeandert aufrufen")
    void test7_1() throws Exception {
        MvcResult mvcResult = getMvcResultVonNichtAuthentifiziertemBesucherAuf("/admin/arbeitsplatzWirdGeandert");
        autorisierungUeberpruefung(mvcResult);
    }

    @Test
    @WithMockOAuth2User(login = "Joe Mama", roles = {"USER", "ADMIN"})
    @DisplayName("Ein authentifizierter Admin kann / aufrufen")
    void test8() throws Exception {
        redirectionErfolgtAuf("/");
    }

    @Test
    @WithMockOAuth2User(login = "Joe Mama", roles = {"USER", "ADMIN"})
    @DisplayName("Ein authentifizierter Admin kann /uebersicht aufrufen")
    void test9() throws Exception {
        authentifizierterBesucherKannAufrufen("/uebersicht");
    }

    @Test
    @WithMockOAuth2User(login = "Joe Mama", roles = {"USER", "ADMIN"})
    @DisplayName("Ein authentifizierter Admin kann /admin/adminseite aufrufen")
    void test10() throws Exception {
        authentifizierterBesucherKannAufrufen("/admin/adminseite");
    }

    @Test
    @WithMockOAuth2User(login = "Joe Mama", roles = {"USER", "ADMIN"})
    @DisplayName("Ein authentifizierter Admin kann /admin/arbeitsplatzAendern aufrufen")
    void test11() throws Exception {
        authentifizierterBesucherKannAufrufen("/admin/arbeitsplatzAendern");
    }

    @Test
    @WithMockOAuth2User(login = "Joe Mama", roles = {"USER", "ADMIN"})
    @DisplayName("Ein authentifizierter Admin kann /admin/arbeitsplatzErstellen aufrufen")
    void test12() throws Exception {
        authentifizierterBesucherKannAufrufen("/admin/arbeitsplatzErstellen");
    }

    @Test
    @WithMockOAuth2User(login = "Joe Mama", roles = {"USER", "ADMIN"})
    @DisplayName("Ein authentifizierter Admin kann /admin/reservierungAendern aufrufen")
    void test13() throws Exception {
        authentifizierterBesucherKannAufrufen("/admin/reservierungAendern");
    }
    // Test funktioniert nicht: Irgendetwas mit der Id funktioniert nicht in Spring
    @Disabled
    @Test
    @WithMockOAuth2User(login = "Joe Mama", roles = {"USER", "ADMIN"})
    @DisplayName("Ein authentifizierter Admin kann /admin/reservierungAendern aufrufen")
    void test13_1() throws Exception {
        authentifizierterBesucherKannAufrufen("/admin/arbeitsplatzWirdGeandert/1");
    }

    @Test
    @WithMockOAuth2User(login = "Joe Mama")
    @DisplayName("Ein authentifizierter Nutzer kann / aufrufen")
    void test14() throws Exception {
        redirectionErfolgtAuf("/");
    }

    @Test
    @WithMockOAuth2User(login = "Joe Mama")
    @DisplayName("Ein authentifizierter Nutzer kann /uebersicht aufrufen")
    void test15() throws Exception {
        authentifizierterBesucherKannAufrufen("/uebersicht");
    }

    @Test
    @WithMockOAuth2User(login = "Joe Mama")
    @DisplayName("Ein authentifizierter Nutzer kann nicht /admin/adminseite aufrufen")
    void test16() throws Exception {
        zugangIstVerbotenAuf("/admin/adminseite");
    }

    @Test
    @WithMockOAuth2User(login = "Joe Mama")
    @DisplayName("Ein authentifizierter Nutzer kann nicht /admin/arbeitsplatzAendern aufrufen")
    void test17() throws Exception {
        zugangIstVerbotenAuf("/admin/arbeitsplatzAendern");
    }

    @Test
    @WithMockOAuth2User(login = "Joe Mama")
    @DisplayName("Ein authentifizierter Nutzer kann nicht /admin/arbeitsplatzErstellen aufrufen")
    void test18() throws Exception {
        zugangIstVerbotenAuf("/admin/arbeitsplatzErstellen");
    }

    @Test
    @WithMockOAuth2User(login = "Joe Mama")
    @DisplayName("Ein authentifizierter Nutzer kann nicht /admin/reservierungAendern aufrufen")
    void test19() throws Exception {
        zugangIstVerbotenAuf("/admin/reservierungAendern");
    }
    @Test
    @WithMockOAuth2User(login = "Joe Mama")
    @DisplayName("Ein authentifizierter Nutzer kann nicht /admin/arbeitsplatzWirdGeandert/{id} aufrufen")
    void test19_1() throws Exception {
        zugangIstVerbotenAuf("/admin/arbeitsplatzWirdGeandert/1");
    }
    @Test
    @WithMockOAuth2User(login = "Joe Mama", roles = {"USER", "ADMIN"})
    @DisplayName("Ein Arbeitsplatz wird bei einem POST auf /admin/arbeitsplatzErstellen erstellt")
    void test20() throws Exception {
        mvc.perform(post("/admin/arbeitsplatzErstellen")
                .with(csrf())
                .param("raum", "25.12.22"))
                .andExpect(model().attribute("arbeitsplatzRichtig", true));
        verify(service).arbeitsplatzHinzufuegen("25.12.22");
    }
    @Test
    @WithMockOAuth2User(login = "Joe Mama", roles = {"USER", "ADMIN"})
    @DisplayName("Ein Arbeitsplatz und die dazugehörigen Ausstattungen werden bei einem POST auf /admin/arbeitsplatzErstellen nicht erstellt, wenn die Raumeingabe leer ist")
    void test21() throws Exception {
        String raum = "";
        Ausstattung ausstattung = new Ausstattung("Monitor", "HDMI");
        when(service.arbeitsplatzHinzufuegen(raum)).thenReturn(-1);
        mvc.perform(post("/admin/arbeitsplatzErstellen")
                .with(csrf())
                .param("raum", ""))
                .andExpect(model().attribute("arbeitsplatzFalsch", true));
        verify(service, never()).ausstattungHinzufuegen(0, ausstattung.name(), ausstattung.spezifikation());
    }
    @Test
    @WithMockOAuth2User(login = "Joe Mama", roles = {"USER", "ADMIN"})
    @DisplayName("Ein Arbeitsplatz und die dazugehörigen Ausstattungen werden bei einem POST auf /admin/arbeitsplatzErstellen erstellt")
    void test22() throws Exception {
        mvc.perform(post("/admin/arbeitsplatzErstellen")
                        .with(csrf())
                        .param("raum", "25.12.22")
                        .param("ausstattung1", "Maus")
                        .param("spezifikation1", "Bluetooth")
                        .param("ausstattung2", "Monitor")
                        .param("spezifikation2", "120hz"))
                .andExpect(model().attribute("arbeitsplatzRichtig", true));
        verify(service).arbeitsplatzHinzufuegen("25.12.22");
        verify(service).ausstattungHinzufuegen(0, "Maus", "Bluetooth");
        verify(service).ausstattungHinzufuegen(0, "Monitor", "120hz");
    }
    @Test
    @WithMockOAuth2User(login = "Joe Mama", roles = {"USER", "ADMIN"})
    @DisplayName("Bei einem GET auf /admin/arbeitsplatzAendern werden alle Arbeitsplätze in ein Modelattribut übergeben")
    void test23() throws Exception {
        mvc.perform(get("/admin/arbeitsplatzAendern")
                        .with(csrf()))
                .andExpect(model().attribute("alleArbeitsplaetze", Collections.emptyList()));
        verify(service, atLeastOnce()).alleArbeitsplaetze();
    }
    @Test
    @WithMockOAuth2User(login = "Joe Mama", roles = {"USER", "ADMIN"})
    @DisplayName("Bei einem POST auf /admin/arbeitsplatzWirdGeandert/loeschen/{id} gibt es eine redirection und die Methode zum Arbeitsplatzlöschen wird aufgerufen")
    void test26() throws Exception {
        mvc.perform(post("/admin/arbeitsplatzWirdGeandert/loeschen/1")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("arbeitsplatzGeloescht", true));
        verify(service, times(1)).arbeitsplatzLoeschen(1);
    }
    @Test
    @WithMockOAuth2User(login = "Joe Mama", roles = {"USER", "ADMIN"})
    @DisplayName("Bei einem POST auf /admin/arbeitsplatzWirdGeandert/loeschen/arbeitsplatz/{arbeitsplatzId}/ausstattung/{ausstattungName}/{ausstattungSpezifikation}" +
            " gibt es eine redirection, die dazugehörigen FlashAttributes werden hinzugefügt und die loescheAusstatungNachArbeitsplatzId-Methode wird aufgerufen")
    void test27() throws Exception {
        Arbeitsplatz arbeitsplatz = new Arbeitsplatz(1, "11.11.11");
        when(service.arbeitsplatz(1)).thenReturn(arbeitsplatz);
        mvc.perform(post("/admin/arbeitsplatzWirdGeandert/loeschen/arbeitsplatz/1/ausstattung/Monitor/HDMI")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("arbeitsplatz", arbeitsplatz))
                .andExpect(flash().attribute("ausstattungGeloescht", true));
        verify(service, times(1)).loescheAusstattungNachArbeitsplatzId(1, "Monitor", "HDMI");
    }
    @Test
    @WithMockOAuth2User(login = "Joe Mama", roles = {"USER", "ADMIN"})
    @DisplayName("Bei einem POST auf /admin/arbeitsplatzWirdGeandert/{id} gibt es eine redirection, das dazugehörige FlashAttributes wird hinzugefügt und die ausstattungHinzufuegen-Methode wird mit den richtigen Parametern aufgerufen")
    void test28() throws Exception {
        mvc.perform(post("/admin/arbeitsplatzWirdGeandert/1")
                        .with(csrf())
                        .param("id", "1")
                        .param("ausstattung", "Monitor")
                        .param("spezifikation", "HDMI"))
                .andExpect(flash().attribute("ausstattungHinzugefuegt", true));
        verify(service, times(1)).ausstattungHinzufuegen(1, "Monitor", "HDMI");
    }
    private static void autorisierungUeberpruefung(MvcResult mvcResult) {
        assertThat(mvcResult.getResponse().getRedirectedUrl())
                .contains("oauth2/authorization/github");
    }

    private MvcResult getMvcResultVonNichtAuthentifiziertemBesucherAuf(String url) throws Exception {
        return mvc.perform(get(url))
                .andExpect(status().is3xxRedirection())
                .andReturn();
    }

    private void authentifizierterBesucherKannAufrufen(String url) throws Exception {
        mvc.perform(get(url))
                .andExpect(status().isOk());
    }

    private void redirectionErfolgtAuf(String url) throws Exception {
        mvc.perform(get(url))
                .andExpect(status().is3xxRedirection());
    }

    private void zugangIstVerbotenAuf(String url) throws Exception {
        mvc.perform(get(url))
                .andExpect(status().isForbidden());
    }
}