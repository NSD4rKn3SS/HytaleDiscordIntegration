package com.kozejin;

public class MessageRelay {
    private final DiscordConfig config;

    public MessageRelay(DiscordConfig config) {
        this.config = config;
    }

    public void sendToDiscord(String playerName, String message) {
        System.out.println("[Discord Integration] MessageRelay.sendToDiscord called for: " + playerName);
        DiscordBot bot = DiscordIntegration.getInstance().discordBot;
        if (bot == null) {
            System.out.println("[Discord Integration] Bot is null!");
            return;
        }
        if (!bot.isConnected()) {
            System.out.println("[Discord Integration] Bot is not connected!");
            return;
        }
        String formatted = config.getMessageFormat().getServerToDiscord()
            .replace("{player}", playerName)
            .replace("{message}", message);
        System.out.println("[Discord Integration] Sending to Discord: " + formatted);
        bot.sendMessage(formatted);
    }

    public void sendJoinMessage(String playerName) {
        DiscordBot bot = DiscordIntegration.getInstance().discordBot;
        if (bot != null && bot.isConnected()) {
            String formatted = config.getMessageFormat().getJoinMessage()
                .replace("{player}", playerName);
            bot.sendMessage(formatted);
        }
    }

    public void sendLeaveMessage(String playerName) {
        DiscordBot bot = DiscordIntegration.getInstance().discordBot;
        if (bot != null && bot.isConnected()) {
            String formatted = config.getMessageFormat().getLeaveMessage()
                .replace("{player}", playerName);
            bot.sendMessage(formatted);
        }
    }

    public void sendDeathMessage(String playerName) {
        DiscordBot bot = DiscordIntegration.getInstance().discordBot;
        if (bot != null && bot.isConnected()) {
            String formatted = config.getMessageFormat().getDeathMessage()
                .replace("{player}", playerName);
            bot.sendMessage(formatted);
        }
    }

    public void sendServerStartMessage() {
        DiscordBot bot = DiscordIntegration.getInstance().discordBot;
        if (bot != null && bot.isConnected()) {
            String formatted = config.getMessageFormat().getServerStartMessage();
            bot.sendMessage(formatted);
        }
    }

    public void sendServerStopMessage() {
        DiscordBot bot = DiscordIntegration.getInstance().discordBot;
        if (bot != null && bot.isConnected()) {
            String formatted = config.getMessageFormat().getServerStopMessage();
            bot.sendMessage(formatted);
        }
    }
}
