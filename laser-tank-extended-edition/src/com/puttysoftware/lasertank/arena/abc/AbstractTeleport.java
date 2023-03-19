/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.abc;

import com.puttysoftware.lasertank.index.GameType;

public abstract class AbstractTeleport extends AbstractArenaObject {
    // Constructors
    protected AbstractTeleport() {
        super();
        this.addType(GameType.TELEPORT);
    }

    @Override
    public int getCustomProperty(final int propID) {
        return AbstractArenaObject.DEFAULT_CUSTOM_VALUE;
    }

    public abstract int getDestinationFloor();

    @Override
    public abstract void postMoveAction(final int dirX, final int dirY, int dirZ);

    @Override
    public void setCustomProperty(final int propID, final int value) {
        // Do nothing
    }
}
