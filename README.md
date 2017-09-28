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

Det finnes mange ulike strategier og patterns for modularisering 
og dette må tilpasses hvert use-case. 
Vi har valgt å bruke en forenklet versjon av ["onion pattern"](https://www.java.com/en/) som utgangspunkt.
---

## Bygg og test
Applikasjonen bygges med maven. For å bygge med enhetstester: 
```
mvn clean install
```


Integrasjonstester kjøres med maven failsafe-plugin. 
Integrasjonstester starter opp applikasjonen på en embedded Tomcat-server med en tom H2-database 
og kjører faktiske HTTP-requester mot applikasjonen. 

For å bygge med integrasjonstester: 
```
mvn clean install failsafe:integration-test failsafe:verify
```
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
Det er også mulig å deploye war-filen direkte til en hvilken som helst servlet-container 
som feks Tomcat eller Jetty på vanlig måte

##### Docker
Et tredje alternativ er å starte applikasjonen i en Docker-container. 
Vi har lagt ved et utgangspunkt for en Dockerfile. 
Kjør følgende kommandoer for å bygge og starte applikasjonen i docker:
```
docker build -t "acntech/employee-service:latest" .
docker run -p 8080:8080 acntech/employee-service:latest
```
---

## Konfigurasjon
Spring Boot er et "opinionated" rammeverk som følger en rekke konvensjoner ift hvordan applikasjonen konfigureres. 

All konfigurasjon som rammeverket gjør kan overstyres. [Her](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html) 
er en gjennomgang av ulike mekanismer for konfigurasjon.

Vi har lagt all konfigurasjon i application.yml filen. 
For å enkelt overstyre denne konfigurasjonen kan man 
sende inn jvm-parametere når man starter applikasjonen på følgende måte:
```
java -jar -Dserver.port=9999 employee-service.war
``` 

Vi har også svært god erfaring med å bruke [Spring Cloud Config Server](https://spring.io/guides/gs/centralized-configuration/) 
for å håndtere konfigurasjon.
---

## Monitorering

For monitorering har vi lagt til Spring Boot Actuator. 
Spring Boot Actuator gjør det mulig å eksponerer REST-endepunkter for å hente ut ulike metrikker fra applikasjonen. 
Hvilke endepunkter man ønsker å eksponere kan styres ved standard konfigurasjon. 

[Her](https://spring.io/guides/gs/actuator-service/) er en oversikt over tilgjengelige endepunkter 
som kan eksponeres.

I vår eksempel-applikasjon har vi eksponert følgende endepunkter:
````
/management/health (Helsesjekk)
/management/env (Miljøinformasjon)
/management/beans (Oversikt over hvilke bønner som er lastet)
/management/dump (Utfører en Thread-dump)
/management/metrics (Viser diverse `metrics`)
/management/trace (Viser trace-informasjon om HTTP-kall)
/management/loggers (Vise og oppdatere logg-konfigurasjon)
````

Actuator blir konfigurert i application.yml. De forskjellige endepunktene kan aktiveres og deaktiveres ved å sette `enabled`-flagget til `true` eller `false`.
---

## Logging
Logging gjøres ved bruk av [SLF4J](https://www.slf4j.org/) og [Logback](https://logback.qos.ch/). 
Vi har lagt ved et enkelt eksempel på logback-konfigurasjon i logback.xml-filen som kan utvides etter behov.


Når man utvikler en distribuert arkitektur med mikrotjenester er det viktig å kunne spore kall på tvers av tjenestene. 
I applikasjonen har vi lagt til et filter som tar inn en HTTP-header kalt `x-correlation-id`. 
Hvis denne ikke er satt genereres det en unik id. 
Denne id’en er unik per request og kommer med i alle logginnslag relatert til requesten.

I tillegg til logging av korrelasjons-id har vi også lagt til logging av alle innkommende requester 
og responsetider.
---

#### Dokumentasjon
REST-endepunktene i applikasjonen dokumenteres ved bruk av [swagger](https://swagger.io/introducing-the-open-api-initiative/).
Endepunktene annoteres med [swagger-annoteringer](http://docs.swagger.io/swagger-core/v1.5.0/apidocs/io/swagger/annotations/package-summary.html).

Når applikasjonen bygges genereres det en swagger.json fil i target-folderen til web-modulen.
Denne kan lastes inn i feks [swagger-ui](https://swagger.io/swagger-ui/) for å visualisere grensesnittene.  
---

#### Kodekvalitet og testdekning
Kodekvalitet og testdekning er viktige metrikker for å måle kvaliteten på kodebasen. 
Vi har lagt til JaCoCo-plugin for måling av testdekning.
 
For å generere en kodekvalitetsrapport til [SonarQube](https://www.sonarqube.org/) 
kan man kjøre følgende kommando:
```
mvn clean install failsafe:integration-test failsafe:verify sonar:sonar
```

Hvis man ikke har en kjørende instans av SonarQube kan man starte opp SonarQube i docker slik:
```
docker run --name sonarqube -p 9000:9000 -p 9092:9092 sonarqube
```
---

#### Infrastruktur og cloud
Applikasjonen er utformet på en måte som gjør den agnostisk ift kjøretidsmijø.
Dette oppnår man ved å følge prinsippene for [the 12 factor app](https://12factor.net/).

På grunn av dette har man full fleksibilitet ift hvor man ønsker å deploye applikasjon. 
Eksempler på mulige kjøretidsmiljøer:
- [Heroku](https://www.heroku.com/) på en embedded tomcat-server
- [Amazon EC2 Container Service](https://aws.amazon.com/ecs/) i en docker-container
- Eksisterende applikasjonsservere som en war-fil