package com.puttysoftware.lasertank;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.puttysoftware.lasertank.arena.ArenaManager;
import com.puttysoftware.lasertank.datatype.LaserTankPlayback;
import com.puttysoftware.lasertank.editor.Editor;
import com.puttysoftware.lasertank.game.Game;
import com.puttysoftware.lasertank.gui.GUIPrinter;
import com.puttysoftware.lasertank.gui.dialog.CommonDialogs;
import com.puttysoftware.lasertank.locale.DialogString;
import com.puttysoftware.lasertank.locale.EditorString;
import com.puttysoftware.lasertank.locale.ErrorString;
import com.puttysoftware.lasertank.locale.MenuString;
import com.puttysoftware.lasertank.locale.Strings;
import com.puttysoftware.lasertank.locale.global.GlobalStrings;
import com.puttysoftware.lasertank.locale.global.UntranslatedString;
import com.puttysoftware.lasertank.settings.Settings;
import com.puttysoftware.lasertank.utility.TaskRunner;

class MenubarEventHandler implements ActionListener, Runnable {
    private final MenubarHost menubarHost;
    private ActionEvent event;

    public MenubarEventHandler(final MenubarHost menuHost) {
	this.menubarHost = menuHost;
    }

    // Handle menus
    @Override
    public void actionPerformed(final ActionEvent e) {
	this.event = e;
	TaskRunner.runTask(this);
    }

    @Override
    public void run() {
	try {
	    final var editor = Editor.get();
	    final var menu = MenubarEventHandler.this.menubarHost;
	    var loaded = false;
	    final var cmd = this.event.getActionCommand();
	    if (cmd.equals(Strings.loadMenu(MenuString.ITEM_NEW))) {
		loaded = Editor.get().newArena();
		ArenaManager.setLoaded(loaded);
	    } else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_OPEN))) {
		ArenaManager.loadArena();
	    } else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_OPEN_DEFAULT))) {
		ArenaManager.loadArenaDefault();
	    } else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_CLOSE))) {
		// Close the window
		if (LaserTankEE.onEditorScreen()) {
		    Editor.get();
		    Editor.handleCloseWindow();
		} else if (LaserTankEE.onGameScreen()) {
		    var saved = true;
		    var status = 0;
		    if (ArenaManager.getDirty()) {
			status = ArenaManager.showSaveDialog();
			if (status == CommonDialogs.YES_OPTION) {
			    saved = ArenaManager.saveArena(ArenaManager.isArenaProtected());
			} else if (status == CommonDialogs.CANCEL_OPTION) {
			    saved = false;
			} else {
			    ArenaManager.setDirty(false);
			}
		    }
		    if (saved) {
			Game.exitGame();
		    }
		}
		LaserTankEE.showGUI();
	    } else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_SAVE))) {
		if (ArenaManager.getLoaded()) {
		    ArenaManager.saveArena(ArenaManager.isArenaProtected());
		} else {
		    CommonDialogs.showDialog(Strings.loadError(ErrorString.NO_ARENA_OPENED));
		}
	    } else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_SAVE_AS))) {
		if (ArenaManager.getLoaded()) {
		    ArenaManager.saveArenaAs(false);
		} else {
		    CommonDialogs.showDialog(Strings.loadError(ErrorString.NO_ARENA_OPENED));
		}
	    } else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_SAVE_AS_PROTECTED))) {
		if (ArenaManager.getLoaded()) {
		    ArenaManager.saveArenaAs(true);
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
		if (LaserTankEE.quitHandler()) {
		    System.exit(0);
		}
	    } else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_PLAY))) {
		// Play the current arena
		final var proceed = Game.newGame();
		if (proceed) {
		    LaserTankEE.leaveScreen();
		    Game.playArena();
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
		    Game.abortAndWaitForMLOLoop();
		    Game.resetCurrentLevel();
		}
	    } else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_SHOW_SCORE_TABLE))) {
		Game.showScoreTable();
	    } else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_REPLAY_SOLUTION))) {
		Game.abortAndWaitForMLOLoop();
		Game.replaySolution();
	    } else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_RECORD_SOLUTION))) {
		Game.toggleRecording();
	    } else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_LOAD_PLAYBACK_FILE))) {
		Game.abortAndWaitForMLOLoop();
		LaserTankPlayback.loadLPB();
	    } else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_PREVIOUS_LEVEL))) {
		Game.abortAndWaitForMLOLoop();
		Game.previousLevel();
	    } else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_SKIP_LEVEL))) {
		Game.abortAndWaitForMLOLoop();
		Game.solvedLevel(false);
	    } else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_LOAD_LEVEL))) {
		Game.abortAndWaitForMLOLoop();
		Game.loadLevel();
	    } else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_SHOW_HINT))) {
		CommonDialogs.showDialog(ArenaManager.getArena().getHint().trim());
	    } else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_CHEATS))) {
		Game.enterCheatCode();
	    } else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_CHANGE_OTHER_AMMO))) {
		Game.changeOtherAmmoMode();
	    } else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_CHANGE_OTHER_TOOL))) {
		Game.changeOtherToolMode();
	    } else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_CHANGE_OTHER_RANGE))) {
		Game.changeOtherRangeMode();
	    } else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_UNDO))) {
		// Undo most recent action
		if (LaserTankEE.onEditorScreen()) {
		    editor.undo();
		} else if (LaserTankEE.onGameScreen()) {
		    Game.abortAndWaitForMLOLoop();
		    Game.undoLastMove();
		}
	    } else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_REDO))) {
		// Redo most recent undone action
		if (LaserTankEE.onEditorScreen()) {
		    editor.redo();
		} else if (LaserTankEE.onGameScreen()) {
		    Game.abortAndWaitForMLOLoop();
		    Game.redoLastMove();
		}
	    } else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_CUT_LEVEL))) {
		// Cut Level
		final var level = editor.getEditorLocationU();
		ArenaManager.getArena().cutLevel();
		editor.fixLimits();
		editor.updateEditorLevelAbsolute(level);
	    } else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_COPY_LEVEL))) {
		// Copy Level
		ArenaManager.getArena().copyLevel();
	    } else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_PASTE_LEVEL))) {
		// Paste Level
		ArenaManager.getArena().pasteLevel();
		editor.fixLimits();
		editor.redrawEditor();
	    } else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_INSERT_LEVEL_FROM_CLIPBOARD))) {
		// Insert Level From Clipboard
		ArenaManager.getArena().insertLevelFromClipboard();
		editor.fixLimits();
	    } else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_CLEAR_HISTORY))) {
		// Clear undo/redo history, confirm first
		final var res = CommonDialogs.showConfirmDialog(Strings.loadDialog(DialogString.CONFIRM_CLEAR_HISTORY),
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
		ArenaManager.getArena().setMoveShootAllowedGlobally(true);
		menu.editorGlobalMoveShoot.setText(Strings.loadMenu(MenuString.ITEM_DISABLE_GLOBAL_MOVE_SHOOT));
	    } else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_DISABLE_GLOBAL_MOVE_SHOOT))) {
		// Disable Global Move-Shoot
		menu.editorGlobalMoveShoot.setText(Strings.loadMenu(MenuString.ITEM_ENABLE_GLOBAL_MOVE_SHOOT));
		ArenaManager.getArena().setMoveShootAllowedGlobally(false);
	    } else if (cmd.equals(Strings.loadMenu(MenuString.ITEM_ABOUT_LASERTANK))) {
		LaserTankEE.getAboutDialog().showScreen();
	    }
	    LaserTankEE.updateMenuItemState();
	} catch (final Exception ex) {
	    LaserTankEE.logError(ex);
	}
    }
}