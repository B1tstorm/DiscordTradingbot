package de.fh_kiel.discordtradingbot.Interaction;

/**
 * alle Nachricht im Channel werden bei uns in ein EventItem gewandelt
 * der typ dieses EventItem beschreibt, worum es in dieser Nachricht geht
 */
public enum EventType {
    BUY_OFFER,
    BUY_ACCEPT,
    BUY_CONFIRM,

    SELL_OFFER,
    SELL_ACCEPT,
    SELL_CONFIRM,

    ACCEPT,

    AUCTION_START,
    AUCTION_BID,
    AUCTION_WON,
    AUCTION_CLOSE,

    ZULU_BUY,
    ZULU_CONFIRM,
    ZULU_DENY,

    VISUALIZE,
    WALLET,
    INVENTORY,

    EVALUATION,
    HISTORY,

    HELP;
}
