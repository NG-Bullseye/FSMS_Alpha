# Entwicklerdokumentation

## Einführung und Ziele

Die Entwicklerdokumentation ist an das Entwicklungsteam gerichtet und sorgt für einen schnellen Überblick über Kontepte die 
wärend der Entwicklung entstanden sind.

### Aufgabenstellung - Möbelgeschäft "Möbel-Hier"
Das von der Moebel-Hunger-Kette übernommene Geschäft Möbel-Hier wird von Ihrer Firma mit einer neuen Kundensoftware(WEBSHOP) ausgestattet.

Im Leistungsumfang sind die Bestellverwaltung (BESTELLÜBERSICHT), das Warenlager(LAGER) und der Auslieferungsservice (FUHRPARK)enthalten. Besonderes Augenmerk soll auf die Warenbestellung und-auslieferung gelegt werden.
Dem Kunden liegt ein Sortiment(KATALOG) im Internet vor, für dessen Einsicht er sich nicht anmelden muss. Möbelstücke und Teile sind in Kategorien unterteilt. Diese lassen sich nach Kategorien filtern und sortieren und können ausschließlich telefonisch bestellt werden. 

Der Katalog enthält einzelne Teile(TEILE), die alleine bestellt werden können, und auch  vorgefertigte Sätze an Teilen(MÖBEL), so z.B. eine Couchecke mit Ein-, Zwei- und Dreisitzer. Variabilität ist dabei über die Farbe möglich.
Mitarbeiter verwalten für den Kunden seinen Warenkorb(WARENKORB) und sollen Produkte aus dem Katalog ausgeblendet werden können.

Einige Mitarbeiter sind Adminstratoren und haben damit weitreichendere Kompetenzen. So verwalten sie zum Beispiel die Mitarbeiter.

Die Lieferung einer Bestellung erfolgt entweder an ein Nebenlager(LAGER) oder direkt an den Kunden(KUNDE). Bei einer Lieferung an ein Nebenlager muss der Kunde automatisch benachrichtigt werden, damit er sie abholen kann.
Die Bezahlung der Bestellung ist nur andeutungsweise zu implementieren. Es soll aber  eine Auswahl der Zahlungsoptionen geben.

Die Auslieferung erfolgt mit dem firmeneigenen LKW-Fuhrpark(FUHRPARK). Alternativ können sich Kunden zum Abtransport der Ware diese LKWs mieten. Je nach Gewicht der Lieferung soll immer der nächst günstigste LKW vermietet bzw.von der Firma genutzt werden. Bis einen Tag vor Auslieferung bzw.  Versand soll das Stornieren einer Bestellung möglich sein.

Die Geschäftsführung von Möbel-Hier möchte eine monatliche Abrechnung(FINANZÜBERSICHT)  haben, in der die Einnahmen wie Möbelkosten oder Ausgaben wie Personalkosten im Vergleich zum Vormonat aufgegliedert sind.

Kannkriterien:
Weiterhin soll eine Statusabfrage über die bereits gelieferten Möbelteile der Bestellungen möglich sein.

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
<td></td>
<td>X</td>
</tr>
<tr>
<td><b>Wartbarkeit</b></td>
<td></td>
<td></td>
<td>X</td>
<td></td>
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
<td></td>
<td>X</td>
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
Beliebiger Computer mit folgenden Spezifikationen:
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

## Lösungsstrategie

Das Programm ist in mehrere Packages aufgeteilt, die die Funktionalität für die 
entsprechenden Aufgabenbereiche des Programms bereitstellen:

| Package              | Beschreibung                                                                                                                                                     |
|:---------------------|:-----------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Order Management     | Hier wird der Warenkorb des Kunden erstellt, bearbeitet und abgeschlossen. Dies geschieht ausschließlich durch den Mitarbeiter.                                  |
| Car Management       | Verwaltung des Fuhrparks. Hier können Fahrzeuge gemietet werden, falls der Kunde zur Abholung seiner Ware einen LKW benötigt.                                    |
| Customer Management  | Das Herzstück: Unsere Kunden. Kunden können mithilfe dieses Packages verwaltet werden. Außerdem ermöglicht es die Registrierung.                                 |
| Inventory Management | Dies ist die lagerverwaltung. Artikel können hier beispielsweise Nachbestellt werden.                                                                            |
| Catalog Management   | Die Katalogverwaltung. Der Kunde kann auf unsere Webseite den Katalog einsehen und ihn filtern. Außerdem ist es möglich, Artikel vor dem Kunden zu "verstecken". |

Die einzelnen Packages interagieren über ihre jeweiligen Controller Klassen (siehe MVC Pattern). Komplexere Aufgaben/Berechnungen werden an die Manager weitergegeben. Somit ist die Trennung des Controllers vom Model gewährleistet.

Ein sehr entscheidener Schwerpunkt des Projekts war die Implementierung des Composite-Musters für die Artikel. Da Datenbanken es nur schwer zulassen eine baumstrukturierte Datenstruktur zu realisieren, haben wir unser Composite dementsprechend angepasst. Eine ausführliche Erklärung dazu kann man unter dem Punkt Entwurfsmuster nachlesen.





