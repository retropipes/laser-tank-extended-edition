/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.arena.abc.ArenaObject;
import com.puttysoftware.lasertank.arena.abc.AbstractMovableObject;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
import com.puttysoftware.lasertank.index.GameObjectID;

public class StunnedAntiTank extends AbstractMovableObject {
	private static final int STUNNED_START = 10;
	// Fields
	private int stunnedLeft;

	// Constructors
	public StunnedAntiTank() {
		super();
		this.activateTimer(1);
		this.stunnedLeft = StunnedAntiTank.STUNNED_START;
	}

	@Override
	public ArenaObject clone() {
		final var copy = (StunnedAntiTank) super.clone();
		copy.stunnedLeft = this.stunnedLeft;
		return copy;
	}

	@Override
	public final GameObjectID getID() {
		return GameObjectID.STUNNED_ANTI_TANK;
	}

	@Override
	public void playSoundHook() {
		Sounds.play(Sound.PUSH_ANTI_TANK);
	}

	@Override
	public void timerExpiredAction(final int locX, final int locY) {
		this.stunnedLeft--;
		if (this.stunnedLeft == 1) {
			Sounds.play(Sound.RETURN);
			this.activateTimer(1);
		} else if (this.stunnedLeft == 0) {
			final var z = LaserTankEE.getApplication().getGameManager().getPlayerManager().getPlayerLocationZ();
			final var at = new AntiTank();
			at.setSavedObject(this.getSavedObject());
			at.setDirection(this.getDirection());
			LaserTankEE.getApplication().getGameManager().morph(at, locX, locY, z, this.getLayer());
		} else {
			Sounds.play(Sound.STUNNED);
			this.activateTimer(1);
		}
	}
}
