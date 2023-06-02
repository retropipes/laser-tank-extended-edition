package com.puttysoftware.lasertank.assets;

import com.puttysoftware.lasertank.engine.asset.sound.LTESoundPlayer;
import com.puttysoftware.lasertank.settings.Settings;

public class Sounds {
    public static void play(final Sound soundID) {
	if (Settings.areSoundsEnabled()) {
	    LTESoundPlayer.play(soundID);
	}
    }

    private Sounds() {
	// Do nothing
    }
}
