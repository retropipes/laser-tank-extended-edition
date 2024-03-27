package com.puttysoftware.lasertank.editor;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.puttysoftware.lasertank.LaserTankEE;

class EditorStartEventHandler extends MouseAdapter {
    private final Editor editor;

    // handle scroll bars
    public EditorStartEventHandler(final Editor theEditor) {
	this.editor = theEditor;
    }

    @Override
    public void mouseClicked(final MouseEvent e) {
	try {
	    final var x = e.getX();
	    final var y = e.getY();
	    this.editor.setPlayerLocation(x, y);
	} catch (final Exception ex) {
	    LaserTankEE.logError(ex);
	}
    }
}