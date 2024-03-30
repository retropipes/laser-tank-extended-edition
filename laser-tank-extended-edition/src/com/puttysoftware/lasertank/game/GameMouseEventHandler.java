package com.puttysoftware.lasertank.game;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.puttysoftware.lasertank.asset.Images;
import com.puttysoftware.lasertank.helper.DirectionHelper;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.LaserType;
import com.puttysoftware.lasertank.tasks.AppTaskManager;

class GameMouseEventHandler extends MouseAdapter implements Runnable {
    private MouseEvent event;

    public GameMouseEventHandler() {
    }

    private static Direction mapMouseToDirection(final MouseEvent me) {
	final var x = me.getX();
	final var y = me.getY();
	final var px = Game.getPlayerLocationX();
	final var py = Game.getPlayerLocationY();
	final var destX = (int) Math.signum(x / Images.getGraphicSize() - px);
	final var destY = (int) Math.signum(y / Images.getGraphicSize() - py);
	return DirectionHelper.resolveRelative(destX, destY);
    }

    @Override
    public void mouseClicked(final MouseEvent e) {
	this.event = e;
	AppTaskManager.runTask(this);
    }

    @Override
    public void run() {
	try {
	    if (this.event.getButton() == MouseEvent.BUTTON1) {
		// Move
		final var dir = GameMouseEventHandler.mapMouseToDirection(this.event);
		final var tankDir = Game.tank.getDirection();
		if (tankDir != dir) {
		    CommonGameEventHandlers.handleTurns(dir);
		} else {
		    final var x = this.event.getX();
		    final var y = this.event.getY();
		    final var px = Game.getPlayerLocationX();
		    final var py = Game.getPlayerLocationY();
		    final var destX = (int) Math.signum(x / Images.getGraphicSize() - px);
		    final var destY = (int) Math.signum(y / Images.getGraphicSize() - py);
		    Game.updatePositionRelative(destX, destY);
		}
	    } else if (this.event.getButton() == MouseEvent.BUTTON2 || this.event.getButton() == MouseEvent.BUTTON3) {
		// Fire Laser
		Game.setLaserType(LaserType.GREEN);
		final var px = Game.getPlayerLocationX();
		final var py = Game.getPlayerLocationY();
		Game.fireLaser(px, py, Game.tank);
	    }
	} catch (final Exception ex) {
	    AppTaskManager.error(ex);
	}
    }
}