package org.nevermined.notifications.core;

import net.kyori.adventure.sound.Sound;

import java.util.List;

public record NotificationData(String title, String subtitle, List<String> chat, Sound sound, NotificationFilter filter) {
}
