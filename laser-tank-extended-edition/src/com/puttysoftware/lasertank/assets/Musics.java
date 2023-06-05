package com.puttysoftware.lasertank.assets;

import java.io.IOException;

import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.engine.asset.music.LTEMusicIndex;
import com.puttysoftware.lasertank.engine.asset.music.LTEMusicPlayer;
import com.puttysoftware.lasertank.engine.random.RandomRange;
import com.puttysoftware.lasertank.settings.Settings;

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
		    LTEMusicPlayer.play(randomGamePlayed);
		    break;
		default:
		    LTEMusicPlayer.play(musicID);
		    break;
		}
	    } catch (final IOException e) {
		LaserTankEE.logError(e);
	    }
	}
    }

    public static void play(final LTEMusicIndex musicID) {
	if (Settings.isMusicEnabled()) {
	    try {
		LTEMusicPlayer.play(musicID);
	    } catch (final IOException e) {
		LaserTankEE.logError(e);
	    }
	}
    }

    public static void stopPlaying() {
	LTEMusicPlayer.stopPlaying();
    }

    private Musics() {
	// Do nothing
    }
}
