/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.abstractobjects;

import com.puttysoftware.lasertank.index.GameType;
import com.puttysoftware.lasertank.index.Layer;

public abstract class AbstractField extends AbstractArenaObject {
    // Constructors
    protected AbstractField() {
	super();
	this.addType(GameType.FIELD);
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
    public abstract void postMoveAction(final int dirX, final int dirY, int dirZ);

    @Override
    public void setCustomProperty(final int propID, final int value) {
	// Do nothing
    }
}