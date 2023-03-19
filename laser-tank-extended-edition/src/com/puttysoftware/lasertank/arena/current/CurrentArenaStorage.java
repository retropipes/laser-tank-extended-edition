/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.current;

import com.puttysoftware.diane.storage.ObjectStorage;
import com.puttysoftware.lasertank.arena.abc.AbstractArenaObject;

class CurrentArenaStorage extends ObjectStorage {
    // Constructor
    public CurrentArenaStorage(final int... shape) {
        super(shape);
    }

    // Methods
    public AbstractArenaObject getArenaDataCell(final int... loc) {
        return (AbstractArenaObject) this.getCell(loc);
    }

    public void setArenaDataCell(final AbstractArenaObject obj, final int... loc) {
        this.setCell(obj, loc);
    }
}
