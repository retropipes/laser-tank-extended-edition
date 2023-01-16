/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.arena.abstractobjects.AbstractPressureButton;
import com.puttysoftware.lasertank.index.GameObjectID;

public class MagneticPressureButton extends AbstractPressureButton {
    // Constructors
    public MagneticPressureButton() {
	super(new MagneticPressureButtonDoor(), false);
    }

    @Override
    public final GameObjectID getID() {
	return GameObjectID.MAGNETIC_PRESSURE_BUTTON;
    }
}