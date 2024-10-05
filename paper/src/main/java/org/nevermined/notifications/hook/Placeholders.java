package org.nevermined.notifications.hook;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.wyne.wutils.i18n.I18n;
import me.wyne.wutils.log.Log;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.nevermined.notifications.Notifications;
import org.nevermined.notifications.core.NotificationManager;
import org.nevermined.notifications.core.data.NotificationData;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

@Singleton
public class Placeholders extends PlaceholderExpansion {

    private final Notifications plugin;
    private final NotificationManager notificationManager;

    private final Map<String, BiFunction<String, Player, String>> notificationDataParserMap = new HashMap<>();

    @Inject
    public Placeholders(Notifications plugin, NotificationManager notificationManager) {
        this.plugin = plugin;
        this.notificationManager = notificationManager;
        createNotificationTitleParser();
        createNotificationSubtitleParser();
        createNotificationTitleFadeInParser();
        createNotificationTitleStayParser();
        createNotificationTitleFadeOutParser();
        createNotificationChatParser();
        createNotificationSoundKeyParser();
        createNotificationSoundVolumeParser();
        createNotificationSoundPitchParser();
        createNotificationFilterTypeParser();
        createNotificationFilterBlacklistParser();
        createNotificationFilterWhitelistParser();
        createNotificationFilterPlayersParser();
        createNotificationFilterPermissionsParser();
        register();
    }

    @Override
    public @NotNull String getIdentifier() {
        return "wenotif";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Wyne";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        if (params.startsWith("notification"))
        {
            // Key word/Queue key/data type
            // notification_  demo-notification-1  _title

            String[] args = params.split("_");

            if (!notificationDataParserMap.containsKey(args[2]))
            {
                Log.global.error("Notification placeholder type '" + args[2] + "' not found!");
                return null;
            }

            try {
                return notificationDataParserMap.get(args[2]).apply(args[1], null);
            } catch (NullPointerException e)
            {
                Log.global.exception(e);
                return null;
            }
        }

        return null;
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (params.startsWith("notification"))
        {
            // Key word/Queue key/data type
            // notification_  demo-notification-1  _title

            String[] args = params.split("_");

            if (!notificationDataParserMap.containsKey(args[2]))
            {
                Log.global.error("Notification placeholder type '" + args[2] + "' not found!");
                return null;
            }

            try {
                return notificationDataParserMap.get(args[2]).apply(args[1], player);
            } catch (NullPointerException e)
            {
                Log.global.exception(e);
                return null;
            }
        }

        return null;
    }

    private void createNotificationTitleParser()
    {
        notificationDataParserMap.put("title", (notificationKey, player) -> {
            NotificationData notificationData = notificationManager.getNotifications().get(notificationKey).getNotificationData();
            return notificationData.titleData() != null
                    ? I18n.global.getLegacyPlaceholderString(I18n.toLocale(player), player, notificationData.titleData().title())
                    : "";
        });
    }

    private void createNotificationSubtitleParser()
    {
        notificationDataParserMap.put("subtitle", (notificationKey, player) -> {
            NotificationData notificationData = notificationManager.getNotifications().get(notificationKey).getNotificationData();
            return notificationData.titleData() != null
                    ? I18n.global.getLegacyPlaceholderString(I18n.toLocale(player), player, notificationData.titleData().subtitle())
                    : "";
        });
    }

    private void createNotificationTitleFadeInParser()
    {
        notificationDataParserMap.put("fadein", (notificationKey, player) -> {
            NotificationData notificationData = notificationManager.getNotifications().get(notificationKey).getNotificationData();
            return notificationData.titleData() != null
                    ? String.valueOf(notificationData.titleData().fadeIn())
                    : "";
        });
    }

