package com.jvmd.sfymARollPlugin.roll;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public final class RollState {
    private static final String ROLL_NO_COLLISION_TEAM = "sfym_roll_noclip";

    private final Set<UUID> invulnerablePlayers = ConcurrentHashMap.newKeySet();
    private final Map<UUID, Boolean> previousCollidable = new ConcurrentHashMap<>();
    private final Map<UUID, Team> previousTeams = new ConcurrentHashMap<>();

    public void begin(Player player, Plugin plugin, int ticks) {
        UUID playerId = player.getUniqueId();
        invulnerablePlayers.add(playerId);
        previousCollidable.putIfAbsent(playerId, player.isCollidable());
        player.setCollidable(false);
        moveToNoCollisionTeam(player);
        player.setNoDamageTicks(Math.max(player.getNoDamageTicks(), ticks));

        Bukkit.getScheduler().runTaskLater(plugin, () -> end(player), ticks);
    }

    public boolean isInvulnerable(Player player) {
        return invulnerablePlayers.contains(player.getUniqueId());
    }

    public void end(Player player) {
        UUID playerId = player.getUniqueId();
        invulnerablePlayers.remove(playerId);
        Boolean collidable = previousCollidable.remove(playerId);
        if (collidable != null && player.isOnline()) {
            player.setCollidable(collidable);
        }
        restoreTeam(player);
    }

    private void moveToNoCollisionTeam(Player player) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team noCollisionTeam = scoreboard.getTeam(ROLL_NO_COLLISION_TEAM);
        if (noCollisionTeam == null) {
            noCollisionTeam = scoreboard.registerNewTeam(ROLL_NO_COLLISION_TEAM);
            noCollisionTeam.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        }

        UUID playerId = player.getUniqueId();
        Team previousTeam = scoreboard.getEntityTeam(player);
        if (previousTeam != null && previousTeam != noCollisionTeam) {
            previousTeams.putIfAbsent(playerId, previousTeam);
        }
        noCollisionTeam.addEntity(player);
    }

    private void restoreTeam(Player player) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team noCollisionTeam = scoreboard.getTeam(ROLL_NO_COLLISION_TEAM);
        if (noCollisionTeam != null) {
            noCollisionTeam.removeEntity(player);
        }

        Team previousTeam = previousTeams.remove(player.getUniqueId());
        if (previousTeam != null) {
            previousTeam.addEntity(player);
        }
    }
}
