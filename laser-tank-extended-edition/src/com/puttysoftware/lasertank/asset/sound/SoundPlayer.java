/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.asset.sound;

import com.puttysoftware.lasertank.tasks.AppTaskManager;

public final class SoundPlayer {
    // Constants
    protected static final int EXTERNAL_BUFFER_SIZE = 4096; // 4Kb

    // Methods
    public static void play(final SoundIndex index) {
	AppTaskManager.runTask(new SoundResource(index.getURL()));
    }

    // Constructor
    protected SoundPlayer() {
    }
}
