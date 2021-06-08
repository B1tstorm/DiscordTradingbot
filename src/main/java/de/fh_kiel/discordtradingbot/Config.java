package de.fh_kiel.discordtradingbot;

import java.util.ArrayList;

public class Config {
    public static final String token =  "ODQ1NDEwMTQ2OTEzNzQ3MDM0.YKgjjQ.VJDUA3YOxYR6yKXQRFShP293Vm4";


    public static String SEGID = null;

    public static final double[] staticLetterValues =
            {6.51, 1.89, 3.06, 5.08, 17.40, 1.66, 3.01, 4.76, 7.55, 0.27, 1.21, 3.44, 2.53, 9.78, 2.51, 0.79, 0.02, 7.00, 7.27, 6.15, 4.35, 0.67, 1.89, 0.03, 0.04, 1.13};

    public static String getToken() {
        return token;
    }

    public static int charToIndex(char c) {
        return (int) c - 65;
    }

    public static char indexToChar(int i){
        return (char) (i + 65);
    }
}
