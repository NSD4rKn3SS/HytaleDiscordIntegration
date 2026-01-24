package com.kozejin;

public class DiscordConfig {
    private String botToken = "Bot Token Here";
    private String channelId = "111111111111111";
    private String commandChannelId = "222222222222222";
    private String adminRoleId = "333333333333333";
    private boolean allowOtherBotMessages = false;
    private boolean enabled = false;
    private boolean enableDeathMessages = true;
    private String chatTagText = "Linked";
    private boolean showChatTag = true;
    private boolean enableInGameChat = true;
    private boolean showPlayerCountInTopic = false;
    private String topicPlayerCountFormat = "Players online: {online}";
    private ChatTagColors chatTagColors = new ChatTagColors();
    private MessageFormat messageFormat = new MessageFormat();

    public static class ChatTagColors {
        private String bracketColor = "#808080";
        private String tagColor = "#5865F2";
        private String usernameColor = "#00FFFF";
        private String messageColor = "#FFFFFF";

        public String getBracketColor() { return bracketColor; }
        public String getTagColor() { return tagColor; }
        public String getUsernameColor() { return usernameColor; }
        public String getMessageColor() { return messageColor; }
    }

    public static class MessageFormat {
        private String serverToDiscord = "**{player}**: {message}";
        private String discordToServer = "[Discord] <{user}> {message}";
        private String joinMessage = "**{player}** joined the server";
        private String leaveMessage = "**{player}** left the server";
        private String deathMessage = "☠️ **{player}** was killed by {cause}";
        private String serverStartMessage = "🟢 **Server is now online!**";
        private String serverStopMessage = "🔴 **Server is shutting down...**";

        public String getServerToDiscord() { return serverToDiscord; }
        public String getDiscordToServer() { return discordToServer; }
        public String getJoinMessage() { return joinMessage; }
        public String getLeaveMessage() { return leaveMessage; }
        public String getDeathMessage() { return deathMessage; }
        public String getServerStartMessage() { return serverStartMessage; }
        public String getServerStopMessage() { return serverStopMessage; }
    }

    public String getBotToken() { return botToken; }
    public void setBotToken(String botToken) { this.botToken = botToken; }
    
    public String getChannelId() { return channelId; }
    public void setChannelId(String channelId) { this.channelId = channelId; }
    
    public String getCommandChannelId() { return commandChannelId; }
    public void setCommandChannelId(String commandChannelId) { this.commandChannelId = commandChannelId; }
    
    public String getAdminRoleId() { return adminRoleId; }
    public void setAdminRoleId(String adminRoleId) { this.adminRoleId = adminRoleId; }

    public boolean isAllowOtherBotMessages() { return allowOtherBotMessages; }
    public void setAllowOtherBotMessages(boolean allowOtherBotMessages) { this.allowOtherBotMessages = allowOtherBotMessages; }
    
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public boolean isEnableDeathMessages() { return enableDeathMessages; }
    public void setEnableDeathMessages(boolean enableDeathMessages) { this.enableDeathMessages = enableDeathMessages; }
    
    public String getChatTagText() { return chatTagText; }
    public void setChatTagText(String chatTagText) { this.chatTagText = chatTagText; }
    
    public boolean isShowChatTag() { return showChatTag; }
    public void setShowChatTag(boolean showChatTag) { this.showChatTag = showChatTag; }
    
    public boolean isEnableInGameChat() { return enableInGameChat; }
    public void setEnableInGameChat(boolean enableInGameChat) { this.enableInGameChat = enableInGameChat; }

    public boolean isShowPlayerCountInTopic() { return showPlayerCountInTopic; }
    public void setShowPlayerCountInTopic(boolean showPlayerCountInTopic) {
        this.showPlayerCountInTopic = showPlayerCountInTopic;
    }

    public String getTopicPlayerCountFormat() { return topicPlayerCountFormat; }
    public void setTopicPlayerCountFormat(String topicPlayerCountFormat) {
        this.topicPlayerCountFormat = topicPlayerCountFormat;
    }

    public ChatTagColors getChatTagColors() { return chatTagColors; }
    public MessageFormat getMessageFormat() { return messageFormat; }
}
