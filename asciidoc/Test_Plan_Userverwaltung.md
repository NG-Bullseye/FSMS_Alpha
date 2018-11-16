# Testplan Userverwaltung

## Registrierung

### Username

| Wert                 | Erwartetes Ergebnis |
|:----------------------|:----------:|
| keine Eingabe        |  Fehler  |
| Zahlen               |   okay   |
| Buchstaben           |   okay   |
| Sonderzeichen |  Fehler        |
| Existiert bereits    |  Fehler  |

### E-Mail

| Wert                            | Erwartetes Ergebnis |
|:---------------------------------|:---------------------:|
| keine Eingabe                   | Fehler              |
| Zahlen                          | okay                |
| Buchstaben                      | okay                |
| enthählt keinen Punkt           | Fehler              |
| enthält kein @                  | Fehler              |
| Sonderzeichen außer Bindestrich | Fehler              |

### Vorname

| Wert                     | Erwartetes Ergebnis |
|:--------------------------|:---------------------:|
| Keine Eingabe            | Fehler              |
| Buchstaben               | okay                |
| Zahlen                   | Fehler              |
| Sonderzeichen            | okay                |
| Enthält keine Buchstaben | Fehler              |

### Nachname

| Wert                     | Erwartetes Ergebnis |
|:--------------------------|:---------------------:|
| Keine Eingabe            | Fehler              |
| Buchstaben               | okay                |
| Zahlen                   | Fehler              |
| Sonderzeichen            | okay                |
| Enthält keine Buchstaben | Fehler              |

### Adresse

| Wert                               | Erwartetes Ergebnis |
|:------------------------------------|:---------------------:|
| keine Eingabe                      | Fehler              |
| Buchstaben                         | okay                |
| Zahlen                             | okay                |
| Sonderzeichen, außer . Und , und - | Fehler              |
| enthält keinen Buchstaben          | Fehler              |
| enthält keine Zahle                | Fehler              |

### Passwort

| Wert          | Erwartetes Ergebnis |
|:---------------|:---------------------:|
| keine Eingabe | Fehler              |
| buchstaben    | okay                |
| zahlen        | okay                |
| sonderzeichen | okay                |