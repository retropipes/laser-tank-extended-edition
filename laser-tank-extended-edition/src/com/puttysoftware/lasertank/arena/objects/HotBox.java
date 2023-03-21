/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.arena.ArenaObject;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.Material;

public class HotBox extends ArenaObject {
    // Constructors
    public HotBox() {
        super();
    }

    @Override
    public ArenaObject changesToOnExposure(final Material materialID) {
        return switch (materialID) {
            case ICE -> new Box();
            default -> this;
        };
    }

    @Override
    public final GameObjectID getID() {
        return GameObjectID.HOT_BOX;
    }

    @Override
    public Sound laserEnteredSound() {
        return Sound.PUSH_BOX;
    }
}