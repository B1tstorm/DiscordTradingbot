package de.fh_kiel.discordtradingbot.Analysis;

public interface Publisher {
    public void registerSubscriber(Subscriber s);
    public void removeSubscriber(Subscriber s);
    public void notifyObservers();
}
