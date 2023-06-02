/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.assets;

import java.awt.Font;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.puttysoftware.lasertank.arena.objects.ArenaObject;
import com.puttysoftware.lasertank.datatype.FileExtensions;
import com.puttysoftware.lasertank.engine.asset.image.BufferedImageIcon;
import com.puttysoftware.lasertank.locale.global.GlobalStrings;
import com.puttysoftware.lasertank.locale.global.UntranslatedString;
import com.puttysoftware.lasertank.utility.InvalidArenaException;

public class Images {
    public static final int MAX_WINDOW_SIZE = 700;
    private static Class<?> LOAD_CLASS = Images.class;
    private static Font DRAW_FONT = null;
    private static String DRAW_FONT_FALLBACK;
    private static boolean stringsLoaded = false;
    private static final int DRAW_HORZ = 10;
    private static final int DRAW_VERT = 22;
    private static final float DRAW_SIZE = 14;

    public static void activeLanguageChanged() {
	ImageCache.flushCache();
    }

    private static void checkLoadStrings() {
	if (!Images.stringsLoaded) {
	    Images.DRAW_FONT_FALLBACK = GlobalStrings.loadUntranslated(UntranslatedString.VERSION_FONT_FALLBACK);
	    Images.stringsLoaded = true;
	}
    }

    public static BufferedImageIcon getCompositeImage(final ArenaObject obj1, final ArenaObject obj2,
	    final boolean useText) {
	final var icon1 = Images.getImage(obj1, useText);
	final var icon2 = Images.getImage(obj2, useText);
	return Images.getCompositeImageDirectly(icon1, icon2);
    }

    static BufferedImageIcon getCompositeImageDirectly(final BufferedImageIcon icon1, final BufferedImageIcon icon2) {
	try {
	    final var result = new BufferedImageIcon(icon1);
	    if (icon1 != null && icon2 != null) {
		final var g2 = result.createGraphics();
		g2.drawImage(icon2, 0, 0, null);
		return result;
	    }
	    return null;
	} catch (final NullPointerException | IllegalArgumentException ia) {
	    return null;
	}
    }

    public static int getGraphicSize() {
	return 64;
    }

    public static BufferedImageIcon getImage(final ArenaObject obj, final boolean useText) {
	return ImageCache.getCachedImage(obj, useText);
    }

    static BufferedImageIcon getUncachedImage(final ArenaObject obj, final boolean useText) {
	Images.checkLoadStrings();
	try {
	    final var normalName = obj.getImageName();
	    final var path = GlobalStrings.loadUntranslated(UntranslatedString.OBJECTS_PATH) + normalName
		    + FileExtensions.getImageExtensionWithPeriod();
	    final var url = Images.LOAD_CLASS.getResource(path);
	    final var image = ImageIO.read(url);
	    final var customText = obj.getCustomText();
	    if (useText && customText != null) {
		if (Images.DRAW_FONT == null) {
		    try (var is = Images.class
			    .getResourceAsStream(GlobalStrings.loadUntranslated(UntranslatedString.FONT_PATH)
				    + GlobalStrings.loadUntranslated(UntranslatedString.FONT_FILENAME))) {
			final var baseFont = Font.createFont(Font.TRUETYPE_FONT, is);
			Images.DRAW_FONT = baseFont.deriveFont(Images.DRAW_SIZE);
		    } catch (final Exception ex) {
			Images.DRAW_FONT = Font.decode(Images.DRAW_FONT_FALLBACK);
		    }
		}
		final var g2 = image.createGraphics();
		g2.setFont(Images.DRAW_FONT);
		g2.setColor(obj.getCustomTextColor());
		g2.drawString(customText, Images.DRAW_HORZ, Images.DRAW_VERT);
	    }
	    return new BufferedImageIcon(image);
	} catch (final IOException ioe) {
	    throw new InvalidArenaException(ioe);
	}
    }

    public static BufferedImageIcon getVirtualCompositeImage(final ArenaObject obj1, final ArenaObject obj2,
	    final ArenaObject... otherObjs) {
	var result = Images.getCompositeImage(obj1, obj2, true);
	for (final ArenaObject otherObj : otherObjs) {
	    final var img = Images.getImage(otherObj, true);
	    result = Images.getCompositeImageDirectly(result, img);
	}
	return result;
    }

    private Images() {
	// Do nothing
    }
}
