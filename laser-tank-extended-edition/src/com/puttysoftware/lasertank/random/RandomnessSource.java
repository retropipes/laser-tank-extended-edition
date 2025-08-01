/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.random;

import java.util.Random;

/**
 * A source of randomness for the other classes.
 */
class RandomnessSource {
    /** The source. */
    // Fields
    private static final Random theSource = new Random();

    /**
     * Gets the source.
     *
     * @return the source
     */
    // Methods
    private static Random getSource() {
        return RandomnessSource.theSource;
    }

    /**
     * Next bounded integer.
     *
     * @return the bounded integer
     */
    static int nextBoundedInt(final int bound) {
        return RandomnessSource.getSource().nextInt(bound);
    }

    /**
     * Next double.
     *
     * @return the double
     */
    static double nextDouble() {
        return RandomnessSource.getSource().nextDouble();
    }

    /**
     * Next integer.
     *
     * @return the integer
     */
    static int nextInt() {
        return RandomnessSource.getSource().nextInt();
    }

    /**
     * Next long.
     *
     * @return the long
     */
    static long nextLong() {
        return RandomnessSource.getSource().nextLong();
    }

    // Constructor
    private RandomnessSource() {
        // Do nothing
    }
}
