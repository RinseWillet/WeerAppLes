public class weerDataPrinter {

    public void getData(String antwoord) {
        //plaatsnaam
        String plaatsRes = antwoord.substring(antwoord.indexOf("name")+7, antwoord.indexOf("\",\"",antwoord.indexOf("name")));

        //weertype
//        String weerTypeRes = antwoord.substring(antwoord.indexOf("main")+7, antwoord.indexOf("\",\"",antwoord.indexOf("main")));

        //beschrijving
        String beschrijvingRes = antwoord.substring(antwoord.indexOf("description")+14, antwoord.indexOf("\",\"",antwoord.indexOf("description")));

        //temperatuur in Celsius
        double tempCRes = Double.valueOf(antwoord.substring(antwoord.indexOf("temp")+6, antwoord.indexOf(",\"",antwoord.indexOf("temp"))));

        // min. temperatuur in Celsius extra
        double tempCminRes = Double.valueOf(antwoord.substring(antwoord.indexOf("temp_min")+10, antwoord.indexOf(",\"",antwoord.indexOf("temp_min"))));

        // max. temperatuur in Celsius extra
        double tempCmaxRes = Double.valueOf(antwoord.substring(antwoord.indexOf("temp_max")+10, antwoord.indexOf(",\"",antwoord.indexOf("temp_max"))));

        // luchtdruk in hPa
        int luchtdrukRes = Integer.valueOf(antwoord.substring(antwoord.indexOf("pressure")+10, antwoord.indexOf(",\"",antwoord.indexOf("pressure"))));

        //luchtvochtigheid
        int vochtigheidRes = 0;
        if(Character.isDigit(antwoord.charAt((antwoord.indexOf("humidity")+12)))) {
            vochtigheidRes = Integer.valueOf(antwoord.substring(antwoord.indexOf("humidity") + 10, antwoord.indexOf("humidity") + 13));
        } else if (Character.isDigit(antwoord.charAt((antwoord.indexOf("humidity") + 11)))) {
            vochtigheidRes = Integer.valueOf(antwoord.substring(antwoord.indexOf("humidity") + 10, antwoord.indexOf("humidity") + 12));
        } else {
            vochtigheidRes = Integer.valueOf(antwoord.substring(antwoord.indexOf("humidity") + 10, antwoord.indexOf("humidity") + 11));//
        }

        // windsnelheid in m/s - dit is extra
        double windSpeedRes = Double.valueOf(antwoord.substring(antwoord.indexOf("speed")+7, antwoord.indexOf(",\"deg\"")));

        //windrichting (van graden naar N-Z-O-W) - dit is extra
        int richtingRes = 0;
        if (Character.isDigit(antwoord.charAt((antwoord.indexOf("deg") + 7)))) {
            richtingRes = Integer.valueOf(antwoord.substring(antwoord.indexOf("deg") + 5, antwoord.indexOf("deg") + 8));
        } else if (Character.isDigit(antwoord.charAt((antwoord.indexOf("deg") + 6))))  {
            richtingRes = Integer.valueOf(antwoord.substring(antwoord.indexOf("deg") + 5, antwoord.indexOf("deg") + 7));
        } else {
            richtingRes = Integer.valueOf(antwoord.substring(antwoord.indexOf("deg") + 5, antwoord.indexOf("deg") + 6));
        }

        // van graden naar N O Z W - dit is een extra
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

        // printen
        System.out.printf("Plaats : " + plaatsRes + " - Weertype : " + beschrijvingRes);
        System.out.printf("\nTemperatuur : %.1f - Min. : %.1f  - Max. : %.1f graden Celsius", tempCRes, tempCminRes, tempCmaxRes);
        System.out.printf("\nLuchtdruk : %4d hPa - Luchtvochtigheid : %3d procent", luchtdrukRes, vochtigheidRes);
        System.out.printf("\nWind : %s %.1f m/s\n", kompasWaarde, windSpeedRes);
    }
}