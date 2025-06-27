package com.puttysoftware.lasertank.asset;

import java.net.URL;

import com.puttysoftware.lasertank.asset.music.MusicIndex;
import com.puttysoftware.lasertank.locale.global.GlobalStrings;
import com.puttysoftware.lasertank.locale.global.UntranslatedString;

public enum Music implements MusicIndex {
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
        final var path = GlobalStrings.loadUntranslated(UntranslatedString.MUSIC_PATH) + this.getName()
                + GlobalStrings.loadUntranslated(UntranslatedString.MUSIC_EXTENSION);
        return Music.class.getResource(path);
    }
}
