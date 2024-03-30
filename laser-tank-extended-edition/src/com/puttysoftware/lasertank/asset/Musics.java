package com.puttysoftware.lasertank.asset;

import java.io.IOException;

import com.puttysoftware.lasertank.asset.music.MusicIndex;
import com.puttysoftware.lasertank.asset.music.MusicPlayer;
import com.puttysoftware.lasertank.random.RandomRange;
import com.puttysoftware.lasertank.settings.Settings;
import com.puttysoftware.lasertank.tasks.AppTaskManager;

public class Musics {
    public static void play(final Music musicID) {
	if (Settings.isMusicEnabled()) {
	    try {
		switch (musicID) {
		case GAME:
		case GAME_2:
		case GAME_3:
		case GAME_4:
		case GAME_5:
		    final var randomGame = new Music[] { Music.GAME, Music.GAME_2, Music.GAME_3, Music.GAME_4,
			    Music.GAME_5 };
		    final var randomGamePlayed = randomGame[RandomRange.generate(0, randomGame.length - 1)];
		    MusicPlayer.play(randomGamePlayed);
		    break;
		default:
		    MusicPlayer.play(musicID);
		    break;
		}
	    } catch (final IOException e) {
		AppTaskManager.error(e);
	    }
	}
    }

    public static void play(final MusicIndex musicID) {
	if (Settings.isMusicEnabled()) {
	    try {
		MusicPlayer.play(musicID);
	    } catch (final IOException e) {
		AppTaskManager.error(e);
	    }
	}
    }

    public static void stopPlaying() {
	MusicPlayer.stopPlaying();
    }

    private Musics() {
	// Do nothing
    }
}
