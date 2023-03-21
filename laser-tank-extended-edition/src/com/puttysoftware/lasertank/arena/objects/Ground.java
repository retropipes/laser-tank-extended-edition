/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.arena.ArenaObject;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.Material;

public class Ground extends ArenaObject {
	// Constructors
	public Ground() {
	}

	@Override
	public ArenaObject changesToOnExposure(final Material materialID) {
		return switch (materialID) {
			case ICE -> {
				final var i = new Ice();
				i.setPreviousState(this);
				yield i;
			}
			case FIRE -> new Lava();
			default -> this;
		};
	}

	@Override
	public final GameObjectID getID() {
		return GameObjectID.GROUND;
	}
}