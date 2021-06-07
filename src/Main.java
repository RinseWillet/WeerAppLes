import java.net.URL;

public class Main {

    //api key in een statische String - ik wil dat ik er vanuit een statische methode bijkan, n.l. de main methode
    static String apiKey = "b9a6bacee0a600df90ee297ace589a82";


    public static void main(String[] args) {


        //stap 1 krijg een API key. Laten zien hoe de url/api eigenlijk werkt.
        // Ik wil deze url gaan aanroepen vanuit Java en daarvoor moeten we eerst een
        // URL object aanmaken, waarin het webadres komt te staan (laat docs zien, kunnen nog veel meer opties meegegeven worden)
        // dit is dus geen variabele zoals je bijv. bij JavaScript veel ziet
        // URL url = new URL ("https://api.openweathermap.org/data/2.5/weather?q=Den+Bosch&appid=" + apiKey); -> Run
        // we zien dat dat niet werkt, omdat er een Exception wordt genoemd die niet declared is - dit is een fout
        // die we moeten afhandelen met een try en catch block. En met een try en catchblock zeg je eigenlijk wat er
        // gedaan moet worden als er een Exception plaatsvindt. Finally gebeurd altijd aan het eind

        try {
            URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=Den+Bosch&appid=" + apiKey);
            System.out.println(url);
        } catch (Exception e) {
            System.out.println("Weer opvragen mislukt");
        } finally {
            System.out.println("***********>>>> Tot zover het weerbericht <<<<**************");
        }
    }
}
