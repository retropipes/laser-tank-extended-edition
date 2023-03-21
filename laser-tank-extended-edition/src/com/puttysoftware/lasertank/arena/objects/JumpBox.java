/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import java.awt.Color;

import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.Material;

public class JumpBox extends ArenaObject {
    // Constructors
    public JumpBox() {
    }

    @Override
    public ArenaObject changesToOnExposure(final Material materialID) {
        return switch (materialID) {
            case ICE -> {
                final var ib = new IcyBox();
                ib.setPreviousState(this);
                yield ib;
            }
            case FIRE -> new HotBox();
            default -> this;
        };
    }

    @Override
    public final Color getCustomTextColor() {
        return Color.black;
    }

    @Override
    public final GameObjectID getID() {
        return GameObjectID.JUMP_BOX;
    }
}