    private void createNotificationTitleStayParser()
    {
        notificationDataParserMap.put("stay", (notificationKey, player) -> {
            NotificationData notificationData = notificationManager.getNotifications().get(notificationKey).getNotificationData();
            return notificationData.titleData() != null
                    ? String.valueOf(notificationData.titleData().stay())
                    : "";
        });
    }

    private void createNotificationTitleFadeOutParser()
    {
        notificationDataParserMap.put("fadeout", (notificationKey, player) -> {
            NotificationData notificationData = notificationManager.getNotifications().get(notificationKey).getNotificationData();
            return notificationData.titleData() != null
                    ? String.valueOf(notificationData.titleData().fadeOut())
                    : "";
        });
    }

    private void createNotificationChatParser()
    {
        notificationDataParserMap.put("chat", (notificationKey, player) -> {
            NotificationData notificationData = notificationManager.getNotifications().get(notificationKey).getNotificationData();
            return notificationData.chat()
                    .stream()
                    .map(s -> I18n.global.getLegacyPlaceholderString(I18n.toLocale(player), player, s))
                    .reduce(I18n::reduceString)
                    .orElse("");
        });
    }

    private void createNotificationSoundKeyParser()
    {
        notificationDataParserMap.put("sound", (notificationKey, player) -> {
            NotificationData notificationData = notificationManager.getNotifications().get(notificationKey).getNotificationData();
            return notificationData.soundData() != null
                    ? notificationData.soundData().key()
                    : "";
        });
    }

    private void createNotificationSoundVolumeParser()
    {
        notificationDataParserMap.put("volume", (notificationKey, player) -> {
            NotificationData notificationData = notificationManager.getNotifications().get(notificationKey).getNotificationData();
            return notificationData.soundData() != null
                    ? String.valueOf(notificationData.soundData().volume())
                    : "";
        });
    }

    private void createNotificationSoundPitchParser()
    {
        notificationDataParserMap.put("pitch", (notificationKey, player) -> {
            NotificationData notificationData = notificationManager.getNotifications().get(notificationKey).getNotificationData();
            return notificationData.soundData() != null
                    ? String.valueOf(notificationData.soundData().pitch())
                    : "";
        });
    }

    private void createNotificationFilterTypeParser()
    {
        notificationDataParserMap.put("type", (notificationKey, player) -> {
            NotificationData notificationData = notificationManager.getNotifications().get(notificationKey).getNotificationData();
            return notificationData.filter().type();
        });
    }

    private void createNotificationFilterWhitelistParser()
    {
        notificationDataParserMap.put("whitelist", (notificationKey, player) -> {
            NotificationData notificationData = notificationManager.getNotifications().get(notificationKey).getNotificationData();
            return notificationData.filter().whitelist()
                    .stream()
                    .reduce((s1, s2) -> s1 + ", " + s2)
                    .orElse("");
        });
    }

    private void createNotificationFilterBlacklistParser()
    {
        notificationDataParserMap.put("blacklist", (notificationKey, player) -> {
            NotificationData notificationData = notificationManager.getNotifications().get(notificationKey).getNotificationData();
            return notificationData.filter().blacklist()
                    .stream()
                    .reduce((s1, s2) -> s1 + ", " + s2)
                    .orElse("");
        });
    }

    private void createNotificationFilterPlayersParser()
    {
        notificationDataParserMap.put("players", (notificationKey, player) -> {
            NotificationData notificationData = notificationManager.getNotifications().get(notificationKey).getNotificationData();
            return notificationData.filter().players()
                    .stream()
                    .reduce((s1, s2) -> s1 + ", " + s2)
                    .orElse("");
        });
    }

    private void createNotificationFilterPermissionsParser()
    {
        notificationDataParserMap.put("permissions", (notificationKey, player) -> {
            NotificationData notificationData = notificationManager.getNotifications().get(notificationKey).getNotificationData();
            return notificationData.filter().permissions()
                    .stream()
                    .reduce((s1, s2) -> s1 + ", " + s2)
                    .orElse("");
        });
    }
}
