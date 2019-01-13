# Testplan CatalogManagement

## Umgebungsvoraussetzungen

* Wird spezielle Hardware benötigt?
	* Nein
* Welche Daten müssen bereitgestellt werden? Wie werden die Daten bereitgestellt?
	* Die Tests, die sich auf das betrachten des Katalogs beziehen, brauchen eine vorher definierte Datenbank, um das Ergebnis kontrollieren zu können.
* Wird zusätzliche Software für das Testen benötigt?
	* Nein
* Wie kommuniziert die Software während des Testens? Internet? Netzwerk?
	* Die Software kommuniziert nur mit internen Funktionen und Datenbanken.

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
