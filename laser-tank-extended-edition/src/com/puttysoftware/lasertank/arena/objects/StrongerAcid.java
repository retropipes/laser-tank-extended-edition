/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.arena.ArenaObject;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.Material;

public class StrongerAcid extends ArenaObject {
	// Constructors
	public StrongerAcid() {
	}

	@Override
	public ArenaObject changesToOnExposure(final Material materialID) {
		return switch (materialID) {
			case ICE -> {
				final var i = new Ice();
				i.setPreviousState(this);
				yield i;
			}
			case FIRE -> new StrongAcid();
			default -> this;
		};
	}

	@Override
	public final GameObjectID getID() {
		return GameObjectID.STRONGER_ACID;
	}

	// Scriptability
	@Override
	public boolean pushIntoAction(final ArenaObject pushed, final int x, final int y, final int z) {
		final var app = LaserTankEE.getApplication();
		if (pushed.isBox()) {
			// Get rid of pushed object
			app.getGameManager().morph(new Empty(), x, y, z, pushed.getLayer());
			if (pushed.getMaterial() == Material.WOODEN) {
				app.getGameManager().morph(new AcidBridge(), x, y, z, this.getLayer());
			} else {
				app.getGameManager().morph(new StrongAcid(), x, y, z, this.getLayer());
			}
		} else {
			app.getGameManager().morph(new Empty(), x, y, z, pushed.getLayer());
		}
		Sounds.play(Sound.SINK);
		return false;
	}
}
