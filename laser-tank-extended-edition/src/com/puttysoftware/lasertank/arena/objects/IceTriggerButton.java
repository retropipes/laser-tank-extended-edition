/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.arena.abc.AbstractTriggerButton;
import com.puttysoftware.lasertank.index.GameObjectID;

public class IceTriggerButton extends AbstractTriggerButton {
    // Constructors
    public IceTriggerButton() {
        super(new IceTriggerButtonDoor(), false);
    }

    @Override
    public final GameObjectID getID() {
        return GameObjectID.ICE_TRIGGER_BUTTON;
    }
}