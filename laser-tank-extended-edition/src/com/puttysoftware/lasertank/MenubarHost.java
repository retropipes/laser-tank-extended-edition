/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.puttysoftware.lasertank.accelerator.Accelerators;
import com.puttysoftware.lasertank.arena.Arena;
import com.puttysoftware.lasertank.arena.ArenaManager;
import com.puttysoftware.lasertank.editor.Editor;
import com.puttysoftware.lasertank.locale.MenuString;
import com.puttysoftware.lasertank.locale.Strings;
import com.puttysoftware.lasertank.locale.global.GlobalStrings;
import com.puttysoftware.lasertank.locale.global.UntranslatedString;
import com.puttysoftware.lasertank.settings.Settings;

class MenubarHost {
    // Fields
    private final JMenuBar menuBar;
    private JMenuItem fileNew, fileOpen, fileOpenDefault, fileClose, fileSave, fileSaveAs, fileSaveAsProtected,
	    filePrint, fileExit;
    private JMenuItem editUndo, editRedo, editCutLevel, editCopyLevel, editPasteLevel, editPasteAndInsert,
	    editPreferences;
    private JMenuItem playPlay, playEdit;
    private JCheckBoxMenuItem playToggleAccelerators;
    private JMenuItem gameReset, gameShowTable, gameReplaySolution, gameLoadLPB, gamePreviousLevel, gameSkipLevel,
	    gameLoadLevel, gameShowHint, gameCheats, gameChangeOtherAmmoMode, gameChangeOtherToolMode,
	    gameChangeOtherRangeMode;
    private JCheckBoxMenuItem gameRecordSolution;
    private JMenuItem editorClearHistory, editorGoToLevel, editorUpOneFloor, editorDownOneFloor, editorUpOneLevel,
	    editorDownOneLevel, editorAddLevel, editorRemoveLevel, editorLevelPreferences, editorSetStartPoint,
	    editorFillLevel, editorResizeLevel, editorChangeLayer;
    JMenuItem editorGlobalMoveShoot;
    private JMenuItem helpAbout;
    private Accelerators accel;

    // Constructors
    public MenubarHost() {
	this.menuBar = new JMenuBar();
	this.accel = Accelerators.getInstance(Settings.useClassicAccelerators());
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

    private JMenu buildEditMenu(final MenubarEventHandler mhandler) {
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

    private JMenu buildEditorMenu(final MenubarEventHandler mhandler) {
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
	return editorMenu;
    }

    private JMenu buildFileMenu(final MenubarEventHandler mhandler) {
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

    private JMenu buildGameMenu(final MenubarEventHandler mhandler) {
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
	return gameMenu;
    }

    private JMenu buildHelpMenu(final MenubarEventHandler mhandler) {
	final var helpMenu = new JMenu(Strings.loadMenu(MenuString.MENU_HELP));
	this.helpAbout = new JMenuItem(Strings.loadMenu(MenuString.ITEM_ABOUT_LASERTANK));
	this.helpAbout.addActionListener(mhandler);
	helpMenu.add(this.helpAbout);
	this.helpAbout.setEnabled(true);
	return helpMenu;
    }

    private JMenu buildPlayMenu(final MenubarEventHandler mhandler) {
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

    private void disableAddLevel() {
	this.editorAddLevel.setEnabled(false);
    }

    private void disableClearHistory() {
	this.editorClearHistory.setEnabled(false);
    }

    private void disableCutLevel() {
	this.editCutLevel.setEnabled(false);
    }

    private void disableDirtyCommands() {
	this.fileSave.setEnabled(false);
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

    private void disableLoadedCommands() {
	this.fileClose.setEnabled(false);
	this.fileSaveAs.setEnabled(false);
	this.fileSaveAsProtected.setEnabled(false);
	this.playPlay.setEnabled(false);
	this.playEdit.setEnabled(false);
    }

    private void disablePasteLevel() {
	this.editPasteLevel.setEnabled(false);
    }

    final void disableRecording() {
	this.gameRecordSolution.setSelected(false);
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

    private void enableDirtyCommands() {
	this.fileSave.setEnabled(true);
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

    private void enableLoadedCommands() {
	if (LaserTankEE.onMainScreen()) {
	    this.fileClose.setEnabled(false);
	    this.fileSaveAs.setEnabled(false);
	    this.fileSaveAsProtected.setEnabled(false);
	} else {
	    this.fileClose.setEnabled(true);
	    this.fileSaveAs.setEnabled(true);
	    this.fileSaveAsProtected.setEnabled(true);
	}
	if (ArenaManager.getArena().doesPlayerExist(0)) {
	    this.playPlay.setEnabled(true);
	} else {
	    this.playPlay.setEnabled(false);
	}
	this.playEdit.setEnabled(true);
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

    final JMenuBar getMenuBar() {
	return this.menuBar;
    }

    void populateMenuBar() {
	final var mhandler = new MenubarEventHandler(this);
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

    void toggleAccelerators() {
	if (Settings.useClassicAccelerators()) {
	    Settings.setClassicAccelerators(false);
	} else {
	    Settings.setClassicAccelerators(true);
	}
	this.accel = Accelerators.getInstance(Settings.useClassicAccelerators());
    }

    // Methods
    final void updateMenuItemState() {
	try {
	    final var editor = Editor.get();
	    if (ArenaManager.getLoaded()) {
		this.enableLoadedCommands();
	    } else {
		this.disableLoadedCommands();
	    }
	    if (ArenaManager.getDirty()) {
		this.enableDirtyCommands();
	    } else {
		this.disableDirtyCommands();
	    }
	    if (LaserTankEE.onEditorScreen()) {
		final var m = ArenaManager.getArena();
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
	    if (LaserTankEE.onGameScreen()) {
		final var a = ArenaManager.getArena();
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
	    final var a = ArenaManager.getArena();
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
}
