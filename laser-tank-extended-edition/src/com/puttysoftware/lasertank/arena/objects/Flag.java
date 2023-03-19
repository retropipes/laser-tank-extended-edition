/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.arena.abc.ArenaObject;
import com.puttysoftware.lasertank.arena.abc.AbstractPassThroughObject;
import com.puttysoftware.lasertank.index.GameObjectID;

public class Flag extends AbstractPassThroughObject {
    // Constructors
    public Flag() {
    }

    // Scriptability
    @Override
    public boolean defersSetProperties() {
        return false;
    }

    @Override
    public ArenaObject editorPropertiesHook() {
        return null;
    }

    @Override
    public int getCustomFormat() {
        return 0;
    }

    @Override
    public final GameObjectID getID() {
        return GameObjectID.FLAG;
    }
}