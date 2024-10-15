package org.nevermined.notifications.api.core.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record NotificationData(@NotNull String key, @Nullable TitleData titleData, @NotNull List<String> chat, @Nullable SoundData soundData, @NotNull NotificationFilter filter) {
}
