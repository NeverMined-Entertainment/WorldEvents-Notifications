package org.nevermined.notifications.core.data;

import me.wyne.wutils.i18n.I18n;
import me.wyne.wutils.i18n.language.replacement.TextReplacement;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record NotificationData(@NotNull String key, @Nullable TitleData titleData, @NotNull List<String> chat, @Nullable SoundData soundData, @NotNull NotificationFilter filter) {

    public Component buildChat(Player player, TextReplacement... replacements)
    {
        return chat().stream()
                .map(s -> I18n.global.getLegacyPlaceholderComponent(player.locale(), player, s, replacements))
                .reduce(I18n::reduceComponent).orElse(Component.empty());
    }

}
