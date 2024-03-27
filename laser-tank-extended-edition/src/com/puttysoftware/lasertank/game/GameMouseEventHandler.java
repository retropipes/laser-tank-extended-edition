package com.puttysoftware.lasertank.game;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.asset.Images;
import com.puttysoftware.lasertank.helper.DirectionHelper;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.LaserType;
import com.puttysoftware.lasertank.utility.TaskRunner;

class GameMouseEventHandler extends MouseAdapter implements Runnable {
    private MouseEvent event;

    public GameMouseEventHandler() {
    }

    private Direction mapMouseToDirection(final MouseEvent me) {
	final var x = me.getX();
	final var y = me.getY();
	final var px = Game.get().getPlayerLocationX();
	final var py = Game.get().getPlayerLocationY();
	final var destX = (int) Math.signum(x / Images.getGraphicSize() - px);
	final var destY = (int) Math.signum(y / Images.getGraphicSize() - py);
	return DirectionHelper.resolveRelative(destX, destY);
    }

    @Override
    public void mouseClicked(final MouseEvent e) {
	this.event = e;
	TaskRunner.runTask(this);
    }

    @Override
    public void run() {
	try {
	    if (this.event.getButton() == MouseEvent.BUTTON1) {
		// Move
		final var dir = GameMouseEventHandler.this.mapMouseToDirection(this.event);
		final var tankDir = Game.get().tank.getDirection();
		if (tankDir != dir) {
		    CommonGameEventHandlers.handleTurns(dir);
		} else {
		    final var x = this.event.getX();
		    final var y = this.event.getY();
		    final var px = Game.get().getPlayerLocationX();
		    final var py = Game.get().getPlayerLocationY();
		    final var destX = (int) Math.signum(x / Images.getGraphicSize() - px);
		    final var destY = (int) Math.signum(y / Images.getGraphicSize() - py);
		    Game.get().updatePositionRelative(destX, destY);
		}
	    } else if (this.event.getButton() == MouseEvent.BUTTON2 || this.event.getButton() == MouseEvent.BUTTON3) {
		// Fire Laser
		Game.get().setLaserType(LaserType.GREEN);
		final var px = Game.get().getPlayerLocationX();
		final var py = Game.get().getPlayerLocationY();
		Game.get().fireLaser(px, py, Game.get().tank);
	    }
	} catch (final Exception ex) {
	    LaserTankEE.logError(ex);
	}
    }
}