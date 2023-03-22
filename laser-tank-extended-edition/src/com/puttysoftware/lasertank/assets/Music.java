package com.puttysoftware.lasertank.assets;

import java.net.URL;

import com.puttysoftware.diane.asset.music.DianeMusicIndex;
import com.puttysoftware.lasertank.locale.global.GlobalStrings;
import com.puttysoftware.lasertank.locale.global.UntranslatedString;

public enum Music implements DianeMusicIndex {
    MAIN_SCREEN,
    GAME,
    GAME_2,
    GAME_3,
    GAME_4,
    GAME_5,
    EDITOR;

    @Override
    public String getName() {
        return Integer.toString(this.ordinal());
    }

    @Override
    public URL getURL() {
        var path = GlobalStrings.loadUntranslated(UntranslatedString.MUSIC_PATH) + this.getName()
                + GlobalStrings.loadUntranslated(UntranslatedString.MUSIC_EXTENSION);
        return Music.class.getResource(path);
    }
}
