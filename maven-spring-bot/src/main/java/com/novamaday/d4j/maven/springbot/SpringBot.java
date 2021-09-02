package com.novamaday.d4j.maven.springbot;

import com.novamaday.d4j.maven.springbot.listeners.SlashCommandListener;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.domain.interaction.SlashCommandEvent;
import discord4j.rest.RestClient;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class SpringBot {
    public static void main(String[] args) {
        //Start spring application
        ApplicationContext springContext = new SpringApplicationBuilder(SpringBot.class)
            .build()
            .run(args);

        //Login
        DiscordClientBuilder.create(System.getenv("BOT_TOKEN")).build()
            .withGateway(gatewayClient -> {
                SlashCommandListener slashCommandListener = new SlashCommandListener(springContext);

                Mono<Void> onSlashCommandMono = gatewayClient
                    .on(SlashCommandEvent.class, slashCommandListener::handle)
                    .then();

                return Mono.when(onSlashCommandMono);
            }).block();
    }


    @Bean
    public RestClient discordRestClient() {
        return RestClient.create(System.getenv("BOT_TOKEN"));
    }
}
