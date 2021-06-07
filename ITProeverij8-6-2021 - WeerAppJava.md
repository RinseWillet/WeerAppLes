# IT Proeverij 8-6-2021

## Programmeer een WeerApp met Java en OpenWeatherMap

# Stap 1

Als allereerste maken we een Java project aan in Repl.it. Daarna moeten we zelf een API Key aanmaken op https://openweathermap.org/api . Hier kunnen we ons subscriben op Current weather data en je krijgt een verificatie e-mail toegestuurd. Verificeer jezelf en dan kun je met je eigen API key bijvoorbeeld : api.openweathermap.org/data/2.5/weather?q=den+bosch&appid={API key} het huidige weer in een browser aanvragen van Den Bosch. Wat je op het scherm krijg is een zgn. JSON (JavaScript Object Notation) antwoord terug, heel herkenbaar aan de { }. En binnen die brackets staat een heleboeel labels met waarden (name : stadsnaam, temp : temperatuur etc.). Uiteindelijk is ons doel wordt deze JSON response in te laden in ons programma en vervolgens de waardes uit het antwoord te peuteren waarin we geïnteresseerd zijn. Maar voor nu wil ik de API key als een statische String in mijn programma hebben, zodat ik hem vanuit de main method (die static is) kan aanvragen.

```
public class Main {

    static String apiKey = "apikey";

    public static void main(String[] args) {

    }
}
```
Ik wil deze url gaan aanroepen vanuit Java en daarvoor moeten we eerst een URL object aanmaken, waarin het webadres komt te staan (laat docs zien, kunnen nog veel meer opties meegegeven worden). Dit is dus geen variabele zoals je bijv. bij JavaScript veel ziet.

```
import java.net.URL;

public class Main {

    static String apiKey = "apikey";

    public static void main(String[] args) {
        URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=Den+Bosch&appid=" + apiKey)
        System.out.println(url);
    }
}
```
Dit werkt niet, omdat er een Exception wordt genoemd die niet declared is - dit is een fout
die we moeten afhandelen met een try en catch block. En met een try en catchblock zeg je eigenlijk wat er
gedaan moet worden als er een Exception plaatsvindt. Finally gebeurt altijd aan het eind. Dus we gaan het aanmaken van de URL en het printen van dat object in een try-catch blok zetten.

```
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

```
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
```
weerData ht = new weerData(url);
```
Dat werkt. Nu wil ik een methode in de weerData class maken waarmee ik echt de api kan gaan aanroepen. En dit kan ik het beste doen met een HttpURLConnection object. (Laat docs zien) - Dit is een class in de Java API die heel veel methodes in zich heeft die handig zijn bij het maken van een HTTP connectie, zoals wij gaan doen. We moeten hem eerst importeren.

```
import java.net.HttpURLConnection;
```
Dan de methode schrijven in de weerData class - we gaan al de connectie openen
```
public String httpConnect() {
    System.out.println("HTTP Request starten...");
    HttpURLConnection verbinding = (HttpURLConnection) httpurl.openConnection();
    return "gelukt";
}
```
En dit geeft een error, want ook hier moeten we een potentiële Exception afhandelen of doorgeven (throwsen). Ik laat deze error throwsen naar de Main class waar de potentiële error van dit object wordt afgehandeld.
```
public String httpConnect() throws Exception {
```
vervolgens wil ik ook een connection maken met deze url en ik wil checken welke HTTP responsecode ik krijg van deze url. Er zijn heel veel HTTP responsecodes (bijv. 404 Not found of 403 Forbidden) en wij willen status 200 krijgen - alles okay.
```
verbinding.connect();
System.out.println("Connection Response code:" + verbinding.getResponseCode());
```
Dit werkt. Maar als we een niet bestaande stad als url meegeven ('Elysium Planitia' bijv.) dan krijgen we inderdaad een status 404 (nl. niet gevonden) 

# Stap 3

We hebben nu verbinding gemaakt vanuit Java met de API. Nu moeten we gaan proberen een antwoord binnen te krijgen en deze uit te lezen. En dit gaan we doen met een Stringwriter en een Inputstream. Dit is nodig omdat je niet het gehele antwoord in 1 keer binnenkrijgt maar character voor character (het gaat wel zo snel dat het lijkt alsof het in 1 keer is). En een Inputstream is een class waarmee je een stroom informatie die vloeit uit een open connectie (HTTP) kunt redigeren. Ook deze heeft een heleboel methodes in de Java API waarmee je de 'datastream' kunt managen. We maken eerst een lege Inputstream (een field die null is) in de weerData classe en die gaan we dadelijk vullen met de data die uit de http connectie binnenkomt. Vervolgens maken we ook alvast een Stringwriter aan. Een String is in principe eenmaal te instantiëren (pool - leashes, het zit wel iets ingewikkelder in elkaar) en met een Stringwriter kunnen we iedere keer een karakter dat binnenkomt toevoegen om uiteindelijk een String te maken van het complete antwoord. Uiteraard moeten we deze classes importeren.

