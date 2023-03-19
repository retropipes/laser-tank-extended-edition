/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.arena.abc.AbstractTriggerButton;
import com.puttysoftware.lasertank.index.GameObjectID;

public class StoneTriggerButton extends AbstractTriggerButton {
    // Constructors
    public StoneTriggerButton() {
        super(new StoneTriggerButtonDoor(), false);
    }

    @Override
    public final GameObjectID getID() {
        return GameObjectID.STONE_TRIGGER_BUTTON;
    }
}