public class weerDataPrinter {

    public void getData(String antwoord) {
        System.out.println("printen maar");
        System.out.println(antwoord);
        String voorbeeld = antwoord.substring(67,72);
        System.out.println(voorbeeld);
        int voorbeeld2 = antwoord.indexOf("name");
        System.out.println(voorbeeld2);
        String voorbeeld3 = antwoord.substring(antwoord.indexOf("name"), antwoord.indexOf("name")+18);
        System.out.println(voorbeeld3);
        String voorbeeld4 = antwoord.substring(antwoord.indexOf("name")+7, antwoord.indexOf("\",\"",antwoord.indexOf("name")));
        System.out.println(voorbeeld4);

        //plaatsnaam
        String plaatsRes = antwoord.substring(antwoord.indexOf("name")+7, antwoord.indexOf("\",\"",antwoord.indexOf("name")));

        //weertype
        String weerTypeRes = antwoord.substring(antwoord.indexOf("main")+7, antwoord.indexOf("\",\"",antwoord.indexOf("main")));

        //beschrijving
        String beschrijvingRes = antwoord.substring(antwoord.indexOf("description")+14, antwoord.indexOf("\",\"",antwoord.indexOf("description")));

        System.out.println("Plaats : " + plaatsRes + " - Weer : " + weerTypeRes + " - Beschrijving : " + beschrijvingRes);
    }
}

