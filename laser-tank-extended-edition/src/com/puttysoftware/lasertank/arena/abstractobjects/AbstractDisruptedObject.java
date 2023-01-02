/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.abstractobjects;

import com.puttysoftware.lasertank.helper.GameActionHelper;
import com.puttysoftware.lasertank.index.GameAction;

public abstract class AbstractDisruptedObject extends AbstractPassThroughObject {
    // Constructors
    protected AbstractDisruptedObject() {
	super();
    }

    @Override
    public boolean acceptTick(final GameAction actionType) {
	return GameActionHelper.isMove(actionType);
    }
}