```
import java.io.InputStream;
import java.io.StringWriter;
```
en in de httpConnect():
```
StringWriter antwoordStringWriter = new StringWriter();
InputStream antwoord = null;
```
Laten we eens kijken wat we aan data binnenkrijgen, daar de InputStream antwoord te vullen met de data uit 'verbinding' d.m.v. de getInputStream() methode:
```
antwoord = verbinding.getInputStream();
System.out.println(antwoord.read());
```
En hij print 123. Dat lijkt raar, want de API call begint toch echt met een { van een volledig JSON antwoord. En het originele JSON antwoord lijkt ook veel langer. Gelukkig is 123 ook de ASCII code voor het { teken. En het feit dat er maar 1 teken binnen is, bewijst dat je het antwoord niet in 1 keer binnenkrijgt. Je zult karakter na karakter moeten gaan wegschrijven. We stoppen eerst het antwoord weg in een integer en deze kunnen we vervolgens wegschrijven in de Stringwriter, aangezien die ook charactercodes(getallen) accepteerd:
```
antwoord = verbinding.getInputStream();
int leesGetal = antwoord.read();
antwoordStringWriter.write(leesGetal);
System.out.println(antwoordStringWriter);
```
We blijven natuurlijk het probleem krijgen waar de rest van de karakters blijft. Laten we nog eens een antwoord.read() onder de code toevoegen:
```
antwoordStringWriter.write(antwoord.read());
System.out.println(antwoordStringWriter);
```
Nu zien we dat {" geprint wordt. M.A.W. er is een karakter binnengekomen weer en weggeschreven. Om het gehele antwoord weg te schrijven kunnen we ofwel heel vaak antwoord.read() aanroepen, maar handiger is een while loop te gebruiken die blijft doorlezen zolang er getallen binnenkomen uit de Inputstream:
```
while (leesGetal > 0) {
    System.out.println("het getal : " + leesGetal);
    leesGetal = antwoord.read();
    antwoordStringWriter.write(leesGetal);
}
System.out.println(antwoordStringWriter);

```
We zien nu dat het hele JSON response in weggeschreven in de antwoordStringWriter en we zien ook dat er een reeks aan getallen in leesgetal wordt geschreven. Gefeliciteerd, je hebt nu eigenlijk al het JSON respons binnengehaald! Nog twee dingen die we moeten doen in deze klasse: we willen de antwoordStringWriter als een String returnen naar de main methode in de MAIN class en ik wil ook dit in een try-catch block zetten, domweg om de duidelijke HTTP status fouten hier al af te vangen:

```
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

```
 String jsonAntwoord = ht.httpConnect();
 System.out.println("in de main, het antwoord : " + jsonAntwoord);
 ```
We zien dat er helemaal aan het einde van het antwoord een teken staat (￿) en die willen we er even afhalen. Dit is handig mochten we later libraries gaan gebruiken om de JSON te parsen (i.e. opsplitsen en uitlezen naar losse datapunten). En dit kunnen we doen door een zgn. substring te maken van het hele antwoord, zonder het laatste teken. Bij de substring methode gebruik geef je de begin- en eind-index getallen aan van een string en daar wordt een nieuwe string mee gemaakt. Voorbeeld:
```
String groet = "hallo"
String geknipt = groet.substring(0, 3) (LET OP! LAATSTE indexnummer is exclusief - 'tot aan')
System.out.println(geknipt) -> hal
```
Hier gaan we het laatste teken eraf knippen door de lengte van het jsonAntwoord te nemen -1 :
```
//buiten try-catch block
String antwoord = "";

//in try block
antwoord = jsonAntwoord.substring(0,(jsonAntwoord.length()-1));
```
In principe hebben we nu het antwoord en alle data al op het scherm staan. Dadelijk gaan we natuurlijk uit deze String de data halen die we interessant vinden (plaats, weertype, beschrijving, temperatuur, min. max. temperatuur, luchtdruk, etc.). Maar eerst moeten we nog de invoer van de gebruiker gaan regelen. Want nu is de stad 'hardcoded' in het URL object, en dat willen we niet. We willen keuze hebben. En die kunnen we inbouwen, en dat lijkt wel een beetje op de API key invoer in het URL object. Wat we moeten doen is zorgen dat er een plaatsnaam ingevoerd kan worden, dat deze aan de url meegegeven kan worden EN dat er bij plaatsen met spaties, de spatie vervangen wordt door een +. De eerste stap is de plaatsnaam variabel te maken.

```
//buiten try block - LET OP: de + is noodzakelijk
String plaats = "Den+Helder";

//in try block
URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + plaats + "&appid=" + apiKey);
```
Vervolgens gaan we om gebruikersinput moeten vragen en dit kunnen we doen met een zgn. Scanner.

```
import java.util.Scanner;

    String plaats = "";

    System.out.println("Voer je plaats in: ");
    Scanner invoer = new Scanner(System.in);
    String input = invoer.nextLine();
    plaats = input;

```
Dit lijkt te werken, maar wat nou als iemand bij ongeluk op enter drukt zonder een plaats in te voeren? Om dat te voorkomen heb ik een default stad ingevoerd (nl. Den Bosch) en een if statement toegevoegd om te checken of de gebruikersinput leeg is

```
String stad = "Den+Bosch";
String input = invoer.nextLine();
if (input.isEmpty()){
    input = stad;
}
plaats = input;
```
als laatste heb ik een stukje code toegevoegd om te zorgen dat de plaats altijd in lower-case is en dat de spaties vervangen worden door een plus teken. Ik merkte dat dit soms problemen gaf als dit niet het geval was.

```
plaats = input.replaceAll(" ", "+").toLowerCase();
```

# Stap 5

