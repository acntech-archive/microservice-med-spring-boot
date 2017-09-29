# Mikrotjenester med Spring Boot
Denne applikasjonen demonstrerer hvordan man effektivt kan komme i gang med utvikling av mikrotjenester ved bruk av Java og Spring Boot. 
Applikasjonen omfatter et fiktivt og begrenset domene. 

Formålet er å vise hvordan man kan bygge mikrotjenester med en 
skalèrbar arkitektur med tanke på modularisering, konfigurasjon, monitorering, kodekvalitet, etc.

## Teknologi og rammeverk
Viktigste teknologier og rammeverk som benyttes i applikasjonen:
- [Java](https://www.java.com/en/)
- [Spring Boot](https://projects.spring.io/spring-boot/)
    - spring-boot-starter-web
    - spring-boot-actuator
    - spring-boot-starter-data-jpa
    - spring-boot-starter-test
- [JUnit](junit.org)
- [Mockito](http://site.mockito.org/)
- [Maven](https://maven.apache.org/)
---

## Modularisering
Applikasjonen består av følgende moduler:
- **web** - eksponerte REST-endepunkter og DTO’er
- **service** - tjenester med forretningslogikk som opererer på domenemodellen
- **domain** - intern domenemodell 

Det finnes mange ulike strategier og patterns for modularisering og dette må tilpasses hvert use-case. 
Vi har valgt å bruke en forenklet versjon av [onion pattern](https://www.infoq.com/news/2014/10/ddd-onion-architecture) som utgangspunkt.

---

## Bygg og test
Applikasjonen bygges med maven. For å bygge med enhetstester:
```
mvn clean install
```

Integrasjonstester kjøres med maven failsafe-plugin. Integrasjonstestene starter opp applikasjonen på en embedded Tomcat-server med en tom H2-database og kjører faktiske HTTP-kall mot applikasjonen. 

For å bygge med integrasjonstester: 
```
mvn clean install failsafe:integration-test failsafe:verify
```
For continuous integration bygges applikasjonen på  [Travis.](https://travis-ci.org/)

[![Build Status](https://travis-ci.org/acntech/microservice-med-spring-boot.svg?branch=master)](https://travis-ci.org/acntech/microservice-med-spring-boot)
---


## Kjøre applikasjonen
Applikasjonen pakkes som en war-fil og kan kjøres på flere ulike måter.

##### Embedded Tomcat
For å starte applikasjonen på en embedded tomcat-server kan man kjøre 
følgende java-kommando i target-folderen til web-modulen: 
```
java -jar employee-service.war
``` 

##### Standalone servlet-container
Det er også mulig å deploye war-filen direkte til en hvilken som helst servlet-container som feks Tomcat eller Jetty på vanlig måte.

##### Docker
Et tredje alternativ er å starte applikasjonen i en Docker-container. Vi har lagt ved et utgangspunkt for en Dockerfile. 
For å bygge og starte applikasjonen i Docker:
```
docker build -t "acntech/employee-service:latest" .
docker run -p 8080:8080 acntech/employee-service:latest
```
---

## Konfigurasjon
Spring Boot er et "opinionated" rammeverk som følger en rekke konvensjoner ift hvordan applikasjonen konfigureres. 

All konfigurasjon som rammeverket gjør kan overstyres. [Her](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html) er en gjennomgang av ulike mekanismer for konfigurasjon.

All konfigurasjon i applikasjonen ligger i application.yml filen. For å enkelt overstyre denne konfigurasjonen kan man sende inn jvm-parametere når man starter applikasjonen på følgende måte:
```
java -jar -Dserver.port=9999 employee-service.war
```

[Spring Cloud Config Server](https://spring.io/guides/gs/centralized-configuration/) 
er også et veldig bra alternativ for å håndtere konfigurasjon.

---

## Monitorering

Spring Boot Actuator benytte for enkel monitorering av applikasjonen. Actuator eksponerer REST-endepunkter for å hente ut en rekke ulike metrikker. Hvilke endepunkter man ønsker å eksponere kan konfigureres.

[Her](https://spring.io/guides/gs/actuator-service/) er en oversikt over tilgjengelige endepunkter 
som kan eksponeres.

I applikasjonen har vi eksponert følgende endepunkter:
```
/management/health (Helsesjekk)
/management/env (Alle miljøvariabler og konfigurasjon)
/management/beans (Oversikt over hvilke bønner som er lastet)
/management/dump (Utfører en Thread-dump)
/management/metrics (Viser diverse `metrics`)
/management/trace (Viser trace-informasjon om HTTP-kall)
/management/loggers (Vise og oppdatere logg-konfigurasjon)
```

---

## Logging
Logging gjøres ved bruk av [SLF4J](https://www.slf4j.org/) og [Logback](https://logback.qos.ch/). 
I applikasjonen ligger det et enkelt eksempel på logback-konfigurasjon i logback.xml-filen som kan utvides etter behov.

Når man utvikler en distribuert arkitektur med mikrotjenester er det viktig å kunne spore kall på tvers av tjenester. 
I applkasjonen ligger det et filter som henter ut en HTTP-header kalt `x-correlation-id`. Hvis denne ikke er satt genereres det en unik id. 
Denne id’en er unik per request og kommer med i alle logginnslag relatert til requesten.

I tillegg til logging av korrelasjons-id logges også alle innkommende requester og responsetider.

---

## Dokumentasjon
REST-endepunktene i applikasjonen dokumenteres ved bruk av [swagger](https://swagger.io/introducing-the-open-api-initiative/).
For å oppnå dette benyttes [swagger-annoteringer](http://docs.swagger.io/swagger-core/v1.5.0/apidocs/io/swagger/annotations/package-summary.html) i koden og en [maven-plugin](https://github.com/kongchen/swagger-maven-plugin) som genererer en swagger.json-fil i target-folderen til web-modulen når applikasjonen bygges.

API'et kan dermed visualiseres i verktøy som feks [swagger-ui](https://swagger.io/swagger-ui/)

<img src="https://i.imgur.com/yHtl1xD.png" width="30%" height="30%">


---

## Kodekvalitet og testdekning
Vi har lagt til JaCoCo-plugin for å måle testdekning på koden.
 
For å generere en kodekvalitetsrapport til [SonarQube](https://www.sonarqube.org/) 
kan man kjøre følgende kommando:
```
mvn clean install failsafe:integration-test failsafe:verify sonar:sonar
```

SonarQube kan startes i Docker med følgende kommando:
```
docker run --name sonarqube -p 9000:9000 -p 9092:9092 sonarqube
```
<img src="https://i.imgur.com/6qjo8Tb.png" width="50%" height="50%">

---

## Infrastruktur og cloud
Applikasjonen er utformet på en måte som gjør den agnostisk ift kjøretidsmijø.
Dette oppnår man blant annet ved å følge prinsippene for [the 12 factor app](https://12factor.net/).

Dette gjør at man full fleksibilitet ift hvor man ønsker å deploye applikasjon. 
Eksempler på mulige kjøretidsmiljøer:
- [Heroku](https://www.heroku.com/) på en embedded tomcat-server
- [Amazon EC2 Container Service](https://aws.amazon.com/ecs/) i en docker-container
- Eksisterende applikasjonsservere som en war-fil
