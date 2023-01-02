/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.abstractobjects;

import com.puttysoftware.lasertank.index.GameType;
import com.puttysoftware.lasertank.index.Layer;

public abstract class AbstractAttribute extends AbstractPassThroughObject {
    // Constructors
    protected AbstractAttribute() {
	super();
	this.addType(GameType.ATTRIBUTE);
    }

    @Override
    public int getLayer() {
	return Layer.UPPER_OBJECTS.ordinal();
    }
}