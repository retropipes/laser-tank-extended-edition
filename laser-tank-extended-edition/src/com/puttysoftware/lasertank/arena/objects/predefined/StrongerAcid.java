/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects.predefined;

import com.puttysoftware.lasertank.arena.objects.ArenaObject;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
import com.puttysoftware.lasertank.game.Game;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.Material;

public class StrongerAcid extends ArenaObject {
    // Constructors
    public StrongerAcid() {
    }

    @Override
    public final GameObjectID getID() {
	return GameObjectID.STRONGER_ACID;
    }

    @Override
    public boolean pushIntoAction(final ArenaObject pushed, final int x, final int y, final int z) {
	if (pushed.isBox()) {
	    // Get rid of pushed object
	    Game.morph(new ArenaObject(GameObjectID.PLACEHOLDER), x, y, z, pushed.layer());
	    if (pushed.material() == Material.WOODEN) {
		Game.morph(new ArenaObject(GameObjectID.ACID_BRIDGE), x, y, z, this.layer());
	    } else {
		Game.morph(new ArenaObject(GameObjectID.STRONG_ACID), x, y, z, this.layer());
	    }
	} else {
	    Game.morph(new ArenaObject(GameObjectID.PLACEHOLDER), x, y, z, pushed.layer());
	}
	Sounds.play(Sound.SINK);
	return false;
    }
}
