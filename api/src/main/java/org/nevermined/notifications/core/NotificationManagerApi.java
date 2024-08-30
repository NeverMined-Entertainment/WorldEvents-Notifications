package org.nevermined.notifications.core;

import java.util.Map;

public interface NotificationManagerApi {

    void reloadNotifications();
    Map<String, NotificationApi> getNotifications();

}
