package com.puttysoftware.lasertank.game;

import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameAction;

class CommonGameEventHandlers {
    static void handleTurns(final Direction dir) {
	try {
	    var fired = false;
	    switch (dir) {
	    case WEST:
		Game.get().tank.setDirection(Direction.WEST);
		if (!Game.get().isReplaying()) {
		    Game.get().updateReplay(GameAction.MOVE, -1, 0);
		}
		fired = true;
		break;
	    case SOUTH:
		Game.get().tank.setDirection(Direction.SOUTH);
		if (!Game.get().isReplaying()) {
		    Game.get().updateReplay(GameAction.MOVE, 0, 1);
		}
		fired = true;
		break;
	    case EAST:
		Game.get().tank.setDirection(Direction.EAST);
		if (!Game.get().isReplaying()) {
		    Game.get().updateReplay(GameAction.MOVE, 1, 0);
		}
		fired = true;
		break;
	    case NORTH:
		Game.get().tank.setDirection(Direction.NORTH);
		if (!Game.get().isReplaying()) {
		    Game.get().updateReplay(GameAction.MOVE, 0, -1);
		}
		fired = true;
		break;
	    default:
		break;
	    }
	    if (fired) {
		Sounds.play(Sound.TURN);
		Game.get().markTankAsDirty();
		Game.get().redrawArena();
	    }
	} catch (final Exception ex) {
	    LaserTankEE.logError(ex);
	}
    }

    private CommonGameEventHandlers() {
    }
}