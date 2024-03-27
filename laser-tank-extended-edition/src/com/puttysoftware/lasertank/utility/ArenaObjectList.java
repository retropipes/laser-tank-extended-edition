/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.utility;

import java.io.IOException;

import com.puttysoftware.lasertank.arena.objects.ArenaObject;
import com.puttysoftware.lasertank.asset.Images;
import com.puttysoftware.lasertank.asset.image.BufferedImageIcon;
import com.puttysoftware.lasertank.fileio.DataIOReader;
import com.puttysoftware.lasertank.helper.GameFormatHelper;
import com.puttysoftware.lasertank.index.GameFormat;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.locale.CommonString;
import com.puttysoftware.lasertank.locale.Strings;

public class ArenaObjectList {
    // Fields
    private static final int COUNT = 172;

    private static ArenaObject createObject(final int x) {
	return new ArenaObject(GameObjectID.values()[x]);
    }

    public static BufferedImageIcon[] getAllEditorAppearances() {
	final var allEditorAppearances = new BufferedImageIcon[ArenaObjectList.COUNT];
	for (var x = 0; x < allEditorAppearances.length; x++) {
	    allEditorAppearances[x] = Images.getImage(ArenaObjectList.createObject(x), false);
	}
	return allEditorAppearances;
    }

    public static BufferedImageIcon[] getAllEditorAppearancesOnLayer(final int layer, final boolean useDisable) {
	if (useDisable) {
	    final var allEditorAppearancesOnLayer = new BufferedImageIcon[ArenaObjectList.COUNT];
	    for (var x = 0; x < ArenaObjectList.COUNT; x++) {
		if (ArenaObjectList.createObject(x).getLayer() == layer) {
		    ArenaObjectList.createObject(x).setEnabled(true);
		} else {
		    ArenaObjectList.createObject(x).setEnabled(false);
		}
		allEditorAppearancesOnLayer[x] = Images.getImage(ArenaObjectList.createObject(x), false);
	    }
	    return allEditorAppearancesOnLayer;
	}
	final var tempAllEditorAppearancesOnLayer = new BufferedImageIcon[ArenaObjectList.COUNT];
	var objectCount = 0;
	for (var x = 0; x < ArenaObjectList.COUNT; x++) {
	    if (ArenaObjectList.createObject(x).getLayer() == layer) {
		tempAllEditorAppearancesOnLayer[x] = Images.getImage(ArenaObjectList.createObject(x), false);
	    }
	}
	for (final BufferedImageIcon element : tempAllEditorAppearancesOnLayer) {
	    if (element != null) {
		objectCount++;
	    }
	}
	final var allEditorAppearancesOnLayer = new BufferedImageIcon[objectCount];
	objectCount = 0;
	for (final BufferedImageIcon element : tempAllEditorAppearancesOnLayer) {
	    if (element != null) {
		allEditorAppearancesOnLayer[objectCount] = element;
		objectCount++;
	    }
	}
	return allEditorAppearancesOnLayer;
    }

    public static ArenaObject[] getAllObjectsOnLayer(final int layer, final boolean useDisable) {
	final var tempAllObjectsOnLayer = new ArenaObject[ArenaObjectList.COUNT];
	var objectCount = 0;
	for (var x = 0; x < ArenaObjectList.COUNT; x++) {
	    if (ArenaObjectList.createObject(x).getLayer() == layer) {
		tempAllObjectsOnLayer[x] = ArenaObjectList.createObject(x);
		if (useDisable) {
		    if (tempAllObjectsOnLayer[x].getLayer() == layer) {
			tempAllObjectsOnLayer[x].setEnabled(true);
		    } else {
			tempAllObjectsOnLayer[x].setEnabled(false);
		    }
		}
	    }
	}
	for (final ArenaObject element : tempAllObjectsOnLayer) {
	    if (element != null) {
		objectCount++;
	    }
	}
	final var objectsOnLayer = new ArenaObject[objectCount];
	objectCount = 0;
	for (final ArenaObject element : tempAllObjectsOnLayer) {
	    if (element != null) {
		objectsOnLayer[objectCount] = element;
		objectCount++;
	    }
	}
	return objectsOnLayer;
    }

    public static boolean[] getObjectEnabledStatuses(final int layer) {
	final var allObjectEnabledStatuses = new boolean[ArenaObjectList.COUNT];
	for (var x = 0; x < ArenaObjectList.COUNT; x++) {
	    if (ArenaObjectList.createObject(x).getLayer() == layer) {
		allObjectEnabledStatuses[x] = true;
	    } else {
		allObjectEnabledStatuses[x] = false;
	    }
	}
	return allObjectEnabledStatuses;
    }

    public static ArenaObject readArenaObjectG2(final DataIOReader reader, final GameFormat formatVersion)
	    throws IOException {
	var UID = Strings.loadCommon(CommonString.SPACE);
	if (!GameFormatHelper.isValidG1(formatVersion) && !GameFormatHelper.isValidG2(formatVersion)) {
	    return null;
	}
	UID = reader.readString();
	final var instance = new ArenaObject();
	if (!GameFormatHelper.isValidG1(formatVersion) && !GameFormatHelper.isValidG2(formatVersion)) {
	    return null;
	}
	return instance.readArenaObjectG2(reader, UID, formatVersion);
    }

    public static ArenaObject readArenaObjectG3(final DataIOReader reader, final GameFormat formatVersion)
	    throws IOException {
	var UID = Strings.loadCommon(CommonString.SPACE);
	if (!GameFormatHelper.isValidG3(formatVersion)) {
	    return null;
	}
	UID = reader.readString();
	final var instance = new ArenaObject();
	if (!GameFormatHelper.isValidG3(formatVersion)) {
	    return null;
	}
	return instance.readArenaObjectG3(reader, UID, formatVersion);
    }

    public static ArenaObject readArenaObjectG4(final DataIOReader reader, final GameFormat formatVersion)
	    throws IOException {
	var UID = Strings.loadCommon(CommonString.SPACE);
	if (!GameFormatHelper.isValidG4(formatVersion)) {
	    return null;
	}
	UID = reader.readString();
	final var instance = new ArenaObject();
	if (!GameFormatHelper.isValidG4(formatVersion)) {
	    return null;
	}
	return instance.readArenaObjectG4(reader, UID, formatVersion);
    }

    public static ArenaObject readArenaObjectG5(final DataIOReader reader, final GameFormat formatVersion)
	    throws IOException {
	var UID = Strings.loadCommon(CommonString.SPACE);
	if (!GameFormatHelper.isValidG5(formatVersion)) {
	    return null;
	}
	UID = reader.readString();
	final var instance = new ArenaObject();
	if (!GameFormatHelper.isValidG5(formatVersion)) {
	    return null;
	}
	return instance.readArenaObjectG5(reader, UID, formatVersion);
    }

    public static ArenaObject readArenaObjectG6(final DataIOReader reader, final GameFormat formatVersion)
	    throws IOException {
	var UID = Strings.loadCommon(CommonString.SPACE);
	if (!GameFormatHelper.isValidG6(formatVersion)) {
	    return null;
	}
	UID = reader.readString();
	final var instance = new ArenaObject();
	if (!GameFormatHelper.isValidG6(formatVersion)) {
	    return null;
	}
	return instance.readArenaObjectG6(reader, UID, formatVersion);
    }
}
