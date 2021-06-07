package de.fh_kiel.discordtradingbot;

public class Config {
    private static final String token =  "ODQ1NDEwMTQ2OTEzNzQ3MDM0.YKgjjQ.VJDUA3YOxYR6yKXQRFShP293Vm4";

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
