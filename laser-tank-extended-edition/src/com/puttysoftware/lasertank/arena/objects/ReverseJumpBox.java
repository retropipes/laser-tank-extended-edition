/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import java.awt.Color;

import com.puttysoftware.lasertank.arena.abstractobjects.AbstractArenaObject;
import com.puttysoftware.lasertank.arena.abstractobjects.AbstractJumpObject;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.GameType;
import com.puttysoftware.lasertank.index.Material;

public class ReverseJumpBox extends AbstractJumpObject {
    // Constructors
    public ReverseJumpBox() {
	super();
	this.addType(GameType.BOX);
	this.setMaterial(Material.STONE);
    }

    @Override
    public AbstractArenaObject changesToOnExposure(final Material materialID) {
	switch (materialID) {
	case ICE:
	    final IcyBox ib = new IcyBox();
	    ib.setPreviousState(this);
	    return ib;
	case FIRE:
	    return new HotBox();
	default:
	    return this;
	}
    }

    @Override
    public int getActualJumpCols() {
	return -super.getActualJumpCols();
    }

    @Override
    public int getActualJumpRows() {
	return -super.getActualJumpRows();
    }

    @Override
    public final Color getCustomTextColor() {
	return Color.black;
    }

    @Override
    public final GameObjectID getStringBaseID() {
	return GameObjectID.REVERSE_JUMP_BOX;
    }
}