/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects.predefined;

import java.awt.Color;

import com.puttysoftware.lasertank.arena.objects.ArenaObject;
import com.puttysoftware.lasertank.index.GameObjectID;

public class ReverseJumpBox extends ArenaObject {
    // Constructors
    public ReverseJumpBox() {
    }

    @Override
    public int getColumnsToJump() {
        return -super.getColumnsToJump();
    }

    @Override
    public final Color getCustomTextColor() {
        return Color.black;
    }

    @Override
    public final GameObjectID getID() {
        return GameObjectID.REVERSE_JUMP_BOX;
    }

    @Override
    public int getRowsToJump() {
        return -super.getRowsToJump();
    }
}