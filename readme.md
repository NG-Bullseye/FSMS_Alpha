# Readme

## Link zu den Folien

[![Hier klicken]()](https://tu-dresden.de/ing/informatik/smt/st/studium/lehrveranstaltungen?subject=381&lang=de&leaf=2&head=13&embedding_id=47eddfa7c5a54ed5be49042aff35a31b)

## Kickstart

The Webshop module is a template project to bootstrap the Java project in the Software Engineering lab.
The project is supposed to be copied into the group's repository to get it started easily.
It contains the following features:

- a skeleton Java 8 web application based on Spring Boot and Salespoint framework (see `src/main/java`)
- a placeholder Asciidoc file in `src/main/asciidoc` for documentation

## How to run the application?

- In the IDE: find `Application.java`, richt-click project, select "Run As > Java Application"
- From the command line: run `mvn spring-boot:run`

## How to package the application?

- Run `mvn clean package`. The packaged application (a JAR in `target/`) can be run with `java -jar $jarName`.
