import java.net.URL;

public class Main {

    //api key in een statische String - ik wil dat ik er vanuit een statische methode bijkan, n.l. de main methode
    static String apiKey = "b9a6bacee0a600df90ee297ace589a82";


    public static void main(String[] args) {

        //stap 2 Ik kan de complete code in de Main class gaan schrijven, maar dan wordt de code erg lang en
        // bovendien wil ik bepaalde taken concentreren in aparte objecten die ik wil aanmaken. Ik wil daarom een class aanmaken
        // waarmee ik een api kan aanroepen, door een URL object mee te geven als methode. Ik maak de file weerData.java aan.
        // Daarin maken we een private field voor een URL en die zetten we op null. Private is om hem af te schermen van buiten de class.
        // Dit field is nu leeg en kunnen we vullen met ons URL object zodra we een weerData object aanmaken.
        // Om dit te doen hebben we enkel nog een constructor nodig waarin we een URL object kunnen meegeven

        try {
            URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=Den+Helder&appid=" + apiKey);
            System.out.println(url);
            weerData ht = new weerData(url);
            ht.httpConnect();
        } catch (Exception e) {
            System.out.println("Weer opvragen mislukt");
        } finally {
            System.out.println("***********>>>> Tot zover het weerbericht <<<<**************");
        }
    }
}
