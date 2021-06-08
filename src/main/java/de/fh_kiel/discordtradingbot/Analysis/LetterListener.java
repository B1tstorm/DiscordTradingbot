package de.fh_kiel.discordtradingbot.Analysis;

import de.fh_kiel.discordtradingbot.Holdings.Letter;
import de.fh_kiel.discordtradingbot.Interaction.EventType;

public interface LetterListener {
    public void update(Letter l, EventType source);
}
