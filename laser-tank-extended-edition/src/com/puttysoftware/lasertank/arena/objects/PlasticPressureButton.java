/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.arena.abc.AbstractPressureButton;
import com.puttysoftware.lasertank.index.GameObjectID;

public class PlasticPressureButton extends AbstractPressureButton {
    // Constructors
    public PlasticPressureButton() {
        super(new PlasticPressureButtonDoor(), false);
    }

    @Override
    public final GameObjectID getID() {
        return GameObjectID.PLASTIC_PRESSURE_BUTTON;
    }
}