/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.abc;

import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.index.GameType;

public abstract class AbstractButtonDoor extends AbstractArenaObject {
    // Constructors
    protected AbstractButtonDoor() {
	super();
	this.addType(GameType.BUTTON_DOOR);
    }

    @Override
    public void editorPlaceHook(final int x, final int y, final int z) {
	final var app = LaserTankEE.getApplication();
	app.getArenaManager().getArena().fullScanButtonBind(x, y, z, this);
	app.getEditor().redrawEditor();
    }

    @Override
    public void editorRemoveHook(final int x, final int y, final int z) {
	final var app = LaserTankEE.getApplication();
	app.getArenaManager().getArena().fullScanFindButtonLostDoor(z, this);
	app.getEditor().redrawEditor();
    }

    @Override
    public int getCustomProperty(final int propID) {
	return AbstractArenaObject.DEFAULT_CUSTOM_VALUE;
    }

    @Override
    public void postMoveAction(final int dirX, final int dirY, final int dirZ) {
	// Do nothing
    }

    @Override
    public void setCustomProperty(final int propID, final int value) {
	// Do nothing
    }
}