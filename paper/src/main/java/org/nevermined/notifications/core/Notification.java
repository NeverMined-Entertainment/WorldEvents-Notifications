package org.nevermined.notifications.core;

import me.wyne.wutils.i18n.I18n;
import me.wyne.wutils.i18n.language.replacement.Placeholder;
import me.wyne.wutils.i18n.language.replacement.TextReplacement;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import org.nevermined.notifications.api.core.NotificationApi;
import org.nevermined.notifications.api.core.data.NotificationData;
import org.nevermined.notifications.api.core.data.TitleData;
import org.nevermined.worldevents.api.core.EventData;
import org.nevermined.worldevents.api.core.QueueData;

import java.time.Duration;

public class Notification implements NotificationApi {

    private final NotificationData notificationData;

    public Notification(NotificationData notificationData)
    {
        this.notificationData = notificationData;
    }

    @Override
    public void broadcast() {
        for (Player player : notificationData.filter().getReceiverList())
        {
            broadcast(player);
        }
    }

    @Override
    public void broadcast(Player player) {
        if (notificationData.titleData() != null)
            player.showTitle(buildTitle(notificationData.titleData(), player));
        if (!notificationData.chat().isEmpty())
            player.sendMessage(buildChat(notificationData, player));
        if (notificationData.soundData() != null)
            player.playSound(notificationData.soundData().sound());
    }

    private void broadcast(Player player, TextReplacement... replacements)
    {
        if (notificationData.titleData() != null)
            player.showTitle(buildTitle(notificationData.titleData(), player, replacements));
        if (!notificationData.chat().isEmpty())
            player.sendMessage(buildChat(notificationData, player, replacements));
        if (notificationData.soundData() != null)
            player.playSound(notificationData.soundData().sound());
    }

    @Override
    public void broadcast(QueueData queue, EventData event)
    {
        if (!notificationData.filter().isValid(queue.key()))
            return;
        if (!notificationData.filter().isValid(event.key()))
            return;

        TextReplacement[] replacements = getReplacements(queue, event);

        for (Player player : notificationData.filter().getReceiverList())
        {
            broadcast(player, replacements);
        }
    }

    @Override
    public void broadcast(Player player, QueueData queue, EventData event) {
        if (!notificationData.filter().isValid(queue.key()))
            return;
        if (!notificationData.filter().isValid(event.key()))
            return;

        TextReplacement[] replacements = getReplacements(queue, event);

        broadcast(player, replacements);
    }

    private TextReplacement[] getReplacements(QueueData queue, EventData event)
    {
        return new TextReplacement[] {
                Placeholder.replace("notification-key", notificationData.key()),
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
    }

    private Component buildChat(NotificationData notificationData, @Nullable Player player, TextReplacement... replacements)
    {
        return notificationData.chat().stream()
                .map(s -> I18n.global.getLegacyPlaceholderComponent(I18n.toLocale(player), player, s, replacements))
                .reduce(I18n::reduceComponent).orElse(Component.empty());
    }

    private Title buildTitle(TitleData titleData, @Nullable Player player, TextReplacement... replacements)
    {
        Component title = I18n.global.getLegacyPlaceholderComponent(I18n.toLocale(player), player, titleData.title(), replacements);
        Component subtitle = titleData.subtitle() != null ? I18n.global.getLegacyPlaceholderComponent(I18n.toLocale(player), player, titleData.subtitle(), replacements) : Component.empty();
        Title.Times times = Title.Times.of(Duration.ofMillis(titleData.fadeIn()), Duration.ofMillis(titleData.stay()), Duration.ofMillis(titleData.fadeOut()));
        return Title.title(title, subtitle, times);
    }

    @Override
    public NotificationData getNotificationData() {
        return notificationData;
    }
}
