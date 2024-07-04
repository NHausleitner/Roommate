# RoomMate

Es herrscht Platzmangel an der Universität! Der Fachbereich Informatik wächst, aber leider wächst das Gebäude nicht mit. Gleichzeitig gibt es seit...
naja, Sie wissen schon ... immer mehr Mitarbeiter:innen, die im Homeoffice arbeiten. Wir könnten die Büros also im Prinzip auch gut mehrfach belegen
und teilen.

Ein Paar Grundregeln:
1. Nicht ohne Abstimmung an fremden Branches weiterarbeiten.
2. Regelmäßig updaten, um den Fortschritt anderer mitverfolgen zu können -> weniger Konflikte.
3. Wir mergen nur, nicht rebasen (z.B. wenn IntelliJ fragt).
4. Auf sinnvolle Commits achten, Regeln/Prinzipien beachten (siehe Regeln zum Commiten).
5. Immer im master sein, wenn man einen anderen Branch mergt

Regeln zum Commiten:
1. Regelmäßig commiten und immer logisch zusammenhängende Dinge gemeinsam commiten.
2. In die erste Zeile des Commits kommt ein Schlagwort, mit dem man beschreibt, was in dem Commit passiert
Typische Schlagwörter: add, update, green, refactor, delete, fix
3. Im Rest des Commits wird die Änderung näher beschrieben

Beispiel 1:
add FooKlasse, BarKlasse
FooKlasse wurde hinzugefügt
BarKlasse wurde hinzugefügt

Beispiel 2:
update Fooklasse
In der FooKlasse gab es einen Fehler. Dieser wurde mit Hilfe einer Exception gefangen

Beispiel 3:
green FooTestKlasse
Tests in FooTestKlasse funktionieren

Beispiel 4:
refactor Fooklasse
BarMethode in Fooklasse effizienter gemacht

ToDo - Liste:

- arbeitsplatzErstellen.html (AP anzeigen lassen, nach verschiedenen Bedingungen bzw. Ausstattungsmerkmalen, AP reservieren, Ausstattung) mithilfe von Datenbanken (Wochenblatt 10/11)
- uebersicht.html (Arbeitsplätze anzeigen lassen, Suchfunktion) mithilfe von Datenbanken (Wochenblatt 10/11)
- Projekt in Self-Contained-Systems (SCS) aufteilen (Wochenblatt 9)
- Integration von Keymaster (Wochenblatt 12)
- Spotbugs hinzufügen (Upgrade Wochenblatt 12)
- Datenbank-Daten aus den Commits entfernen und BFG verwenden (Wochenblatt 14)

Ideen und andere ToDos:

- Idee: WebControllerTest: Alle Web-Tests von den HTML-Seiten in eigene Testdateien aufteilen (-> Testdatei für übersicht.hmtl, Testdatei für arbeitsplatzErstellen, ...)
- Idee: WebControllerTest: Tests kleinschrittiger machen?
- Idee: Die verschiedenen HTML-Adminseiten in Unterordner tun, damit es etwas übersichtlicher ist
- Fixen: WebControllerTest: Es gibt 1 Test, welcher auf disabled ist
- Ändern: WebController, WebControllerTest: PathVariable überall wo nur id steht zu arbeitsplatzId umändern (natürlich auch, nur wenn es die arbeitsplatzId ist)
- Ändern: WebController, arbeitsplatzErstellen.html: Am besten sollten nicht 5 Ausstattungen über Eingabefelder erzeugt werden. Stattdessen wäre eine andere Lösung besser
- (Potenziell ändern: Alles was zu Datenbanken gehört: Überall toLowerCase() benutzen, damit keine Verwechslungen von Groß- und Kleinschreibung entstehen)
- (Potenziell ändern: Überall wo noch Warnings angezeigt werden, also so etwas wie 'unused import statement' sollte entfernt werden)
