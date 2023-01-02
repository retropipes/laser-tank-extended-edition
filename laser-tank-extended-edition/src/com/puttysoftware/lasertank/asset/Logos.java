/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.asset;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;

import com.puttysoftware.diane.asset.image.BufferedImageIcon;
import com.puttysoftware.lasertank.Application;
import com.puttysoftware.lasertank.locale.Strings;
import com.puttysoftware.lasertank.locale.global.GlobalStrings;
import com.puttysoftware.lasertank.locale.global.UntranslatedString;
import com.puttysoftware.lasertank.utility.InvalidArenaException;

public class Logos {
    private static final String DEFAULT_LOAD_PATH = "/locale/";
    private static Class<?> LOAD_CLASS = Logos.class;
    private static Font LOGO_DRAW_FONT = null;
    private static final String LOGO_DRAW_FONT_FALLBACK = "Times-BOLD-24";
    private static final int LOGO_FALLBACK_DRAW_HORZ = 100;
    private static final int LOGO_FALLBACK_DRAW_HORZ_MAX = 16;
    private static final int LOGO_FALLBACK_DRAW_HORZ_PCO = 8;
    private static final int LOGO_FALLBACK_DRAW_VERT = 146;
    private static final int LOGO_DRAW_HORZ = 156;
    private static final int LOGO_DRAW_HORZ_MAX = 2;
    private static final int LOGO_DRAW_HORZ_PCO = 2;
    private static final int LOGO_DRAW_VERT = 146;
    private static BufferedImageIcon openingCache, controlCache;

    public static BufferedImageIcon getOpening() {
	if (Logos.openingCache == null) {
	    Logos.openingCache = Logos.getLogo("opening.png", true);
	}
	return Logos.openingCache;
    }

    public static BufferedImageIcon getControl() {
	if (Logos.controlCache == null) {
	    Logos.controlCache = Logos.getLogo("control.png", false);
	}
	return Logos.controlCache;
    }

    public static void activeLanguageChanged() {
	// Invalidate caches
	Logos.openingCache = null;
	Logos.controlCache = null;
    }

    private static BufferedImageIcon getLogo(final String name, final boolean drawing) {
	try {
	    final URL url = Logos.LOAD_CLASS.getResource(Logos.DEFAULT_LOAD_PATH + Strings.getLanguageName() + name);
	    final BufferedImage image = ImageIO.read(url);
	    final Graphics2D g2 = image.createGraphics();
	    g2.setColor(Color.yellow);
	    final String logoVer = Application.getLogoVersionString();
	    if (drawing) {
		if (Logos.LOGO_DRAW_FONT == null) {
		    try (InputStream is = Logos.class
			    .getResourceAsStream(GlobalStrings.loadUntranslated(UntranslatedString.FONT_PATH)
				    + GlobalStrings.loadUntranslated(UntranslatedString.FONT_FILENAME))) {
			final Font baseFont = Font.createFont(Font.TRUETYPE_FONT, is);
			Logos.LOGO_DRAW_FONT = baseFont.deriveFont((float) 24);
			g2.setFont(Logos.LOGO_DRAW_FONT);
			g2.drawString(logoVer,
				Logos.LOGO_DRAW_HORZ
					+ (Logos.LOGO_DRAW_HORZ_MAX - logoVer.length()) * Logos.LOGO_DRAW_HORZ_PCO,
				Logos.LOGO_DRAW_VERT);
		    } catch (final Exception ex) {
			Logos.LOGO_DRAW_FONT = Font.decode(Logos.LOGO_DRAW_FONT_FALLBACK);
			g2.setFont(Logos.LOGO_DRAW_FONT);
			g2.drawString(logoVer,
				Logos.LOGO_FALLBACK_DRAW_HORZ + (Logos.LOGO_FALLBACK_DRAW_HORZ_MAX - logoVer.length())
					* Logos.LOGO_FALLBACK_DRAW_HORZ_PCO,
				Logos.LOGO_FALLBACK_DRAW_VERT);
		    }
		}
	    }
	    return new BufferedImageIcon(image);
	} catch (final IOException ioe) {
	    throw new InvalidArenaException(ioe);
	}
    }

    private Logos() {
	// Do nothing
    }
}
