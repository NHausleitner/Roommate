package roommate.web.controller;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import roommate.domain.model.Arbeitsplatz;
import roommate.security.AdminOnly;
import roommate.security.AppUserService;
import roommate.application.service.RoomMateService;

import java.util.Collections;
import java.util.List;

@Controller
public class WebController {

    private final RoomMateService service;

    public WebController(RoomMateService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String index(){
        return "redirect:/uebersicht";
    }

    @GetMapping("/uebersicht")
    public String uebersicht(OAuth2AuthenticationToken auth, Model model){
        String name = auth.getPrincipal().getAttribute("login");
        adminUeberpruefungModel(model, name);
        return "uebersicht";
    }
    @PostMapping("/uebersicht")
    public String arbeitsplatzSuche(OAuth2AuthenticationToken auth, Model model){
        String name = auth.getPrincipal().getAttribute("login");
        adminUeberpruefungModel(model, name);
        return "uebersicht";
    }

    @GetMapping("/admin/adminseite")
    @AdminOnly
    public String adminseite(){
        return "admin/adminseite";
    }

    @GetMapping("/admin/arbeitsplatzErstellen")
    @AdminOnly
    public String arbeitsplatzErstellen(){
        return "admin/arbeitsplatzErstellen";
    }
    @PostMapping("/admin/arbeitsplatzErstellen")
    @AdminOnly
    public String arbeitsplatzErstellt(String raum, String ausstattung1, String spezifikation1, String ausstattung2, String spezifikation2, String ausstattung3, String spezifikation3, String ausstattung4, String spezifikation4, String ausstattung5, String spezifikation5, Model model){
            int rueckgabeId = service.arbeitsplatzHinzufuegen(raum);
            if(rueckgabeId == -1) {
                model.addAttribute("arbeitsplatzFalsch", true);
                return "/admin/arbeitsplatzErstellen";
            }
            int rueckgabe;
        service.ausstattungHinzufuegen(rueckgabeId, ausstattung1, spezifikation1);
        service.ausstattungHinzufuegen(rueckgabeId, ausstattung2, spezifikation2);
        service.ausstattungHinzufuegen(rueckgabeId, ausstattung3, spezifikation3);
        service.ausstattungHinzufuegen(rueckgabeId, ausstattung4, spezifikation4);
        rueckgabe = service.ausstattungHinzufuegen(rueckgabeId, ausstattung5, spezifikation5);
        if(rueckgabe == -1){
            model.addAttribute("ausstattungNameLeer", true);
        }
        model.addAttribute("arbeitsplatzRichtig", true);
        return "/admin/arbeitsplatzErstellen";
    }

    @GetMapping("/admin/arbeitsplatzAendern")
    @AdminOnly
    public String arbeitsplatzAendern(Model model){
        List<Arbeitsplatz> alleArbeitsplaetze = service.alleArbeitsplaetze();
        model.addAttribute("alleArbeitsplaetze", alleArbeitsplaetze != null ? alleArbeitsplaetze : Collections.emptyList());
        return "admin/arbeitsplatzAendern";
    }

    @GetMapping("/admin/reservierungAendern")
    @AdminOnly
    public String reservierungAendern(){
        return "admin/reservierungAendern";
    }

    @PostMapping("/admin/reservierungAendern")
    @AdminOnly
    public String reservierungGeaendert(){
        return "admin/reservierungAendern";
    }
    @GetMapping("/admin/arbeitsplatzWirdGeandert/{id}")
    @AdminOnly
    public String arbeitsplatzWirdGeaendert(Model model, @PathVariable("id") int id){
        model.addAttribute("arbeitsplatz", service.arbeitsplatz(id));
        return "admin/arbeitsplatzWirdGeandert";
    }
    @PostMapping("/admin/arbeitsplatzWirdGeandert/{id}")
    @AdminOnly
    public String ausstattungHinzufuegen(RedirectAttributes redirectAttributes, @PathVariable("id") int arbeitsplatzId, String ausstattung, String spezifikation){
        int rueckgabeId = service.ausstattungHinzufuegen(arbeitsplatzId, ausstattung, spezifikation);
        if(rueckgabeId == -1){
            redirectAttributes.addFlashAttribute("ausstattungNichtHinzugefuegt", true);
            return "redirect:/admin/arbeitsplatzWirdGeandert/{id}";
        }
        if(rueckgabeId == -2){
            redirectAttributes.addFlashAttribute("ausstattungNichtVorhanden", true);
            return "redirect:/admin/arbeitsplatzWirdGeandert/{id}";
        }
        redirectAttributes.addFlashAttribute("ausstattungHinzugefuegt", true);
        return "redirect:/admin/arbeitsplatzWirdGeandert/{id}";
    }

    @PostMapping("/admin/arbeitsplatzWirdGeandert/loeschen/{id}")
    @AdminOnly
    public String arbeitsplatzWirdGeloescht(RedirectAttributes redirectAttributes, @PathVariable("id") int id){
        redirectAttributes.addFlashAttribute("arbeitsplatzGeloescht", true);
        service.arbeitsplatzLoeschen(id);
        return "redirect:/admin/arbeitsplatzAendern";
    }

    @PostMapping("/admin/arbeitsplatzWirdGeandert/loeschen/arbeitsplatz/{arbeitsplatzId}/ausstattung/{ausstattungName}/{ausstattungSpezifikation}")
    @AdminOnly
    public String ausstattungWirdGeloescht(RedirectAttributes redirectAttributes, @PathVariable("arbeitsplatzId") int arbeitsplatzId, @PathVariable("ausstattungName") String ausstattungName, @PathVariable("ausstattungSpezifikation") String ausstattungSpezifikation){
        service.loescheAusstattungNachArbeitsplatzId(arbeitsplatzId, ausstattungName, ausstattungSpezifikation);
        redirectAttributes.addFlashAttribute("ausstattungGeloescht", true);
        redirectAttributes.addFlashAttribute("arbeitsplatz", service.arbeitsplatz(arbeitsplatzId));
        return "redirect:/admin/arbeitsplatzWirdGeandert/{arbeitsplatzId}";
    }


    private static void adminUeberpruefungModel(Model model, String name) {
        System.out.println(name);
        if (AppUserService.getAdmins().contains(name)){
            model.addAttribute("istAdmin", true);
        } else {
            model.addAttribute("istAdmin", false);
        }
    }

}