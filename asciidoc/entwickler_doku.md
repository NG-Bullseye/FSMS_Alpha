# Entwicklerdokumentation

## Einführung und Ziele

Die Entwiklerdokumentation ist an das Entwicklungsteam gerichtet und sorgt für einen schnellen Überblick über Kontepte die 
wärend der Entwicklung entstanden sind.

### Aufgabenstellung - Möbelgeschäft "Möbel-Hier"
Das von der Moebel-Hunger-Kette übernommene Geschäft Möbel-Hier wird von Ihrer Firma mit einer neuen Kundensoftware(WEBSHOP) ausgestattet.
Im Leistungsumfang sind die Bestellverwaltung (BESTELLÜBERSICHT), das Warenlager(LAGER) und der Auslieferungsservice (FUHRPARK)enthalten.
Besonderes Augenmerk soll auf die Warenbestellung,  -auslieferung gelegt werden.
Dem Kunden liegt ein Sortiment(KATALOG) im Internet vor, für dessen Einsicht er sich nicht anmelden muss.
Möbelstücke und Teile sind in Kategorien unterteilt.
Die Möbel dort lassen sich nach Kategorisierungen filtern und sortieren.
Möbel lassen sich vom Kunden ausschließlich telefonisch bestellen.
Der Mitarbeiter verwaltet den Warenkorb(WARENKORB). 
Der Mitarbeiter soll Produkte ausblenden können.
Einige Mitarbeiter sind Administratoren(ADMIN).
Ausschließlich die Administratoren verwalten die Mitarbeiter.
Die Lieferung erfolgt entweder in das Nebenlager(LAGER) oder direkt an den Kunden(KUNDE).
Für ersteres muss der Kunde bei vollständiger Ware automatisch benachrichtigt werden, damit er sie abholen kommen kann.
Weiterhin soll eine Statusabfrage über die bereits gelieferten Möbelteile der Bestellungen möglich sein.
Bezahlung ist nur andeutungsweise zu implementieren. Es muss nur eine Auswahl der Zahlungsoptionen geben.
Der Kunde kann sich Teile(TEILE) einzeln bestellen, jedoch auch vorgefertigte Sätze an Teilen(MÖBEL), so z.B. eine Couchecke mit Ein-, Zwei- und Dreisitzer.
Variabilität ist über die Farbe möglich.
Die Auslieferung erfolgt mit dem firmeneigenen LKW-Park(FUHRPARK).
Alternativ können sich Kunden zum Abtransport der Ware diese LKW's mieten.
Je nach Gewicht der Lieferung soll immer der nächst günstigere LKW vermietet bzw.
von der Firma genutzt werden.
Die Software soll Stornieren bis zu einen Tag vor Versand berücksichtigen.
Die Geschäftsführung von Möbel-Hier möchte eine monatliche Abrechnung(FINANZÜBERSICHT)  haben, in der die Möbelverkäufe im Vergleich zum Vormonat aufgeglieder sind.

<table>

<tbody><tr>
<th> Qualitätsziel </th>
<th>1</th>
<th>2</th>
<th>3</th>
<th>4</th>
<th>5</th>
</tr>
<tr>
<td><b>Erweiterbarkeit</b></td>
<td></td>
<td></td>
<td></td>
<td>X</td>
<td></td>
</tr>
<tr>
<td><b>Wartbarkeit</b></td>
<td></td>
<td></td>
<td></td>
<td>X</td>
<td></td>
</tr>
<tr>
<td><b>Benutzbarkeit</b></td>
<td></td>
<td></td>
<td></td>
<td></td>
<td>X</td>
</tr>
<tr>
<td><b>Zuverlässigkeit</b></td>
<td></td>
<td></td>
<td></td>
<td>X</td>
<td></td>
</tr>
<tr>
<td><b>Sicherheit</b></td>
<td></td>
<td>X</td>
<td></td>
<td></td>
<td></td>
</tr>
<tr>
<td><b>Organisierbarkeit</b></td>
<td></td>
<td></td>
<td></td>
<td></td>
<td>X</td>
</tr>
<tr>
<td><b>Leistungsfähigkeit</b></td>
<td></td>
<td>X</td>
<td></td>
<td></td>
<td></td>
</tr>

