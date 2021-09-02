# Discord4J Example Projects

<a href="https://discord4j.com"><img align="right" src="https://raw.githubusercontent.com/Discord4J/discord4j-web/master/public/logo.svg?sanitize=true" width=27%></a>

[![Support Server Invite](https://img.shields.io/discord/208023865127862272.svg?color=7289da&label=Discord4J&logo=discord&style=flat-square)](https://discord.gg/d4j)

Provided are a set of very simple example projects showcasing two simple slash commands: ping-pong and greet using Discord4J `3.2.0-RC3`.

## 🔗 Quick Links

* [Github](https://github.com/Discord4J/Discord4J)
* [Javadocs](https://www.javadoc.io/doc/com.discord4j/discord4j-core)
* [Documentation](https://docs.discord4j.com)
* [Discord](https://discord.gg/d4j)

## 📦 Dependency Managers
Each project is duplicated in Maven and Gradle to show how to import Discord4J into your project with the package manager of your choice.

## 📐 Spring Boot

Discord4J is fully compatible with the Spring Boot framework, and a set of example projects are provided showcasing what is possible with Spring and Discord4J together; simplifying and/or removing boilerplate shown in the Discord4J-only projects.

## 🧾 Logging

* [Logging Docs](https://docs.discord4j.com/logging/)

Discord4j utilizes the SLF4J logging API. In this set of example projects, a simple logback configuration is used to demonstrate basic logging to console.

## 🌳 Minecraft

Discord4J can be used for a Minecraft plugin, however special care should be taken to avoid blocking on the main thread and dealing with Minecraft's old Netty & Jackson requirements.

For more information on how to use D4J for your Minecraft plugin, check the wiki [here](https://docs.discord4j.com/frequently-asked-questions/#im-getting-javalangnosuchmethoderror-ionetty-or-javalangnoclassdeffounderror-comfasterxml)
