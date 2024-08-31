package org.nevermined.notifications.core;

import org.bukkit.entity.Player;
import org.nevermined.worldevents.api.core.EventData;
import org.nevermined.worldevents.api.core.QueueData;

import java.util.Map;

public interface NotificationManagerApi {

    void broadcast(String notificationKey);
    void broadcast(String notificationKey, Player player);
    void broadcast(String notificationKey, QueueData queue, EventData event);
    void broadcast(String notificationKey, Player player, QueueData queue, EventData event);

    void reloadNotifications();
    Map<String, NotificationApi> getNotifications();

}
