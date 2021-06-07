import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.net.HttpURLConnection;

public class weerData{

    //field voor het URL object
    private URL httpurl = null;

    //constructor
    public weerData(URL aanmaken) {
        httpurl = aanmaken;
        System.out.println("gelukt : " + httpurl);
    }

    //methode om API aan te roepen
    public String httpConnect() throws Exception {

        //in de methode boodschap
        System.out.println("HTTP Request starten...");

        //Start een nieuwe HttpURLConnection (uit java.net.*) waarbij je het URL object cast als een HttpURLConnection en de openConnection methode gebruikt van het URL object
        HttpURLConnection verbinding = (HttpURLConnection) httpurl.openConnection() ;

        //Connectie krijgen met de connect() methode uit het HttpURLConnection object 'verbinding'
        verbinding.connect();

        // Antwoord van de connectie krijgen met de getResponseCode() methode
//        System.out.println("Connection Response code:" + verbinding.getResponseCode());

        //Uitlezen HTTP antwoord.
        StringWriter antwoordStringWriter = new StringWriter();
        InputStream antwoord = null;

        try {
            //gebruik de methode getInputStream die in het HttpURLConnection object 'verbinding' zit om de leash 'antwoord' van InputStream te koppelen. Een Inputstream is een stroom informatie die vloeit uit een open connectie (HTTP)
            antwoord = verbinding.getInputStream();

            // je gebruikt nu de read() methode uit de InputStream 'antwoord' om een integer te vullen met de data die binnenkomt. De getallen in de integers staan gelijk aan ASCII codes voor tekens
            int leesGetal = antwoord.read();

            //en iedere integer die binnenkomt gaan we wegschrijven in het StringWriter object 'antwoordStringWriter'.
            // Een Stringwriter slaat een characterstroom op in een string buffer, die vervolgens gebruikt kan worden om een string te bouwen
            // als je de write() methode gebruikt met een integer bijv. write(123) dan wordt een character met ASCII characternummer 123 weggeschreven (dat is "{" voor 123)
            antwoordStringWriter.write(leesGetal);
            System.out.println(antwoordStringWriter);

            //zolang er een getal groter dan nul uit de inputstream in de integer 'rint' gezet kan worden, blijf hetvolgende doen
            while (leesGetal > 0) {
                //hier laat je het getal dat binnenkomt uit de printen
                System.out.println("het getal : " + leesGetal);

                //hier laat je de inputstream met methode read() het character-integer uitlezen en in de integer rint zetten
                leesGetal = antwoord.read();

                // het lees getal uit en schrijf als character weg in de antwoordStringWriter
                antwoordStringWriter.write(leesGetal);
            }
        } catch (Exception httpfout) {
            //print de responsecode, als die niet 200 is (= alles okay), dan een IOException throwen
            System.out.println("HTTP ResponseCode : " + verbinding.getResponseCode() + " - plaats niet gevonden");
            if (verbinding.getResponseCode() != 200) {
                throw new IOException(verbinding.getResponseMessage());
            }
        }

        //return een String door de antwoordStringWriter met de methode toString() om te zetten tot String
        return antwoordStringWriter.toString();
    }
}
