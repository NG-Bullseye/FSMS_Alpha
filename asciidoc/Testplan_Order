# Testplan Order


### Order erstellen

| Wert                 | Erwartetes Ergebnis |
|:----------------------|:----------:|
| Warenkorb ist leer        |  Fehler  |
| Warenkorb ist nicht leer               |   okay   |
| eingeloggter User ist Mitarbeiter oder Admin| okay|
| User ist nicht eingeloggt| Fehler|
| eingeloggter User ist Kunde| Fehler|
| übergebener User existiert | okay|
| übergebener User existiert nicht | Fehler|

### Warenkorb Produkt hinzufügen

| Wert                 | Erwartetes Ergebnis |
|:----------------------|:----------:|
| Produkt ID ist Null        |  Fehler  |
| Produkt ID ist vorhanden               |   okay   |
| Preis existiert           |   okay   |
| Preis existiert nicht |  Fehler        |
| Artikel Anzahl ist Null oder kleiner der bestellten Anzahl   |  Fehler  |
| Artikel Anzahl größer gleich der bestellten Anzahl| okay|

### Order abschließen

| Wert                 | Erwartetes Ergebnis |
|:----------------------|:----------:|
|Order existiert nicht| Fehler|
|Order existiert und ...|*Erwartetes Ergebnis* |
| Order Status ist PAID        |  okay  |
| Order Status ist OPEN               |   Fehler   |
| Order Status ist COMPLETE           |   Fehler   |


### Order bezahlen

| Wert                 | Erwartetes Ergebnis |
|:----------------------|:----------:|
|Order existiert nicht| Fehler|
|Order existiert und ...|*Erwartetes Ergebnis* |
| Order Status ist PAID        |  Fehler  |
| Order Status ist OPEN               |   okay   |
| Order Status ist COMPLETE           |   Fehler   |

### Order stornieren

| Wert                 | Erwartetes Ergebnis |
|:----------------------|:----------:|
|Order existiert nicht| Fehler|
|Order existiert und ...|*Erwartetes Ergebnis* |
| Order Status ist PAID        |  Fehler  |
| Order Status ist OPEN               |   okay   |
| Order Status ist COMPLETE           |   Fehler   |
