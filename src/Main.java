import java.net.URL;
import java.util.Scanner;

public class Main {

    //api key in een statische String - ik wil dat ik er vanuit een statische methode bijkan, n.l. de main methode
    static String apiKey = "b9a6bacee0a600df90ee297ace589a82";


    public static void main(String[] args) {

        //In deze string gaat de JSON
        String antwoord = "";
        String plaats = "";
        String stad = "Den+Bosch";

        System.out.println("Voer je plaats in: ");
        Scanner invoer = new Scanner(System.in);
        String input = invoer.nextLine();
        if (input.isEmpty()){
            input = stad;
        }

        plaats = input.replaceAll(" ", "+").toLowerCase();

        try {
            URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + plaats + "&appid=" + apiKey);
            System.out.println(url);
            weerData ht = new weerData(url);
            String jsonAntwoord = ht.httpConnect();

            //hier wordt het allerlaatste karakter eraf gehaald
            antwoord = jsonAntwoord.substring(0,(jsonAntwoord.length()-1));

            //nieuw printer object instantiëren
            weerDataPrinter printer = new weerDataPrinter();
            printer.getData(antwoord);

        } catch (Exception e) {
            System.out.println("Weer opvragen mislukt");
        } finally {
            System.out.println("***********>>>> Tot zover het weerbericht <<<<**************");
        }
    }
}
