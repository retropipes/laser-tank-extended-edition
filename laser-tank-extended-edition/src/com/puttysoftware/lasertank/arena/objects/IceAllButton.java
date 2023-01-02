/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.arena.abstractobjects.AbstractAllButton;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.Material;

public class IceAllButton extends AbstractAllButton {
    // Constructors
    public IceAllButton() {
	super(new IceAllButtonDoor(), false);
	this.setMaterial(Material.ICE);
    }

    @Override
    public final GameObjectID getStringBaseID() {
	return GameObjectID.ICE_ALL_BUTTON;
    }
}