package com.puttysoftware.lasertank.assets;

import com.puttysoftware.diane.asset.sound.DianeSoundPlayer;

public class Sounds {
    private Sounds() {
        // Do nothing
    }

    public static void play(final Sound soundID) {
        DianeSoundPlayer.play(soundID);
    }
}
