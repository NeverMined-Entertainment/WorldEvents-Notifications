package org.nevermined.notifications.modules;

import com.google.inject.AbstractModule;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.nevermined.notifications.Notifications;

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
        bind(FileConfiguration.class)
                .toInstance(plugin.getConfig());
    }

}
