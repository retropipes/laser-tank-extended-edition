package com.puttysoftware.lasertank.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.index.Layer;
import com.puttysoftware.lasertank.locale.EditorString;
import com.puttysoftware.lasertank.locale.Strings;
import com.puttysoftware.lasertank.utility.TaskRunner;

class EditorSwitcherHandler implements ActionListener, Runnable {
    private final Editor editor;
    private ActionEvent event;

    EditorSwitcherHandler(final Editor theEditor) {
	this.editor = theEditor;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
	this.event = e;
	TaskRunner.runTask(this);
    }

    @Override
    public void run() {
	try {
	    if (this.editor != null && this.event != null) {
		final var cmd = this.event.getActionCommand();
		final var ae = EditorSwitcherHandler.this.editor;
		if (cmd.equals(Strings.loadEditor(EditorString.LOWER_GROUND_LAYER))) {
		    ae.changeLayerImpl(Layer.LOWER_GROUND.ordinal());
		} else if (cmd.equals(Strings.loadEditor(EditorString.UPPER_GROUND_LAYER))) {
		    ae.changeLayerImpl(Layer.UPPER_GROUND.ordinal());
		} else if (cmd.equals(Strings.loadEditor(EditorString.LOWER_OBJECTS_LAYER))) {
		    ae.changeLayerImpl(Layer.LOWER_OBJECTS.ordinal());
		} else if (cmd.equals(Strings.loadEditor(EditorString.UPPER_OBJECTS_LAYER))) {
		    ae.changeLayerImpl(Layer.UPPER_OBJECTS.ordinal());
		}
	    }
	} catch (final Exception ex) {
	    LaserTankEE.logError(ex);
	}
    }
}