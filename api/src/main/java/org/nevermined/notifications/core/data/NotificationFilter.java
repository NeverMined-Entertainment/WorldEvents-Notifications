package org.nevermined.notifications.core.data;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public record NotificationFilter(@NotNull String type, @NotNull List<String> whitelist, @NotNull List<String> blacklist, @NotNull List<String> permissions, @NotNull List<String> players) {

    public boolean isValid(String key)
    {
        if (whitelist.isEmpty() && blacklist.isEmpty())
            return true;
        return whitelist.contains(key);
    }

    public List<Player> getRecieverList()
    {
        return Bukkit.getOnlinePlayers().stream()
                .filter(player -> players.isEmpty() || players.stream().anyMatch(playerName -> player.getName().equals(playerName)))
                .filter(player -> permissions.isEmpty() || permissions.stream().anyMatch(player::hasPermission))
                .collect(Collectors.toUnmodifiableList());
    }

}
