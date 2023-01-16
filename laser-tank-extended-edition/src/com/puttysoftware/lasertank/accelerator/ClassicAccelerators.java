/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.accelerator;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import com.puttysoftware.lasertank.locale.global.GlobalStrings;
import com.puttysoftware.lasertank.locale.global.UntranslatedString;

class ClassicAccelerators extends Accelerators {
    ClassicAccelerators() {
	int modKey;
	if (System.getProperty(GlobalStrings.loadUntranslated(UntranslatedString.OS_NAME))
		.equalsIgnoreCase(GlobalStrings.loadUntranslated(UntranslatedString.MAC_OS_X))) {
	    modKey = InputEvent.META_DOWN_MASK;
	} else {
	    modKey = InputEvent.CTRL_DOWN_MASK;
	}
	this.fileNewAccel = KeyStroke.getKeyStroke(KeyEvent.VK_N, 0);
	this.fileOpenAccel = KeyStroke.getKeyStroke(KeyEvent.VK_O, 0);
	this.fileCloseAccel = KeyStroke.getKeyStroke(KeyEvent.VK_W, 0);
	this.fileSaveAccel = KeyStroke.getKeyStroke(KeyEvent.VK_S, 0);
	this.fileSaveAsAccel = KeyStroke.getKeyStroke(KeyEvent.VK_A, 0);
	this.filePrintAccel = KeyStroke.getKeyStroke(KeyEvent.VK_P, 0);
	this.fileExitAccel = KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0);
	this.editUndoAccel = KeyStroke.getKeyStroke(KeyEvent.VK_U, 0);
	this.editRedoAccel = KeyStroke.getKeyStroke(KeyEvent.VK_Z, 0);
	this.editCutLevelAccel = KeyStroke.getKeyStroke(KeyEvent.VK_X, 0);
	this.editCopyLevelAccel = KeyStroke.getKeyStroke(KeyEvent.VK_C, 0);
	this.editPasteLevelAccel = KeyStroke.getKeyStroke(KeyEvent.VK_V, 0);
	this.editPasteAndInsertAccel = KeyStroke.getKeyStroke(KeyEvent.VK_F, 0);
	this.editPreferencesAccel = KeyStroke.getKeyStroke(KeyEvent.VK_COMMA, 0);
	this.editorClearHistoryAccel = KeyStroke.getKeyStroke(KeyEvent.VK_Y, 0);
	this.editorGoToLocationAccel = KeyStroke.getKeyStroke(KeyEvent.VK_G, 0);
	this.playPlayArenaAccel = KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0);
	this.playEditArenaAccel = KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0);
	this.gameResetAccel = KeyStroke.getKeyStroke(KeyEvent.VK_R, 0);
	this.gameShowTableAccel = KeyStroke.getKeyStroke(KeyEvent.VK_T, 0);
	this.editorUpOneLevelAccel = KeyStroke.getKeyStroke(KeyEvent.VK_UP, modKey);
	this.editorDownOneLevelAccel = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, modKey);
    }
}
