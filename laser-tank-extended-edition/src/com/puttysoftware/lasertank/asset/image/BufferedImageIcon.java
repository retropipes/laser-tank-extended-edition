/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.asset.image;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.Icon;

public class BufferedImageIcon extends BufferedImage implements Icon {
    // Fields
    private static final int DEFAULT_TYPE = BufferedImage.TYPE_INT_ARGB;
    private static final int SCALE_MIN = 100;
    private static final double SCALE_MULT = 100.0;
    protected static int SCALE = 100;

    /**
     * Convenience method for determining the normalized scale.
     *
     * @return the normalized scale value
     */
    public static double getNormalizedScale() {
        return BufferedImageIcon.SCALE / BufferedImageIcon.SCALE_MULT;
    }

    /**
     * Gets the global scaling factor for image drawing.
     *
     * @return the global scaling factor
     */
    public static int getScale() {
        return BufferedImageIcon.SCALE;
    }

    /**
     * Convenience method for scaling fixed values.
     *
     * @param value the input
     * @return the output
     */
    public static int getScaledValue(final int value) {
        return (int) (value * BufferedImageIcon.SCALE / BufferedImageIcon.SCALE_MULT);
    }

    /**
     * Convenience method for getting the scaling multiplier.
     *
     * @return the scaling multiplier
     */
    public static int getScaleMult() {
        return (int) BufferedImageIcon.SCALE_MULT;
    }

    /**
     * Sets the global scaling factor for image drawing. A value of 100 means 100%
     * scaling (smallest allowed value). A value of 200 means 200% scaling (Apple
     * Retina mode).
     *
     * @param value the new global scaling factor
     * @throws IllegalArgumentException if the global scaling factor isn't valid
     */
    public static void setScale(final int value) {
        if (value < BufferedImageIcon.SCALE_MIN) {
            throw new IllegalArgumentException(Integer.toString(value));
        }
        BufferedImageIcon.SCALE = value;
    }

    /**
     * Creates a BufferedImageIcon based on a BufferedImage object.
     *
     * @param bi
     */
    public BufferedImageIcon(final BufferedImage bi) {
        super(bi.getWidth(), bi.getHeight(), BufferedImageIcon.DEFAULT_TYPE);
        for (var x = 0; x < bi.getWidth(); x++) {
            for (var y = 0; y < bi.getHeight(); y++) {
                this.setRGB(x, y, bi.getRGB(x, y));
            }
        }
    }

    /**
     * Creates a square BufferedImageIcon of a given size and color.
     *
     * @param size
     * @param color
     */
    public BufferedImageIcon(final int size, final Color color) {
        super(size, size, BufferedImageIcon.DEFAULT_TYPE);
        for (var x = 0; x < size; x++) {
            for (var y = 0; y < size; y++) {
                this.setRGB(x, y, color.getRGB());
            }
        }
    }

    // Constructors
    /**
     * Creates a BufferedImageIcon of a given size.
     *
     * @param width
     * @param height
     */
    public BufferedImageIcon(final int width, final int height) {
        super(width, height, BufferedImageIcon.DEFAULT_TYPE);
    }

    /**
     * Gets the pixel height of this BufferedImageIcon, adjusted for the scale
     * factor.
     *
     * @return the adjusted height of this BufferedImageIcon, in pixels
     */
    @Override
    public int getIconHeight() {
        return (int) BufferedImageIcon.SCALE_MULT * this.getHeight() / BufferedImageIcon.SCALE;
    }

    /**
     * Gets the pixel width of this BufferedImageIcon, adjusted for the scale
     * factor.
     *
     * @return the adjusted width of this BufferedImageIcon, in pixels
     */
    @Override
    public int getIconWidth() {
        return (int) BufferedImageIcon.SCALE_MULT * this.getWidth() / BufferedImageIcon.SCALE;
    }

    /**
     * Paints the BufferedImageIcon, using the given Graphics, on the given
     * Component at the given x, y location, using the scale factor.
     *
     * @param c the Component to paint on
     * @param g the Graphics to paint with
     * @param x the horizontal (X) coordinate to start drawing
     * @param y the vertical (Y) coordinate to start drawing
     */
    @Override
    public void paintIcon(final Component c, final Graphics g, final int x, final int y) {
        if (g != null) {
            if (BufferedImageIcon.SCALE > BufferedImageIcon.SCALE_MIN) {
                final var factor = BufferedImageIcon.SCALE_MULT / BufferedImageIcon.SCALE;
                final var width = this.getWidth(c);
                final var height = this.getHeight(c);
                final var g2d = (Graphics2D) g.create(x, y, width, height);
                g2d.scale(factor, factor);
                g2d.drawImage(this, 0, 0, c);
                g2d.scale(1, 1);
                g2d.dispose();
            } else {
                g.drawImage(this, x, y, c);
            }
        }
    }
}