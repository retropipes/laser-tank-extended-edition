/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.abstractobjects;

import com.puttysoftware.lasertank.index.Layer;
import com.puttysoftware.lasertank.index.Material;

public abstract class AbstractInventoryModifier extends AbstractGround {
    // Constructors
    protected AbstractInventoryModifier() {
	this.setMaterial(Material.NONE);
    }

    @Override
    public int getLayer() {
	return Layer.LOWER_OBJECTS.ordinal();
    }
}