</tbody></table>

(Bewertung von 1( nicht wichtig) bis 5 (sehr wichtig))

zur Organisierbarkeit: dieser Punkt beschreibt die Eignung der Software die Elemente im Onlineshop und allgemein im Möbelhaus zu verwalten

## Randbedingungen

### Hardware-Vorgaben
Belieber Computer mit folgenden Spezifikationen:
- Pentium 4 oder neuer
- 1024 MB Arbeitsspeicher oder mehr
- min. 1GB freier Speicherplatz

### Software-Vorgaben
- Modernes Betriebssystem (Mac OS X 10.8.3+, Windows Vista SP2+, Ubuntu Linux 12.04+)
- Java 8 JDK vorinstalliert
- Maven 3.3 oder höher vorinstalliert
- Folgende Frameworks kommen zum Einsatz: Spring, Spring-Boot, SalesPoint, Thymeleaf
- Von Folgenden Webbrowser kann die Webseite richtig dargestellt werden:
  - Firefox 52 (oder neuer)
  - Chrome 49 (oder neuer)
  - Internet Explorer 11

## Kontextabgrenzung
- Externe Schnittstellen

## Lösungsstrategie
Kurzer Überblick über Ihre grundlegenden Entscheidungen und Lösungsansätze, die jeder, der mit der Architektur zu tun hat, verstanden haben sollte.

