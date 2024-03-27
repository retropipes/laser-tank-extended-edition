package com.puttysoftware.lasertank.editor;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.utility.TaskRunner;

class EditorMouseClickedEventHandler extends MouseAdapter implements Runnable {
    private final Editor editor;
    private MouseEvent event;

    public EditorMouseClickedEventHandler(final Editor theEditor) {
	this.editor = theEditor;
    }

    @Override
    public void mouseClicked(final MouseEvent e) {
	this.event = e;
	TaskRunner.runTask(this);
    }

    @Override
    public void run() {
	try {
	    if (this.editor != null && this.event != null) {
		final var me = EditorMouseClickedEventHandler.this.editor;
		final var x = this.event.getX();
		final var y = this.event.getY();
		if (this.event.isAltDown() || this.event.isAltGraphDown() || this.event.isControlDown()) {
		    me.editObjectProperties(x, y);
		} else {
		    me.editObject(x, y);
		}
	    }
	} catch (final Exception ex) {
	    LaserTankEE.logError(ex);
	}
    }
}