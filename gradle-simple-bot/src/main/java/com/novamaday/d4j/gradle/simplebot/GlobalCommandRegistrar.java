package com.novamaday.d4j.gradle.simplebot;

import discord4j.common.JacksonResources;
import discord4j.discordjson.json.ApplicationCommandData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.rest.RestClient;
import discord4j.rest.service.ApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class GlobalCommandRegistrar {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final RestClient restClient;

    public GlobalCommandRegistrar(RestClient restClient) {
        this.restClient = restClient;
    }

    //Since this will only run once on startup, blocking is okay.
    protected void registerCommands() throws IOException {
        //Create an ObjectMapper that supports Discord4J classes
        final JacksonResources d4jMapper = JacksonResources.create();

        // Convenience variables for the sake of easier to read code below
        final ApplicationService applicationService = restClient.getApplicationService();
        final long applicationId = restClient.getApplicationId().block();

        //These are commands already registered with discord from previous runs of the bot.
        Map<String, ApplicationCommandData> discordCommands = applicationService
            .getGlobalApplicationCommands(applicationId)
            .collectMap(ApplicationCommandData::name)
            .block();

        //Get our commands json from resources as command data
        Map<String, ApplicationCommandRequest> commands = new HashMap<>();
        for (String json : getCommandsJson()) {
            ApplicationCommandRequest request = d4jMapper.getObjectMapper()
                .readValue(json, ApplicationCommandRequest.class);

            commands.put(request.name(), request); //Add to our array list

            //Check if this is a new command that has not already been registered.
            if (!discordCommands.containsKey(request.name())) {
                //Not yet created with discord, let's do it now.
                applicationService.createGlobalApplicationCommand(applicationId, request).block();

                LOGGER.info("Created global command: " + request.name());
            }
        }

        //Check if any commands have been deleted or changed.
        for (ApplicationCommandData discordCommand : discordCommands.values()) {
            long discordCommandId = Long.parseLong(discordCommand.id());

            ApplicationCommandRequest command = commands.get(discordCommand.name());

            if (command == null) {
                //Removed command.json, delete global command
                applicationService.deleteGlobalApplicationCommand(applicationId, discordCommandId).block();

                LOGGER.info("Deleted global command: " + discordCommand.name());
                continue; //Skip further processing on this command.
            }

            //Check if the command has been changed and needs to be updated.
            if (hasChanged(discordCommand, command)) {
                applicationService.modifyGlobalApplicationCommand(applicationId, discordCommandId, command).block();

                LOGGER.info("Updated global command: " + command.name());
            }
        }
    }


    private boolean hasChanged(ApplicationCommandData discordCommand, ApplicationCommandRequest command) {
        // Compare types
        if (!discordCommand.type().toOptional().orElse(1).equals(command.type().toOptional().orElse(1))) return true;

        //Check if description has changed.
        if (!discordCommand.description().equals(command.description().toOptional().orElse(""))) return true;

        //Check if default permissions have changed
        boolean discordCommandDefaultPermission = discordCommand.defaultPermission().toOptional().orElse(true);
        boolean commandDefaultPermission = command.defaultPermission().toOptional().orElse(true);

        if (discordCommandDefaultPermission != commandDefaultPermission) return true;

        //Check and return if options have changed.
        return !discordCommand.options().equals(command.options());
    }

    /* The two below methods are boilerplate that can be completely removed when using Spring Boot */

    private static List<String> getCommandsJson() throws IOException {
        //The name of the folder the commands json is in, inside our resources folder
        final String commandsFolderName = "commands/";

        //Get the folder as a resource
        URL url = GlobalCommandRegistrar.class.getClassLoader().getResource(commandsFolderName);
        Objects.requireNonNull(url, commandsFolderName + " could not be found");

        File folder;
        try {
            folder = new File(url.toURI());
        } catch (URISyntaxException e) {
            folder = new File(url.getPath());
        }

        //Get all the files inside this folder and return the contents of the files as a list of strings
        List<String> list = new ArrayList<>();
        File[] files = Objects.requireNonNull(folder.listFiles(), folder + " is not a directory");

        for (File file : files) {
            String resourceFileAsString = getResourceFileAsString(commandsFolderName + file.getName());
            list.add(resourceFileAsString);
        }
        return list;
    }

    /**
     * Gets a specific resource file as String
     * @param fileName The file path omitting "resources/"
     * @return The contents of the file as a String, otherwise throws an exception
     */
    private static String getResourceFileAsString(String fileName) throws IOException {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        try (InputStream resourceAsStream = classLoader.getResourceAsStream(fileName)) {
            if (resourceAsStream == null) return null;
            try (InputStreamReader inputStreamReader = new InputStreamReader(resourceAsStream);
                 BufferedReader reader = new BufferedReader(inputStreamReader)) {
                return reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }
        }
    }
}
