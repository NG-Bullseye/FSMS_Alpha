| Version | Status    | Bearbeitungsdatum | Autoren(en) | Vermerk          |
| ------- | --------- | ----------------- | ----------- | ---------------- |
| 0.1     | In Arbeit | 10.10.2017        | Autor       | Initiale Version |

# Inhaltsverzeichnis

Dieses Dokument benötigt ein Inhaltsverzeichnis. Es existieren mehrere Einbindungsmöglichkeiten.

# Zusammenfassung

Eine kurze Beschreibung des Dokuments. Wenige Absätze.

# Aufgabenstellung und Zielsetzung

Text aus Aufgabenstellung kopieren und ggfs. präzisieren. Insbesondere ergänzen, welche Ziele mit dem Abschluss des Projektes erreicht werden sollen.

# Produktnutzung

In welchem Kontext soll das System später genutzt werden? Welche Rahmenbedingungen gelten? Zusätzlich kurze Einleitung für fachfremde Personen

# Interessensgruppen (Stakeholders)

Welche realen und juristischen Personen(-gruppen) haben Einfluss auf die Anforderungen im Projekt?

# Systemgrenze und Top-Level-Architektur

## Kontextdiagramm

Das Kontextdiagramm zeigt das geplante Software-System in seiner Umgebung. Zur Umgebung gehören alle Nutzergruppen des Systems und Nachbarsysteme. Die Grafik kann auch informell gehalten sein. Überlegen Sie sich dann geeignete Symbole. Die Grafik kann beispielsweise mit Visio erstellt werden. Wenn nötig, erläutern Sie diese Grafik.

## Top-Level-Architektur

Dokumentieren Sie ihre Top-Level-Architektur mit Hilfe eines Komponentendiagramm.

# Anwendungsfälle

## Akteure

Akteure sind die Benutzer des Software-Systems oder Nachbarsysteme, welche darauf zugreifen. Dokumentieren Sie die Akteure in einer Tabelle. Diese Tabelle gibt einen Überblick über die Akteure und beschreibt sie kurz. Die Tabelle hat also mindestens zwei Spalten (Akteur Name und Kommentar). Weitere relevante Spalten können bei Bedarf ergänzt werden.

| Name                        | Beschreibung                                                                                                                                                        |
| --------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| User                        | Dies ist jede Person, die mit der Software interagieret                                                                                                             |
| Authentifizierter Kunde     | Diese Personen sind im System als Kunden registriert und eingeloggt, sodass sie sich Artikel kaufen können und weitere Funktionen nutzen.                           |
| Nichtauthentifizierter User | Diese Person nutzt das System nur eingeschränkt, weil sie entweder nicht eingeloggt oder nicht im System registriert ist.                                           |
| Mitarbeiter                 | Diese Personen sind Mitarbeiter des Möbelgeschäfts und können die Mitarbeiterfunktionen nutzen, wie z.B. auf das Lager zugreifen oder Bestellungen entgegen nehmen. |
| Admin                       | Dies ist ein spezieller Mitarbeiter, der erweiterter Berechtigungen im System besitz und verschiedene Informationen bearbeiten kann.                                |

## Überblick Anwendungsfalldiagramm

Anwendungsfall-Diagramm, das alle Anwendungsfälle und alle Akteure darstellt === Anwendungsfallbeschreibungen

Dieser Unterabschnitt beschreibt die Anwendungsfälle. In dieser Beschreibung müssen noch nicht alle Sonderfälle und Varianten berücksichtigt werden. Schwerpunkt ist es, die wichtigsten Anwendungsfälle des Systems zu finden. Wichtig sind solche Anwendungsfälle, die für den Auftraggeber, den Nutzer den größten Nutzen bringen.

Für komplexere Anwendungsfälle ein UML-Sequenzdiagramm ergänzen. Einfache Anwendungsfälle mit einem Absatz beschreiben.

Die typischen Anwendungsfälle (Anlegen, Ändern, Löschen) können zu einem einzigen zusammengefasst werden.

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td><p>ID</p></td>
<td></td>
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
<td></td>
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
<td></td>
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
<td></td>
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
<td></td>
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
<td></td>
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
<td></td>
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
<td></td>
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
<td></td>
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
<td></td>
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
<td></td>
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
<td></td>
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
<td></td>
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
<td></td>
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
<td></td>
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
<td></td>
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
<td></td>
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
<td></td>
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
<td></td>
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
<td></td>
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
<td></td>
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
<td><p><em>Hinzufügen</em>: -</p>
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
<td></td>
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
<td></td>
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
<td></td>
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
<td></td>
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
<td></td>
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
<td></td>
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
<td></td>
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
<td></td>
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
<td></td>
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
<td></td>
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
<td></td>
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
<td></td>
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
<td></td>
</tr>
</tbody>
</table>

# Funktionale Anforderungen

## Muss-Kriterien

Was das zu erstellende Programm auf alle Fälle leisten muss.

## Kann-Kriterien

Anforderungen die das Programm leisten können soll, aber für den korrekten Betrieb entbehrlich sind.

# Nicht-Funktionale Anforderungen

## Qualitätsziele

Dokumentieren Sie in einer Tabelle die Qualitätsziele, welche das System erreichen soll, sowie deren Priorität.

## Konkrete Nicht-Funktionale Anforderungen

Beschreiben Sie Nicht-Funktionale Anforderungen, welche dazu dienen, die zuvor definierten Qualitätsziele zu erreichen. Achten Sie darauf, dass deren Erfüllung (mindestens theoretisch) messbar sein muss.

