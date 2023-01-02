/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.arena.abstractobjects.AbstractPressureButton;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.Material;

public class MetallicPressureButton extends AbstractPressureButton {
    // Constructors
    public MetallicPressureButton() {
	super(new MetallicPressureButtonDoor(), false);
	this.setMaterial(Material.METALLIC);
    }

    @Override
    public final GameObjectID getStringBaseID() {
	return GameObjectID.METALLIC_PRESSURE_BUTTON;
    }
}