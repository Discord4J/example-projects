package com.novamaday.d4j.gradle.simplebot.commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import reactor.core.publisher.Mono;

public class PingCommand implements SlashCommand {
    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        //We reply to the command with "Pong!" and make sure it is ephemeral (only the command user can see it)
        return event.reply()
            .withEphemeral(true)
            .withContent("Pong!");
    }
}
