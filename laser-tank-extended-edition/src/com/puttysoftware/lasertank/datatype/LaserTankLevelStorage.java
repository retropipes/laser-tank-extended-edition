package com.puttysoftware.lasertank.datatype;

import java.io.IOException;

import com.puttysoftware.diane.fileio.GameIODataReader;
import com.puttysoftware.diane.fileio.GameIODataWriter;
import com.puttysoftware.diane.storage.NumberStorage;

class LaserTankLevelStorage extends NumberStorage {
    public LaserTankLevelStorage(final int... shape) {
	super(shape);
    }

    public LaserTankLevelStorage(final LaserTankLevelStorage source) {
	super(source);
    }

    public void save(final GameIODataWriter gio) throws IOException {
	final var shape = this.getShape();
	final var shapeLen = shape.length;
	gio.writeInt(shapeLen);
	for (var s = 0; s < shapeLen; s++) {
	    gio.writeInt(shape[s]);
	}
	final var rawLength = this.getRawLength();
	gio.writeInt(rawLength);
	for (var d = 0; d < rawLength; d++) {
	    gio.writeInt(this.getRawCell(d));
	}
    }

    public static LaserTankLevelStorage load(final GameIODataReader gio) throws IOException {
	final var shapeLen = gio.readInt();
	final var shape = new int[shapeLen];
	for (var s = 0; s < shapeLen; s++) {
	    shape[s] = gio.readInt();
	}
	final var obj = new LaserTankLevelStorage(shape);
	final var rawLength = gio.readInt();
	for (var d = 0; d < rawLength; d++) {
	    obj.setRawCell(gio.readInt(), d);
	}
	return obj;
    }
}
