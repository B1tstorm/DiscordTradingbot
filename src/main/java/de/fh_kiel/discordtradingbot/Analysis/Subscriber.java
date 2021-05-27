package de.fh_kiel.discordtradingbot.Analysis;

import de.fh_kiel.discordtradingbot.Holdings.Letter;

public interface Subscriber {
    public void update(Letter l);
}
