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
import com.puttysoftware.lasertank.locale.ErrorString;
import com.puttysoftware.lasertank.locale.Strings;

class GeneralDialog {
	private static MainWindow dialogFrame;
	private static JComponent dialogPane;
	private static CompletableFuture<Void> completer = new CompletableFuture<>();

	public static Future<Void> showDialog(final String text, final String title, final BufferedImageIcon icon) {
		try (var exec = Executors.newSingleThreadExecutor()) {
			exec.submit(() -> {
				// Create and initialize the dialog.
				GeneralDialog.dialogFrame = MainWindow.mainWindow();
				GeneralDialog.dialogPane = GeneralDialog.dialogFrame.createContent();
				// Create and initialize the buttons.
				final var setButton = new JButton(Strings.loadError(ErrorString.OK_BUTTON));
				setButton.setActionCommand(Strings.loadError(ErrorString.OK_BUTTON));
				setButton.addActionListener(h -> {
					GeneralDialog.completer.complete(null);
					GeneralDialog.dialogFrame.restoreSaved();
				});
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
				mainPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
				// Lay out the buttons from left to right.
				final var buttonPane = new JPanel();
				buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
				buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
				buttonPane.add(Box.createHorizontalGlue());
				buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
				buttonPane.add(setButton);
				// Put everything together, using the content pane's BorderLayout.
				GeneralDialog.dialogPane.add(iconPane, BorderLayout.WEST);
				GeneralDialog.dialogPane.add(mainPane, BorderLayout.CENTER);
				GeneralDialog.dialogPane.add(buttonPane, BorderLayout.SOUTH);
				GeneralDialog.dialogFrame.setAndSave(GeneralDialog.dialogPane, title);
			});
			return GeneralDialog.completer;
		}
	}
}
