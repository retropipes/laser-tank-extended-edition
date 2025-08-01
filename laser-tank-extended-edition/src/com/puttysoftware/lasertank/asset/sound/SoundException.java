/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.asset.sound;

import java.io.Externalizable;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class SoundException extends RuntimeException implements Externalizable {
    public SoundException() {
    }

    public SoundException(final String message) {
        super(message);
    }

    public SoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public SoundException(final String message, final Throwable cause, final boolean enableSuppression,
            final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public SoundException(final Throwable cause) {
        super(cause);
    }

    @Override
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        throw new NotSerializableException();
    }

    @Override
    public void writeExternal(final ObjectOutput out) throws IOException {
        throw new NotSerializableException();
    }
}
