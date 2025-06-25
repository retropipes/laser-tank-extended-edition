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

public class HotLava extends ArenaObject {
    // Constructors
    public HotLava() {
    }

    @Override
    public final GameObjectID getID() {
	return GameObjectID.HOT_LAVA;
    }

    @Override
    public boolean pushIntoAction(final ArenaObject pushed, final int x, final int y, final int z) {
	if (pushed.getID() == GameObjectID.ICY_BOX) {
	    Game.morph(new ArenaObject(GameObjectID.LAVA_BRIDGE), x, y, z, this.getLayer());
	    Sounds.play(Sound.COOL_OFF);
	    return true;
	}
	Game.morph(new ArenaObject(GameObjectID.LAVA), x, y, z, pushed.getLayer());
	Sounds.play(Sound.MELT);
	return false;
    }
}
