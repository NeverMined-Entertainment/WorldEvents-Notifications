package org.nevermined.notifications.core;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.lucko.helper.Events;
import me.lucko.helper.event.filter.EventFilters;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.nevermined.notifications.Notifications;
import org.nevermined.notifications.core.data.NotificationData;
import org.nevermined.notifications.core.data.NotificationFilter;
import org.nevermined.notifications.core.data.SoundData;
import org.nevermined.notifications.core.data.TitleData;
import org.nevermined.worldevents.api.core.EventData;
import org.nevermined.worldevents.api.core.QueueData;
import org.nevermined.worldevents.api.event.WorldEventStart;
import org.nevermined.worldevents.api.event.WorldEventStop;

import java.util.*;

@Singleton
public class NotificationManager implements NotificationManagerApi {

    private final Map<String, NotificationApi> notifications = new HashMap<>();

    private final Notifications plugin;

    @Inject
    public NotificationManager(Notifications plugin)
    {
        this.plugin = plugin;
        registerEvents();
    }

    @Override
    public void broadcast(String notificationKey) {
        notifications.get(notificationKey).broadcast();
    }

    @Override
    public void broadcast(String notificationKey, Player player) {
        notifications.get(notificationKey).broadcast(player);
    }

    @Override
    public void broadcast(String notificationKey, QueueData queue, EventData event) {
        notifications.get(notificationKey).broadcast(queue, event);
    }

    @Override
    public void broadcast(String notificationKey, Player player, QueueData queue, EventData event) {
        notifications.get(notificationKey).broadcast(player, queue, event);
    }

    private void registerEvents()
    {
        Events.subscribe(WorldEventStart.class)
                .filter(EventFilters.ignoreCancelled())
                .handler(e -> {
                    notifications.values().stream()
                            .filter(notification -> notification.getNotificationData().filter().type().equalsIgnoreCase("start"))
                            .forEach(notification -> notification.broadcast(e.getWorldEventQueue().getQueueData(), e.getWorldEvent().getEventData()));
                });
        Events.subscribe(WorldEventStop.class)
                .handler(e -> {
                    notifications.values().stream()
                            .filter(notification -> notification.getNotificationData().filter().type().equalsIgnoreCase("stop"))
                            .forEach(notification -> notification.broadcast(e.getWorldEventQueue().getQueueData(), e.getWorldEvent().getEventData()));
                });
    }

    private void loadNotifications(FileConfiguration config)
    {
        for (String notificationKey : config.getConfigurationSection("notifications").getKeys(false))
        {
            ConfigurationSection notificationSection = config.getConfigurationSection("notifications." + notificationKey);
            ConfigurationSection titleSection = notificationSection.getConfigurationSection("title");
            ConfigurationSection soundSection = notificationSection.getConfigurationSection("sound");
            notifications.put(notificationKey, new Notification(new NotificationData(
                    notificationKey,
                    titleSection != null ? new TitleData(
                            titleSection.getString("title"),
                            titleSection.getString("subtitle"),
                            titleSection.contains("fadein")
                                ? titleSection.getInt("fadein")
                                : 500,
                            titleSection.contains("stay")
                                ? titleSection.getInt("stay")
                                : 3500,
                            titleSection.contains("fadeout")
                                ? titleSection.getInt("fadeout")
                                : 1000
                    ) : null,
                    notificationSection.getStringList("chat"),
                    soundSection != null ? new SoundData(
                            soundSection.getString("key"),
                            soundSection.contains("volume")
                                ? (float) soundSection.getDouble("volume")
                                : 1.0f,
                            soundSection.contains("pitch")
                                ? (float) soundSection.getDouble("pitch")
                                : 1.0f
                    ) : null,
                    new NotificationFilter(
                            notificationSection.contains("type")
                                ? notificationSection.getString("type")
                                : "start",
                            notificationSection.getStringList("whitelist"),
                            notificationSection.getStringList("blacklist"),
                            notificationSection.getStringList("permissions"),
                            notificationSection.getStringList("players")
                    )
            )));
        }
    }

    @Override
    public void reloadNotifications() {
        loadNotifications(plugin.getConfig());
    }

    @Override
    public Map<String, NotificationApi> getNotifications() {
        return notifications;
    }
}
