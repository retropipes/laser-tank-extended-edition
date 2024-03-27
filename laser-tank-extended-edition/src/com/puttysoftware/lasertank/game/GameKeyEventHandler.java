package com.puttysoftware.lasertank.game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.arena.ArenaManager;
import com.puttysoftware.lasertank.gui.dialog.CommonDialogs;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.LaserType;
import com.puttysoftware.lasertank.index.RangeType;
import com.puttysoftware.lasertank.locale.GameString;
import com.puttysoftware.lasertank.locale.Strings;
import com.puttysoftware.lasertank.settings.Settings;
import com.puttysoftware.lasertank.utility.TankInventory;
import com.puttysoftware.lasertank.utility.TaskRunner;

class GameKeyEventHandler extends KeyAdapter implements Runnable {
    private static Direction mapKeyToDirection(final KeyEvent e) {
	final var keyCode = e.getKeyCode();
	return switch (keyCode) {
	case KeyEvent.VK_LEFT -> Direction.WEST;
	case KeyEvent.VK_DOWN -> Direction.SOUTH;
	case KeyEvent.VK_RIGHT -> Direction.EAST;
	case KeyEvent.VK_UP -> Direction.NORTH;
	default -> Direction.NONE;
	};
    }

    private KeyEvent event;

    public GameKeyEventHandler() {
    }

    private static void handleBlueLasers() {
	try {
	    Game.setLaserType(LaserType.BLUE);
	    final var px = Game.getPlayerLocationX();
	    final var py = Game.getPlayerLocationY();
	    Game.fireLaser(px, py, Game.tank);
	} catch (final Exception ex) {
	    LaserTankEE.logError(ex);
	}
    }

    private static void handleBombs() {
	try {
	    if (!Game.getCheatStatus(Game.CHEAT_BOMBS) && TankInventory.getBombsLeft() > 0
		    || Game.getCheatStatus(Game.CHEAT_BOMBS)) {
		TankInventory.fireBomb();
		Game.fireRange();
	    } else {
		CommonDialogs.showDialog(Strings.loadGame(GameString.OUT_OF_BOMBS));
	    }
	} catch (final Exception ex) {
	    LaserTankEE.logError(ex);
	}
    }

    private static void handleBoosts(final KeyEvent e) {
	try {
	    if (!Game.getCheatStatus(Game.CHEAT_BOOSTS) && TankInventory.getBoostsLeft() > 0
		    || Game.getCheatStatus(Game.CHEAT_BOOSTS)) {
		TankInventory.fireBoost();
		final var keyCode = e.getKeyCode();
		switch (keyCode) {
		case KeyEvent.VK_LEFT:
		    Game.updatePositionRelative(-2, 0);
		    break;
		case KeyEvent.VK_DOWN:
		    Game.updatePositionRelative(0, 2);
		    break;
		case KeyEvent.VK_RIGHT:
		    Game.updatePositionRelative(2, 0);
		    break;
		case KeyEvent.VK_UP:
		    Game.updatePositionRelative(0, -2);
		    break;
		default:
		    break;
		}
	    } else {
		CommonDialogs.showDialog(Strings.loadGame(GameString.OUT_OF_BOOSTS));
	    }
	} catch (final Exception ex) {
	    LaserTankEE.logError(ex);
	}
    }

    private static void handleHeatBombs() {
	try {
	    if (!Game.getCheatStatus(Game.CHEAT_HEAT_BOMBS) && TankInventory.getHeatBombsLeft() > 0
		    || Game.getCheatStatus(Game.CHEAT_HEAT_BOMBS)) {
		TankInventory.fireHeatBomb();
		Game.fireRange();
	    } else {
		CommonDialogs.showDialog(Strings.loadGame(GameString.OUT_OF_HEAT_BOMBS));
	    }
	} catch (final Exception ex) {
	    LaserTankEE.logError(ex);
	}
    }

    private static void handleIceBombs() {
	try {
	    if (!Game.getCheatStatus(Game.CHEAT_ICE_BOMBS) && TankInventory.getIceBombsLeft() > 0
		    || Game.getCheatStatus(Game.CHEAT_ICE_BOMBS)) {
		TankInventory.fireIceBomb();
		Game.fireRange();
	    } else {
		CommonDialogs.showDialog(Strings.loadGame(GameString.OUT_OF_ICE_BOMBS));
	    }
	} catch (final Exception ex) {
	    LaserTankEE.logError(ex);
	}
    }

