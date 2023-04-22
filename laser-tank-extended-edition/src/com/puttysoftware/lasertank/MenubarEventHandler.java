package com.puttysoftware.lasertank;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.puttysoftware.diane.gui.GUIPrinter;
import com.puttysoftware.diane.gui.dialog.CommonDialogs;
import com.puttysoftware.lasertank.arena.ArenaManager;
import com.puttysoftware.lasertank.datatype.LaserTankPlayback;
import com.puttysoftware.lasertank.editor.Editor;
import com.puttysoftware.lasertank.game.Game;
import com.puttysoftware.lasertank.locale.DialogString;
import com.puttysoftware.lasertank.locale.EditorString;
import com.puttysoftware.lasertank.locale.ErrorString;
import com.puttysoftware.lasertank.locale.MenuString;
import com.puttysoftware.lasertank.locale.Strings;
import com.puttysoftware.lasertank.locale.global.GlobalStrings;
import com.puttysoftware.lasertank.locale.global.UntranslatedString;
import com.puttysoftware.lasertank.settings.Settings;

class MenubarEventHandler implements ActionListener {
	private final MenubarHost menubarHost;

	public MenubarEventHandler(MenubarHost menuHost) {
		this.menubarHost = menuHost;
	}

	// Handle menus
	@Override
	public void actionPerformed(final ActionEvent e) {
		try {
			final var game = Game.get();
			final var editor = Editor.get();
			final var menu = this.menubarHost;
			var loaded = false;
			final var cmd = e.getActionCommand();
			if (cmd.equals(Strings.loadMenu(MenuString.ITEM_NEW))) {
				loaded = Editor.get().newArena();
				ArenaManager.get().setLoaded(loaded);
			} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_OPEN))) {
				loaded = ArenaManager.get().loadArena();
				ArenaManager.get().setLoaded(loaded);
			} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_OPEN_DEFAULT))) {
				loaded = ArenaManager.get().loadArenaDefault();
				ArenaManager.get().setLoaded(loaded);
			} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_CLOSE))) {
				// Close the window
				if (LaserTankEE.onEditorScreen()) {
					Editor.get().handleCloseWindow();
				} else if (LaserTankEE.onGameScreen()) {
					var saved = true;
					var status = 0;
					if (ArenaManager.get().getDirty()) {
						status = ArenaManager.showSaveDialog();
						if (status == CommonDialogs.YES_OPTION) {
							saved = ArenaManager.get()
									.saveArena(ArenaManager.get().isArenaProtected());
						} else if (status == CommonDialogs.CANCEL_OPTION) {
							saved = false;
						} else {
							ArenaManager.get().setDirty(false);
						}
					}
					if (saved) {
						Game.get().exitGame();
					}
				}
				LaserTankEE.getMainScreen().showGUI();
			} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_SAVE))) {
				if (ArenaManager.get().getLoaded()) {
					ArenaManager.get().saveArena(ArenaManager.get().isArenaProtected());
				} else {
					CommonDialogs.showDialog(Strings.loadError(ErrorString.NO_ARENA_OPENED));
				}
			} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_SAVE_AS))) {
				if (ArenaManager.get().getLoaded()) {
					ArenaManager.get().saveArenaAs(false);
				} else {
					CommonDialogs.showDialog(Strings.loadError(ErrorString.NO_ARENA_OPENED));
				}
			} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_SAVE_AS_PROTECTED))) {
				if (ArenaManager.get().getLoaded()) {
					ArenaManager.get().saveArenaAs(true);
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
				if (LaserTankEE.getMainScreen().quitHandler()) {
					System.exit(0);
				}
			} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_PLAY))) {
				// Play the current arena
				final var proceed = Game.get().newGame();
				if (proceed) {
					LaserTankEE.leaveScreen();
					Game.get().playArena();
				}
			} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_EDIT))) {
				// Edit the current arena
				LaserTankEE.leaveScreen();
				Editor.get().editArena();
			} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_USE_CLASSIC_ACCELERATORS))) {
				// Toggle accelerators
				LaserTankEE.getMenus().toggleAccelerators();
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
				CommonDialogs.showDialog(ArenaManager.get().getArena().getHint().trim());
			} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_CHEATS))) {
				game.enterCheatCode();
			} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_CHANGE_OTHER_AMMO))) {
				game.changeOtherAmmoMode();
			} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_CHANGE_OTHER_TOOL))) {
				game.changeOtherToolMode();
			} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_CHANGE_OTHER_RANGE))) {
				game.changeOtherRangeMode();
			} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_UNDO))) {
				// Undo most recent action
				if (LaserTankEE.onEditorScreen()) {
					editor.undo();
				} else if (LaserTankEE.onGameScreen()) {
					Game.get().abortAndWaitForMLOLoop();
					Game.get().undoLastMove();
				}
			} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_REDO))) {
				// Redo most recent undone action
				if (LaserTankEE.onEditorScreen()) {
					editor.redo();
				} else if (LaserTankEE.onGameScreen()) {
					Game.get().abortAndWaitForMLOLoop();
					Game.get().redoLastMove();
				}
			} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_CUT_LEVEL))) {
				// Cut Level
				final var level = editor.getEditorLocationU();
				ArenaManager.get().getArena().cutLevel();
				editor.fixLimits();
				editor.updateEditorLevelAbsolute(level);
			} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_COPY_LEVEL))) {
				// Copy Level
				ArenaManager.get().getArena().copyLevel();
			} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_PASTE_LEVEL))) {
				// Paste Level
				ArenaManager.get().getArena().pasteLevel();
				editor.fixLimits();
				editor.redrawEditor();
			} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_INSERT_LEVEL_FROM_CLIPBOARD))) {
				// Insert Level From Clipboard
				ArenaManager.get().getArena().insertLevelFromClipboard();
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
				ArenaManager.get().getArena().setMoveShootAllowedGlobally(true);
				menu.editorGlobalMoveShoot.setText(Strings.loadMenu(MenuString.ITEM_DISABLE_GLOBAL_MOVE_SHOOT));
			} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_DISABLE_GLOBAL_MOVE_SHOOT))) {
				// Disable Global Move-Shoot
				menu.editorGlobalMoveShoot.setText(Strings.loadMenu(MenuString.ITEM_ENABLE_GLOBAL_MOVE_SHOOT));
				ArenaManager.get().getArena().setMoveShootAllowedGlobally(false);
			} else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_ABOUT_LASERTANK))) {
				LaserTankEE.getAboutDialog().showScreen();
			}
			LaserTankEE.getMenus().updateMenuItemState();
		} catch (final Exception ex) {
			LaserTankEE.logError(ex);
		}
	}
}