/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.puttysoftware.diane.gui.GUIPrinter;
import com.puttysoftware.diane.gui.dialog.CommonDialogs;
import com.puttysoftware.lasertank.accelerator.Accelerators;
import com.puttysoftware.lasertank.arena.Arena;
import com.puttysoftware.lasertank.arena.ArenaManager;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
import com.puttysoftware.lasertank.datatype.LaserTankPlayback;
import com.puttysoftware.lasertank.index.Era;
import com.puttysoftware.lasertank.locale.DialogString;
import com.puttysoftware.lasertank.locale.EditorString;
import com.puttysoftware.lasertank.locale.ErrorString;
import com.puttysoftware.lasertank.locale.MenuString;
import com.puttysoftware.lasertank.locale.Strings;
import com.puttysoftware.lasertank.locale.global.GlobalStrings;
import com.puttysoftware.lasertank.locale.global.UntranslatedString;
import com.puttysoftware.lasertank.settings.Settings;

public class MenuManager {
	private class MenuHandler implements ActionListener {
		public MenuHandler() {
			// Do nothing
		}

		// Handle menus
		@Override
		public void actionPerformed(final ActionEvent e) {
			try {
				final var app = LaserTankEE.getApplication();
				final var game = app.getGameManager();
				final var editor = app.getEditor();
				final var menu = MenuManager.this;
				var loaded = false;
				final var cmd = e.getActionCommand();
				if (cmd.equals(Strings.loadMenu(MenuString.ITEM_NEW))) {
					loaded = app.getEditor().newArena();
					app.getArenaManager().setLoaded(loaded);
				} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_OPEN))) {
					loaded = app.getArenaManager().loadArena();
					app.getArenaManager().setLoaded(loaded);
				} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_OPEN_DEFAULT))) {
					loaded = app.getArenaManager().loadArenaDefault();
					app.getArenaManager().setLoaded(loaded);
				} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_CLOSE))) {
					// Close the window
					if (app.isInEditorMode()) {
						app.getEditor().handleCloseWindow();
					} else if (app.isInGameMode()) {
						var saved = true;
						var status = 0;
						if (app.getArenaManager().getDirty()) {
							status = ArenaManager.showSaveDialog();
							if (status == CommonDialogs.YES_OPTION) {
								saved = app.getArenaManager().saveArena(app.getArenaManager().isArenaProtected());
							} else if (status == CommonDialogs.CANCEL_OPTION) {
								saved = false;
							} else {
								app.getArenaManager().setDirty(false);
							}
						}
						if (saved) {
							app.getGameManager().exitGame();
						}
					}
					app.getGUIManager().showGUI();
				} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_SAVE))) {
					if (app.getArenaManager().getLoaded()) {
						app.getArenaManager().saveArena(app.getArenaManager().isArenaProtected());
					} else {
						CommonDialogs.showDialog(Strings.loadError(ErrorString.NO_ARENA_OPENED));
					}
				} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_SAVE_AS))) {
					if (app.getArenaManager().getLoaded()) {
						app.getArenaManager().saveArenaAs(false);
					} else {
						CommonDialogs.showDialog(Strings.loadError(ErrorString.NO_ARENA_OPENED));
					}
				} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_SAVE_AS_PROTECTED))) {
					if (app.getArenaManager().getLoaded()) {
						app.getArenaManager().saveArenaAs(true);
					} else {
						CommonDialogs.showDialog(Strings.loadError(ErrorString.NO_ARENA_OPENED));
					}
				} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_SETTINGS))) {
					// Show preferences dialog
					Settings.showSettings();
				} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_PRINT_GAMEBOARD))) {
					GUIPrinter.printScreen();
				} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_EXIT))
						|| cmd.equals(Strings.loadMenu(MenuString.ITEM_QUIT))) {
					// Exit program
					if (app.getGUIManager().quitHandler()) {
						System.exit(0);
					}
				} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_PLAY))) {
					// Play the current arena
					final var proceed = app.getGameManager().newGame();
					if (proceed) {
						app.exitCurrentMode();
						app.getGameManager().playArena();
					}
				} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_EDIT))) {
					// Edit the current arena
					app.exitCurrentMode();
					app.getEditor().editArena();
				} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_USE_CLASSIC_ACCELERATORS))) {
					// Toggle accelerators
					app.getMenuManager().toggleAccelerators();
				} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_RESET_CURRENT_LEVEL))) {
					final var result = CommonDialogs.showConfirmDialog(
							Strings.loadDialog(DialogString.CONFIRM_RESET_CURRENT_LEVEL),
							GlobalStrings.loadUntranslated(UntranslatedString.PROGRAM_NAME));
					if (result == CommonDialogs.YES_OPTION) {
						game.abortAndWaitForMLOLoop();
						game.resetCurrentLevel();
					}
				} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_SHOW_SCORE_TABLE))) {
					game.showScoreTable();
				} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_REPLAY_SOLUTION))) {
					game.abortAndWaitForMLOLoop();
					game.replaySolution();
				} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_RECORD_SOLUTION))) {
					game.toggleRecording();
				} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_LOAD_PLAYBACK_FILE))) {
					game.abortAndWaitForMLOLoop();
					LaserTankPlayback.loadLPB();
				} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_PREVIOUS_LEVEL))) {
					game.abortAndWaitForMLOLoop();
					game.previousLevel();
				} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_SKIP_LEVEL))) {
					game.abortAndWaitForMLOLoop();
					game.solvedLevel(false);
				} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_LOAD_LEVEL))) {
					game.abortAndWaitForMLOLoop();
					game.loadLevel();
				} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_SHOW_HINT))) {
					CommonDialogs.showDialog(app.getArenaManager().getArena().getHint().trim());
				} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_CHEATS))) {
					game.enterCheatCode();
				} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_CHANGE_OTHER_AMMO))) {
					game.changeOtherAmmoMode();
				} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_CHANGE_OTHER_TOOL))) {
					game.changeOtherToolMode();
				} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_CHANGE_OTHER_RANGE))) {
					game.changeOtherRangeMode();
				} else if (cmd.equals(Strings.loadEra(Era.DISTANT_PAST))) {
					// Time Travel: Distant Past
					Sounds.play(Sound.ERA_CHANGE);
					app.getArenaManager().getArena().switchEra(Era.DISTANT_PAST.ordinal());
					menu.gameEraDistantPast.setSelected(true);
					menu.gameEraPast.setSelected(false);
					menu.gameEraPresent.setSelected(false);
					menu.gameEraFuture.setSelected(false);
					menu.gameEraDistantFuture.setSelected(false);
				} else if (cmd.equals(Strings.loadEra(Era.PAST))) {
					// Time Travel: Past
					Sounds.play(Sound.ERA_CHANGE);
					app.getArenaManager().getArena().switchEra(Era.PAST.ordinal());
					menu.gameEraDistantPast.setSelected(false);
					menu.gameEraPast.setSelected(true);
					menu.gameEraPresent.setSelected(false);
					menu.gameEraFuture.setSelected(false);
					menu.gameEraDistantFuture.setSelected(false);
				} else if (cmd.equals(Strings.loadEra(Era.PRESENT))) {
					// Time Travel: Present
					Sounds.play(Sound.ERA_CHANGE);
					app.getArenaManager().getArena().switchEra(Era.PRESENT.ordinal());
					menu.gameEraDistantPast.setSelected(false);
					menu.gameEraPast.setSelected(false);
					menu.gameEraPresent.setSelected(true);
					menu.gameEraFuture.setSelected(false);
					menu.gameEraDistantFuture.setSelected(false);
				} else if (cmd.equals(Strings.loadEra(Era.FUTURE))) {
					// Time Travel: Future
					Sounds.play(Sound.ERA_CHANGE);
					app.getArenaManager().getArena().switchEra(Era.FUTURE.ordinal());
					menu.gameEraDistantPast.setSelected(false);
					menu.gameEraPast.setSelected(false);
					menu.gameEraPresent.setSelected(false);
					menu.gameEraFuture.setSelected(true);
					menu.gameEraDistantFuture.setSelected(false);
				} else if (cmd.equals(Strings.loadEra(Era.DISTANT_FUTURE))) {
					// Time Travel: Distant Future
					Sounds.play(Sound.ERA_CHANGE);
					app.getArenaManager().getArena().switchEra(Era.DISTANT_FUTURE.ordinal());
					menu.gameEraDistantPast.setSelected(false);
					menu.gameEraPast.setSelected(false);
					menu.gameEraPresent.setSelected(false);
					menu.gameEraFuture.setSelected(false);
					menu.gameEraDistantFuture.setSelected(true);
				} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_UNDO))) {
					// Undo most recent action
					if (app.isInEditorMode()) {
						editor.undo();
					} else if (app.isInGameMode()) {
						app.getGameManager().abortAndWaitForMLOLoop();
						app.getGameManager().undoLastMove();
					}
				} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_REDO))) {
					// Redo most recent undone action
					if (app.isInEditorMode()) {
						editor.redo();
					} else if (app.isInGameMode()) {
						app.getGameManager().abortAndWaitForMLOLoop();
						app.getGameManager().redoLastMove();
					}
				} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_CUT_LEVEL))) {
					// Cut Level
					final var level = editor.getEditorLocationU();
					app.getArenaManager().getArena().cutLevel();
					editor.fixLimits();
					editor.updateEditorLevelAbsolute(level);
				} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_COPY_LEVEL))) {
					// Copy Level
					app.getArenaManager().getArena().copyLevel();
				} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_PASTE_LEVEL))) {
					// Paste Level
					app.getArenaManager().getArena().pasteLevel();
					editor.fixLimits();
					editor.redrawEditor();
				} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_INSERT_LEVEL_FROM_CLIPBOARD))) {
					// Insert Level From Clipboard
					app.getArenaManager().getArena().insertLevelFromClipboard();
					editor.fixLimits();
				} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_CLEAR_HISTORY))) {
					// Clear undo/redo history, confirm first
					final var res = CommonDialogs.showConfirmDialog(
							Strings.loadDialog(DialogString.CONFIRM_CLEAR_HISTORY),
							Strings.loadEditor(EditorString.EDITOR));
					if (res == CommonDialogs.YES_OPTION) {
						editor.clearHistory();
					}
				} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_GO_TO_LEVEL))) {
					// Go To Level
					editor.goToLevelHandler();
				} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_UP_ONE_FLOOR))) {
					// Go up one floor
					editor.updateEditorPosition(1, 0);
				} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_DOWN_ONE_FLOOR))) {
					// Go down one floor
					editor.updateEditorPosition(-1, 0);
				} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_UP_ONE_LEVEL))) {
					// Go up one level
					editor.updateEditorPosition(0, 1);
				} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_DOWN_ONE_LEVEL))) {
					// Go down one level
					editor.updateEditorPosition(0, -1);
				} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_ADD_A_LEVEL))) {
					// Add a level
					editor.addLevel();
				} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_REMOVE_A_LEVEL))) {
					// Remove a level
					editor.removeLevel();
				} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_FILL_CURRENT_LEVEL))) {
					// Fill level
					editor.fillLevel();
				} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_RESIZE_CURRENT_LEVEL))) {
					// Resize level
					editor.resizeLevel();
				} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_LEVEL_SETTINGS))) {
					// Set Level Preferences
					editor.setLevelSettings();
				} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_SET_START_POINT))) {
					// Set Start Point
					editor.editPlayerLocation();
				} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_CHANGE_LAYER))) {
					// Change Layer
					editor.changeLayer();
				} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_ENABLE_GLOBAL_MOVE_SHOOT))) {
					// Enable Global Move-Shoot
					LaserTankEE.getApplication().getArenaManager().getArena().setMoveShootAllowedGlobally(true);
					menu.editorGlobalMoveShoot.setText(Strings.loadMenu(MenuString.ITEM_DISABLE_GLOBAL_MOVE_SHOOT));
				} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_DISABLE_GLOBAL_MOVE_SHOOT))) {
					// Disable Global Move-Shoot
					menu.editorGlobalMoveShoot.setText(Strings.loadMenu(MenuString.ITEM_ENABLE_GLOBAL_MOVE_SHOOT));
					app.getArenaManager().getArena().setMoveShootAllowedGlobally(false);
				} else if (cmd.equals(Strings.loadEra(Era.DISTANT_PAST))) {
					// Time Travel: Distant Past
					app.getArenaManager().getArena().switchEra(Era.DISTANT_PAST.ordinal());
					menu.editorEraDistantPast.setSelected(true);
					menu.editorEraPast.setSelected(false);
					menu.editorEraPresent.setSelected(false);
					menu.editorEraFuture.setSelected(false);
					menu.editorEraDistantFuture.setSelected(false);
				} else if (cmd.equals(Strings.loadEra(Era.PAST))) {
					// Time Travel: Past
					app.getArenaManager().getArena().switchEra(Era.PAST.ordinal());
					menu.editorEraDistantPast.setSelected(false);
					menu.editorEraPast.setSelected(true);
					menu.editorEraPresent.setSelected(false);
					menu.editorEraFuture.setSelected(false);
					menu.editorEraDistantFuture.setSelected(false);
				} else if (cmd.equals(Strings.loadEra(Era.PRESENT))) {
					// Time Travel: Present
					app.getArenaManager().getArena().switchEra(Era.PRESENT.ordinal());
					menu.editorEraDistantPast.setSelected(false);
					menu.editorEraPast.setSelected(false);
					menu.editorEraPresent.setSelected(true);
					menu.editorEraFuture.setSelected(false);
					menu.editorEraDistantFuture.setSelected(false);
				} else if (cmd.equals(Strings.loadEra(Era.FUTURE))) {
					// Time Travel: Future
					app.getArenaManager().getArena().switchEra(Era.FUTURE.ordinal());
					menu.editorEraDistantPast.setSelected(false);
					menu.editorEraPast.setSelected(false);
					menu.editorEraPresent.setSelected(false);
					menu.editorEraFuture.setSelected(true);
					menu.editorEraDistantFuture.setSelected(false);
				} else if (cmd.equals(Strings.loadEra(Era.DISTANT_FUTURE))) {
					// Time Travel: Distant Future
					app.getArenaManager().getArena().switchEra(Era.DISTANT_FUTURE.ordinal());
					menu.editorEraDistantPast.setSelected(false);
					menu.editorEraPast.setSelected(false);
					menu.editorEraPresent.setSelected(false);
					menu.editorEraFuture.setSelected(false);
					menu.editorEraDistantFuture.setSelected(true);
				} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_ABOUT_LASERTANK))) {
					app.getAboutDialog().showScreen();
				}
				app.getMenuManager().updateMenuItemState();
			} catch (final Exception ex) {
				LaserTankEE.logError(ex);
			}
		}
	}

	// Fields
	private final JMenuBar menuBar;
	private JMenuItem fileNew, fileOpen, fileOpenDefault, fileClose, fileSave, fileSaveAs, fileSaveAsProtected,
			filePrint, fileExit;
	private JMenuItem editUndo, editRedo, editCutLevel, editCopyLevel, editPasteLevel, editPasteAndInsert,
			editPreferences;
	private JMenuItem playPlay, playEdit;
	private JCheckBoxMenuItem playToggleAccelerators;
	private JMenu gameTimeTravelSubMenu;
	JCheckBoxMenuItem gameEraDistantPast, gameEraPast, gameEraPresent, gameEraFuture, gameEraDistantFuture;
	private JMenuItem gameReset, gameShowTable, gameReplaySolution, gameLoadLPB, gamePreviousLevel, gameSkipLevel,
			gameLoadLevel, gameShowHint, gameCheats, gameChangeOtherAmmoMode, gameChangeOtherToolMode,
			gameChangeOtherRangeMode;
	private JCheckBoxMenuItem gameRecordSolution;
	private JMenu editorTimeTravelSubMenu;
	JCheckBoxMenuItem editorEraDistantPast, editorEraPast, editorEraPresent, editorEraFuture, editorEraDistantFuture;
	private JMenuItem editorClearHistory, editorGoToLevel, editorUpOneFloor, editorDownOneFloor, editorUpOneLevel,
			editorDownOneLevel, editorAddLevel, editorRemoveLevel, editorLevelPreferences, editorSetStartPoint,
			editorFillLevel, editorResizeLevel, editorChangeLayer, editorGlobalMoveShoot;
	private JMenuItem helpAbout;
	private Accelerators accel;

	// Constructors
	public MenuManager() {
		this.menuBar = new JMenuBar();
		this.accel = Accelerators.getInstance(Settings.useClassicAccelerators());
	}

	// Methods
	public final void updateMenuItemState() {
		try {
			final var app = LaserTankEE.getApplication();
			final var editor = app.getEditor();
			if (app.getArenaManager().getLoaded()) {
				this.enableLoadedCommands();
			} else {
				this.disableLoadedCommands();
			}
			if (app.getArenaManager().getDirty()) {
				this.enableDirtyCommands();
			} else {
				this.disableDirtyCommands();
			}
			if (app.isInEditorMode()) {
				final var m = app.getArenaManager().getArena();
				if (m.getLevels() == Arena.getMinLevels()) {
					this.disableRemoveLevel();
				} else {
					this.enableRemoveLevel();
				}
				if (m.getLevels() == Arena.getMaxLevels()) {
					this.disableAddLevel();
				} else {
					this.enableAddLevel();
				}
				try {
					if (editor.checkLimitMinZ()) {
						this.disableDownOneFloor();
					} else {
						this.enableDownOneFloor();
					}
					if (editor.checkLimitMaxZ()) {
						this.disableUpOneFloor();
					} else {
						this.enableUpOneFloor();
					}
				} catch (final NullPointerException npe) {
					this.disableDownOneFloor();
					this.disableUpOneFloor();
				}
				try {
					if (editor.checkLimitMinU()) {
						this.disableDownOneLevel();
					} else {
						this.enableDownOneLevel();
					}
					if (editor.checkLimitMaxU()) {
						this.disableUpOneLevel();
					} else {
						this.enableUpOneLevel();
					}
				} catch (final NullPointerException npe) {
					this.disableDownOneLevel();
					this.disableUpOneLevel();
				}
				if (editor.checkSetStartPoint()) {
					this.enableSetStartPoint();
				} else {
					this.disableSetStartPoint();
				}
				if (!editor.tryUndo()) {
					this.disableUndo();
				} else {
					this.enableUndo();
				}
				if (!editor.tryRedo()) {
					this.disableRedo();
				} else {
					this.enableRedo();
				}
				if (editor.tryBoth()) {
					this.disableClearHistory();
				} else {
					this.enableClearHistory();
				}
			}
			if (app.isInGameMode()) {
				final var a = app.getArenaManager().getArena();
				if (a.tryUndo()) {
					this.enableUndo();
				} else {
					this.disableUndo();
				}
				if (a.tryRedo()) {
					this.enableRedo();
				} else {
					this.disableRedo();
				}
			}
			final var a = app.getArenaManager().getArena();
			if (a != null && a.isPasteBlocked()) {
				this.disablePasteLevel();
				this.disableInsertLevelFromClipboard();
			} else {
				this.enablePasteLevel();
				this.enableInsertLevelFromClipboard();
			}
			if (a != null && a.isCutBlocked()) {
				this.disableCutLevel();
			} else {
				this.enableCutLevel();
			}
		} catch (final Exception ex) {
			LaserTankEE.logError(ex);
		}
	}

	public final JMenuBar getMenuBar() {
		return this.menuBar;
	}

	public final void disableRecording() {
		this.gameRecordSolution.setSelected(false);
	}

	private void disableAddLevel() {
		this.editorAddLevel.setEnabled(false);
	}

	private void disableClearHistory() {
		this.editorClearHistory.setEnabled(false);
	}

	private void disableCutLevel() {
		this.editCutLevel.setEnabled(false);
	}

	private void disableDownOneFloor() {
		this.editorDownOneFloor.setEnabled(false);
	}

	private void disableDownOneLevel() {
		this.editorDownOneLevel.setEnabled(false);
	}

	private void disableInsertLevelFromClipboard() {
		this.editPasteAndInsert.setEnabled(false);
	}

	private void disablePasteLevel() {
		this.editPasteLevel.setEnabled(false);
	}

	private void disableRedo() {
		this.editRedo.setEnabled(false);
	}

	private void disableRemoveLevel() {
		this.editorRemoveLevel.setEnabled(false);
	}

	private void disableSetStartPoint() {
		this.editorSetStartPoint.setEnabled(false);
	}

	private void disableUndo() {
		this.editUndo.setEnabled(false);
	}

	private void disableUpOneFloor() {
		this.editorUpOneFloor.setEnabled(false);
	}

	private void disableUpOneLevel() {
		this.editorUpOneLevel.setEnabled(false);
	}

	private void enableAddLevel() {
		this.editorAddLevel.setEnabled(true);
	}

	private void enableClearHistory() {
		this.editorClearHistory.setEnabled(true);
	}

	private void enableCutLevel() {
		this.editCutLevel.setEnabled(true);
	}

	private void enableDownOneFloor() {
		this.editorDownOneFloor.setEnabled(true);
	}

	private void enableDownOneLevel() {
		this.editorDownOneLevel.setEnabled(true);
	}

	private void enableInsertLevelFromClipboard() {
		this.editPasteAndInsert.setEnabled(true);
	}

	private void enablePasteLevel() {
		this.editPasteLevel.setEnabled(true);
	}

	private void enableRedo() {
		this.editRedo.setEnabled(true);
	}

	private void enableRemoveLevel() {
		this.editorRemoveLevel.setEnabled(true);
	}

	private void enableSetStartPoint() {
		this.editorSetStartPoint.setEnabled(true);
	}

	private void enableUndo() {
		this.editUndo.setEnabled(true);
	}

	private void enableUpOneFloor() {
		this.editorUpOneFloor.setEnabled(true);
	}

	private void enableUpOneLevel() {
		this.editorUpOneLevel.setEnabled(true);
	}

	void populateMenuBar() {
		final var mhandler = new MenuHandler();
		final var fileMenu = this.buildFileMenu(mhandler);
		final var editMenu = this.buildEditMenu(mhandler);
		final var playMenu = this.buildPlayMenu(mhandler);
		final var gameMenu = this.buildGameMenu(mhandler);
		final var editorMenu = this.buildEditorMenu(mhandler);
		final var helpMenu = this.buildHelpMenu(mhandler);
		this.attachAccelerators();
		this.menuBar.add(fileMenu);
		this.menuBar.add(editMenu);
		this.menuBar.add(playMenu);
		this.menuBar.add(gameMenu);
		this.menuBar.add(editorMenu);
		this.menuBar.add(helpMenu);
	}

	private JMenu buildFileMenu(final MenuHandler mhandler) {
		final var fileMenu = new JMenu(Strings.loadMenu(MenuString.MENU_FILE));
		this.fileNew = new JMenuItem(Strings.loadMenu(MenuString.ITEM_NEW));
		this.fileOpen = new JMenuItem(Strings.loadMenu(MenuString.ITEM_OPEN));
		this.fileOpenDefault = new JMenuItem(Strings.loadMenu(MenuString.ITEM_OPEN_DEFAULT));
		this.fileClose = new JMenuItem(Strings.loadMenu(MenuString.ITEM_CLOSE));
		this.fileSave = new JMenuItem(Strings.loadMenu(MenuString.ITEM_SAVE));
		this.fileSaveAs = new JMenuItem(Strings.loadMenu(MenuString.ITEM_SAVE_AS));
		this.fileSaveAsProtected = new JMenuItem(Strings.loadMenu(MenuString.ITEM_SAVE_AS_PROTECTED));
		this.filePrint = new JMenuItem(Strings.loadMenu(MenuString.ITEM_PRINT_GAMEBOARD));
		if (System.getProperty(GlobalStrings.loadUntranslated(UntranslatedString.OS_NAME))
				.contains(GlobalStrings.loadUntranslated(UntranslatedString.WINDOWS))) {
			this.fileExit = new JMenuItem(Strings.loadMenu(MenuString.ITEM_EXIT));
		} else {
			this.fileExit = new JMenuItem(Strings.loadMenu(MenuString.ITEM_QUIT));
		}
		this.fileNew.addActionListener(mhandler);
		this.fileOpen.addActionListener(mhandler);
		this.fileOpenDefault.addActionListener(mhandler);
		this.fileClose.addActionListener(mhandler);
		this.fileSave.addActionListener(mhandler);
		this.fileSaveAs.addActionListener(mhandler);
		this.fileSaveAsProtected.addActionListener(mhandler);
		this.filePrint.addActionListener(mhandler);
		this.fileExit.addActionListener(mhandler);
		fileMenu.add(this.fileNew);
		fileMenu.add(this.fileOpen);
		fileMenu.add(this.fileOpenDefault);
		fileMenu.add(this.fileClose);
		fileMenu.add(this.fileSave);
		fileMenu.add(this.fileSaveAs);
		fileMenu.add(this.fileSaveAsProtected);
		fileMenu.add(this.filePrint);
		fileMenu.add(this.fileExit);
		this.fileNew.setEnabled(true);
		this.fileOpen.setEnabled(true);
		this.fileOpenDefault.setEnabled(true);
		this.fileClose.setEnabled(false);
		this.fileSave.setEnabled(false);
		this.fileSaveAs.setEnabled(false);
		this.fileSaveAsProtected.setEnabled(false);
		this.filePrint.setEnabled(true);
		this.fileExit.setEnabled(true);
		return fileMenu;
	}

	private JMenu buildEditMenu(final MenuHandler mhandler) {
		final var editMenu = new JMenu(Strings.loadMenu(MenuString.MENU_EDIT));
		this.editUndo = new JMenuItem(Strings.loadMenu(MenuString.ITEM_UNDO));
		this.editRedo = new JMenuItem(Strings.loadMenu(MenuString.ITEM_REDO));
		this.editCutLevel = new JMenuItem(Strings.loadMenu(MenuString.ITEM_CUT_LEVEL));
		this.editCopyLevel = new JMenuItem(Strings.loadMenu(MenuString.ITEM_COPY_LEVEL));
		this.editPasteLevel = new JMenuItem(Strings.loadMenu(MenuString.ITEM_PASTE_LEVEL));
		this.editPasteAndInsert = new JMenuItem(Strings.loadMenu(MenuString.ITEM_INSERT_LEVEL_FROM_CLIPBOARD));
		this.editPreferences = new JMenuItem(Strings.loadMenu(MenuString.ITEM_SETTINGS));
		this.editUndo.addActionListener(mhandler);
		this.editRedo.addActionListener(mhandler);
		this.editCutLevel.addActionListener(mhandler);
		this.editCopyLevel.addActionListener(mhandler);
		this.editPasteLevel.addActionListener(mhandler);
		this.editPasteAndInsert.addActionListener(mhandler);
		this.editPreferences.addActionListener(mhandler);
		editMenu.add(this.editUndo);
		editMenu.add(this.editRedo);
		editMenu.add(this.editCutLevel);
		editMenu.add(this.editCopyLevel);
		editMenu.add(this.editPasteLevel);
		editMenu.add(this.editPasteAndInsert);
		editMenu.add(this.editPreferences);
		this.editUndo.setEnabled(false);
		this.editRedo.setEnabled(false);
		this.editCutLevel.setEnabled(false);
		this.editCopyLevel.setEnabled(false);
		this.editPasteLevel.setEnabled(false);
		this.editPasteAndInsert.setEnabled(false);
		this.editPreferences.setEnabled(true);
		return editMenu;
	}

	private JMenu buildPlayMenu(final MenuHandler mhandler) {
		final var playMenu = new JMenu(Strings.loadMenu(MenuString.MENU_PLAY));
		this.playPlay = new JMenuItem(Strings.loadMenu(MenuString.ITEM_PLAY));
		this.playEdit = new JMenuItem(Strings.loadMenu(MenuString.ITEM_EDIT));
		this.playToggleAccelerators = new JCheckBoxMenuItem(Strings.loadMenu(MenuString.ITEM_USE_CLASSIC_ACCELERATORS));
		this.playPlay.addActionListener(mhandler);
		this.playEdit.addActionListener(mhandler);
		this.playToggleAccelerators.addActionListener(mhandler);
		playMenu.add(this.playPlay);
		playMenu.add(this.playEdit);
		playMenu.add(this.playToggleAccelerators);
		this.playPlay.setEnabled(false);
		this.playEdit.setEnabled(false);
		this.playToggleAccelerators.setEnabled(true);
		return playMenu;
	}

	private JMenu buildGameMenu(final MenuHandler mhandler) {
		final var gameMenu = new JMenu(Strings.loadMenu(MenuString.MENU_GAME));
		this.gameReset = new JMenuItem(Strings.loadMenu(MenuString.ITEM_RESET_CURRENT_LEVEL));
		this.gameShowTable = new JMenuItem(Strings.loadMenu(MenuString.ITEM_SHOW_SCORE_TABLE));
		this.gameReplaySolution = new JMenuItem(Strings.loadMenu(MenuString.ITEM_REPLAY_SOLUTION));
		this.gameRecordSolution = new JCheckBoxMenuItem(Strings.loadMenu(MenuString.ITEM_RECORD_SOLUTION));
		this.gameLoadLPB = new JMenuItem(Strings.loadMenu(MenuString.ITEM_LOAD_PLAYBACK_FILE));
		this.gamePreviousLevel = new JMenuItem(Strings.loadMenu(MenuString.ITEM_PREVIOUS_LEVEL));
		this.gameSkipLevel = new JMenuItem(Strings.loadMenu(MenuString.ITEM_SKIP_LEVEL));
		this.gameLoadLevel = new JMenuItem(Strings.loadMenu(MenuString.ITEM_LOAD_LEVEL));
		this.gameShowHint = new JMenuItem(Strings.loadMenu(MenuString.ITEM_SHOW_HINT));
		this.gameCheats = new JMenuItem(Strings.loadMenu(MenuString.ITEM_CHEATS));
		this.gameChangeOtherAmmoMode = new JMenuItem(Strings.loadMenu(MenuString.ITEM_CHANGE_OTHER_AMMO));
		this.gameChangeOtherToolMode = new JMenuItem(Strings.loadMenu(MenuString.ITEM_CHANGE_OTHER_TOOL));
		this.gameChangeOtherRangeMode = new JMenuItem(Strings.loadMenu(MenuString.ITEM_CHANGE_OTHER_RANGE));
		this.gameEraDistantPast = new JCheckBoxMenuItem(Strings.loadEra(Era.DISTANT_PAST), false);
		this.gameEraPast = new JCheckBoxMenuItem(Strings.loadEra(Era.PAST), false);
		this.gameEraPresent = new JCheckBoxMenuItem(Strings.loadEra(Era.PRESENT), true);
		this.gameEraFuture = new JCheckBoxMenuItem(Strings.loadEra(Era.FUTURE), false);
		this.gameEraDistantFuture = new JCheckBoxMenuItem(Strings.loadEra(Era.DISTANT_FUTURE), false);
		this.gameReset.addActionListener(mhandler);
		this.gameShowTable.addActionListener(mhandler);
		this.gameReplaySolution.addActionListener(mhandler);
		this.gameRecordSolution.addActionListener(mhandler);
		this.gameLoadLPB.addActionListener(mhandler);
		this.gamePreviousLevel.addActionListener(mhandler);
		this.gameSkipLevel.addActionListener(mhandler);
		this.gameLoadLevel.addActionListener(mhandler);
		this.gameShowHint.addActionListener(mhandler);
		this.gameCheats.addActionListener(mhandler);
		this.gameChangeOtherAmmoMode.addActionListener(mhandler);
		this.gameChangeOtherToolMode.addActionListener(mhandler);
		this.gameChangeOtherRangeMode.addActionListener(mhandler);
		this.gameEraDistantPast.addActionListener(mhandler);
		this.gameEraPast.addActionListener(mhandler);
		this.gameEraPresent.addActionListener(mhandler);
		this.gameEraFuture.addActionListener(mhandler);
		this.gameEraDistantFuture.addActionListener(mhandler);
		this.gameTimeTravelSubMenu = new JMenu(Strings.loadMenu(MenuString.SUB_TIME_TRAVEL));
		this.gameTimeTravelSubMenu.add(this.gameEraDistantPast);
		this.gameTimeTravelSubMenu.add(this.gameEraPast);
		this.gameTimeTravelSubMenu.add(this.gameEraPresent);
		this.gameTimeTravelSubMenu.add(this.gameEraFuture);
		this.gameTimeTravelSubMenu.add(this.gameEraDistantFuture);
		gameMenu.add(this.gameReset);
		gameMenu.add(this.gameShowTable);
		gameMenu.add(this.gameReplaySolution);
		gameMenu.add(this.gameRecordSolution);
		gameMenu.add(this.gameLoadLPB);
		gameMenu.add(this.gamePreviousLevel);
		gameMenu.add(this.gameSkipLevel);
		gameMenu.add(this.gameLoadLevel);
		gameMenu.add(this.gameShowHint);
		gameMenu.add(this.gameCheats);
		gameMenu.add(this.gameChangeOtherAmmoMode);
		gameMenu.add(this.gameChangeOtherToolMode);
		gameMenu.add(this.gameChangeOtherRangeMode);
		gameMenu.add(this.gameTimeTravelSubMenu);
		this.gameReset.setEnabled(false);
		this.gameShowTable.setEnabled(false);
		this.gameReplaySolution.setEnabled(false);
		this.gameRecordSolution.setEnabled(false);
		this.gameLoadLPB.setEnabled(false);
		this.gamePreviousLevel.setEnabled(false);
		this.gameSkipLevel.setEnabled(false);
		this.gameLoadLevel.setEnabled(false);
		this.gameShowHint.setEnabled(false);
		this.gameCheats.setEnabled(false);
		this.gameChangeOtherAmmoMode.setEnabled(false);
		this.gameChangeOtherToolMode.setEnabled(false);
		this.gameChangeOtherRangeMode.setEnabled(false);
		this.gameEraDistantPast.setEnabled(false);
		this.gameEraPast.setEnabled(false);
		this.gameEraPresent.setEnabled(false);
		this.gameEraFuture.setEnabled(false);
		this.gameEraDistantFuture.setEnabled(false);
		return gameMenu;
	}

	private JMenu buildEditorMenu(final MenuHandler mhandler) {
		final var editorMenu = new JMenu(Strings.loadMenu(MenuString.MENU_EDITOR));
		this.editorClearHistory = new JMenuItem(Strings.loadMenu(MenuString.ITEM_CLEAR_HISTORY));
		this.editorGoToLevel = new JMenuItem(Strings.loadMenu(MenuString.ITEM_GO_TO_LEVEL));
		this.editorUpOneFloor = new JMenuItem(Strings.loadMenu(MenuString.ITEM_UP_ONE_FLOOR));
		this.editorDownOneFloor = new JMenuItem(Strings.loadMenu(MenuString.ITEM_DOWN_ONE_FLOOR));
		this.editorUpOneLevel = new JMenuItem(Strings.loadMenu(MenuString.ITEM_UP_ONE_LEVEL));
		this.editorDownOneLevel = new JMenuItem(Strings.loadMenu(MenuString.ITEM_DOWN_ONE_LEVEL));
		this.editorAddLevel = new JMenuItem(Strings.loadMenu(MenuString.ITEM_ADD_A_LEVEL));
		this.editorRemoveLevel = new JMenuItem(Strings.loadMenu(MenuString.ITEM_REMOVE_A_LEVEL));
		this.editorFillLevel = new JMenuItem(Strings.loadMenu(MenuString.ITEM_FILL_CURRENT_LEVEL));
		this.editorResizeLevel = new JMenuItem(Strings.loadMenu(MenuString.ITEM_RESIZE_CURRENT_LEVEL));
		this.editorLevelPreferences = new JMenuItem(Strings.loadMenu(MenuString.ITEM_LEVEL_SETTINGS));
		this.editorSetStartPoint = new JMenuItem(Strings.loadMenu(MenuString.ITEM_SET_START_POINT));
		this.editorChangeLayer = new JMenuItem(Strings.loadMenu(MenuString.ITEM_CHANGE_LAYER));
		this.editorGlobalMoveShoot = new JMenuItem(Strings.loadMenu(MenuString.ITEM_ENABLE_GLOBAL_MOVE_SHOOT));
		this.editorTimeTravelSubMenu = new JMenu(Strings.loadMenu(MenuString.SUB_TIME_TRAVEL));
		this.editorEraDistantPast = new JCheckBoxMenuItem(Strings.loadEra(Era.DISTANT_PAST), false);
		this.editorEraPast = new JCheckBoxMenuItem(Strings.loadEra(Era.PAST), false);
		this.editorEraPresent = new JCheckBoxMenuItem(Strings.loadEra(Era.PRESENT), true);
		this.editorEraFuture = new JCheckBoxMenuItem(Strings.loadEra(Era.FUTURE), false);
		this.editorEraDistantFuture = new JCheckBoxMenuItem(Strings.loadEra(Era.DISTANT_FUTURE), false);
		this.editorClearHistory.addActionListener(mhandler);
		this.editorGoToLevel.addActionListener(mhandler);
		this.editorUpOneFloor.addActionListener(mhandler);
		this.editorDownOneFloor.addActionListener(mhandler);
		this.editorUpOneLevel.addActionListener(mhandler);
		this.editorDownOneLevel.addActionListener(mhandler);
		this.editorAddLevel.addActionListener(mhandler);
		this.editorRemoveLevel.addActionListener(mhandler);
		this.editorFillLevel.addActionListener(mhandler);
		this.editorResizeLevel.addActionListener(mhandler);
		this.editorLevelPreferences.addActionListener(mhandler);
		this.editorSetStartPoint.addActionListener(mhandler);
		this.editorChangeLayer.addActionListener(mhandler);
		this.editorGlobalMoveShoot.addActionListener(mhandler);
		this.editorEraDistantPast.addActionListener(mhandler);
		this.editorEraPast.addActionListener(mhandler);
		this.editorEraPresent.addActionListener(mhandler);
		this.editorEraFuture.addActionListener(mhandler);
		this.editorEraDistantFuture.addActionListener(mhandler);
		this.editorTimeTravelSubMenu.add(this.editorEraDistantPast);
		this.editorTimeTravelSubMenu.add(this.editorEraPast);
		this.editorTimeTravelSubMenu.add(this.editorEraPresent);
		this.editorTimeTravelSubMenu.add(this.editorEraFuture);
		this.editorTimeTravelSubMenu.add(this.editorEraDistantFuture);
		editorMenu.add(this.editorClearHistory);
		editorMenu.add(this.editorGoToLevel);
		editorMenu.add(this.editorUpOneFloor);
		editorMenu.add(this.editorDownOneFloor);
		editorMenu.add(this.editorUpOneLevel);
		editorMenu.add(this.editorDownOneLevel);
		editorMenu.add(this.editorAddLevel);
		editorMenu.add(this.editorRemoveLevel);
		editorMenu.add(this.editorFillLevel);
		editorMenu.add(this.editorResizeLevel);
		editorMenu.add(this.editorLevelPreferences);
		editorMenu.add(this.editorSetStartPoint);
		editorMenu.add(this.editorChangeLayer);
		editorMenu.add(this.editorGlobalMoveShoot);
		editorMenu.add(this.editorTimeTravelSubMenu);
		this.editorClearHistory.setEnabled(false);
		this.editorGoToLevel.setEnabled(false);
		this.editorUpOneFloor.setEnabled(false);
		this.editorDownOneFloor.setEnabled(false);
		this.editorUpOneLevel.setEnabled(false);
		this.editorDownOneLevel.setEnabled(false);
		this.editorAddLevel.setEnabled(false);
		this.editorRemoveLevel.setEnabled(false);
		this.editorFillLevel.setEnabled(false);
		this.editorResizeLevel.setEnabled(false);
		this.editorLevelPreferences.setEnabled(false);
		this.editorSetStartPoint.setEnabled(false);
		this.editorChangeLayer.setEnabled(false);
		this.editorGlobalMoveShoot.setEnabled(false);
		this.editorEraDistantPast.setEnabled(false);
		this.editorEraPast.setEnabled(false);
		this.editorEraPresent.setEnabled(false);
		this.editorEraFuture.setEnabled(false);
		this.editorEraDistantFuture.setEnabled(false);
		return editorMenu;
	}

	private JMenu buildHelpMenu(final MenuHandler mhandler) {
		final var helpMenu = new JMenu(Strings.loadMenu(MenuString.MENU_HELP));
		this.helpAbout = new JMenuItem(Strings.loadMenu(MenuString.ITEM_ABOUT_LASERTANK));
		this.helpAbout.addActionListener(mhandler);
		helpMenu.add(this.helpAbout);
		this.helpAbout.setEnabled(true);
		return helpMenu;
	}

	private void attachAccelerators() {
		this.fileNew.setAccelerator(this.accel.fileNewAccel);
		this.fileOpen.setAccelerator(this.accel.fileOpenAccel);
		this.fileClose.setAccelerator(this.accel.fileCloseAccel);
		this.fileSave.setAccelerator(this.accel.fileSaveAccel);
		this.fileSaveAs.setAccelerator(this.accel.fileSaveAsAccel);
		this.filePrint.setAccelerator(this.accel.filePrintAccel);
		if (System.getProperty(GlobalStrings.loadUntranslated(UntranslatedString.OS_NAME))
				.contains(GlobalStrings.loadUntranslated(UntranslatedString.WINDOWS))) {
			this.fileExit.setAccelerator(null);
		} else {
			this.fileExit.setAccelerator(this.accel.fileExitAccel);
		}
		this.playPlay.setAccelerator(this.accel.playPlayArenaAccel);
		this.playEdit.setAccelerator(this.accel.playEditArenaAccel);
		this.editUndo.setAccelerator(this.accel.editUndoAccel);
		this.editRedo.setAccelerator(this.accel.editRedoAccel);
		this.editCutLevel.setAccelerator(this.accel.editCutLevelAccel);
		this.editCopyLevel.setAccelerator(this.accel.editCopyLevelAccel);
		this.editPasteLevel.setAccelerator(this.accel.editPasteLevelAccel);
		this.editPasteAndInsert.setAccelerator(this.accel.editPasteAndInsertAccel);
		this.editPreferences.setAccelerator(this.accel.editPreferencesAccel);
		this.gameReset.setAccelerator(this.accel.gameResetAccel);
		this.gameShowTable.setAccelerator(this.accel.gameShowTableAccel);
		this.editorClearHistory.setAccelerator(this.accel.editorClearHistoryAccel);
		this.editorGoToLevel.setAccelerator(this.accel.editorGoToLocationAccel);
		this.editorUpOneLevel.setAccelerator(this.accel.editorUpOneLevelAccel);
		this.editorDownOneLevel.setAccelerator(this.accel.editorDownOneLevelAccel);
	}

	private void disableDirtyCommands() {
		this.fileSave.setEnabled(false);
	}

	private void enableDirtyCommands() {
		this.fileSave.setEnabled(true);
	}

	private void disableLoadedCommands() {
		this.fileClose.setEnabled(false);
		this.fileSaveAs.setEnabled(false);
		this.fileSaveAsProtected.setEnabled(false);
		this.playPlay.setEnabled(false);
		this.playEdit.setEnabled(false);
	}

	private void enableLoadedCommands() {
		final var app = LaserTankEE.getApplication();
		if (app.isInGUIMode()) {
			this.fileClose.setEnabled(false);
			this.fileSaveAs.setEnabled(false);
			this.fileSaveAsProtected.setEnabled(false);
		} else {
			this.fileClose.setEnabled(true);
			this.fileSaveAs.setEnabled(true);
			this.fileSaveAsProtected.setEnabled(true);
		}
		if (app.getArenaManager().getArena().doesPlayerExist(0)) {
			this.playPlay.setEnabled(true);
		} else {
			this.playPlay.setEnabled(false);
		}
		this.playEdit.setEnabled(true);
	}

	void activateEditorCommands() {
		this.fileNew.setEnabled(false);
		this.fileOpen.setEnabled(false);
		this.fileOpenDefault.setEnabled(false);
		this.gameReset.setEnabled(false);
		this.gameShowTable.setEnabled(false);
		this.gameReplaySolution.setEnabled(false);
		this.gameRecordSolution.setEnabled(false);
		this.gameLoadLPB.setEnabled(false);
		this.gamePreviousLevel.setEnabled(false);
		this.gameSkipLevel.setEnabled(false);
		this.gameLoadLevel.setEnabled(false);
		this.gameShowHint.setEnabled(false);
		this.gameCheats.setEnabled(false);
		this.gameChangeOtherAmmoMode.setEnabled(false);
		this.gameChangeOtherToolMode.setEnabled(false);
		this.gameChangeOtherRangeMode.setEnabled(false);
		this.gameEraDistantPast.setEnabled(false);
		this.gameEraPast.setEnabled(false);
		this.gameEraPresent.setEnabled(false);
		this.gameEraFuture.setEnabled(false);
		this.gameEraDistantFuture.setEnabled(false);
		this.editUndo.setEnabled(false);
		this.editRedo.setEnabled(false);
		this.editCutLevel.setEnabled(true);
		this.editCopyLevel.setEnabled(true);
		this.editPasteLevel.setEnabled(true);
		this.editPasteAndInsert.setEnabled(true);
		this.editorGoToLevel.setEnabled(true);
		this.editorFillLevel.setEnabled(true);
		this.editorResizeLevel.setEnabled(true);
		this.editorLevelPreferences.setEnabled(true);
		this.editorSetStartPoint.setEnabled(true);
		this.editorChangeLayer.setEnabled(true);
		this.editorGlobalMoveShoot.setEnabled(true);
		this.editorEraDistantPast.setEnabled(true);
		this.editorEraPast.setEnabled(true);
		this.editorEraPresent.setEnabled(true);
		this.editorEraFuture.setEnabled(true);
		this.editorEraDistantFuture.setEnabled(true);
	}

	void activateGameCommands() {
		this.fileNew.setEnabled(false);
		this.fileOpen.setEnabled(false);
		this.fileOpenDefault.setEnabled(false);
		this.gameReset.setEnabled(true);
		this.gameShowTable.setEnabled(true);
		this.gameReplaySolution.setEnabled(true);
		this.gameRecordSolution.setEnabled(true);
		this.gameLoadLPB.setEnabled(true);
		this.gamePreviousLevel.setEnabled(true);
		this.gameSkipLevel.setEnabled(true);
		this.gameLoadLevel.setEnabled(true);
		this.gameShowHint.setEnabled(true);
		this.gameCheats.setEnabled(true);
		this.gameChangeOtherAmmoMode.setEnabled(true);
		this.gameChangeOtherToolMode.setEnabled(true);
		this.gameChangeOtherRangeMode.setEnabled(true);
		this.gameEraDistantPast.setEnabled(true);
		this.gameEraPast.setEnabled(true);
		this.gameEraPresent.setEnabled(true);
		this.gameEraFuture.setEnabled(true);
		this.gameEraDistantFuture.setEnabled(true);
		this.editUndo.setEnabled(false);
		this.editRedo.setEnabled(false);
		this.editCutLevel.setEnabled(false);
		this.editCopyLevel.setEnabled(false);
		this.editPasteLevel.setEnabled(false);
		this.editPasteAndInsert.setEnabled(false);
		this.editorClearHistory.setEnabled(false);
		this.editorGoToLevel.setEnabled(false);
		this.editorUpOneFloor.setEnabled(false);
		this.editorDownOneFloor.setEnabled(false);
		this.editorUpOneLevel.setEnabled(false);
		this.editorDownOneLevel.setEnabled(false);
		this.editorAddLevel.setEnabled(false);
		this.editorRemoveLevel.setEnabled(false);
		this.editorFillLevel.setEnabled(false);
		this.editorResizeLevel.setEnabled(false);
		this.editorLevelPreferences.setEnabled(false);
		this.editorSetStartPoint.setEnabled(false);
		this.editorChangeLayer.setEnabled(false);
		this.editorGlobalMoveShoot.setEnabled(false);
		this.editorEraDistantPast.setEnabled(false);
		this.editorEraPast.setEnabled(false);
		this.editorEraPresent.setEnabled(false);
		this.editorEraFuture.setEnabled(false);
		this.editorEraDistantFuture.setEnabled(false);
	}

	void activateGUICommands() {
		this.fileNew.setEnabled(true);
		this.fileOpen.setEnabled(true);
		this.fileOpenDefault.setEnabled(true);
		this.gameReset.setEnabled(false);
		this.gameShowTable.setEnabled(false);
		this.gameReplaySolution.setEnabled(false);
		this.gameRecordSolution.setEnabled(false);
		this.gameLoadLPB.setEnabled(false);
		this.gamePreviousLevel.setEnabled(false);
		this.gameSkipLevel.setEnabled(false);
		this.gameLoadLevel.setEnabled(false);
		this.gameShowHint.setEnabled(false);
		this.gameCheats.setEnabled(false);
		this.gameChangeOtherAmmoMode.setEnabled(false);
		this.gameChangeOtherToolMode.setEnabled(false);
		this.gameChangeOtherRangeMode.setEnabled(false);
		this.gameEraDistantPast.setEnabled(false);
		this.gameEraPast.setEnabled(false);
		this.gameEraPresent.setEnabled(false);
		this.gameEraFuture.setEnabled(false);
		this.gameEraDistantFuture.setEnabled(false);
		this.editUndo.setEnabled(false);
		this.editRedo.setEnabled(false);
		this.editCutLevel.setEnabled(false);
		this.editCopyLevel.setEnabled(false);
		this.editPasteLevel.setEnabled(false);
		this.editPasteAndInsert.setEnabled(false);
		this.editorClearHistory.setEnabled(false);
		this.editorGoToLevel.setEnabled(false);
		this.editorUpOneFloor.setEnabled(false);
		this.editorDownOneFloor.setEnabled(false);
		this.editorUpOneLevel.setEnabled(false);
		this.editorDownOneLevel.setEnabled(false);
		this.editorAddLevel.setEnabled(false);
		this.editorRemoveLevel.setEnabled(false);
		this.editorFillLevel.setEnabled(false);
		this.editorResizeLevel.setEnabled(false);
		this.editorLevelPreferences.setEnabled(false);
		this.editorSetStartPoint.setEnabled(false);
		this.editorChangeLayer.setEnabled(false);
		this.editorGlobalMoveShoot.setEnabled(false);
		this.editorEraDistantPast.setEnabled(false);
		this.editorEraPast.setEnabled(false);
		this.editorEraPresent.setEnabled(false);
		this.editorEraFuture.setEnabled(false);
		this.editorEraDistantFuture.setEnabled(false);
	}

	void toggleAccelerators() {
		if (Settings.useClassicAccelerators()) {
			Settings.setClassicAccelerators(false);
		} else {
			Settings.setClassicAccelerators(true);
		}
		this.accel = Accelerators.getInstance(Settings.useClassicAccelerators());
	}
}
