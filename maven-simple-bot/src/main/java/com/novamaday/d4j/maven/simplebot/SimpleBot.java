package com.novamaday.d4j.maven.simplebot;

import com.novamaday.d4j.maven.simplebot.listeners.SlashCommandListener;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.SlashCommandEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleBot {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleBot.class);

    public static void main(String[] args) {
        //Creates the gateway client and connects to the gateway
        final GatewayDiscordClient client = DiscordClientBuilder.create(System.getenv("BOT_TOKEN")).build()
            .login()
            .block();

        //Call our code to handle creating/deleting/editing our global slash commands.
        try {
            new GlobalCommandRegistrar(client.getRestClient()).registerCommands();
        } catch (Exception e) {
            LOGGER.error("Error trying to register global slash commands", e);
        }

        //Register our slash command listener
        client.on(SlashCommandEvent.class, SlashCommandListener::handle)
            .then(client.onDisconnect())
            .block(); // We use .block() as there is not another non-daemon thread and the jvm would close otherwise.
    }
}
