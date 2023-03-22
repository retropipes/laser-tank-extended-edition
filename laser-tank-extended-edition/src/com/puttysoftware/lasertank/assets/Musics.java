package com.puttysoftware.lasertank.assets;

import java.io.IOException;

import com.puttysoftware.diane.asset.music.DianeMusicPlayer;
import com.puttysoftware.diane.random.RandomRange;
import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.settings.Settings;

public class Musics {
    private Musics() {
        // Do nothing
    }

    public static void play(final Music musicID) {
        if (Settings.isMusicEnabled()) {
            try {
                switch (musicID) {
                    case GAME:
                    case GAME_2:
                    case GAME_3:
                    case GAME_4:
                    case GAME_5:
                        var randomGame = new Music[] { Music.GAME, Music.GAME_2, Music.GAME_3, Music.GAME_4,
                                Music.GAME_5 };
                        var randomGamePlayed = randomGame[RandomRange.generate(0, randomGame.length - 1)];
                        DianeMusicPlayer.play(randomGamePlayed);
                        break;
                    default:
                        DianeMusicPlayer.play(musicID);
                        break;
                }
            } catch (IOException e) {
                LaserTankEE.logError(e);
            }
        }
    }
}