## Bausteinsicht
![Paketdiagramm](https://github.com/st-tu-dresden-praktikum/swt18w34/blob/master/asciidoc/models/design/Paket-Diagramm.jpg)

## Entwurfsentscheidungen

## Architektur

![Entwurfsklassendiagramm](https://github.com/st-tu-dresden-praktikum/swt18w34/blob/master/asciidoc/models/design/Entwurfsklassendiagramm.jpg)

Bei dem Projekt handelt es sich um ein Java Objekt orientiertes Design, zur modularen und polymorph-erweiterbaren Architektur.
 
Das Springframework bietet umfangreiche Programmiermöglichkeiten. Dessweiteren stellt es ein konfigurierbares Model(glossar) für Java basierende Firmensoftware, auf einer art "deployment platform"(glossar), zur Verfügung.
 
Eine Schlüsseleigenschaft von Spring ist die Unterstürzung der Grundstruktur auf Implementationslevel. 
Spring legt Wert auf schnelle Produktion von Firmensoftware ohne, dass sich Entwicklerteams um unnötige Deployment-Umgebungen sorgen müssen.

Spring Boot ist konziepiert worden um einen schnellen Einstieg zu ermöglichen, mit minnimalem Konfigurationsaufwand der Springsoftware.

Spring Boot takes an opinionated view of building production ready applications.
 
SalesPoint ist ein auf Javabasis geschriebenes Applicationsframework um Verkaufs- oder auch Einkaufsstellen (PoS) zu realisieren, welche Spring und Springboot benutzt.
Es macht dem Nutzer möglich hochentwickelte Web Applicationen zu implementiern. 

## Verwendete Muster

 Model View Controller(MVC) Pattern
 
 /////////////////////////zu erweitern/////////////////////
 
## Persistenz

Die Speicherung der Daten erfolgt mittels der Java Persistence API und werden als H2 Datenbank abgelegt.

## Benutzeroberfläche

![startseite](https://github.com/st-tu-dresden-praktikum/swt18w34/blob/master/asciidoc/GUI/startseite.png)
Dies ist die Startseite von Möbel hier. Oben rechts finden sich die Buttons für den Login bzw. Registrieren und auf der linken Seite die Kategorien, um die Möbel grob sortieren zu können.

![artikelansicht](https://github.com/st-tu-dresden-praktikum/swt18w34/blob/master/asciidoc/GUI/artikelansicht.png)
Die Artikeldetailansicht, wenn man Beispielsweise einen Artikel aus der Kategorie "Unsere Empfehlungen" auf der Startseite auswählt. Hier finden sie Details zum Artikel, sowie die Artikel Bewertungen.

![kundenprofil](https://github.com/st-tu-dresden-praktikum/swt18w34/blob/master/asciidoc/GUI/kundenprofil.png)
Nach dem Login landet der Kunde Standardmäßig hier. Es lassen sich die vergangenen Bestellungen ansehen. Desweiteren hat der Kunde die Möglichkeit seine Adressdaten zu ändern.

![bestellung](https://github.com/st-tu-dresden-praktikum/swt18w34/blob/master/asciidoc/GUI/bestellung.png)
Bei einem Klick auf die Bestellung vom Kundenprofil kann der Kunde die Bestellung im Detail ansehen und hat die Möglichkeit die Bestellung zu stornieren, falls sie noch nicht versendet wurde.

![kundenliste](https://github.com/st-tu-dresden-praktikum/swt18w34/blob/master/asciidoc/GUI/kundenliste.png)
Die Mitarbeiter haben hier die Möglichkeit die Kundenliste einzusehen und bei Bedarf Kunden durch einen Klick auf den Status zu deaktivieren bzw. wieder zu aktivieren.

![mitarbeiterliste](https://github.com/st-tu-dresden-praktikum/swt18w34/blob/master/asciidoc/GUI/mitarbeiterliste.png)
Der Admin kann hier alle Mitarbeiter einsehen. Durch einen Klick auf den Status kann der Admin Mitarbeiter deaktivieren, wenn sie nicht mehr im Unternehmen arbeiten und das Gehalt, durch einen Klick auf das Gehalt, anpassen.

## Glossar


<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
    <td><p>Begriff</p></td>
    <td><p>Erklärung</p></td>
    </tr>
    <tr class="even">
        <td><p>Teil</p></td>
        <td><p>kleinste bestellbare Einheit</p></td>
    </tr>
    <tr class="odd">
        <td><p>Möbel</p></td>
        <td><p>Set aus Teilen</p></td>
    </tr>
    <tr class="even">
        <td><p>Administrator</p></td>
        <td><p>Synonym für Boss</p></td>
    </tr>
    <tr class="odd">
        <td><p>Nutzer</p></td>
        <td><p>jeder der die Website Nutzt</p></td>
    </tr>
    <tr class="odd">
        <td><p>Kunde</p></td>
        <td><p>registrierter Nutzer der nicht in der Firma arbeitet</p></td>
    </tr>
    <tr class="even">
        <td><p>Mitarbeiter</p></td>
        <td><p>Angestellter der Firma</p></td>
    </tr>
    <tr class="odd">
        <td><p>Fuhrpark</p></td>
        <td><p>Anzahl der verschiedenen LKWs</p></td>
    </tr>
      <tr class="even">
        <td><p>Webbrowser</p></td>
        <td><p>spezielle Computerprogramme zur Darstellung von Webseiten im World Wide Web oder allgemein von Dokumenten und Daten.</p></td>
     </tr>
        <tr class="odd">
        <td><p>Pentium 4</p></td>
        <td><p>Mikroprozessor der Firma Intel</p></td>
    </tr>
        <tr class="even">
        <td><p>Konfiguration</p></td>
        <td><p>eine bestimmte Anpassung von Programmen und/oder Hardwarebestandteilen eines Computers an das vorhandene System</p></td>
    </tr>
      <tr class="odd">
        <td><p>Framework</p></td>
        <td><p>Programmiergerüst in der Softwaretechnik</p></td>
    </tr>
    </tbody>
</table>
