package de.fh_kiel.discordtradingbot.Interaction;

public class LogItem {

    // !SEG auction start 12 A 10
    // !SEG auction bid 12 11
    // !SEG auction bid 12 901251 11
    // !SEG auction won 12 901251 345
    // !SEG auction closed 12


    private int logNr;  // chronological item number
    private boolean isActive; // if State == START || BID ist es active bei CLOSE || WON is inactive
    private String from; // oder sellerID?
    private AuctionState auctionState;
    private int auctionId;
    private int traderID; // hier lieber String, falls Name ?
    private String product; // Oder char?
    private int currentPrice; // reicht es den aktuellen Preis zu speichern?


    /*
     LogItem = {
        logNr = 1;
        from = !SEG
        AuctionState = START
        auctionId = 123145
        traderId = 123414 / Tom Calvin Haak?
        product = "A"
        currentPrice = 12
        isActive = true

     }
     * */


}
