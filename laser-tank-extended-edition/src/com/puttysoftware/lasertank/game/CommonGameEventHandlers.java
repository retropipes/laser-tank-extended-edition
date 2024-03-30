package com.puttysoftware.lasertank.game;

import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameAction;
import com.puttysoftware.lasertank.tasks.AppTaskManager;

class CommonGameEventHandlers {
    static void handleTurns(final Direction dir) {
	try {
	    var fired = false;
	    switch (dir) {
	    case WEST:
		Game.tank.setDirection(Direction.WEST);
		if (!Game.isReplaying()) {
		    Game.updateReplay(GameAction.MOVE, -1, 0);
		}
		fired = true;
		break;
	    case SOUTH:
		Game.tank.setDirection(Direction.SOUTH);
		if (!Game.isReplaying()) {
		    Game.updateReplay(GameAction.MOVE, 0, 1);
		}
		fired = true;
		break;
	    case EAST:
		Game.tank.setDirection(Direction.EAST);
		if (!Game.isReplaying()) {
		    Game.updateReplay(GameAction.MOVE, 1, 0);
		}
		fired = true;
		break;
	    case NORTH:
		Game.tank.setDirection(Direction.NORTH);
		if (!Game.isReplaying()) {
		    Game.updateReplay(GameAction.MOVE, 0, -1);
		}
		fired = true;
		break;
	    default:
		break;
	    }
	    if (fired) {
		Sounds.play(Sound.TURN);
		Game.markTankAsDirty();
		Game.redrawArena();
	    }
	} catch (final Exception ex) {
	    AppTaskManager.error(ex);
	}
    }

    private CommonGameEventHandlers() {
    }
}