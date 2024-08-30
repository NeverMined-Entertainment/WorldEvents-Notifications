package org.nevermined.notifications.core;

import org.nevermined.notifications.core.data.NotificationData;
import org.nevermined.worldevents.api.core.EventData;
import org.nevermined.worldevents.api.core.QueueData;

public interface NotificationApi {

    void broadcast(QueueData queue, EventData event);
    NotificationData getNotificationData();

}
