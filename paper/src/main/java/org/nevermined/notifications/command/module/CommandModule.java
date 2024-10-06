package org.nevermined.notifications.command.module;

import com.google.inject.AbstractModule;
import org.nevermined.notifications.command.NotificationsCommand;

public class CommandModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(NotificationsCommand.class);
    }
}
