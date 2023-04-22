/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.assets.Sound;
import com.puttysoftware.lasertank.assets.Sounds;
import com.puttysoftware.lasertank.game.Game;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.Material;

public class Ice extends ArenaObject {
	public Ice() {
	}

	@Override
	public ArenaObject changesToOnExposure(final Material materialID) {
		switch (materialID) {
			case FIRE:
				if (this.hasPreviousState()) {
					return this.getPreviousState();
				} else {
					return new ArenaObject(GameObjectID.GROUND);
				}
			default:
				return this;
		}
	}

	@Override
	public final GameObjectID getID() {
		return GameObjectID.ICE;
	}

	@Override
	public void postMoveActionHook(final int dirX, final int dirY, final int dirZ) {
		Sounds.play(Sound.PUSH_MIRROR);
	}

	@Override
	public boolean pushIntoAction(final ArenaObject pushed, final int x, final int y, final int z) {
		if (pushed instanceof HotBox) {
			final var g = new ArenaObject(GameObjectID.GROUND);
			Game.get().morph(g, x, y, z, g.getLayer());
			Sounds.play(Sound.DEFROST);
		}
		return true;
	}
}
