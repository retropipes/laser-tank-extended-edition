/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects.predefined;

import com.puttysoftware.lasertank.arena.objects.ArenaObject;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.index.GameObjectID;

public class RollingCrystal extends ArenaObject {
    // Constructors
    public RollingCrystal() {
    }

    @Override
    public final GameObjectID getID() {
	return GameObjectID.ROLLING_CRYSTAL;
    }

    @Override
    public Sound laserEnteredSound() {
	return Sound.ROLL;
    }
}