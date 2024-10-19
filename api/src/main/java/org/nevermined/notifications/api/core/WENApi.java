package org.nevermined.notifications.api.core;

import org.nevermined.notifications.api.NotificationsApi;

public final class WENApi {

    private static NotificationsApi instance;

    public static void setInstance(NotificationsApi instance) {
        WENApi.instance = instance;
    }

    public static NotificationsApi getInstance() {
        return instance;
    }
}
