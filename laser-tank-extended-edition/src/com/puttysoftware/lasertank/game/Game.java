/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.game;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
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
import com.puttysoftware.lasertank.asset.Images;
import com.puttysoftware.lasertank.asset.Music;
import com.puttysoftware.lasertank.asset.Musics;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
import com.puttysoftware.lasertank.cheat.Cheats;
import com.puttysoftware.lasertank.fileio.DataIOReader;
import com.puttysoftware.lasertank.fileio.DataIOWriter;
import com.puttysoftware.lasertank.fileio.XDataReader;
import com.puttysoftware.lasertank.fileio.XDataWriter;
import com.puttysoftware.lasertank.gui.Screen;
import com.puttysoftware.lasertank.gui.dialog.CommonDialogs;
import com.puttysoftware.lasertank.helper.DifficultyHelper;
import com.puttysoftware.lasertank.helper.DirectionHelper;
import com.puttysoftware.lasertank.helper.RangeTypeHelper;
import com.puttysoftware.lasertank.index.Difficulty;
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
import com.puttysoftware.lasertank.tasks.AppTaskManager;
import com.puttysoftware.lasertank.utility.AlreadyDeadException;
import com.puttysoftware.lasertank.utility.CustomDialogs;
import com.puttysoftware.lasertank.utility.FileExtensions;
import com.puttysoftware.lasertank.utility.InvalidArenaException;
import com.puttysoftware.lasertank.utility.RCLGenerator;
import com.puttysoftware.lasertank.utility.TankInventory;