# GUI Prototyp

In diesem Kapitel soll ein Entwurf der Navigationsmöglichkeiten und Dialoge des Systems erstellt werden. Idealerweise entsteht auch ein grafischer Prototyp, welcher dem Kunden zeigt, wie sein System visuell umgesetzt werden soll. Konkrete Absprachen - beispielsweise ob der grafische Prototyp oder die Dialoglandkarte höhere Priorität hat - sind mit dem Kunden zu treffen.

## Überblick: Dialoglandkarte

Erstellen Sie ein Übersichtsdiagramm, das das Zusammenspiel Ihrer Masken zur Laufzeit darstellt. Also mit welchen Aktionen zwischen den Masken navigiert wird.

## Dialogbeschreibung

Für jeden Dialog:

1.  Kurze textuelle Dialogbeschreibung eingefügt: Was soll der jeweilige Dialog? Was kann man damit tun? Überblick?

2.  Maskenentwürfe (Screenshot, Mockup)

3.  Maskenelemente (Ein/Ausgabefelder, Aktionen wie Buttons, Listen, …)

4.  Evtl. Maskendetails, spezielle Widgets

# Datenmodell

## Überblick: Klassendiagramm

UML-Analyseklassendiagramm

## Klassen und Enumerationen

Dieser Abschnitt stellt eine Vereinigung von Glossar und der Beschreibung von Klassen/Enumerationen dar. Jede Klasse und Enumeration wird in Form eines Glossars textuell beschrieben. Zusätzlich werden eventuellen Konsistenz- und Formatierungsregeln aufgeführt.

| Klasse/Enumeration | Beschreibung |  |
| ------------------ | ------------ |  |
| …                  | …            |  |

# Akzeptanztestfälle

Mithilfe von Akzeptanztests wird geprüft, ob die Software die funktionalen Erwartungen und Anforderungen im Gebrauch erfüllt. Diese sollen und können aus den Anwendungsfallbeschreibungen und den UML-Sequenzdiagrammen abgeleitet werden. D.h., pro (komplexen) Anwendungsfall gibt es typischerweise mindestens ein Sequenzdiagramm (welches ein Szenarium beschreibt). Für jedes Szenarium sollte es einen Akzeptanztestfall geben. Listen Sie alle Akzeptanztestfälle in tabellarischer Form auf. Jeder Testfall soll mit einer ID versehen werde, um später zwischen den Dokumenten (z.B. im Test-Plan) referenzieren zu können.

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td><p>ID</p></td>
<td><p>…​</p></td>
</tr>
<tr class="even">
<td><p>Use Case</p></td>
<td><p>…​</p></td>
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
<td><p>…​</p></td>
</tr>
<tr class="even">
<td><p>Use Case</p></td>
<td><p>…​</p></td>
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
<td><p>…​</p></td>
</tr>
<tr class="even">
<td><p>Use Case</p></td>
<td><p>…​</p></td>
</tr>
<tr class="odd">
<td><p>Voraussetzung</p></td>
<td><p>registrierter Mitarbeiter will Möbelstück anlegen</p></td>
</tr>
<tr class="even">
<td><p>Ereignis</p></td>
<td><p>Mitarbeiter wählt &quot;Lager&quot;, drückt &quot;Möbelstück anlegen&quot; und gibt die Daten ein:</p>
<p>- Titel: Schrank</p>
<p>- Kategorie: Schlafzimmer</p>
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
<td><p>…​</p></td>
</tr>
<tr class="even">
<td><p>Use Case</p></td>
<td><p>…​</p></td>
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
<td><p>…​</p></td>
</tr>
<tr class="even">
<td><p>Use Case</p></td>
<td><p>…​</p></td>
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
<td><p>…​</p></td>
</tr>
<tr class="even">
<td><p>Use Case</p></td>
<td><p>…​</p></td>
</tr>
<tr class="odd">
<td><p>Voraussetzung</p></td>
  <td><p>registrierten Mitarbeiter möchte für einen registrierten Kunden eine Bestellung aufgeben</p></td>
  </tr>
  <tr class="odd">
    <td><p>Ereignis</p></td>
    <td><p>...</p></td>
  </tr>
  <tr class="odd">
    <td><p>Erwartetes Ergebnis</p></td>
    <td><p>...</p></td>
    </tr>
</tbody>
</table>
    

|                     |                                                                                                                           |
| ------------------- | ------------------------------------------------------------------------------------------------------------------------- |
| ID                  | …​                                                                                                                        |
| Use Case            | …​                                                                                                                        |
| Voraussetzung       | registrierter Kunde will bestehende Bestellung stornieren die innerhalb eines Tages ankommen soll                         |
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
<td><p>…​</p></td>
</tr>
<tr class="even">
<td><p>Use Case</p></td>
<td><p>…​</p></td>
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

# Glossar

Sämtliche Begriffe, die innerhalb des Projektes verwendet werden und deren gemeinsames Verständnis aller beteiligten Stakeholder essentiell ist, sollten hier aufgeführt werden. Insbesondere Begriffe der zu implementierenden Domäne wurden bereits beschrieben, jedoch gibt es meist mehr Begriffe, die einer Beschreibung bedürfen.  
Beispiel: Was bedeutet "Kunde"? Ein Nutzer des Systems? Der Kunde des Projektes (Auftraggeber)?

# Offene Punkte

Offene Punkte werden entweder direkt in der Spezifikation notiert. Wenn das Pflichtenheft zum finalen Review vorgelegt wird, sollte es keine offenen Punkte mehr geben.
