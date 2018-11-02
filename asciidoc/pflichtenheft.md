| Version | Status    | Bearbeitungsdatum | Autoren(en)                              | Vermerk          |
| ------- | --------- | ----------------- | ---------------------------------------- | ---------------- |
| 1.0     | In Arbeit | 29.10.2018        | Daniel, Danliel, Ricco, Leonard, Johannes| Erste Version    |

# Inhaltsverzeichnis

1. Zusammenfassung

2. Aufgabenstellung und Zielsetzung

3. Priduktnutzung

4. Stakeholders

5. Systemgrenze und Top-Level-Architektur

6. Anwendungsfälle

    6.1 Aktuere
  
    6.2 Überblick Anwendungsfalldiagramm
  
7. Funktionale Anforderungen

    7.1. Muss-Kriterien
  
    7.2. Kann-Kriterien
  
8. Nicht-Funktionale Anforderungen

    8.1. Qualitätsziele
  
    8.2. Konkrete Nicht-Funktionale Anforderungen
  
9. GUI Prototyp

    9.1 Überblick: Dialoglandkarte
  
    9.2 Dialogbeschreibung
  
10. Datenmodell

    10.1 Überblick: Klassendiagramm
  
    10.2 Klassen und Enumerationen
  
11. Akzeptanztestfälle

12. Glossar

13. Offene Punkte


# 1. Zusammenfassung

Dieses Dokument stellt die <behinderte> Software-Anforderungsspezifikation , gennant Pflichtenheft, des Projekts  Videoladen dar. Es soll einen Überblick über das Softwareprodukt geben und als Grundlage der Kommunikation zwischen den Akteuren des Projekts funktionieren. Vor allem aber dem Kunden und dem Entwicklungsteam zur Verfügung zu stehen. Es soll für das Dokument als Grundlage für ein Vertrag zwischen dem Auftraggeber und dem Auftragnehmer gelten und somit die Konsistenz geprüft werden. Das Pflichtenheft beschreibt was das gewünschte System zu erfüllen hat, und teilweise, wie der Auftragnehmer die Lösung umzusetzen gedenkt.

Das Pflichtenheft sollte stehts zu vervollständigen,  konsequente und korrekt sein. Am Ende des Projekts wird es verwendet um zu überprüfen, ob die definierte Software geliefert wurde. Es wird während des gesamten Projekts verwendet, weshalb der Inhalt aller Artefakte , die erstellt werden, im gesamten  rückverfolgbar sein sollte. In Verbindung mit dem oben genanntem ist es wünschenswert, einfach zu veränderlich und entwickelbar zu sein, obwohl Änderungen auf ein Minimum reduziert werden sollten, nachdem die Akteure einmal auf den Inhalt geeinigt. Da sich Bedürfnisse und Gegebenheiten ständig während eines Projekts ändern, sind Anpassungen  jedoch zu erwarten und müssen dokumentiert werden.

# 2. Aufgabenstellung und Zielsetzung

### Möbelgeschäft
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

# 3. Produktnutzung

Das Produkt (im Folgenden als Website bezeichnet) für das Möbelhaus Möbel-Hier soll die Möglichkeit bieten das Sortiment des Möbelhauses auch im Internet für Kunden bereitzustellen und die interne Verwaltung für die Angestellten des Möbelhauses zu erleichtern.

Den Kunden werden auf der Website verschiedene Funktionalitäten bereitgestellt. 
Die primäre Funktion ist das betrachten des Kataloges des Möbelhauses für alle Besucher der Website. Die Website stellt für neue Kunden eine Registrierung in dem Onlineshop bereit und für bereits registrierte Nutzer gibt es eine Login-Funktion. Ein angemeldeter Kunde hat die Möglichkeit seine bisherigen Bestellungen bei dem Möbelhaus einzusehen und aktuell laufende Bestellungen bis zu einem bestimmten Zeitpunkt noch zu stornieren. Außerdem können angemeldete Kunden Artikel im Katalog bewerten und kommentieren. Ein Kunde, der bereits eine Bestellung aufgegeben hat, kann nachträglich auch noch die Lieferart verändern, beispielsweise nachträglich noch einen firmeneigenen LKW zur Abholung mieten oder die Bestellung von einem anderen Abholpunkt des Möbelhauses abzuholen.

Die Website stellt für die Angestellten des Möbelhauses die folgenden Funktionalitäten zur Verfügung.
Um eine Bestellung aufzunehmen, ruft ein registrierter Kunden bei einem Mitarbeiter des Möbelhauses an, welcher für ihn dann die Bestellung tätigt. Dazu legt der Mitarbeiter für den Kunden einen Warenkorb an, dem er Artikel aus dem Katalog hinzufügen und auch wieder entfernen kann. Ein Artikel kann dabei ein einzelnes Teil oder ein komplettes Möbelstück sein, wobei es jeden Artikel in verschiedenen Farbausführungen gibt, zwischen denen der Kunde wählen kann. Wenn sich alle gewünschten Artikel des Kunden in dem Warenkorb befinden, kann der Mitarbeiter dem Kunden den Gesamtpreis der Bestellung berechnen und die Bestellung für den Kunden abschließen. Der Mitarbeiter kann außerdem Benutzeraccounts von Kunden, die lange nicht mehr auf der Website aktiv waren, deaktivieren, jedoch nicht löschen.
Die Website dient außerdem der Verwaltung des Lagers und des Katalogs des Möbelhauses. Ein Mitarbeiter kann Artikel dem Katalog ( und damit auch dem Lager) hinzufügen, den Preis von Artikeln ändern, Artikel aus dem Katalog nehmen, wodurch sie jedoch nur für Kunden unsichtbar gemacht und nicht gelöscht werden und den Bestand einzelner Artikel im Lager aufstocken.

Der Administrator dient als oberste Verwaltungsinstanz der Website. Er besitzt die Möglichkeit Mitarbeiter in das System hinzuzufügen und zu deaktivieren. Außerdem ist er die einzige Instanz, die die monatliche Finanzübersicht des Möbelhauses, welche monatlich automatisch erstellt wird, einsehen kann.


# 4. Interessensgruppen (Stakeholders)

<table>






<thead>
<tr>
<th>Name</th>
    
<th>Priorität (1..5)</th>
    
<th>Beschreibung</th>
    
<th>Ziele</th>

</tr>
</thead>
<tbody>
<tr>
    
    
    
<td><p>Tutor</p></td>
<td><p>5</p></td>
    
<td><p>Der Hauptauftragsgeber dieses Projektes.</p></td>
<td><div><div>
<ul>
<li>
    
<p>Mehr Möbel verkaufen</p>
</li>
<li>
    
<p>Prozess automatisieren</p>
</li>
<li>
    
<p>Software Auftrag simulieren</p>
</li>
<li>
    
<p>Bestellungen einsehen</p>
</li>
<li>
    
<p>Kunden verwalten</p>
</li>
</ul>
</div></div></td>
</tr>
<tr>
    
    
    
<td><p>Kunden</p></td>
<td><p>3</p></td>
    
<td><p>Die Kunden des Möbelgeschäfts.</p></td>
<td><div><div>
<ul>
<li>
    
<p>bestellung von einem oder mehreren Möbelstücken</p>
</li>
</ul>
<ul>
<li>
    
<p>mieten eines Lkw's</p>
</li>
</ul>
</div></div></td>
</tr>
  <tbody>
<tr>
    
    
    
<td><p>Mitarbeiter</p></td>
<td><p>4</p></td>
    
<td><p>Hauptnutzer der Seite.</p></td>
<td><div><div>
<ul>
<li>
    
<p>Verwaltung des Warenkorbs und der Bestellungen </p>
</li>
<li>
    
<p>Lieferungen managen</p>
</li>
<li>
    
<p>Katalog einsehen </p>
</li>
<li>
    
<p></p>
</li>
</ul>
</div></div></td>
</tr>
  
<tr>
<td><p>Administratoren</p></td>
<td><p>2</p></td>
    
<td><p>Die berechtigten Mitarbeiter, welche die Website und Mitarbeiter managen können. (e.g. overview all orders)</p></td>
<td><div><div>
<ul>
<li>
    
<p>Mitarbeiter verwalten</p>
</li>
</ul>
</div></div></td>
</tr>
<tr>
    
    
    
<td><p>Entwickler (Studenten)</p></td>
<td><p>1</p></td>

