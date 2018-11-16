# Protokoll Woche 6

Gruppe 34, Schriftführer: Daniel Körsten

## Konsultation

### Basisfunktionalität

- Katalog
- Startseite
- Grundlegende Funktionen sollen da sein

### Zwischenpräsentation

- Selbstreflexion
- Zeigen was geht
- Was lief gut? Was nicht?
- Basisfunktionalität soll stehen
- Termin wird noch vereinbart

### Diagramme

- alles homogen machen (gleiche Rechtschreibung für gleiche Begriffe hauptsählich um den Begriff Management)
- Katalog ist nicht fertig?!
- common used salespoint frameworks - frameworks ist zuviel
- Aufteilung Manager Controller homogen
- Customer deaktivieren
- Mitarbeiter, Admins fehlen
- Car management - Auto abholen
- Hauptlager und Nebenlager (Packstationen) sind nicht da
- Produkte sollten Standort haben
- OrderManager gibt es in DalesPoint - umbennen
- Pflichtenheft und Diagramme gegenchecken

### Sonstiges

- Pflichtenheft und Entwicklerdoku im Readme verlinken
- Architektur komplett überarbeiten
- Entwicklerdoku nochmal auf Rechtschreibfehler prüfen
- Entwursentscheidungen in ENtwicklerdoku noch schreiben
- verfeinerte Akzptanztestfälle fehlen noch
- alle Testfälle in ein Doc

## Gruppentreffen vom 14.11.2018

- Besprechung des Git Workflows (siehe unten)
- klären von Fragen rund um Git
- Besprechung zukünftige Weiterentwicklung des Projekts

### Git Workflow nach Treffen vom 14.11.2018

- sämtliche Dokumentation auf den master pushen
- **Immer einen Pull ausführen, bevor man pusht!**
- Für die Programmierung gilt folgendes:
  - für jedes Features eine neue Branch mit einem treffenden Namen erstellen. **Als Grundlage dafür den beta Branch nutzen!**
  - jeder kann beliebig viele parallele Feature Branches erstellen
  - Wenn in dem Feature keine groben Fehler mehr existieren und man meint, es sei fertig: auf den beta Branch pushen
  - Jetzt testen, ob der beta Branch ausführbar ist. Wenn nicht prüfen, ob es am eigenen Feature liegt.
  - **Bugfixing kann auf dem beta Branch passieren.**
- Regelmäßig (meist vor den Meilensteinen) wird der beta in den master Branch gemergt. Hier können wir über *CI* sehen, ob alles passt. Wenn nötig hier noch bugfixing betreiben.
- **Niemals ohne Abprache den beta auf den master mergen!**
