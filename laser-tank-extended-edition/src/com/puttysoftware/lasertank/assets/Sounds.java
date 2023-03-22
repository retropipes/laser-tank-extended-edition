package com.puttysoftware.lasertank.assets;

import com.puttysoftware.diane.asset.sound.DianeSoundPlayer;
import com.puttysoftware.lasertank.settings.Settings;

public class Sounds {
    private Sounds() {
        // Do nothing
    }

    public static void play(final Sound soundID) {
        if (Settings.areSoundsEnabled()) {
            DianeSoundPlayer.play(soundID);
        }
    }
}