<td><p>Die Praktikums Gruppe swt18w34.</p></td>
<td><div><div>
<ul>
<li>
    
<p>Erweiterbare Software</p>
</li>
<li>
    
<p>keine Wartungsarbeiten</p>
</li>
<li>
   
    
<p>Aufwand gering wie möglich</p>
</li>
<li>
    
    
<p>Nutzen hoch wie nötig</p>
</li>
</ul>
</div></div></td>
</tr>
</tbody>
</table>

# 5. Systemgrenze und Top-Level-Architektur

## 5.1. Kontextdiagramm
![Kontextdiagramm](https://github.com/st-tu-dresden-praktikum/swt18w34/blob/master/asciidoc/models/analysis/Kontextdiagramm.jpg "Kontextdiagramm")

Auf das Webshopsystem greifen 4 Benutzerklassen zu. Das sind zum einen "interne" Zugriffe, das heißt von Mitarbeitern der Firma, die auf der rechten Seite stehen, und zum anderen externe Zugriffe durch (nicht)-authentifizierte Kunden. Genaueres zu diesen Rollen gibt es im Kapitel 6 "Anwendungsfälle". Welche Bereiche die einzelnen User nutzen können, kann man in der Top-Level-Architektur hier unter sehen.

## 5.2. Top-Level-Architektur

![Top-Level-Architektur](https://github.com/st-tu-dresden-praktikum/swt18w34/blob/master/asciidoc/models/analysis/TopLevelArchitektur.jpg "Top-Level-Architektur")
# 6. Anwendungsfälle

## 6.1. Akteure

Akteure sind die Benutzer des Software-Systems oder Nachbarsysteme, welche darauf zugreifen. Dokumentieren Sie die Akteure in einer Tabelle. Diese Tabelle gibt einen Überblick über die Akteure und beschreibt sie kurz. Die Tabelle hat also mindestens zwei Spalten (Akteur Name und Kommentar). Weitere relevante Spalten können bei Bedarf ergänzt werden.

| Name                        | Beschreibung                                                                                                                                                        |
| --------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| User                        | Dies ist jede Person, die mit der Software interagieret                                                                                                             |
| Authentifizierter Kunde     | Diese Personen sind im System als Kunden registriert und eingeloggt, sodass sie sich Artikel kaufen können und weitere Funktionen nutzen.                           |
| Nichtauthentifizierter User | Diese Person nutzt das System nur eingeschränkt, weil sie entweder nicht eingeloggt oder nicht im System registriert ist.                                           |
| Mitarbeiter                 | Diese Personen sind Mitarbeiter des Möbelgeschäfts und können die Mitarbeiterfunktionen nutzen, wie z.B. auf das Lager zugreifen oder Bestellungen entgegen nehmen. |
| Admin                       | Dies ist ein spezieller Mitarbeiter, der erweiterter Berechtigungen im System besitz und verschiedene Informationen bearbeiten kann.                                |

## 6.2. Überblick Anwendungsfalldiagramm

Anwendungsfall-Diagramm, das alle Anwendungsfälle und alle Akteure darstellt

![Anwendungsfalldiagramm](https://github.com/st-tu-dresden-praktikum/swt18w34/blob/master/asciidoc/models/analysis/UseCaseDiagramm.jpg "Anwendungsfalldiagramm")
=== Anwendungsfallbeschreibungen

Dieser Unterabschnitt beschreibt die Anwendungsfälle. In dieser Beschreibung müssen noch nicht alle Sonderfälle und Varianten berücksichtigt werden. Schwerpunkt ist es, die wichtigsten Anwendungsfälle des Systems zu finden. Wichtig sind solche Anwendungsfälle, die für den Auftraggeber, den Nutzer den größten Nutzen bringen.

Für komplexere Anwendungsfälle ein UML-Sequenzdiagramm ergänzen. Einfache Anwendungsfälle mit einem Absatz beschreiben.

Die typischen Anwendungsfälle (Anlegen, Ändern, Löschen) können zu einem einzigen zusammengefasst werden.

![Kunden registrieren](https://github.com/st-tu-dresden-praktikum/swt18w34/blob/master/asciidoc/models/analysis/SequenzdiagrammKundenRegistrieren.jpg)
Kunden registrieren
<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td><p>ID</p></td>
<td>U1</td>
</tr>
<tr class="even">
<td><p>Name</p></td>
<td><p>Registrieren</p></td>
</tr>
<tr class="odd">
<td><p>Beschreibung</p></td>
<td><p>Hinzufügen eines neuen Kundens in die Datenbank</p></td>
</tr>
<tr class="even">
<td><p>Akteur</p></td>
<td><p>Nichtauthentifizierter Kunde</p></td>
</tr>
<tr class="odd">
<td><p>Auslöser</p></td>
<td><p>Auswahl des Registrierungsbuttons auf der Website</p></td>
</tr>
<tr class="even">
<td><p>Vorraussetzungen</p></td>
<td><p>Der User ist nicht eingeloggt.</p></td>
</tr>
<tr class="odd">
<td><p>Schrittfolge</p></td>
<td><ol type="1">
<li><p>Eingabe der Eingabedaten(Name, Adresse, E-mail, Password …​)</p></li>
<li><p>Überprüfung auf Vollständig-und Verfügbarkeit</p></li>
<li><p>Hinzufügen zum System</p></li>
</ol></td>
</tr>
<tr class="even">
<td><p>Erweiterungen</p></td>
<td><p>-</p></td>
</tr>
<tr class="odd">
<td><p>Funktionale Anforderungen</p></td>
<td>F2, F3</td>
</tr>
</tbody>
</table>

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td><p>ID</p></td>
<td>U2</td>
</tr>
<tr class="even">
<td><p>Name</p></td>
<td><p>LogIn/LogOut</p></td>
</tr>
<tr class="odd">
<td><p>Beschreibung</p></td>
<td><p>LogIn ermöglicht Authentifizieren im System für weitere Funktionalitäten. Dies kann mit LogOut umgekehrt werden.</p></td>
</tr>
<tr class="even">
<td><p>Akteur</p></td>
<td><p>User</p></td>
</tr>
<tr class="odd">
<td><p>Auslöser</p></td>
<td><p><em>LogIn</em>: Auswahl des LogIn Buttons</p>
<p><em>LogOut</em>: Auswahl des LogOut Buttons</p></td>
</tr>
<tr class="even">
<td><p>Vorraussetzungen</p></td>
<td><p><em>LogIn</em>: Benutzer ist nicht authentifiziert.</p>
<p><em>LogOut</em>: Benutzer ist bereits eingeloggt.</p></td>
</tr>
<tr class="odd">
<td><p>Schrittfolge</p></td>
<td><p><em>LogIn</em>:</p>
<p>1.Auswahl des LogIn Buttons</p>
<p>2.Eingabe der Daten</p>
<p>3.Überprüfung der Daten</p>
<p>4.Abhängig von der Eingabe</p>
<ol type="1">
<li><p>Wenn die Daten richtig sind, ist der User nun authentifiziert und landet auf seiner persönlichen Seite.</p></li>
<li><p>Sonst wird eine Fehlermeldung ausgegeben.</p></li>
</ol>
<p><em>LogOut</em>:</p>
<ol type="1">
<li><p>Auswahl des LogOut Buttons</p></li>
<li><p>Der User ist nun nicht mehr authentifiziert und landet auf der Hauptseite.</p></li>
</ol></td>
</tr>
<tr class="even">
<td><p>Erweiterungen</p></td>
<td><p>-</p></td>
</tr>
<tr class="odd">
<td><p>Funktionale Anforderungen</p></td>
<td>F1, F3</td>
</tr>
</tbody>
</table>

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td><p>ID</p></td>
<td>U3</td>
</tr>
<tr class="even">
<td><p>Name</p></td>
<td><p>Kundenkonto deaktivieren</p></td>
</tr>
<tr class="odd">
<td><p>Beschreibung</p></td>
<td><p>Ein Kundenkonto kann deaktiviert werden und dann nicht mehr benutzt werden können. Dafür werden die meisten Daten entfernt. Es ist nicht möglich ein Kundenkonto komplett zu</p></td>
</tr>
<tr class="even">
<td><p>Akteur</p></td>
<td><p>Authentifizierter User</p></td>
</tr>
<tr class="odd">
<td><p>Auslöser</p></td>
<td><p>Auswahl von Deaktivieren in der Kundenseite.</p></td>
</tr>
<tr class="even">
<td><p>Vorraussetzungen</p></td>
<td><p>Kunde ist eingeloggt.</p></td>
</tr>
<tr class="odd">
<td><p>Schrittfolge</p></td>
<td><p>1.Auswahl von Deaktivieren</p>
<p>2.Deaktivierung des Accounts, sodass keine weiteren Aktionen durchgeführt werden können.</p>
<p>3.Verbergen von Kundendaten, sodass diese nicht einsehbar sind.</p>
<p>4.Rückkehr als nicht eingeloggter Kunde auf die Startseite.</p></td>
</tr>
<tr class="even">
<td><p>Erweiterungen</p></td>
<td><p>-</p></td>
</tr>
<tr class="odd">
<td><p>Funktionale Anforderungen</p></td>
<td>F3, F18</td>
</tr>
</tbody>
</table>

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td><p>ID</p></td>
<td>U4</td>
</tr>
<tr class="even">
<td><p>Name</p></td>
<td><p>Bestellübersicht betrachten</p></td>
</tr>
<tr class="odd">
<td><p>Beschreibung</p></td>
<td><p>Ein Benutzer sieht hier alle Bestellungen, die für ihn sind. Ein Mitarbeiter sieht alle Bestellungen des Geschäfts.</p></td>
</tr>
<tr class="even">
<td><p>Akteur</p></td>
<td><p>Authentifizierter User, Mitarbeiter</p></td>
</tr>
<tr class="odd">
<td><p>Auslöser</p></td>
<td><p>Auswahl der Bestellübersicht</p></td>
</tr>
<tr class="even">
<td><p>Vorraussetzungen</p></td>
<td><p>Mitarbeiter bzw. Kunde ist eingeloggt.</p></td>
</tr>
<tr class="odd">
<td><p>Schrittfolge</p></td>
<td><p>1.Auswahl des Bestellübersichtsbutton</p>
<p>2.Anzeige der Bestellungen abhängig von der Rolle im System.</p></td>
</tr>
<tr class="even">
<td><p>Erweiterungen</p></td>
<td><p>-</p></td>
</tr>
<tr class="odd">
<td><p>Funktionale Anforderungen</p></td>
<td>F3, F16</td>
</tr>
</tbody>
</table>

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td><p>ID</p></td>
<td>U5</td>
</tr>
<tr class="even">
<td><p>Name</p></td>
<td><p>Filtern und Suchen</p></td>
</tr>
<tr class="odd">
<td><p>Beschreibung</p></td>
<td><p>Im Katalog kann nach bestimmten Namesbestandteilen(z.B. Schreibtisch) gesucht werden oder über Eigenschaften(z.B. Farbe = Fichte; Kategorie = Sofa) gesucht werden.</p></td>
</tr>
<tr class="even">
<td><p>Akteur</p></td>
<td><p>User</p></td>
</tr>
<tr class="odd">
<td><p>Auslöser</p></td>
<td><p><em>Filtern</em>: Auswahl einer Eigenschaft zum Filtern</p>
<p><em>Suchen</em>: Eingabe eines Suchworts in die Suchleiste</p></td>
</tr>
<tr class="even">
<td><p>Vorraussetzungen</p></td>
<td><p>-</p></td>
</tr>
<tr class="odd">
<td><p>Schrittfolge</p></td>
<td><p><em>Filtern</em>:</p>
<p>1.Auswahl einer Eigenschaft nach der gefiltert werden soll und Auswahl des &quot;Wertes&quot; dieser Eigenschaft</p>
<p>2.Aktualisierung der Website und Anzeige aller Artikel, die diese Eigenschaft erfüllen.</p>
<p><em>Suchen</em>:</p>
<p>1.Eingabe eines Suchwortes in die Suchleiste</p>
<p>2.Ermittlung aller Elemente, die dieses Suchwort im Titel enthalten</p>
<p>3.Anzeige dieser</p></td>
</tr>
<tr class="even">
<td><p>Erweiterungen</p></td>
<td><p>-</p></td>
</tr>
<tr class="odd">
<td><p>Funktionale Anforderungen</p></td>
<td>F7, F8</td>
</tr>
</tbody>
</table>

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td><p>ID</p></td>
<td>U6</td>
</tr>
<tr class="even">
<td><p>Name</p></td>
<td><p>Daten bearbeiten</p></td>
</tr>
<tr class="odd">
<td><p>Beschreibung</p></td>
<td><p>Die Daten eines Kundens(z.B. E-Mail Adresse, Anschrift) können von ihm im Falle einer Veränderung auf der Website bearbeitet werden</p></td>
</tr>
<tr class="even">
<td><p>Akteur</p></td>
<td><p>User</p></td>
</tr>
<tr class="odd">
<td><p>Auslöser</p></td>
<td><p>Drücken des Bearbeiten-Buttons auf der persönlichen Seite des Kunden.</p></td>
</tr>
<tr class="even">
<td><p>Vorraussetzungen</p></td>
<td><p>Der User ist eingeloggt.</p></td>
</tr>
<tr class="odd">
<td><p>Schrittfolge</p></td>
<td><p>1.Drücken des Bearbeiten-Buttons und Anzeigen der Daten</p>
<p>2.Ersetzung von Daten durch den User</p>
<p>3.Auswahl von Speichern und Änderung im System</p></td>
</tr>
<tr class="even">
<td><p>Erweiterungen</p></td>
<td><p>-</p></td>
</tr>
<tr class="odd">
<td><p>Funktionale Anforderungen</p></td>
<td>F16, F1</td>
</tr>
</tbody>
</table>

![stornieren](https://github.com/st-tu-dresden-praktikum/swt18w34/blob/master/asciidoc/models/analysis/SequenzdiagrammStornieren.jpg)
Bestellung stornieren
<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td><p>ID</p></td>
<td>U7</td>
</tr>
<tr class="even">
<td><p>Name</p></td>
<td><p>Bestellung stornieren</p></td>
</tr>
<tr class="odd">
<td><p>Beschreibung</p></td>
<td><p>Ein Benutzer kann bis zu einem gewissen Zeitlimit eine Bestellung abbrechen.</p></td>
</tr>
<tr class="even">
<td><p>Akteur</p></td>
<td><p>Authentifizierter User</p></td>
</tr>
<tr class="odd">
<td><p>Auslöser</p></td>
<td><p>Auswahl von Stornieren in der Bestellübersicht</p></td>
</tr>
<tr class="even">
<td><p>Vorraussetzungen</p></td>
<td><p>Es existieren Bestellungen in der Übersicht</p></td>
</tr>
<tr class="odd">
<td><p>Schrittfolge</p></td>
<td><p>1.Auswahl von Stornieren in der Bestellübersicht</p>
<p>2.Überprüfung ob stornierbar</p>
<ol type="1">
<li><p>Sofern möglich, Entfernung der Bestellung und Hinzufügen der Elemente zum Lager und Zurücküberweisung des Preises.</p></li>
<li><p>Sofern nicht möglich, Fehlermeldung und Rückkehr in die Übersicht</p></li>
</ol></td>
</tr>
<tr class="even">
<td><p>Erweiterungen</p></td>
<td><p>-</p></td>
</tr>
<tr class="odd">
<td><p>Funktionale Anforderungen</p></td>
<td>F1, F16, F20</td>
</tr>
</tbody>
</table>

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td><p>ID</p></td>
<td>U8</td>
</tr>
<tr class="even">
<td><p>Name</p></td>
<td><p>Artikel betrachten</p></td>
</tr>
<tr class="odd">
<td><p>Beschreibung</p></td>
<td><p>Im Katalog können einzelne Artikel ausgewählt werden und ihre Informationen wie Bild oder Beschreibung angezeigt werden.</p></td>
</tr>
<tr class="even">
<td><p>Akteur</p></td>
<td><p>User</p></td>
</tr>
<tr class="odd">
<td><p>Auslöser</p></td>
<td><p>Auswählen eines Artikels im Katalog</p></td>
</tr>
<tr class="even">
<td><p>Vorraussetzungen</p></td>
<td><p>-</p></td>
</tr>
<tr class="odd">
<td><p>Schrittfolge</p></td>
<td><p>1.Auswahl eines Artikels im Katalog</p>
<p>2.Öffnen der Artikelübersicht und Anzeige von Beschreibung, Bild, Preis, Kommentaren und Bewertungen</p></td>
</tr>
<tr class="even">
<td><p>Erweiterungen</p></td>
<td><p>-</p></td>
</tr>
<tr class="odd">
<td><p>Funktionale Anforderungen</p></td>
<td>F7, F8</td>
</tr>
</tbody>
</table>

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td><p>ID</p></td>
<td>U9</td>
</tr>
<tr class="even">
<td><p>Name</p></td>
<td><p>Katalog betrachten</p></td>
</tr>
<tr class="odd">
<td><p>Beschreibung</p></td>
<td><p>Im Webshop können alle Artikel im Katalog angezeigt werden</p></td>
</tr>
<tr class="even">
<td><p>Akteur</p></td>
<td><p>User</p></td>
</tr>
<tr class="odd">
<td><p>Auslöser</p></td>
<td><p>Auswahl des Katalogs auf der Website</p></td>
</tr>
<tr class="even">
<td><p>Vorraussetzungen</p></td>
<td><p>-</p></td>
</tr>
<tr class="odd">
<td><p>Schrittfolge</p></td>
<td><p>1.Auswahl des Katalogs</p>
<p>2.Anzeige aller nicht versteckten Artikel</p></td>
</tr>
<tr class="even">
<td><p>Erweiterungen</p></td>
<td><p>-</p></td>
</tr>
<tr class="odd">
<td><p>Funktionale Anforderungen</p></td>
<td>F7</td>
</tr>
</tbody>
</table>

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td><p>ID</p></td>
<td>U10</td>
</tr>
<tr class="even">
<td><p>Name</p></td>
<td><p>Artikel kommentieren und bewerten</p></td>
</tr>
<tr class="odd">
<td><p>Beschreibung</p></td>
<td><p>Die Daten eines Kundens(z.B. E-Mail Adresse, Anschrift) können von ihm im Falle einer Veränderung auf der Website bearbeitet werden</p></td>
</tr>
<tr class="even">
<td><p>Akteur</p></td>
<td><p>Authentifizierter User</p></td>
</tr>
<tr class="odd">
<td><p>Auslöser</p></td>
<td><p>Eingabe eines Kommentars in der Artikelübersicht oder Auswahl einer Bewertung zwischen 1 bis 5</p></td>
</tr>
<tr class="even">
<td><p>Vorraussetzungen</p></td>
<td><p>Der User ist eingeloggt.</p></td>
</tr>
<tr class="odd">
<td><p>Schrittfolge</p></td>
<td><p>1.Eingabe eines Kommentars bzw. einer Bewertung</p>
<p>2.Auswahl zum Speichern und damit Hinzufügen zur Datenbank und damit zur Artikelübersicht</p></td>
</tr>
<tr class="even">
<td><p>Erweiterungen</p></td>
<td><p>Mögliche Überprüfung, ob der Artikel wirklich erworben wurde.</p></td>
</tr>
<tr class="odd">
<td><p>Funktionale Anforderungen</p></td>
<td>F7, F10, F9</td>
</tr>
</tbody>
</table>

![Bestellung aufnehmen und bestellen](https://github.com/st-tu-dresden-praktikum/swt18w34/blob/master/asciidoc/models/analysis/SequenzdiagrammBestellungAufnehmen.jpg)
Bestellung aufnehmen und bestellen
<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td><p>ID</p></td>
<td>U11</td>
</tr>
<tr class="even">
<td><p>Name</p></td>
<td><p>Artikel hinzufügen/entfernen</p></td>
</tr>
<tr class="odd">
<td><p>Beschreibung</p></td>
<td><p>Ein Mitarbeiter kann zum Warenkorb eines Kundes einen neuen Artikel hinzufügen oder auch entfernen damit dieser für ihn bestellt werden kann.</p></td>
</tr>
<tr class="even">
<td><p>Akteur</p></td>
<td><p>Mitarbeiter</p></td>
</tr>
<tr class="odd">
<td><p>Auslöser</p></td>
<td><p><em>Hinzufügen</em>: Auswahl eines Artikels im Katalog durch den Mitarbeiter</p>
<p><em>Entfernen</em>: Auswahl eines Artikels im Warenkorb</p></td>
</tr>
<tr class="even">
<td><p>Vorraussetzungen</p></td>
<td><p><em>Hinzufügen</em>: Vorhandensein des Artikels</p>
<p><em>Entfernen</em>: Vorhandensein eines Artikels im Warenkorb</p></td>
</tr>
<tr class="odd">
<td><p>Schrittfolge</p></td>
<td><p><em>Hinzufügen</em>:</p>
<p>1.Auswahl eines Artikels im Katalog zum Hinzufügen</p>
<p>2.Anzeige des aktualisierten Katalogs und Aktualisierung des Preises</p>
<p><em>Entfernen</em>:</p>
<p>1.Auswählen des Entfernenbuttons neben dem Artikel im Warenkorb.</p>
<p>2.Anzeige des aktualisierten Katalogs und Aktualisierung des Preises</p></td>
</tr>
<tr class="even">
<td><p>Erweiterungen</p></td>
<td><p>-</p></td>
</tr>
<tr class="odd">
<td><p>Funktionale Anforderungen</p></td>
<td>F7, F21</td>
</tr>
</tbody>
</table>

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td><p>ID</p></td>
<td>U12</td>
</tr>
<tr class="even">
<td><p>Name</p></td>
<td><p>Bestellung abschließen</p></td>
</tr>
<tr class="odd">
<td><p>Beschreibung</p></td>
<td><p>Nachdem alle gewünschten Artikel im Warenkorb ausgewählt wurden, muss die Bestellung abgeschlossen werden, damit die Produkte versendet werden.</p></td>
</tr>
<tr class="even">
<td><p>Akteur</p></td>
<td><p>Mitarbeiter</p></td>
</tr>
<tr class="odd">
<td><p>Auslöser</p></td>
<td><p>Drücken des Abschließenbutton in der Warenübersicht</p></td>
</tr>
<tr class="even">
<td><p>Vorraussetzungen</p></td>
<td><p>Vorhandensein von mindestens einem Artikel im Warenkorb</p></td>
</tr>
<tr class="odd">
<td><p>Schrittfolge</p></td>
<td><p>1.Auswählen des Abschließenbuttons</p>
<p>2.Überprüfung ob alle Artikel lieferbar sind</p>
<ol type="1">
<li><p>Wenn nicht lieferbar, Ausgabe einer Fehlermeldung und Rückkehrt in den Warenkorb</p></li>
<li><p>Sonst Entfernung der Artikelbestände im Lager und Hinzufügen einer Bestellung in der Bestellungsübersicht</p></li>
</ol></td>
</tr>
<tr class="even">
<td><p>Erweiterungen</p></td>
<td><p>-</p></td>
</tr>
<tr class="odd">
<td><p>Funktionale Anforderungen</p></td>
<td>F11, F12, F7</td>
</tr>
</tbody>
</table>

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td><p>ID</p></td>
<td>U13</td>
</tr>
<tr class="even">
<td><p>Name</p></td>
<td><p>LKW mieten</p></td>
</tr>
<tr class="odd">
<td><p>Beschreibung</p></td>
<td><p>Zu einer Bestellung kann ein Kunde einen LKW dazumieten um diese zu transportieren</p></td>
</tr>
<tr class="even">
<td><p>Akteur</p></td>
<td><p>Mitarbeiter</p></td>
</tr>
<tr class="odd">
<td><p>Auslöser</p></td>
<td><p>Auswahl des LKW-Buttons in der Bestellübersicht</p></td>
</tr>
<tr class="even">
<td><p>Vorraussetzungen</p></td>
<td><p>Vorhandensein von Artikeln und kein verwendeter LKW</p></td>
</tr>
<tr class="odd">
<td><p>Schrittfolge</p></td>
<td><p>1.Auswahl des LKW-Buttons in der Bestellübersicht</p>
<p>2.Ermittlung des bestmöglichen LKWs(d.h. mit möglichst geringen Preis, aber ausreichender Kapazität)</p>
<p>3.Hinzufügen der LKW-Leihe zum Warenkorb</p>
<p>4.Reservierung des LKWs hinzufügen</p>
<p>5.Warenkorb aktualisieren</p></td>
</tr>
<tr class="even">
<td><p>Erweiterungen</p></td>
<td><p>-</p></td>
</tr>
<tr class="odd">
<td><p>Funktionale Anforderungen</p></td>
<td>F14, F15, F11, F5, F4</td>
</tr>
</tbody>
</table>

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td><p>ID</p></td>
<td>U14</td>
</tr>
<tr class="even">
<td><p>Name</p></td>
<td><p>Artikel bearbeiten</p></td>
</tr>
<tr class="odd">
<td><p>Beschreibung</p></td>
<td><p>Die Eigenschaften eines Artikels wie Beschreibung oder Preis können durch Mitarbeiter geändert werden.</p></td>
</tr>
<tr class="even">
<td><p>Akteur</p></td>
<td><p>Mitarbeiter</p></td>
</tr>
<tr class="odd">
<td><p>Auslöser</p></td>
<td><p>Auswahl von Bearbeiten als eingeloggter Mitarbeiter in der Artikelübersicht</p></td>
</tr>
<tr class="even">
<td><p>Vorraussetzungen</p></td>
<td><p>Der Mitarbeiter ist eingeloggt und der Artikel existiert</p></td>
</tr>
<tr class="odd">
<td><p>Schrittfolge</p></td>
<td><p>1.Auswahl von Bearbeiten als eingeloggter Mitarbeiter in der Artikelübersicht</p>
<p>2.Eingabe neuer Daten</p>
<p>3.Speichern neuer Informationen und Rückkehr zur Artikelübersicht</p></td>
</tr>
<tr class="even">
<td><p>Erweiterungen</p></td>
<td><p>-</p></td>
</tr>
<tr class="odd">
<td><p>Funktionale Anforderungen</p></td>
<td>F1, F5, F9</td>
</tr>
</tbody>
</table>

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td><p>ID</p></td>
<td>U15</td>
</tr>
<tr class="even">
<td><p>Name</p></td>
<td><p>Artikel nachbestellen</p></td>
</tr>
<tr class="odd">
<td><p>Beschreibung</p></td>
<td><p>Die Anzahl der Artikel im Lager kann durch einen Mitarbeiter erhöht werden, damit diese weiterhin geliefert werden können.</p></td>
</tr>
<tr class="even">
<td><p>Akteur</p></td>
<td><p>Mitarbeiter</p></td>
</tr>
<tr class="odd">
<td><p>Auslöser</p></td>
<td><p>Auswahl von Nachbestellen als eingeloggter Mitarbeiter im Artikel</p></td>
</tr>
<tr class="even">
<td><p>Vorraussetzungen</p></td>
<td><p>Der Mitarbeiter ist eingeloggt und der Artikel existiert</p></td>
</tr>
<tr class="odd">
<td><p>Schrittfolge</p></td>
<td><p>1.Auswahl von Nachbestellen</p>
<p>2.Eingabe der Anzahl, die nachbestellt werden soll.</p>
<p>3.Hinzufügen einer Bestellung für das Lager</p></td>
</tr>
<tr class="even">
<td><p>Erweiterungen</p></td>
<td><p>-</p></td>
</tr>
<tr class="odd">
<td><p>Funktionale Anforderungen</p></td>
<td>F1, F6</td>
</tr>
</tbody>
</table>



![Artikel zum Lager hinzufügen](https://github.com/st-tu-dresden-praktikum/swt18w34/blob/master/asciidoc/models/analysis/SequenzdiagrammArtikelHinzuf%C3%BCgen.jpg)
Artikel zum Lager hinzufügen
<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td><p>ID</p></td>
<td>U16</td>
</tr>
<tr class="even">
<td><p>Name</p></td>
<td><p>Artikel zum Lager hinzufügen</p></td>
</tr>
<tr class="odd">
<td><p>Beschreibung</p></td>
<td><p>Dem Lager können durch Mitarbeiter neue Artikel wie Teile oder Möbel hinzugefügt werden</p></td>
</tr>
<tr class="even">
<td><p>Akteur</p></td>
<td><p>Mitarbeiter</p></td>
</tr>
<tr class="odd">
<td><p>Auslöser</p></td>
<td><p>Auswahl von neuen Artikel zu Sortiment hinzufügen als angemeldeter Mitarbeiter</p></td>
</tr>
<tr class="even">
<td><p>Vorraussetzungen</p></td>
<td><p>Der Mitarbeiter ist eingeloggt</p></td>
</tr>
<tr class="odd">
<td><p>Schrittfolge</p></td>
<td><p>1.Auswahl von neuen Artikel zum Sortiment hinzufügen</p>
<p>2.Eigenschaften des neuen Artikels eingeben.</p>
<p>3. Artikel erstellen</p>
<p>4. Artikel zur Liste aller Artikel hinzufügen</p></td>
</tr>
<tr class="even">
<td><p>Erweiterungen</p></td>
<td><p>-</p></td>
</tr>
<tr class="odd">
<td><p>Funktionale Anforderungen</p></td>
<td>F1, F21, F6</td>
</tr>
</tbody>
</table>

![Mitarbeiter hinzufügen](https://github.com/st-tu-dresden-praktikum/swt18w34/blob/master/asciidoc/models/analysis/SequenzdiagrammMitarbeiterErstellen.jpg)
Mitarbeiter hinzufügen
<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td><p>ID</p></td>
<td>U17</td>
</tr>
<tr class="even">
<td><p>Name</p></td>
<td><p>Personal hinzufügen/bearbeiten/löschen</p></td>
</tr>
<tr class="odd">
<td><p>Beschreibung</p></td>
<td><p>Der Admin kann Mitarbeiter entfernen(=entlassen), hinzufügen(=einstellen) oder ihre Informationen arbeiten</p></td>
</tr>
<tr class="even">
<td><p>Akteur</p></td>
<td><p>Admin</p></td>
</tr>
<tr class="odd">
<td><p>Auslöser</p></td>
<td><p>Auswahl von Hinzufügen/Bearbeiten/Löschen in der Personalübersicht</p></td>
</tr>
<tr class="even">
<td><p>Vorraussetzungen</p></td>
<td><p>Der Admin ist eingeloggt.</p>
<p><em>Bearbeiten/Löschen</em>: Der Mitarbeiter existiert.</p></td>
</tr>
<tr class="odd">
<td><p>Schrittfolge</p></td>
<td><p><em>Hinzufügen</em>:</p>
<p>1.Auswahl von Hinzufügen</p>
<p>2.Eingabe der Daten und Erzeugen eines Mitarbeites</p>
<ol type="1">
<li><p>Sofern möglich, Hinzufügen zur Mitarbeiterübersicht</p></li>
<li><p>Sonst Fehlermeldung und Rückkehr zur Personalübersicht</p></li>
</ol>
<p><em>Bearbeiten</em>:</p>
<p>1.Auswahl von Bearbeiten</p>
<p>2.Eingabe der neuen Daten und Bearbeiten des Mitarbeites</p>
<ol type="1">
<li><p>Sofern möglich, Änderung in der Mitarbeiterübersicht</p></li>
<li><p>Sonst Fehlermeldung und Rückkehr zur Personalübersicht</p></li>
</ol>
<p><em>Löschen</em>:</p>
<p>1.Auswahl von Löschen</p>
<p>2.Entfernen des Mitarbeiters aus der Personalübersicht</p></td>
</tr>
<tr class="even">
<td><p>Erweiterungen</p></td>
<td><p>-</p></td>
</tr>
<tr class="odd">
<td><p>Funktionale Anforderungen</p></td>
<td><F1, F2, F3, F19/td>
</tr>
</tbody>
</table>

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td><p>ID</p></td>
<td>U18</td>
</tr>
<tr class="even">
<td><p>Name</p></td>
<td><p>Finanzübersicht</p></td>
</tr>
<tr class="odd">
<td><p>Beschreibung</p></td>
<td><p>Anzeigen der Ein-und Ausgaben, wie z.B. Personalkosten oder Einnahmen durch Möbelverkäufe</p></td>
</tr>
<tr class="even">
<td><p>Akteur</p></td>
<td><p>Admin</p></td>
</tr>
<tr class="odd">
<td><p>Auslöser</p></td>
<td><p>Auswahl der Finanzübersicht</p></td>
</tr>
<tr class="even">
<td><p>Vorraussetzungen</p></td>
<td><p>Der Admin ist eingeloggt.</p></td>
</tr>
<tr class="odd">
<td><p>Schrittfolge</p></td>
<td><p>1.Auswahl der Finanzübersicht auf der Website</p>
<p>2.Anzeigen der Ein-und Ausnahmen</p>
<p>3.Errechnung und Ausgabe der Bilanz für diesen Monat</p></td>
</tr>
<tr class="even">
<td><p>Erweiterungen</p></td>
<td><p>-</p></td>
</tr>
<tr class="odd">
<td><p>Funktionale Anforderungen</p></td>
<td>F1, F17</td>
</tr>
</tbody>
</table>

# 7. Funktionale Anforderungen

## 7.1. Muss-Kriterien

Was das zu erstellende Programm auf alle Fälle leisten muss.
<table>
  <body>
    <tr><th>ID</th><th>Name<th>Beschreibung</th></tr>
   <tr><td>F1</td><td>Authentifizierung<td>Es gibt Bereiche, die um betrachtet zu werden eine Authentifizierung brauchen. User des Systems(Mitarbeiter, Registrierte Kunden, Admins) können sich mittels eines Namens und ihres Passworts anmelden. </td></tr>
    <tr><td>F2</td><td>Registrierung<td>Um Funktion F1 zu erfüllen zu können, müssen User zum System hinzugefügt werden. Dies geschieht je nach Art der Person unterschiedlich und benötigt gewisse Daten.</td></tr>
    <tr><td>F3</td><td>Validierung von Eingabedaten<td>Für die Funktionen F1 und F2 müssen Eingaben auf ihre Richtigkeit überprüft werden können. Dies umfasst die Überprüfung ob, dass Password zum Username passt oder ob dieser Username noch frei ist.</td></tr>
    <tr><td>F4</td><td>Inventar<td>Das System speichert Informationen über die verschiedenen Möbel und Teil.</td></tr>
    <tr><td>F5</td><td>Bearbeitung des Inventars<td>Das Inventar aus Funktion F4 kann von Mitarbeitern/Admins verändert und bearbeitet werden.</td></tr>
    <tr><td>F6</td><td>Speicherung und Änderung der Anzahl<td>Zu jedem Möbel bzw.Teil gibt es Informationen über die Anzahl im Lager. Diese kann durch Nachbestellen erhöht als auch durch Kundenbestellungen verringert werden. </td></tr>
    <tr><td>F7</td><td>Katalog<td>Es gibt eine Übersicht über die Artikel, die von authentifizierten und nicht-authentifizierten Nutzern durchsucht werden kann. </td></tr>
    <tr><td>F8</td><td>Filtern des Katalogs<td>Der Katalog kann anhand von Kategorien(z.B. Suche nach Stühlen) gefiltert werden.</td></tr>
    <tr><td>F9</td><td>Ansehen von Artikeln<td>Im Katalog können einzelne Artikel zusammen mit ihren Informationen und Bewertungen betrachtet werden.</td></tr>
    <tr><td>F10</td><td>Bewerten<td>Einzelne Artikel können von authentifizierten Kunden mittels einer Bewertung und eines Kommentars versehen werden, die dann andere Kunden betrachten können.</td></tr>
    <tr><td>F11</td><td>Warenkorb<td>Es gibt für jeden registrierten Kunden einen persönlichen Warenkorb. Dort kann ein Mitarbeiter alle bisherigen Artikel und den Preis sehen.</td></tr>
    <tr><td>F12</td><td>Warenkorb bearbeiten<td>Ein Mitarbeiter kann zum Warenkorb Artikel aus dem Katalog hinzufügen oder auch wieder Artikel aus dem Warenkorb entfernen. Dieser wird danach aktualisiert.</td></tr>
    <tr><td>F13</td><td>Bestellen<td>Ein Mitarbeiter kann eine Bestellung aus einem Warenkorb erstellen.</td></tr>
    <tr><td>F14</td><td>Fuhrpark<td>Das System speichert Informationen über Fahrzeuge und diese können durch Mitarbeiter verwaltet werden.</td></tr>
    <tr><td>F15</td><td>Fahrzeugermittlung<td>Für jeden Warenkorb muss das System ein Fahrzeug aus dem Fuhrpark ermittlen, das diese Artikel transportieren könnte, oder ausgeben, dass es kein mögliches Fahrzeug gäbe.</td></tr>
    <tr><td>F16</td><td>Bestellübersicht<td>Das System kann jedem Kunden seine getätigten Bestellungen anzeigen oder einem Admin alle getätigten Bestellungen.</td></tr>
    <tr><td>F17</td><td>Finanzübersicht<td>Das System enthält Informationen über Einnahmen und Ausgaben und kann diese einem Admin anzeigen.</td></tr>
        <tr><td>F17</td><td>Daten bearbeiten<td>Die Daten eines Nutzers </td></tr>
              <tr><td>F18</td><td>Daten bearbeiten<td>Die Daten eines Nutzers können von ihm oder einem Admin geändert werden.</td></tr>
    <tr><td>F19</td><td>Userlisten<td>Ein Admin kann alle User oder auch User mit nur einer speziellen Rolle, wie Kunde oder Mitarbeiter sich anzeigen lassen.</td></tr>
    <tr><td>F20</td><td> Bestellungen stornieren<td>Das System muss überprüfen ob Bestellungen stornierbar sind und falls dies möglich ist die Effekte der Bestellung umkehren(das heißt Rücküberweisung des Geldes und Erhöhung der Artikelanzahl)</td></tr> 
      <tr><td>F21</td><td>Katalog bearbeiten<td>Neue Artikel können von Mitarbeitern zum Katalog hinzugefügt werden und bereits vorhandene bearbeitet bzw. versteckt werden.</td></tr>
    </body>
    </table>

## 7.2. Kann-Kriterien

Anforderungen die das Programm leisten können soll, aber für den korrekten Betrieb entbehrlich sind.

# 8. Nicht-Funktionale Anforderungen

## 8.1. Qualitätsziele


<table>
<body>
<tr>
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
</body>
</table>

(Bewertung von 1( nicht wichtig) bis 5 (sehr wichtig))

zur Organisierbarkeit: dieser Punkt beschreibt die Eignung der Software die Elemente im Onlineshop und allgemein im Möbelhaus zu verwalten

## 8.2. Konkrete Nicht-Funktionale Anforderungen


<table><body>
<tr><th> Qualitätsziel </th>
<th> Beschreibung</th>
<tr>
<td><b>Erweiterbarkeit</b></td>
<td>Das System soll sich zur Laufzeit dynamisch erweitern lassen können. Dies geschieht beispielsweise durch das Hinzufügen von Artikeln oder Kunden während der Laufzeit.</td>
</tr>
<tr>
<td><b>Wartbarkeit</b></td>
<td>Die Website kann leicht um weitere Funktionen erweitert werden und der Aufwand, um eventuelle Fehler im Programm zu beheben, wird durch seinen leicht verständlichen Programmaufbau und die Nutzung des MVC-Patterns so gering wie möglich gehalten. </td></tr>
<td><b>Benutzbarkeit</b></td>
<td>Das Webinterface ist so gestaltet, dass die Bedienung für Kunden wie auch Mitarbeiter einfach und intuitiv ist und es keine Schwierigkeiten oder Missverständnisse bei der Nutzung der Website gibt. </td></tr>
<tr>
<td><b>Zuverlässigkeit</b></td>
<td>Die Software soll den Nutzern zu jeder Zeit die gewünschte Funktionalität bieten. </td></tr>
<tr><td><b>Sicherheit</b></td>
<td> Jeder Nutzer besitzt einen eigenen Account, welcher von einem Passwort geschützt wird, um sicherzustellen, dass jeder Nutzer nur die ihm zur Verfügung stehenden Funktionen benutzen kann. Die gespeicherten Nutzerdaten sind außerdem von außen nicht einzusehen, was eine Entwendung der Daten verhindern soll.</td></tr>
<tr><td><b>Organisierbarkeit</b></td>
<td>Die Software bietet die Möglichkeit für die Mitarbeiter den Katalog zu bearbeiten und das Lager nachzufüllen, um den flüssigen Betrieb des Möbelhauses zu gewährleisten. Der Admin kann außerdem das Personal des Möbelhauses verwalten.</td></tr>
<tr> <td><b>Leistungsfähigkeit</b></td>
<td>Die Software soll den Nutzern ein flüssiges navigieren auf der Website ermöglichen und die gewünschten Aktionen schnell bearbeiten. </td></tr>

</body></table>


# 9. GUI Prototyp

In diesem Kapitel soll ein Entwurf der Navigationsmöglichkeiten und Dialoge des Systems erstellt werden. Idealerweise entsteht auch ein grafischer Prototyp, welcher dem Kunden zeigt, wie sein System visuell umgesetzt werden soll. Konkrete Absprachen - beispielsweise ob der grafische Prototyp oder die Dialoglandkarte höhere Priorität hat - sind mit dem Kunden zu treffen.

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

## 9.1 Überblick: Dialoglandkarte

Erstellen Sie ein Übersichtsdiagramm, das das Zusammenspiel Ihrer Masken zur Laufzeit darstellt. Also mit welchen Aktionen zwischen den Masken navigiert wird.

## 9.2 Dialogbeschreibung

![login](https://github.com/st-tu-dresden-praktikum/swt18w34/blob/master/asciidoc/GUI/login.png)

Klickt der Kunde auf der Startseite auf Login, öffnet gelangt er zu diesem Dialog, wo er aufgefordert wird, seine Login Daten einzugeben. Danach landet er auf dem in 9. beschriebenen Kundenprofil.

![registrieren](https://github.com/st-tu-dresden-praktikum/swt18w34/blob/master/asciidoc/GUI/registrieren.png)

Klickt der Kunde auf der Startseite auf registrieren, kann der Kunde in dieser Maske sein Kundenprofil erstellen. Er erhält im Anschluss eine Bestätigungsmail mit seiner Kundennummer.

![schreiben](https://github.com/st-tu-dresden-praktikum/swt18w34/blob/master/asciidoc/GUI/schreiben.png)

Ist der Kunde eingeloggt und befindet sich auf der Deatilansicht eines Artikels, kann gelangt er durch klicken des Buttons "Bewertung schreiben" zu dieser Eingabemaske. Hier hat der Kunde die Möglichkeit, eine eigene Bewertung zu verfassen.

![aktualisieren](https://github.com/st-tu-dresden-praktikum/swt18w34/blob/master/asciidoc/GUI/aktualisieren.png)

Befindet sich der eingeloggte Kunde auf seinem Profil, kann er durch einen Klick auf "Daten aktualisieren" seine Adresse, Email und Passwort ändern.

![gehalt](https://github.com/st-tu-dresden-praktikum/swt18w34/blob/master/asciidoc/GUI/gehalt.png)

Der Admin ist berechtigt, die Mitarbeiterliste einzusehen. Durch einen Klick auf das Gehalt, ist er in der Lage dieses anzupassen.

# 10. Datenmodell

## 10.1 Überblick: Klassendiagramm

![Analyseklassendiagramm](https://github.com/st-tu-dresden-praktikum/swt18w34/blob/master/asciidoc/models/analysis/Analyseklassendiagramm.jpg)

## 10.2 Klassen und Enumerationen

Dieser Abschnitt stellt eine Vereinigung von Glossar und der Beschreibung von Klassen/Enumerationen dar. Jede Klasse und Enumeration wird in Form eines Glossars textuell beschrieben. Zusätzlich werden eventuellen Konsistenz- und Formatierungsregeln aufgeführt.


<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td><p><b>Klasse</b></p></td>
<td><p><b>Beschreibung</b></p></td>
</tr>
<tr class="even">
<td><p>Webshop</p></td>
<td><p>zentraler Client, über den der Nutzer alle weiteren Funktionen bereitgestellt bekommt</p></td>
</tr>
<tr class="odd">
<td><p>Nutzer</p></td>
<td><p>Grundlage für die persönlichen Daten eines registrierten Nutzers der Website  </p></td>
</tr>
<tr>
<td><p>Kunde</p></td>
<td><p>erweitert die Klasse Nutzer um die gewünschte Bezahlmethode des Kunden</td>
</tr>
<tr>
<td><p>Mitarbeiter</p></td>
<td> Verwaltung der Attribute, die nur für den Mitarbeiter bzw. den Admin benötigt werden
</td>
</tr>
<tr>
<td><p>Artikel</p></td>
<td>Superklasse für die im Webshop zu bestellenden Artikel, welche grundlegende Eigenschaften dieser bereitstellt
</td>
</tr>
<tr>
<td><p>Möbel</p></td>
<td> ein Set bestehend aus verschiedenen Teilen
</td>
</tr>
<tr>
<td><p>Teil</p></td>
<td> stellt die kleinste bestellbare Einheit dar und bildet die Grundlage für die Berechnung von Preis und Gewicht einer Bestellung
</td>
</tr>
<tr>
<td><p>Katalog</p></td>
<td> dient der Organisation und Darstellung aller im Möbelhaus erhältlichen Artikel
</td>
</tr>
<tr>
<td><p>Warenkorb</p></td>
<td> ermöglicht dem Mitarbeiter die Erstellung einer Bestellung für einen Kunden
</td>
</tr>
<tr>
<td><p>Bestellung</p></td>
<td> stellt alle nötigen Informationen für eine Bestellung dar und bietet die Möglichkeit Änderungen am Bestellstatus vorzunehmen
</td>
</tr>
<tr>
<td><p>Bestellübersicht</p></td>
<td> zeigt dem Kunden seine bisherigen Bestellungen an und bietet dem Mitarbeiter die Möglichkeit alle Bestellungen des Möbelhauses einzusehen
</td>
</tr>
<tr>
<td><p>Finanzübersicht</p></td>
<td> stellt eine monatliche Übersicht von Einnahmen und Ausgaben bereit
</td>
</tr>
<tr>
<td><p>Lager</p></td>
<td> bietet die Funktionalität alle Artikel im physischen Lager zu verwalten
</td>
</tr>
<tr>
<td><p>Fahrzeug</p></td>
<td> speichert die Eigenschaften eines firmeneigenen Fahrzeugs
</td>
</tr>
<tr>
<td><p>Fuhrpark</p></td>
<td> Verwaltung der firmeneigenen Fahrzeuge
</td>
</tr>

</tbody>
</table>


# 11. Akzeptanztestfälle

Mithilfe von Akzeptanztests wird geprüft, ob die Software die funktionalen Erwartungen und Anforderungen im Gebrauch erfüllt. Diese sollen und können aus den Anwendungsfallbeschreibungen und den UML-Sequenzdiagrammen abgeleitet werden. D.h., pro (komplexen) Anwendungsfall gibt es typischerweise mindestens ein Sequenzdiagramm (welches ein Szenarium beschreibt). Für jedes Szenarium sollte es einen Akzeptanztestfall geben. Listen Sie alle Akzeptanztestfälle in tabellarischer Form auf. Jeder Testfall soll mit einer ID versehen werde, um später zwischen den Dokumenten (z.B. im Test-Plan) referenzieren zu können.

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td><p>ID</p></td>
<td><p>A1​</p></td>
</tr>
<tr class="even">
<td><p>Use Case</p></td>
<td><p>U1​</p></td>
</tr>
<tr class="odd">
<td><p>Voraussetzung</p></td>
<td><p>Nutzer möchte sich registrieren</p></td>
</tr>
<tr class="even">
<td><p>Ereignis</p></td>
<td><p>Nutzer drückt &quot;Registrieren&quot; und gibt seine Daten ein:</p>
<p>- Name: Max Mustermann</p>
<p>- Passwort: passwort</p>
<p>- Adresse: Musterstraße 10 01217 Dresden</p>
<p>- E-mail: <a href="mailto:Max.Musterman@gmx.de">Max.Musterman@gmx.de</a></p>
<p>- Zahlinformationen: Bar</p>
<p>dann drückt er &quot;Registrieren&quot;</p></td>
</tr>
<tr class="odd">
<td><p>Erwartetes Ergebnis</p></td>
<td><p>- ein neuer Kunde wird angelegt mit intern zugewiesener eindeutigen ID</p>
<p>- Kunde wird zur Startseite weitergeleitet</p></td>
</tr>
</tbody>
</table>


<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td><p>ID</p></td>
<td><p>A2​</p></td>
</tr>
<tr class="even">
<td><p>Use Case</p></td>
<td><p>U17​</p></td>
</tr>
<tr class="odd">
<td><p>Voraussetzung</p></td>
<td><p>Admin will neuen Mitarbeiter anlegen</p></td>
</tr>
<tr class="even">
<td><p>Ereignis</p></td>
<td><p>Admin drückt &quot;Login&quot; und gibt seine persönlichen Zugangsdaten ein er wählt &quot;Mitarbeiter Registrieren&quot; und gibt die Mitarbeiterdaten ein:</p>
<p>- Name: Bob Schreiner</p>
<p>- Passwort: Arbeitender</p>
<p>- Adresse: Musterstraße 10 01217 Dresden</p>
<p>- E-mail: <a href="mailto:Bob_Schreiner@gmail.com">Bob_Schreiner@gmail.com</a></p>
<p>- Gehalt: 8,50</p>
<p>dann drückt er &quot;Registrieren&quot;</p></td>
</tr>
<tr class="odd">
<td><p>Erwartetes Ergebnis</p></td>
<td><p>- Admin wird angemeldet</p>
<p>- ein neuer Mitarbeiter wird angelegt mit eigener eindeutiger internen ID</p>
<p>- Admin wird auf Startseite weitergeleitet</p></td>
</tr>
</tbody>
</table>

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td><p>ID</p></td>
<td><p>A3​</p></td>
</tr>
<tr class="even">
<td><p>Use Case</p></td>
<td><p>U16​</p></td>
</tr>
<tr class="odd">
<td><p>Voraussetzung</p></td>
<td><p>registrierter Mitarbeiter will Möbelstück anlegen</p></td>
</tr>
<tr class="even">
<td><p>Ereignis</p></td>
<td><p>Mitarbeiter wählt &quot;Lager&quot;, drückt &quot;Möbelstück anlegen&quot; und gibt die Daten ein:</p>
<p>- Titel: Schrank</p>
<p>- Kategorie: Schrank</p>
<p>- Farbe: Kirschholz</p>
<p>- Bestandteile: Schranktür, Schrankwand, …​</p>
<p>- Erstbestand bestellen: 10</p>
<p>Dann drückt er &quot;Möbelstück erzeugen&quot;</p></td>
</tr>
<tr class="odd">
<td><p>Erwartetes Ergebnis</p></td>
<td><p>- ein neues Möbelstück wird dem Lager hinzugefügt</p>
<p>- Mitarbeiter wird weitergeleitet auf Lager</p>
<p>- innerhalb von 6 Tagen wird Stückzahl aufgefüllt</p></td>
</tr>
</tbody>
</table>

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td><p>ID</p></td>
<td><p>A4​</p></td>
</tr>
<tr class="even">
<td><p>Use Case</p></td>
<td><p>U16​</p></td>
</tr>
<tr class="odd">
<td><p>Voraussetzung</p></td>
<td><p>registrierter Mitarbeiter will Einzelteil anlegen</p></td>
</tr>
<tr class="even">
<td><p>Ereignis</p></td>
<td><p>Mitarbeiter wählt &quot;Lager&quot;, drückt &quot;Einzelteil anlegen&quot; und gibt die Daten ein:</p>
<p>- Titel: Stuhlbein</p>
<p>- Kategorie: Stuhl</p>
<p>- Farbe: Eiche</p>
<p>- Gewicht: 0,2</p>
<p>- Preis: 3,50</p>
<p>- Erstbestand bestellen: 10</p>
<p>Dann drückt er &quot;Einzelteil erzeugen&quot;</p></td>
</tr>
<tr class="odd">
<td><p>Erwartetes Ergebnis</p></td>
<td><p>- ein neues Einzelteil wird dem Lager hinzugefügt</p>
<p>- Mitarbeiter wird weitergeleitet auf Lager</p>
<p>- innerhalb von 6 Tagen wird Stückzahl aufgefüllt</p></td>
</tr>
</tbody>
</table>


<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td><p>ID</p></td>
<td><p>A5​</p></td>
</tr>
<tr class="even">
<td><p>Use Case</p></td>
<td><p>U7​</p></td>
</tr>
<tr class="odd">
<td><p>Voraussetzung</p></td>
<td><p>registrierter Kunde will bestehende Bestellung stornieren</p></td>
</tr>
<tr class="even">
<td><p>Ereignis</p></td>
<td><p>registrierter Kunde drückt &quot;Bestellübersicht&quot;, dann die zu stornierende Bestellung und wählt &quot;stornieren&quot;</p></td>
</tr>
<tr class="odd">
<td><p>Erwartetes Ergebnis</p></td>
<td><p>- Bestellung wird aus dem System genommen</p>
<p>- Kunde wird auf Bestellübersicht weitergeleitet</p></td>
</tr>
</tbody>
</table>


<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td><p>ID</p></td>
<td><p>A6​</p></td>
</tr>
<tr class="even">
<td><p>Use Case</p></td>
<td><p>U11​</p></td>
</tr>
<tr class="odd">
<td><p>Voraussetzung</p></td>
  <td><p>registrierter Mitarbeiter möchte für einen registrierten Kunden eine Bestellung aufgeben</p></td>
  </tr>
  <tr class="odd">
    <td><p>Ereignis</p></td>
    <td><p>Mitarbeiter wählt &quot;neue Bestellung&quot;, gibt Kundendaten ein und drückt &quot;bestätigen&quot;.
        Er wählt den gewünschten Artikel und klickt auf &quot;hinzufügen&quot;.
        Dann drückt er bestellen</p></td>
  </tr>
  <tr class="odd">
    <td><p>Erwartetes Ergebnis</p></td>
    <td><p>- ein Kundenbasierter Warenkorb wird erzeugt</p>
        <p>- gewünschter Artikel wird dem Warenkorb hinzugefügt</p>
        <p>- eine neue Bestellung wird erzeugt</p>
        <p>- Mitarbeiter wird auf Startseite weitergeleitet</p></td>
    </tr>
</tbody>
</table>
    

|                     |                                                                                                                           |
| ------------------- | ------------------------------------------------------------------------------------------------------------------------- |
| ID                  | A7​                                                                                                                        |
| Use Case            | U7​                                                                                                                        |
| Voraussetzung       | registrierter Kunde will bestehende Bestellung stornieren die bereits versendet ist                         |
| Ereignis            | registrierter Kunde drückt "Bestellübersicht", dann die zu stornierende Bestellung und wählt "stornieren"                 |
| Erwartetes Ereignis | \- es wird eine Fehlernachricht angezeigt die den Kunden unterrichtet das dies zu diesem Zeitpunkt nicht mehr möglich ist |

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td><p>ID</p></td>
<td><p>A8​</p></td>
</tr>
<tr class="even">
<td><p>Use Case</p></td>
<td><p>U2​</p></td>
</tr>
<tr class="odd">
<td><p>Voraussetzung</p></td>
<td><p>es existieren Accounts</p></td>
</tr>
<tr class="even">
<td><p>Ereignis</p></td>
<td><p>Nutzer drückt auf &quot;Login&quot; und gibt persönliche Zugangsdaten ein (Bob Schreiner, Arbeitender)</p></td>
</tr>
<tr class="odd">
<td><p>Erwartetes Ereignis</p></td>
<td><p>- der Nutzer wird als Bob Schreiner angemeldet</p>
<p>- er wird weitergeleitet auf die Startseite</p>
<p>- er hat alle Rechte des Mitarbeiters</p></td>
</tr>
</tbody>
</table>

# 12. Glossar

Sämtliche Begriffe, die innerhalb des Projektes verwendet werden und deren gemeinsames Verständnis aller beteiligten Stakeholder essentiell ist, sollten hier aufgeführt werden. Insbesondere Begriffe der zu implementierenden Domäne wurden bereits beschrieben, jedoch gibt es meist mehr Begriffe, die einer Beschreibung bedürfen.  
Beispiel: Was bedeutet "Kunde"? Ein Nutzer des Systems? Der Kunde des Projektes (Auftraggeber)?

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
        <td><p>Registrieren</p></td>
        <td><p>einen neuen Account erstellen</p></td>
    </tr>
    <tr class="odd">
        <td><p>Login</p></td>
        <td><p>Nutzer wird authentifiziert</p></td>
    </tr>
    <tr class="even">
        <td><p>Webshop</p></td>
        <td><p>zentrale Klasse des Systems</p></td>
    </tr>
    <tr class="odd">
        <td><p>System</p></td>
        <td><p>die hier entwickelte Software</p></td>
    </tr>
    <tr class="even">
        <td><p>Warenkorb</p></td>
        <td><p>ermöglicht Mitarbeiter die Erstellung einer Bestellung für einen Kunden</p></td>
    </tr>
    </tbody>
</table>
    

# 13. Offene Punkte

Offene Punkte werden entweder direkt in der Spezifikation notiert. Wenn das Pflichtenheft zum finalen Review vorgelegt wird, sollte es keine offenen Punkte mehr geben.
