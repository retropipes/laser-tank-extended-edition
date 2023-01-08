/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.abstractobjects;

import com.puttysoftware.lasertank.index.GameType;
import com.puttysoftware.lasertank.index.Layer;

public abstract class AbstractSpell extends AbstractArenaObject {
    // Constructors
    protected AbstractSpell() {
	this.addType(GameType.SPELL);
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
    public void setCustomProperty(final int propID, final int value) {
	// Do nothing
    }
}
