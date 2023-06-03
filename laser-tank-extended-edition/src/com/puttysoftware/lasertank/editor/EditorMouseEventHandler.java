package com.puttysoftware.lasertank.editor;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.puttysoftware.lasertank.LaserTankEE;

class EditorMouseEventHandler extends MouseAdapter {
    private final Editor editor;

    public EditorMouseEventHandler(Editor theEditor) {
	this.editor = theEditor;
    }

    // handle scroll bars
    @Override
    public void mouseClicked(final MouseEvent e) {
	new Thread() {
	    @Override
	    public void run() {
		try {
		    final var me = EditorMouseEventHandler.this.editor;
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
	}.start();
    }

    @Override
    public void mouseDragged(final MouseEvent e) {
	new Thread() {
	    @Override
	    public void run() {
		try {
		    final var me = EditorMouseEventHandler.this.editor;
		    final var x = e.getX();
		    final var y = e.getY();
		    me.editObject(x, y);
		} catch (final Exception ex) {
		    LaserTankEE.logError(ex);
		}
	    }
	}.start();
    }
}