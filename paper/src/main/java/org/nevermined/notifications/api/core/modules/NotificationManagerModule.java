package org.nevermined.notifications.api.core.modules;

import com.google.inject.AbstractModule;
import org.nevermined.notifications.api.core.NotificationManager;
import org.nevermined.notifications.api.core.NotificationManagerApi;

public class NotificationManagerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(NotificationManagerApi.class).to(NotificationManager.class);
    }

}
