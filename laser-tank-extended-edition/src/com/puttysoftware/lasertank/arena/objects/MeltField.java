/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.arena.abc.AbstractField;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
import com.puttysoftware.lasertank.index.GameObjectID;

public class MeltField extends AbstractField {
    // Constructors
    public MeltField() {
    }

    @Override
    public final GameObjectID getID() {
	return GameObjectID.MELT_FIELD;
    }

    @Override
    public void postMoveAction(final int dirX, final int dirY, final int dirZ) {
	Sounds.play(Sound.MELT);
	LaserTankEE.getApplication().getGameManager().updatePositionRelativeMolten();
    }
}