/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.arena.abstractobjects.AbstractTransientObject;
import com.puttysoftware.lasertank.index.GameObjectID;

public class RedLaser extends AbstractTransientObject {
    // Constructors
    public RedLaser() {
	super();
    }

    @Override
    public int getForceUnitsImbued() {
	return 1;
    }

    @Override
    public final GameObjectID getStringBaseID() {
	return GameObjectID.RED_LASER;
    }
}
