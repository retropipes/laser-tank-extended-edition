/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.assets;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.puttysoftware.diane.asset.image.BufferedImageIcon;
import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.locale.Strings;
import com.puttysoftware.lasertank.locale.global.GlobalStrings;
import com.puttysoftware.lasertank.locale.global.UntranslatedString;
import com.puttysoftware.lasertank.utility.InvalidArenaException;

public class Logos {
	private static String DEFAULT_LOAD_PATH;
	private static Class<?> LOAD_CLASS = Logos.class;
	private static Font LOGO_DRAW_FONT = null;
	private static String LOGO_DRAW_FONT_FALLBACK;
	private static String LOGO_OPENING;
	private static String LOGO_CONTROL;
	private static boolean stringsLoaded = false;
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
		Logos.checkLoadStrings();
		if (Logos.openingCache == null) {
			Logos.openingCache = Logos.getLogo(Logos.LOGO_OPENING, true);
		}
		return Logos.openingCache;
	}

	public static BufferedImageIcon getControl() {
		Logos.checkLoadStrings();
		if (Logos.controlCache == null) {
			Logos.controlCache = Logos.getLogo(Logos.LOGO_CONTROL, false);
		}
		return Logos.controlCache;
	}

	public static void activeLanguageChanged() {
		Logos.checkLoadStrings();
		// Invalidate caches
		Logos.openingCache = null;
		Logos.controlCache = null;
	}

	private static BufferedImageIcon getLogo(final String name, final boolean drawing) {
		Logos.checkLoadStrings();
		try {
			final var url = Logos.LOAD_CLASS.getResource(Logos.DEFAULT_LOAD_PATH + Strings.getLanguageName() + name);
			final var image = ImageIO.read(url);
			final var g2 = image.createGraphics();
			g2.setColor(Color.yellow);
			final var logoVer = LaserTankEE.getLogoVersionString();
			if (drawing && Logos.LOGO_DRAW_FONT == null) {
				try (var is = Logos.class
						.getResourceAsStream(GlobalStrings.loadUntranslated(UntranslatedString.FONT_PATH)
								+ GlobalStrings.loadUntranslated(UntranslatedString.FONT_FILENAME))) {
					final var baseFont = Font.createFont(Font.TRUETYPE_FONT, is);
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
			return new BufferedImageIcon(image);
		} catch (final IOException ioe) {
			throw new InvalidArenaException(ioe);
		}
	}

	private static void checkLoadStrings() {
		if (!Logos.stringsLoaded) {
			Logos.DEFAULT_LOAD_PATH = GlobalStrings.loadUntranslated(UntranslatedString.LOGO_PATH);
			Logos.LOGO_DRAW_FONT_FALLBACK = GlobalStrings.loadUntranslated(UntranslatedString.DRAW_FONT_FALLBACK);
			Logos.LOGO_CONTROL = GlobalStrings.loadUntranslated(UntranslatedString.LOGO_CONTROL);
			Logos.LOGO_OPENING = GlobalStrings.loadUntranslated(UntranslatedString.LOGO_OPENING);
			Logos.stringsLoaded = true;
		}
	}

	private Logos() {
		// Do nothing
	}
}
