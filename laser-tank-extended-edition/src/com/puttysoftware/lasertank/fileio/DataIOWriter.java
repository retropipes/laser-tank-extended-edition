/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.fileio;

import java.io.File;

public interface DataIOWriter extends AutoCloseable {
    @Override
    void close() throws DataIOException;

    // Methods
    DataMode getDataIOMode();

    File getFile();

    void writeBoolean(boolean value) throws DataIOException;

    void writeByte(byte value) throws DataIOException;

    void writeDouble(double value) throws DataIOException;

    void writeInt(int value) throws DataIOException;

    void writeLong(long value) throws DataIOException;

    void writeString(String value) throws DataIOException;

    void writeUnsignedByte(int value) throws DataIOException;
}