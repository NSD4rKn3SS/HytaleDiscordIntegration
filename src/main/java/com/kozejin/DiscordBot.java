package com.kozejin;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class DiscordBot extends ListenerAdapter {
    private final DiscordConfig config;
    private final BiConsumer<String, String> onDiscordMessage;
    private JDA jda;
    private TextChannel textChannel;

    public DiscordBot(DiscordConfig config, BiConsumer<String, String> onDiscordMessage) {
        this.config = config;
        this.onDiscordMessage = onDiscordMessage;
    }

    public CompletableFuture<Boolean> start() {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        if (!config.isEnabled()) {
            System.out.println("[Discord] Bot is disabled in config");
            future.complete(false);
            return future;
        }

        if ("YOUR_BOT_TOKEN_HERE".equals(config.getBotToken())) {
            System.out.println("[Discord] Please set your bot token in the config!");
            future.complete(false);
            return future;
        }

        try {
            System.out.println("[Discord] Starting Discord bot...");

            jda = JDABuilder.createDefault(config.getBotToken())
                .enableIntents(
                    GatewayIntent.GUILD_MESSAGES,
                    GatewayIntent.MESSAGE_CONTENT
                )
                .addEventListeners(this)
                .build();

            jda.awaitReady();

            textChannel = jda.getTextChannelById(config.getChannelId());
            if (textChannel == null) {
                System.out.println("[Discord] Could not find channel with ID: " + config.getChannelId());
                future.complete(false);
                return future;
            }

            System.out.println("[Discord] Bot connected successfully to channel: " + textChannel.getName());
            updatePlayerCount(0, 0);
            future.complete(true);

        } catch (Exception e) {
            System.out.println("[Discord] Failed to start bot: " + e.getMessage());
            e.printStackTrace();
            future.complete(false);
        }

        return future;
    }

    public void shutdown() {
        System.out.println("[Discord] Shutting down Discord bot...");
        if (jda != null) {
            jda.shutdown();
            jda = null;
        }
        textChannel = null;
    }

    public void sendMessage(String message) {
        if (textChannel != null) {
            textChannel.sendMessage(message).queue(
                success -> System.out.println("[Discord] Message sent: " + message),
                error -> System.out.println("[Discord] Failed to send message: " + error.getMessage())
            );
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        
        String channelId = event.getChannel().getId();
        String username = event.getAuthor().getName();
        String message = event.getMessage().getContentDisplay();

        if (channelId.equals(config.getCommandChannelId())) {
            if (message.equalsIgnoreCase("!link")) {
                handleLinkCommand(event);
                return;
            }
            
            if (message.toLowerCase().startsWith("!profile")) {
                handleProfileCommand(event, message);
                return;
            }
        }

        if (channelId.equals(config.getChannelId())) {
            onDiscordMessage.accept(username, message);
        }
    }

    private void handleLinkCommand(MessageReceivedEvent event) {
        String discordId = event.getAuthor().getId();
        String discordUsername = event.getAuthor().getName();

        LinkCodeManager linkManager = DiscordIntegration.getInstance().getLinkCodeManager();
        String code = linkManager.generateCode(discordId, discordUsername);

        event.getAuthor().openPrivateChannel().queue(privateChannel -> {
            privateChannel.sendMessage(
                "**Account Linking**\n" +
                "Your link code: `" + code + "`\n" +
                "Use `/link " + code + "` in-game to link your account.\n" +
                "Code expires in 5 minutes."
            ).queue(
                success -> {
                    event.getChannel().sendMessage("Check your DMs for your link code!").queue();
                    System.out.println("[Discord Integration] Generated link code for " + discordUsername + ": " + code);
                },
                error -> {
                    event.getChannel().sendMessage("Could not send you a DM. Please enable DMs from server members.").queue();
                    System.out.println("[Discord Integration] Failed to DM link code to " + discordUsername);
                }
            );
        });
    }
    
    private void handleProfileCommand(MessageReceivedEvent event, String message) {
        String[] parts = message.split("\\s+");
        String discordId = event.getAuthor().getId();
        
        PlayerDataStorage storage = DiscordIntegration.getInstance().getPlayerDataStorage();
        PlayerData playerData = null;
        String targetUsername = null;
        
        if (parts.length > 1) {
            targetUsername = parts[1];
            for (PlayerData data : storage.getAllPlayers().values()) {
                if (data.getUsername().equalsIgnoreCase(targetUsername)) {
                    playerData = data;
                    break;
                }
            }
            
            if (playerData == null) {
                event.getChannel().sendMessage("Player `" + targetUsername + "` not found!").queue();
                return;
            }
        } else {
            playerData = storage.getPlayerByDiscordId(discordId);
            
            if (playerData == null) {
                event.getChannel().sendMessage(
                    "Your Discord account is not linked!\n" +
                    "Use `!link` to get a link code, then use `/link <code>` in-game."
                ).queue();
                return;
            }
            targetUsername = playerData.getUsername();
        }
        
        String discordTag = playerData.getDiscordId() != null ? "<@" + playerData.getDiscordId() + ">" : "Not linked";
        long firstLogin = playerData.getFirstLoginTime();
        String firstLoginDate = new java.text.SimpleDateFormat("MMM dd, yyyy").format(new java.util.Date(firstLogin));
        
        event.getChannel().sendMessage(
            "**Player Profile: " + targetUsername + "**\n" +
            "**Total Playtime:** " + playerData.getFormattedPlayTime() + "\n" +
            "**First Login:** " + firstLoginDate + "\n" +
            "**Discord:** " + discordTag
        ).queue();
        
        System.out.println("[Discord Integration] Profile requested for: " + targetUsername);
    }

    public boolean isConnected() {
        return jda != null && textChannel != null;
    }

    public void updatePlayerCount(int online, int max) {
        if (jda != null) {
            String status = max > 0 ? online + "/" + max + " players online" : online + " players online";
            jda.getPresence().setActivity(Activity.playing(status));
        }
    }
}
