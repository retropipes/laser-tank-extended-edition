/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.game;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.WindowConstants;

import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.arena.ArenaManager;
import com.puttysoftware.lasertank.arena.HistoryStatus;
import com.puttysoftware.lasertank.arena.objects.ArenaObject;
import com.puttysoftware.lasertank.assets.Images;
import com.puttysoftware.lasertank.assets.Music;
import com.puttysoftware.lasertank.assets.Musics;
import com.puttysoftware.lasertank.assets.Sound;
import com.puttysoftware.lasertank.assets.Sounds;
import com.puttysoftware.lasertank.cheat.Cheats;
import com.puttysoftware.lasertank.datatype.FileExtensions;
import com.puttysoftware.lasertank.engine.fileio.DataIOReader;
import com.puttysoftware.lasertank.engine.fileio.DataIOWriter;
import com.puttysoftware.lasertank.engine.fileio.XDataReader;
import com.puttysoftware.lasertank.engine.fileio.XDataWriter;
import com.puttysoftware.lasertank.engine.gui.Screen;
import com.puttysoftware.lasertank.engine.gui.dialog.CommonDialogs;
import com.puttysoftware.lasertank.helper.DifficultyHelper;
import com.puttysoftware.lasertank.helper.DirectionHelper;
import com.puttysoftware.lasertank.helper.RangeTypeHelper;
import com.puttysoftware.lasertank.index.Difficulty;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameAction;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.LaserType;
import com.puttysoftware.lasertank.index.Layer;
import com.puttysoftware.lasertank.index.RangeType;
import com.puttysoftware.lasertank.locale.CommonString;
import com.puttysoftware.lasertank.locale.DialogString;
import com.puttysoftware.lasertank.locale.ErrorString;
import com.puttysoftware.lasertank.locale.GameString;
import com.puttysoftware.lasertank.locale.Strings;
import com.puttysoftware.lasertank.locale.global.GlobalStrings;
import com.puttysoftware.lasertank.locale.global.UntranslatedString;
import com.puttysoftware.lasertank.settings.Settings;
import com.puttysoftware.lasertank.utility.AlreadyDeadException;
import com.puttysoftware.lasertank.utility.CustomDialogs;
import com.puttysoftware.lasertank.utility.InvalidArenaException;
import com.puttysoftware.lasertank.utility.RCLGenerator;
import com.puttysoftware.lasertank.utility.TankInventory;

public class Game extends Screen {
    private class DifficultyEventHandler implements ActionListener, WindowListener {
	public DifficultyEventHandler() {
	    // Do nothing
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
	    final var cmd = e.getActionCommand();
	    final var gm = Game.this;
	    if (cmd.equals(Strings.loadDialog(DialogString.OK_BUTTON))) {
		gm.okButtonClicked();
	    } else {
		gm.cancelButtonClicked();
	    }
	}

	@Override
	public void windowActivated(final WindowEvent e) {
	    // Ignore
	}

	@Override
	public void windowClosed(final WindowEvent e) {
	    // Ignore
	}

	@Override
	public void windowClosing(final WindowEvent e) {
	    Game.this.cancelButtonClicked();
	}

	@Override
	public void windowDeactivated(final WindowEvent e) {
	    // Ignore
	}

	@Override
	public void windowDeiconified(final WindowEvent e) {
	    // Ignore
	}

	@Override
	public void windowIconified(final WindowEvent e) {
	    // Ignore
	}

	@Override
	public void windowOpened(final WindowEvent e) {
	    // Ignore
	}
    }

    private class EventHandler implements KeyListener, WindowListener, MouseListener {
	public EventHandler() {
	    // Do nothing
	}

	public void handleBlueLasers() {
	    try {
		final var gm = Game.this;
		gm.setLaserType(LaserType.BLUE);
		final var px = gm.getPlayerLocationX();
		final var py = gm.getPlayerLocationY();
		Game.this.fireLaser(px, py, gm.tank);
	    } catch (final Exception ex) {
		LaserTankEE.logError(ex);
	    }
	}

