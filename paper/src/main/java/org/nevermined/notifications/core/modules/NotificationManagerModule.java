package org.nevermined.notifications.core.modules;

import com.google.inject.AbstractModule;
import org.nevermined.notifications.core.NotificationManager;
import org.nevermined.notifications.core.NotificationManagerApi;

public class NotificationManagerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(NotificationManagerApi.class).to(NotificationManager.class);
    }

}
