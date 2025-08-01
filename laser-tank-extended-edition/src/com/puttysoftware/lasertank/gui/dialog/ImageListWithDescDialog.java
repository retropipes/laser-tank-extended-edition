/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import com.puttysoftware.lasertank.asset.image.BufferedImageIcon;
import com.puttysoftware.lasertank.gui.MainWindow;
import com.puttysoftware.lasertank.locale.ErrorString;
import com.puttysoftware.lasertank.locale.Strings;

class ImageListWithDescDialog {
	private static class SubJList<T> extends JList<T> {
		private static final long serialVersionUID = 1L;

		// Subclass JList to workaround bug 4832765, which can cause the
		// scroll pane to not let the user easily scroll up to the beginning
		// of the list. An alternative would be to set the unitIncrement
		// of the JScrollBar to a fixed value. You wouldn't get the nice
		// aligned scrolling, but it should work.
		SubJList(final T[] data) {
			super(data);
		}

		@Override
		public int getScrollableUnitIncrement(final Rectangle visibleRect, final int orientation, final int direction) {
			int row;
			if (orientation == SwingConstants.VERTICAL && direction < 0 && (row = this.getFirstVisibleIndex()) != -1) {
				final var r = this.getCellBounds(row, row);
				if (r.y == visibleRect.y && row != 0) {
					final var loc = r.getLocation();
					loc.y--;
					final var prevIndex = this.locationToIndex(loc);
					final var prevR = this.getCellBounds(prevIndex, prevIndex);
					if (prevR == null || prevR.y >= r.y) {
						return 0;
					}
					return prevR.height;
				}
			}
			return super.getScrollableUnitIncrement(visibleRect, orientation, direction);
		}
	}

	private static MainWindow dialogFrame;
	private static JComponent dialogPane;
	private static String[] descs;
	private static JList<BufferedImageIcon> list;
	private static CompletableFuture<Integer> completer = new CompletableFuture<>();

	private static void setValue(final int newValue) {
		ImageListWithDescDialog.list.setSelectedValue(newValue, true);
		ImageListWithDescDialog.completer.complete(newValue);
	}

	/**
	 * Set up and show the dialog. The first Component argument determines which
	 * frame the dialog depends on; it should be a component in the dialog's
	 * controlling frame. The second Component argument should be null if you want
	 * the dialog to come up with its left corner in the center of the screen;
	 * otherwise, it should be the component on top of which the dialog should
	 * appear.
	 */
	public static Future<Integer> showDialog(final String labelText, final String title,
			final BufferedImageIcon[] possibleValues, final int initialValue, final String descValue,
			final String... possibleDescriptions) {
		try (var exec = Executors.newSingleThreadExecutor()) {
			exec.submit(() -> {
				// Create and initialize the dialog.
				ImageListWithDescDialog.dialogFrame = MainWindow.mainWindow();
				ImageListWithDescDialog.dialogPane = ImageListWithDescDialog.dialogFrame.createContent();
				// Initialize the descriptions
				ImageListWithDescDialog.descs = possibleDescriptions;
				// Create and initialize the buttons.
				final var cancelButton = new JButton(Strings.loadError(ErrorString.CANCEL_BUTTON));
				cancelButton.addActionListener(h -> {
					ImageListWithDescDialog.setValue(CommonDialogs.CANCEL);
					ImageListWithDescDialog.dialogFrame.restoreSaved();
				});
				final var setButton = new JButton(Strings.loadError(ErrorString.OK_BUTTON));
				setButton.setActionCommand(Strings.loadError(ErrorString.OK_BUTTON));
				setButton.addActionListener(h -> {
					ImageListWithDescDialog.setValue(ImageListWithDescDialog.list.getSelectedIndex());
					ImageListWithDescDialog.dialogFrame.restoreSaved();
				});
				// Create a text area to hold the description
				final var descPane = new JPanel();
				final var descArea = new JTextArea(descValue);
				descArea.setLineWrap(true);
				descArea.setWrapStyleWord(true);
				descArea.setPreferredSize(new Dimension(250, 80));
				descPane.add(descArea);
				// main part of the dialog
				ImageListWithDescDialog.list = new SubJList<>(possibleValues);
				ImageListWithDescDialog.list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				ImageListWithDescDialog.list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
				ImageListWithDescDialog.list.setVisibleRowCount(-1);
				ImageListWithDescDialog.list.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(final MouseEvent e) {
						if (e.getClickCount() == 2) {
							setButton.doClick(); // emulate button click
						}
					}
				});
				ImageListWithDescDialog.list.addListSelectionListener(e -> descArea
						.setText(ImageListWithDescDialog.descs[ImageListWithDescDialog.list.getSelectedIndex()]));
				final var listScroller = new JScrollPane(ImageListWithDescDialog.list);
				listScroller.setPreferredSize(new Dimension(250, 80));
				listScroller.setAlignmentX(Component.LEFT_ALIGNMENT);
				// Create a container so that we can add a title around
				// the scroll pane. Can't add a title directly to the
				// scroll pane because its background would be white.
				// Lay out the label and scroll pane from top to bottom.
				final var listPane = new JPanel();
				listPane.setLayout(new BoxLayout(listPane, BoxLayout.PAGE_AXIS));
				final var label = new JLabel(labelText);
				label.setLabelFor(ImageListWithDescDialog.list);
				listPane.add(label);
				listPane.add(Box.createRigidArea(new Dimension(0, 5)));
				listPane.add(listScroller);
				listPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
				// Lay out the buttons from left to right.
				final var buttonPane = new JPanel();
				buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
				buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
				buttonPane.add(Box.createHorizontalGlue());
				buttonPane.add(cancelButton);
				buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
				buttonPane.add(setButton);
				// Put everything together, using the content pane's BorderLayout.
				ImageListWithDescDialog.dialogPane.add(listPane, BorderLayout.NORTH);
				ImageListWithDescDialog.dialogPane.add(descPane, BorderLayout.CENTER);
				ImageListWithDescDialog.dialogPane.add(buttonPane, BorderLayout.PAGE_END);
				// Initialize values.
				ImageListWithDescDialog.setValue(initialValue);
				ImageListWithDescDialog.dialogFrame.setAndSave(ImageListWithDescDialog.dialogPane, title);
			});
			return ImageListWithDescDialog.completer;
		}
	}
}