## Bausteinsicht
![Paketdiagramm](https://github.com/st-tu-dresden-praktikum/swt18w34/blob/master/asciidoc/models/design/Paket-Diagramm.jpg)

## Entwurfsentscheidungen
Dieser Abschnitt beschreibt die Umsetzung verschiedener Probleme und Fragen, die während der Entwicklung aufgetreten sind. 

Für viele unserer Funktionen konnten wir Salespointfunktionalitäten nutzen. Dennoch mussten wir für weitere Funktionalitäten diese oft erweitern und haben die ursprüngliche Klasse per Vererbung oder Komposition genutzt.

Zur Update unserer Artikel(siehe Abschnitt Entwurfsentscheidungen) verändern wir zunächst den ausgewählten Artikel. Danach suchen wir mithilfe einer Tiefensuche (alternativ würde auch Breitensuche gehen) die Artikel die den geänderten Artikel enthalten und konstruieren damit einen Baum. Dabei aktualisieren wir jedoch noch nicht gleich, da auch ein Teil dieses Artikels den ursprünglich geänderten Artikel enthalten könnte, sodass dieser zuerst aktualisiert werden muss. Danach wenden wir eine Art topologische Sortierung an. Es werden im Baum nacheinander alle Artikel aktualisiert, deren Teile nicht im Baum der Artikel, die eine Aktualisierung benötigen, auftaucht.

Das Projekt besitzt neben den regulären Artikeln auch ausleihbare Produkte, die LKWs. Diese werden als Erweiterung des Salespoint Produktes implementiert und besitzen ein Attribut, das anzeigt ob dieser LKW momentan verliehen ist. Das Zurückgeben kann einfach über eine Attributänderung stattfinden. Da es dazu kein Salespointlager gibt, nutzen wir stattdessen ChargeLines, die in Salespoint für zusätzliche Ausgaben einer Bestellung verwendet werden können.

## Architektur

![Entwurfsklassendiagramm](https://github.com/st-tu-dresden-praktikum/swt18w34/blob/master/asciidoc/models/design/Entwurfsklassendiagramm.jpg)

Das gesamte Projekt basiert auf der objektorientierten Programmiersprache Java, um eine modulare und polymorph-erweiterbare Architektur zu verwirklichen.
 
Das Springframework, welches einen sehr großen Teil dieser Software ausmacht, ermöglicht in Verbindung mit Thymeleaf eine dynamische Verwendbarkeit und Erweiterbarkeit der zu betreibenden Website.
 
Spring-Boot wird als spezieller Teil von Spring in dieser Software verwendet.

Des weiteren ist Salespoint ein sehr wichtiges Framework für diese Software, da es bereits sehr viele Funktionalitäten und Verwaltungsstrukturen für den Betrieb einer Verkaufswebsite bietet.

Um die Übersichtlichkeit und Einfachheit des Codes zu erhöhen, wurde die Project-Lombok Library verwendet.

Für die Webansicht wurde das CSS-Framework Semantic-UI benutzt.


## Verwendete Muster
Entwurfsmuster

Model-View-Controller(MVC)

Wir verwenden das MVC-Muster (Model-View-Controller) mit einer Umsetzung durch Spring und Thymeleaf. 
Die Controller und Manager bilden das Java-basierte Backend des Programms und sind für die Verwaltung und Verarbeitung der Daten verantwortlich. Der Controller dient als Verbindung zwischen View und Model, da er die Eingaben des Views verarbeitet und auf Fehler prüft und anschließend das Model dementsprechend modifiziert. 
Im View werden mittels Thymeleaftemplates die Daten des Models angezeigt. Ebenso werden darüber die Eingaben der Benutzer getätigt. Dabei bleibt die View aber vom Model unabhängig.

Composite

Mithilfe des Composite-Musters stellen wir die Artikel in unserem System dar. Es gibt Möbel und Teile als bestellbare Artikel. Dabei sind Teile die kleinstmöglichen Einheiten, welche als Blätter des Baumes dienen. Möbel setzen sich aus anderen Möbeln und Teilen zusammen und bilden dadurch eine Baumstruktur. 
Dies erlaubt zum einen eine einfache Bearbeitung, da neue Bestandteile einfach in dem Baum eingefügt bzw. alte Bestandteile entfernt werden können. Ebenso wird über die Baumstruktur das Gewicht und der Preis bestimmt, da sich dieser aus den Preisen/Gewichten der Einzelteilen ergibt. Außerdem ermöglicht uns das auch zu jedem Möbelstück die Einzelteile anzubieten(wie in den Anforderungen gefordert), da sowohl Möbel als auch Teile Artikel sind, die bestellt werden können.

Diese ursprüngliche Idee hat sich in der Implementierung jedoch als nicht praktikabel herausgestellt. Daher haben wir das Muster abgewandelt, wobei aber grundsätzlich die Idee erhalten bleibt. Es war problematisch Artikel als Attribute von Artikeln zu implementieren, da dann die Datenbank unter Umständen viele verschiedene Artikel für nur einen Artikel laden muss. Daher haben wir die Verbindung gelockert und nur noch die Ids der Teile gespeichert. Mithilfe des Katalogs können anhand dieser Ids die Artikel zum Beispiel für Updates immer noch gefunden werden. Außerdem verwenden wir anstelle einer einfachen Liste eine assoziative Datenstruktur, dass heißt eine Map, um auch die Anzahl zu speichern, sodass nicht mehrmals der selbe Artikel und seine Daten ermittelt werden muss.
 
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
    <tr class="even">
      <td><p>Assoziatives Datenfeld</p></td>
      <td><p>Ein Datenfeld, das Werte mithilfe eines Schlüssels speichert. In Java ist dies die Klasse Map.
    </tbody>
</table>
