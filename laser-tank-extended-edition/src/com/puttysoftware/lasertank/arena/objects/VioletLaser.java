/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.arena.abc.AbstractTransientObject;
import com.puttysoftware.lasertank.index.GameObjectID;

public class VioletLaser extends AbstractTransientObject {
    // Constructors
    public VioletLaser() {
    }

    @Override
    public int getForceUnitsImbued() {
	return 5;
    }

    @Override
    public final GameObjectID getID() {
	return GameObjectID.VIOLET_LASER;
    }
}
