# DiscordIntegration

Discord Integration offers a two way chat bridge between your Hytale server and Discord server. This mod also offers you the ability to link your in-game account to the discord bot.

## Requirements

- Java 25+
- Hytale Server API (`HytaleServer.jar`)

## Extensions in this fork

Using this API list: https://rentry.co/gykiza2m

and this guide: https://hytalemodding.dev/en/docs/guides/plugin/setting-up-env

PS.: I did have to use AI help, because I'm merely a webdev and have only very basic Java experience.

### Additions:
 - Player death relay
 - Server online and shutdown relay
 - Update the config file with newly added or missing parameter options while keeping already set variables
 - Updating the Discord Channel topic automatically with current player count, it can also be disabled
 - Death messages can now be turned off via config, (requested by: https://github.com/mauzao9)
 - Death messages also contain cause of death
 - Fixed permission requirement for using configuration commands
 - Account linking can now also be called from the messaging channel and not just the command channel for convenience
 - **Webhook Integration**: [optional, toggleable feature]
   - (For transparency, I've used another great plugin as reference to implement this functionality https://github.com/Diversion98/discordbridgeHytale/)
   - Bot can now relay messages as a player
   - Uses Discord profile pictures for linked accounts
   - Configurable server name and avatar for system messages
   - Smart avatar caching to respect Discord rate limits