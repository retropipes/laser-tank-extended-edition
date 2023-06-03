package com.puttysoftware.lasertank.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.index.Layer;
import com.puttysoftware.lasertank.locale.EditorString;
import com.puttysoftware.lasertank.locale.Strings;

class EditorSwitcherHandler implements ActionListener {
    /**
     * 
     */
    private final Editor editor;

    EditorSwitcherHandler(Editor theEditor) {
	this.editor = theEditor;
	// Do nothing
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
	new Thread() {
	    @Override
	    public void run() {
		try {
		    final var cmd = e.getActionCommand();
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
		} catch (final Exception ex) {
		    LaserTankEE.logError(ex);
		}
	    }
	}.start();
    }
}