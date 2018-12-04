# Testplan Order

### Warenkorb und Bestellung

|ID|Anwendungsfall|Vorbedingung|Eingabe|Ausgabe|
|:----:|:--------------------------|:-----------------------------|:------------------------------------:|:---------:|
|1|Bestellung abschließen|Warenkorb leer |Bestellen|Warenkorb |
|2|Bestellung abschließen|Warenkorb ist nicht leer|Bestellen|Order|
|3|Bestellung abschließen|User existiert nicht|Bestellen|Fehler|
|4|Bestellung abschließen|User existiert|Bestellen|Order|
|5|Ware in Warenkorb hinzufügen|Artikel Anzahl ist Null oder kleiner der bestellten Anzahl|Warenkorb hinzufügen|Fehler|
|6|Ware in Warenkorb hinzufügen|Artikel Anzahl größer gleich der bestellten Anzahl|Warenkorb hinzufügen|Ware wird Warenkorb hinzugefügt|
|7|Bestellung stornieren|Bestellung ist bezahlt|Stornieren|Fehler|
|8|Bestellung stornieren|Bestellung ist offen|Stornieren|Bestellung ist storniert|
|9|Bestellung bezahlen|Bestellung ist bezahlt|Bezahlen|Fehler|
|10|Bestellung bezahlen|Bestellung ist offen|Bezahlen|Bestellung wird auf Status bezahlt gesetzt|
|11|Bestellung abschließen|Bestellung ist offen|Abschließen|Fehler|
|12|Bestellung abschließen|Bestellung ist bezahlt|Abschließen|Bestellung wird auf Status abschlossen gesetzt|
