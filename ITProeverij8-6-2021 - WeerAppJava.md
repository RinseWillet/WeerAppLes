# IT Proeverij 8-6-2021

## Programmeer een WeerApp met Java en OpenWeatherMap

# Stap 1

Als allereerste maken we een Java project aan in Repl.it. Daarna moeten we zelf een API Key aanmaken op https://openweathermap.org/api . Hier kunnen we ons subscriben op Current weather data en je krijgt een verificatie e-mail toegestuurd. Verificeer jezelf en dan kun je met je eigen API key bijvoorbeeld : api.openweathermap.org/data/2.5/weather?q=den+bosch&appid={API key} het huidige weer in een browser aanvragen van Den Bosch. Wat je op het scherm krijg is een zgn. JSON (JavaScript Object Notation) antwoord terug, heel herkenbaar aan de { }. En binnen die brackets staat een heleboeel labels met waarden (name : stadsnaam, temp : temperatuur etc.). Uiteindelijk is ons doel wordt deze JSON response in te laden in ons programma en vervolgens de waardes uit het antwoord te peuteren waarin we geïnteresseerd zijn. Maar voor nu wil ik de API key als een statische String in mijn programma hebben, zodat ik hem vanuit de main method (die static is) kan aanvragen.

```java
public class Main {

    static String apiKey = "apikey";

    public static void main(String[] args) {

    }
}
```
Ik wil deze url gaan aanroepen vanuit Java en daarvoor moeten we eerst een URL object aanmaken, waarin het webadres komt te staan (laat docs zien, kunnen nog veel meer opties meegegeven worden). Dit is dus geen variabele zoals je bijv. bij JavaScript veel ziet.

```java
import java.net.URL;

public class Main {

    static String apiKey = "apikey";

    public static void main(String[] args) {
        URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + plaats + "&appid=" + apiKey + "&lang=nl")
        System.out.println(url);
    }
}
```
Dit werkt niet, omdat er een Exception wordt genoemd die niet declared is - dit is een fout
die we moeten afhandelen met een try en catch block. En met een try en catchblock zeg je eigenlijk wat er
gedaan moet worden als er een Exception plaatsvindt. Finally gebeurt altijd aan het eind. Dus we gaan het aanmaken van de URL en het printen van dat object in een try-catch blok zetten.

```java
import java.net.URL;

public class Main {

    static String apiKey = "apikey";

    public static void main(String[] args) {
        try {
            URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=Den+Bosch&appid=" + apiKey)
            System.out.println(url);
        } catch (Exception e) {
            System.out.println("Weer opvragen mislukt");
        } finally {
            System.out.println("***********>>>> Tot zover het weerbericht <<<<**************")
        }
    }
}
```

# Stap 2

Ik kan de complete code in de Main class gaan schrijven, maar dan wordt de code erg lang en
bovendien wil ik bepaalde taken concentreren in aparte objecten die ik wil aanmaken. Die objecten maak je met behulp van een class in Java (een soort blauwdruk) op basis waarvan ze geïnstantieerd kunnen worden. Ik wil daarom een class aanmaken waarmee ik objecten kan maken die mijn API kunnen aanroepen en vervolgens een antwoord kunnen ontvangen. Ook moet dit object een url adres weten dus daarom maak ik een leeg field aan waaraan een URL object gelinkt kan worden. Ik maak de file weerData.java aan.
Daarin maken we een private field voor een URL en die zetten we op null. Private is om hem af te schermen van buiten de class.
Dit field is nu leeg en kunnen we vullen met ons URL object zodra we een weerData object aanmaken. Om dit te doen hebben we enkel nog een constructor nodig waarin we een URL object kunnen meegeven.

