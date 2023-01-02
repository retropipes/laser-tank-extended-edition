/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.abstractobjects;

import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
import com.puttysoftware.lasertank.index.GameType;

public abstract class AbstractAllButton extends AbstractButton {
    // Constructors
    protected AbstractAllButton(final AbstractAllButtonDoor abd, final boolean isUniversal) {
	super(abd, isUniversal);
	this.addType(GameType.ALL_BUTTON);
    }

    @Override
    public boolean pushIntoAction(final AbstractMovableObject pushed, final int x, final int y, final int z) {
	if (this.isUniversal() || pushed.getMaterial() == this.getMaterial()) {
	    Sounds.play(Sound.BUTTON);
	    if (!this.isTriggered()) {
		// Check to open door at location
		this.setTriggered(true);
		LaserTankEE.getApplication().getArenaManager().getArena().fullScanAllButtonOpen(z, this);
	    }
	}
	return true;
    }

    @Override
    public void pushOutAction(final AbstractMovableObject pushed, final int x, final int y, final int z) {
	if (this.isUniversal() || pushed.getMaterial() == this.getMaterial()) {
	    if (this.isTriggered()) {
		// Check to close door at location
		this.setTriggered(false);
		LaserTankEE.getApplication().getArenaManager().getArena().fullScanAllButtonClose(z, this);
	    }
	}
    }
}
