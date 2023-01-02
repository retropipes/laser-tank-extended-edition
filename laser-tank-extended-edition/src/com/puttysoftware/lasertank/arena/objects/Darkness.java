/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.arena.abstractobjects.AbstractAttribute;
import com.puttysoftware.lasertank.index.GameObjectID;

public class Darkness extends AbstractAttribute {
    // Constructors
    public Darkness() {
	super();
    }

    @Override
    public final GameObjectID getStringBaseID() {
	return GameObjectID.DARKNESS;
    }
}