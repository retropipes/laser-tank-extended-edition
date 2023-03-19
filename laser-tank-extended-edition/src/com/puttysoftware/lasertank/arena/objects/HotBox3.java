/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.arena.abc.ArenaObject;
import com.puttysoftware.lasertank.arena.abc.AbstractMovableObject;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.GameType;
import com.puttysoftware.lasertank.index.Material;

public class HotBox3 extends AbstractMovableObject {
    // Constructors
    public HotBox3() {
        super();
        this.addType(GameType.BOX);
    }

    @Override
    public ArenaObject changesToOnExposure(final Material materialID) {
        return switch (materialID) {
            case ICE -> new Box3();
            default -> this;
        };
    }

    @Override
    public final GameObjectID getID() {
        return GameObjectID.HOT_BOX_3;
    }

    @Override
    public void playSoundHook() {
        Sounds.play(Sound.PUSH_BOX);
    }
}