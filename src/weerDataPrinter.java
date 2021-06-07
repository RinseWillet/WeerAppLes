public class weerDataPrinter {

    public void getData(String antwoord) {
        //plaatsnaam
        String plaatsRes = antwoord.substring(antwoord.indexOf("name")+7, antwoord.indexOf("\",\"",antwoord.indexOf("name")));

        //weertype
        String weerTypeRes = antwoord.substring(antwoord.indexOf("main")+7, antwoord.indexOf("\",\"",antwoord.indexOf("main")));

        //beschrijving
        String beschrijvingRes = antwoord.substring(antwoord.indexOf("description")+14, antwoord.indexOf("\",\"",antwoord.indexOf("description")));

        //temperatuur in Celsius
        double tempKRes = Double.valueOf(antwoord.substring(antwoord.indexOf("temp")+6, antwoord.indexOf(",\"",antwoord.indexOf("temp"))));
        double tempCRes = tempKRes - 273.15;

        // min. temperatuur in Celsius extra
        double tempKminRes = Double.valueOf(antwoord.substring(antwoord.indexOf("temp_min")+10, antwoord.indexOf(",\"",antwoord.indexOf("temp_min"))));
        double tempCminRes = tempKminRes - 273.15;

        // max. temperatuur in Celsius extra
        double tempKmaxRes = Double.valueOf(antwoord.substring(antwoord.indexOf("temp_max")+10, antwoord.indexOf(",\"",antwoord.indexOf("temp_max"))));
        double tempCmaxRes = tempKmaxRes - 273.15;

        // luchtdruk in hPa
        int luchtdrukRes = Integer.valueOf(antwoord.substring(antwoord.indexOf("pressure")+10, antwoord.indexOf(",\"",antwoord.indexOf("pressure"))));

        //luchtvochtigheid
        System.out.println(Character.isDigit('1'));
        System.out.println(Character.isDigit('q'));
        System.out.println(antwoord.charAt(antwoord.indexOf("humidity")+10));
        System.out.println(Character.isDigit(antwoord.charAt(antwoord.indexOf("humidity")+12)));

        int vochtigheidRes = 0;
        if(Character.isDigit(antwoord.charAt((antwoord.indexOf("humidity")+12)))) {
            vochtigheidRes = Integer.valueOf(antwoord.substring(antwoord.indexOf("humidity") + 10, antwoord.indexOf("humidity") + 13));
        } else if (Character.isDigit(antwoord.charAt((antwoord.indexOf("humidity") + 11)))) {
            vochtigheidRes = Integer.valueOf(antwoord.substring(antwoord.indexOf("humidity") + 10, antwoord.indexOf("humidity") + 12));
        } else {
            vochtigheidRes = Integer.valueOf(antwoord.substring(antwoord.indexOf("humidity") + 10, antwoord.indexOf("humidity") + 11));//
        }


        // printen
        System.out.printf("Plaats : " + plaatsRes + " - weertype : " + weerTypeRes + " - beschrijving : " + beschrijvingRes);
        System.out.printf("\nTemperatuur : %.1f Min. : %.1f Max. : %.1f graden Celsius", tempCRes, tempCminRes, tempCmaxRes);
        System.out.printf("\nLuchtdruk : %4d hPa Luchtvochtigheid : %3d procent", luchtdrukRes, vochtigheidRes);

    }
}

