/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects.predefined;

import java.awt.Color;

import com.puttysoftware.lasertank.arena.objects.ArenaObject;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.Material;

public class ReverseJumpBox extends ArenaObject {
    // Constructors
    public ReverseJumpBox() {
    }

    @Override
    public ArenaObject changesToOnExposure(final Material materialID) {
	return switch (materialID) {
	case ICE -> {
	    final var ib = new ArenaObject(GameObjectID.ICY_BOX);
	    ib.setPreviousState(this);
	    yield ib;
	}
	case FIRE -> new ArenaObject(GameObjectID.HOT_BOX);
	default -> this;
	};
    }

    @Override
    public int getColumnsToJump() {
	return -super.getColumnsToJump();
    }

    @Override
    public final Color getCustomTextColor() {
	return Color.black;
    }

    @Override
    public final GameObjectID getID() {
	return GameObjectID.REVERSE_JUMP_BOX;
    }

    @Override
    public int getRowsToJump() {
	return -super.getRowsToJump();
    }
}