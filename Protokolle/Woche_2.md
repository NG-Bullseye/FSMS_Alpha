# Protokoll - Woche 2

Gruppe 34, Schriftführer: Daniel Körsten

## Konsultation

### Allgemeines & Deadlines

- Beispiel des Pflichtenhefts findet sich in der Repo des [Videoshops](https://github.com/st-tu-dresden/videoshop) und eine Vorlage in unserer Repo
- Bis **28.10.2018** muss der Videoshop um eine Funktion erweitert werden. Dazu das Projekt [Videoshops](https://github.com/st-tu-dresden/videoshop) in den eigenen Account forken. Beispiel Funktionen:
	-  E-Mail Bestätigung (Daran würde ich mich mal versuchen)
	-  Saisonabhängige Rabatte
	-  Nachbestellung von DVD's, wenn sie alle verkauft sind
- Daten in Java sollen mittels *Hibernate* gespeichert werden
- Bis **28.10.2018** ebenfalls Sequenzdiagramme, CRC-Karten und Top-Level-Architektur erstellen -> **Probleme mit Magic Draw Lizenz**
- Schon einmal mit dem Pflichtenheft vertraut machen und beginnen

### Programmfeatures Möbelgeschäft

- Onlinebestellung nicht möglich, sondern nur telefonisch möglich bei Mitarbeiter
- Personal soll Online verwaltet werden können (hinzufügen)
- Kundenverwaltung, die gefiltert, sortiert und durchsucht werden kann:
	- Kunden können vom Admin entfernt oder deaktiviert werden
	- Eine Detailansicht des Kunden  mit all seinen Bestellungen
	- Kunden können ihr Konto selbst ansehen und ihre Daten ändern
- Kunden können sich registrieren und sollen anschließend eine Bestätigungsmail erhalten
- Login für Kunden, Mitarbeiter und Admins erstellen -> verschiedene Berechtigungen
- Es soll ein Lager und einen Fuhrpark geben
- Mitarbeiter sollen das Lager auffüllen können, neue Artikel hinzufügen oder entfernen können
- Kunde kann nur bestellen, wenn Ware ab Lager. Standard Nachlieferungs-zeit für Nachbestellung: 4 Tage
- Artikel sollen zwischen Standorten verschoben werden können
- Kunden sollen einen Online Katalog haben, ums sich die Artikel anzusehen. Dieser soll filterbar, sortierbar und durchsuchbar sein. Mitarbeiter sollen Artikel verstecken können
- Es soll Artikelkategorien geben
- Artikel sollen eine Detailansicht besitzen. Hier soll es Sterne-Bewertungen und Text-Bewertungen geben
- Bestellungen:
	- sind bis 1 Tag vor Auslieferung stornierbar
	- es gibt folgende Zustände:
		- offen
		- bezahlt
		- versendet
		- abholbereit, falls Kunde abholen will
		- abgeholt bzw. zugestellt
- Rest siehe Aufgaben Text

## Gruppentreffen

jeweils am 17. & 18.10.2018 für je 3 Stunden

### TODO Liste

| Aufgabe                 | Deadline      | Status | Bemerkung |
| :-----------------------|:-------------:| :-----:|:----------|
| Ein grüner Haken        | -             | :white_check_mark: | - |
| Videoshop               | 28.10.2018    | :x: | - |
| CRC-Karten              | 28.10.2018    | :x: | In Arbeit, erste Version fertig |
| Sequenzdiagramme        | 28.10.2018    | :x: | In Arbeit, erste Version fertig |
| Top-Level-Architektur   | 28.10.2018    | :x: | In Arbeit, erste Version fertig |
| Anwendungsfalldiagramm  | 28.10.2018    | :x: | -  |
| Analysediagramm         | -             | :x: | Später. -> Magic Draw funktioniert nicht |
| Pflichtenheft           | -             | :x: | - |

### Entstandene Fragen

- Sind Nebenlager nur Verteilerzentren (ähnlich wie Packstation) oder können die auch Fuhrparks besitzen?
- Muss Kunde Warenkorb bestätigen? Sprich, muss er speicherbar sein, wenn der Kunde sagt, ich überlege es mir nochmal?
- Können Mitarbeiter gekündigt werden?
- Soll die Zustellung simuliert werden?
- Kann der Admin das Gehalt festlegen/ändern? 
- Können Kategorien umbenannt werden oder neue erstellt werden?
- Können Preise von Artikeln verändert werden?
- Zahlen wir Mehrwertsteuer?
- Eingaben/Ausgaben detailliert, sprich Personalkosten, welche Artikel verkaufen sich besonders gut?
- Können Autos gekauft werden? Sollen die dann in der Finazstatistik erscheinen? Haben Fahrzeuge monatliche, fixe Kosten?