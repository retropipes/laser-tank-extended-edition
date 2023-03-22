/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.game;

import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.arena.ArenaLocks;
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
			final var a = LaserTankEE.getArenaManager().getArena();
			while (!this.stop) {
				final var pz = LaserTankEE.getGame().getPlayerLocationZ();
				final var maxX = a.getRows();
				final var maxY = a.getColumns();
				final var maxW = LayerHelper.COUNT;
				for (var x = 0; x < maxX; x++) {
					for (var y = 0; y < maxY; y++) {
						for (var w = 0; w < maxW; w++) {
							synchronized (ArenaLocks.LOCK_OBJECT) {
								final var oldFN = a.getCell(x, y, pz, w).getFrameNumber();
								a.getCell(x, y, pz, w).nextFrame();
								final var newFN = a.getCell(x, y, pz, w).getFrameNumber();
								if (oldFN != newFN) {
									a.markAsDirty(x, y, pz);
								}
							}
						}
					}
				}
				LaserTankEE.getGame().redrawArena();
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
