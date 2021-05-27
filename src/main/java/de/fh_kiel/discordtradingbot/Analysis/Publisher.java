package de.fh_kiel.discordtradingbot.Analysis;

import de.fh_kiel.discordtradingbot.Holdings.Letter;

public interface Publisher {
    public void registerSubscriber(Subscriber s);
    public void removeSubscriber(Subscriber s);
    public void notifySubscribers(Letter l);
}
