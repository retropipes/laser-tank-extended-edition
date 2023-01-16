/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.arena.abc.AbstractCharacter;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameObjectID;

public class Tank extends AbstractCharacter {
    public Tank(final Direction dir, final int number) {
	super(number);
	this.setDirection(dir);
    }

    // Constructors
    public Tank(final int number) {
	super(number);
    }

    @Override
    public void editorPlaceHook(final int x, final int y, final int z) {
	final var me = LaserTankEE.getApplication().getEditor();
	me.setPlayerLocation();
    }

    @Override
    public final GameObjectID getID() {
	return GameObjectID.TANK;
    }
}