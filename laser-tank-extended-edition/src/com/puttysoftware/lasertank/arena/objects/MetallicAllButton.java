/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.arena.abstractobjects.AbstractAllButton;
import com.puttysoftware.lasertank.index.GameObjectID;

public class MetallicAllButton extends AbstractAllButton {
    // Constructors
    public MetallicAllButton() {
	super(new MetallicAllButtonDoor(), false);
    }

    @Override
    public final GameObjectID getID() {
	return GameObjectID.METALLIC_ALL_BUTTON;
    }
}