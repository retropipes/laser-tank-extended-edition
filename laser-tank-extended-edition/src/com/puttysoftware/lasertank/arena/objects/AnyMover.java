/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.arena.abc.AbstractGround;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.GameType;

public class AnyMover extends AbstractGround {
    // Constructors
    public AnyMover() {
        this.addType(GameType.MOVER);
        this.addType(GameType.ANTI_MOVER);
        this.addType(GameType.BOX_MOVER);
        this.addType(GameType.MIRROR_MOVER);
    }

    @Override
    public final GameObjectID getID() {
        return GameObjectID.ANY_MOVER;
    }
}