/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.arena.abc.AbstractPassThroughObject;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.GameType;

public class Empty extends AbstractPassThroughObject {
    // Constructors
    public Empty() {
	this.addType(GameType.EMPTY_SPACE);
    }

    @Override
    public final GameObjectID getID() {
	return GameObjectID.PLACEHOLDER;
    }
}