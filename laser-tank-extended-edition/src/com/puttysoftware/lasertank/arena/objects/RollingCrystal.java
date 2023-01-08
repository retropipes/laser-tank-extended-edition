/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.arena.abstractobjects.AbstractMovableObject;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.GameType;

public class RollingCrystal extends AbstractMovableObject {
    // Constructors
    public RollingCrystal() {
	super(true);
	this.addType(GameType.BALL);
	this.addType(GameType.ICY);
    }

    @Override
    public final GameObjectID getID() {
	return GameObjectID.ROLLING_CRYSTAL;
    }

    @Override
    public void playSoundHook() {
	Sounds.play(Sound.ROLL);
    }
}