/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.utility;

public class InvalidArenaException extends RuntimeException {
    // Serialization
    private static final long serialVersionUID = 999L;

    // Constructors
    public InvalidArenaException() {
    }

    public InvalidArenaException(final String msg) {
        super(msg);
    }

    public InvalidArenaException(final String msg, final Throwable cause) {
        super(msg, cause);
    }

    public InvalidArenaException(final Throwable cause) {
        super(cause);
    }
}
