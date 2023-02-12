/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.editor;

import java.awt.Adjustable;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JToggleButton;

import com.puttysoftware.diane.asset.image.BufferedImageIcon;
import com.puttysoftware.diane.gui.Screen;
import com.puttysoftware.diane.gui.dialog.CommonDialogs;
import com.puttysoftware.diane.gui.picker.AnonymousPicturePicker;
import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.arena.Arena;
import com.puttysoftware.lasertank.arena.ArenaManager;
import com.puttysoftware.lasertank.arena.abc.AbstractArenaObject;
import com.puttysoftware.lasertank.arena.abc.AbstractJumpObject;
import com.puttysoftware.lasertank.arena.objects.Ground;
import com.puttysoftware.lasertank.arena.objects.Tank;
import com.puttysoftware.lasertank.asset.Images;
import com.puttysoftware.lasertank.helper.EditorLayoutHelper;
import com.puttysoftware.lasertank.helper.LayerHelper;
import com.puttysoftware.lasertank.index.Layer;
import com.puttysoftware.lasertank.locale.CommonString;
import com.puttysoftware.lasertank.locale.EditorString;
import com.puttysoftware.lasertank.locale.ErrorString;
import com.puttysoftware.lasertank.locale.Strings;
import com.puttysoftware.lasertank.settings.Settings;
import com.puttysoftware.lasertank.utility.ArenaObjectList;
import com.puttysoftware.lasertank.utility.InvalidArenaException;
import com.puttysoftware.lasertank.utility.RCLGenerator;

public class Editor extends Screen {
    private class EventHandler implements MouseListener, MouseMotionListener, WindowListener {
	// handle scroll bars
	public EventHandler() {
	    // Do nothing
	}

	@Override
	public void mouseClicked(final MouseEvent e) {
	    try {
		final var me = Editor.this;
		final var x = e.getX();
		final var y = e.getY();
		if (e.isAltDown() || e.isAltGraphDown() || e.isControlDown()) {
		    me.editObjectProperties(x, y);
		} else {
		    me.editObject(x, y);
		}
	    } catch (final Exception ex) {
		LaserTankEE.logError(ex);
	    }
	}

