# Akzeptanztestfälle Lager
## Artikel nachbestellen

|Auslöser|Eigenschaften|Ergebnis|
|--------|----------------|--------|
|Auswahl von Nachbestellen|User ist eingeloggt als Mitarbeiter bzw. Admin|Anzeige einer Website mit Auswahlmöglichkeiten der Artikel und der Eingabe der Anzahl|
|Auswahl von Nachbestellen|User ist nicht Mitarbeiter oder Admin|Weiterleitung auf Fehlerseite
|Bestätigung des Nachbestellens|Daten sind unvollständig eingegeben|Rückkehr auf Nachbestellbestellseite mit Fehlermeldung|
|Bestätigung des Nachbestellens|Negative Anzahl eingegeben|Rückkehr auf Nachbestellbestellseite mit Fehlermeldung|
|Bestätigung des Nachbestellens|1. korrekte Eingabe von Daten 2.Abwarten der Nachbestellzeit| Erhöhung der Artikelanzahl um eingegebene Menge|
|Entfernung eines Artikels aus dem Sortiment|Artikel wurde nachbestellt|Entfernung der Nachbestellung und Entfernung der Ausgaben



## Artikel bearbeiten
|Auslöser|Eigenschaft|Ergebnis|
|--------|-----------|--------|
|Auswahl von Nachbestellen|User ist eingeloggt als Mitarbeiter bzw. Admin|Anzeige einer Website mit Bearbeitungsmöglichkeiten|
|Auswahl von Nachbestellen|User ist nicht Mitarbeiter oder Admin|Weiterleitung auf Fehlerseite
|Auswahl des zubearbeitenden Artikels|-|Füllung des Bearbeitungsformular mit den aktuellen Daten
|Bestätigung des Bearbeitens|Daten sind unvollständig eingegeben|Rückkehr auf Bearbeitungsseite mit Fehlermeldungen|
|Bestätigung des Bearbeitens|Daten entsprechen nicht dem gewünschten Format(z.B. Buchstaben im Preisfeld|Rückkehr auf Bearbeitungsseite mit Fehlermeldung|
Bestätigung des Bearbeitens|Daten sind vollständig eingegeben und entsprechen dem gewünschten Format|Änderung der Daten und Rückkehr auf Hauptseite|

## Artikelanzahl verändert
Dies wird nicht durch einen User sondern durch das System ausgeführt.

|Auslöser|Eigenschaft|Ergebnis|
|--------|-----------|--------|
|Anfrage ob Artikel in bestimmter Anzahl vorhanden ist|Artikel ist im Lagersystem unbekannt|Ausgabe einer Fehlermeldung|
|Anfrage ob Artikel in bestimmter Anzahl vorhanden ist|Artikel vorhanden| Ausgabe ob Anzahl größer bzw. gleich oft vorhanden ist|
|Bestellung eines Artikels|Artikel im Lagersystem unbekannt|Ausgabe einer Fehlermeldung|
|Bestellung eines Artikels|Gewünschte Anzahl kleiner als verfügbare Anzahl|Ausgabe der Fehlermeldung und keine Änderung der Anzahl|
|Bestellung eines Artikels| Gewünschte Anzahl größer oder gleich der verfügbaren Anzahl|Änderung der Artikelanzahl im Lager|