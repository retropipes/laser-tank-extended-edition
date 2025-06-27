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

public class HotterLava extends ArenaObject {
    // Constructors
    public HotterLava() {
    }

    @Override
    public final GameObjectID getID() {
        return GameObjectID.HOTTER_LAVA;
    }

    @Override
    public boolean pushIntoAction(final ArenaObject pushed, final int x, final int y, final int z) {
        if (pushed.getID() == GameObjectID.ICY_BOX) {
            Game.morph(new ArenaObject(GameObjectID.LAVA_BRIDGE), x, y, z, this.layer());
            Sounds.play(Sound.COOL_OFF);
            return true;
        }
        Game.morph(new ArenaObject(GameObjectID.HOT_LAVA), x, y, z, pushed.layer());
        Sounds.play(Sound.MELT);
        return false;
    }
}
