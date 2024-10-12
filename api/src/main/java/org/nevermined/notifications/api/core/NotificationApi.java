package org.nevermined.notifications.api.core;

import org.bukkit.entity.Player;
import org.nevermined.notifications.api.core.data.NotificationData;
import org.nevermined.worldevents.api.core.EventData;
import org.nevermined.worldevents.api.core.QueueData;

public interface NotificationApi {

    void broadcast();
    void broadcast(Player player);
    void broadcast(QueueData queue, EventData event);
    void broadcast(Player player, QueueData queue, EventData event);
    NotificationData getNotificationData();

}