    private static void handleKeystrokes(final KeyEvent e) {
	if (e.getKeyCode() == KeyEvent.VK_SPACE) {
	    if (e.isAltDown() || e.isAltGraphDown() || e.isControlDown()) {
		switch (Game.otherAmmoMode) {
		case Game.OTHER_AMMO_MODE_MISSILES:
		    GameKeyEventHandler.handleMissiles();
		    break;
		case Game.OTHER_AMMO_MODE_STUNNERS:
		    GameKeyEventHandler.handleStunners();
		    break;
		case Game.OTHER_AMMO_MODE_BLUE_LASERS:
		    GameKeyEventHandler.handleBlueLasers();
		    break;
		default:
		    break;
		}
	    } else {
		GameKeyEventHandler.handleLasers();
	    }
	} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
	    if (e.isAltDown() || e.isAltGraphDown() || e.isControlDown()) {
		if (Game.otherRangeMode == RangeType.BOMB) {
		    GameKeyEventHandler.handleBombs();
		} else {
		    if (Game.otherRangeMode == RangeType.HEAT_BOMB) {
			GameKeyEventHandler.handleHeatBombs();
		    } else {
			if (Game.otherRangeMode == RangeType.ICE_BOMB) {
			    GameKeyEventHandler.handleIceBombs();
			}
		    }
		}
	    }
	} else {
	    final var currDir = Game.tank.getDirection();
	    final var newDir = GameKeyEventHandler.mapKeyToDirection(e);
	    if (currDir != newDir) {
		CommonGameEventHandlers.handleTurns(newDir);
	    } else if (e.isAltDown() || e.isAltGraphDown() || e.isControlDown()) {
		if (Game.otherToolMode == Game.OTHER_TOOL_MODE_BOOSTS) {
		    GameKeyEventHandler.handleBoosts(e);
		} else {
		    if (Game.otherToolMode == Game.OTHER_TOOL_MODE_MAGNETS) {
			GameKeyEventHandler.handleMagnets(e);
		    }
		}
	    } else {
		GameKeyEventHandler.handleMovement(e);
	    }
	}
    }

    private static void handleLasers() {
	try {
	    Game.setLaserType(LaserType.GREEN);
	    final var px = Game.getPlayerLocationX();
	    final var py = Game.getPlayerLocationY();
	    Game.fireLaser(px, py, Game.tank);
	} catch (final Exception ex) {
	    LaserTankEE.logError(ex);
	}
    }

    private static void handleMagnets(final KeyEvent e) {
	try {
	    if (!Game.getCheatStatus(Game.CHEAT_MAGNETS) && TankInventory.getMagnetsLeft() > 0
		    || Game.getCheatStatus(Game.CHEAT_MAGNETS)) {
		TankInventory.fireMagnet();
		final var keyCode = e.getKeyCode();
		switch (keyCode) {
		case KeyEvent.VK_LEFT:
		    Game.updatePositionRelative(-3, 0);
		    break;
		case KeyEvent.VK_DOWN:
		    Game.updatePositionRelative(0, 3);
		    break;
		case KeyEvent.VK_RIGHT:
		    Game.updatePositionRelative(3, 0);
		    break;
		case KeyEvent.VK_UP:
		    Game.updatePositionRelative(0, -3);
		    break;
		default:
		    break;
		}
	    } else {
		CommonDialogs.showDialog(Strings.loadGame(GameString.OUT_OF_MAGNETS));
	    }
	} catch (final Exception ex) {
	    LaserTankEE.logError(ex);
	}
    }

    private static void handleMissiles() {
	try {
	    Game.setLaserType(LaserType.MISSILE);
	    final var px = Game.getPlayerLocationX();
	    final var py = Game.getPlayerLocationY();
	    Game.fireLaser(px, py, Game.tank);
	} catch (final Exception ex) {
	    LaserTankEE.logError(ex);
	}
    }

    private static void handleMovement(final KeyEvent e) {
	try {
	    final var keyCode = e.getKeyCode();
	    switch (keyCode) {
	    case KeyEvent.VK_LEFT:
		Game.updatePositionRelative(-1, 0);
		break;
	    case KeyEvent.VK_DOWN:
		Game.updatePositionRelative(0, 1);
		break;
	    case KeyEvent.VK_RIGHT:
		Game.updatePositionRelative(1, 0);
		break;
	    case KeyEvent.VK_UP:
		Game.updatePositionRelative(0, -1);
		break;
	    default:
		break;
	    }
	} catch (final Exception ex) {
	    LaserTankEE.logError(ex);
	}
    }

    private static void handleStunners() {
	try {
	    Game.setLaserType(LaserType.STUNNER);
	    final var px = Game.getPlayerLocationX();
	    final var py = Game.getPlayerLocationY();
	    Game.fireLaser(px, py, Game.tank);
	} catch (final Exception ex) {
	    LaserTankEE.logError(ex);
	}
    }

    @Override
    public void keyPressed(final KeyEvent e) {
	this.event = e;
	TaskRunner.runTask(this);
    }

    @Override
    public void keyReleased(final KeyEvent e) {
	this.event = e;
	TaskRunner.runTask(this);
    }

    @Override
    public void run() {
	final var a = ArenaManager.getArena();
	if ((!a.isMoveShootAllowed() && !Game.laserActive || a.isMoveShootAllowed()) && !Game.moving) {
	    if (!Settings.oneMove()) {
		GameKeyEventHandler.handleKeystrokes(this.event);
	    }
	}
    }
}