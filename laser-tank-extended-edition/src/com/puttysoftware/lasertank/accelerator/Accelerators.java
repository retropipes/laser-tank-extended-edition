/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.accelerator;

import javax.swing.KeyStroke;

public abstract class Accelerators {
    public KeyStroke fileNewAccel, fileOpenAccel, fileCloseAccel, fileSaveAccel, fileSaveAsAccel, filePrintAccel,
	    fileExitAccel;
    public KeyStroke editUndoAccel, editRedoAccel, editCutLevelAccel, editCopyLevelAccel, editPasteLevelAccel,
	    editPasteAndInsertAccel, editPreferencesAccel;
    public KeyStroke playPlayArenaAccel, playEditArenaAccel;
    public KeyStroke gameResetAccel, gameShowTableAccel;
    public KeyStroke editorClearHistoryAccel, editorGoToLocationAccel, editorUpOneLevelAccel, editorDownOneLevelAccel;

    Accelerators() {
	// Do nothing
    }

    public static Accelerators getInstance(final boolean useClassic) {
	if (useClassic) {
	    return new ClassicAccelerators();
	} else {
	    return new ModernAccelerators();
	}
    }
}