public final class Game extends Screen {
	private static Game instance;
	static final int OTHER_AMMO_MODE_MISSILES = 0;
	static final int OTHER_AMMO_MODE_STUNNERS = 1;
	static final int OTHER_AMMO_MODE_BLUE_LASERS = 2;
	private static final int OTHER_AMMO_MODE_DISRUPTORS = 3;
	static final int OTHER_TOOL_MODE_BOOSTS = 0;
	static final int OTHER_TOOL_MODE_MAGNETS = 1;
	static final int CHEAT_SWIMMING = 0;
	static final int CHEAT_GHOSTLY = 1;
	static final int CHEAT_INVINCIBLE = 2;
	private static final int CHEAT_MISSILES = 3;
	private static final int CHEAT_STUNNERS = 4;
	static final int CHEAT_BOOSTS = 5;
	static final int CHEAT_MAGNETS = 6;
	private static final int CHEAT_BLUE_LASERS = 7;
	private static final int CHEAT_DISRUPTORS = 8;
	static final int CHEAT_BOMBS = 9;
	static final int CHEAT_HEAT_BOMBS = 10;
	static final int CHEAT_ICE_BOMBS = 11;
	private static Thread animatorThread, actionThread;
	private static String[] OTHER_AMMO_CHOICES = { Strings.loadGame(GameString.MISSILES),
			Strings.loadGame(GameString.STUNNERS), Strings.loadGame(GameString.BLUE_LASERS),
			Strings.loadGame(GameString.DISRUPTORS) };
	private static String[] OTHER_TOOL_CHOICES = { Strings.loadGame(GameString.BOOSTS),
			Strings.loadGame(GameString.MAGNETS) };
	private static String[] OTHER_RANGE_CHOICES = { Strings.loadGame(GameString.BOMBS),
			Strings.loadGame(GameString.HEAT_BOMBS), Strings.loadGame(GameString.ICE_BOMBS) };
	private static Container borderPane, scorePane, infoPane, outerOutputPane;
	private static GameDraw outputPane;
	static ArenaObject tank;
	private static boolean savedGameFlag;
	private static LaserType activeLaserType;
	static final PlayerLocationManager plMgr = new PlayerLocationManager();
	private static final Cheats cMgr = new Cheats();
	private static final ScoreTracker st = new ScoreTracker();
	private static JLabel scoreMoves;
	private static JLabel scoreShots;
	private static JLabel scoreOthers;
	private static JLabel otherAmmoLeft;
	private static JLabel otherToolsLeft;
	private static JLabel otherRangesLeft;
	private static JLabel levelInfo;
	private static boolean delayedDecayActive;
	private static ArenaObject delayedDecayObject;
	static boolean laserActive;
	static boolean moving;
	private static boolean remoteDecay;
	private static AnimationTask animator;
	private static GameReplayEngine gre;
	private static boolean recording;
	private static boolean replaying;
	private static boolean newGameResult;
	private static MLOTask mlot;
	private static JDialog difficultyFrame;
	private static JList<String> difficultyList;
	private static boolean lpbLoaded;
	private static final boolean[] cheatStatus = new boolean[Game.cMgr.getCheatCount()];
	private static boolean autoMove;
	private static boolean dead;
	static int otherAmmoMode;
	static int otherToolMode;
	static RangeType otherRangeMode;
	private static final GameKeyEventHandler kHandler = new GameKeyEventHandler();
	private static final GameMouseEventHandler mHandler = new GameMouseEventHandler();
	private static final GameWindowEventHandler wHandler = new GameWindowEventHandler();
	private static final GameFocusHandler fHandler = new GameFocusHandler();

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
		final var a = ArenaManager.getArena();
		a.updateRedoHistory(new HistoryStatus(las, mis, stu, boo, mag, blu, dis, bom, hbm, ibm));
		LaserTankEE.updateMenuItemState();
	}

	static void updateUndo(final boolean las, final boolean mis, final boolean stu, final boolean boo,
			final boolean mag, final boolean blu, final boolean dis, final boolean bom, final boolean hbm,
			final boolean ibm) {
		final var a = ArenaManager.getArena();
		a.updateUndoHistory(new HistoryStatus(las, mis, stu, boo, mag, blu, dis, bom, hbm, ibm));
		LaserTankEE.updateMenuItemState();
	}

	// Constructors
	private Game() {
		Game.savedGameFlag = false;
		Game.delayedDecayActive = false;
		Game.delayedDecayObject = null;
		Game.laserActive = false;
		Game.activeLaserType = LaserType.GREEN;
		Game.remoteDecay = false;
		Game.moving = false;
		Game.gre = new GameReplayEngine();
		Game.recording = false;
		Game.replaying = false;
		Game.newGameResult = false;
		Game.lpbLoaded = false;
		Game.autoMove = false;
		Game.dead = false;
		Game.otherAmmoMode = Game.OTHER_AMMO_MODE_MISSILES;
		Game.otherToolMode = Game.OTHER_TOOL_MODE_BOOSTS;
		Game.otherRangeMode = RangeType.BOMB;
	}

	public static void abortAndWaitForMLOLoop() {
		if (Game.actionThread != null && Game.actionThread.isAlive()) {
			Game.mlot.abortLoop();
			var waiting = true;
			while (waiting) {
				try {
					Game.actionThread.join();
					waiting = false;
				} catch (final InterruptedException ie) {
					// Ignore
				}
			}
		}
		Game.moveLoopDone();
		Game.laserDone();
	}

	private static void abortMovementLaserObjectLoop() {
		Game.mlot.abortLoop();
		Game.moveLoopDone();
		Game.laserDone();
	}

	// Methods
	public static void activeLanguageChanged() {
		Game.setUpDifficultyDialog();
		Game.OTHER_AMMO_CHOICES = new String[] { Strings.loadGame(GameString.MISSILES),
				Strings.loadGame(GameString.STUNNERS), Strings.loadGame(GameString.BLUE_LASERS),
				Strings.loadGame(GameString.DISRUPTORS) };
		Game.OTHER_TOOL_CHOICES = new String[] { Strings.loadGame(GameString.BOOSTS),
				Strings.loadGame(GameString.MAGNETS) };
		Game.OTHER_RANGE_CHOICES = new String[] { Strings.loadGame(GameString.BOMBS),
				Strings.loadGame(GameString.HEAT_BOMBS), Strings.loadGame(GameString.ICE_BOMBS) };
	}

	static void cancelButtonClicked() {
		Game.difficultyFrame.setVisible(false);
		Game.newGameResult = false;
	}

	public static void changeOtherAmmoMode() {
		final var choice = CommonDialogs.showInputDialog(Strings.loadGame(GameString.WHICH_AMMO),
				Strings.loadGame(GameString.CHANGE_AMMO), Game.OTHER_AMMO_CHOICES,
				Game.OTHER_AMMO_CHOICES[Game.otherAmmoMode]);
		if (choice != null) {
			for (var z = 0; z < Game.OTHER_AMMO_CHOICES.length; z++) {
				if (choice.equals(Game.OTHER_AMMO_CHOICES[z])) {
					Game.otherAmmoMode = z;
					break;
				}
			}
			Game.updateScoreText();
			CommonDialogs.showDialog(Strings.loadGame(GameString.AMMO_CHANGED) + Strings.loadCommon(CommonString.SPACE)
					+ Game.OTHER_AMMO_CHOICES[Game.otherAmmoMode] + Strings.loadCommon(CommonString.NOTL_PERIOD));
		}
	}

	public static void changeOtherRangeMode() {
		final var choice = CommonDialogs.showInputDialog(Strings.loadGame(GameString.WHICH_RANGE),
				Strings.loadGame(GameString.CHANGE_RANGE), Game.OTHER_RANGE_CHOICES,
				Game.OTHER_RANGE_CHOICES[Game.otherRangeMode.ordinal()]);
		if (choice != null) {
			for (var z = 0; z < Game.OTHER_RANGE_CHOICES.length; z++) {
				if (choice.equals(Game.OTHER_RANGE_CHOICES[z])) {
					Game.otherRangeMode = RangeTypeHelper.fromOrdinal(z);
					break;
				}
			}
			Game.updateScoreText();
			CommonDialogs.showDialog(Strings.loadGame(GameString.RANGE_CHANGED) + Strings.loadCommon(CommonString.SPACE)
					+ Game.OTHER_RANGE_CHOICES[Game.otherRangeMode.ordinal()]
					+ Strings.loadCommon(CommonString.NOTL_PERIOD));
		}
	}

	public static void changeOtherToolMode() {
		final var choice = CommonDialogs.showInputDialog(Strings.loadGame(GameString.WHICH_TOOL),
				Strings.loadGame(GameString.CHANGE_TOOL), Game.OTHER_TOOL_CHOICES,
				Game.OTHER_TOOL_CHOICES[Game.otherToolMode]);
		if (choice != null) {
			for (var z = 0; z < Game.OTHER_TOOL_CHOICES.length; z++) {
				if (choice.equals(Game.OTHER_TOOL_CHOICES[z])) {
					Game.otherToolMode = z;
					break;
				}
			}
			Game.updateScoreText();
			CommonDialogs.showDialog(Strings.loadGame(GameString.TOOL_CHANGED) + Strings.loadCommon(CommonString.SPACE)
					+ Game.OTHER_TOOL_CHOICES[Game.otherToolMode] + Strings.loadCommon(CommonString.NOTL_PERIOD));
		}
	}

	static void clearDead() {
		Game.dead = false;
	}

	public static void clearReplay() {
		Game.gre = new GameReplayEngine();
		Game.lpbLoaded = true;
	}

	public static void decay() {
		if (Game.tank != null) {
			Game.tank.setSavedObject(new ArenaObject(GameObjectID.PLACEHOLDER));
		}
	}

	static void doAction(final GameAction action, final int x, final int y) {
		final var px = Game.getPlayerLocationX();
		final var py = Game.getPlayerLocationY();
		final var currDir = Game.tank.getDirection();
		final var newDir = DirectionHelper.resolveRelative(x, y);
		switch (action) {
			case MOVE:
				if (currDir != newDir) {
					Game.tank.setDirection(newDir);
					Sounds.play(Sound.TURN);
					Game.redrawArena();
				} else {
					Game.updatePositionRelative(x, y);
				}
				break;
			case SHOOT:
			case SHOOT_ALT_AMMO:
				Game.fireLaser(px, py, Game.tank);
				break;
			case USE_RANGE:
				Game.fireRange();
				break;
			case USE_TOOL:
				Game.updatePositionRelative(x, y);
				break;
			default:
				break;
		}
	}

	static void doDelayedDecay() {
		Game.tank.setSavedObject(Game.delayedDecayObject);
		Game.delayedDecayActive = false;
	}

	static void doRemoteDelayedDecay(final ArenaObject o) {
		o.setSavedObject(Game.delayedDecayObject);
		Game.remoteDecay = false;
		Game.delayedDecayActive = false;
	}

	public static void enterCheatCode() {
		final var rawCheat = Game.cMgr.enterCheat();
		if (rawCheat != null) {
			if (rawCheat.contains(Strings.loadGame(GameString.ENABLE_CHEAT))) {
				// Enable cheat
				final var cheat = rawCheat.substring(7);
				for (var x = 0; x < Game.cMgr.getCheatCount(); x++) {
					if (Game.cMgr.queryCheatCache(cheat) == x) {
						Game.cheatStatus[x] = true;
						break;
					}
				}
			} else {
				// Disable cheat
				final var cheat = rawCheat.substring(8);
				for (var x = 0; x < Game.cMgr.getCheatCount(); x++) {
					if (Game.cMgr.queryCheatCache(cheat) == x) {
						Game.cheatStatus[x] = false;
						break;
					}
				}
			}
		}
	}

	public static void exitGame() {
		// Halt the animator
		if (Game.animator != null) {
			Game.animator.stopAnimator();
			Game.animator = null;
		}
		// Halt the movement/laser processor
		if (Game.mlot != null) {
			Game.abortMovementLaserObjectLoop();
		}
		Game.mlot = null;
		final var m = ArenaManager.getArena();
		// Restore the arena
		m.restore();
		final var playerExists = m.doesPlayerExist(Game.plMgr.getActivePlayerNumber());
		if (playerExists) {
			try {
				Game.resetPlayerToStart();
			} catch (final InvalidArenaException iae) {
				// Ignore
			}
		} else {
			ArenaManager.setLoaded(false);
		}
		// Reset saved game flag
		Game.savedGameFlag = false;
		ArenaManager.setDirty(false);
		// Exit game
		LaserTankEE.showGUI();
	}

	public static boolean fireLaser(final int ox, final int oy, final ArenaObject shooter) {
		if (Game.otherAmmoMode == Game.OTHER_AMMO_MODE_MISSILES && Game.activeLaserType == LaserType.MISSILE
				&& TankInventory.getMissilesLeft() == 0 && !Game.getCheatStatus(Game.CHEAT_MISSILES)) {
			CommonDialogs.showDialog(Strings.loadGame(GameString.OUT_OF_MISSILES));
		} else if (Game.otherAmmoMode == Game.OTHER_AMMO_MODE_STUNNERS && Game.activeLaserType == LaserType.STUNNER
				&& TankInventory.getStunnersLeft() == 0 && !Game.getCheatStatus(Game.CHEAT_STUNNERS)) {
			CommonDialogs.showDialog(Strings.loadGame(GameString.OUT_OF_STUNNERS));
		} else if (Game.otherAmmoMode == Game.OTHER_AMMO_MODE_BLUE_LASERS && Game.activeLaserType == LaserType.BLUE
				&& TankInventory.getBlueLasersLeft() == 0 && !Game.getCheatStatus(Game.CHEAT_BLUE_LASERS)) {
			CommonDialogs.showDialog(Strings.loadGame(GameString.OUT_OF_BLUE_LASERS));
		} else {
			final var a = ArenaManager.getArena();
			if (!a.isMoveShootAllowed() && !Game.laserActive || a.isMoveShootAllowed()) {
				Game.laserActive = true;
				final var currDirection = DirectionHelper.unresolveRelative(shooter.getDirection());
				final var x = currDirection[0];
				final var y = currDirection[1];
				if (Game.mlot == null) {
					Game.mlot = new MLOTask();
				}
				if (Game.actionThread == null || !Game.actionThread.isAlive()) {
					Game.actionThread = AppTaskManager.runTrackedTask(Game.mlot);
				}
				Game.mlot.activateLasers(x, y, ox, oy, Game.activeLaserType, shooter);
				if (!Game.actionThread.isAlive()) {
					Game.actionThread = AppTaskManager.runTrackedTask(Game.mlot);
				}
				if (Game.replaying) {
					// Wait
					while (Game.laserActive) {
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

	static void fireRange() {
		// Boom!
		Sounds.play(Sound.BOOM);
		Game.updateScore(0, 0, 1);
		if (Game.otherRangeMode == RangeType.BOMB) {
			Game.updateUndo(false, false, false, false, false, false, false, true, false, false);
		} else if (Game.otherRangeMode == RangeType.HEAT_BOMB) {
			Game.updateUndo(false, false, false, false, false, false, false, false, true, false);
		} else if (Game.otherRangeMode == RangeType.ICE_BOMB) {
			Game.updateUndo(false, false, false, false, false, false, false, false, false, true);
		}
		final var a = ArenaManager.getArena();
		final var px = Game.plMgr.getPlayerLocationX();
		final var py = Game.plMgr.getPlayerLocationY();
		final var pz = Game.plMgr.getPlayerLocationZ();
		a.circularScanRange(px, py, pz, 1, Game.otherRangeMode,
				ArenaObject.getImbuedForce(RangeTypeHelper.material(Game.otherRangeMode)));
		ArenaManager.getArena().tickTimers(pz, GameAction.USE_RANGE);
		Game.updateScoreText();
	}

	public static void gameOver() {
		// Check cheats
		if (Game.getCheatStatus(Game.CHEAT_INVINCIBLE)) {
			return;
		}
		// Check dead
		if (Game.dead) {
			// Already dead
			throw new AlreadyDeadException();
		}
		// We are dead
		Game.dead = true;
		// Stop the movement/laser/object loop
		if (Game.actionThread != null && Game.actionThread.isAlive()) {
			Game.abortMovementLaserObjectLoop();
		}
		Game.mlot = null;
		Sounds.play(Sound.DEAD);
		final var choice = CustomDialogs.showDeadDialog();
		switch (choice) {
			case CommonDialogs.CANCEL_OPTION:
				// End
				Game.exitGame();
				break;
			case CommonDialogs.YES_OPTION:
				// Undo
				Game.undoLastMove();
				break;
			case CommonDialogs.NO_OPTION:
				// Restart
				try {
					Game.resetCurrentLevel();
				} catch (final InvalidArenaException iae) {
					CommonDialogs.showErrorDialog(Strings.loadError(ErrorString.TANK_LOCATION),
							GlobalStrings.loadUntranslated(UntranslatedString.PROGRAM_NAME));
					Game.exitGame();
					return;
				}
				break;
			default:
				// Closed Dialog
				Game.exitGame();
				break;
		}
	}

	public static int getActivePlayerNumber() {
		return Game.plMgr.getActivePlayerNumber();
	}

	static boolean getCheatStatus(final int cheatID) {
		return Game.cheatStatus[cheatID];
	}

	public static int getPlayerLocationX() {
		return Game.plMgr.getPlayerLocationX();
	}

	public static int getPlayerLocationY() {
		return Game.plMgr.getPlayerLocationY();
	}

	public static int getPlayerLocationZ() {
		return Game.plMgr.getPlayerLocationZ();
	}

	public static ArenaObject getTank() {
		return Game.tank;
	}

	public static int[] getTankLocation() {
		return new int[] { Game.plMgr.getPlayerLocationX(), Game.plMgr.getPlayerLocationY(),
				Game.plMgr.getPlayerLocationZ() };
	}

	public static void haltMovingObjects() {
		if (Game.actionThread != null && Game.actionThread.isAlive()) {
			Game.mlot.haltMovingObjects();
		}
	}

	@Override
	public void hideScreenHook() {
		this.removeKeyListener(Game.kHandler);
		this.removeWindowListener(Game.wHandler);
		this.removeWindowFocusListener(Game.fHandler);
	}

	static boolean isAutoMoveScheduled() {
		return Game.autoMove;
	}

	static boolean isDelayedDecayActive() {
		return Game.delayedDecayActive;
	}

	static boolean isRemoteDecayActive() {
		return Game.remoteDecay;
	}

	static boolean isReplaying() {
		return Game.replaying;
	}

	static void laserDone() {
		Game.laserActive = false;
		LaserTankEE.updateMenuItemState();
	}

	public static void loadGameHookG1(final DataIOReader arenaFile) throws IOException {
		ArenaManager.setScoresFileName(arenaFile.readString());
		Game.st.setMoves(arenaFile.readLong());
		Game.st.setShots(arenaFile.readLong());
		Game.st.setOthers(arenaFile.readLong());
	}

	public static void loadGameHookG2(final DataIOReader arenaFile) throws IOException {
		ArenaManager.setScoresFileName(arenaFile.readString());
		Game.st.setMoves(arenaFile.readLong());
		Game.st.setShots(arenaFile.readLong());
		Game.st.setOthers(arenaFile.readLong());
		TankInventory.setRedKeysLeft(arenaFile.readInt());
		TankInventory.setGreenKeysLeft(arenaFile.readInt());
		TankInventory.setBlueKeysLeft(arenaFile.readInt());
	}

	public static void loadGameHookG3(final DataIOReader arenaFile) throws IOException {
		ArenaManager.setScoresFileName(arenaFile.readString());
		Game.st.setMoves(arenaFile.readLong());
		Game.st.setShots(arenaFile.readLong());
		Game.st.setOthers(arenaFile.readLong());
		TankInventory.readInventory(arenaFile);
	}

	public static void loadGameHookG4(final DataIOReader arenaFile) throws IOException {
		ArenaManager.setScoresFileName(arenaFile.readString());
		Game.st.setMoves(arenaFile.readLong());
		Game.st.setShots(arenaFile.readLong());
		Game.st.setOthers(arenaFile.readLong());
		TankInventory.readInventory(arenaFile);
	}

	public static void loadGameHookG5(final DataIOReader arenaFile) throws IOException {
		ArenaManager.setScoresFileName(arenaFile.readString());
		Game.st.setMoves(arenaFile.readLong());
		Game.st.setShots(arenaFile.readLong());
		Game.st.setOthers(arenaFile.readLong());
		TankInventory.readInventory(arenaFile);
	}

	public static void loadGameHookG6(final DataIOReader arenaFile) throws IOException {
		ArenaManager.setScoresFileName(arenaFile.readString());
		Game.st.setMoves(arenaFile.readLong());
		Game.st.setShots(arenaFile.readLong());
		Game.st.setOthers(arenaFile.readLong());
		TankInventory.readInventory(arenaFile);
	}

	public static void loadLevel() {
		final var m = ArenaManager.getArena();
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
			Game.suspendAnimator();
			m.restore();
			m.switchLevel(number);
			ArenaManager.getArena().setDirtyFlags(Game.plMgr.getPlayerLocationZ());
			m.resetHistoryEngine();
			Game.gre = new GameReplayEngine();
			LaserTankEE.updateMenuItemState();
			Game.processLevelExists();
		}
	}

	public static void loadReplay(final GameAction a, final int x, final int y) {
		Game.gre.updateRedoHistory(a, x, y);
	}

	static void markTankAsDirty() {
		ArenaManager.getArena().markAsDirty(Game.plMgr.getPlayerLocationX(), Game.plMgr.getPlayerLocationY(),
				Game.plMgr.getPlayerLocationZ());
	}

	public static void morph(final ArenaObject morphInto, final int x, final int y, final int z, final int w) {
		final var m = ArenaManager.getArena();
		try {
			m.setCell(morphInto, x, y, z, w);
			ArenaManager.setDirty(true);
		} catch (final ArrayIndexOutOfBoundsException | NullPointerException np) {
			// Do nothing
		}
	}

	static void moveLoopDone() {
		Game.moving = false;
		LaserTankEE.updateMenuItemState();
	}

	public static boolean newGame() {
		Game.difficultyList.clearSelection();
		final var retVal = Game.getEnabledDifficulties();
		Game.difficultyList.setSelectedIndices(retVal);
		Game.difficultyFrame.setVisible(true);
		return Game.newGameResult;
	}

	static void offsetPlayerLocationX(final int val) {
		Game.plMgr.offsetPlayerLocationX(val);
	}

	static void offsetPlayerLocationY(final int val) {
		Game.plMgr.offsetPlayerLocationY(val);
	}

	static void okButtonClicked() {
		Game.difficultyFrame.setVisible(false);
		if (Game.difficultyList.isSelectedIndex(Difficulty.KIDS.ordinal() - 1)) {
			Settings.setKidsDifficultyEnabled(true);
		} else {
			Settings.setKidsDifficultyEnabled(false);
		}
		if (Game.difficultyList.isSelectedIndex(Difficulty.EASY.ordinal() - 1)) {
			Settings.setEasyDifficultyEnabled(true);
		} else {
			Settings.setEasyDifficultyEnabled(false);
		}
		if (Game.difficultyList.isSelectedIndex(Difficulty.MEDIUM.ordinal() - 1)) {
			Settings.setMediumDifficultyEnabled(true);
		} else {
			Settings.setMediumDifficultyEnabled(false);
		}
		if (Game.difficultyList.isSelectedIndex(Difficulty.HARD.ordinal() - 1)) {
			Settings.setHardDifficultyEnabled(true);
		} else {
			Settings.setHardDifficultyEnabled(false);
		}
		if (Game.difficultyList.isSelectedIndex(Difficulty.DEADLY.ordinal() - 1)) {
			Settings.setDeadlyDifficultyEnabled(true);
		} else {
			Settings.setDeadlyDifficultyEnabled(false);
		}
		Game.newGameResult = true;
	}

	public static void playArena() {
		if (ArenaManager.getLoaded()) {
			LaserTankEE.setOnGameScreen();
			ArenaManager.getArena().switchLevel(0);
			final var res = ArenaManager.getArena().switchToNextLevelWithDifficulty(Game.getEnabledDifficulties());
			if (res) {
				try {
					Game.resetPlayerToStart();
				} catch (final InvalidArenaException iae) {
					CommonDialogs.showErrorDialog(Strings.loadError(ErrorString.TANK_LOCATION),
							GlobalStrings.loadUntranslated(UntranslatedString.PROGRAM_NAME));
					Game.exitGame();
					return;
				}
				Game.updateTank();
				Game.tank.setSavedObject(new ArenaObject(GameObjectID.PLACEHOLDER));
				Game.st.setScoreFile(ArenaManager.getScoresFileName());
				if (!Game.savedGameFlag) {
					Game.st.resetScore(ArenaManager.getScoresFileName());
				}
				Game.updateInfo();
				Game.borderPane.removeAll();
				Game.borderPane.add(Game.outerOutputPane, BorderLayout.CENTER);
				Game.borderPane.add(Game.scorePane, BorderLayout.NORTH);
				Game.borderPane.add(Game.infoPane, BorderLayout.SOUTH);
				LaserTankEE.updateMenuItemState();
				ArenaManager.getArena().setDirtyFlags(Game.plMgr.getPlayerLocationZ());
				Game.redrawArena();
				Game.updateScoreText();
				Game.replaying = false;
				// Start animator, if enabled
				if (Settings.enableAnimation()) {
					Game.animator = new AnimationTask();
					Game.animatorThread = AppTaskManager.runTrackedTask(Game.animator);
				}
			} else {
				CommonDialogs.showDialog(Strings.loadGame(GameString.NO_LEVEL_WITH_DIFFICULTY));
				LaserTankEE.showGUI();
			}
		} else {
			CommonDialogs.showDialog(Strings.loadError(ErrorString.NO_ARENA_OPENED));
		}
	}

	@Override
	protected void populateMainPanel() {
		Game.borderPane = new Container();
		Game.borderPane.setLayout(new BorderLayout());
		Game.outerOutputPane = RCLGenerator.generateRowColumnLabels();
		Game.outputPane = new GameDraw();
		Game.outputPane.setLayout(new GridLayout(GameViewingWindowManager.getViewingWindowSizeX(),
				GameViewingWindowManager.getViewingWindowSizeY()));
		Game.outputPane.addMouseListener(Game.mHandler);
		Game.scoreMoves = new JLabel(Strings.loadGame(GameString.MOVES) + Strings.loadCommon(CommonString.COLON)
				+ Strings.loadCommon(CommonString.SPACE) + Strings.loadCommon(CommonString.ZERO));
		Game.scoreShots = new JLabel(Strings.loadGame(GameString.SHOTS) + Strings.loadCommon(CommonString.COLON)
				+ Strings.loadCommon(CommonString.SPACE) + Strings.loadCommon(CommonString.ZERO));
		Game.scoreOthers = new JLabel(Strings.loadGame(GameString.OTHERS) + Strings.loadCommon(CommonString.COLON)
				+ Strings.loadCommon(CommonString.SPACE) + Strings.loadCommon(CommonString.ZERO));
		Game.otherAmmoLeft = new JLabel(
				Strings.loadCommon(CommonString.OPEN_PARENTHESES) + Strings.loadGame(GameString.MISSILES)
						+ Strings.loadCommon(CommonString.COLON) + Strings.loadCommon(CommonString.SPACE)
						+ Strings.loadCommon(CommonString.ZERO) + Strings.loadCommon(CommonString.CLOSE_PARENTHESES));
		Game.otherToolsLeft = new JLabel(
				Strings.loadCommon(CommonString.OPEN_PARENTHESES) + Strings.loadGame(GameString.BOOSTS)
						+ Strings.loadCommon(CommonString.COLON) + Strings.loadCommon(CommonString.SPACE)
						+ Strings.loadCommon(CommonString.ZERO) + Strings.loadCommon(CommonString.CLOSE_PARENTHESES));
		Game.otherRangesLeft = new JLabel(
				Strings.loadCommon(CommonString.OPEN_PARENTHESES) + Strings.loadGame(GameString.BOMBS)
						+ Strings.loadCommon(CommonString.COLON) + Strings.loadCommon(CommonString.SPACE)
						+ Strings.loadCommon(CommonString.ZERO) + Strings.loadCommon(CommonString.CLOSE_PARENTHESES));
		Game.scorePane = new Container();
		Game.scorePane.setLayout(new FlowLayout());
		Game.scorePane.add(Game.scoreMoves);
		Game.scorePane.add(Game.scoreShots);
		Game.scorePane.add(Game.scoreOthers);
		Game.scorePane.add(Game.otherAmmoLeft);
		Game.scorePane.add(Game.otherToolsLeft);
		Game.scorePane.add(Game.otherRangesLeft);
		Game.levelInfo = new JLabel(Strings.loadCommon(CommonString.SPACE));
		Game.infoPane = new Container();
		Game.infoPane.setLayout(new FlowLayout());
		Game.infoPane.add(Game.levelInfo);
		Game.scoreMoves.setLabelFor(Game.outputPane);
		Game.scoreShots.setLabelFor(Game.outputPane);
		Game.scoreOthers.setLabelFor(Game.outputPane);
		Game.otherAmmoLeft.setLabelFor(Game.outputPane);
		Game.otherToolsLeft.setLabelFor(Game.outputPane);
		Game.otherRangesLeft.setLabelFor(Game.outputPane);
		Game.levelInfo.setLabelFor(Game.outputPane);
		Game.outerOutputPane.add(Game.outputPane, BorderLayout.CENTER);
		Game.borderPane.add(Game.outerOutputPane, BorderLayout.CENTER);
		Game.borderPane.add(Game.scorePane, BorderLayout.NORTH);
		Game.borderPane.add(Game.infoPane, BorderLayout.SOUTH);
		Game.setUpDifficultyDialog();
		this.setTitle(GlobalStrings.loadUntranslated(UntranslatedString.PROGRAM_NAME));
	}

	public static void previousLevel() {
		final var m = ArenaManager.getArena();
		m.resetHistoryEngine();
		Game.gre = new GameReplayEngine();
		LaserTankEE.updateMenuItemState();
		Game.suspendAnimator();
		m.restore();
		if (m.doesLevelExistOffset(-1)) {
			m.switchLevelOffset(-1);
			final var levelExists = m.switchToPreviousLevelWithDifficulty(Game.getEnabledDifficulties());
			if (levelExists) {
				m.setDirtyFlags(Game.plMgr.getPlayerLocationZ());
				Game.processLevelExists();
			} else {
				CommonDialogs.showErrorDialog(Strings.loadError(ErrorString.NO_PREVIOUS_LEVEL),
						GlobalStrings.loadUntranslated(UntranslatedString.PROGRAM_NAME));
			}
		} else {
			CommonDialogs.showErrorDialog(Strings.loadError(ErrorString.NO_PREVIOUS_LEVEL),
					GlobalStrings.loadUntranslated(UntranslatedString.PROGRAM_NAME));
		}
	}

	private static void processLevelExists() {
		try {
			Game.resetPlayerToStart();
		} catch (final InvalidArenaException iae) {
			CommonDialogs.showErrorDialog(Strings.loadError(ErrorString.TANK_LOCATION),
					GlobalStrings.loadUntranslated(UntranslatedString.PROGRAM_NAME));
			Game.exitGame();
			return;
		}
		Game.updateTank();
		Game.st.resetScore(ArenaManager.getScoresFileName());
		TankInventory.resetInventory();
		Game.scoreMoves.setText(Strings.loadGame(GameString.MOVES) + Strings.loadCommon(CommonString.COLON)
				+ Strings.loadCommon(CommonString.SPACE) + Strings.loadCommon(CommonString.ZERO));
		Game.scoreShots.setText(Strings.loadGame(GameString.SHOTS) + Strings.loadCommon(CommonString.COLON)
				+ Strings.loadCommon(CommonString.SPACE) + Strings.loadCommon(CommonString.ZERO));
		Game.scoreOthers.setText(Strings.loadGame(GameString.OTHERS) + Strings.loadCommon(CommonString.COLON)
				+ Strings.loadCommon(CommonString.SPACE) + Strings.loadCommon(CommonString.ZERO));
		switch (Game.otherAmmoMode) {
			case Game.OTHER_AMMO_MODE_MISSILES:
				if (Game.getCheatStatus(Game.CHEAT_MISSILES)) {
					Game.otherAmmoLeft.setText(Strings.loadCommon(CommonString.OPEN_PARENTHESES)
							+ Strings.loadGame(GameString.MISSILES) + Strings.loadCommon(CommonString.COLON)
							+ Strings.loadCommon(CommonString.SPACE) + Strings.loadGame(GameString.INFINITE)
							+ Strings.loadCommon(CommonString.CLOSE_PARENTHESES));
				} else {
					Game.otherAmmoLeft.setText(Strings.loadCommon(CommonString.OPEN_PARENTHESES)
							+ Strings.loadGame(GameString.MISSILES) + Strings.loadCommon(CommonString.COLON)
							+ Strings.loadCommon(CommonString.SPACE) + Strings.loadCommon(CommonString.ZERO)
							+ Strings.loadCommon(CommonString.CLOSE_PARENTHESES));
				}
				break;
			case Game.OTHER_AMMO_MODE_STUNNERS:
				if (Game.getCheatStatus(Game.CHEAT_STUNNERS)) {
					Game.otherAmmoLeft.setText(Strings.loadCommon(CommonString.OPEN_PARENTHESES)
							+ Strings.loadGame(GameString.STUNNERS) + Strings.loadCommon(CommonString.COLON)
							+ Strings.loadCommon(CommonString.SPACE) + Strings.loadGame(GameString.INFINITE)
							+ Strings.loadCommon(CommonString.CLOSE_PARENTHESES));
				} else {
					Game.otherAmmoLeft.setText(Strings.loadCommon(CommonString.OPEN_PARENTHESES)
							+ Strings.loadGame(GameString.STUNNERS) + Strings.loadCommon(CommonString.COLON)
							+ Strings.loadCommon(CommonString.SPACE) + Strings.loadCommon(CommonString.ZERO)
							+ Strings.loadCommon(CommonString.CLOSE_PARENTHESES));
				}
				break;
			case Game.OTHER_AMMO_MODE_BLUE_LASERS:
				if (Game.getCheatStatus(Game.CHEAT_BLUE_LASERS)) {
					Game.otherAmmoLeft.setText(Strings.loadCommon(CommonString.OPEN_PARENTHESES)
							+ Strings.loadGame(GameString.BLUE_LASERS) + Strings.loadCommon(CommonString.COLON)
							+ Strings.loadCommon(CommonString.SPACE) + Strings.loadGame(GameString.INFINITE)
							+ Strings.loadCommon(CommonString.CLOSE_PARENTHESES));
				} else {
					Game.otherAmmoLeft.setText(Strings.loadCommon(CommonString.OPEN_PARENTHESES)
							+ Strings.loadGame(GameString.BLUE_LASERS) + Strings.loadCommon(CommonString.COLON)
							+ Strings.loadCommon(CommonString.SPACE) + Strings.loadCommon(CommonString.ZERO)
							+ Strings.loadCommon(CommonString.CLOSE_PARENTHESES));
				}
				break;
			case Game.OTHER_AMMO_MODE_DISRUPTORS:
				if (Game.getCheatStatus(Game.CHEAT_DISRUPTORS)) {
					Game.otherAmmoLeft.setText(Strings.loadCommon(CommonString.OPEN_PARENTHESES)
							+ Strings.loadGame(GameString.DISRUPTORS) + Strings.loadCommon(CommonString.COLON)
							+ Strings.loadCommon(CommonString.SPACE) + Strings.loadGame(GameString.INFINITE)
							+ Strings.loadCommon(CommonString.CLOSE_PARENTHESES));
				} else {
					Game.otherAmmoLeft.setText(Strings.loadCommon(CommonString.OPEN_PARENTHESES)
							+ Strings.loadGame(GameString.DISRUPTORS) + Strings.loadCommon(CommonString.COLON)
							+ Strings.loadCommon(CommonString.SPACE) + Strings.loadCommon(CommonString.ZERO)
							+ Strings.loadCommon(CommonString.CLOSE_PARENTHESES));
				}
				break;
			default:
				break;
		}
		Game.updateInfo();
		Game.redrawArena();
		Game.resumeAnimator();
	}

	private static boolean readSolution() {
		try {
			final var activeLevel = ArenaManager.getArena().getActiveLevelNumber();
			final var levelFile = ArenaManager.getLastUsedArena();
			final var filename = levelFile + Strings.loadCommon(CommonString.UNDERSCORE) + activeLevel
					+ FileExtensions.getSolutionExtensionWithPeriod();
			try (DataIOReader file = new XDataReader(filename,
					GlobalStrings.loadUntranslated(UntranslatedString.SOLUTION))) {
				Game.gre = GameReplayEngine.readReplay(file);
			}
			return true;
		} catch (final IOException ioe) {
			throw new InvalidArenaException(ioe);
		}
	}

	public static void redoLastMove() {
		final var a = ArenaManager.getArena();
		if (a.tryRedo()) {
			Game.moving = false;
			Game.laserActive = false;
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
				Game.updateScore(0, 0, -1);
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
				Game.updateScore(0, 1, 0);
			} else {
				Game.updateScore(1, 0, 0);
			}
			try {
				Game.resetPlayerToStart();
			} catch (final InvalidArenaException iae) {
				CommonDialogs.showErrorDialog(Strings.loadError(ErrorString.TANK_LOCATION),
						GlobalStrings.loadUntranslated(UntranslatedString.PROGRAM_NAME));
				Game.exitGame();
				return;
			}
			Game.updateTank();
			Game.updateUndo(laser, missile, stunner, boost, magnet, blue, disrupt, bomb, heatBomb, iceBomb);
		}
		LaserTankEE.updateMenuItemState();
		Game.updateScoreText();
		a.setDirtyFlags(Game.plMgr.getPlayerLocationZ());
		Game.redrawArena();
	}

	public synchronized static void redrawArena() {
		// Draw the arena
		final var a = ArenaManager.getArena();
		final var drawGrid = Game.outputPane.getGrid();
		int x, y;
		final var pz = Game.plMgr.getPlayerLocationZ();
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
		a.clearDirtyFlags(Game.plMgr.getPlayerLocationZ());
		Game.outputPane.repaint();
	}

	public static void remoteDelayedDecayTo(final ArenaObject obj) {
		Game.delayedDecayActive = true;
		Game.delayedDecayObject = obj;
		Game.remoteDecay = true;
	}

	static void replayDone() {
		Game.replaying = false;
	}

	static boolean replayLastMove() {
		if (Game.gre.tryRedo()) {
			Game.gre.redo();
			final var action = Game.gre.getAction();
			final var x = Game.gre.getX();
			final var y = Game.gre.getY();
			Game.doAction(action, x, y);
			return true;
		}
		return false;
	}

	public static void replaySolution() {
		if (Game.lpbLoaded) {
			Game.replaying = true;
			// Turn recording off
			Game.recording = false;
			LaserTankEE.disableRecording();
			try {
				Game.resetCurrentLevel(false);
			} catch (final InvalidArenaException iae) {
				CommonDialogs.showErrorDialog(Strings.loadError(ErrorString.TANK_LOCATION),
						GlobalStrings.loadUntranslated(UntranslatedString.PROGRAM_NAME));
				Game.exitGame();
				return;
			}
			final var rt = new ReplayTask();
			AppTaskManager.runTask(rt);
		} else {
			final var success = Game.readSolution();
			if (!success) {
				CommonDialogs.showErrorDialog(Strings.loadError(ErrorString.NO_SOLUTION_FILE),
						GlobalStrings.loadUntranslated(UntranslatedString.PROGRAM_NAME));
			} else {
				Game.replaying = true;
				// Turn recording off
				Game.recording = false;
				LaserTankEE.disableRecording();
				try {
					Game.resetCurrentLevel(false);
				} catch (final InvalidArenaException iae) {
					CommonDialogs.showErrorDialog(Strings.loadError(ErrorString.TANK_LOCATION),
							GlobalStrings.loadUntranslated(UntranslatedString.PROGRAM_NAME));
					Game.exitGame();
					return;
				}
				final var rt = new ReplayTask();
				AppTaskManager.runTask(rt);
			}
		}
	}

	public static void resetCurrentLevel() throws InvalidArenaException {
		Game.resetLevel(true);
	}

	private static void resetCurrentLevel(final boolean flag) throws InvalidArenaException {
		Game.resetLevel(flag);
	}

	public static void resetGameState() {
		final var m = ArenaManager.getArena();
		ArenaManager.setDirty(false);
		m.restore();
		Game.setSavedGameFlag(false);
		Game.st.resetScore();
		final var playerExists = m.doesPlayerExist(Game.plMgr.getActivePlayerNumber());
		if (playerExists) {
			Game.plMgr.setPlayerLocation(m.getStartColumn(0), m.getStartRow(0), m.getStartFloor(0));
		}
	}

	private static void resetLevel(final boolean flag) throws InvalidArenaException {
		final var m = ArenaManager.getArena();
		if (flag) {
			m.resetHistoryEngine();
		}
		ArenaManager.setDirty(true);
		if (Game.actionThread != null && Game.actionThread.isAlive()) {
			Game.abortMovementLaserObjectLoop();
		}
		Game.moving = false;
		Game.laserActive = false;
		TankInventory.resetInventory();
		m.restore();
		m.setDirtyFlags(Game.plMgr.getPlayerLocationZ());
		final var playerExists = m.doesPlayerExist(Game.plMgr.getActivePlayerNumber());
		if (playerExists) {
			Game.st.resetScore(ArenaManager.getScoresFileName());
			Game.resetPlayerToStart();
			Game.updateTank();
			m.clearVirtualGrid();
			Game.updateScore();
			Game.decay();
			Game.redrawArena();
		}
		LaserTankEE.updateMenuItemState();
	}

	public static void resetPlayerLocation() {
		Game.plMgr.resetPlayerLocation();
	}

	public static void resetPlayerToStart() throws InvalidArenaException {
		final var m = ArenaManager.getArena();
		final var found = m.findPlayer(1);
		if (found == null) {
			throw new InvalidArenaException(Strings.loadError(ErrorString.TANK_LOCATION));
		}
		Game.plMgr.setPlayerLocation(found[0], found[1], found[2]);
	}

	private static void resetTank() {
		ArenaManager.getArena().setCell(Game.tank, Game.plMgr.getPlayerLocationX(), Game.plMgr.getPlayerLocationY(),
				Game.plMgr.getPlayerLocationZ(), Game.tank.layer());
		Game.markTankAsDirty();
	}

	static void restorePlayerLocation() {
		Game.plMgr.restorePlayerLocation();
	}

	private static void resumeAnimator() {
		if (Game.animator == null) {
			Game.animator = new AnimationTask();
			Game.animatorThread = AppTaskManager.runTrackedTask(Game.animator);
		}
	}

	public static void saveGameHook(final DataIOWriter arenaFile) throws IOException {
		arenaFile.writeString(ArenaManager.getScoresFileName());
		arenaFile.writeLong(Game.st.getMoves());
		arenaFile.writeLong(Game.st.getShots());
		arenaFile.writeLong(Game.st.getOthers());
		TankInventory.writeInventory(arenaFile);
	}

	static void savePlayerLocation() {
		Game.plMgr.savePlayerLocation();
	}

	static void scheduleAutoMove() {
		Game.autoMove = true;
	}

	public static void setActivePlayerNumber(final int value) {
		Game.plMgr.setActivePlayerNumber(value);
	}

	public static void setLaserType(final LaserType type) {
		Game.activeLaserType = type;
	}

	public static void setNormalTank() {
		final var saveTank = Game.tank;
		Game.tank = new ArenaObject(GameObjectID.TANK, saveTank.getDirection(), saveTank.getNumber());
		Game.resetTank();
	}

	public static void setPlayerLocation(final int valX, final int valY, final int valZ) {
		Game.plMgr.setPlayerLocation(valX, valY, valZ);
	}

	public static void setPowerfulTank() {
		final var saveTank = Game.tank;
		Game.tank = new ArenaObject(GameObjectID.POWER_TANK, saveTank.getDirection(), saveTank.getNumber());
		Game.resetTank();
	}

	public static void setSavedGameFlag(final boolean value) {
		Game.savedGameFlag = value;
	}

	private static void setUpDifficultyDialog() {
		// Set up Difficulty Dialog
		final var dahandler = new GameDifficultyActionEventHandler();
		final var dwhandler = new GameDifficultyWindowEventHandler();
		Game.difficultyFrame = new JDialog((JFrame) null, Strings.loadGame(GameString.SELECT_DIFFICULTY));
		final var difficultyPane = new Container();
		final var listPane = new Container();
		final var buttonPane = new Container();
		Game.difficultyList = new JList<>(DifficultyHelper.getNames());
		final var okButton = new JButton(Strings.loadDialog(DialogString.OK_BUTTON));
		final var cancelButton = new JButton(Strings.loadDialog(DialogString.CANCEL_BUTTON));
		buttonPane.setLayout(new FlowLayout());
		buttonPane.add(okButton);
		buttonPane.add(cancelButton);
		listPane.setLayout(new FlowLayout());
		listPane.add(Game.difficultyList);
		difficultyPane.setLayout(new BorderLayout());
		difficultyPane.add(listPane, BorderLayout.CENTER);
		difficultyPane.add(buttonPane, BorderLayout.SOUTH);
		Game.difficultyFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		Game.difficultyFrame.setModal(true);
		Game.difficultyFrame.setResizable(false);
		okButton.setDefaultCapable(true);
		cancelButton.setDefaultCapable(false);
		Game.difficultyFrame.getRootPane().setDefaultButton(okButton);
		Game.difficultyFrame.addWindowListener(dwhandler);
		okButton.addActionListener(dahandler);
		cancelButton.addActionListener(dahandler);
		Game.difficultyFrame.setContentPane(difficultyPane);
		Game.difficultyFrame.pack();
	}

	public static void showScoreTable() {
		Game.st.showScoreTable();
	}

	@Override
	public void showScreenHook() {
		this.addKeyListener(Game.kHandler);
		this.addWindowListener(Game.wHandler);
		this.addWindowFocusListener(Game.fHandler);
		Musics.play(Music.GAME);
	}

	private static void solvedArena() {
		TankInventory.resetInventory();
		Game.exitGame();
	}

	public static void solvedLevel(final boolean playSound) {
		if (playSound) {
			Sounds.play(Sound.END_LEVEL);
		}
		final var m = ArenaManager.getArena();
		if (playSound) {
			if (Game.recording) {
				Game.writeSolution();
			}
			if (Game.st.checkScore()) {
				Game.st.commitScore();
			}
		}
		m.resetHistoryEngine();
		Game.gre = new GameReplayEngine();
		LaserTankEE.updateMenuItemState();
		Game.suspendAnimator();
		m.restore();
		if (m.doesLevelExistOffset(1)) {
			m.switchLevelOffset(1);
			final var levelExists = m.switchToNextLevelWithDifficulty(Game.getEnabledDifficulties());
			if (levelExists) {
				m.setDirtyFlags(Game.plMgr.getPlayerLocationZ());
				Game.processLevelExists();
			} else {
				Game.solvedArena();
			}
		} else {
			Game.solvedArena();
		}
	}

	private static void suspendAnimator() {
		if (Game.animator != null && Game.animatorThread != null) {
			Game.animator.stopAnimator();
			try {
				Game.animatorThread.join();
			} catch (final InterruptedException ie) {
				// Ignore
			}
			Game.animator = null;
			Game.animatorThread = null;
		}
	}

	public static void togglePlayerInstance() {
		Game.plMgr.togglePlayerInstance();
	}

	public static void toggleRecording() {
		Game.recording = !Game.recording;
	}

	public static void undoLastMove() {
		final var a = ArenaManager.getArena();
		if (a.tryUndo()) {
			Game.moving = false;
			Game.laserActive = false;
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
				Game.updateScore(0, 0, -1);
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
				Game.updateScore(0, -1, 0);
			} else {
				Game.updateScore(-1, 0, 0);
			}
			try {
				Game.resetPlayerToStart();
			} catch (final InvalidArenaException iae) {
				CommonDialogs.showErrorDialog(Strings.loadError(ErrorString.TANK_LOCATION),
						GlobalStrings.loadUntranslated(UntranslatedString.PROGRAM_NAME));
				Game.exitGame();
				return;
			}
			Game.updateTank();
			Game.updateRedo(laser, missile, stunner, boost, magnet, blue, disrupt, bomb, heatBomb, iceBomb);
		}
		LaserTankEE.updateMenuItemState();
		Game.updateScoreText();
		a.setDirtyFlags(Game.plMgr.getPlayerLocationZ());
		Game.redrawArena();
	}

	static void unscheduleAutoMove() {
		Game.autoMove = false;
	}

	private static void updateInfo() {
		final var a = ArenaManager.getArena();
		Game.levelInfo.setText(Strings.loadGame(GameString.LEVEL) + Strings.loadCommon(CommonString.SPACE)
				+ (a.getActiveLevelNumber() + 1) + Strings.loadCommon(CommonString.COLON)
				+ Strings.loadCommon(CommonString.SPACE) + a.getName().trim() + Strings.loadCommon(CommonString.SPACE)
				+ Strings.loadDialog(DialogString.ARENA_LEVEL_BY) + Strings.loadCommon(CommonString.SPACE)
				+ a.getAuthor().trim());
	}

	public static void updatePositionAbsoluteNoEvents(final int z) {
		final var x = Game.plMgr.getPlayerLocationX();
		final var y = Game.plMgr.getPlayerLocationY();
		Game.updatePositionAbsoluteNoEvents(x, y, z);
	}

	public static void updatePositionAbsoluteNoEvents(final int x, final int y, final int z) {
		final var template = new ArenaObject(GameObjectID.TANK);
		template.setIndex(Game.plMgr.getActivePlayerNumber() + 1);
		final var m = ArenaManager.getArena();
		Game.plMgr.savePlayerLocation();
		try {
			if (!m.getCell(x, y, z, template.layer()).isConditionallySolid()) {
				if (z != 0) {
					Game.suspendAnimator();
					m.setDirtyFlags(Game.plMgr.getPlayerLocationZ());
					m.setDirtyFlags(z);
				}
				m.setCell(Game.tank.getSavedObject(), Game.plMgr.getPlayerLocationX(), Game.plMgr.getPlayerLocationY(),
						Game.plMgr.getPlayerLocationZ(), template.layer());
				Game.plMgr.setPlayerLocation(x, y, z);
				Game.tank.setSavedObject(m.getCell(Game.plMgr.getPlayerLocationX(), Game.plMgr.getPlayerLocationY(),
						Game.plMgr.getPlayerLocationZ(), template.layer()));
				m.setCell(Game.tank, Game.plMgr.getPlayerLocationX(), Game.plMgr.getPlayerLocationY(),
						Game.plMgr.getPlayerLocationZ(), template.layer());
				ArenaManager.setDirty(true);
				if (z != 0) {
					Game.resumeAnimator();
				}
			}
		} catch (final ArrayIndexOutOfBoundsException | NullPointerException np) {
			Game.plMgr.restorePlayerLocation();
			m.setCell(Game.tank, Game.plMgr.getPlayerLocationX(), Game.plMgr.getPlayerLocationY(),
					Game.plMgr.getPlayerLocationZ(), template.layer());
			LaserTankEE.showMessage(Strings.loadGame(GameString.OUTSIDE_ARENA));
		}
	}

	static void updatePositionRelative(final int x, final int y) {
		if (!Game.moving) {
			Game.moving = true;
			if (Game.mlot == null) {
				Game.mlot = new MLOTask();
			}
			Game.mlot.activateMovement(x, y);
			if (Game.actionThread == null || !Game.actionThread.isAlive()) {
				Game.actionThread = AppTaskManager.runTrackedTask(Game.mlot);
			}
			if (Game.replaying) {
				// Wait
				while (Game.moving) {
					try {
						Thread.sleep(100);
					} catch (final InterruptedException ie) {
						// Ignore
					}
				}
			}
		}
	}

	public static void updatePositionRelativeFrozen() {
		if (Game.mlot == null) {
			Game.mlot = new MLOTask();
		}
		final var dir = Game.getTank().getDirection();
		final var unres = DirectionHelper.unresolveRelative(dir);
		final var x = unres[0];
		final var y = unres[1];
		Game.mlot.activateFrozenMovement(x, y);
		if (Game.actionThread == null || !Game.actionThread.isAlive()) {
			Game.actionThread = AppTaskManager.runTrackedTask(Game.mlot);
		}
		if (Game.replaying) {
			// Wait
			while (Game.moving) {
				try {
					Thread.sleep(100);
				} catch (final InterruptedException ie) {
					// Ignore
				}
			}
		}
	}

	public static void updatePositionRelativeMolten() {
		if (Game.mlot == null) {
			Game.mlot = new MLOTask();
		}
		final var dir = Game.getTank().getDirection();
		final var unres = DirectionHelper.unresolveRelative(dir);
		final var x = unres[0];
		final var y = unres[1];
		Game.mlot.activateMoltenMovement(x, y);
		if (Game.actionThread == null || !Game.actionThread.isAlive()) {
			Game.actionThread = AppTaskManager.runTrackedTask(Game.mlot);
		}
		if (Game.replaying) {
			// Wait
			while (Game.moving) {
				try {
					Thread.sleep(100);
				} catch (final InterruptedException ie) {
					// Ignore
				}
			}
		}
	}

	public static void updatePositionRelativeNoEvents(final int z) {
		final var dx = Game.plMgr.getPlayerLocationX();
		final var dy = Game.plMgr.getPlayerLocationY();
		final var pz = Game.plMgr.getPlayerLocationZ();
		final var dz = pz + z;
		Game.updatePositionAbsoluteNoEvents(dx, dy, dz);
	}

	public static void updatePushedIntoPositionAbsolute(final int x, final int y, final int z, final int x2,
			final int y2, final int z2, final ArenaObject pushedInto, final ArenaObject source) {
		final var template = new ArenaObject(GameObjectID.TANK);
		template.setIndex(Game.plMgr.getActivePlayerNumber() + 1);
		final var m = ArenaManager.getArena();
		var needsFixup1 = false;
		var needsFixup2 = false;
		try {
			if (!m.getCell(x, y, z, pushedInto.layer()).isConditionallySolid()) {
				final var saved = m.getCell(x, y, z, pushedInto.layer());
				final var there = m.getCell(x2, y2, z2, pushedInto.layer());
				if (there.canControl()) {
					needsFixup1 = true;
				}
				if (saved.canControl()) {
					needsFixup2 = true;
				}
				if (needsFixup2) {
					m.setCell(Game.tank, x, y, z, template.layer());
					pushedInto.setSavedObject(saved.getSavedObject());
					Game.tank.setSavedObject(pushedInto);
				} else {
					m.setCell(pushedInto, x, y, z, pushedInto.layer());
					pushedInto.setSavedObject(saved);
				}
				if (needsFixup1) {
					m.setCell(Game.tank, x2, y2, z2, template.layer());
					Game.tank.setSavedObject(source);
				} else {
					m.setCell(source, x2, y2, z2, pushedInto.layer());
				}
				ArenaManager.setDirty(true);
			}
		} catch (final ArrayIndexOutOfBoundsException ae) {
			final var e = new ArenaObject(GameObjectID.PLACEHOLDER);
			m.setCell(e, x2, y2, z2, pushedInto.layer());
		}
	}

	public synchronized static void updatePushedPosition(final int x, final int y, final int pushX, final int pushY,
			final ArenaObject o) {
		if (Game.mlot == null) {
			Game.mlot = new MLOTask();
		}
		Game.mlot.activateObjects(x, y, pushX, pushY, o);
		if (Game.actionThread == null || !Game.actionThread.isAlive()) {
			Game.actionThread = AppTaskManager.runTrackedTask(Game.mlot);
		}
	}

	public static void updatePushedPositionLater(final int x, final int y, final int pushX, final int pushY,
			final ArenaObject o, final int x2, final int y2, final ArenaObject other, final LaserType laserType,
			final int forceUnits) {
		AppTaskManager
				.runTask(new UpdatePushedPositionTask(x, y, pushX, pushY, o, x2, y2, other, laserType, forceUnits));
	}

	static void updateReplay(final GameAction a, final int x, final int y) {
		Game.gre.updateUndoHistory(a, x, y);
	}

	private static void updateScore() {
		Game.scoreMoves.setText(Strings.loadGame(GameString.MOVES) + Strings.loadCommon(CommonString.COLON)
				+ Strings.loadCommon(CommonString.SPACE) + Game.st.getMoves());
		Game.scoreShots.setText(Strings.loadGame(GameString.SHOTS) + Strings.loadCommon(CommonString.COLON)
				+ Strings.loadCommon(CommonString.SPACE) + Game.st.getShots());
		Game.scoreShots.setText(Strings.loadGame(GameString.OTHERS) + Strings.loadCommon(CommonString.COLON)
				+ Strings.loadCommon(CommonString.SPACE) + Game.st.getOthers());
		Game.updateScoreText();
	}

	static void updateScore(final int moves, final int shots, final int others) {
		if (moves > 0) {
			Game.st.incrementMoves();
		} else if (moves < 0) {
			Game.st.decrementMoves();
		}
		if (shots > 0) {
			Game.st.incrementShots();
		} else if (shots < 0) {
			Game.st.decrementShots();
		}
		if (others > 0) {
			Game.st.incrementOthers();
		} else if (others < 0) {
			Game.st.decrementOthers();
		}
		Game.scoreMoves.setText(Strings.loadGame(GameString.MOVES) + Strings.loadCommon(CommonString.COLON)
				+ Strings.loadCommon(CommonString.SPACE) + Game.st.getMoves());
		Game.scoreShots.setText(Strings.loadGame(GameString.SHOTS) + Strings.loadCommon(CommonString.COLON)
				+ Strings.loadCommon(CommonString.SPACE) + Game.st.getShots());
		Game.scoreOthers.setText(Strings.loadGame(GameString.OTHERS) + Strings.loadCommon(CommonString.COLON)
				+ Strings.loadCommon(CommonString.SPACE) + Game.st.getOthers());
		Game.updateScoreText();
	}

	private static void updateScoreText() {
		// Ammo
		switch (Game.otherAmmoMode) {
			case Game.OTHER_AMMO_MODE_MISSILES:
				if (Game.getCheatStatus(Game.CHEAT_MISSILES)) {
					Game.otherAmmoLeft.setText(Strings.loadCommon(CommonString.OPEN_PARENTHESES)
							+ Strings.loadGame(GameString.MISSILES) + Strings.loadCommon(CommonString.COLON)
							+ Strings.loadCommon(CommonString.SPACE) + Strings.loadGame(GameString.INFINITE)
							+ Strings.loadCommon(CommonString.CLOSE_PARENTHESES));
				} else {
					Game.otherAmmoLeft.setText(
							Strings.loadCommon(CommonString.OPEN_PARENTHESES) + Strings.loadGame(GameString.MISSILES)
									+ Strings.loadCommon(CommonString.COLON) + Strings.loadCommon(CommonString.SPACE)
									+ TankInventory.getMissilesLeft()
									+ Strings.loadCommon(CommonString.CLOSE_PARENTHESES));
				}
				break;
			case Game.OTHER_AMMO_MODE_STUNNERS:
				if (Game.getCheatStatus(Game.CHEAT_STUNNERS)) {
					Game.otherAmmoLeft.setText(Strings.loadCommon(CommonString.OPEN_PARENTHESES)
							+ Strings.loadGame(GameString.STUNNERS) + Strings.loadCommon(CommonString.COLON)
							+ Strings.loadCommon(CommonString.SPACE) + Strings.loadGame(GameString.INFINITE)
							+ Strings.loadCommon(CommonString.CLOSE_PARENTHESES));
				} else {
					Game.otherAmmoLeft.setText(
							Strings.loadCommon(CommonString.OPEN_PARENTHESES) + Strings.loadGame(GameString.STUNNERS)
									+ Strings.loadCommon(CommonString.COLON) + Strings.loadCommon(CommonString.SPACE)
									+ TankInventory.getStunnersLeft()
									+ Strings.loadCommon(CommonString.CLOSE_PARENTHESES));
				}
				break;
			case Game.OTHER_AMMO_MODE_BLUE_LASERS:
				if (Game.getCheatStatus(Game.CHEAT_BLUE_LASERS)) {
					Game.otherAmmoLeft.setText(Strings.loadCommon(CommonString.OPEN_PARENTHESES)
							+ Strings.loadGame(GameString.BLUE_LASERS) + Strings.loadCommon(CommonString.COLON)
							+ Strings.loadCommon(CommonString.SPACE) + Strings.loadGame(GameString.INFINITE)
							+ Strings.loadCommon(CommonString.CLOSE_PARENTHESES));
				} else {
					Game.otherAmmoLeft.setText(Strings.loadCommon(CommonString.OPEN_PARENTHESES)
							+ Strings.loadGame(GameString.BLUE_LASERS) + Strings.loadCommon(CommonString.COLON)
							+ Strings.loadCommon(CommonString.SPACE) + TankInventory.getBlueLasersLeft()
							+ Strings.loadCommon(CommonString.CLOSE_PARENTHESES));
				}
				break;
			case Game.OTHER_AMMO_MODE_DISRUPTORS:
				if (Game.getCheatStatus(Game.CHEAT_DISRUPTORS)) {
					Game.otherAmmoLeft.setText(Strings.loadCommon(CommonString.OPEN_PARENTHESES)
							+ Strings.loadGame(GameString.DISRUPTORS) + Strings.loadCommon(CommonString.COLON)
							+ Strings.loadCommon(CommonString.SPACE) + Strings.loadGame(GameString.INFINITE)
							+ Strings.loadCommon(CommonString.CLOSE_PARENTHESES));
				} else {
					Game.otherAmmoLeft.setText(Strings.loadCommon(CommonString.OPEN_PARENTHESES)
							+ Strings.loadGame(GameString.DISRUPTORS) + Strings.loadCommon(CommonString.COLON)
							+ Strings.loadCommon(CommonString.SPACE) + TankInventory.getDisruptorsLeft()
							+ Strings.loadCommon(CommonString.CLOSE_PARENTHESES));
				}
				break;
			default:
				break;
		}
		// Tools
		if (Game.otherToolMode == Game.OTHER_TOOL_MODE_BOOSTS) {
			if (Game.getCheatStatus(Game.CHEAT_BOOSTS)) {
				Game.otherToolsLeft.setText(Strings.loadCommon(CommonString.OPEN_PARENTHESES)
						+ Strings.loadGame(GameString.BOOSTS) + Strings.loadCommon(CommonString.COLON)
						+ Strings.loadCommon(CommonString.SPACE) + Strings.loadGame(GameString.INFINITE)
						+ Strings.loadCommon(CommonString.CLOSE_PARENTHESES));
			} else {
				Game.otherToolsLeft
						.setText(Strings.loadCommon(CommonString.OPEN_PARENTHESES) + Strings.loadGame(GameString.BOOSTS)
								+ Strings.loadCommon(CommonString.COLON) + Strings.loadCommon(CommonString.SPACE)
								+ TankInventory.getBoostsLeft() + Strings.loadCommon(CommonString.CLOSE_PARENTHESES));
			}
		} else if (Game.otherToolMode == Game.OTHER_TOOL_MODE_MAGNETS) {
			if (Game.getCheatStatus(Game.CHEAT_MAGNETS)) {
				Game.otherToolsLeft.setText(Strings.loadCommon(CommonString.OPEN_PARENTHESES)
						+ Strings.loadGame(GameString.MAGNETS) + Strings.loadCommon(CommonString.COLON)
						+ Strings.loadCommon(CommonString.SPACE) + Strings.loadGame(GameString.INFINITE)
						+ Strings.loadCommon(CommonString.CLOSE_PARENTHESES));
			} else {
				Game.otherToolsLeft.setText(
						Strings.loadCommon(CommonString.OPEN_PARENTHESES) + Strings.loadGame(GameString.MAGNETS)
								+ Strings.loadCommon(CommonString.COLON) + Strings.loadCommon(CommonString.SPACE)
								+ TankInventory.getMagnetsLeft() + Strings.loadCommon(CommonString.CLOSE_PARENTHESES));
			}
		}
		// Ranges
		if (Game.otherRangeMode == RangeType.BOMB) {
			if (Game.getCheatStatus(Game.CHEAT_BOMBS)) {
				Game.otherRangesLeft.setText(Strings.loadCommon(CommonString.OPEN_PARENTHESES)
						+ Strings.loadGame(GameString.BOMBS) + Strings.loadCommon(CommonString.COLON)
						+ Strings.loadCommon(CommonString.SPACE) + Strings.loadGame(GameString.INFINITE)
						+ Strings.loadCommon(CommonString.CLOSE_PARENTHESES));
			} else {
				Game.otherRangesLeft
						.setText(Strings.loadCommon(CommonString.OPEN_PARENTHESES) + Strings.loadGame(GameString.BOMBS)
								+ Strings.loadCommon(CommonString.COLON) + Strings.loadCommon(CommonString.SPACE)
								+ TankInventory.getBombsLeft() + Strings.loadCommon(CommonString.CLOSE_PARENTHESES));
			}
		} else if (Game.otherRangeMode == RangeType.HEAT_BOMB) {
			if (Game.getCheatStatus(Game.CHEAT_HEAT_BOMBS)) {
				Game.otherRangesLeft.setText(Strings.loadCommon(CommonString.OPEN_PARENTHESES)
						+ Strings.loadGame(GameString.HEAT_BOMBS) + Strings.loadCommon(CommonString.COLON)
						+ Strings.loadCommon(CommonString.SPACE) + Strings.loadGame(GameString.INFINITE)
						+ Strings.loadCommon(CommonString.CLOSE_PARENTHESES));
			} else {
				Game.otherRangesLeft.setText(Strings.loadCommon(CommonString.OPEN_PARENTHESES)
						+ Strings.loadGame(GameString.HEAT_BOMBS) + Strings.loadCommon(CommonString.COLON)
						+ Strings.loadCommon(CommonString.SPACE) + TankInventory.getHeatBombsLeft()
						+ Strings.loadCommon(CommonString.CLOSE_PARENTHESES));
			}
		} else if (Game.otherRangeMode == RangeType.ICE_BOMB) {
			if (Game.getCheatStatus(Game.CHEAT_ICE_BOMBS)) {
				Game.otherRangesLeft.setText(Strings.loadCommon(CommonString.OPEN_PARENTHESES)
						+ Strings.loadGame(GameString.ICE_BOMBS) + Strings.loadCommon(CommonString.COLON)
						+ Strings.loadCommon(CommonString.SPACE) + Strings.loadGame(GameString.INFINITE)
						+ Strings.loadCommon(CommonString.CLOSE_PARENTHESES));
			} else {
				Game.otherRangesLeft.setText(
						Strings.loadCommon(CommonString.OPEN_PARENTHESES) + Strings.loadGame(GameString.ICE_BOMBS)
								+ Strings.loadCommon(CommonString.COLON) + Strings.loadCommon(CommonString.SPACE)
								+ TankInventory.getIceBombsLeft() + Strings.loadCommon(CommonString.CLOSE_PARENTHESES));
			}
		}
	}

	static void updateTank() {
		final var template = new ArenaObject(GameObjectID.TANK);
		template.setIndex(Game.plMgr.getActivePlayerNumber() + 1);
		Game.tank = ArenaManager.getArena().getCell(Game.plMgr.getPlayerLocationX(), Game.plMgr.getPlayerLocationY(),
				Game.plMgr.getPlayerLocationZ(), template.layer());
	}

	static void waitForMLOLoop() {
		if (Game.actionThread != null && Game.actionThread.isAlive()) {
			var waiting = true;
			while (waiting) {
				try {
					Game.actionThread.join();
					waiting = false;
				} catch (final InterruptedException ie) {
					// Ignore
				}
			}
		}
	}

	private static void writeSolution() {
		try {
			final var activeLevel = ArenaManager.getArena().getActiveLevelNumber();
			final var levelFile = ArenaManager.getLastUsedArena();
			final var filename = levelFile + Strings.loadCommon(CommonString.UNDERSCORE) + activeLevel
					+ FileExtensions.getSolutionExtensionWithPeriod();
			try (DataIOWriter file = new XDataWriter(filename,
					GlobalStrings.loadUntranslated(UntranslatedString.SOLUTION))) {
				Game.gre.writeReplay(file);
			}
		} catch (final IOException ioe) {
			throw new InvalidArenaException(ioe);
		}
	}
}