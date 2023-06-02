/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.assets.Sound;
import com.puttysoftware.lasertank.assets.Sounds;
import com.puttysoftware.lasertank.game.Game;
import com.puttysoftware.lasertank.index.GameObjectID;

public class StunnedAntiTank extends ArenaObject {
    private static final int STUNNED_START = 10;
    // Fields
    private int stunnedLeft;

    // Constructors
    public StunnedAntiTank() {
	this.activateTimer(1);
	this.stunnedLeft = StunnedAntiTank.STUNNED_START;
    }

    @Override
    public ArenaObject clone() {
	final var copy = (StunnedAntiTank) super.clone();
	copy.stunnedLeft = this.stunnedLeft;
	return copy;
    }

    @Override
    public final GameObjectID getID() {
	return GameObjectID.STUNNED_ANTI_TANK;
    }

    @Override
    public Sound laserEnteredSound() {
	return Sound.PUSH_ANTI_TANK;
    }

    @Override
    public void timerExpiredAction(final int locX, final int locY) {
	this.stunnedLeft--;
	if (this.stunnedLeft == 1) {
	    Sounds.play(Sound.RETURN);
	    this.activateTimer(1);
	} else if (this.stunnedLeft == 0) {
	    final var z = Game.get().getPlayerLocationZ();
	    final var at = new ArenaObject(GameObjectID.ANTI_TANK);
	    at.setSavedObject(this.getSavedObject());
	    at.setDirection(this.getDirection());
	    Game.get().morph(at, locX, locY, z, this.getLayer());
	} else {
	    Sounds.play(Sound.STUNNED);
	    this.activateTimer(1);
	}
    }
}
