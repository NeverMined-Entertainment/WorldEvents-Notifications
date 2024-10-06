package org.nevermined.notifications.core.data;

import me.wyne.wutils.i18n.I18n;
import me.wyne.wutils.i18n.language.replacement.TextReplacement;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;

public record TitleData(@NotNull String title, @Nullable String subtitle, int fadeIn, int stay, int fadeOut) {

    public static final int DEFAULT_FADE_IN = 500;
    public static final int DEFAULT_STAY = 3500;
    public static final int DEFAULT_FADE_OUT = 1000;

    public Title buildTitle(Player player, TextReplacement... replacements)
    {
        Component title = I18n.global.getLegacyPlaceholderComponent(player.locale(), player, title(), replacements);
        Component subtitle = subtitle() != null ? I18n.global.getLegacyPlaceholderComponent(player.locale(), player, subtitle(), replacements) : Component.empty();
        Title.Times times = Title.Times.of(Duration.ofMillis(fadeIn), Duration.ofMillis(stay), Duration.ofMillis(fadeOut));
        return Title.title(title, subtitle, times);
    }

}
