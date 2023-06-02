/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.editor.Editor;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameObjectID;

public class Tank extends ArenaObject {
    public Tank(final Direction dir, final int number) {
	super(GameObjectID.TANK, dir, number);
    }

    // Constructors
    public Tank(final int number) {
	super(GameObjectID.TANK, number);
    }

    @Override
    public void editorPlaceHook(final int x, final int y, final int z) {
	final var me = Editor.get();
	me.setPlayerLocation();
    }

    @Override
    public final GameObjectID getID() {
	return GameObjectID.TANK;
    }
}