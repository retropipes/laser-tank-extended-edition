/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.puttysoftware.lasertank.asset.image.BufferedImageIcon;
import com.puttysoftware.lasertank.gui.MainWindow;
import com.puttysoftware.lasertank.locale.DialogString;
import com.puttysoftware.lasertank.locale.ErrorString;
import com.puttysoftware.lasertank.locale.Strings;

class InputDialog {
	private static MainWindow dialogFrame;
	private static JComponent dialogPane;
	private static CompletableFuture<Integer> completer;

	private static void initializeDialog(final String text, final String title, final BufferedImageIcon icon,
			final String[] possibleValues) {
		// Create and initialize the dialog.
		InputDialog.dialogFrame = MainWindow.mainWindow();
		InputDialog.dialogPane = InputDialog.dialogFrame.createContent();
		// main part of the dialog
		final var iconPane = new JPanel();
		final var iconLabel = new JLabel(icon);
		iconPane.setLayout(new BoxLayout(iconPane, BoxLayout.PAGE_AXIS));
		iconPane.add(iconLabel);
		final var mainPane = new JPanel();
		mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.PAGE_AXIS));
		final var textLabel = new JTextArea(text);
		textLabel.setEditable(false);
		textLabel.setLineWrap(true);
		mainPane.add(textLabel);
		mainPane.add(Box.createRigidArea(new Dimension(0, 5)));
		mainPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		// Lay out the buttons from left to right.
		final var buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		buttonPane.add(Box.createHorizontalGlue());
		// Create and initialize the buttons.
		for (var i = 0; i < possibleValues.length; i++) {
			final var button = new JButton(possibleValues[i]);
			button.setActionCommand(Integer.toString(i));
			button.addActionListener(h -> {
				InputDialog.setValue(Integer.parseInt(h.getActionCommand()));
				InputDialog.dialogFrame.restoreSaved();
			});
			buttonPane.add(button);
			if (i != possibleValues.length - 1) {
				buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
			}
		}
		// Put everything together, using the content pane's BorderLayout.
		InputDialog.dialogPane.add(iconPane, BorderLayout.WEST);
		InputDialog.dialogPane.add(mainPane, BorderLayout.CENTER);
		InputDialog.dialogPane.add(buttonPane, BorderLayout.SOUTH);
		InputDialog.dialogFrame.setAndSave(InputDialog.dialogPane, title);
	}

	private static void setValue(final int newValue) {
		InputDialog.completer.complete(newValue);
	}

	/**
	 * Set up and show the dialog. The first Component argument determines which
	 * frame the dialog depends on; it should be a component in the dialog's
	 * controlling frame. The second Component argument should be null if you want
	 * the dialog to come up with its left corner in the center of the screen;
	 * otherwise, it should be the component on top of which the dialog should
	 * appear.
	 */
	public static Future<Integer> showConfirmDialog(final String text, final String title,
			final BufferedImageIcon icon) {
		InputDialog.completer = new CompletableFuture<>();
		try (var exec = Executors.newSingleThreadExecutor()) {
			exec.submit(() -> {
				final String[] possibleValues = { Strings.loadDialog(DialogString.YES),
						Strings.loadDialog(DialogString.NO) };
				InputDialog.initializeDialog(text, title, icon, possibleValues);
			});
			return InputDialog.completer;
		}
	}

	public static Future<Integer> showDialog(final String text, final String title, final BufferedImageIcon icon,
			final String[] possibleValues) {
		InputDialog.completer = new CompletableFuture<>();
		try (var exec = Executors.newSingleThreadExecutor()) {
			exec.submit(() -> {
				InputDialog.initializeDialog(text, title, icon, possibleValues);
			});
			return InputDialog.completer;
		}
	}

	public static Future<Integer> showYNCConfirmDialog(final String text, final String title,
			final BufferedImageIcon icon) {
		InputDialog.completer = new CompletableFuture<>();
		try (var exec = Executors.newSingleThreadExecutor()) {
			exec.submit(() -> {
				final String[] possibleValues = { Strings.loadDialog(DialogString.YES),
						Strings.loadDialog(DialogString.NO), Strings.loadError(ErrorString.CANCEL_BUTTON) };
				InputDialog.initializeDialog(text, title, icon, possibleValues);
			});
			return InputDialog.completer;
		}
	}
}
