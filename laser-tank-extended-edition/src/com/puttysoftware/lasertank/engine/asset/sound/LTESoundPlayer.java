/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.engine.asset.sound;

public final class LTESoundPlayer {
    // Constants
    protected static final int EXTERNAL_BUFFER_SIZE = 4096; // 4Kb

    // Methods
    public static void play(final LTESoundIndex index) {
	new SoundResource(index.getURL()).play();
    }

    // Constructor
    protected LTESoundPlayer() {
    }
}
