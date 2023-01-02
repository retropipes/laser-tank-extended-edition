/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.arena.abstractobjects.AbstractPassThroughObject;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.GameType;
import com.puttysoftware.lasertank.index.Layer;

public class UpperObjectsEmpty extends AbstractPassThroughObject {
    // Constructors
    public UpperObjectsEmpty() {
	super();
	this.addType(GameType.EMPTY_SPACE);
    }

    @Override
    public int getLayer() {
	return Layer.UPPER_OBJECTS.ordinal();
    }

    @Override
    public final GameObjectID getStringBaseID() {
	return GameObjectID.PLACEHOLDER;
    }
}