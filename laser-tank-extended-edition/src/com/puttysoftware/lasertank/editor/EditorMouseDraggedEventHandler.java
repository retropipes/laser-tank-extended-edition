package com.puttysoftware.lasertank.editor;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.puttysoftware.lasertank.tasks.AppTaskManager;

class EditorMouseDraggedEventHandler extends MouseAdapter implements Runnable {
    private final Editor editor;
    private MouseEvent event;

    public EditorMouseDraggedEventHandler(final Editor theEditor) {
	this.editor = theEditor;
    }

    @Override
    public void mouseDragged(final MouseEvent e) {
	this.event = e;
	AppTaskManager.runTask(this);
    }

    @Override
    public void run() {
	try {
	    if (this.editor != null && this.event != null) {
		final var me = this.editor;
		final var x = this.event.getX();
		final var y = this.event.getY();
		me.editObject(x, y);
	    }
	} catch (final Exception ex) {
	    AppTaskManager.error(ex);
	}
    }
}