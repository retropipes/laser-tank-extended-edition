/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.game;

import com.puttysoftware.lasertank.settings.Settings;

class ReplayTask implements Runnable {
    // Constructors
    public ReplayTask() {
	// Do nothing
    }

    @Override
    public void run() {
	var result = true;
	while (result) {
	    result = Game.replayLastMove();
	    // Delay, for animation purposes
	    try {
		Thread.sleep(Settings.getReplaySpeed());
	    } catch (final InterruptedException ie) {
		// Ignore
	    }
	}
	Game.replayDone();
    }
}
