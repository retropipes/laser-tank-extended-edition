package com.puttysoftware.lasertank.game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.arena.ArenaManager;
import com.puttysoftware.lasertank.assets.Images;
import com.puttysoftware.lasertank.assets.Sound;
import com.puttysoftware.lasertank.assets.Sounds;
import com.puttysoftware.lasertank.engine.gui.dialog.CommonDialogs;
import com.puttysoftware.lasertank.helper.DirectionHelper;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameAction;
import com.puttysoftware.lasertank.index.LaserType;
import com.puttysoftware.lasertank.index.RangeType;
import com.puttysoftware.lasertank.locale.GameString;
import com.puttysoftware.lasertank.locale.Strings;
import com.puttysoftware.lasertank.settings.Settings;
import com.puttysoftware.lasertank.utility.TankInventory;

class GameEventHandler implements KeyListener, WindowListener, MouseListener {
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

    /**
     *
     */
    private final Game game;

    public GameEventHandler(final Game theGame) {
	this.game = theGame;
	// Do nothing
    }

    private void handleBlueLasers() {
	try {
	    final var gm = this.game;
	    gm.setLaserType(LaserType.BLUE);
	    final var px = gm.getPlayerLocationX();
	    final var py = gm.getPlayerLocationY();
	    this.game.fireLaser(px, py, gm.tank);
	} catch (final Exception ex) {
	    LaserTankEE.logError(ex);
	}
    }

    private void handleBombs() {
	try {
	    final var gm = this.game;
	    if (!gm.getCheatStatus(Game.CHEAT_BOMBS) && TankInventory.getBombsLeft() > 0
		    || gm.getCheatStatus(Game.CHEAT_BOMBS)) {
		TankInventory.fireBomb();
		gm.fireRange();
	    } else {
		CommonDialogs.showDialog(Strings.loadGame(GameString.OUT_OF_BOMBS));
	    }
	} catch (final Exception ex) {
	    LaserTankEE.logError(ex);
	}
    }

