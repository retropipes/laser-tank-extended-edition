/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.arena.ArenaObject;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameObjectID;

public class PowerfulTank extends ArenaObject {
    public PowerfulTank(final Direction dir, final int number) {
        super(GameObjectID.POWER_TANK, dir, number);
        this.activateTimer(50);
    }

    // Constructors
    public PowerfulTank(final int number) {
        super(GameObjectID.POWER_TANK, number);
        this.activateTimer(50);
    }

    @Override
    public final GameObjectID getID() {
        return GameObjectID.POWER_TANK;
    }

    @Override
    public void timerExpiredAction(final int x, final int y) {
        Sounds.play(Sound.RETURN);
        LaserTankEE.getGame().setNormalTank();
    }
}