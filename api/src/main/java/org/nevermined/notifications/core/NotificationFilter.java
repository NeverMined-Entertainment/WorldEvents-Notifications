package org.nevermined.notifications.core;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public record NotificationFilter(List<String> whitelist, List<String> blacklist, List<String> permissions, List<String> players) {

    public boolean isValid(String key)
    {
        if (whitelist.isEmpty() && blacklist.isEmpty())
            return true;
        return whitelist.contains(key);
    }

    public List<Player> getRecieverList()
    {
        return Bukkit.getOnlinePlayers().stream()
                .filter(player -> players.stream().anyMatch(playerName -> player.getName().equals(playerName)))
                .filter(player -> permissions.stream().anyMatch(player::hasPermission))
                .collect(Collectors.toUnmodifiableList());
    }

}
