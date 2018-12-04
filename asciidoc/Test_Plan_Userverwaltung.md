# Testplan Userverwaltung

### Einführung

Dieses Dokument enthält einen Testplan rund um die Userverwaltung. Dazu zählen Kunden, Mitarbeiter und Admins. Es werden hierbei Eingaben im Webinterface validiert und die Klassen getestet. Es wird auf die entsprechenden Abschnitte verwiesen, was bei welchem Gruppe unter welchen Umständen getestet wird.

### Herangehensweise

- Klassen werden mit JUnit getestet
- Validierung im Webinterface werden über Thymeleaf realisiert
- Integrationstest nach Bottom-Up Prinzip

### Nicht zu testende Funktionalitäten

Bibliotheken von Drittanbietern (eigene Tests beschränken sich auf Schnittstelle, bzw. Zusammenspiel).

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

