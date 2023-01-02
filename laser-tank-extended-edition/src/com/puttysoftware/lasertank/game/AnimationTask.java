/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.game;

import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.arena.Arena;
import com.puttysoftware.lasertank.arena.current.CurrentArenaData;
import com.puttysoftware.lasertank.helper.LayerHelper;
import com.puttysoftware.lasertank.locale.global.GlobalStrings;
import com.puttysoftware.lasertank.locale.global.UntranslatedString;

class AnimationTask extends Thread {
    // Fields
    private boolean stop = false;

    // Constructors
    public AnimationTask() {
	this.setName(GlobalStrings.loadUntranslated(UntranslatedString.ANIMATOR_NAME));
	this.setPriority(Thread.MIN_PRIORITY);
    }

    @Override
    public void run() {
	try {
	    final Arena a = LaserTankEE.getApplication().getArenaManager().getArena();
	    while (!this.stop) {
		final int pz = LaserTankEE.getApplication().getGameManager().getPlayerManager().getPlayerLocationZ();
		final int maxX = a.getRows();
		final int maxY = a.getColumns();
		final int maxW = LayerHelper.COUNT;
		for (int x = 0; x < maxX; x++) {
		    for (int y = 0; y < maxY; y++) {
			for (int w = 0; w < maxW; w++) {
			    synchronized (CurrentArenaData.LOCK_OBJECT) {
				final int oldFN = a.getCell(x, y, pz, w).getFrameNumber();
				a.getCell(x, y, pz, w).toggleFrameNumber();
				final int newFN = a.getCell(x, y, pz, w).getFrameNumber();
				if (oldFN != newFN) {
				    a.markAsDirty(x, y, pz);
				}
			    }
			}
		    }
		}
		LaserTankEE.getApplication().getGameManager().redrawArena();
		try {
		    Thread.sleep(200);
		} catch (final InterruptedException ie) {
		    // Ignore
		}
	    }
	} catch (final Throwable t) {
	    LaserTankEE.logError(t);
	}
    }

    void stopAnimator() {
	this.stop = true;
    }
}
