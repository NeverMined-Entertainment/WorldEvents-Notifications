package org.nevermined.notifications.commands;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.LiteralArgument;
import me.wyne.wutils.i18n.I18n;
import org.nevermined.notifications.Notifications;

@Singleton
public class NotificationsCommand {

    private final Notifications plugin;

    @Inject
    public NotificationsCommand(Notifications plugin)
    {
        this.plugin = plugin;
        registerMainCommand();
    }

    private void registerMainCommand()
    {
        new CommandTree("wenotif")
                .then(new LiteralArgument("reload")
                        .withPermission(CommandPermission.OP)
                        .executes(((sender, args) -> {
                            plugin.reload();
                            sender.sendMessage(I18n.global.getLegacyPlaceholderComponent(I18n.toLocale(sender), sender, "success-plugin-reloaded"));
                        })))
                .register();
    }

}
