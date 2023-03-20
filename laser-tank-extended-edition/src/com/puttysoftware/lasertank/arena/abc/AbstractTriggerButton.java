/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.abc;

import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.arena.objects.Empty;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;

public abstract class AbstractTriggerButton extends AbstractButton {
	// Constructors
	protected AbstractTriggerButton(final AbstractTriggerButtonDoor tbd, final boolean isUniversal) {
		super(tbd, isUniversal);
	}

	@Override
	public boolean pushIntoAction(final AbstractMovableObject pushed, final int x, final int y, final int z) {
		final var app = LaserTankEE.getApplication();
		if (this.isUniversal() || pushed.getMaterial() == this.getMaterial()) {
			Sounds.play(Sound.BUTTON);
			if (this.isTriggered()) {
				// Close door at location
				app.getGameManager().morph(this.getButtonDoor(), this.getDoorX(), this.getDoorY(), z, this.getLayer());
				Sounds.play(Sound.DOOR_CLOSES);
				this.setTriggered(false);
			} else {
				// Open door at location
				app.getGameManager().morph(new Empty(), this.getDoorX(), this.getDoorY(), z, this.getLayer());
				Sounds.play(Sound.DOOR_OPENS);
				this.setTriggered(true);
			}
		}
		return true;
	}

	@Override
	public void pushOutAction(final AbstractMovableObject pushed, final int x, final int y, final int z) {
		// Do nothing
	}
}
