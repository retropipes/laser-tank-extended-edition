/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.abstractobjects;

import com.puttysoftware.lasertank.index.GameType;
import com.puttysoftware.lasertank.index.Layer;
import com.puttysoftware.lasertank.index.Material;

public abstract class AbstractWall extends AbstractArenaObject {
    // Constructors
    protected AbstractWall() {
	super();
	this.addType(GameType.WALL);
	this.setMaterial(Material.STONE);
    }

    @Override
    public boolean doLasersPassThrough() {
	return false;
    }

    @Override
    public int getCustomProperty(final int propID) {
	return AbstractArenaObject.DEFAULT_CUSTOM_VALUE;
    }

    @Override
    public int getLayer() {
	return Layer.LOWER_OBJECTS.ordinal();
    }

    @Override
    public void postMoveAction(final int dirX, final int dirY, final int dirZ) {
	// Do nothing
    }

    @Override
    public void setCustomProperty(final int propID, final int value) {
	// Do nothing
    }
}