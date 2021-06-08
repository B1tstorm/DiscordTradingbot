package de.fh_kiel.discordtradingbot.Analysis;

import de.fh_kiel.discordtradingbot.Holdings.Letter;
import de.fh_kiel.discordtradingbot.Interaction.EventListener;

import java.util.ArrayList;
import java.util.List;

public interface Publisher {
    List<Subscriber> subscribers = new ArrayList<>();
    
    public void registerSubscriber(Subscriber s);

    public void removeSubscriber(Subscriber s);

    public void notifySubscribers(Letter l);
}
