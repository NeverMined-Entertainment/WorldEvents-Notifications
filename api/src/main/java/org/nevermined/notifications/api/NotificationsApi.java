package org.nevermined.notifications.api;

import org.bukkit.plugin.java.JavaPlugin;
import org.nevermined.notifications.api.core.NotificationManagerApi;

public interface NotificationsApi {
    JavaPlugin getPlugin();
    NotificationManagerApi getNotificationManager();
}
