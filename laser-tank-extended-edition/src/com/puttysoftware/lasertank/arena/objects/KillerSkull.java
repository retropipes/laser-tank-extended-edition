/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.arena.abstractobjects.AbstractSpell;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
import com.puttysoftware.lasertank.index.GameObjectID;

public class KillerSkull extends AbstractSpell {
    // Constructors
    public KillerSkull() {
	super();
    }

    @Override
    public final GameObjectID getStringBaseID() {
	return GameObjectID.KILLER_SKULL;
    }

    // Scriptability
    @Override
    public void postMoveAction(final int dirX, final int dirY, final int dirZ) {
	Sounds.play(Sound.KILL);
	LaserTankEE.getApplication().getArenaManager().getArena().fullScanKillTanks();
	LaserTankEE.getApplication().getGameManager().morph(new Empty(), dirX, dirY, dirZ, this.getLayer());
    }
}