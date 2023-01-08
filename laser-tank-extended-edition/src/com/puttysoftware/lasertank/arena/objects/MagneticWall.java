/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.arena.abstractobjects.AbstractWall;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.GameType;
import com.puttysoftware.lasertank.index.Material;

public class MagneticWall extends AbstractWall {
    // Constructors
    public MagneticWall() {
	this.addType(GameType.PLAIN_WALL);
	this.setMaterial(Material.MAGNETIC);
    }

    @Override
    public final GameObjectID getID() {
	return GameObjectID.MAGNETIC_WALL;
    }
}