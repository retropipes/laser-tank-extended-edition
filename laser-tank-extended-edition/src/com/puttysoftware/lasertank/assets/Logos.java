/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.assets;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.engine.asset.image.BufferedImageIcon;
import com.puttysoftware.lasertank.locale.Strings;
import com.puttysoftware.lasertank.locale.global.GlobalStrings;
import com.puttysoftware.lasertank.locale.global.UntranslatedString;
import com.puttysoftware.lasertank.utility.InvalidArenaException;

public class Logos {
    private static String DEFAULT_LOAD_PATH;
    private static Class<?> LOAD_CLASS = Logos.class;
    private static Font LOGO_VERSION_FONT = null;
    private static Font LOGO_EDITION_FONT = null;
    private static String LOGO_VERSION_FONT_FALLBACK;
    private static String LOGO_EDITION_FONT_FALLBACK;
    private static String LOGO_OPENING;
    private static String LOGO_CONTROL;
    private static String LOGO_EDITION;
    private static boolean stringsLoaded = false;
    private static final int LOGO_FALLBACK_VERSION_HORZ = 100;
    private static final int LOGO_FALLBACK_VERSION_HORZ_MAX = 16;
    private static final int LOGO_FALLBACK_VERSION_HORZ_PCO = 8;
    private static final int LOGO_FALLBACK_VERSION_VERT = 140;
    private static final int LOGO_VERSION_HORZ = 156;
    private static final int LOGO_VERSION_HORZ_MAX = 2;
    private static final int LOGO_VERSION_HORZ_PCO = 2;
    private static final int LOGO_VERSION_VERT = 140;
    private static final int LOGO_FALLBACK_EDITION_HORZ = 166;
    private static final int LOGO_FALLBACK_EDITION_HORZ_MAX = 8;
    private static final int LOGO_FALLBACK_EDITION_HORZ_PCO = 4;
    private static final int LOGO_FALLBACK_EDITION_VERT = 155;
    private static final int LOGO_EDITION_HORZ = 120;
    private static final int LOGO_EDITION_HORZ_MAX = 2;
    private static final int LOGO_EDITION_HORZ_PCO = 2;
    private static final int LOGO_EDITION_VERT = 155;
    private static BufferedImageIcon openingCache, controlCache;

    public static void activeLanguageChanged() {
	Logos.checkLoadStrings();
	// Invalidate caches
	Logos.openingCache = null;
	Logos.controlCache = null;
    }

    private static void checkLoadStrings() {
	if (!Logos.stringsLoaded) {
	    Logos.DEFAULT_LOAD_PATH = GlobalStrings.loadUntranslated(UntranslatedString.LOGO_PATH);
	    Logos.LOGO_VERSION_FONT_FALLBACK = GlobalStrings.loadUntranslated(UntranslatedString.VERSION_FONT_FALLBACK);
	    Logos.LOGO_EDITION_FONT_FALLBACK = GlobalStrings.loadUntranslated(UntranslatedString.EDITION_FONT_FALLBACK);
	    Logos.LOGO_EDITION = GlobalStrings.loadUntranslated(UntranslatedString.EDITION_NAME);
	    Logos.LOGO_CONTROL = GlobalStrings.loadUntranslated(UntranslatedString.LOGO_CONTROL);
	    Logos.LOGO_OPENING = GlobalStrings.loadUntranslated(UntranslatedString.LOGO_OPENING);
	    Logos.stringsLoaded = true;
	}
    }

    public static BufferedImageIcon getControl() {
	Logos.checkLoadStrings();
	if (Logos.controlCache == null) {
	    Logos.controlCache = Logos.getLogo(Logos.LOGO_CONTROL, false);
	}
	return Logos.controlCache;
    }

    private static BufferedImageIcon getLogo(final String name, final boolean drawing) {
	Logos.checkLoadStrings();
	try {
	    final var url = Logos.LOAD_CLASS.getResource(Logos.DEFAULT_LOAD_PATH + Strings.getLanguageName() + name);
	    final var image = ImageIO.read(url);
	    final var g2 = image.createGraphics();
	    // Add text
	    g2.setColor(Color.yellow);
	    final var logoVer = LaserTankEE.getLogoVersionString();
	    if (drawing && (Logos.LOGO_VERSION_FONT == null || Logos.LOGO_EDITION_FONT == null)) {
		try (var is = Logos.class
			.getResourceAsStream(GlobalStrings.loadUntranslated(UntranslatedString.FONT_PATH)
				+ GlobalStrings.loadUntranslated(UntranslatedString.FONT_FILENAME))) {
		    final var baseFont = Font.createFont(Font.TRUETYPE_FONT, is);
		    Logos.LOGO_VERSION_FONT = baseFont.deriveFont((float) 24);
		    g2.setFont(Logos.LOGO_VERSION_FONT);
		    g2.drawString(logoVer,
			    Logos.LOGO_VERSION_HORZ
				    + (Logos.LOGO_VERSION_HORZ_MAX - logoVer.length()) * Logos.LOGO_VERSION_HORZ_PCO,
			    Logos.LOGO_VERSION_VERT);
		    Logos.LOGO_EDITION_FONT = baseFont.deriveFont((float) 10);
		    g2.setFont(Logos.LOGO_EDITION_FONT);
		    g2.drawString(Logos.LOGO_EDITION,
			    Logos.LOGO_EDITION_HORZ
				    + (Logos.LOGO_EDITION_HORZ_MAX - logoVer.length()) * Logos.LOGO_EDITION_HORZ_PCO,
			    Logos.LOGO_EDITION_VERT);
		} catch (final Exception ex) {
		    Logos.LOGO_VERSION_FONT = Font.decode(Logos.LOGO_VERSION_FONT_FALLBACK);
		    g2.setFont(Logos.LOGO_VERSION_FONT);
		    g2.drawString(logoVer,
			    Logos.LOGO_FALLBACK_VERSION_HORZ + (Logos.LOGO_FALLBACK_VERSION_HORZ_MAX - logoVer.length())
				    * Logos.LOGO_FALLBACK_VERSION_HORZ_PCO,
			    Logos.LOGO_FALLBACK_VERSION_VERT);
		    Logos.LOGO_EDITION_FONT = Font.decode(Logos.LOGO_EDITION_FONT_FALLBACK);
		    g2.setFont(Logos.LOGO_EDITION_FONT);
		    g2.drawString(Logos.LOGO_EDITION,
			    Logos.LOGO_FALLBACK_EDITION_HORZ + (Logos.LOGO_FALLBACK_EDITION_HORZ_MAX - logoVer.length())
				    * Logos.LOGO_FALLBACK_EDITION_HORZ_PCO,
			    Logos.LOGO_FALLBACK_EDITION_VERT);
		}
	    }
	    // Add objects
	    return Images.getCompositeImageDirectly(new BufferedImageIcon(image),
		    new BufferedImageIcon(ImageIO.read(Logos.LOAD_CLASS.getResource(Logos.DEFAULT_LOAD_PATH
			    + GlobalStrings.loadUntranslated(UntranslatedString.OBJECT_MASK_IMAGE)))));
	} catch (final IOException ioe) {
	    throw new InvalidArenaException(ioe);
	}
    }

    public static BufferedImageIcon getOpening() {
	Logos.checkLoadStrings();
	if (Logos.openingCache == null) {
	    Logos.openingCache = Logos.getLogo(Logos.LOGO_OPENING, true);
	}
	return Logos.openingCache;
    }

    private Logos() {
	// Do nothing
    }
}