    private void handleBoosts(final KeyEvent e) {
	try {
	    final var gm = this.game;
	    if (!gm.getCheatStatus(Game.CHEAT_BOOSTS) && TankInventory.getBoostsLeft() > 0
		    || gm.getCheatStatus(Game.CHEAT_BOOSTS)) {
		TankInventory.fireBoost();
		final var keyCode = e.getKeyCode();
		switch (keyCode) {
		case KeyEvent.VK_LEFT:
		    gm.updatePositionRelative(-2, 0);
		    break;
		case KeyEvent.VK_DOWN:
		    gm.updatePositionRelative(0, 2);
		    break;
		case KeyEvent.VK_RIGHT:
		    gm.updatePositionRelative(2, 0);
		    break;
		case KeyEvent.VK_UP:
		    gm.updatePositionRelative(0, -2);
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

    private void handleHeatBombs() {
	try {
	    final var gm = this.game;
	    if (!gm.getCheatStatus(Game.CHEAT_HEAT_BOMBS) && TankInventory.getHeatBombsLeft() > 0
		    || gm.getCheatStatus(Game.CHEAT_HEAT_BOMBS)) {
		TankInventory.fireHeatBomb();
		gm.fireRange();
	    } else {
		CommonDialogs.showDialog(Strings.loadGame(GameString.OUT_OF_HEAT_BOMBS));
	    }
	} catch (final Exception ex) {
	    LaserTankEE.logError(ex);
	}
    }

    private void handleIceBombs() {
	try {
	    final var gm = this.game;
	    if (!gm.getCheatStatus(Game.CHEAT_ICE_BOMBS) && TankInventory.getIceBombsLeft() > 0
		    || gm.getCheatStatus(Game.CHEAT_ICE_BOMBS)) {
		TankInventory.fireIceBomb();
		gm.fireRange();
	    } else {
		CommonDialogs.showDialog(Strings.loadGame(GameString.OUT_OF_ICE_BOMBS));
	    }
	} catch (final Exception ex) {
	    LaserTankEE.logError(ex);
	}
    }

    private void handleKeystrokes(final KeyEvent e) {
	if (e.getKeyCode() == KeyEvent.VK_SPACE) {
	    if (e.isAltDown() || e.isAltGraphDown() || e.isControlDown()) {
		switch (this.game.otherAmmoMode) {
		case Game.OTHER_AMMO_MODE_MISSILES:
		    this.handleMissiles();
		    break;
		case Game.OTHER_AMMO_MODE_STUNNERS:
		    this.handleStunners();
		    break;
		case Game.OTHER_AMMO_MODE_BLUE_LASERS:
		    this.handleBlueLasers();
		    break;
		default:
		    break;
		}
	    } else {
		this.handleLasers();
	    }
	} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
	    if (e.isAltDown() || e.isAltGraphDown() || e.isControlDown()) {
		if (this.game.otherRangeMode == RangeType.BOMB) {
		    this.handleBombs();
		} else if (this.game.otherRangeMode == RangeType.HEAT_BOMB) {
		    this.handleHeatBombs();
		} else if (this.game.otherRangeMode == RangeType.ICE_BOMB) {
		    this.handleIceBombs();
		}
	    }
	} else {
	    final var currDir = this.game.tank.getDirection();
	    final var newDir = GameEventHandler.mapKeyToDirection(e);
	    if (currDir != newDir) {
		this.handleTurns(newDir);
	    } else if (e.isAltDown() || e.isAltGraphDown() || e.isControlDown()) {
		if (this.game.otherToolMode == Game.OTHER_TOOL_MODE_BOOSTS) {
		    this.handleBoosts(e);
		} else if (this.game.otherToolMode == Game.OTHER_TOOL_MODE_MAGNETS) {
		    this.handleMagnets(e);
		}
	    } else {
		this.handleMovement(e);
	    }
	}
    }

    private void handleLasers() {
	try {
	    final var gm = this.game;
	    gm.setLaserType(LaserType.GREEN);
	    final var px = gm.getPlayerLocationX();
	    final var py = gm.getPlayerLocationY();
	    this.game.fireLaser(px, py, gm.tank);
	} catch (final Exception ex) {
	    LaserTankEE.logError(ex);
	}
    }

    private void handleMagnets(final KeyEvent e) {
	try {
	    final var gm = this.game;
	    if (!gm.getCheatStatus(Game.CHEAT_MAGNETS) && TankInventory.getMagnetsLeft() > 0
		    || gm.getCheatStatus(Game.CHEAT_MAGNETS)) {
		TankInventory.fireMagnet();
		final var keyCode = e.getKeyCode();
		switch (keyCode) {
		case KeyEvent.VK_LEFT:
		    gm.updatePositionRelative(-3, 0);
		    break;
		case KeyEvent.VK_DOWN:
		    gm.updatePositionRelative(0, 3);
		    break;
		case KeyEvent.VK_RIGHT:
		    gm.updatePositionRelative(3, 0);
		    break;
		case KeyEvent.VK_UP:
		    gm.updatePositionRelative(0, -3);
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

    private void handleMissiles() {
	try {
	    final var gm = this.game;
	    gm.setLaserType(LaserType.MISSILE);
	    final var px = gm.getPlayerLocationX();
	    final var py = gm.getPlayerLocationY();
	    this.game.fireLaser(px, py, gm.tank);
	} catch (final Exception ex) {
	    LaserTankEE.logError(ex);
	}
    }

    private void handleMovement(final KeyEvent e) {
	try {
	    final var gm = this.game;
	    final var keyCode = e.getKeyCode();
	    switch (keyCode) {
	    case KeyEvent.VK_LEFT:
		gm.updatePositionRelative(-1, 0);
		break;
	    case KeyEvent.VK_DOWN:
		gm.updatePositionRelative(0, 1);
		break;
	    case KeyEvent.VK_RIGHT:
		gm.updatePositionRelative(1, 0);
		break;
	    case KeyEvent.VK_UP:
		gm.updatePositionRelative(0, -1);
		break;
	    default:
		break;
	    }
	} catch (final Exception ex) {
	    LaserTankEE.logError(ex);
	}
    }

    private void handleStunners() {
	try {
	    final var gm = this.game;
	    gm.setLaserType(LaserType.STUNNER);
	    final var px = gm.getPlayerLocationX();
	    final var py = gm.getPlayerLocationY();
	    this.game.fireLaser(px, py, gm.tank);
	} catch (final Exception ex) {
	    LaserTankEE.logError(ex);
	}
    }

    private void handleTurns(final Direction dir) {
	try {
	    final var gm = this.game;
	    var fired = false;
	    switch (dir) {
	    case WEST:
		gm.tank.setDirection(Direction.WEST);
		if (!gm.isReplaying()) {
		    gm.updateReplay(GameAction.MOVE, -1, 0);
		}
		fired = true;
		break;
	    case SOUTH:
		gm.tank.setDirection(Direction.SOUTH);
		if (!gm.isReplaying()) {
		    gm.updateReplay(GameAction.MOVE, 0, 1);
		}
		fired = true;
		break;
	    case EAST:
		gm.tank.setDirection(Direction.EAST);
		if (!gm.isReplaying()) {
		    gm.updateReplay(GameAction.MOVE, 1, 0);
		}
		fired = true;
		break;
	    case NORTH:
		gm.tank.setDirection(Direction.NORTH);
		if (!gm.isReplaying()) {
		    gm.updateReplay(GameAction.MOVE, 0, -1);
		}
		fired = true;
		break;
	    default:
		break;
	    }
	    if (fired) {
		Sounds.play(Sound.TURN);
		gm.markTankAsDirty();
		gm.redrawArena();
	    }
	} catch (final Exception ex) {
	    LaserTankEE.logError(ex);
	}
    }

    @Override
    public void keyPressed(final KeyEvent e) {
	new Thread() {
	    @Override
	    public void run() {
		final var a = ArenaManager.get().getArena();
		if ((!a.isMoveShootAllowed() && !GameEventHandler.this.game.laserActive || a.isMoveShootAllowed())
			&& !GameEventHandler.this.game.moving) {
		    if (!Settings.oneMove()) {
			GameEventHandler.this.handleKeystrokes(e);
		    }
		}
	    }
	}.start();
    }

    @Override
    public void keyReleased(final KeyEvent e) {
	new Thread() {
	    @Override
	    public void run() {
		final var a = ArenaManager.get().getArena();
		if ((!a.isMoveShootAllowed() && !GameEventHandler.this.game.laserActive || a.isMoveShootAllowed())
			&& !GameEventHandler.this.game.moving) {
		    if (Settings.oneMove()) {
			GameEventHandler.this.handleKeystrokes(e);
		    }
		}
	    }
	}.start();
    }

    @Override
    public void keyTyped(final KeyEvent e) {
	// Do nothing
    }

    private Direction mapMouseToDirection(final MouseEvent me) {
	final var gm = this.game;
	final var x = me.getX();
	final var y = me.getY();
	final var px = gm.getPlayerLocationX();
	final var py = gm.getPlayerLocationY();
	final var destX = (int) Math.signum(x / Images.getGraphicSize() - px);
	final var destY = (int) Math.signum(y / Images.getGraphicSize() - py);
	return DirectionHelper.resolveRelative(destX, destY);
    }

    @Override
    public void mouseClicked(final MouseEvent e) {
	new Thread() {
	    @Override
	    public void run() {
		try {
		    final var gm = GameEventHandler.this.game;
		    if (e.getButton() == MouseEvent.BUTTON1) {
			// Move
			final var dir = GameEventHandler.this.mapMouseToDirection(e);
			final var tankDir = gm.tank.getDirection();
			if (tankDir != dir) {
			    GameEventHandler.this.handleTurns(dir);
			} else {
			    final var x = e.getX();
			    final var y = e.getY();
			    final var px = gm.getPlayerLocationX();
			    final var py = gm.getPlayerLocationY();
			    final var destX = (int) Math.signum(x / Images.getGraphicSize() - px);
			    final var destY = (int) Math.signum(y / Images.getGraphicSize() - py);
			    gm.updatePositionRelative(destX, destY);
			}
		    } else if (e.getButton() == MouseEvent.BUTTON2 || e.getButton() == MouseEvent.BUTTON3) {
			// Fire Laser
			gm.setLaserType(LaserType.GREEN);
			final var px = gm.getPlayerLocationX();
			final var py = gm.getPlayerLocationY();
			gm.fireLaser(px, py, gm.tank);
		    }
		} catch (final Exception ex) {
		    LaserTankEE.logError(ex);
		}
	    }
	}.start();
    }

    @Override
    public void mouseEntered(final MouseEvent e) {
	// Do nothing
    }

    @Override
    public void mouseExited(final MouseEvent e) {
	// Do nothing
    }

    // handle mouse
    @Override
    public void mousePressed(final MouseEvent e) {
	// Do nothing
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
	// Do nothing
    }

    // Handle windows
    @Override
    public void windowActivated(final WindowEvent we) {
	// Do nothing
    }

    @Override
    public void windowClosed(final WindowEvent we) {
	// Do nothing
    }

    @Override
    public void windowClosing(final WindowEvent we) {
	new Thread() {
	    @Override
	    public void run() {
		try {
		    var success = false;
		    var status = 0;
		    if (ArenaManager.get().getDirty()) {
			status = ArenaManager.showSaveDialog();
			if (status == CommonDialogs.YES_OPTION) {
			    success = ArenaManager.get().saveArena(ArenaManager.get().isArenaProtected());
			    if (success) {
				Game.get().exitGame();
				LaserTankEE.getMainScreen().showGUI();
			    }
			} else if (status == CommonDialogs.NO_OPTION) {
			    Game.get().exitGame();
			    LaserTankEE.getMainScreen().showGUI();
			} else {
			    // Don't stop controls from working
			    final var gm = GameEventHandler.this.game;
			    gm.moving = false;
			    gm.laserActive = false;
			}
		    } else {
			Game.get().exitGame();
			LaserTankEE.getMainScreen().showGUI();
		    }
		} catch (final Exception ex) {
		    LaserTankEE.logError(ex);
		}
	    }
	}.start();
    }

    @Override
    public void windowDeactivated(final WindowEvent we) {
	// Do nothing
    }

    @Override
    public void windowDeiconified(final WindowEvent we) {
	// Do nothing
    }

    @Override
    public void windowIconified(final WindowEvent we) {
	// Do nothing
    }

    @Override
    public void windowOpened(final WindowEvent we) {
	// Do nothing
    }
}