	@Override
	public void mouseDragged(final MouseEvent e) {
	    try {
		final var me = Editor.this;
		final var x = e.getX();
		final var y = e.getY();
		me.editObject(x, y);
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

	@Override
	public void mouseMoved(final MouseEvent e) {
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
	    Editor.this.handleCloseWindow();
	    LaserTankEE.getApplication().getGUIManager().showGUI();
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
	    LaserTankEE.getApplication().getMenuManager().updateMenuItemState();
	}

	@Override
	public void windowLostFocus(final WindowEvent e) {
	    // Do nothing
	}
    }

    private class StartEventHandler implements MouseListener {
	// handle scroll bars
	public StartEventHandler() {
	    // Do nothing
	}

	@Override
	public void mouseClicked(final MouseEvent e) {
	    try {
		final var x = e.getX();
		final var y = e.getY();
		Editor.this.setPlayerLocation(x, y);
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
    }

    private class SwitcherHandler implements ActionListener {
	SwitcherHandler() {
	    // Do nothing
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
	    try {
		final var cmd = e.getActionCommand();
		final var ae = Editor.this;
		if (cmd.equals(Strings.loadEditor(EditorString.LOWER_GROUND_LAYER))) {
		    ae.changeLayerImpl(Layer.LOWER_GROUND.ordinal());
		} else if (cmd.equals(Strings.loadEditor(EditorString.UPPER_GROUND_LAYER))) {
		    ae.changeLayerImpl(Layer.UPPER_GROUND.ordinal());
		} else if (cmd.equals(Strings.loadEditor(EditorString.LOWER_OBJECTS_LAYER))) {
		    ae.changeLayerImpl(Layer.LOWER_OBJECTS.ordinal());
		} else if (cmd.equals(Strings.loadEditor(EditorString.UPPER_OBJECTS_LAYER))) {
		    ae.changeLayerImpl(Layer.UPPER_OBJECTS.ordinal());
		}
	    } catch (final Exception ex) {
		LaserTankEE.logError(ex);
	    }
	}
    }

    private static final String[] JUMP_LIST = { Integer.toString(0), Integer.toString(1), Integer.toString(2),
	    Integer.toString(3), Integer.toString(4), Integer.toString(5), Integer.toString(6), Integer.toString(7),
	    Integer.toString(8), Integer.toString(9) };
    // Declarations
    private JPanel secondaryPane, outerOutputPane, switcherPane;
    private EditorDraw outputPane;
    private JToggleButton lowerGround, upperGround, lowerObjects, upperObjects;
    private JLabel messageLabel;
    private AbstractArenaObject savedArenaObject;
    private JScrollBar vertScroll, horzScroll;
    private final EventHandler mhandler;
    private final StartEventHandler shandler;
    private final LevelSettings lSettings;
    private AnonymousPicturePicker picker;
    private AbstractArenaObject[] objects;
    private BufferedImageIcon[] editorAppearances;
    private boolean[] objectsEnabled;
    private EditorUndoRedoEngine engine;
    private EditorLocationManager elMgr;
    private boolean arenaChanged;
    private final int activePlayer;
    private final FocusHandler fHandler = new FocusHandler();

    public Editor() {
	this.savedArenaObject = new Ground();
	this.lSettings = new LevelSettings();
	this.mhandler = new EventHandler();
	this.shandler = new StartEventHandler();
	this.engine = new EditorUndoRedoEngine();
	this.objects = ArenaObjectList.getAllObjectsOnLayer(Layer.LOWER_GROUND.ordinal(),
		Settings.getEditorShowAllObjects());
	this.editorAppearances = ArenaObjectList.getAllEditorAppearancesOnLayer(Layer.LOWER_GROUND.ordinal(),
		Settings.getEditorShowAllObjects());
	this.objectsEnabled = ArenaObjectList.getObjectEnabledStatuses(Layer.LOWER_GROUND.ordinal());
	this.arenaChanged = true;
	this.activePlayer = 0;
    }

    public void activeLanguageChanged() {
	EditorLayoutHelper.activeLanguageChanged();
	this.updatePicker();
    }

    public boolean addLevel() {
	final var success = this.addLevelInternal();
	if (success) {
	    CommonDialogs.showDialog(Strings.loadEditor(EditorString.LEVEL_ADDED));
	} else {
	    CommonDialogs.showDialog(Strings.loadEditor(EditorString.LEVEL_ADDING_FAILED));
	}
	return success;
    }

    private boolean addLevelInternal() {
	final var app = LaserTankEE.getApplication();
	final var saveLevel = app.getArenaManager().getArena().getActiveLevelNumber();
	final var success = app.getArenaManager().getArena().addLevel();
	if (success) {
	    this.fixLimits();
	    app.getArenaManager().getArena().fillDefault();
	    // Save the entire level
	    app.getArenaManager().getArena().save();
	    app.getArenaManager().getArena().switchLevel(saveLevel);
	    app.getMenuManager().updateMenuItemState();
	}
	return success;
    }

    public void arenaChanged() {
	this.arenaChanged = true;
    }

    public void changeLayer() {
	final var list = LayerHelper.getNames();
	final var choice = CommonDialogs.showInputDialog(Strings.loadEditor(EditorString.CHANGE_LAYER_PROMPT),
		Strings.loadEditor(EditorString.EDITOR), list, list[this.elMgr.getEditorLocationW()]);
	if (choice != null) {
	    final var len = list.length;
	    var index = -1;
	    for (var z = 0; z < len; z++) {
		if (choice.equals(list[z])) {
		    index = z;
		    break;
		}
	    }
	    if (index != -1) {
		// Update selected button
		if (index == Layer.LOWER_GROUND.ordinal()) {
		    this.lowerGround.setSelected(true);
		} else if (index == Layer.UPPER_GROUND.ordinal()) {
		    this.upperGround.setSelected(true);
		} else if (index == Layer.LOWER_OBJECTS.ordinal()) {
		    this.lowerObjects.setSelected(true);
		} else if (index == Layer.UPPER_OBJECTS.ordinal()) {
		    this.upperObjects.setSelected(true);
		}
		this.changeLayerImpl(index);
	    }
	}
    }

    void changeLayerImpl(final int layer) {
	this.elMgr.setEditorLocationW(layer);
	this.updatePicker();
	this.redrawEditor();
    }

    public void clearHistory() {
	final var app = LaserTankEE.getApplication();
	this.engine = new EditorUndoRedoEngine();
	app.getMenuManager().updateMenuItemState();
    }

    public boolean checkLimitMinZ() {
	if (this.elMgr != null) {
	    return this.elMgr.getEditorLocationZ() == this.elMgr.getMinEditorLocationZ();
	}
	return false;
    }

    public boolean checkLimitMaxZ() {
	if (this.elMgr != null) {
	    return this.elMgr.getEditorLocationZ() == this.elMgr.getMaxEditorLocationZ();
	}
	return false;
    }

    public boolean checkLimitMinU() {
	if (this.elMgr != null) {
	    return this.elMgr.getEditorLocationU() == this.elMgr.getMinEditorLocationU();
	}
	return false;
    }

    public boolean checkLimitMaxU() {
	if (this.elMgr != null) {
	    return this.elMgr.getEditorLocationU() == this.elMgr.getMaxEditorLocationU();
	}
	return false;
    }

    public boolean checkSetStartPoint() {
	return this.elMgr != null;
    }

    private boolean confirmNonUndoable() {
	final var confirm = CommonDialogs.showConfirmDialog(Strings.loadEditor(EditorString.CONFIRM_CANNOT_BE_UNDONE),
		Strings.loadEditor(EditorString.EDITOR));
	if (confirm == CommonDialogs.YES_OPTION) {
	    this.clearHistory();
	    return true;
	}
	return false;
    }

    void disableOutput() {
	this.theContent.setEnabled(false);
    }

    public void editArena() {
	final var app = LaserTankEE.getApplication();
	if (app.getArenaManager().getLoaded()) {
	    app.setInEditor();
	    // Reset game state
	    app.getGameManager().resetGameState();
	    // Create the managers
	    if (this.arenaChanged) {
		this.elMgr = new EditorLocationManager();
		this.elMgr.setLimitsFromArena(app.getArenaManager().getArena());
		this.arenaChanged = false;
	    }
	    this.updatePicker();
	    this.clearHistory();
	    this.redrawEditor();
	    this.updatePickerLayout();
	    this.rebuildGUI();
	    app.getMenuManager().updateMenuItemState();
	} else {
	    CommonDialogs.showDialog(Strings.loadError(ErrorString.NO_ARENA_OPENED));
	}
    }

    public void editJumpBox(final AbstractJumpObject jumper) {
	final var currentX = jumper.getJumpCols();
	final var currentY = jumper.getJumpRows();
	final var newXStr = CommonDialogs.showInputDialog(Strings.loadEditor(EditorString.HORZ_JUMP),
		Strings.loadEditor(EditorString.EDITOR), Editor.JUMP_LIST, Editor.JUMP_LIST[currentX]);
	if (newXStr != null) {
	    final var newYStr = CommonDialogs.showInputDialog(Strings.loadEditor(EditorString.VERT_JUMP),
		    Strings.loadEditor(EditorString.EDITOR), Editor.JUMP_LIST, Editor.JUMP_LIST[currentY]);
	    if (newYStr != null) {
		final var newX = Integer.parseInt(newXStr);
		final var newY = Integer.parseInt(newYStr);
		jumper.setJumpCols(newX);
		jumper.setJumpRows(newY);
	    }
	}
    }

    void editObject(final int x, final int y) {
	final var app = LaserTankEE.getApplication();
	final var currentObjectIndex = this.picker.getPicked();
	final var xOffset = this.vertScroll.getValue() - this.vertScroll.getMinimum();
	final var yOffset = this.horzScroll.getValue() - this.horzScroll.getMinimum();
	final var gridX = x / Images.getGraphicSize() + EditorViewingWindowManager.getViewingWindowLocationX() - xOffset
		+ yOffset;
	final var gridY = y / Images.getGraphicSize() + EditorViewingWindowManager.getViewingWindowLocationY() + xOffset
		- yOffset;
	try {
	    this.savedArenaObject = app.getArenaManager().getArena().getCell(gridX, gridY,
		    this.elMgr.getEditorLocationZ(), this.elMgr.getEditorLocationW());
	} catch (final ArrayIndexOutOfBoundsException ae) {
	    return;
	}
	final var choices = this.objects;
	final var mo = choices[currentObjectIndex];
	final var instance = mo.clone();
	this.elMgr.setEditorLocationX(gridX);
	this.elMgr.setEditorLocationY(gridY);
	this.savedArenaObject.editorRemoveHook(gridX, gridY, this.elMgr.getEditorLocationZ());
	mo.editorPlaceHook(gridX, gridY, this.elMgr.getEditorLocationZ());
	try {
	    this.updateUndoHistory(this.savedArenaObject, gridX, gridY, this.elMgr.getEditorLocationZ(),
		    this.elMgr.getEditorLocationW(), this.elMgr.getEditorLocationU());
	    app.getArenaManager().getArena().setCell(instance, gridX, gridY, this.elMgr.getEditorLocationZ(),
		    this.elMgr.getEditorLocationW());
	    app.getArenaManager().setDirty(true);
	    app.getMenuManager().updateMenuItemState();
	    this.redrawEditor();
	} catch (final ArrayIndexOutOfBoundsException aioob) {
	    app.getArenaManager().getArena().setCell(this.savedArenaObject, gridX, gridY,
		    this.elMgr.getEditorLocationZ(), this.elMgr.getEditorLocationW());
	    this.redrawEditor();
	}
    }

    void editObjectProperties(final int x, final int y) {
	final var app = LaserTankEE.getApplication();
	final var xOffset = this.vertScroll.getValue() - this.vertScroll.getMinimum();
	final var yOffset = this.horzScroll.getValue() - this.horzScroll.getMinimum();
	final var gridX = x / Images.getGraphicSize() + EditorViewingWindowManager.getViewingWindowLocationX() - xOffset
		+ yOffset;
	final var gridY = y / Images.getGraphicSize() + EditorViewingWindowManager.getViewingWindowLocationY() + xOffset
		- yOffset;
	try {
	    final var mo = app.getArenaManager().getArena().getCell(gridX, gridY, this.elMgr.getEditorLocationZ(),
		    this.elMgr.getEditorLocationW());
	    this.elMgr.setEditorLocationX(gridX);
	    this.elMgr.setEditorLocationY(gridY);
	    if (!mo.defersSetProperties()) {
		final var mo2 = mo.editorPropertiesHook();
		if (mo2 == null) {
		    LaserTankEE.getApplication().showMessage(Strings.loadEditor(EditorString.NO_PROPERTIES));
		} else {
		    this.updateUndoHistory(this.savedArenaObject, gridX, gridY, this.elMgr.getEditorLocationZ(),
			    this.elMgr.getEditorLocationW(), this.elMgr.getEditorLocationU());
		    app.getArenaManager().getArena().setCell(mo2, gridX, gridY, this.elMgr.getEditorLocationZ(),
			    this.elMgr.getEditorLocationW());
		    app.getMenuManager().updateMenuItemState();
		    app.getArenaManager().setDirty(true);
		}
	    } else {
		mo.editorPropertiesHook();
	    }
	    this.redrawEditor();
	} catch (final ArrayIndexOutOfBoundsException aioob) {
	    // Do nothing
	}
    }

    public void editPlayerLocation() {
	// Swap event handlers
	this.secondaryPane.removeMouseListener(this.mhandler);
	this.secondaryPane.addMouseListener(this.shandler);
	LaserTankEE.getApplication().showMessage(Strings.loadEditor(EditorString.SET_START_POINT));
    }

    void enableOutput() {
	final var app = LaserTankEE.getApplication();
	this.theContent.setEnabled(true);
	app.getMenuManager().updateMenuItemState();
    }

    public void exitEditor() {
	final var app = LaserTankEE.getApplication();
	final var mm = app.getArenaManager();
	final var gm = app.getGameManager();
	// Save the entire level
	mm.getArena().save();
	// Reset the player location
	try {
	    gm.resetPlayerLocation();
	} catch (final InvalidArenaException iae) {
	    // Harmless error, ignore it
	}
    }

    public void fillLevel() {
	if (this.confirmNonUndoable()) {
	    LaserTankEE.getApplication().getArenaManager().getArena().fillDefault();
	    LaserTankEE.getApplication().showMessage(Strings.loadEditor(EditorString.LEVEL_FILLED));
	    LaserTankEE.getApplication().getArenaManager().setDirty(true);
	    this.redrawEditor();
	}
    }

    public void fixLimits() {
	// Fix limits
	final var app = LaserTankEE.getApplication();
	if (app.getArenaManager().getArena() != null && this.elMgr != null) {
	    this.elMgr.setLimitsFromArena(app.getArenaManager().getArena());
	}
    }

    public int getEditorLocationU() {
	if (this.elMgr != null) {
	    return this.elMgr.getEditorLocationU();
	}
	return 0;
    }

    public void goToLevelHandler() {
	int locW;
	final var msg = Strings.loadEditor(EditorString.GO_TO_LEVEL);
	String input;
	final var choices = LaserTankEE.getApplication().getLevelInfoList();
	input = CommonDialogs.showInputDialog(Strings.loadEditor(EditorString.GO_TO_WHICH_LEVEL), msg, choices,
		choices[0]);
	if (input != null) {
	    for (locW = 0; locW < choices.length; locW++) {
		if (input.equals(choices[locW])) {
		    this.updateEditorLevelAbsolute(locW);
		    break;
		}
	    }
	}
    }

    public void handleCloseWindow() {
	try {
	    final var app = LaserTankEE.getApplication();
	    var success = false;
	    var status = CommonDialogs.DEFAULT_OPTION;
	    if (app.getArenaManager().getDirty()) {
		status = ArenaManager.showSaveDialog();
		if (status == CommonDialogs.YES_OPTION) {
		    success = app.getArenaManager().saveArena(app.getArenaManager().isArenaProtected());
		    if (success) {
			this.exitEditor();
		    }
		} else if (status == CommonDialogs.NO_OPTION) {
		    app.getArenaManager().setDirty(false);
		    this.exitEditor();
		}
	    } else {
		this.exitEditor();
	    }
	} catch (final Exception ex) {
	    LaserTankEE.logError(ex);
	}
    }

    public boolean newArena() {
	final var app = LaserTankEE.getApplication();
	var success = true;
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
	    app.getGameManager().getPlayerManager().resetPlayerLocation();
	    Arena a = null;
	    try {
		a = ArenaManager.createArena();
	    } catch (final IOException ioe) {
		throw new InvalidArenaException(ioe);
	    }
	    if (success) {
		app.getArenaManager().setArena(a);
		success = this.addLevelInternal();
		if (success) {
		    app.getArenaManager().clearLastUsedFilenames();
		    this.clearHistory();
		}
	    }
	} else {
	    success = false;
	}
	if (success) {
	    this.arenaChanged = true;
	    CommonDialogs.showDialog(Strings.loadEditor(EditorString.ARENA_CREATED));
	} else {
	    CommonDialogs.showDialog(Strings.loadEditor(EditorString.ARENA_CREATION_FAILED));
	}
	return success;
    }

    public void redo() {
	final var app = LaserTankEE.getApplication();
	this.engine.redo();
	final var obj = this.engine.getObject();
	final var x = this.engine.getX();
	final var y = this.engine.getY();
	final var z = this.engine.getZ();
	final var w = this.engine.getW();
	final var u = this.engine.getU();
	this.elMgr.setEditorLocationX(x);
	this.elMgr.setEditorLocationY(y);
	if (x != -1 && y != -1 && z != -1 && u != -1) {
	    final var oldObj = app.getArenaManager().getArena().getCell(x, y, z, w);
	    app.getArenaManager().getArena().setCell(obj, x, y, z, w);
	    this.updateUndoHistory(oldObj, x, y, z, w, u);
	    app.getMenuManager().updateMenuItemState();
	    this.redrawEditor();
	} else {
	    LaserTankEE.getApplication().showMessage(Strings.loadEditor(EditorString.NOTHING_TO_REDO));
	}
    }

    public void redrawEditor() {
	final var z = this.elMgr.getEditorLocationZ();
	final var w = this.elMgr.getEditorLocationW();
	final var u = this.elMgr.getEditorLocationU();
	final var e = LaserTankEE.getApplication().getArenaManager().getArena().getActiveEraNumber();
	if (w == Layer.LOWER_GROUND.ordinal()) {
	    this.redrawEditorBottomGround();
	} else if (w == Layer.UPPER_GROUND.ordinal()) {
	    this.redrawEditorGround();
	} else if (w == Layer.LOWER_OBJECTS.ordinal()) {
	    this.redrawEditorGroundBottomObjects();
	} else if (w == Layer.UPPER_OBJECTS.ordinal()) {
	    this.redrawEditorGroundObjects();
	}
	this.pack();
	this.setTitle(Strings.loadEditor(EditorString.EDITOR_TITLE_1) + (z + 1)
		+ Strings.loadEditor(EditorString.EDITOR_TITLE_2) + (u + 1)
		+ Strings.loadCommon(CommonString.SPACE_DASH_SPACE) + Strings.loadEra(e));
	this.outputPane.repaint();
    }

    private void redrawEditorBottomGround() {
	// Draw the arena in edit mode
	final var app = LaserTankEE.getApplication();
	int x, y;
	int xFix, yFix;
	final var drawGrid = this.outputPane.getGrid();
	for (x = EditorViewingWindowManager.getViewingWindowLocationX(); x <= EditorViewingWindowManager
		.getLowerRightViewingWindowLocationX(); x++) {
	    for (y = EditorViewingWindowManager.getViewingWindowLocationY(); y <= EditorViewingWindowManager
		    .getLowerRightViewingWindowLocationY(); y++) {
		xFix = x - EditorViewingWindowManager.getViewingWindowLocationX();
		yFix = y - EditorViewingWindowManager.getViewingWindowLocationY();
		final var lgobj = app.getArenaManager().getArena().getCell(y, x, this.elMgr.getEditorLocationZ(),
			Layer.LOWER_GROUND.ordinal());
		drawGrid.setImageCell(Images.getImage(lgobj, true), xFix, yFix);
	    }
	}
    }

    private void redrawEditorGround() {
	// Draw the arena in edit mode
	final var app = LaserTankEE.getApplication();
	int x, y;
	int xFix, yFix;
	final var drawGrid = this.outputPane.getGrid();
	for (x = EditorViewingWindowManager.getViewingWindowLocationX(); x <= EditorViewingWindowManager
		.getLowerRightViewingWindowLocationX(); x++) {
	    for (y = EditorViewingWindowManager.getViewingWindowLocationY(); y <= EditorViewingWindowManager
		    .getLowerRightViewingWindowLocationY(); y++) {
		xFix = x - EditorViewingWindowManager.getViewingWindowLocationX();
		yFix = y - EditorViewingWindowManager.getViewingWindowLocationY();
		final var lgobj = app.getArenaManager().getArena().getCell(y, x, this.elMgr.getEditorLocationZ(),
			Layer.LOWER_GROUND.ordinal());
		final var ugobj = app.getArenaManager().getArena().getCell(y, x, this.elMgr.getEditorLocationZ(),
			Layer.UPPER_GROUND.ordinal());
		drawGrid.setImageCell(Images.getCompositeImage(lgobj, ugobj, true), xFix, yFix);
	    }
	}
    }

    private void redrawEditorGroundBottomObjects() {
	// Draw the arena in edit mode
	final var app = LaserTankEE.getApplication();
	int x, y;
	int xFix, yFix;
	final var drawGrid = this.outputPane.getGrid();
	for (x = EditorViewingWindowManager.getViewingWindowLocationX(); x <= EditorViewingWindowManager
		.getLowerRightViewingWindowLocationX(); x++) {
	    for (y = EditorViewingWindowManager.getViewingWindowLocationY(); y <= EditorViewingWindowManager
		    .getLowerRightViewingWindowLocationY(); y++) {
		xFix = x - EditorViewingWindowManager.getViewingWindowLocationX();
		yFix = y - EditorViewingWindowManager.getViewingWindowLocationY();
		final var lgobj = app.getArenaManager().getArena().getCell(y, x, this.elMgr.getEditorLocationZ(),
			Layer.LOWER_GROUND.ordinal());
		final var ugobj = app.getArenaManager().getArena().getCell(y, x, this.elMgr.getEditorLocationZ(),
			Layer.UPPER_GROUND.ordinal());
		final var loobj = app.getArenaManager().getArena().getCell(y, x, this.elMgr.getEditorLocationZ(),
			Layer.LOWER_OBJECTS.ordinal());
		drawGrid.setImageCell(Images.getVirtualCompositeImage(lgobj, ugobj, loobj), xFix, yFix);
	    }
	}
    }

    private void redrawEditorGroundObjects() {
	// Draw the arena in edit mode
	final var app = LaserTankEE.getApplication();
	int x, y;
	int xFix, yFix;
	final var drawGrid = this.outputPane.getGrid();
	for (x = EditorViewingWindowManager.getViewingWindowLocationX(); x <= EditorViewingWindowManager
		.getLowerRightViewingWindowLocationX(); x++) {
	    for (y = EditorViewingWindowManager.getViewingWindowLocationY(); y <= EditorViewingWindowManager
		    .getLowerRightViewingWindowLocationY(); y++) {
		xFix = x - EditorViewingWindowManager.getViewingWindowLocationX();
		yFix = y - EditorViewingWindowManager.getViewingWindowLocationY();
		final var lgobj = app.getArenaManager().getArena().getCell(y, x, this.elMgr.getEditorLocationZ(),
			Layer.LOWER_GROUND.ordinal());
		final var ugobj = app.getArenaManager().getArena().getCell(y, x, this.elMgr.getEditorLocationZ(),
			Layer.UPPER_GROUND.ordinal());
		final var loobj = app.getArenaManager().getArena().getCell(y, x, this.elMgr.getEditorLocationZ(),
			Layer.LOWER_OBJECTS.ordinal());
		final var uoobj = app.getArenaManager().getArena().getCell(y, x, this.elMgr.getEditorLocationZ(),
			Layer.UPPER_OBJECTS.ordinal());
		final var lvobj = app.getArenaManager().getArena().getVirtualCell(y, x, this.elMgr.getEditorLocationZ(),
			Layer.VIRTUAL.ordinal());
		drawGrid.setImageCell(Images.getVirtualCompositeImage(lgobj, ugobj, loobj, uoobj, lvobj), xFix, yFix);
	    }
	}
    }

    public boolean removeLevel() {
	final var app = LaserTankEE.getApplication();
	int level;
	var success = true;
	var choices = app.getLevelInfoList();
	if (choices == null) {
	    choices = app.getLevelInfoList();
	}
	String input;
	input = CommonDialogs.showInputDialog(Strings.loadEditor(EditorString.WHICH_LEVEL_TO_REMOVE),
		Strings.loadEditor(EditorString.REMOVE_LEVEL), choices, choices[0]);
	if (input != null) {
	    for (level = 0; level < choices.length; level++) {
		if (input.equals(choices[level])) {
		    success = app.getArenaManager().getArena().removeLevel(level);
		    if (success) {
			this.fixLimits();
			if (level == this.elMgr.getEditorLocationU()) {
			    // Deleted current level - go to level 1
			    this.updateEditorLevelAbsolute(0);
			}
			app.getMenuManager().updateMenuItemState();
			app.getArenaManager().setDirty(true);
		    }
		    break;
		}
	    }
	} else {
	    // User canceled
	    success = false;
	}
	return success;
    }

    public void rebuildGUI() {
	if (this.theContent != null) {
	    this.updatePicker();
	    this.theContent.removeAll();
	    this.theContent.add(this.outerOutputPane, BorderLayout.CENTER);
	    this.theContent.add(this.messageLabel, BorderLayout.NORTH);
	    this.theContent.add(this.picker.getPicker(), BorderLayout.EAST);
	    this.theContent.add(this.switcherPane, BorderLayout.SOUTH);
	    this.pack();
	}
    }

    public boolean resizeLevel() {
	final var app = LaserTankEE.getApplication();
	int levelSizeZ;
	final var maxF = Arena.getMaxFloors();
	final var minF = Arena.getMinFloors();
	final var msg = Strings.loadEditor(EditorString.RESIZE_LEVEL);
	var success = true;
	String input3;
	input3 = CommonDialogs.showTextInputDialogWithDefault(Strings.loadEditor(EditorString.NUMBER_OF_FLOORS), msg,
		Integer.toString(app.getArenaManager().getArena().getFloors()));
	if (input3 != null) {
	    try {
		levelSizeZ = Integer.parseInt(input3);
		if (levelSizeZ < minF) {
		    throw new NumberFormatException(Strings.loadEditor(EditorString.FLOORS_TOO_LOW));
		}
		if (levelSizeZ > maxF) {
		    throw new NumberFormatException(Strings.loadEditor(EditorString.FLOORS_TOO_HIGH));
		}
		app.getArenaManager().getArena().resize(levelSizeZ, new Ground());
		this.fixLimits();
		// Save the entire level
		app.getArenaManager().getArena().save();
		app.getMenuManager().updateMenuItemState();
		// Redraw
		this.redrawEditor();
	    } catch (final NumberFormatException nf) {
		CommonDialogs.showDialog(nf.getMessage());
		success = false;
	    }
	} else {
	    // User canceled
	    success = false;
	}
	return success;
    }

    public void setLevelSettings() {
	this.lSettings.showSettings();
    }

    public void setPlayerLocation() {
	final var template = new Tank(this.activePlayer + 1);
	final var app = LaserTankEE.getApplication();
	final var oldX = app.getArenaManager().getArena().getStartColumn(this.activePlayer);
	final var oldY = app.getArenaManager().getArena().getStartRow(this.activePlayer);
	final var oldZ = app.getArenaManager().getArena().getStartFloor(this.activePlayer);
	// Erase old player
	try {
	    app.getArenaManager().getArena().setCell(new Ground(), oldX, oldY, oldZ, template.getLayer());
	} catch (final ArrayIndexOutOfBoundsException aioob) {
	    // Ignore
	}
	// Set new player
	app.getArenaManager().getArena().setStartRow(this.activePlayer, this.elMgr.getEditorLocationY());
	app.getArenaManager().getArena().setStartColumn(this.activePlayer, this.elMgr.getEditorLocationX());
	app.getArenaManager().getArena().setStartFloor(this.activePlayer, this.elMgr.getEditorLocationZ());
	app.getArenaManager().getArena().setCell(template, this.elMgr.getEditorLocationX(),
		this.elMgr.getEditorLocationY(), this.elMgr.getEditorLocationZ(), template.getLayer());
    }

    void setPlayerLocation(final int x, final int y) {
	final var template = new Tank(this.activePlayer + 1);
	final var app = LaserTankEE.getApplication();
	final var xOffset = this.vertScroll.getValue() - this.vertScroll.getMinimum();
	final var yOffset = this.horzScroll.getValue() - this.horzScroll.getMinimum();
	final var destX = x / Images.getGraphicSize() + EditorViewingWindowManager.getViewingWindowLocationX() - xOffset
		+ yOffset;
	final var destY = y / Images.getGraphicSize() + EditorViewingWindowManager.getViewingWindowLocationY() + xOffset
		- yOffset;
	final var oldX = app.getArenaManager().getArena().getStartColumn(this.activePlayer);
	final var oldY = app.getArenaManager().getArena().getStartRow(this.activePlayer);
	final var oldZ = app.getArenaManager().getArena().getStartFloor(this.activePlayer);
	// Erase old player
	try {
	    app.getArenaManager().getArena().setCell(new Ground(), oldX, oldY, oldZ, template.getLayer());
	} catch (final ArrayIndexOutOfBoundsException aioob) {
	    // Ignore
	}
	// Set new player
	try {
	    app.getArenaManager().getArena().setStartRow(this.activePlayer, destY);
	    app.getArenaManager().getArena().setStartColumn(this.activePlayer, destX);
	    app.getArenaManager().getArena().setStartFloor(this.activePlayer, this.elMgr.getEditorLocationZ());
	    app.getArenaManager().getArena().setCell(template, destX, destY, this.elMgr.getEditorLocationZ(),
		    template.getLayer());
	    LaserTankEE.getApplication().showMessage(Strings.loadEditor(EditorString.START_POINT_SET));
	} catch (final ArrayIndexOutOfBoundsException aioob) {
	    try {
		app.getArenaManager().getArena().setStartRow(this.activePlayer, oldY);
		app.getArenaManager().getArena().setStartColumn(this.activePlayer, oldX);
		app.getArenaManager().getArena().setCell(template, oldX, oldY, oldZ, template.getLayer());
	    } catch (final ArrayIndexOutOfBoundsException aioob2) {
		// Ignore
	    }
	    LaserTankEE.getApplication().showMessage(Strings.loadEditor(EditorString.AIM_WITHIN_THE_ARENA));
	}
	// Swap event handlers
	this.secondaryPane.removeMouseListener(this.shandler);
	this.secondaryPane.addMouseListener(this.mhandler);
	// Set dirty flag
	app.getArenaManager().setDirty(true);
	this.redrawEditor();
    }

    public void setStatusMessage(final String msg) {
	this.messageLabel.setText(msg);
    }

    @Override
    protected void populateMainPanel() {
	this.messageLabel = new JLabel(Strings.loadCommon(CommonString.SPACE));
	this.outputPane = new EditorDraw();
	this.secondaryPane = new JPanel();
	this.theContent.setLayout(new BorderLayout());
	this.messageLabel.setLabelFor(this.outputPane);
	this.outerOutputPane = RCLGenerator.generateRowColumnLabels();
	this.outerOutputPane.add(this.outputPane, BorderLayout.CENTER);
	this.outputPane.setLayout(new GridLayout(1, 1));
	this.secondaryPane.setLayout(new GridLayout(EditorViewingWindowManager.getViewingWindowSizeX(),
		EditorViewingWindowManager.getViewingWindowSizeY()));
	this.horzScroll = new JScrollBar(Adjustable.HORIZONTAL,
		EditorViewingWindowManager.getMinimumViewingWindowLocationY(),
		EditorViewingWindowManager.getViewingWindowSizeY(),
		EditorViewingWindowManager.getMinimumViewingWindowLocationY(),
		EditorViewingWindowManager.getViewingWindowSizeY());
	this.vertScroll = new JScrollBar(Adjustable.VERTICAL,
		EditorViewingWindowManager.getMinimumViewingWindowLocationX(),
		EditorViewingWindowManager.getViewingWindowSizeX(),
		EditorViewingWindowManager.getMinimumViewingWindowLocationX(),
		EditorViewingWindowManager.getViewingWindowSizeX());
	this.outputPane.add(this.secondaryPane);
	this.secondaryPane.addMouseListener(this.mhandler);
	this.secondaryPane.addMouseMotionListener(this.mhandler);
	this.switcherPane = new JPanel();
	final var switcherHandler = new SwitcherHandler();
	final var switcherGroup = new ButtonGroup();
	this.lowerGround = new JToggleButton(Strings.loadEditor(EditorString.LOWER_GROUND_LAYER));
	this.upperGround = new JToggleButton(Strings.loadEditor(EditorString.UPPER_GROUND_LAYER));
	this.lowerObjects = new JToggleButton(Strings.loadEditor(EditorString.LOWER_OBJECTS_LAYER));
	this.upperObjects = new JToggleButton(Strings.loadEditor(EditorString.UPPER_OBJECTS_LAYER));
	this.lowerGround.setSelected(true);
	this.lowerGround.addActionListener(switcherHandler);
	this.upperGround.addActionListener(switcherHandler);
	this.lowerObjects.addActionListener(switcherHandler);
	this.upperObjects.addActionListener(switcherHandler);
	switcherGroup.add(this.lowerGround);
	switcherGroup.add(this.upperGround);
	switcherGroup.add(this.lowerObjects);
	switcherGroup.add(this.upperObjects);
	this.switcherPane.setLayout(new FlowLayout());
	this.switcherPane.add(this.lowerGround);
	this.switcherPane.add(this.upperGround);
	this.switcherPane.add(this.lowerObjects);
	this.switcherPane.add(this.upperObjects);
	this.setTitle(Strings.loadEditor(EditorString.EDITOR));
    }

    public boolean tryUndo() {
	return this.engine.tryUndo();
    }

    public boolean tryRedo() {
	return this.engine.tryRedo();
    }

    public boolean tryBoth() {
	return this.engine.tryBoth();
    }

    public void undo() {
	final var app = LaserTankEE.getApplication();
	this.engine.undo();
	final var obj = this.engine.getObject();
	final var x = this.engine.getX();
	final var y = this.engine.getY();
	final var z = this.engine.getZ();
	final var w = this.engine.getW();
	final var u = this.engine.getU();
	this.elMgr.setEditorLocationX(x);
	this.elMgr.setEditorLocationY(y);
	if (x != -1 && y != -1 && z != -1 && u != -1) {
	    final var oldObj = app.getArenaManager().getArena().getCell(x, y, z, w);
	    app.getArenaManager().getArena().setCell(obj, x, y, z, w);
	    this.updateRedoHistory(oldObj, x, y, z, w, u);
	    app.getMenuManager().updateMenuItemState();
	    this.redrawEditor();
	} else {
	    LaserTankEE.getApplication().showMessage(Strings.loadEditor(EditorString.NOTHING_TO_UNDO));
	}
    }

    public void updateEditorLevelAbsolute(final int w) {
	final var app = LaserTankEE.getApplication();
	this.elMgr.setEditorLocationU(w);
	// Level Change
	app.getArenaManager().getArena().switchLevel(w);
	this.fixLimits();
	this.rebuildGUI();
	app.getMenuManager().updateMenuItemState();
	this.redrawEditor();
    }

    public void updateEditorPosition(final int z, final int w) {
	final var app = LaserTankEE.getApplication();
	this.elMgr.offsetEditorLocationU(w);
	this.elMgr.offsetEditorLocationZ(z);
	if (w != 0) {
	    // Level Change
	    app.getArenaManager().getArena().switchLevelOffset(w);
	    this.fixLimits();
	    this.rebuildGUI();
	}
	app.getMenuManager().updateMenuItemState();
	this.redrawEditor();
    }

    private void updatePicker() {
	if (this.elMgr != null) {
	    this.objects = ArenaObjectList.getAllObjectsOnLayer(this.elMgr.getEditorLocationW(),
		    Settings.getEditorShowAllObjects());
	    this.editorAppearances = ArenaObjectList.getAllEditorAppearancesOnLayer(this.elMgr.getEditorLocationW(),
		    Settings.getEditorShowAllObjects());
	    this.objectsEnabled = ArenaObjectList.getObjectEnabledStatuses(this.elMgr.getEditorLocationW());
	    final var newImages = this.editorAppearances;
	    final var enabled = this.objectsEnabled;
	    if (this.picker != null) {
		this.picker.updatePicker(newImages, enabled);
	    } else {
		this.picker = new AnonymousPicturePicker(newImages, enabled);
	    }
	    this.updatePickerLayout();
	}
    }

    private void updatePickerLayout() {
	if (this.picker != null) {
	    this.picker.updatePickerLayout(this.outputPane.getLayout().preferredLayoutSize(this.outputPane).height);
	}
    }

    private void updateRedoHistory(final AbstractArenaObject obj, final int x, final int y, final int z, final int w,
	    final int u) {
	this.engine.updateRedoHistory(obj, x, y, z, w, u);
    }

    private void updateUndoHistory(final AbstractArenaObject obj, final int x, final int y, final int z, final int w,
	    final int u) {
	this.engine.updateUndoHistory(obj, x, y, z, w, u);
    }

    @Override
    public void showScreenHook() {
	this.addWindowListener(this.mhandler);
	this.addWindowFocusListener(this.fHandler);
    }

    @Override
    public void hideScreenHook() {
	this.removeWindowListener(this.mhandler);
	this.removeWindowFocusListener(this.fHandler);
    }
}
