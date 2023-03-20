/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.abc;

import com.puttysoftware.lasertank.LaserTankEE;

public abstract class AbstractButtonDoor extends ArenaObject {
    // Constructors
    protected AbstractButtonDoor() {
        super();
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
}