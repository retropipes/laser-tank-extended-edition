package com.puttysoftware.lasertank.datatype;

import java.awt.FileDialog;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Deque;
import java.util.LinkedList;

import javax.swing.JFrame;

import com.puttysoftware.diane.fileio.DataIOUtilities;
import com.puttysoftware.diane.fileio.utility.FilenameChecker;
import com.puttysoftware.diane.gui.dialog.CommonDialogs;
import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.index.GameAction;
import com.puttysoftware.lasertank.locale.DialogString;
import com.puttysoftware.lasertank.locale.GameString;
import com.puttysoftware.lasertank.locale.Strings;
import com.puttysoftware.lasertank.settings.Settings;

public class LaserTankPlayback {
	public static class LPBLoadException extends IOException {
		private static final long serialVersionUID = 8993383672852880300L;

		public LPBLoadException() {
		}
	}

	// Constants
	private static final int LEVEL_NAME_LEN = 31;
	private static final int AUTHOR_LEN = 31;

	private static Deque<LaserTankPlaybackEntry> decodeRawData(final byte[] d) throws LPBLoadException {
		final Deque<LaserTankPlaybackEntry> decoded = new LinkedList<>();
		for (final byte element : d) {
			decoded.add(LaserTankPlayback.decodeRawDataPoint(element));
		}
		return decoded;
	}

	private static LaserTankPlaybackEntry decodeRawDataPoint(final byte d) throws LPBLoadException {
		final var game = LaserTankEE.getApplication().getGameManager();
		return switch (d) {
			case 0x20 -> {
				game.loadReplay(GameAction.SHOOT, 0, 0);
				yield new LaserTankPlaybackEntry(GameAction.SHOOT, 0, 0, 0, 0);
			}
			case 0x25 -> {
				game.loadReplay(GameAction.MOVE, -1, 0);
				yield new LaserTankPlaybackEntry(GameAction.MOVE, 0, -1, 0, 0);
			}
			case 0x26 -> {
				game.loadReplay(GameAction.MOVE, 0, -1);
				yield new LaserTankPlaybackEntry(GameAction.MOVE, 0, -1, 0, 0);
			}
			case 0x27 -> {
				game.loadReplay(GameAction.MOVE, 1, 0);
				yield new LaserTankPlaybackEntry(GameAction.MOVE, 1, 0, 0, 0);
			}
			case 0x28 -> {
				game.loadReplay(GameAction.MOVE, 0, 1);
				yield new LaserTankPlaybackEntry(GameAction.MOVE, 0, 1, 0, 0);
			}
			default -> throw new LPBLoadException();
		};
	}

	private static String getExtension(final String s) {
		String ext = null;
		final var i = s.lastIndexOf('.');
		if (i > 0 && i < s.length() - 1) {
			ext = s.substring(i + 1).toLowerCase();
		}
		return ext;
	}

	private static String getFileNameOnly(final String s) {
		String fno = null;
		final var i = s.lastIndexOf(File.separatorChar);
		if (i > 0 && i < s.length() - 1) {
			fno = s.substring(i + 1);
		} else {
			fno = s;
		}
		return fno;
	}

	private static String getNameWithoutExtension(final String s) {
		String ext = null;
		final var i = s.lastIndexOf('.');
		if (i > 0 && i < s.length() - 1) {
			ext = s.substring(0, i);
		} else {
			ext = s;
		}
		return ext;
	}

	private static void loadFile(final String filename) {
		if (!FilenameChecker
				.isFilenameOK(LaserTankPlayback.getNameWithoutExtension(LaserTankPlayback.getFileNameOnly(filename)))) {
			CommonDialogs.showErrorDialog(Strings.loadDialog(DialogString.ILLEGAL_CHARACTERS),
					Strings.loadDialog(DialogString.LOAD));
		} else {
			final var lpblt = new LaserTankPlaybackLoadTask(new File(filename));
			lpblt.start();
		}
	}

	static LaserTankPlayback loadFromFile(final File file) throws IOException {
		try (var fs = new FileInputStream(file)) {
			return LaserTankPlayback.loadFromStream(fs);
		}
	}

	// Internal stuff
	private static LaserTankPlayback loadFromStream(final InputStream fs) throws IOException {
		// Load level name
		final var levelNameData = new byte[LaserTankPlayback.LEVEL_NAME_LEN];
		var bytesRead = fs.read(levelNameData);
		if (bytesRead < LaserTankPlayback.LEVEL_NAME_LEN) {
			throw new LPBLoadException();
		}
		final var loadLevelName = DataIOUtilities.decodeWindowsStringData(levelNameData);
		// Load author
		final var authorData = new byte[LaserTankPlayback.AUTHOR_LEN];
		bytesRead = fs.read(authorData);
		if (bytesRead < LaserTankPlayback.AUTHOR_LEN) {
			throw new LPBLoadException();
		}
		final var loadAuthor = DataIOUtilities.decodeWindowsStringData(authorData);
		// Load info
		final var levelNumberData = new byte[Short.BYTES];
		bytesRead = fs.read(levelNumberData);
		if (bytesRead < Short.BYTES) {
			throw new LPBLoadException();
		}
		final var loadLevelNumber = DataIOUtilities.unsignedShortByteArrayToInt(levelNumberData);
		// Load recording size
		final var recordingSizeData = new byte[Short.BYTES];
		bytesRead = fs.read(recordingSizeData);
		if (bytesRead < Short.BYTES) {
			throw new LPBLoadException();
		}
		final var recordingSize = DataIOUtilities.unsignedShortByteArrayToInt(levelNumberData);
		// Load raw recording data
		final var rawRecordingData = new byte[recordingSize];
		bytesRead = fs.read(rawRecordingData);
		if (bytesRead < recordingSize) {
			throw new LPBLoadException();
		}
		// Decode raw recording data
		final var loadRecordingData = LaserTankPlayback.decodeRawData(rawRecordingData);
		// Return final result
		return new LaserTankPlayback(loadLevelName, loadAuthor, loadLevelNumber, loadRecordingData);
	}

	public static void loadLPB() {
		String filename, extension, file, dir;
		final var lastOpen = Settings.getLastDirOpen();
		final var fd = new FileDialog((JFrame) null, Strings.loadGame(GameString.LOAD_PLAYBACK), FileDialog.LOAD);
		fd.setDirectory(lastOpen);
		fd.setVisible(true);
		file = fd.getFile();
		dir = fd.getDirectory();
		if (file != null && dir != null) {
			filename = dir + file;
			extension = LaserTankPlayback.getExtension(filename);
			if (extension.equals(FileExtensions.getOldPlaybackExtension())) {
				LaserTankPlayback.loadFile(filename);
			} else {
				CommonDialogs.showDialog(Strings.loadDialog(DialogString.NON_PLAYBACK_FILE));
			}
		}
	}

	// Fields
	private final String levelName;
	private final String author;
	private final int levelNumber;
	private final Deque<LaserTankPlaybackEntry> recordingData;

	// Constructor - used only internally
	private LaserTankPlayback(final String loadLevelName, final String loadAuthor, final int loadLevelNumber,
			final Deque<LaserTankPlaybackEntry> loadRecordingData) {
		this.levelName = loadLevelName;
		this.author = loadAuthor;
		this.levelNumber = loadLevelNumber;
		this.recordingData = loadRecordingData;
	}

	public final String getAuthor() {
		return this.author;
	}

	// Methods
	public final String getLevelName() {
		return this.levelName;
	}

	public final int getLevelNumber() {
		return this.levelNumber;
	}

	public final Deque<LaserTankPlaybackEntry> getRecordingData() {
		return this.recordingData;
	}
}
