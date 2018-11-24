# Test-Plan

Dieser Testplan stellt eine vereinfachte Version der IEEE 829-1998 Vorlage dar.

## Einführung
Wozu dient das Dokument? Zielgruppe?

## Aufbau der Testbezeichner
Nach welchem Schema sind die Tests benannt?

-Bsp.-

-U - Unit Test, I - Integrationstest-

-U-1, U-2, I-1-

## Test Gegenstände

## Zu testende Funktionalitäten

## Nicht zu testende Funktionalitäten
z.B. Bibliotheken von Drittanbietern (eigene Tests beschränken sich auf Schnittstelle, bzw. Zusammenspiel).

## Herangehensweise
-Bsp.:-

-Klassen werden mit JUnit getestet.

-Integrationstest nach Bottom-Up Prinzip-

## Umgebungsvoraussetzungen
- Wird spezielle Hardware benötigt?
- Welche Daten müssen bereitgestellt werden? Wie werden die Daten bereitgestellt?
- Wird zusätzliche Software für das Testen benötigt?
- Wie kommuniziert die Software während des Testens? Internet? Netzwerk?

## Testfälle und Verantwortlichkeit
Jede testbezogene Aufgabe muss einem Ansprechpartner zugeordnet werden.

// See http://asciidoctor.org/docs/user-manual/#tables
[options#"headers"]
|###
|ID |Anwendungsfall |Vorbedingungen |Eingabe |Ausgabe
|…  |…              |…              |…       |…
|###


## Katalog betrachten

<table>
<tr>
<th> ID </th>
<th> Anwendungsfall </th>
<th>Vorbedingung </th>
<th> Eingabe </th>
<th> Ausgabe </th>
</tr>
<tr>
<td>01</td>
<td>Alle Artikel anzeigen</td>
<td>Katalog initialisiert</td>
<td>Zeige alle Artikel</td>
<td>alle vorhandenen Möbel und Möbelteile</td>
</tr>
<tr>
<td>01</td>
<td>Alle Artikel anzeigen</td>
<td>Katalog initialisiert</td>
<td>Zeige alle Artikel</td>
<td>alle vorhandenen Möbel und Möbelteile</td>
</tr>
<tr>
<td>02</td>
<td>Alle Möbel anzeigen</td>
<td>Katalog initialisiert</td>
<td>Zeige alle Möbel</td>
<td>alle vorhandenen Möbel</td>
</tr>
<tr>
<td>03</td>
<td>Alle Möbelteile anzeigen</td>
<td>Katalog initialisiert</td>
<td>Zeige alle Möbelteile</td>
<td>alle vorhandenen Möbelteile</td>
</tr>
<tr>
<td>04</td>
<td>Alle Artikel einer bestimmten Farbe anzeigen</td>
<td>Katalog initialisiert</td>
<td>Zeige alle weißen Artikel</td>
<td>alle vorhandenen weißen Möbel und Möbelteile</td>
</tr>
<tr>
<td>05</td>
<td>Alle Möbel einer bestimmten Farbe anzeigen</td>
<td>Katalog initialisiert</td>
<td>Zeige alle weißen Möbel</td>
<td>alle vorhandenen weißen Möbel</td>
</tr>
<tr>
<td>06</td>
<td>Alle Möbelteile einer bestimmten Farbe anzeigen</td>
<td>Katalog initialisiert</td>
<td>Zeige alle weißen Möbelteile</td>
<td>alle vorhandenen weißen Möbelteile</td>
</tr>



## Einen neuen Artikel hinzufügen

<table>
<tr>
<th> ID </th>
<th> Anwendungsfall </th>
<th>Vorbedingung </th>
<th> Eingabe </th>
<th> Ausgabe </th>
</tr>
<tr>
<td>07</td>
<td>Produktnamen eingeben</td>
<td>alle anderen Eingaben sind richtig</td>
<td>keine Eingabe</td>
<td>Fehler</td>
</tr>
<tr>
<td>08</td>
<td>Produktnamen eingeben</td>
<td>alle anderen Eingaben sind richtig</td>
<td>Buchstaben</td>
<td>Artikel hinzugefügt</td>
</tr>
<tr>
<td>09</td>
<td>Produktkategorien eingeben</td>
<td>alle anderen Eingaben sind richtig</td>
<td>alle eingegebenen Kategorien sind bereits vorhanden </td>
<td>Artikel hinzugefügt</td>
</tr>
<tr>
<td>10</td>
<td>Produktkategorien eingeben</td>
<td>alle anderen Eingaben sind richtig</td>
<td>eingegebene Kategorie nicht vorhanden </td>
<td>Fehler</td>
<tr>
<td>11</td>
<td>Produktkategorien eingeben</td>
<td>alle anderen Eingaben sind richtig</td>
<td>keine Eingabe</td>
<td>Fehler</td>
</tr>
<tr>
<td>12</td>
<td>Produktfarbe eingegeben</td>
<td>alle anderen Eingaben sind richtig</td>
<td>die eingegebenen Farben sind alle als Produktfarben möglich </td>
<td>Artikel hinzugefügt</td>
</tr>
<tr>
<td>13</td>
<td>Produktfarbe eingegeben</td>
<td>alle anderen Eingaben sind richtig</td>
<td>eine oder mehrere Farben sind als Produktfarben nicht zulässig </td>
<td>Fehler</td>
</tr>
<tr>
<td>14</td>
<td>Produktfarbe eingegeben</td>
<td>alle anderen Eingaben sind richtig</td>
<td>die eingegebenen Farben sind alle als Produktfarben möglich </td>
<td>Artikel hinzugefügt</td>
</tr>
<tr>
<td>15</td>
<td>Produktgewicht eingeben</td>
<td>alle anderen Eingaben sind richtig</td>
<td>Zahl </td>
<td>Artikel hinzugefügt</td>
</tr>
<tr>
<td>16</td>
<td>Produktgewicht eingeben</td>
<td>alle anderen Eingaben sind richtig</td>
<td>Zahl </td>
<td>Artikel hinzugefügt</td>
</tr>
<tr>
<td>17</td>
<td>Produktgewicht eingeben</td>
<td>alle anderen Eingaben sind richtig</td>
<td>Buchstaben </td>
<td>Fehler</td>
</tr>
<tr>
<td>18</td>
<td>Produkttyp eingeben</td>
<td>alle anderen Eingaben sind richtig</td>
<td>Part oder Set </td>
<td>Artikel hinzugefügt</td>
</tr>
<tr>
<td>19</td>
<td>Produkttyp eingeben</td>
<td>alle anderen Eingaben sind richtig</td>
<td>beliebige Eingabe, die nicht Part oder Set ist </td>
<td>Fehler</td>
</tr>

</table>

## Einen neue Kategorie hinzufügen

<table>
<tr>
<th> ID </th>
<th> Anwendungsfall </th>
<th>Vorbedingung </th>
<th> Eingabe </th>
<th> Ausgabe </th>
</tr>
<tr>
<td> 20 </td>
<td>eine neue Kategorie hinzufügen</td>
<td> die Kategorie existiert noch nicht</td>
<td>Buchstaben </td>
<td>Kategorie hinzugefügt</td>
</tr>
<tr>
<td> 21 </td>
<td>eine neue Kategorie hinzufügen</td>
<td> die Kategorie existiert noch nicht</td>
<td>Zahlen oder Sonderzeichen </td>
<td>Fehler</td>
</tr>
<tr>
<td> 22 </td>
<td>eine neue Kategorie hinzufügen</td>
<td> Eingabe korrekt</td>
<td>bereits bestehende Kategorie </td>
<td>Fehler</td>
</tr>
  
</table>

## Registrierung

Nicht Registrierter Kunde möchte sich registrieren und befindet sich auf der Seite */register*. Es erfolgt eine Eingabenvalidierung nach folgendem Schema:



| ID | Anwendungsfall           | Vorbedingung                | Eingabe                            | Ausgabe |
|:----:|:--------------------------|:-----------------------------|:------------------------------------:|:---------:|
| 1  | Registrierung - Username | User noch nicht registriert | keine Eingabe                      | Fehler  |
| 2  | Registrierung - Username | User noch nicht registriert | Zahlen                             | okay    |
| 3  | Registrierung - Username | User noch nicht registriert | Buchstaben                         | okay    |
| 4  | Registrierung - Username | User noch nicht registriert | Sonderzeichen                      | Fehler  |
| 5  | Registrierung - Username | User noch nicht registriert | Existiert bereits                  | Fehler  |
| 6  | Registrierung - E-Mail   | User noch nicht registriert | keine Eingabe                      | Fehler  |
| 7  | Registrierung - E-Mail   | User noch nicht registriert | Zahlen                             | okay    |
| 8  | Registrierung - E-Mail   | User noch nicht registriert | Buchstaben                         | okay    |
| 9  | Registrierung - E-Mail   | User noch nicht registriert | enthählt keinen Punkt              | Fehler  |
| 10 | Registrierung - E-Mail   | User noch nicht registriert | enthält kein @                     | Fehler  |
| 11 | Registrierung - E-Mail   | User noch nicht registriert | Sonderzeichen außer Bindestrich    | Fehler  |
| 12 | Registrierung  - Vorname | User noch nicht registriert | Keine Eingabe                      | Fehler  |
| 13 | Registrierung  - Vorname | User noch nicht registriert | Buchstaben                         | okay    |
| 14 | Registrierung  - Vorname | User noch nicht registriert | Zahlen                             | Fehler  |
| 15 | Registrierung  - Vorname | User noch nicht registriert | Sonderzeichen                      | okay    |
| 16 | Registrierung  - Vorname | User noch nicht registriert | Enthält keine Buchstaben           | Fehler  |
| 17 | Registrierung - Nachname | User noch nicht registriert | Keine Eingabe                      | Fehler  |
| 18 | Registrierung - Nachname | User noch nicht registriert | Buchstaben                         | okay    |
| 19 | Registrierung - Nachname | User noch nicht registriert | Zahlen                             | Fehler  |
| 20 | Registrierung - Nachname | User noch nicht registriert | Sonderzeichen                      | okay    |
| 21 | Registrierung - Nachname | User noch nicht registriert | Enthält keine Buchstaben           | Fehler  |
| 22 | Registrierung - Adresse  | User noch nicht registriert | keine Eingabe                      | Fehler  |
| 23 | Registrierung - Adresse  | User noch nicht registriert | Buchstaben                         | okay    |
| 24 | Registrierung - Adresse  | User noch nicht registriert | Zahlen                             | okay    |
| 25 | Registrierung - Adresse  | User noch nicht registriert | Sonderzeichen, außer . Und , und - | Fehler  |
| 26 | Registrierung - Adresse  | User noch nicht registriert | enthält keinen Buchstaben          | Fehler  |
| 27 | Registrierung - Adresse  | User noch nicht registriert | enthält keine Zahle                | Fehler  |
| 28 | Registrierung - Passwort | User noch nicht registriert | keine Eingabe                      | Fehler  |
| 29 | Registrierung - Passwort | User noch nicht registriert | buchstaben                         | okay    |
| 30 | Registrierung - Passwort | User noch nicht registriert | zahlen                             | okay    |
| 31 | Registrierung - Passwort | User noch nicht registriert | sonderzeichen                      | okay    |

## Artikel nachbestellen

| ID | Anwendungsfall           | Vorbedingung                | Eingabe                            | Ausgabe |
|:----:|:--------------------------|:-----------------------------|:------------------------------------:|:---------:|
| 1 | Artikel nachbestellen |User ist eingeloggt als Mitarbeiter bzw. Admin|Auswahl von Nachbestellen|Anzeige einer Website mit Auswahlmöglichkeiten der Artikel und der Eingabe der Anzahl|
| 1 | Artikel nachbestellen |User ist nicht Mitarbeiter oder Admin|Auswahl von Nachbestellen|Weiterleitung auf Fehlerseite
| 1 | Artikel nachbestellen |Daten sind unvollständig eingegeben|Bestätigung des Nachbestellens|Rückkehr auf Nachbestellbestellseite mit Fehlermeldung|
| 1 | Artikel nachbestellen |Negative Anzahl eingegeben|Bestätigung des Nachbestellens|Rückkehr auf Nachbestellbestellseite mit Fehlermeldung|
| 1 | Artikel nachbestellen | korrekte Eingabe von Daten |Bestätigung des Nachbestellens| Erhöhung der Artikelanzahl um eingegebene Menge nach Nachbestellzeit|
| 1 | Artikel nachbestellen |Artikel wurde nachbestellt|Entfernung eines Artikels aus dem Sortiment|Entfernung der Nachbestellung und Entfernung der Ausgaben

## Artikel bearbeiten

| ID | Anwendungsfall           | Vorbedingung                | Eingabe                            | Ausgabe |
|:----:|:--------------------------|:-----------------------------|:------------------------------------:|:---------:|
| 1 | Artikel bearbeiten|User ist eingeloggt als Mitarbeiter bzw. Admin|Auswahl von Bearbeiten|Anzeige einer Website mit Bearbeitungsmöglichkeiten|
| 1 | Artikel bearbeiten|User ist nicht Mitarbeiter oder Admin|Auswahl von Bearbeiten|Weiterleitung auf Fehlerseite
| 1 | Artikel bearbeiten|-|Auswahl des zubearbeitenden Artikels|Füllung des Bearbeitungsformular mit den aktuellen Daten
| 1 | Artikel bearbeiten|Daten sind unvollständig eingegeben|Bestätigung des Bearbeitens|Rückkehr auf Bearbeitungsseite mit Fehlermeldungen|
| 1 | Artikel bearbeiten|Daten entsprechen nicht dem gewünschten Format(z.B. Buchstaben im Preisfeld)|Bestätigung des Bearbeitens| Rückkehr auf Bearbeitungsseite mit Fehlermeldung|
| 1 | Artikel bearbeiten|Daten sind vollständig eingegeben und entsprechen dem gewünschten Format|Bestätigung des Bearbeitens| Änderung der Daten und Rückkehr auf Hauptseite|
