package com.puttysoftware.lasertank.datatype;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import com.puttysoftware.lasertank.fileio.DataIOUtilities;
import com.puttysoftware.lasertank.scoring.LaserTankScore;
import com.puttysoftware.lasertank.scoring.LaserTankScoreTable;

public class LaserTankHighScores {
    // Constants
    private static final int NAME_LEN = 6;

    public static LaserTankHighScores loadFromFile(final File file) throws IOException {
	try (var fs = new FileInputStream(file)) {
	    return LaserTankHighScores.loadFromStream(fs);
	}
    }

    public static LaserTankHighScores loadFromResource(final String resource) throws IOException {
	try (var fs = LaserTankHighScores.class.getResourceAsStream(resource)) {
	    return LaserTankHighScores.loadFromStream(fs);
	}
    }

    // Internal stuff
    private static LaserTankHighScores loadFromStream(final InputStream fs) throws IOException {
	// Create temporary storage
	final var nameTemp = new ArrayList<String>();
	final var moveTemp = new ArrayList<Integer>();
	final var shotTemp = new ArrayList<Integer>();
	var success = true;
	var bytesRead = 0;
	while (success) {
	    try {
		// Load name
		final var nameData = new byte[LaserTankHighScores.NAME_LEN];
		bytesRead = fs.read(nameData);
		if (bytesRead < LaserTankHighScores.NAME_LEN) {
		    success = false;
		    break;
		}
		final var loadName = DataIOUtilities.decodeWindowsStringData(nameData);
		// Load moves
		final var moveData = new byte[Short.BYTES];
		bytesRead = fs.read(moveData);
		if (bytesRead < Short.BYTES) {
		    success = false;
		    break;
		}
		final int moves = ByteBuffer.wrap(moveData).asShortBuffer().get();
		// Load shots
		final var shotData = new byte[Short.BYTES];
		bytesRead = fs.read(shotData);
		if (bytesRead < Short.BYTES) {
		    success = false;
		    break;
		}
		final int shots = ByteBuffer.wrap(shotData).asShortBuffer().get();
		// Add values to temporary storage
		nameTemp.add(loadName);
		moveTemp.add(moves);
		shotTemp.add(shots);
	    } catch (final EOFException e) {
		success = false;
		break;
	    }
	}
	// Convert temporary storage to the correct format
	final var nameLoad = nameTemp.toArray(new String[nameTemp.size()]);
	final var len = nameLoad.length;
	final var moveLoadTemp = moveTemp.toArray(new Integer[moveTemp.size()]);
	final var shotLoadTemp = shotTemp.toArray(new Integer[shotTemp.size()]);
	final var table = new LaserTankScoreTable(len);
	for (var x = 0; x < len; x++) {
	    table.add(new LaserTankScore(moveLoadTemp[x], shotLoadTemp[x], nameLoad[x]));
	}
	// Return final result
	return new LaserTankHighScores(table);
    }

    // Fields
    private final LaserTankScoreTable scoreData;

    // Constructor - used only internally
    private LaserTankHighScores(final LaserTankScoreTable data) {
	this.scoreData = data;
    }

    // Methods
    public final LaserTankScoreTable getScores() {
	return this.scoreData;
    }
}
