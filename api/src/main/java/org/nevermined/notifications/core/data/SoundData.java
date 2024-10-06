package org.nevermined.notifications.core.data;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.jetbrains.annotations.NotNull;

public record SoundData(@NotNull String key, float volume, float pitch) {

    public static final float DEFAULT_VOLUME = 1.0f;
    public static final float DEFAULT_PITCH = 1.0f;

    public Sound sound()
    {
        return Sound.sound(Key.key(key()), Sound.Source.MASTER, volume(), pitch());
    }

}
