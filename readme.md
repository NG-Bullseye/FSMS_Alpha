# Fishstone Management Service

## Hilfreiche Links

[![Folien zum Projekt]()](https://tu-dresden.de/ing/informatik/smt/st/studium/lehrveranstaltungen?subject=381&lang=de&leaf=2&head=13&embedding_id=47eddfa7c5a54ed5be49042aff35a31b)

[Pflichtenheft](../master/asciidoc/pflichtenheft.md)

[Entwicklerdokumentation](../master/asciidoc/entwickler_doku.md)

## Git Workflow nach Treffen vom 14.11.2018

- sämtliche Dokumentation auf den master pushen
- **Immer einen Pull ausführen, bevor man pusht!**
- Für die Programmierung gilt folgendes:
  - für jedes Features eine neue Branch mit einem treffenden Namen erstellen. **Als Grundlage dafür den beta Branch nutzen!**
  - jeder kann beliebig viele parallele Feature Branches erstellen
  - Wenn in dem Feature keine groben Fehler mehr existieren und man meint, es sei fertig: auf den beta Branch pushen
  - Jetzt testen, ob der beta Branch ausführbar ist. Wenn nicht prüfen, ob es am eigenen Feature liegt.
  - **Bugfixing kann auf dem beta Branch passieren.**
- Regelmäßig (meist vor den Meilensteinen) wird der beta in den master Branch gemergt. Hier können wir über *CI* sehen, ob alles passt. Wenn nötig hier noch bugfixing betreiben.
- **Niemals ohne Abprache den beta auf den master mergen!**
  

## Projekt starten (manuell)
``` bash
mvn clean package
mvn spring-boot:run
```
