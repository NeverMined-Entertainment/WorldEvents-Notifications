package org.nevermined.notifications.commands.modules;

import com.google.inject.AbstractModule;
import org.nevermined.notifications.commands.NotificationsCommand;

public class CommandModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(NotificationsCommand.class);
    }
}
