package com.puttysoftware.lasertank.asset;

import com.puttysoftware.lasertank.asset.sound.SoundPlayer;
import com.puttysoftware.lasertank.settings.Settings;

public class Sounds {
    public static void play(final Sound soundID) {
        if (Settings.areSoundsEnabled()) {
            SoundPlayer.play(soundID);
        }
    }

    private Sounds() {
        // Do nothing
    }
}
