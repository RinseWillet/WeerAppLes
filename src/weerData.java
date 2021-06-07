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
        System.out.println("Connection Response code:" + verbinding.getResponseCode());

        return "gelukt";
    }
}
