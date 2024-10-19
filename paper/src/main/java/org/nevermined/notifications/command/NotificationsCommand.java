package org.nevermined.notifications.command;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import dev.jorel.commandapi.CommandAPIBukkit;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.SuggestionInfo;
import dev.jorel.commandapi.arguments.*;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandArguments;
import me.wyne.wutils.i18n.I18n;
import me.wyne.wutils.i18n.language.replacement.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.nevermined.notifications.Notifications;
import org.nevermined.notifications.core.NotificationApi;
import org.nevermined.notifications.core.NotificationManagerApi;
import org.nevermined.worldevents.api.WEApi;
import org.nevermined.worldevents.api.core.WorldEventManagerApi;
import org.nevermined.worldevents.api.core.WorldEventQueueApi;
import org.nevermined.worldevents.api.core.WorldEventSelfFactoryApi;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Singleton
public class NotificationsCommand {

    private final Notifications plugin;
    private final NotificationManagerApi notificationManager;

    @Inject
    public NotificationsCommand(Notifications plugin, NotificationManagerApi notificationManager)
    {
        this.plugin = plugin;
        this.notificationManager = notificationManager;
        registerMainCommand();
    }

    private void registerMainCommand()
    {
        new CommandTree("wenotif")
                .then(new LiteralArgument("notifications")
                        .withPermission("wenotif.notificationcontrol")
                        .then(new LiteralArgument("reload")
                            .withPermission(CommandPermission.OP)
                            .executes(((sender, args) -> {
                                notificationManager.reloadNotifications();
                                sender.sendMessage(I18n.global.getLegacyPlaceholderComponent(I18n.toLocale(sender), sender, "success-notifications-reloaded"));
                            })))
                        .then(new StringArgument("notificationKey")
                                .replaceSuggestions(ArgumentSuggestions.stringCollection(info -> notificationManager.getNotifications().keySet()))
                                .then(new MultiLiteralArgument("notificationAction", "info", "broadcast")
                                        .executes(this::executeNotificationCommand)
                                        .then(new EntitySelectorArgument.ManyPlayers("broadcastTargets").setOptional(true)
                                                .executes(this::executeNotificationCommand)
                                                .then(new StringArgument("notificationEvent").setOptional(true)
                                                        .replaceSuggestions(ArgumentSuggestions.stringCollection(this::getEventKeySuggestions))
                                                        .executes(this::executeNotificationCommand))))))
                .then(new LiteralArgument("reload")
                        .withPermission(CommandPermission.OP)
                        .executes(((sender, args) -> {
                            plugin.reload();
                            sender.sendMessage(I18n.global.getLegacyPlaceholderComponent(I18n.toLocale(sender), sender, "success-plugin-reloaded"));
                        })))
                .register();
    }

    private Collection<String> getEventKeySuggestions(SuggestionInfo<CommandSender> info)
    {
        Set<String> suggestions = new HashSet<>();

        if (!((String)info.previousArgs().getOrDefault("notificationAction", "")).equalsIgnoreCase("broadcast"))
            return suggestions;

        WorldEventManagerApi worldEventManager = WEApi.getInstance().getWorldEventManager();
        suggestions.addAll(worldEventManager.getEventQueueMap().values().stream()
                .flatMap(queue -> queue.getEventSet().keySet().stream())
                .collect(Collectors.toSet()));

        return suggestions;
    }

    private void executeNotificationCommand(CommandSender sender, CommandArguments args) throws WrapperCommandSyntaxException
    {
        String key = args.getOrDefaultRaw("notificationKey", "");
        String action = (String) args.getOrDefault("notificationAction", "");
        Optional<Collection<Player>> targets = args.getOptionalUnchecked("broadcastTargets");
        Optional<String> eventKeyOptional = args.getOptionalByClass("notificationEvent", String.class);
        ImmutableList<Player> onlinePlayers = ImmutableList.copyOf(Bukkit.getOnlinePlayers());

        if (!validateNotificationKey(key))
            throw CommandAPIBukkit.failWithAdventureComponent(I18n.global.getLegacyPlaceholderComponent(I18n.toLocale(sender), sender, "error-notification-not-found", Placeholder.replace("notification-key", key)));

        NotificationApi notification = notificationManager.getNotifications().get(key);

        switch (action) {
            case "info" -> {
                sender.sendMessage(I18n.reduceComponent(I18n.global.getLegacyPlaceholderComponentList(I18n.toLocale(sender), sender, "info-notification", Placeholder.replace("notification-key", key))));
            }
            case "broadcast" -> {
                if (eventKeyOptional.isPresent())
                {
                    String eventKey = eventKeyOptional.get();
                    WorldEventManagerApi worldEventManager = WEApi.getInstance().getWorldEventManager();
                    Optional<WorldEventQueueApi> queue = worldEventManager.getEventQueueMap().values().stream()
                            .filter(streamQueue -> streamQueue.getEventSet().containsKey(eventKey))
                            .findAny();
                    if (queue.isEmpty())
                        throw CommandAPIBukkit.failWithAdventureComponent(I18n.global.getLegacyPlaceholderComponent(I18n.toLocale(sender), sender, "error-event-key-not-found", Placeholder.replace("event-key", eventKey)));
                    WorldEventSelfFactoryApi event = queue.get().getEventSet().get(eventKey);
                    if (event == null)
                        throw CommandAPIBukkit.failWithAdventureComponent(I18n.global.getLegacyPlaceholderComponent(I18n.toLocale(sender), sender, "error-event-key-not-found", Placeholder.replace("event-key", eventKey)));

                    if (targets.isPresent() && !targets.get().containsAll(onlinePlayers))
                        targets.get().forEach(target -> notification.broadcast(target, queue.get().getQueueData(), event.getEventData()));
                    else
                        notification.broadcast(queue.get().getQueueData(), event.getEventData());
                    return;
                }

                if (targets.isPresent() && !targets.get().containsAll(onlinePlayers))
                    targets.get().forEach(notification::broadcast);
                else
                    notification.broadcast();
            }
        }
    }

    private boolean validateNotificationKey(String notificationKey)
    {
        return notificationManager.getNotifications().containsKey(notificationKey);
    }
}
