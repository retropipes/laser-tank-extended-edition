/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.arena.abc.ArenaObject;
import com.puttysoftware.lasertank.arena.abc.AbstractAttribute;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.GameType;
import com.puttysoftware.lasertank.index.LaserType;

public class Cloak extends AbstractAttribute {
    // Constructors
    public Cloak() {
        this.addType(GameType.CLOAK);
    }

    @Override
    public ArenaObject attributeGameRenderHook() {
        return new Empty();
    }

    @Override
    public final GameObjectID getID() {
        return GameObjectID.CLOAK;
    }

    @Override
    public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
            final LaserType laserType, final int forceUnits) {
        final var app = LaserTankEE.getApplication();
        app.getGameManager().morph(new Empty(), locX, locY, locZ, this.getLayer());
        Sounds.play(Sound.CHANGE);
        return Direction.NONE;
    }

    @Override
    public void moveFailedAction(final int locX, final int locY, final int locZ) {
        final var app = LaserTankEE.getApplication();
        app.getGameManager().morph(new Empty(), locX, locY, locZ, this.getLayer());
        Sounds.play(Sound.CHANGE);
    }

    @Override
    public void postMoveActionHook(final int locX, final int locY, final int locZ) {
        final var app = LaserTankEE.getApplication();
        app.getGameManager().morph(new Empty(), locX, locY, locZ, this.getLayer());
        Sounds.play(Sound.CHANGE);
    }
}