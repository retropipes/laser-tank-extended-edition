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
	final int[] shape = this.getShape();
	final int shapeLen = shape.length;
	gio.writeInt(shapeLen);
	for (int s = 0; s < shapeLen; s++) {
	    gio.writeInt(shape[s]);
	}
	final int rawLength = this.getRawLength();
	gio.writeInt(rawLength);
	for (int d = 0; d < rawLength; d++) {
	    gio.writeInt(this.getRawCell(d));
	}
    }

    public static LaserTankLevelStorage load(final GameIODataReader gio) throws IOException {
	final int shapeLen = gio.readInt();
	final int[] shape = new int[shapeLen];
	for (int s = 0; s < shapeLen; s++) {
	    shape[s] = gio.readInt();
	}
	final LaserTankLevelStorage obj = new LaserTankLevelStorage(shape);
	final int rawLength = gio.readInt();
	for (int d = 0; d < rawLength; d++) {
	    obj.setRawCell(gio.readInt(), d);
	}
	return obj;
    }
}
