package com.kozejin;

import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.damage.DeathComponent;
import com.hypixel.hytale.server.core.modules.entity.damage.DeathSystems;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;

public class PlayerDeathSystem extends DeathSystems.OnDeathSystem {
    
    @Nonnull
    @Override
    public Query<EntityStore> getQuery() {
        return Query.and(Player.getComponentType());
    }
    
    @Override
    public void onComponentAdded(
            @Nonnull Ref ref,
            @Nonnull DeathComponent component,
            @Nonnull Store store,
            @Nonnull CommandBuffer commandBuffer) {
        
        Player playerComponent = (Player) store.getComponent(ref, Player.getComponentType());
        
        if (playerComponent != null) {
            String playerName = playerComponent.getDisplayName();
            System.out.println("[Discord Integration] Player died: " + playerName);
            
            // Notify Discord via the plugin instance
            DiscordIntegration plugin = DiscordIntegration.getInstance();
            if (plugin != null) {
                plugin.notifyPlayerDeath(playerName);
            }
        }
    }
}
