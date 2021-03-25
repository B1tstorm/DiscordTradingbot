package de.fh_kiel.discordtradingbot;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;

public class DemoBot {

    private static final String DiscordAuthToken = "ODIzOTkwNDA1NjgyOTU0MzAw.YFo23Q.c8rbWnHwAE2QtxJPSgD2KvsAUBw";

    public static void main(String[] args) {

        //Bei Discord über Token authentifizieren
        GatewayDiscordClient client =
                DiscordClientBuilder.create(DiscordAuthToken)
                        .build()
                        .login()
                        .block();

        //Erfolgreiche Authentifikation nach Stdout loggen
        client.getEventDispatcher().on(ReadyEvent.class)
                .subscribe(event -> {
                    final User self = event.getSelf();
                    System.out.printf("Logged in as %s#%s%n", self.getUsername(), self.getDiscriminator());
                });

        //Auf ping hören - Beispielinteraktion von https://docs.discord4j.com/basic-bot-tutorial
        client.getEventDispatcher().on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> message.getContent().equalsIgnoreCase("!ping"))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage("Moin!"))
                .subscribe();

        //Verbindung schließen
        client.onDisconnect().block();
    }
}