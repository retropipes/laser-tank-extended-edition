/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.abc;

import com.puttysoftware.lasertank.index.GameType;

public abstract class AbstractPressureButtonDoor extends AbstractButtonDoor {
    // Constructors
    protected AbstractPressureButtonDoor() {
	this.addType(GameType.PRESSURE_BUTTON_DOOR);
    }
}