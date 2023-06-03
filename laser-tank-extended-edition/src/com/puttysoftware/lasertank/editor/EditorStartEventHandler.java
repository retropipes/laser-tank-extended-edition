package com.puttysoftware.lasertank.editor;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.puttysoftware.lasertank.LaserTankEE;

class EditorStartEventHandler implements MouseListener {
    /**
     * 
     */
    private final Editor editor;

    // handle scroll bars
    public EditorStartEventHandler(Editor theEditor) {
	this.editor = theEditor;
	// Do nothing
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