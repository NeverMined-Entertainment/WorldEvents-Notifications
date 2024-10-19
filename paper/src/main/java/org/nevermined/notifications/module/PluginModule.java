package org.nevermined.notifications.module;

import com.google.inject.AbstractModule;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.nevermined.notifications.Notifications;
import org.nevermined.notifications.api.NotificationsApi;

public class PluginModule extends AbstractModule {

    private final Notifications plugin;

    public PluginModule(Notifications plugin)
    {
        this.plugin = plugin;
    }

    @Override
    protected void configure() {
        bind(JavaPlugin.class)
                .toInstance(plugin);
        bind(Notifications.class)
                .toInstance(plugin);
        bind(NotificationsApi.class)
                .to(org.nevermined.notifications.NotificationsApi.class);
        bind(FileConfiguration.class)
                .toInstance(plugin.getConfig());
    }

}
