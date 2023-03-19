/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.arena.abc.AbstractGround;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.utility.TankInventory;

public class TenStunners extends AbstractGround {
    // Constructors
    public TenStunners() {
    }

    @Override
    public boolean doLasersPassThrough() {
        return true;
    }

    @Override
    public final GameObjectID getID() {
        return GameObjectID.TEN_STUNNERS;
    }

    @Override
    public void postMoveAction(final int dirX, final int dirY, final int dirZ) {
        final var gm = LaserTankEE.getApplication().getGameManager();
        TankInventory.addTenStunners();
        gm.morph(new Empty(), dirX, dirY, dirZ, this.getLayer());
    }
}
