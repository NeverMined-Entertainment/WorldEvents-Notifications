package org.nevermined.notifications.core.data;

import com.google.common.collect.ImmutableList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public record NotificationFilter(@NotNull String type, @NotNull List<String> whitelist, @NotNull List<String> blacklist, @NotNull List<String> permissions, @NotNull List<String> players) {

    public boolean isValid(String key)
    {
        if (whitelist.isEmpty() && blacklist.isEmpty())
            return true;
        return whitelist.contains(key);
    }

    public Set<Player> getReceiverList()
    {
        if (permissions.isEmpty() && players.isEmpty())
            return Set.copyOf(Bukkit.getOnlinePlayers());

        ImmutableList<Player> playersSnapshot = ImmutableList.copyOf(Bukkit.getOnlinePlayers());
        Set<Player> receiverList = new HashSet<>();
        players.stream()
                .map(Bukkit::getPlayer)
                .filter(Objects::nonNull)
                .forEach(receiverList::add);
        playersSnapshot.stream()
                .filter(player -> permissions.stream().anyMatch(player::hasPermission))
                .forEach(receiverList::add);
        return receiverList;
    }

}
