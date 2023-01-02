/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.arena.abstractobjects.AbstractAllButton;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.Material;

public class PlasticAllButton extends AbstractAllButton {
    // Constructors
    public PlasticAllButton() {
	super(new PlasticAllButtonDoor(), false);
	this.setMaterial(Material.PLASTIC);
    }

    @Override
    public final GameObjectID getStringBaseID() {
	return GameObjectID.PLASTIC_ALL_BUTTON;
    }
}