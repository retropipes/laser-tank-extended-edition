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

public class StrongerAcid extends ArenaObject {
	// Constructors
	public StrongerAcid() {
	}

	@Override
	public ArenaObject changesToOnExposure(final Material materialID) {
		return switch (materialID) {
			case ICE -> {
				final var i = new ArenaObject(GameObjectID.ICE);
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
		if (pushed.isBox()) {
			// Get rid of pushed object
			Game.get().morph(new ArenaObject(GameObjectID.PLACEHOLDER), x, y, z, pushed.getLayer());
			if (pushed.getMaterial() == Material.WOODEN) {
				Game.get().morph(new ArenaObject(GameObjectID.ACID_BRIDGE), x, y, z, this.getLayer());
			} else {
				Game.get().morph(new StrongAcid(), x, y, z, this.getLayer());
			}
		} else {
			Game.get().morph(new ArenaObject(GameObjectID.PLACEHOLDER), x, y, z, pushed.getLayer());
		}
		Sounds.play(Sound.SINK);
		return false;
	}
}