	public void handleBombs() {
	    try {
		final var gm = Game.this;
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

	public void handleBoosts(final KeyEvent e) {
	    try {
		final var gm = Game.this;
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

	public void handleHeatBombs() {
	    try {
		final var gm = Game.this;
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

	public void handleIceBombs() {
	    try {
		final var gm = Game.this;
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
		    switch (Game.this.otherAmmoMode) {
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
		    if (Game.this.otherRangeMode == RangeType.BOMB) {
			this.handleBombs();
		    } else if (Game.this.otherRangeMode == RangeType.HEAT_BOMB) {
			this.handleHeatBombs();
		    } else if (Game.this.otherRangeMode == RangeType.ICE_BOMB) {
			this.handleIceBombs();
		    }
		}
	    } else {
		final var currDir = Game.this.tank.getDirection();
		final var newDir = this.mapKeyToDirection(e);
		if (currDir != newDir) {
		    this.handleTurns(newDir);
		} else if (e.isAltDown() || e.isAltGraphDown() || e.isControlDown()) {
		    if (Game.this.otherToolMode == Game.OTHER_TOOL_MODE_BOOSTS) {
			this.handleBoosts(e);
		    } else if (Game.this.otherToolMode == Game.OTHER_TOOL_MODE_MAGNETS) {
			this.handleMagnets(e);
		    }
		} else {
		    this.handleMovement(e);
		}
	    }
	}

	public void handleLasers() {
	    try {
		final var gm = Game.this;
		gm.setLaserType(LaserType.GREEN);
		final var px = gm.getPlayerLocationX();
		final var py = gm.getPlayerLocationY();
		Game.this.fireLaser(px, py, gm.tank);
	    } catch (final Exception ex) {
		LaserTankEE.logError(ex);
	    }
	}

	public void handleMagnets(final KeyEvent e) {
	    try {
		final var gm = Game.this;
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

	public void handleMissiles() {
	    try {
		final var gm = Game.this;
		gm.setLaserType(LaserType.MISSILE);
		final var px = gm.getPlayerLocationX();
		final var py = gm.getPlayerLocationY();
		Game.this.fireLaser(px, py, gm.tank);
	    } catch (final Exception ex) {
		LaserTankEE.logError(ex);
	    }
	}

	public void handleMovement(final KeyEvent e) {
	    try {
		final var gm = Game.this;
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

	public void handleStunners() {
	    try {
		final var gm = Game.this;
		gm.setLaserType(LaserType.STUNNER);
		final var px = gm.getPlayerLocationX();
		final var py = gm.getPlayerLocationY();
		Game.this.fireLaser(px, py, gm.tank);
	    } catch (final Exception ex) {
		LaserTankEE.logError(ex);
	    }
	}

	public void handleTurns(final Direction dir) {
	    try {
		final var gm = Game.this;
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
	    final var a = ArenaManager.get().getArena();
	    if ((!a.isMoveShootAllowed() && !Game.this.laserActive || a.isMoveShootAllowed()) && !Game.this.moving) {
		if (!Settings.oneMove()) {
		    this.handleKeystrokes(e);
		}
	    }
	}

	@Override
	public void keyReleased(final KeyEvent e) {
	    final var a = ArenaManager.get().getArena();
	    if ((!a.isMoveShootAllowed() && !Game.this.laserActive || a.isMoveShootAllowed()) && !Game.this.moving) {
		if (Settings.oneMove()) {
		    this.handleKeystrokes(e);
		}
	    }
	}

	@Override
	public void keyTyped(final KeyEvent e) {
	    // Do nothing
	}

	public Direction mapKeyToDirection(final KeyEvent e) {
	    final var keyCode = e.getKeyCode();
	    return switch (keyCode) {
	    case KeyEvent.VK_LEFT -> Direction.WEST;
	    case KeyEvent.VK_DOWN -> Direction.SOUTH;
	    case KeyEvent.VK_RIGHT -> Direction.EAST;
	    case KeyEvent.VK_UP -> Direction.NORTH;
	    default -> Direction.NONE;
	    };
	}

	public Direction mapMouseToDirection(final MouseEvent me) {
	    final var gm = Game.this;
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
	    try {
		final var gm = Game.this;
		if (e.getButton() == MouseEvent.BUTTON1) {
		    // Move
		    final var dir = this.mapMouseToDirection(e);
		    final var tankDir = gm.tank.getDirection();
		    if (tankDir != dir) {
			this.handleTurns(dir);
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
			final var gm = Game.this;
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

    private static class FocusHandler implements WindowFocusListener {
	public FocusHandler() {
	    // Do nothing
	}

	@Override
	public void windowGainedFocus(final WindowEvent e) {
	    LaserTankEE.getMenus().updateMenuItemState();
	}

	@Override
	public void windowLostFocus(final WindowEvent e) {
	    // Do nothing
	}
    }

    private static Game instance;
    private static final int OTHER_AMMO_MODE_MISSILES = 0;
    private static final int OTHER_AMMO_MODE_STUNNERS = 1;
    private static final int OTHER_AMMO_MODE_BLUE_LASERS = 2;
    private static final int OTHER_AMMO_MODE_DISRUPTORS = 3;
    private static final int OTHER_TOOL_MODE_BOOSTS = 0;
    private static final int OTHER_TOOL_MODE_MAGNETS = 1;
    static final int CHEAT_SWIMMING = 0;
    static final int CHEAT_GHOSTLY = 1;
    static final int CHEAT_INVINCIBLE = 2;
    private static final int CHEAT_MISSILES = 3;
    private static final int CHEAT_STUNNERS = 4;
    private static final int CHEAT_BOOSTS = 5;
    private static final int CHEAT_MAGNETS = 6;
    private static final int CHEAT_BLUE_LASERS = 7;
    private static final int CHEAT_DISRUPTORS = 8;
    private static final int CHEAT_BOMBS = 9;
    private static final int CHEAT_HEAT_BOMBS = 10;
    private static final int CHEAT_ICE_BOMBS = 11;
    private static String[] OTHER_AMMO_CHOICES = { Strings.loadGame(GameString.MISSILES),
	    Strings.loadGame(GameString.STUNNERS), Strings.loadGame(GameString.BLUE_LASERS),
	    Strings.loadGame(GameString.DISRUPTORS) };
    private static String[] OTHER_TOOL_CHOICES = { Strings.loadGame(GameString.BOOSTS),
	    Strings.loadGame(GameString.MAGNETS) };
    private static String[] OTHER_RANGE_CHOICES = { Strings.loadGame(GameString.BOMBS),
	    Strings.loadGame(GameString.HEAT_BOMBS), Strings.loadGame(GameString.ICE_BOMBS) };

    public static boolean canObjectMove(final int locX, final int locY, final int dirX, final int dirY) {
	return MLOTask.checkSolid(locX + dirX, locY + dirY);
    }

    public static Game get() {
	if (Game.instance == null) {
	    Game.instance = new Game();
	}
	return Game.instance;
    }

    private static int[] getEnabledDifficulties() {
	final var temp = new ArrayList<Integer>();
	if (Settings.isKidsDifficultyEnabled()) {
	    temp.add(Difficulty.KIDS.ordinal() - 1);
	}
	if (Settings.isEasyDifficultyEnabled()) {
	    temp.add(Difficulty.EASY.ordinal() - 1);
	}
	if (Settings.isMediumDifficultyEnabled()) {
	    temp.add(Difficulty.MEDIUM.ordinal() - 1);
	}
	if (Settings.isHardDifficultyEnabled()) {
	    temp.add(Difficulty.HARD.ordinal() - 1);
	}
	if (Settings.isDeadlyDifficultyEnabled()) {
	    temp.add(Difficulty.DEADLY.ordinal() - 1);
	}
	final var temp2 = temp.toArray(new Integer[temp.size()]);
	final var retVal = new int[temp2.length];
	for (var x = 0; x < temp2.length; x++) {
	    retVal[x] = temp2[x];
	}
	return retVal;
    }

    private static void updateRedo(final boolean las, final boolean mis, final boolean stu, final boolean boo,
	    final boolean mag, final boolean blu, final boolean dis, final boolean bom, final boolean hbm,
	    final boolean ibm) {
	final var a = ArenaManager.get().getArena();
	a.updateRedoHistory(new HistoryStatus(las, mis, stu, boo, mag, blu, dis, bom, hbm, ibm));
	LaserTankEE.getMenus().updateMenuItemState();
    }

    static void updateUndo(final boolean las, final boolean mis, final boolean stu, final boolean boo,
	    final boolean mag, final boolean blu, final boolean dis, final boolean bom, final boolean hbm,
	    final boolean ibm) {
	final var a = ArenaManager.get().getArena();
	a.updateUndoHistory(new HistoryStatus(las, mis, stu, boo, mag, blu, dis, bom, hbm, ibm));
	LaserTankEE.getMenus().updateMenuItemState();
    }

    // Fields
    private Container borderPane, scorePane, infoPane, outerOutputPane;
    private GameDraw outputPane;
    ArenaObject tank;
    private boolean savedGameFlag;
    private LaserType activeLaserType;
    final PlayerLocationManager plMgr;
    private final Cheats cMgr;
    private final ScoreTracker st;
    private JLabel scoreMoves;
    private JLabel scoreShots;
    private JLabel scoreOthers;
    private JLabel otherAmmoLeft;
    private JLabel otherToolsLeft;
    private JLabel otherRangesLeft;
    private JLabel levelInfo;
    private boolean delayedDecayActive;
    private ArenaObject delayedDecayObject;
    boolean laserActive;
    boolean moving;
    private boolean remoteDecay;
    private AnimationTask animator;
    private GameReplayEngine gre;
    private boolean recording;
    private boolean replaying;
    private boolean newGameResult;
    private MLOTask mlot;
    private JDialog difficultyFrame;
    private JList<String> difficultyList;
    private boolean lpbLoaded;
    private final boolean[] cheatStatus;
    private boolean autoMove;
    private boolean dead;
    int otherAmmoMode;
    int otherToolMode;
    RangeType otherRangeMode;
    private final EventHandler handler = new EventHandler();
    private final FocusHandler fHandler = new FocusHandler();

    // Constructors
    private Game() {
	this.plMgr = new PlayerLocationManager();
	this.cMgr = new Cheats();
	this.st = new ScoreTracker();
	this.savedGameFlag = false;
	this.delayedDecayActive = false;
	this.delayedDecayObject = null;
	this.laserActive = false;
	this.activeLaserType = LaserType.GREEN;
	this.remoteDecay = false;
	this.moving = false;
	this.gre = new GameReplayEngine();
	this.recording = false;
	this.replaying = false;
	this.newGameResult = false;
	this.lpbLoaded = false;
	this.cheatStatus = new boolean[this.cMgr.getCheatCount()];
	this.autoMove = false;
	this.dead = false;
	this.otherAmmoMode = Game.OTHER_AMMO_MODE_MISSILES;
	this.otherToolMode = Game.OTHER_TOOL_MODE_BOOSTS;
	this.otherRangeMode = RangeType.BOMB;
    }

    public void abortAndWaitForMLOLoop() {
	if (this.mlot != null && this.mlot.isAlive()) {
	    this.mlot.abortLoop();
	    var waiting = true;
	    while (waiting) {
		try {
		    this.mlot.join();
		    waiting = false;
		} catch (final InterruptedException ie) {
		    // Ignore
		}
	    }
	}
	this.moveLoopDone();
	this.laserDone();
    }

    private void abortMovementLaserObjectLoop() {
	this.mlot.abortLoop();
	this.moveLoopDone();
	this.laserDone();
    }

    // Methods
    public void activeLanguageChanged() {
	this.setUpDifficultyDialog();
	Game.OTHER_AMMO_CHOICES = new String[] { Strings.loadGame(GameString.MISSILES),
		Strings.loadGame(GameString.STUNNERS), Strings.loadGame(GameString.BLUE_LASERS),
		Strings.loadGame(GameString.DISRUPTORS) };
	Game.OTHER_TOOL_CHOICES = new String[] { Strings.loadGame(GameString.BOOSTS),
		Strings.loadGame(GameString.MAGNETS) };
	Game.OTHER_RANGE_CHOICES = new String[] { Strings.loadGame(GameString.BOMBS),
		Strings.loadGame(GameString.HEAT_BOMBS), Strings.loadGame(GameString.ICE_BOMBS) };
    }

    void cancelButtonClicked() {
	this.difficultyFrame.setVisible(false);
	this.newGameResult = false;
    }

    public void changeOtherAmmoMode() {
	final var choice = CommonDialogs.showInputDialog(Strings.loadGame(GameString.WHICH_AMMO),
		Strings.loadGame(GameString.CHANGE_AMMO), Game.OTHER_AMMO_CHOICES,
		Game.OTHER_AMMO_CHOICES[this.otherAmmoMode]);
	if (choice != null) {
	    for (var z = 0; z < Game.OTHER_AMMO_CHOICES.length; z++) {
		if (choice.equals(Game.OTHER_AMMO_CHOICES[z])) {
		    this.otherAmmoMode = z;
		    break;
		}
	    }
	    this.updateScoreText();
	    CommonDialogs.showDialog(Strings.loadGame(GameString.AMMO_CHANGED) + Strings.loadCommon(CommonString.SPACE)
		    + Game.OTHER_AMMO_CHOICES[this.otherAmmoMode] + Strings.loadCommon(CommonString.NOTL_PERIOD));
	}
    }

    public void changeOtherRangeMode() {
	final var choice = CommonDialogs.showInputDialog(Strings.loadGame(GameString.WHICH_RANGE),
		Strings.loadGame(GameString.CHANGE_RANGE), Game.OTHER_RANGE_CHOICES,
		Game.OTHER_RANGE_CHOICES[this.otherRangeMode.ordinal()]);
	if (choice != null) {
	    for (var z = 0; z < Game.OTHER_RANGE_CHOICES.length; z++) {
		if (choice.equals(Game.OTHER_RANGE_CHOICES[z])) {
		    this.otherRangeMode = RangeTypeHelper.fromOrdinal(z);
		    break;
		}
	    }
	    this.updateScoreText();
	    CommonDialogs.showDialog(Strings.loadGame(GameString.RANGE_CHANGED) + Strings.loadCommon(CommonString.SPACE)
		    + Game.OTHER_RANGE_CHOICES[this.otherRangeMode.ordinal()]
		    + Strings.loadCommon(CommonString.NOTL_PERIOD));
	}
    }

    public void changeOtherToolMode() {
	final var choice = CommonDialogs.showInputDialog(Strings.loadGame(GameString.WHICH_TOOL),
		Strings.loadGame(GameString.CHANGE_TOOL), Game.OTHER_TOOL_CHOICES,
		Game.OTHER_TOOL_CHOICES[this.otherToolMode]);
	if (choice != null) {
	    for (var z = 0; z < Game.OTHER_TOOL_CHOICES.length; z++) {
		if (choice.equals(Game.OTHER_TOOL_CHOICES[z])) {
		    this.otherToolMode = z;
		    break;
		}
	    }
	    this.updateScoreText();
	    CommonDialogs.showDialog(Strings.loadGame(GameString.TOOL_CHANGED) + Strings.loadCommon(CommonString.SPACE)
		    + Game.OTHER_TOOL_CHOICES[this.otherToolMode] + Strings.loadCommon(CommonString.NOTL_PERIOD));
	}
    }

    void clearDead() {
	this.dead = false;
    }

    public void clearReplay() {
	this.gre = new GameReplayEngine();
	this.lpbLoaded = true;
    }

    public void decay() {
	if (this.tank != null) {
	    this.tank.setSavedObject(new ArenaObject(GameObjectID.PLACEHOLDER));
	}
    }

    void doAction(final GameAction action, final int x, final int y) {
	final var px = this.getPlayerLocationX();
	final var py = this.getPlayerLocationY();
	final var currDir = this.tank.getDirection();
	final var newDir = DirectionHelper.resolveRelative(x, y);
	switch (action) {
	case MOVE:
	    if (currDir != newDir) {
		this.tank.setDirection(newDir);
		Sounds.play(Sound.TURN);
		this.redrawArena();
	    } else {
		this.updatePositionRelative(x, y);
	    }
	    break;
	case SHOOT:
	case SHOOT_ALT_AMMO:
	    this.fireLaser(px, py, this.tank);
	    break;
	case USE_RANGE:
	    this.fireRange();
	    break;
	case USE_TOOL:
	    this.updatePositionRelative(x, y);
	    break;
	default:
	    break;
	}
    }

    void doDelayedDecay() {
	this.tank.setSavedObject(this.delayedDecayObject);
	this.delayedDecayActive = false;
    }

    void doRemoteDelayedDecay(final ArenaObject o) {
	o.setSavedObject(this.delayedDecayObject);
	this.remoteDecay = false;
	this.delayedDecayActive = false;
    }

    public void enterCheatCode() {
	final var rawCheat = this.cMgr.enterCheat();
	if (rawCheat != null) {
	    if (rawCheat.contains(Strings.loadGame(GameString.ENABLE_CHEAT))) {
		// Enable cheat
		final var cheat = rawCheat.substring(7);
		for (var x = 0; x < this.cMgr.getCheatCount(); x++) {
		    if (this.cMgr.queryCheatCache(cheat) == x) {
			this.cheatStatus[x] = true;
			break;
		    }
		}
	    } else {
		// Disable cheat
		final var cheat = rawCheat.substring(8);
		for (var x = 0; x < this.cMgr.getCheatCount(); x++) {
		    if (this.cMgr.queryCheatCache(cheat) == x) {
			this.cheatStatus[x] = false;
			break;
		    }
		}
	    }
	}
    }

    public void exitGame() {
	// Halt the animator
	if (this.animator != null) {
	    this.animator.stopAnimator();
	    this.animator = null;
	}
	// Halt the movement/laser processor
	if (this.mlot != null) {
	    this.abortMovementLaserObjectLoop();
	}
	this.mlot = null;
	final var m = ArenaManager.get().getArena();
	// Restore the arena
	m.restore();
	final var playerExists = m.doesPlayerExist(this.plMgr.getActivePlayerNumber());
	if (playerExists) {
	    try {
		this.resetPlayerToStart();
	    } catch (final InvalidArenaException iae) {
		// Ignore
	    }
	} else {
	    ArenaManager.get().setLoaded(false);
	}
	// Reset saved game flag
	this.savedGameFlag = false;
	ArenaManager.get().setDirty(false);
	// Exit game
	LaserTankEE.getMainScreen().showGUI();
    }

    public boolean fireLaser(final int ox, final int oy, final ArenaObject shooter) {
	if (this.otherAmmoMode == Game.OTHER_AMMO_MODE_MISSILES && this.activeLaserType == LaserType.MISSILE
		&& TankInventory.getMissilesLeft() == 0 && !this.getCheatStatus(Game.CHEAT_MISSILES)) {
	    CommonDialogs.showDialog(Strings.loadGame(GameString.OUT_OF_MISSILES));
	} else if (this.otherAmmoMode == Game.OTHER_AMMO_MODE_STUNNERS && this.activeLaserType == LaserType.STUNNER
		&& TankInventory.getStunnersLeft() == 0 && !this.getCheatStatus(Game.CHEAT_STUNNERS)) {
	    CommonDialogs.showDialog(Strings.loadGame(GameString.OUT_OF_STUNNERS));
	} else if (this.otherAmmoMode == Game.OTHER_AMMO_MODE_BLUE_LASERS && this.activeLaserType == LaserType.BLUE
		&& TankInventory.getBlueLasersLeft() == 0 && !this.getCheatStatus(Game.CHEAT_BLUE_LASERS)) {
	    CommonDialogs.showDialog(Strings.loadGame(GameString.OUT_OF_BLUE_LASERS));
	} else {
	    final var a = ArenaManager.get().getArena();
	    if (!a.isMoveShootAllowed() && !this.laserActive || a.isMoveShootAllowed()) {
		this.laserActive = true;
		final var currDirection = DirectionHelper.unresolveRelative(shooter.getDirection());
		final var x = currDirection[0];
		final var y = currDirection[1];
		if (this.mlot == null || !this.mlot.isAlive()) {
		    this.mlot = new MLOTask();
		}
		this.mlot.activateLasers(x, y, ox, oy, this.activeLaserType, shooter);
		if (!this.mlot.isAlive()) {
		    this.mlot.start();
		}
		if (this.replaying) {
		    // Wait
		    while (this.laserActive) {
			try {
			    Thread.sleep(100);
			} catch (final InterruptedException ie) {
			    // Ignore
			}
		    }
		}
		return true;
	    }
	}
	return false;
    }

    void fireRange() {
	// Boom!
	Sounds.play(Sound.BOOM);
	this.updateScore(0, 0, 1);
	if (this.otherRangeMode == RangeType.BOMB) {
	    Game.updateUndo(false, false, false, false, false, false, false, true, false, false);
	} else if (this.otherRangeMode == RangeType.HEAT_BOMB) {
	    Game.updateUndo(false, false, false, false, false, false, false, false, true, false);
	} else if (this.otherRangeMode == RangeType.ICE_BOMB) {
	    Game.updateUndo(false, false, false, false, false, false, false, false, false, true);
	}
	final var a = ArenaManager.get().getArena();
	final var px = this.plMgr.getPlayerLocationX();
	final var py = this.plMgr.getPlayerLocationY();
	final var pz = this.plMgr.getPlayerLocationZ();
	a.circularScanRange(px, py, pz, 1, this.otherRangeMode,
		ArenaObject.getImbuedForce(RangeTypeHelper.material(this.otherRangeMode)));
	ArenaManager.get().getArena().tickTimers(pz, GameAction.USE_RANGE);
	this.updateScoreText();
    }

    public void gameOver() {
	// Check cheats
	if (this.getCheatStatus(Game.CHEAT_INVINCIBLE)) {
	    return;
	}
	// Check dead
	if (this.dead) {
	    // Already dead
	    throw new AlreadyDeadException();
	}
	// We are dead
	this.dead = true;
	// Stop the movement/laser/object loop
	if (this.mlot != null && this.mlot.isAlive()) {
	    this.abortMovementLaserObjectLoop();
	}
	this.mlot = null;
	Sounds.play(Sound.DEAD);
	final var choice = CustomDialogs.showDeadDialog();
	switch (choice) {
	case CommonDialogs.CANCEL_OPTION:
	    // End
	    this.exitGame();
	    break;
	case CommonDialogs.YES_OPTION:
	    // Undo
	    this.undoLastMove();
	    break;
	case CommonDialogs.NO_OPTION:
	    // Restart
	    try {
		this.resetCurrentLevel();
	    } catch (final InvalidArenaException iae) {
		CommonDialogs.showErrorDialog(Strings.loadError(ErrorString.TANK_LOCATION),
			GlobalStrings.loadUntranslated(UntranslatedString.PROGRAM_NAME));
		this.exitGame();
		return;
	    }
	    break;
	default:
	    // Closed Dialog
	    this.exitGame();
	    break;
	}
    }

    public int getActivePlayerNumber() {
	return this.plMgr.getActivePlayerNumber();
    }

    boolean getCheatStatus(final int cheatID) {
	return this.cheatStatus[cheatID];
    }

    public int getPlayerLocationX() {
	return this.plMgr.getPlayerLocationX();
    }

    public int getPlayerLocationY() {
	return this.plMgr.getPlayerLocationY();
    }

    public int getPlayerLocationZ() {
	return this.plMgr.getPlayerLocationZ();
    }

    public ArenaObject getTank() {
	return this.tank;
    }

    public int[] getTankLocation() {
	return new int[] { this.plMgr.getPlayerLocationX(), this.plMgr.getPlayerLocationY(),
		this.plMgr.getPlayerLocationZ() };
    }

    public void haltMovingObjects() {
	if (this.mlot != null && this.mlot.isAlive()) {
	    this.mlot.haltMovingObjects();
	}
    }

    @Override
    public void hideScreenHook() {
	this.removeKeyListener(this.handler);
	this.removeWindowListener(this.handler);
	this.removeWindowFocusListener(this.fHandler);
    }

    boolean isAutoMoveScheduled() {
	return this.autoMove;
    }

    boolean isDelayedDecayActive() {
	return this.delayedDecayActive;
    }

    boolean isRemoteDecayActive() {
	return this.remoteDecay;
    }

    boolean isReplaying() {
	return this.replaying;
    }

    void laserDone() {
	this.laserActive = false;
	LaserTankEE.getMenus().updateMenuItemState();
    }

    public void loadGameHookG1(final DataIOReader arenaFile) throws IOException {
	ArenaManager.get().setScoresFileName(arenaFile.readString());
	this.st.setMoves(arenaFile.readLong());
	this.st.setShots(arenaFile.readLong());
	this.st.setOthers(arenaFile.readLong());
    }

    public void loadGameHookG2(final DataIOReader arenaFile) throws IOException {
	ArenaManager.get().setScoresFileName(arenaFile.readString());
	this.st.setMoves(arenaFile.readLong());
	this.st.setShots(arenaFile.readLong());
	this.st.setOthers(arenaFile.readLong());
	TankInventory.setRedKeysLeft(arenaFile.readInt());
	TankInventory.setGreenKeysLeft(arenaFile.readInt());
	TankInventory.setBlueKeysLeft(arenaFile.readInt());
    }

    public void loadGameHookG3(final DataIOReader arenaFile) throws IOException {
	ArenaManager.get().setScoresFileName(arenaFile.readString());
	this.st.setMoves(arenaFile.readLong());
	this.st.setShots(arenaFile.readLong());
	this.st.setOthers(arenaFile.readLong());
	TankInventory.readInventory(arenaFile);
    }

    public void loadGameHookG4(final DataIOReader arenaFile) throws IOException {
	ArenaManager.get().setScoresFileName(arenaFile.readString());
	this.st.setMoves(arenaFile.readLong());
	this.st.setShots(arenaFile.readLong());
	this.st.setOthers(arenaFile.readLong());
	TankInventory.readInventory(arenaFile);
    }

    public void loadGameHookG5(final DataIOReader arenaFile) throws IOException {
	ArenaManager.get().setScoresFileName(arenaFile.readString());
	this.st.setMoves(arenaFile.readLong());
	this.st.setShots(arenaFile.readLong());
	this.st.setOthers(arenaFile.readLong());
	TankInventory.readInventory(arenaFile);
    }

    public void loadGameHookG6(final DataIOReader arenaFile) throws IOException {
	ArenaManager.get().setScoresFileName(arenaFile.readString());
	this.st.setMoves(arenaFile.readLong());
	this.st.setShots(arenaFile.readLong());
	this.st.setOthers(arenaFile.readLong());
	TankInventory.readInventory(arenaFile);
    }

    public void loadLevel() {
	final var m = ArenaManager.get().getArena();
	final var choices = LaserTankEE.getLevelInfoList();
	final var res = CommonDialogs.showInputDialog(Strings.loadGame(GameString.LOAD_LEVEL_PROMPT),
		Strings.loadGame(GameString.LOAD_LEVEL), choices, choices[m.getActiveLevelNumber()]);
	var number = -1;
	for (number = 0; number < m.getLevels(); number++) {
	    if (choices[number].equals(res)) {
		break;
	    }
	}
	if (m.doesLevelExist(number)) {
	    this.suspendAnimator();
	    m.restore();
	    m.switchLevel(number);
	    ArenaManager.get().getArena().setDirtyFlags(this.plMgr.getPlayerLocationZ());
	    m.resetHistoryEngine();
	    this.gre = new GameReplayEngine();
	    LaserTankEE.getMenus().updateMenuItemState();
	    this.processLevelExists();
	}
    }

    public void loadReplay(final GameAction a, final int x, final int y) {
	this.gre.updateRedoHistory(a, x, y);
    }

    void markTankAsDirty() {
	ArenaManager.get().getArena().markAsDirty(this.plMgr.getPlayerLocationX(), this.plMgr.getPlayerLocationY(),
		this.plMgr.getPlayerLocationZ());
    }

    public void morph(final ArenaObject morphInto, final int x, final int y, final int z, final int w) {
	final var m = ArenaManager.get().getArena();
	try {
	    m.setCell(morphInto, x, y, z, w);
	    ArenaManager.get().setDirty(true);
	} catch (final ArrayIndexOutOfBoundsException | NullPointerException np) {
	    // Do nothing
	}
    }

    void moveLoopDone() {
	this.moving = false;
	LaserTankEE.getMenus().updateMenuItemState();
    }

    public boolean newGame() {
	this.difficultyList.clearSelection();
	final var retVal = Game.getEnabledDifficulties();
	this.difficultyList.setSelectedIndices(retVal);
	this.difficultyFrame.setVisible(true);
	return this.newGameResult;
    }

    void offsetPlayerLocationX(final int val) {
	this.plMgr.offsetPlayerLocationX(val);
    }

    void offsetPlayerLocationY(final int val) {
	this.plMgr.offsetPlayerLocationY(val);
    }

    void okButtonClicked() {
	this.difficultyFrame.setVisible(false);
	if (this.difficultyList.isSelectedIndex(Difficulty.KIDS.ordinal() - 1)) {
	    Settings.setKidsDifficultyEnabled(true);
	} else {
	    Settings.setKidsDifficultyEnabled(false);
	}
	if (this.difficultyList.isSelectedIndex(Difficulty.EASY.ordinal() - 1)) {
	    Settings.setEasyDifficultyEnabled(true);
	} else {
	    Settings.setEasyDifficultyEnabled(false);
	}
	if (this.difficultyList.isSelectedIndex(Difficulty.MEDIUM.ordinal() - 1)) {
	    Settings.setMediumDifficultyEnabled(true);
	} else {
	    Settings.setMediumDifficultyEnabled(false);
	}
	if (this.difficultyList.isSelectedIndex(Difficulty.HARD.ordinal() - 1)) {
	    Settings.setHardDifficultyEnabled(true);
	} else {
	    Settings.setHardDifficultyEnabled(false);
	}
	if (this.difficultyList.isSelectedIndex(Difficulty.DEADLY.ordinal() - 1)) {
	    Settings.setDeadlyDifficultyEnabled(true);
	} else {
	    Settings.setDeadlyDifficultyEnabled(false);
	}
	this.newGameResult = true;
    }

    public void playArena() {
	if (ArenaManager.get().getLoaded()) {
	    LaserTankEE.setOnGameScreen();
	    ArenaManager.get().getArena().switchLevel(0);
	    final var res = ArenaManager.get().getArena()
		    .switchToNextLevelWithDifficulty(Game.getEnabledDifficulties());
	    if (res) {
		try {
		    this.resetPlayerToStart();
		} catch (final InvalidArenaException iae) {
		    CommonDialogs.showErrorDialog(Strings.loadError(ErrorString.TANK_LOCATION),
			    GlobalStrings.loadUntranslated(UntranslatedString.PROGRAM_NAME));
		    this.exitGame();
		    return;
		}
		this.updateTank();
		this.tank.setSavedObject(new ArenaObject(GameObjectID.PLACEHOLDER));
		this.st.setScoreFile(ArenaManager.get().getScoresFileName());
		if (!this.savedGameFlag) {
		    this.st.resetScore(ArenaManager.get().getScoresFileName());
		}
		this.updateInfo();
		this.borderPane.removeAll();
		this.borderPane.add(this.outerOutputPane, BorderLayout.CENTER);
		this.borderPane.add(this.scorePane, BorderLayout.NORTH);
		this.borderPane.add(this.infoPane, BorderLayout.SOUTH);
		LaserTankEE.getMenus().updateMenuItemState();
		ArenaManager.get().getArena().setDirtyFlags(this.plMgr.getPlayerLocationZ());
		this.redrawArena();
		this.updateScoreText();
		this.pack();
		this.replaying = false;
		// Start animator, if enabled
		if (Settings.enableAnimation()) {
		    this.animator = new AnimationTask();
		    this.animator.start();
		}
	    } else {
		CommonDialogs.showDialog(Strings.loadGame(GameString.NO_LEVEL_WITH_DIFFICULTY));
		LaserTankEE.getMainScreen().showGUI();
	    }
	} else {
	    CommonDialogs.showDialog(Strings.loadError(ErrorString.NO_ARENA_OPENED));
	}
    }

    @Override
    protected void populateMainPanel() {
	this.borderPane = new Container();
	this.borderPane.setLayout(new BorderLayout());
	this.outerOutputPane = RCLGenerator.generateRowColumnLabels();
	this.outputPane = new GameDraw();
	this.outputPane.setLayout(new GridLayout(GameViewingWindowManager.getViewingWindowSizeX(),
		GameViewingWindowManager.getViewingWindowSizeY()));
	this.outputPane.addMouseListener(this.handler);
	this.scoreMoves = new JLabel(Strings.loadGame(GameString.MOVES) + Strings.loadCommon(CommonString.COLON)
		+ Strings.loadCommon(CommonString.SPACE) + Strings.loadCommon(CommonString.ZERO));
	this.scoreShots = new JLabel(Strings.loadGame(GameString.SHOTS) + Strings.loadCommon(CommonString.COLON)
		+ Strings.loadCommon(CommonString.SPACE) + Strings.loadCommon(CommonString.ZERO));
	this.scoreOthers = new JLabel(Strings.loadGame(GameString.OTHERS) + Strings.loadCommon(CommonString.COLON)
		+ Strings.loadCommon(CommonString.SPACE) + Strings.loadCommon(CommonString.ZERO));
	this.otherAmmoLeft = new JLabel(
		Strings.loadCommon(CommonString.OPEN_PARENTHESES) + Strings.loadGame(GameString.MISSILES)
			+ Strings.loadCommon(CommonString.COLON) + Strings.loadCommon(CommonString.SPACE)
			+ Strings.loadCommon(CommonString.ZERO) + Strings.loadCommon(CommonString.CLOSE_PARENTHESES));
	this.otherToolsLeft = new JLabel(
		Strings.loadCommon(CommonString.OPEN_PARENTHESES) + Strings.loadGame(GameString.BOOSTS)
			+ Strings.loadCommon(CommonString.COLON) + Strings.loadCommon(CommonString.SPACE)
			+ Strings.loadCommon(CommonString.ZERO) + Strings.loadCommon(CommonString.CLOSE_PARENTHESES));
	this.otherRangesLeft = new JLabel(
		Strings.loadCommon(CommonString.OPEN_PARENTHESES) + Strings.loadGame(GameString.BOMBS)
			+ Strings.loadCommon(CommonString.COLON) + Strings.loadCommon(CommonString.SPACE)
			+ Strings.loadCommon(CommonString.ZERO) + Strings.loadCommon(CommonString.CLOSE_PARENTHESES));
	this.scorePane = new Container();
	this.scorePane.setLayout(new FlowLayout());
	this.scorePane.add(this.scoreMoves);
	this.scorePane.add(this.scoreShots);
	this.scorePane.add(this.scoreOthers);
	this.scorePane.add(this.otherAmmoLeft);
	this.scorePane.add(this.otherToolsLeft);
	this.scorePane.add(this.otherRangesLeft);
	this.levelInfo = new JLabel(Strings.loadCommon(CommonString.SPACE));
	this.infoPane = new Container();
	this.infoPane.setLayout(new FlowLayout());
	this.infoPane.add(this.levelInfo);
	this.scoreMoves.setLabelFor(this.outputPane);
	this.scoreShots.setLabelFor(this.outputPane);
	this.scoreOthers.setLabelFor(this.outputPane);
	this.otherAmmoLeft.setLabelFor(this.outputPane);
	this.otherToolsLeft.setLabelFor(this.outputPane);
	this.otherRangesLeft.setLabelFor(this.outputPane);
	this.levelInfo.setLabelFor(this.outputPane);
	this.outerOutputPane.add(this.outputPane, BorderLayout.CENTER);
	this.borderPane.add(this.outerOutputPane, BorderLayout.CENTER);
	this.borderPane.add(this.scorePane, BorderLayout.NORTH);
	this.borderPane.add(this.infoPane, BorderLayout.SOUTH);
	this.setUpDifficultyDialog();
	this.setTitle(GlobalStrings.loadUntranslated(UntranslatedString.PROGRAM_NAME));
    }

    public void previousLevel() {
	final var m = ArenaManager.get().getArena();
	m.resetHistoryEngine();
	this.gre = new GameReplayEngine();
	LaserTankEE.getMenus().updateMenuItemState();
	this.suspendAnimator();
	m.restore();
	if (m.doesLevelExistOffset(-1)) {
	    m.switchLevelOffset(-1);
	    final var levelExists = m.switchToPreviousLevelWithDifficulty(Game.getEnabledDifficulties());
	    if (levelExists) {
		m.setDirtyFlags(this.plMgr.getPlayerLocationZ());
		this.processLevelExists();
	    } else {
		CommonDialogs.showErrorDialog(Strings.loadError(ErrorString.NO_PREVIOUS_LEVEL),
			GlobalStrings.loadUntranslated(UntranslatedString.PROGRAM_NAME));
	    }
	} else {
	    CommonDialogs.showErrorDialog(Strings.loadError(ErrorString.NO_PREVIOUS_LEVEL),
		    GlobalStrings.loadUntranslated(UntranslatedString.PROGRAM_NAME));
	}
    }

    private void processLevelExists() {
	try {
	    this.resetPlayerToStart();
	} catch (final InvalidArenaException iae) {
	    CommonDialogs.showErrorDialog(Strings.loadError(ErrorString.TANK_LOCATION),
		    GlobalStrings.loadUntranslated(UntranslatedString.PROGRAM_NAME));
	    this.exitGame();
	    return;
	}
	this.updateTank();
	this.st.resetScore(ArenaManager.get().getScoresFileName());
	TankInventory.resetInventory();
	this.scoreMoves.setText(Strings.loadGame(GameString.MOVES) + Strings.loadCommon(CommonString.COLON)
		+ Strings.loadCommon(CommonString.SPACE) + Strings.loadCommon(CommonString.ZERO));
	this.scoreShots.setText(Strings.loadGame(GameString.SHOTS) + Strings.loadCommon(CommonString.COLON)
		+ Strings.loadCommon(CommonString.SPACE) + Strings.loadCommon(CommonString.ZERO));
	this.scoreOthers.setText(Strings.loadGame(GameString.OTHERS) + Strings.loadCommon(CommonString.COLON)
		+ Strings.loadCommon(CommonString.SPACE) + Strings.loadCommon(CommonString.ZERO));
	switch (this.otherAmmoMode) {
	case Game.OTHER_AMMO_MODE_MISSILES:
	    if (this.getCheatStatus(Game.CHEAT_MISSILES)) {
		this.otherAmmoLeft.setText(Strings.loadCommon(CommonString.OPEN_PARENTHESES)
			+ Strings.loadGame(GameString.MISSILES) + Strings.loadCommon(CommonString.COLON)
			+ Strings.loadCommon(CommonString.SPACE) + Strings.loadGame(GameString.INFINITE)
			+ Strings.loadCommon(CommonString.CLOSE_PARENTHESES));
	    } else {
		this.otherAmmoLeft.setText(Strings.loadCommon(CommonString.OPEN_PARENTHESES)
			+ Strings.loadGame(GameString.MISSILES) + Strings.loadCommon(CommonString.COLON)
			+ Strings.loadCommon(CommonString.SPACE) + Strings.loadCommon(CommonString.ZERO)
			+ Strings.loadCommon(CommonString.CLOSE_PARENTHESES));
	    }
	    break;
	case Game.OTHER_AMMO_MODE_STUNNERS:
	    if (this.getCheatStatus(Game.CHEAT_STUNNERS)) {
		this.otherAmmoLeft.setText(Strings.loadCommon(CommonString.OPEN_PARENTHESES)
			+ Strings.loadGame(GameString.STUNNERS) + Strings.loadCommon(CommonString.COLON)
			+ Strings.loadCommon(CommonString.SPACE) + Strings.loadGame(GameString.INFINITE)
			+ Strings.loadCommon(CommonString.CLOSE_PARENTHESES));
	    } else {
		this.otherAmmoLeft.setText(Strings.loadCommon(CommonString.OPEN_PARENTHESES)
			+ Strings.loadGame(GameString.STUNNERS) + Strings.loadCommon(CommonString.COLON)
			+ Strings.loadCommon(CommonString.SPACE) + Strings.loadCommon(CommonString.ZERO)
			+ Strings.loadCommon(CommonString.CLOSE_PARENTHESES));
	    }
	    break;
	case Game.OTHER_AMMO_MODE_BLUE_LASERS:
	    if (this.getCheatStatus(Game.CHEAT_BLUE_LASERS)) {
		this.otherAmmoLeft.setText(Strings.loadCommon(CommonString.OPEN_PARENTHESES)
			+ Strings.loadGame(GameString.BLUE_LASERS) + Strings.loadCommon(CommonString.COLON)
			+ Strings.loadCommon(CommonString.SPACE) + Strings.loadGame(GameString.INFINITE)
			+ Strings.loadCommon(CommonString.CLOSE_PARENTHESES));
	    } else {
		this.otherAmmoLeft.setText(Strings.loadCommon(CommonString.OPEN_PARENTHESES)
			+ Strings.loadGame(GameString.BLUE_LASERS) + Strings.loadCommon(CommonString.COLON)
			+ Strings.loadCommon(CommonString.SPACE) + Strings.loadCommon(CommonString.ZERO)
			+ Strings.loadCommon(CommonString.CLOSE_PARENTHESES));
	    }
	    break;
	case Game.OTHER_AMMO_MODE_DISRUPTORS:
	    if (this.getCheatStatus(Game.CHEAT_DISRUPTORS)) {
		this.otherAmmoLeft.setText(Strings.loadCommon(CommonString.OPEN_PARENTHESES)
			+ Strings.loadGame(GameString.DISRUPTORS) + Strings.loadCommon(CommonString.COLON)
			+ Strings.loadCommon(CommonString.SPACE) + Strings.loadGame(GameString.INFINITE)
			+ Strings.loadCommon(CommonString.CLOSE_PARENTHESES));
	    } else {
		this.otherAmmoLeft.setText(Strings.loadCommon(CommonString.OPEN_PARENTHESES)
			+ Strings.loadGame(GameString.DISRUPTORS) + Strings.loadCommon(CommonString.COLON)
			+ Strings.loadCommon(CommonString.SPACE) + Strings.loadCommon(CommonString.ZERO)
			+ Strings.loadCommon(CommonString.CLOSE_PARENTHESES));
	    }
	    break;
	default:
	    break;
	}
	this.updateInfo();
	this.redrawArena();
	this.resumeAnimator();
    }

    private boolean readSolution() {
	try {
	    final var activeLevel = ArenaManager.get().getArena().getActiveLevelNumber();
	    final var levelFile = ArenaManager.get().getLastUsedArena();
	    final var filename = levelFile + Strings.loadCommon(CommonString.UNDERSCORE) + activeLevel
		    + FileExtensions.getSolutionExtensionWithPeriod();
	    try (DataIOReader file = new XDataReader(filename,
		    GlobalStrings.loadUntranslated(UntranslatedString.SOLUTION))) {
		this.gre = GameReplayEngine.readReplay(file);
	    }
	    return true;
	} catch (final IOException ioe) {
	    throw new InvalidArenaException(ioe);
	}
    }

    public void redoLastMove() {
	final var a = ArenaManager.get().getArena();
	if (a.tryRedo()) {
	    this.moving = false;
	    this.laserActive = false;
	    a.redo();
	    final var laser = a.getWhatWas().wasSomething(HistoryStatus.WAS_LASER);
	    final var missile = a.getWhatWas().wasSomething(HistoryStatus.WAS_MISSILE);
	    final var stunner = a.getWhatWas().wasSomething(HistoryStatus.WAS_STUNNER);
	    final var boost = a.getWhatWas().wasSomething(HistoryStatus.WAS_BOOST);
	    final var magnet = a.getWhatWas().wasSomething(HistoryStatus.WAS_MAGNET);
	    final var blue = a.getWhatWas().wasSomething(HistoryStatus.WAS_BLUE_LASER);
	    final var disrupt = a.getWhatWas().wasSomething(HistoryStatus.WAS_DISRUPTOR);
	    final var bomb = a.getWhatWas().wasSomething(HistoryStatus.WAS_BOMB);
	    final var heatBomb = a.getWhatWas().wasSomething(HistoryStatus.WAS_HEAT_BOMB);
	    final var iceBomb = a.getWhatWas().wasSomething(HistoryStatus.WAS_ICE_BOMB);
	    final var other = missile || stunner || boost || magnet || blue || disrupt || bomb || heatBomb || iceBomb;
	    if (other) {
		this.updateScore(0, 0, -1);
		if (boost) {
		    TankInventory.fireBoost();
		} else if (magnet) {
		    TankInventory.fireMagnet();
		} else if (missile) {
		    TankInventory.fireMissile();
		} else if (stunner) {
		    TankInventory.fireStunner();
		} else if (blue) {
		    TankInventory.fireBlueLaser();
		} else if (disrupt) {
		    TankInventory.fireDisruptor();
		} else if (bomb) {
		    TankInventory.fireBomb();
		} else if (heatBomb) {
		    TankInventory.fireHeatBomb();
		} else if (iceBomb) {
		    TankInventory.fireIceBomb();
		}
	    } else if (laser && !other) {
		this.updateScore(0, 1, 0);
	    } else {
		this.updateScore(1, 0, 0);
	    }
	    try {
		this.resetPlayerToStart();
	    } catch (final InvalidArenaException iae) {
		CommonDialogs.showErrorDialog(Strings.loadError(ErrorString.TANK_LOCATION),
			GlobalStrings.loadUntranslated(UntranslatedString.PROGRAM_NAME));
		this.exitGame();
		return;
	    }
	    this.updateTank();
	    Game.updateUndo(laser, missile, stunner, boost, magnet, blue, disrupt, bomb, heatBomb, iceBomb);
	}
	LaserTankEE.getMenus().updateMenuItemState();
	this.updateScoreText();
	a.setDirtyFlags(this.plMgr.getPlayerLocationZ());
	this.redrawArena();
    }

    public synchronized void redrawArena() {
	// Draw the arena
	final var a = ArenaManager.get().getArena();
	final var drawGrid = this.outputPane.getGrid();
	int x, y;
	final var pz = this.plMgr.getPlayerLocationZ();
	for (x = GameViewingWindowManager.getViewingWindowLocationX(); x <= GameViewingWindowManager
		.getLowerRightViewingWindowLocationX(); x++) {
	    for (y = GameViewingWindowManager.getViewingWindowLocationY(); y <= GameViewingWindowManager
		    .getLowerRightViewingWindowLocationY(); y++) {
		if (a.isCellDirty(y, x, pz)) {
		    final var gbobj = a.getCell(y, x, pz, Layer.LOWER_GROUND.ordinal());
		    final var gtobj = a.getCell(y, x, pz, Layer.UPPER_GROUND.ordinal());
		    var obobj = a.getCell(y, x, pz, Layer.LOWER_OBJECTS.ordinal());
		    var otobj = a.getCell(y, x, pz, Layer.UPPER_OBJECTS.ordinal());
		    final var vbobj = a.getVirtualCell(y, x, pz, Layer.VIRTUAL.ordinal());
		    final var otrep = otobj.attributeRenderHook();
		    if (otrep != null) {
			if (otobj.canCloak()) {
			    obobj = otrep;
			}
			otobj = otrep;
		    }
		    drawGrid.setImageCell(Images.getVirtualCompositeImage(gbobj, gtobj, obobj, otobj, vbobj), x, y);
		}
	    }
	}
	a.clearDirtyFlags(this.plMgr.getPlayerLocationZ());
	this.outputPane.repaint();
    }

    public void remoteDelayedDecayTo(final ArenaObject obj) {
	this.delayedDecayActive = true;
	this.delayedDecayObject = obj;
	this.remoteDecay = true;
    }

    void replayDone() {
	this.replaying = false;
    }

    boolean replayLastMove() {
	if (this.gre.tryRedo()) {
	    this.gre.redo();
	    final var action = this.gre.getAction();
	    final var x = this.gre.getX();
	    final var y = this.gre.getY();
	    this.doAction(action, x, y);
	    return true;
	}
	return false;
    }

    public void replaySolution() {
	final var menu = LaserTankEE.getMenus();
	if (this.lpbLoaded) {
	    this.replaying = true;
	    // Turn recording off
	    this.recording = false;
	    menu.disableRecording();
	    try {
		this.resetCurrentLevel(false);
	    } catch (final InvalidArenaException iae) {
		CommonDialogs.showErrorDialog(Strings.loadError(ErrorString.TANK_LOCATION),
			GlobalStrings.loadUntranslated(UntranslatedString.PROGRAM_NAME));
		this.exitGame();
		return;
	    }
	    final var rt = new ReplayTask();
	    rt.start();
	} else {
	    final var success = this.readSolution();
	    if (!success) {
		CommonDialogs.showErrorDialog(Strings.loadError(ErrorString.NO_SOLUTION_FILE),
			GlobalStrings.loadUntranslated(UntranslatedString.PROGRAM_NAME));
	    } else {
		this.replaying = true;
		// Turn recording off
		this.recording = false;
		menu.disableRecording();
		try {
		    this.resetCurrentLevel(false);
		} catch (final InvalidArenaException iae) {
		    CommonDialogs.showErrorDialog(Strings.loadError(ErrorString.TANK_LOCATION),
			    GlobalStrings.loadUntranslated(UntranslatedString.PROGRAM_NAME));
		    this.exitGame();
		    return;
		}
		final var rt = new ReplayTask();
		rt.start();
	    }
	}
    }

    public void resetCurrentLevel() throws InvalidArenaException {
	this.resetLevel(true);
    }

    private void resetCurrentLevel(final boolean flag) throws InvalidArenaException {
	this.resetLevel(flag);
    }

    public void resetGameState() {
	final var m = ArenaManager.get().getArena();
	ArenaManager.get().setDirty(false);
	m.restore();
	this.setSavedGameFlag(false);
	this.st.resetScore();
	final var playerExists = m.doesPlayerExist(this.plMgr.getActivePlayerNumber());
	if (playerExists) {
	    this.plMgr.setPlayerLocation(m.getStartColumn(0), m.getStartRow(0), m.getStartFloor(0));
	}
    }

    private void resetLevel(final boolean flag) throws InvalidArenaException {
	final var m = ArenaManager.get().getArena();
	if (flag) {
	    m.resetHistoryEngine();
	}
	ArenaManager.get().setDirty(true);
	if (this.mlot != null && this.mlot.isAlive()) {
	    this.abortMovementLaserObjectLoop();
	}
	this.moving = false;
	this.laserActive = false;
	TankInventory.resetInventory();
	m.restore();
	m.setDirtyFlags(this.plMgr.getPlayerLocationZ());
	final var playerExists = m.doesPlayerExist(this.plMgr.getActivePlayerNumber());
	if (playerExists) {
	    this.st.resetScore(ArenaManager.get().getScoresFileName());
	    this.resetPlayerToStart();
	    this.updateTank();
	    m.clearVirtualGrid();
	    this.updateScore();
	    this.decay();
	    this.redrawArena();
	}
	LaserTankEE.getMenus().updateMenuItemState();
    }

    public void resetPlayerLocation() {
	this.plMgr.resetPlayerLocation();
    }

    public void resetPlayerToStart() throws InvalidArenaException {
	final var m = ArenaManager.get().getArena();
	final var found = m.findPlayer(1);
	if (found == null) {
	    throw new InvalidArenaException(Strings.loadError(ErrorString.TANK_LOCATION));
	}
	this.plMgr.setPlayerLocation(found[0], found[1], found[2]);
    }

    private void resetTank() {
	ArenaManager.get().getArena().setCell(this.tank, this.plMgr.getPlayerLocationX(),
		this.plMgr.getPlayerLocationY(), this.plMgr.getPlayerLocationZ(), this.tank.getLayer());
	this.markTankAsDirty();
    }

    void restorePlayerLocation() {
	this.plMgr.restorePlayerLocation();
    }

    private void resumeAnimator() {
	if (this.animator == null) {
	    this.animator = new AnimationTask();
	    this.animator.start();
	}
    }

    public void saveGameHook(final DataIOWriter arenaFile) throws IOException {
	arenaFile.writeString(ArenaManager.get().getScoresFileName());
	arenaFile.writeLong(this.st.getMoves());
	arenaFile.writeLong(this.st.getShots());
	arenaFile.writeLong(this.st.getOthers());
	TankInventory.writeInventory(arenaFile);
    }

    void savePlayerLocation() {
	this.plMgr.savePlayerLocation();
    }

    void scheduleAutoMove() {
	this.autoMove = true;
    }

    public void setActivePlayerNumber(final int value) {
	this.plMgr.setActivePlayerNumber(value);
    }

    public void setLaserType(final LaserType type) {
	this.activeLaserType = type;
    }

    public void setNormalTank() {
	final var saveTank = this.tank;
	this.tank = new ArenaObject(GameObjectID.TANK, saveTank.getDirection(), saveTank.getNumber());
	this.resetTank();
    }

    public void setPlayerLocation(final int valX, final int valY, final int valZ) {
	this.plMgr.setPlayerLocation(valX, valY, valZ);
    }

    public void setPowerfulTank() {
	final var saveTank = this.tank;
	this.tank = new ArenaObject(GameObjectID.POWER_TANK, saveTank.getDirection(), saveTank.getNumber());
	this.resetTank();
    }

    public void setSavedGameFlag(final boolean value) {
	this.savedGameFlag = value;
    }

    private void setUpDifficultyDialog() {
	// Set up Difficulty Dialog
	final var dhandler = new DifficultyEventHandler();
	this.difficultyFrame = new JDialog((JFrame) null, Strings.loadGame(GameString.SELECT_DIFFICULTY));
	final var difficultyPane = new Container();
	final var listPane = new Container();
	final var buttonPane = new Container();
	this.difficultyList = new JList<>(DifficultyHelper.getNames());
	final var okButton = new JButton(Strings.loadDialog(DialogString.OK_BUTTON));
	final var cancelButton = new JButton(Strings.loadDialog(DialogString.CANCEL_BUTTON));
	buttonPane.setLayout(new FlowLayout());
	buttonPane.add(okButton);
	buttonPane.add(cancelButton);
	listPane.setLayout(new FlowLayout());
	listPane.add(this.difficultyList);
	difficultyPane.setLayout(new BorderLayout());
	difficultyPane.add(listPane, BorderLayout.CENTER);
	difficultyPane.add(buttonPane, BorderLayout.SOUTH);
	this.difficultyFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	this.difficultyFrame.setModal(true);
	this.difficultyFrame.setResizable(false);
	okButton.setDefaultCapable(true);
	cancelButton.setDefaultCapable(false);
	this.difficultyFrame.getRootPane().setDefaultButton(okButton);
	this.difficultyFrame.addWindowListener(dhandler);
	okButton.addActionListener(dhandler);
	cancelButton.addActionListener(dhandler);
	this.difficultyFrame.setContentPane(difficultyPane);
	this.difficultyFrame.pack();
    }

    public void showScoreTable() {
	this.st.showScoreTable();
    }

    @Override
    public void showScreenHook() {
	this.addKeyListener(this.handler);
	this.addWindowListener(this.handler);
	this.addWindowFocusListener(this.fHandler);
	Musics.play(Music.GAME);
    }

    private void solvedArena() {
	TankInventory.resetInventory();
	this.exitGame();
    }

    public void solvedLevel(final boolean playSound) {
	if (playSound) {
	    Sounds.play(Sound.END_LEVEL);
	}
	final var m = ArenaManager.get().getArena();
	if (playSound) {
	    if (this.recording) {
		this.writeSolution();
	    }
	    if (this.st.checkScore()) {
		this.st.commitScore();
	    }
	}
	m.resetHistoryEngine();
	this.gre = new GameReplayEngine();
	LaserTankEE.getMenus().updateMenuItemState();
	this.suspendAnimator();
	m.restore();
	if (m.doesLevelExistOffset(1)) {
	    m.switchLevelOffset(1);
	    final var levelExists = m.switchToNextLevelWithDifficulty(Game.getEnabledDifficulties());
	    if (levelExists) {
		m.setDirtyFlags(this.plMgr.getPlayerLocationZ());
		this.processLevelExists();
	    } else {
		this.solvedArena();
	    }
	} else {
	    this.solvedArena();
	}
    }

    private void suspendAnimator() {
	if (this.animator != null) {
	    this.animator.stopAnimator();
	    try {
		this.animator.join();
	    } catch (final InterruptedException ie) {
		// Ignore
	    }
	    this.animator = null;
	}
    }

    public void togglePlayerInstance() {
	this.plMgr.togglePlayerInstance();
    }

    public void toggleRecording() {
	this.recording = !this.recording;
    }

    public void undoLastMove() {
	final var a = ArenaManager.get().getArena();
	if (a.tryUndo()) {
	    this.moving = false;
	    this.laserActive = false;
	    a.undo();
	    final var laser = a.getWhatWas().wasSomething(HistoryStatus.WAS_LASER);
	    final var missile = a.getWhatWas().wasSomething(HistoryStatus.WAS_MISSILE);
	    final var stunner = a.getWhatWas().wasSomething(HistoryStatus.WAS_STUNNER);
	    final var boost = a.getWhatWas().wasSomething(HistoryStatus.WAS_BOOST);
	    final var magnet = a.getWhatWas().wasSomething(HistoryStatus.WAS_MAGNET);
	    final var blue = a.getWhatWas().wasSomething(HistoryStatus.WAS_BLUE_LASER);
	    final var disrupt = a.getWhatWas().wasSomething(HistoryStatus.WAS_DISRUPTOR);
	    final var bomb = a.getWhatWas().wasSomething(HistoryStatus.WAS_BOMB);
	    final var heatBomb = a.getWhatWas().wasSomething(HistoryStatus.WAS_HEAT_BOMB);
	    final var iceBomb = a.getWhatWas().wasSomething(HistoryStatus.WAS_ICE_BOMB);
	    final var other = missile || stunner || boost || magnet || blue || disrupt || bomb || heatBomb || iceBomb;
	    if (other) {
		this.updateScore(0, 0, -1);
		if (boost) {
		    TankInventory.addOneBoost();
		} else if (magnet) {
		    TankInventory.addOneMagnet();
		} else if (missile) {
		    TankInventory.addOneMissile();
		} else if (stunner) {
		    TankInventory.addOneStunner();
		} else if (blue) {
		    TankInventory.addOneBlueLaser();
		} else if (disrupt) {
		    TankInventory.addOneDisruptor();
		} else if (bomb) {
		    TankInventory.addOneBomb();
		} else if (heatBomb) {
		    TankInventory.addOneHeatBomb();
		} else if (iceBomb) {
		    TankInventory.addOneIceBomb();
		}
	    } else if (laser) {
		this.updateScore(0, -1, 0);
	    } else {
		this.updateScore(-1, 0, 0);
	    }
	    try {
		this.resetPlayerToStart();
	    } catch (final InvalidArenaException iae) {
		CommonDialogs.showErrorDialog(Strings.loadError(ErrorString.TANK_LOCATION),
			GlobalStrings.loadUntranslated(UntranslatedString.PROGRAM_NAME));
		this.exitGame();
		return;
	    }
	    this.updateTank();
	    Game.updateRedo(laser, missile, stunner, boost, magnet, blue, disrupt, bomb, heatBomb, iceBomb);
	}
	LaserTankEE.getMenus().updateMenuItemState();
	this.updateScoreText();
	a.setDirtyFlags(this.plMgr.getPlayerLocationZ());
	this.redrawArena();
    }

    void unscheduleAutoMove() {
	this.autoMove = false;
    }

    private void updateInfo() {
	final var a = ArenaManager.get().getArena();
	this.levelInfo.setText(Strings.loadGame(GameString.LEVEL) + Strings.loadCommon(CommonString.SPACE)
		+ (a.getActiveLevelNumber() + 1) + Strings.loadCommon(CommonString.COLON)
		+ Strings.loadCommon(CommonString.SPACE) + a.getName().trim() + Strings.loadCommon(CommonString.SPACE)
		+ Strings.loadDialog(DialogString.ARENA_LEVEL_BY) + Strings.loadCommon(CommonString.SPACE)
		+ a.getAuthor().trim());
    }

    public void updatePositionAbsoluteNoEvents(final int z) {
	final var x = this.plMgr.getPlayerLocationX();
	final var y = this.plMgr.getPlayerLocationY();
	this.updatePositionAbsoluteNoEvents(x, y, z);
    }

    public void updatePositionAbsoluteNoEvents(final int x, final int y, final int z) {
	final var template = new ArenaObject(GameObjectID.TANK, this.plMgr.getActivePlayerNumber() + 1);
	final var m = ArenaManager.get().getArena();
	this.plMgr.savePlayerLocation();
	try {
	    if (!m.getCell(x, y, z, template.getLayer()).isConditionallySolid()) {
		if (z != 0) {
		    this.suspendAnimator();
		    m.setDirtyFlags(this.plMgr.getPlayerLocationZ());
		    m.setDirtyFlags(z);
		}
		m.setCell(this.tank.getSavedObject(), this.plMgr.getPlayerLocationX(), this.plMgr.getPlayerLocationY(),
			this.plMgr.getPlayerLocationZ(), template.getLayer());
		this.plMgr.setPlayerLocation(x, y, z);
		this.tank.setSavedObject(m.getCell(this.plMgr.getPlayerLocationX(), this.plMgr.getPlayerLocationY(),
			this.plMgr.getPlayerLocationZ(), template.getLayer()));
		m.setCell(this.tank, this.plMgr.getPlayerLocationX(), this.plMgr.getPlayerLocationY(),
			this.plMgr.getPlayerLocationZ(), template.getLayer());
		ArenaManager.get().setDirty(true);
		if (z != 0) {
		    this.resumeAnimator();
		}
	    }
	} catch (final ArrayIndexOutOfBoundsException | NullPointerException np) {
	    this.plMgr.restorePlayerLocation();
	    m.setCell(this.tank, this.plMgr.getPlayerLocationX(), this.plMgr.getPlayerLocationY(),
		    this.plMgr.getPlayerLocationZ(), template.getLayer());
	    LaserTankEE.showMessage(Strings.loadGame(GameString.OUTSIDE_ARENA));
	}
    }

    void updatePositionRelative(final int x, final int y) {
	if (!this.moving) {
	    this.moving = true;
	    if (this.mlot == null || !this.mlot.isAlive()) {
		this.mlot = new MLOTask();
	    }
	    this.mlot.activateMovement(x, y);
	    if (!this.mlot.isAlive()) {
		this.mlot.start();
	    }
	    if (this.replaying) {
		// Wait
		while (this.moving) {
		    try {
			Thread.sleep(100);
		    } catch (final InterruptedException ie) {
			// Ignore
		    }
		}
	    }
	}
    }

    public void updatePositionRelativeFrozen() {
	if (this.mlot == null || !this.mlot.isAlive()) {
	    this.mlot = new MLOTask();
	}
	final var dir = this.getTank().getDirection();
	final var unres = DirectionHelper.unresolveRelative(dir);
	final var x = unres[0];
	final var y = unres[1];
	this.mlot.activateFrozenMovement(x, y);
	if (!this.mlot.isAlive()) {
	    this.mlot.start();
	}
	if (this.replaying) {
	    // Wait
	    while (this.moving) {
		try {
		    Thread.sleep(100);
		} catch (final InterruptedException ie) {
		    // Ignore
		}
	    }
	}
    }

    public void updatePositionRelativeMolten() {
	if (this.mlot == null || !this.mlot.isAlive()) {
	    this.mlot = new MLOTask();
	}
	final var dir = this.getTank().getDirection();
	final var unres = DirectionHelper.unresolveRelative(dir);
	final var x = unres[0];
	final var y = unres[1];
	this.mlot.activateMoltenMovement(x, y);
	if (!this.mlot.isAlive()) {
	    this.mlot.start();
	}
	if (this.replaying) {
	    // Wait
	    while (this.moving) {
		try {
		    Thread.sleep(100);
		} catch (final InterruptedException ie) {
		    // Ignore
		}
	    }
	}
    }

    public void updatePositionRelativeNoEvents(final int z) {
	final var dx = this.plMgr.getPlayerLocationX();
	final var dy = this.plMgr.getPlayerLocationY();
	final var pz = this.plMgr.getPlayerLocationZ();
	final var dz = pz + z;
	this.updatePositionAbsoluteNoEvents(dx, dy, dz);
    }

    public void updatePushedIntoPositionAbsolute(final int x, final int y, final int z, final int x2, final int y2,
	    final int z2, final ArenaObject pushedInto, final ArenaObject source) {
	final var template = new ArenaObject(GameObjectID.TANK, this.plMgr.getActivePlayerNumber() + 1);
	final var m = ArenaManager.get().getArena();
	var needsFixup1 = false;
	var needsFixup2 = false;
	try {
	    if (!m.getCell(x, y, z, pushedInto.getLayer()).isConditionallySolid()) {
		final var saved = m.getCell(x, y, z, pushedInto.getLayer());
		final var there = m.getCell(x2, y2, z2, pushedInto.getLayer());
		if (there.canControl()) {
		    needsFixup1 = true;
		}
		if (saved.canControl()) {
		    needsFixup2 = true;
		}
		if (needsFixup2) {
		    m.setCell(this.tank, x, y, z, template.getLayer());
		    pushedInto.setSavedObject(saved.getSavedObject());
		    this.tank.setSavedObject(pushedInto);
		} else {
		    m.setCell(pushedInto, x, y, z, pushedInto.getLayer());
		    pushedInto.setSavedObject(saved);
		}
		if (needsFixup1) {
		    m.setCell(this.tank, x2, y2, z2, template.getLayer());
		    this.tank.setSavedObject(source);
		} else {
		    m.setCell(source, x2, y2, z2, pushedInto.getLayer());
		}
		ArenaManager.get().setDirty(true);
	    }
	} catch (final ArrayIndexOutOfBoundsException ae) {
	    final var e = new ArenaObject(GameObjectID.PLACEHOLDER);
	    m.setCell(e, x2, y2, z2, pushedInto.getLayer());
	}
    }

    public synchronized void updatePushedPosition(final int x, final int y, final int pushX, final int pushY,
	    final ArenaObject o) {
	if (this.mlot == null || !this.mlot.isAlive()) {
	    this.mlot = new MLOTask();
	}
	this.mlot.activateObjects(x, y, pushX, pushY, o);
	if (!this.mlot.isAlive()) {
	    this.mlot.start();
	}
    }

    public void updatePushedPositionLater(final int x, final int y, final int pushX, final int pushY,
	    final ArenaObject o, final int x2, final int y2, final ArenaObject other, final LaserType laserType,
	    final int forceUnits) {
	new Thread() {
	    @Override
	    public void run() {
		try {
		    other.laserEnteredAction(x2, y2, Game.this.plMgr.getPlayerLocationZ(), pushX, pushY, laserType,
			    forceUnits);
		    Game.this.waitForMLOLoop();
		    Game.this.updatePushedPosition(x, y, x + pushX, y + pushY, o);
		    Game.this.waitForMLOLoop();
		} catch (final Throwable t) {
		    LaserTankEE.logError(t);
		}
	    }
	}.start();
    }

    void updateReplay(final GameAction a, final int x, final int y) {
	this.gre.updateUndoHistory(a, x, y);
    }

    private void updateScore() {
	this.scoreMoves.setText(Strings.loadGame(GameString.MOVES) + Strings.loadCommon(CommonString.COLON)
		+ Strings.loadCommon(CommonString.SPACE) + this.st.getMoves());
	this.scoreShots.setText(Strings.loadGame(GameString.SHOTS) + Strings.loadCommon(CommonString.COLON)
		+ Strings.loadCommon(CommonString.SPACE) + this.st.getShots());
	this.scoreShots.setText(Strings.loadGame(GameString.OTHERS) + Strings.loadCommon(CommonString.COLON)
		+ Strings.loadCommon(CommonString.SPACE) + this.st.getOthers());
	this.updateScoreText();
    }

    void updateScore(final int moves, final int shots, final int others) {
	if (moves > 0) {
	    this.st.incrementMoves();
	} else if (moves < 0) {
	    this.st.decrementMoves();
	}
	if (shots > 0) {
	    this.st.incrementShots();
	} else if (shots < 0) {
	    this.st.decrementShots();
	}
	if (others > 0) {
	    this.st.incrementOthers();
	} else if (others < 0) {
	    this.st.decrementOthers();
	}
	this.scoreMoves.setText(Strings.loadGame(GameString.MOVES) + Strings.loadCommon(CommonString.COLON)
		+ Strings.loadCommon(CommonString.SPACE) + this.st.getMoves());
	this.scoreShots.setText(Strings.loadGame(GameString.SHOTS) + Strings.loadCommon(CommonString.COLON)
		+ Strings.loadCommon(CommonString.SPACE) + this.st.getShots());
	this.scoreOthers.setText(Strings.loadGame(GameString.OTHERS) + Strings.loadCommon(CommonString.COLON)
		+ Strings.loadCommon(CommonString.SPACE) + this.st.getOthers());
	this.updateScoreText();
    }

    private void updateScoreText() {
	// Ammo
	switch (this.otherAmmoMode) {
	case Game.OTHER_AMMO_MODE_MISSILES:
	    if (this.getCheatStatus(Game.CHEAT_MISSILES)) {
		this.otherAmmoLeft.setText(Strings.loadCommon(CommonString.OPEN_PARENTHESES)
			+ Strings.loadGame(GameString.MISSILES) + Strings.loadCommon(CommonString.COLON)
			+ Strings.loadCommon(CommonString.SPACE) + Strings.loadGame(GameString.INFINITE)
			+ Strings.loadCommon(CommonString.CLOSE_PARENTHESES));
	    } else {
		this.otherAmmoLeft.setText(
			Strings.loadCommon(CommonString.OPEN_PARENTHESES) + Strings.loadGame(GameString.MISSILES)
				+ Strings.loadCommon(CommonString.COLON) + Strings.loadCommon(CommonString.SPACE)
				+ TankInventory.getMissilesLeft() + Strings.loadCommon(CommonString.CLOSE_PARENTHESES));
	    }
	    break;
	case Game.OTHER_AMMO_MODE_STUNNERS:
	    if (this.getCheatStatus(Game.CHEAT_STUNNERS)) {
		this.otherAmmoLeft.setText(Strings.loadCommon(CommonString.OPEN_PARENTHESES)
			+ Strings.loadGame(GameString.STUNNERS) + Strings.loadCommon(CommonString.COLON)
			+ Strings.loadCommon(CommonString.SPACE) + Strings.loadGame(GameString.INFINITE)
			+ Strings.loadCommon(CommonString.CLOSE_PARENTHESES));
	    } else {
		this.otherAmmoLeft.setText(
			Strings.loadCommon(CommonString.OPEN_PARENTHESES) + Strings.loadGame(GameString.STUNNERS)
				+ Strings.loadCommon(CommonString.COLON) + Strings.loadCommon(CommonString.SPACE)
				+ TankInventory.getStunnersLeft() + Strings.loadCommon(CommonString.CLOSE_PARENTHESES));
	    }
	    break;
	case Game.OTHER_AMMO_MODE_BLUE_LASERS:
	    if (this.getCheatStatus(Game.CHEAT_BLUE_LASERS)) {
		this.otherAmmoLeft.setText(Strings.loadCommon(CommonString.OPEN_PARENTHESES)
			+ Strings.loadGame(GameString.BLUE_LASERS) + Strings.loadCommon(CommonString.COLON)
			+ Strings.loadCommon(CommonString.SPACE) + Strings.loadGame(GameString.INFINITE)
			+ Strings.loadCommon(CommonString.CLOSE_PARENTHESES));
	    } else {
		this.otherAmmoLeft.setText(Strings.loadCommon(CommonString.OPEN_PARENTHESES)
			+ Strings.loadGame(GameString.BLUE_LASERS) + Strings.loadCommon(CommonString.COLON)
			+ Strings.loadCommon(CommonString.SPACE) + TankInventory.getBlueLasersLeft()
			+ Strings.loadCommon(CommonString.CLOSE_PARENTHESES));
	    }
	    break;
	case Game.OTHER_AMMO_MODE_DISRUPTORS:
	    if (this.getCheatStatus(Game.CHEAT_DISRUPTORS)) {
		this.otherAmmoLeft.setText(Strings.loadCommon(CommonString.OPEN_PARENTHESES)
			+ Strings.loadGame(GameString.DISRUPTORS) + Strings.loadCommon(CommonString.COLON)
			+ Strings.loadCommon(CommonString.SPACE) + Strings.loadGame(GameString.INFINITE)
			+ Strings.loadCommon(CommonString.CLOSE_PARENTHESES));
	    } else {
		this.otherAmmoLeft.setText(Strings.loadCommon(CommonString.OPEN_PARENTHESES)
			+ Strings.loadGame(GameString.DISRUPTORS) + Strings.loadCommon(CommonString.COLON)
			+ Strings.loadCommon(CommonString.SPACE) + TankInventory.getDisruptorsLeft()
			+ Strings.loadCommon(CommonString.CLOSE_PARENTHESES));
	    }
	    break;
	default:
	    break;
	}
	// Tools
	if (this.otherToolMode == Game.OTHER_TOOL_MODE_BOOSTS) {
	    if (this.getCheatStatus(Game.CHEAT_BOOSTS)) {
		this.otherToolsLeft.setText(Strings.loadCommon(CommonString.OPEN_PARENTHESES)
			+ Strings.loadGame(GameString.BOOSTS) + Strings.loadCommon(CommonString.COLON)
			+ Strings.loadCommon(CommonString.SPACE) + Strings.loadGame(GameString.INFINITE)
			+ Strings.loadCommon(CommonString.CLOSE_PARENTHESES));
	    } else {
		this.otherToolsLeft
			.setText(Strings.loadCommon(CommonString.OPEN_PARENTHESES) + Strings.loadGame(GameString.BOOSTS)
				+ Strings.loadCommon(CommonString.COLON) + Strings.loadCommon(CommonString.SPACE)
				+ TankInventory.getBoostsLeft() + Strings.loadCommon(CommonString.CLOSE_PARENTHESES));
	    }
	} else if (this.otherToolMode == Game.OTHER_TOOL_MODE_MAGNETS) {
	    if (this.getCheatStatus(Game.CHEAT_MAGNETS)) {
		this.otherToolsLeft.setText(Strings.loadCommon(CommonString.OPEN_PARENTHESES)
			+ Strings.loadGame(GameString.MAGNETS) + Strings.loadCommon(CommonString.COLON)
			+ Strings.loadCommon(CommonString.SPACE) + Strings.loadGame(GameString.INFINITE)
			+ Strings.loadCommon(CommonString.CLOSE_PARENTHESES));
	    } else {
		this.otherToolsLeft.setText(
			Strings.loadCommon(CommonString.OPEN_PARENTHESES) + Strings.loadGame(GameString.MAGNETS)
				+ Strings.loadCommon(CommonString.COLON) + Strings.loadCommon(CommonString.SPACE)
				+ TankInventory.getMagnetsLeft() + Strings.loadCommon(CommonString.CLOSE_PARENTHESES));
	    }
	}
	// Ranges
	if (this.otherRangeMode == RangeType.BOMB) {
	    if (this.getCheatStatus(Game.CHEAT_BOMBS)) {
		this.otherRangesLeft.setText(Strings.loadCommon(CommonString.OPEN_PARENTHESES)
			+ Strings.loadGame(GameString.BOMBS) + Strings.loadCommon(CommonString.COLON)
			+ Strings.loadCommon(CommonString.SPACE) + Strings.loadGame(GameString.INFINITE)
			+ Strings.loadCommon(CommonString.CLOSE_PARENTHESES));
	    } else {
		this.otherRangesLeft
			.setText(Strings.loadCommon(CommonString.OPEN_PARENTHESES) + Strings.loadGame(GameString.BOMBS)
				+ Strings.loadCommon(CommonString.COLON) + Strings.loadCommon(CommonString.SPACE)
				+ TankInventory.getBombsLeft() + Strings.loadCommon(CommonString.CLOSE_PARENTHESES));
	    }
	} else if (this.otherRangeMode == RangeType.HEAT_BOMB) {
	    if (this.getCheatStatus(Game.CHEAT_HEAT_BOMBS)) {
		this.otherRangesLeft.setText(Strings.loadCommon(CommonString.OPEN_PARENTHESES)
			+ Strings.loadGame(GameString.HEAT_BOMBS) + Strings.loadCommon(CommonString.COLON)
			+ Strings.loadCommon(CommonString.SPACE) + Strings.loadGame(GameString.INFINITE)
			+ Strings.loadCommon(CommonString.CLOSE_PARENTHESES));
	    } else {
		this.otherRangesLeft.setText(Strings.loadCommon(CommonString.OPEN_PARENTHESES)
			+ Strings.loadGame(GameString.HEAT_BOMBS) + Strings.loadCommon(CommonString.COLON)
			+ Strings.loadCommon(CommonString.SPACE) + TankInventory.getHeatBombsLeft()
			+ Strings.loadCommon(CommonString.CLOSE_PARENTHESES));
	    }
	} else if (this.otherRangeMode == RangeType.ICE_BOMB) {
	    if (this.getCheatStatus(Game.CHEAT_ICE_BOMBS)) {
		this.otherRangesLeft.setText(Strings.loadCommon(CommonString.OPEN_PARENTHESES)
			+ Strings.loadGame(GameString.ICE_BOMBS) + Strings.loadCommon(CommonString.COLON)
			+ Strings.loadCommon(CommonString.SPACE) + Strings.loadGame(GameString.INFINITE)
			+ Strings.loadCommon(CommonString.CLOSE_PARENTHESES));
	    } else {
		this.otherRangesLeft.setText(
			Strings.loadCommon(CommonString.OPEN_PARENTHESES) + Strings.loadGame(GameString.ICE_BOMBS)
				+ Strings.loadCommon(CommonString.COLON) + Strings.loadCommon(CommonString.SPACE)
				+ TankInventory.getIceBombsLeft() + Strings.loadCommon(CommonString.CLOSE_PARENTHESES));
	    }
	}
    }

    void updateTank() {
	final var template = new ArenaObject(GameObjectID.TANK, this.plMgr.getActivePlayerNumber() + 1);
	this.tank = ArenaManager.get().getArena().getCell(this.plMgr.getPlayerLocationX(),
		this.plMgr.getPlayerLocationY(), this.plMgr.getPlayerLocationZ(), template.getLayer());
    }

    void waitForMLOLoop() {
	if (this.mlot != null && this.mlot.isAlive()) {
	    var waiting = true;
	    while (waiting) {
		try {
		    this.mlot.join();
		    waiting = false;
		} catch (final InterruptedException ie) {
		    // Ignore
		}
	    }
	}
    }

    private void writeSolution() {
	try {
	    final var activeLevel = ArenaManager.get().getArena().getActiveLevelNumber();
	    final var levelFile = ArenaManager.get().getLastUsedArena();
	    final var filename = levelFile + Strings.loadCommon(CommonString.UNDERSCORE) + activeLevel
		    + FileExtensions.getSolutionExtensionWithPeriod();
	    try (DataIOWriter file = new XDataWriter(filename,
		    GlobalStrings.loadUntranslated(UntranslatedString.SOLUTION))) {
		this.gre.writeReplay(file);
	    }
	} catch (final IOException ioe) {
	    throw new InvalidArenaException(ioe);
	}
    }
}