/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.arena.abc.ArenaObject;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.Material;

public class Box2 extends ArenaObject {
	// Constructors
	public Box2() {
		super();
	}

	@Override
	public ArenaObject changesToOnExposure(final Material materialID) {
		return switch (materialID) {
			case ICE -> {
				final var ib = new IcyBox2();
				ib.setPreviousState(this);
				yield ib;
			}
			case FIRE -> new HotBox2();
			default -> this;
		};
	}

	@Override
	public final GameObjectID getID() {
		return GameObjectID.BOX_2;
	}

	@Override
	public Sound laserEnteredSound() {
		return Sound.PUSH_BOX;
	}
}