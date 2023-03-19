package com.puttysoftware.lasertank.datatype;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.puttysoftware.diane.asset.image.BufferedImageIcon;
import com.puttysoftware.diane.fileio.DataIOUtilities;
import com.puttysoftware.diane.fileio.DataIOUtilities.ImageBMP;
import com.puttysoftware.lasertank.locale.CommonString;
import com.puttysoftware.lasertank.locale.Strings;

public class LaserTankGraphics {
	public static class LTGLoadException extends IOException {
		private static final long serialVersionUID = 2667335570076496956L;

		public LTGLoadException() {
		}
	}

	// Constants
	private static final String FILE_ID = Strings.loadCommon(CommonString.LTG_ID);
	private static final int FILE_ID_LEN = 5;
	private static final int NAME_LEN = 40;
	private static final int AUTHOR_LEN = 30;
	private static final int INFO_LEN = 245;

	public static LaserTankGraphics loadFromFile(final File file) throws IOException {
		try (var fs = new FileInputStream(file)) {
			return LaserTankGraphics.loadFromStream(fs);
		}
	}

	public static LaserTankGraphics loadFromResource(final String resource) throws IOException {
		try (var fs = LaserTankGraphics.class.getResourceAsStream(resource)) {
			return LaserTankGraphics.loadFromStream(fs);
		}
	}

	// Internal stuff
	private static LaserTankGraphics loadFromStream(final InputStream fs) throws IOException {
		// Load file ID
		final var fileIdData = new byte[LaserTankGraphics.FILE_ID_LEN];
		fs.read(fileIdData);
		final var loadFileId = DataIOUtilities.decodeWindowsStringData(fileIdData);
		// Check for a valid ID
		if (!LaserTankGraphics.FILE_ID.equals(loadFileId)) {
			throw new LTGLoadException();
		}
		// Load name
		final var nameData = new byte[LaserTankGraphics.NAME_LEN];
		var bytesRead = fs.read(nameData);
		if (bytesRead < LaserTankGraphics.NAME_LEN) {
			throw new LTGLoadException();
		}
		final var loadName = DataIOUtilities.decodeWindowsStringData(nameData);
		// Load author
		final var authorData = new byte[LaserTankGraphics.AUTHOR_LEN];
		bytesRead = fs.read(authorData);
		if (bytesRead < LaserTankGraphics.AUTHOR_LEN) {
			throw new LTGLoadException();
		}
		final var loadAuthor = DataIOUtilities.decodeWindowsStringData(authorData);
		// Load info
		final var infoData = new byte[LaserTankGraphics.INFO_LEN];
		bytesRead = fs.read(infoData);
		if (bytesRead < LaserTankGraphics.INFO_LEN) {
			throw new LTGLoadException();
		}
		final var loadInfo = DataIOUtilities.decodeWindowsStringData(infoData);
		// Load game image
		final var loadGame = ImageBMP.readFromStream(fs).convertToGameImage();
		// Load mask image
		final var loadMask = ImageBMP.readFromStream(fs).convertToGameImage();
		// Merge game and mask images
		final var mergeGraphics = LaserTankGraphics.mergeGameAndMask(loadGame, loadMask);
		// Return final result
		return new LaserTankGraphics(loadName, loadAuthor, loadInfo, mergeGraphics);
	}

	private static BufferedImageIcon mergeGameAndMask(final BufferedImageIcon game, final BufferedImageIcon mask) {
		final var MASK_TRANSPARENT = Color.black;
		final var GAME_TRANSPARENT = new Color(200, 100, 100, 0).getRGB();
		if (game != null && mask != null) {
			final var gWidth = game.getWidth();
			final var mWidth = mask.getWidth();
			final var gHeight = game.getHeight();
			final var mHeight = mask.getHeight();
			if (gWidth == mWidth && gHeight == mHeight) {
				final var width = gWidth;
				final var height = gHeight;
				final var result = new BufferedImageIcon(game);
				for (var x = 0; x < width; x++) {
					for (var y = 0; y < height; y++) {
						final var pixel = mask.getRGB(x, y);
						final var c = new Color(pixel);
						if (c.equals(MASK_TRANSPARENT)) {
							result.setRGB(x, y, GAME_TRANSPARENT);
						}
					}
				}
				return result;
			}
		}
		return null;
	}

	// Fields
	private final String name;
	private final String author;
	private final String info;
	private final BufferedImageIcon graphics;

	// Constructor - used only internally
	private LaserTankGraphics(final String loadName, final String loadAuthor, final String loadInfo,
			final BufferedImageIcon mergeGraphics) {
		this.name = loadName;
		this.info = loadInfo;
		this.author = loadAuthor;
		this.graphics = mergeGraphics;
	}

	public final String getAuthor() {
		return this.author;
	}

	public final BufferedImageIcon getGraphics() {
		return this.graphics;
	}

	public final String getInfo() {
		return this.info;
	}

	// Methods
	public final String getName() {
		return this.name;
	}
}
