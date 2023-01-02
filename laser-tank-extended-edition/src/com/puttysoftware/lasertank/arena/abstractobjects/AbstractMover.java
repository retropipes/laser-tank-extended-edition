/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.abstractobjects;

import com.puttysoftware.lasertank.index.Layer;

public abstract class AbstractMover extends AbstractGround {
    // Constructors
    protected AbstractMover() {
	super();
    }

    protected AbstractMover(final boolean hasFriction) {
	super(hasFriction);
    }

    @Override
    public int getLayer() {
	return Layer.UPPER_GROUND.ordinal();
    }
}
