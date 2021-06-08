package de.fh_kiel.discordtradingbot.Analysis;

import de.fh_kiel.discordtradingbot.Holdings.Letter;

import java.util.ArrayList;
import java.util.List;

public interface LetterPublisher {
    List<LetterListener> subscribers = new ArrayList<>();
    
    public void registerSubscriber(LetterListener s);

    public void removeSubscriber(LetterListener s);

    public void notifySubscribers(Letter l);
}
