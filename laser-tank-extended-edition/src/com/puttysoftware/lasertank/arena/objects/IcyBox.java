/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.assets.Sound;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.Material;

public class IcyBox extends ArenaObject {
	// Constructors
	public IcyBox() {
		super();
	}

	@Override
	public ArenaObject changesToOnExposure(final Material materialID) {
		switch (materialID) {
			case FIRE:
				if (this.hasPreviousState()) {
					return this.getPreviousState();
				} else {
					return new ArenaObject(GameObjectID.BOX);
				}
			default:
				return this;
		}
	}

	@Override
	public final GameObjectID getID() {
		return GameObjectID.ICY_BOX;
	}

	@Override
	public Sound laserEnteredSound() {
		return Sound.PUSH_BOX;
	}
}