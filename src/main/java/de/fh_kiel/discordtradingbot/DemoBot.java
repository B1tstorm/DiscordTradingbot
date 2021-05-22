package de.fh_kiel.discordtradingbot;

import de.fh_kiel.discordtradingbot.Interaction.ChannelInteracter;

public class DemoBot {
    public static void main(String[] args) {
        ChannelInteracter interacter = new ChannelInteracter(args[0]);
        interacter.listenToChannel();

        //TODO: Nachricht senden, wenn eingeloggt.
        // Wo kann ich jetzt mit dem Bot interagieren?
    }
}