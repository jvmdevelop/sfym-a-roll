package com.jvmd.sfymARollPlugin.roll;

import io.papermc.paper.event.entity.EntityPushedByEntityAttackEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public final class RollProtectionListener implements Listener {
    private final RollState rollState;

    public RollProtectionListener(RollState rollState) {
        this.rollState = rollState;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player && rollState.isInvulnerable(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityPushedByEntityAttack(EntityPushedByEntityAttackEvent event) {
        if (event.getEntity() instanceof Player player && rollState.isInvulnerable(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        rollState.end(event.getPlayer());
    }
}
