package org.nevermined.notifications.core;

import me.wyne.wutils.i18n.I18n;
import me.wyne.wutils.i18n.language.replacement.Placeholder;
import me.wyne.wutils.i18n.language.replacement.TextReplacement;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;
import org.nevermined.worldevents.api.core.EventData;
import org.nevermined.worldevents.api.core.QueueData;

public class Notification {

    private final NotificationData notificationData;

    public Notification(NotificationData notificationData)
    {
        this.notificationData = notificationData;
    }

    public void broadcast(QueueData queue, EventData event)
    {
        if (!notificationData.filter().isValid(queue.key()))
            return;
        if (!notificationData.filter().isValid(event.key()))
            return;

        TextReplacement[] replacements = {
                Placeholder.replace("queue-key", queue.key()),
                Placeholder.legacy("queue-name", queue.name()),
                Placeholder.legacy("queue-description", I18n.reduceComponent(queue.description())),
                Placeholder.replace("queue-capacity", String.valueOf(queue.capacity())),
                Placeholder.replace("event-key", event.key()),
                Placeholder.legacy("event-name", event.name()),
                Placeholder.legacy("event-description", I18n.reduceComponent(event.description())),
                Placeholder.replace("event-chance", String.valueOf(event.chancePercent())),
                Placeholder.replace("event-duration", String.valueOf(event.durationSeconds())),
                Placeholder.replace("event-cooldown", String.valueOf(event.cooldownSeconds()))
        };

        for (Player player : notificationData.filter().getRecieverList())
        {
            Component title = I18n.global.getLegacyPlaceholderComponent(player.locale(), player, notificationData.title(), replacements);
            Component subtitle = I18n.global.getLegacyPlaceholderComponent(player.locale(), player, notificationData.subtitle(), replacements);
            Component chat = notificationData.chat().stream()
                    .map(s -> I18n.global.getLegacyPlaceholderComponent(player.locale(), player, s, replacements))
                    .reduce(I18n::reduceComponent).orElse(Component.empty());
            Title showTitle = Title.title(title, subtitle);

            player.showTitle(showTitle);
            player.sendMessage(chat);
            player.playSound(notificationData.sound());
        }
    }

}
