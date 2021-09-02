package com.novamaday.d4j.maven.simplebot.commands;

import discord4j.core.event.domain.interaction.SlashCommandEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import reactor.core.publisher.Mono;

public class GreetCommand implements SlashCommand {
    @Override
    public String getName() {
        return "greet";
    }

    @Override
    public Mono<Void> handle(SlashCommandEvent event) {
        /*
        Since slash command options are optional according to discal, we will wrap it into the following function
        that gets the value of our option as a Mono<String>, so that we may use it later on without
        needing to call .get() several times on our optional values.

        In this case, there is no fear the mono will return empty as this is marked "required: true" in our json.
         */
        Mono<String> nameMono = Mono.justOrEmpty(event.getOption("name")
            .flatMap(ApplicationCommandInteractionOption::getValue)
        ).map(ApplicationCommandInteractionOptionValue::asString);


        return nameMono.flatMap(name -> {
            //Reply to the slash command, with the name the user supplied
            return event.reply()
                .withEphemeral(true)
                .withContent("Hello, " + name);
        });
    }
}
