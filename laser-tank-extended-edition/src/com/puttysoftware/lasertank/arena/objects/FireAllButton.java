/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.arena.abc.AbstractAllButton;
import com.puttysoftware.lasertank.index.GameObjectID;

public class FireAllButton extends AbstractAllButton {
    // Constructors
    public FireAllButton() {
	super(new FireAllButtonDoor(), false);
    }

    @Override
    public final GameObjectID getID() {
	return GameObjectID.FIRE_ALL_BUTTON;
    }
}