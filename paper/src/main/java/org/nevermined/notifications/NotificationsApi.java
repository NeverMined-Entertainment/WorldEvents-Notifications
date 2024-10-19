package org.nevermined.notifications;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.nevermined.notifications.api.core.NotificationManagerApi;
import org.nevermined.notifications.api.core.WENApi;

@Singleton
public class NotificationsApi implements org.nevermined.notifications.api.NotificationsApi {

    private final Notifications plugin;
    private final NotificationManagerApi notificationManager;

    @Inject
    public NotificationsApi(Notifications plugin, NotificationManagerApi notificationManager) {
        this.plugin = plugin;
        this.notificationManager = notificationManager;
        plugin.getServer().getServicesManager().register(org.nevermined.notifications.api.NotificationsApi.class, this, plugin, ServicePriority.Normal);
        WENApi.setInstance(this);
    }

    @Override
    public JavaPlugin getPlugin() {
        return plugin;
    }

    @Override
    public NotificationManagerApi getNotificationManager() {
        return notificationManager;
    }
}