```java
import java.net.URL;

public class weerData{

    //field voor het URL object
    private URL httpurl = null;

    //constructor
    public weerData(URL aanmaken) {
        httpurl = aanmaken;
        System.out.println("gelukt : " + httpurl);
    }
}
```
Vervolgens ga ik als test zo'n weerData object proberen aan te maken vanuit het try block in de main methode in de Main class
```java
weerData ht = new weerData(url);
```
Dat werkt. Nu wil ik een methode in de weerData class maken waarmee ik echt de api kan gaan aanroepen. En dit kan ik het beste doen met een HttpURLConnection object. (Laat docs zien) - Dit is een class in de Java API die heel veel methodes in zich heeft die handig zijn bij het maken van een HTTP connectie, zoals wij gaan doen. We moeten hem eerst importeren.

```java
import java.net.HttpURLConnection;
```
Dan de methode schrijven in de weerData class - we gaan al de connectie openen
```java
public String httpConnect() {
    System.out.println("HTTP Request starten...");
    HttpURLConnection verbinding = (HttpURLConnection) httpurl.openConnection();
    return "gelukt";
}
```
En dit geeft een error, want ook hier moeten we een potentiële Exception afhandelen of doorgeven (throwsen). Ik laat deze error throwsen naar de Main class waar de potentiële error van dit object wordt afgehandeld.
```java
public String httpConnect() throws Exception {
```
vervolgens wil ik ook een connection maken met deze url en ik wil checken welke HTTP responsecode ik krijg van deze url. Er zijn heel veel HTTP responsecodes (bijv. 404 Not found of 403 Forbidden) en wij willen status 200 krijgen - alles okay.
```java
verbinding.connect();
System.out.println("Connection Response code:" + verbinding.getResponseCode());
```
Dit werkt. Maar als we een niet bestaande stad als url meegeven ('Elysium Planitia' bijv.) dan krijgen we inderdaad een status 404 (nl. niet gevonden) 

# Stap 3

We hebben nu verbinding gemaakt vanuit Java met de API. Nu moeten we gaan proberen een antwoord binnen te krijgen en deze uit te lezen. En dit gaan we doen met een Stringwriter en een Inputstream. Dit is nodig omdat je niet het gehele antwoord in 1 keer binnenkrijgt maar character voor character (het gaat wel zo snel dat het lijkt alsof het in 1 keer is). En een Inputstream is een class waarmee je een stroom informatie die vloeit uit een open connectie (HTTP) kunt redigeren. Ook deze heeft een heleboel methodes in de Java API waarmee je de 'datastream' kunt managen. We maken eerst een lege Inputstream (een field die null is) in de weerData classe en die gaan we dadelijk vullen met de data die uit de http connectie binnenkomt. Vervolgens maken we ook alvast een Stringwriter aan. Een String is in principe eenmaal te instantiëren (pool - leashes, het zit wel iets ingewikkelder in elkaar) en met een Stringwriter kunnen we iedere keer een karakter dat binnenkomt toevoegen om uiteindelijk een String te maken van het complete antwoord. Uiteraard moeten we deze classes importeren.

```java
import java.io.InputStream;
import java.io.StringWriter;
```

en in de httpConnect():

```java
StringWriter antwoordStringWriter = new StringWriter();
InputStream antwoord = null;
```

