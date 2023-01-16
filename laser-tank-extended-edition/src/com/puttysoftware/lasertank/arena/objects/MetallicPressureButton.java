/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.arena.abc.AbstractPressureButton;
import com.puttysoftware.lasertank.index.GameObjectID;

public class MetallicPressureButton extends AbstractPressureButton {
    // Constructors
    public MetallicPressureButton() {
	super(new MetallicPressureButtonDoor(), false);
    }

    @Override
    public final GameObjectID getID() {
	return GameObjectID.METALLIC_PRESSURE_BUTTON;
    }
}