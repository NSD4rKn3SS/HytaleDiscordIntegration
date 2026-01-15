package com.kozejin;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;

public class LinkCommand extends AbstractPlayerCommand {

    public LinkCommand() {
        super("link", "Link your Discord account to your in-game account", false);
        this.setAllowsExtraArguments(true);
    }
    
    @Override
    protected boolean canGeneratePermission() {
        return false;
    }

    @Override
    protected void execute(
            @Nonnull CommandContext context,
            @Nonnull Store<EntityStore> store,
            @Nonnull Ref<EntityStore> ref,
            @Nonnull PlayerRef player,
            @Nonnull World world
    ) {
        String input = context.getInputString().trim();
        String[] parts = input.split("\\s+");
        
        if (parts.length < 2) {
            player.sendMessage(Message.raw("Usage: /link <code>"));
            player.sendMessage(Message.raw("Get your code by typing !link in Discord!"));
            return;
        }

        String code = parts[1];
        DiscordIntegration plugin = DiscordIntegration.getInstance();
        LinkCodeManager.LinkRequest request = plugin.getLinkCodeManager().verifyCode(code, player.getUuid(), player.getUsername());

        if (request == null) {
            player.sendMessage(Message.raw("Invalid or expired link code!"));
            player.sendMessage(Message.raw("Codes expire after 5 minutes. Get a new code from Discord."));
            return;
        }

        PlayerData playerData = plugin.getPlayerDataStorage().getPlayerData(player.getUuid());
        if (playerData == null) {
            player.sendMessage(Message.raw("Error: Player data not found!"));
            return;
        }

        if (playerData.getDiscordId() != null) {
            player.sendMessage(Message.raw("Your account is already linked to Discord!"));
            player.sendMessage(Message.raw("Linked to: " + request.discordUsername));
            return;
        }

        playerData.setDiscordId(request.discordId);
        plugin.getPlayerDataStorage().updatePlayerData(player.getUuid(), playerData);
        plugin.getPlayerDataStorage().saveAllPlayers();

        player.sendMessage(Message.raw("Successfully linked your account to Discord!"));
        player.sendMessage(Message.raw("Discord: " + request.discordUsername));

        plugin.notifyDiscordLink(request.discordId, player.getUsername(), true);

        System.out.println("[Discord Integration] " + player.getUsername() + " linked to Discord: " + request.discordUsername);
    }
}