Laten we eens kijken wat we aan data binnenkrijgen, daar de InputStream antwoord te vullen met de data uit 'verbinding' d.m.v. de getInputStream() methode:
```java
antwoord = verbinding.getInputStream();
System.out.println(antwoord.read());
```
En hij print 123. Dat lijkt raar, want de API call begint toch echt met een { van een volledig JSON antwoord. En het originele JSON antwoord lijkt ook veel langer. Gelukkig is 123 ook de ASCII code voor het { teken. En het feit dat er maar 1 teken binnen is, bewijst dat je het antwoord niet in 1 keer binnenkrijgt. Je zult karakter na karakter moeten gaan wegschrijven. We stoppen eerst het antwoord weg in een integer en deze kunnen we vervolgens wegschrijven in de Stringwriter, aangezien die ook charactercodes(getallen) accepteerd:
```java
antwoord = verbinding.getInputStream();
int leesGetal = antwoord.read();
antwoordStringWriter.write(leesGetal);
System.out.println(antwoordStringWriter);
```
We blijven natuurlijk het probleem krijgen waar de rest van de karakters blijft. Laten we nog eens een antwoord.read() onder de code toevoegen:
```java
antwoordStringWriter.write(antwoord.read());
System.out.println(antwoordStringWriter);
```
Nu zien we dat {" geprint wordt. M.A.W. er is een karakter binnengekomen weer en weggeschreven. Om het gehele antwoord weg te schrijven kunnen we ofwel heel vaak antwoord.read() aanroepen, maar handiger is een while loop te gebruiken die blijft doorlezen zolang er getallen binnenkomen uit de Inputstream:
```java
while (leesGetal > 0) {
    System.out.println("het getal : " + leesGetal);
    leesGetal = antwoord.read();
    antwoordStringWriter.write(leesGetal);
}
System.out.println(antwoordStringWriter);

```
We zien nu dat het hele JSON response in weggeschreven in de antwoordStringWriter en we zien ook dat er een reeks aan getallen in leesgetal wordt geschreven. Gefeliciteerd, je hebt nu eigenlijk al het JSON respons binnengehaald! Nog twee dingen die we moeten doen in deze klasse: we willen de antwoordStringWriter als een String returnen naar de main methode in de MAIN class en ik wil ook dit in een try-catch block zetten, domweg om de duidelijke HTTP status fouten hier al af te vangen:

```java
import java.io.IOException;

try { 
    antwoord = verbinding.getInputStream();
    ...
    antwoordStringWriter.write(leesGetal);
}
} catch (Exception httpfout) {
    //print de responsecode, als die niet 200 is (= alles okay), dan een IOException throwen
            System.out.println("HTTP ResponseCode : " + verbinding.getResponseCode() + " - plaats niet gevonden");
            if (verbinding.getResponseCode() != 200) {
                throw new IOException(verbinding.getResponseMessage());
            }
}
return antwoordStringWriter.toString();
```
De IO Exception die throw ik (gooi ik) om te zorgen dat ook het in de MAIN class duidelijk is dat er iets fout is gegaan. Als ik dit if statement weglaat dan zie je dat de foutmelding in de MAIN class niet getriggerd wordt, en dat wil ik in dit geval wel (er is iets foutgegaan, en dit is er specifiek foutgegaan).

# Stap 4
Nu is de weerData class eigenlijk af en kunnen we weer naar de Main class kijken hoe we met de string van het JSON antwoord om kunnen gaan. Als eerste moeten we de antwoord String uit de weerData classe en de httpConnect() methode in een string in de MAIN classe krijgen: 

```java
 String jsonAntwoord = ht.httpConnect();
 System.out.println("in de main, het antwoord : " + jsonAntwoord);
 ```
We zien dat er helemaal aan het einde van het antwoord een teken staat (￿) en die willen we er even afhalen. Dit is handig mochten we later libraries gaan gebruiken om de JSON te parsen (i.e. opsplitsen en uitlezen naar losse datapunten). En dit kunnen we doen door een zgn. substring te maken van het hele antwoord, zonder het laatste teken. Bij de substring methode gebruik geef je de begin- en eind-index getallen aan van een string en daar wordt een nieuwe string mee gemaakt. Voorbeeld:
```java
String groet = "hallo"
String geknipt = groet.substring(0, 3) (LET OP! LAATSTE indexnummer is exclusief - 'tot aan')
System.out.println(geknipt) -> hal
```
Hier gaan we het laatste teken eraf knippen door de lengte van het jsonAntwoord te nemen -1 :
```java
//buiten try-catch block
String antwoord = "";

//in try block
antwoord = jsonAntwoord.substring(0,(jsonAntwoord.length()-1));
```
In principe hebben we nu het antwoord en alle data al op het scherm staan. Dadelijk gaan we natuurlijk uit deze String de data halen die we interessant vinden (plaats, weertype, beschrijving, temperatuur, min. max. temperatuur, luchtdruk, etc.). Maar eerst moeten we nog de invoer van de gebruiker gaan regelen. Want nu is de stad 'hardcoded' in het URL object, en dat willen we niet. We willen keuze hebben. En die kunnen we inbouwen, en dat lijkt wel een beetje op de API key invoer in het URL object. Wat we moeten doen is zorgen dat er een plaatsnaam ingevoerd kan worden, dat deze aan de url meegegeven kan worden EN dat er bij plaatsen met spaties, de spatie vervangen wordt door een +. De eerste stap is de plaatsnaam variabel te maken.

```java
//buiten try block - LET OP: de + is noodzakelijk
String plaats = "Den+Helder";

//in try block
URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + plaats + "&appid=" + apiKey);
```
Vervolgens gaan we om gebruikersinput moeten vragen en dit kunnen we doen met een zgn. Scanner.

```java
import java.util.Scanner;

    String plaats = "";

    System.out.println("Voer je plaats in: ");
    Scanner invoer = new Scanner(System.in);
    String input = invoer.nextLine();
    plaats = input;

```
Dit lijkt te werken, maar wat nou als iemand bij ongeluk op enter drukt zonder een plaats in te voeren? Om dat te voorkomen heb ik een default stad ingevoerd (nl. Den Bosch) en een if statement toegevoegd om te checken of de gebruikersinput leeg is.

```java
String stad = "Den+Bosch";
String input = invoer.nextLine();
if (input.isEmpty()){
    input = stad;
}
plaats = input;
```
als laatste heb ik een stukje code toegevoegd om te zorgen dat de plaats altijd in lower-case is en dat de spaties vervangen worden door een plus teken. Ik merkte dat dit soms problemen gaf als dit niet het geval was.

```java
plaats = input.replaceAll(" ", "+").toLowerCase();
```

# Stap 5

We krijgen nu weerdata binnen van de plaats die wij willen. Maar het antwoord dat we krijgen is natuurlijk lastig leesbaar. Buiten replit bestaan er allerlei libraries die je in Java heel makkelijk kunt toepassen om deze JSON string te parsen zodat je kunt zoeken op bepaalde data (bijv. temperatuur) en daar de waarde bij krijgen. Voorbeelden van zulke libraries zijn Jackson (https://www.baeldung.com/jackson-object-mapper-tutorial), org.simple, org.json.simple (https://www.baeldung.com/java-org-json) en nog anderen. Het probleem is dat deze niet in Replit werken en dus moeten we zelf handmatig de string gaan proberen te parsen. Ga je zelf in IntelliJ, Eclipse of andere IDE aan de gang, dan zou ik zeker het gemak van deze libraries gebruiken! Wij moeten dus zelf aan de slag, maar dat laat ons wel heel wat functies van String in Java kennen. Om te beginnen is het afdrukken van de data in de console wel een hele eigen functie en eigen entiteit. Hier moet dus ook een aparte classe voor worden aangemaakt, waarmee je een printend object kunt instantiëren m.b.v. het antwoord dat we uit het weerData object hebben gekregen. Hierin moet een methode komen die in het object aan te roepen is om de data te gaan printen.

```java
//nieuwe file weerDataPrinter.java

public class weerDataPrinter {
    public void getData(String antwoord) {
        System.out.println("printen maar");
        System.out.println(antwoord);
    } 
}

// en in de Main.java, in het try block

weerDataPrinter printer = new weerDataPrinter();
printer.getData(antwoord);
```

We kunnen de string dus doorgeven aan het printer object met de getData methode. Nu moeten we gaan kijken om de leesbaarheid te verbeteren. Als eerste kunnen we de plaatsnaam uit het antwoord peuteren. We gaan dit doen door gebruik te maken van de substring methode en de indexOf methode. Bij de substring methode geef je het begin indexgetal aan (vanaf) en het eind indexgetal (tot waar) binnen een string en knipt daar een nieuwe string van. We gaan die methode inzetten om zo de waarden van bepaalde data uit te knippen. Je zou dit kunnen doen als volgt:
```java
/// bij The Hague
String voorbeeld = antwoord.substring(67,72);
System.out.println(voorbeeld);
// geeft Clear
```
Dit werkt wel, maar is erg onhandig, en bovendien kun je al je indexgetallen gaan aanpassen als de beschrijving langer of korter is. Maar wat we wel zien is dat de beschrijving van weertype na de eerste "main" komt in de JSON en dat de plaatsnaam bijvoorbeeld na "name" komt. Laten we proberen de plaatsnaam uit de JSON te peuteren. We kunnen dus vragen of ze van die keywords een index getal kunnen geven. En met indexOf("name") wordt het indexgetal in de String gegeven waar "name" begint.
```java
int voorbeeld2 = antwoord.indexOf("name");
System.out.println(voorbeeld2);
```
En dit kunnen we combineren:
```java
String voorbeeld3 = antwoord.substring(antwoord.indexOf("name"), antwoord.indexOf("name")+18);
System.out.println(voorbeeld3);
```
en ook dit werkt ten dele en je komt in de problemen zodra de plaatsnaam korter of langer wordt. Je moet eigenlijk de substring laten lopen tot de komma die het volgende label aankondigt in de JSON string. En het gave van indexOf is dat je niet alleen de String kunt meegeven ("name") maar ook vanaf waar in de String die moet zoeken. We zien in het JSON antwoord dat de plaatsnaam tussen "" staat, en dat er na de laatste " een , volgt, en daarna een nieuw label dat ook met een " begint. Eigenlijk moeten we dus zoeken voor het eind-indexgetal voor het begin indexgetal van ",". En de indexOf van deze draad aan tekens moet gevonden worden, direct na "name". Dit kun je als volgt doen:
```java
String voorbeeld4 = antwoord.substring(antwoord.indexOf("name")+7, antwoord.indexOf("\",\"",antwoord.indexOf("name")));
System.out.println(voorbeeld4);
```
En nu zie je dat de lengte van de plaatsnaam niet uitmaakt, er wordt altijd de volledige naam uit het JSON antwoord gehaald. Met deze techniek kunnen nu ook vrij eenvoudig de beschrijving (dat na "description" staat) uit de JSON halen:

```java
String plaatsRes = antwoord.substring(antwoord.indexOf("name")+7, antwoord.indexOf("\",\"",antwoord.indexOf("name")));

String beschrijvingRes = antwoord.substring(antwoord.indexOf("description")+14, antwoord.indexOf("\",\"",antwoord.indexOf("description")));
```
We hebben nu de plaatsnaam en weertype en de beschrijving uit de JSON halen en in losse Strings stoppen.

# Stap 6

Nu gaan we de andere data uitlezen, te beginnen met de temperatuur en de min. en max., daarna de luchtdruk, en vervolgend de vochtigheid. We kunnen eigenlijk dezelfde techniek als voor de plaatsnaam etc. gebruiken. Alleen moeten we nu getallen (hele en kommagetallen) uit de JSON halen. de temperatuur is een kommagetal, en je kunt in Java kommagetallen gebruiken als Float of Double. Hier gebruiken we de double, en we moeten dus de "Double waarde" van een specifiek stukje string uit de JSON halen. Dit doe je met Double.valueOf en de de substring en indexOf combinatie, alleen nu zoeken we naar het kopje "temp":

```java
double tempCRes = Double.valueOf(antwoord.substring(antwoord.indexOf("temp")+6, antwoord.indexOf(",\"",antwoord.indexOf("temp"))));
```
We kunnen nu hetzelfde doen voor de minimum ("temp_min") en maximum ("temp_max") temperaturen:

```java
double tempCminRes = Double.valueOf(antwoord.substring(antwoord.indexOf("temp_min")+10, antwoord.indexOf(",\"",antwoord.indexOf("temp_min"))));

double tempCmaxRes = Double.valueOf(antwoord.substring(antwoord.indexOf("temp_max")+10, antwoord.indexOf(",\"",antwoord.indexOf("temp_max"))));
```
Natuurlijk krijg je hier getallen op 2 decimalen achter de komma en dat is eigenlijk niet per se nodig voor een weerApp.
En we kunnen dat format vrij eenvoudig aanpassen met de System.out.printf functie. In deze functie geef je eerst het format van de string aan, en dit doe je met codes achter het % teken, en vervolgens geef je de argumenten aan die volgens dat format geprint moeten worden (zie ook: https://www.cs.colostate.edu/~cs160/.Summer16/resources/Java_printf_method_quick_reference.pdf):

```java
System.out.printf("Plaats : " + plaatsRes + " - Weertype : " + beschrijvingRes);
System.out.printf("\nTemperatuur : %.1f Min. : %.1f Max. : %.1f graden Celsius", tempCRes, tempCminRes, tempCmaxRes);
```
de %.1f geeft aan dat we 1f floating point number (double of float) na de komma willen hebben. We kunnen ook de luchtdruk (in hPa of mBar) uit het antwoord krijgen. Dit is altijd een integer (geen komma) in deze JSON:

```java
int luchtdrukRes = Integer.valueOf(antwoord.substring(antwoord.indexOf("pressure")+10, antwoord.indexOf(",\"",antwoord.indexOf("pressure"))));
```

# Stap 7

Vervolgens kunnen we de luchtvochtigheid (humidity) uit de JSON halen. En nu lopen we tegen een probleem aan, omdat voor de meeste steden er na de humidity een "," volgt met een nieuw label, maar voor andere steden volgt "} - en dan werkt onze truc niet meer. Wat we wel weten is dat de luchtvochtigheid een percentage is, en dat die dus uit 1, 2, of 3 getallen bestaat. Daarom kunnen we een stukje code schrijven die checkt of de humidity waarde uit 1, 2, of 3 getallen bestaat en vervolgens op basis daarvan een substring maakt vanaf de indexOf("humidity"). We doen dit in een if-statement (als iets zo is, dan) gecombineerd met een else if (anders als). En wat we gaan vragen in het if statement is of het karakter dat op een indexgetal na "humidity" een getal is. Dit doen we met Character.isDigit (is het karakter een getal):

```java
System.out.println(Character.isDigit('1')); //print true
System.out.println(Character.isDigit('q')); //print false
```

en om het specifieke karakter in het antwoord aan te wijzen gebruiken we de charAt methode.

```java
System.out.println(antwoord.charAt(antwoord.indexOf("humidity")+10)); //gebruik ook + 0 en + 1 en + 11 en + 9;
```

en dan nu combineren of dat character een getal is of niet :

```java
System.out.println(Character.isDigit(antwoord.charAt(antwoord.indexOf("humidity")+10)));
```

nu kunnen we deze vraag ingeven in een serie van if-statements, waarbij we vragen of het karakter op 12 plekjes na het begin "humidity" een getal is (dan heb je dus 100 als luchtvochtigheid), of op 11 plekjes (percentage 10-99) of op 10 plekjes (percentage 9 of lager). En als er uit deze if statements true is wil ik een substring maken van het eerste getal (+10) tot en met het indegetal waar het karakter nog een getal is:

```java
int vochtigheidRes = 0;
if(Character.isDigit(antwoord.charAt((antwoord.indexOf("humidity")+12)))) {
    vochtigheidRes = Integer.valueOf(antwoord.substring(antwoord.indexOf("humidity") + 10, antwoord.indexOf("humidity") + 13));
} else if (Character.isDigit(antwoord.charAt((antwoord.indexOf("humidity") + 11)))) {
    vochtigheidRes = Integer.valueOf(antwoord.substring(antwoord.indexOf("humidity") + 10, antwoord.indexOf("humidity") + 12));
} else {
    vochtigheidRes = Integer.valueOf(antwoord.substring(antwoord.indexOf("humidity") + 10, antwoord.indexOf("humidity") + 11));//
}
```

Als laatste ga ik de luchtdruk en de luchtvochtigheid ook printen:

```java
System.out.printf("\nLuchtdruk : %4d hPa Luchtvochtigheid : %3d procent", luchtdrukRes, vochtigheidRes);
```
de %4d geeft aan dat het er tot 4 integers te verwachten zijn voor de luchtdruk, en de %3d tot 3 integers voor de luchtvochtigheid.

# Stap 8

Als allerlaatste kunnen we ook de windgegevens uitladen. We beginnen met de windsnelheid in m/s die als kommagetal in de JSON staat en altijd na "speed" komt:

```java
double windSpeedRes = Double.valueOf(antwoord.substring(antwoord.indexOf("speed")+7, antwoord.indexOf(",\"deg\"")));
```

Daarna willen we de windrichting inladen. Deze staat achter "deg" in de JSON en is een integer, het zijn namelijk kompasgraden (van 0 - 359). Maar net zoals bij de luchtvochtigheid, wisselt het 'uiteinde' van de windrichting waarde, en we moeten op dezelfde manier dus gaan checken of na "deg" er 1, 2, of 3 cijfers staan:

```java
//windrichting (van graden naar N-Z-O-W) - dit is extra
    int richtingRes = 0;
    if (Character.isDigit(antwoord.charAt((antwoord.indexOf("deg") + 7)))) {
        richtingRes = Integer.valueOf(antwoord.substring(antwoord.indexOf("deg") + 5, antwoord.indexOf("deg") + 8));
    } else if (Character.isDigit(antwoord.charAt((antwoord.indexOf("deg") + 6))))  {
        richtingRes = Integer.valueOf(antwoord.substring(antwoord.indexOf("deg") + 5, antwoord.indexOf("deg") + 7));
    } else {
        richtingRes = Integer.valueOf(antwoord.substring(antwoord.indexOf("deg") + 5, antwoord.indexOf("deg") + 6));
    }
```
We hebben nu de windrichting in graden, en dat is op zich prima, maar het is natuurlijk nog mooier als je de windrichting zelf kunt weergeven. Daarvoor gaan we een simpel if-statement reeks gebruiken die checkt in welk gedeelte van de cirkel van 360 (bijv. 337-23 = Noord, 22-68 = NoordOost, 157-203 = Zuid, allemaal stukken van 45 graden) de wind komt en daar een letter aan toewijst. We gaan hier met Logical Operators werken voor OR (of - ||) en AND (en - &&):

```java
String kompasWaarde = "";
        if ((richtingRes > 337) || (richtingRes < 23)) {
            kompasWaarde = "N"; // 0 graden
        } else if ((richtingRes > 22) && (richtingRes < 68)) {
            kompasWaarde = "NO"; // 45 graden
        } else if ((richtingRes > 67) && (richtingRes < 113)) {
            kompasWaarde = "O"; // 90 graden
        } else if ((richtingRes > 112) && (richtingRes < 158)) {
            kompasWaarde = "ZO"; // 135 graden
        } else if ((richtingRes > 157) && (richtingRes < 203)) {
            kompasWaarde = "Z"; // 180 graden
        } else if ((richtingRes > 202) && (richtingRes < 248)) {
            kompasWaarde = "ZW"; //225 graden
        } else if ((richtingRes > 247) && (richtingRes < 293)) {
            kompasWaarde = "W"; //270 graden
        } else if ((richtingRes > 292) && (richtingRes < 338)) {
            kompasWaarde = "NW"; //315 graden
        }

```
Als allerlaatste willen we de windsnelheid en richting nog netjes uitprinten:

```java
System.out.printf("\nWind : %s %.1f m/s\n", kompasWaarde, windSpeedRes);

```
En dan is het een kwestie van de code hier en daar opschonen en dan is de WeerApp in Java klaar!