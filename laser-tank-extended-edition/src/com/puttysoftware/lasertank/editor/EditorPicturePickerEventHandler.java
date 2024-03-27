package com.puttysoftware.lasertank.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class EditorPicturePickerEventHandler implements ActionListener {
    private final EditorPicturePicker editorPicturePicker;

    EditorPicturePickerEventHandler(EditorPicturePicker thePicker) {
	this.editorPicturePicker = thePicker;
	// Do nothing
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
	final var cmd = e.getActionCommand();
	// A radio button
	this.editorPicturePicker.index = Integer.parseInt(cmd);
    }
}