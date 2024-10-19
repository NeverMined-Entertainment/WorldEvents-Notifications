package org.nevermined.notifications.api.core.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record TitleData(@NotNull String title, @Nullable String subtitle, int fadeIn, int stay, int fadeOut) {

    public static final int DEFAULT_FADE_IN = 500;
    public static final int DEFAULT_STAY = 3500;
    public static final int DEFAULT_FADE_OUT = 1000;

}
