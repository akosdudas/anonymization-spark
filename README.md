# Anonymizer

Az anonimizáló alkalmazás futásához szükséges eszközök: 

1. Datastax Apache Cassandra 3.0 vagy későbbi
2. Spark 1.6.3 vagy későbbi
3. JDK 1.8.0 vagy későbbi. 
4. python 2.7 

Adatbázis beüzemelése:

1. *./path/cassandra/bin/cassandra* segítségével elindítató az adatbázis lokálisan.
2. *./path/cassandra/bin/cqlsh* parancs megnyitja az cql interfészt az adatbázissal való kommunikáláshoz.
3. A cassandra.txt file segítségével létrehozható a dolgozat során használt adatbázis tábla. 
4. Az src/main/resources/cassandra.properties file tartalma módosítható, amennyiben más címen vagy porton szeretné a felhasználó futattni az adatbázist.

Alkalmazás beindítása:

1. Buildet a projekt könyvtárban *mvn clean install* parancs segítsével lehet végrehajtani. 
2. Az alkalmazást a *java -jar target/anonymization-0.0.1-SNAPSHOT.jar* parancs segítségével lehet futtatni.
3. A http://localhost:8080/api/report_data/ címen érhető el ezután az alkalmazás. [Postman](https://www.getpostman.com/) segítségével például REST kéréseket tudunk megfogalmazzni.

Az integrácós tesztek külön-külön a *python test_.py* parancs segítségével futatthatók.
