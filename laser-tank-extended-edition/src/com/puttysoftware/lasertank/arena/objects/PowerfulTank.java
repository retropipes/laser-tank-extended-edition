/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.arena.abstractobjects.AbstractCharacter;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
import com.puttysoftware.lasertank.helper.GameActionHelper;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameAction;
import com.puttysoftware.lasertank.index.GameObjectID;

public class PowerfulTank extends AbstractCharacter {
    public PowerfulTank(final Direction dir, final int number) {
	super(number);
	this.setDirection(dir);
	this.activateTimer(50);
    }

    // Constructors
    public PowerfulTank(final int number) {
	super(number);
	this.activateTimer(50);
    }

    @Override
    public boolean acceptTick(final GameAction actionType) {
	return GameActionHelper.isMove(actionType);
    }

    @Override
    public final GameObjectID getStringBaseID() {
	return GameObjectID.POWER_TANK;
    }

    @Override
    public void timerExpiredAction(final int x, final int y) {
	Sounds.play(Sound.RETURN);
	LaserTankEE.getApplication().getGameManager().setNormalTank();
    }
}