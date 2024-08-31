package org.nevermined.notifications.core.data;

import com.google.common.collect.ImmutableList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public record NotificationFilter(@NotNull String type, @NotNull List<String> whitelist, @NotNull List<String> blacklist, @NotNull List<String> permissions, @NotNull List<String> players) {

    public boolean isValid(String key)
    {
        if (whitelist.isEmpty() && blacklist.isEmpty())
            return true;
        return whitelist.contains(key);
    }

    public List<Player> getRecieverList()
    {
        ImmutableList<Player> playersSnapshot = ImmutableList.copyOf(Bukkit.getOnlinePlayers());
        List<Player> recieverList = new ArrayList<>();
        players.stream()
                .filter(playerName -> Bukkit.getPlayer(playerName) != null)
                .forEach(playerName -> recieverList.add(Bukkit.getPlayer(playerName)));
        playersSnapshot.stream()
                .filter(player -> permissions.isEmpty() || permissions.stream().anyMatch(player::hasPermission))
                .forEach(recieverList::add);
        return recieverList;
    }

}
