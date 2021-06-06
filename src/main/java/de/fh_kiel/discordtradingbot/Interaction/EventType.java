package de.fh_kiel.discordtradingbot.Interaction;

public enum EventType {
    BUY_OFFER,
    BUY_ACCEPT,
    BUY_CONFIRM,

    SELL_OFFER,
    SELL_ACCEPT,
    SELL_CONFIRM,


    AUCTION_START,
    AUCTION_BID,
    AUCTION_WON,
    AUCTION_CLOSE,

    HELP;
}
