/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.abc;

import com.puttysoftware.lasertank.index.GameType;

public abstract class AbstractGround extends AbstractArenaObject {
    // Constructors
    protected AbstractGround() {
        super();
        this.addType(GameType.GROUND);
    }
}
