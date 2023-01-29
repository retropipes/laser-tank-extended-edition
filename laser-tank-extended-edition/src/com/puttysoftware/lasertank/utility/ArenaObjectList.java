/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.utility;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import com.puttysoftware.diane.asset.image.BufferedImageIcon;
import com.puttysoftware.diane.fileio.DataIOReader;
import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.arena.abc.AbstractArenaObject;
import com.puttysoftware.lasertank.arena.objects.*;
import com.puttysoftware.lasertank.asset.Images;
import com.puttysoftware.lasertank.helper.GameFormatHelper;
import com.puttysoftware.lasertank.index.GameFormat;
import com.puttysoftware.lasertank.locale.CommonString;
import com.puttysoftware.lasertank.locale.Strings;

public class ArenaObjectList {
    // Fields
    private static final AbstractArenaObject[] allObjects = { new UpperGroundEmpty(), new Empty(),
	    new UpperObjectsEmpty(), new Ground(), new TankMover(), new Ice(), new Water(), new ThinIce(), new Bridge(),
	    new Tank(1), new Tank(2), new Tank(3), new Tank(4), new Tank(5), new Tank(6), new Tank(7), new Tank(8),
	    new Tank(9), new Flag(), new Wall(), new AntiTank(), new DeadAntiTank(), new CrystalBlock(), new Bricks(),
	    new Tunnel(), new Mirror(), new RotaryMirror(), new Box(), new AntiTankMover(), new AnyMover(),
	    new TenMissiles(), new MagneticBox(), new MagneticMirror(), new MirrorCrystalBlock(), new TenStunners(),
	    new TenBoosts(), new TenMagnets(), new MagneticAttractWall(), new FrostField(), new StairsDown(),
	    new StairsUp(), new TenBlueLasers(), new IcyBox(), new WaterDoor(), new WaterKey(), new MagneticDoor(),
	    new MagneticKey(), new RedDoor(), new RedKey(), new ProximityCrystal(), new Crystal(), new RollingCrystal(),
	    new TenBombs(), new TenHeatBombs(), new TenIceBombs(), new IcyWall(), new HotWall(), new Lava(),
	    new HotBox(), new MetallicBricks(), new MetallicMirror(), new MetallicRotaryMirror(), new DeepWater(),
	    new DeeperWater(), new DeepestWater(), new IceBridge(), new PlasticBox(), new MetallicBox(),
	    new FireAllButton(), new FireAllButtonDoor(), new FirePressureButton(), new FirePressureButtonDoor(),
	    new FireTriggerButton(), new FireTriggerButtonDoor(), new IceAllButton(), new IceAllButtonDoor(),
	    new IcePressureButton(), new IcePressureButtonDoor(), new IceTriggerButton(), new IceTriggerButtonDoor(),
	    new MagneticAllButton(), new MagneticAllButtonDoor(), new MagneticPressureButton(),
	    new MagneticPressureButtonDoor(), new MagneticTriggerButton(), new MagneticTriggerButtonDoor(),
	    new MetallicAllButton(), new MetallicAllButtonDoor(), new MetallicPressureButton(),
	    new MetallicPressureButtonDoor(), new MetallicTriggerButton(), new MetallicTriggerButtonDoor(),
	    new PlasticAllButton(), new PlasticAllButtonDoor(), new PlasticPressureButton(),
	    new PlasticPressureButtonDoor(), new PlasticTriggerButton(), new PlasticTriggerButtonDoor(),
	    new StoneAllButton(), new StoneAllButtonDoor(), new StonePressureButton(), new StonePressureButtonDoor(),
	    new StoneTriggerButton(), new StoneTriggerButtonDoor(), new UniversalAllButton(),
	    new UniversalAllButtonDoor(), new UniversalPressureButton(), new UniversalPressureButtonDoor(),
	    new UniversalTriggerButton(), new UniversalTriggerButtonDoor(), new BoxMover(), new JumpBox(),
	    new ReverseJumpBox(), new MirrorMover(), new HotCrystalBlock(), new IcyCrystalBlock(), new Cracked(),
	    new Crumbling(), new Damaged(), new Weakened(), new Cloak(), new Darkness(), new PowerBolt(),
	    new RollingCrystalHorizontal(), new RollingCrystalVertical(), new KillerSkull(), new Bomb(), new Acid(),
	    new StrongAcid(), new StrongerAcid(), new StrongestAcid(), new AcidBridge(), new HotLava(),
	    new HotterLava(), new HottestLava(), new ToughBricks(), new TougherBricks(), new ToughestBricks(),
	    new ShadowCrystalBlock(), new MagneticRepelWall(), new HotBricks(), new IcyBricks(), new WildWall(),
	    new Bombable(), new Breakable(), new Fake(), new Invisible(), new Fading() };

    public static void enableAllObjects() {
	for (final AbstractArenaObject allObject : ArenaObjectList.allObjects) {
	    allObject.setEnabled(true);
	}
    }

    public static BufferedImageIcon[] getAllEditorAppearances() {
	final var allEditorAppearances = new BufferedImageIcon[ArenaObjectList.allObjects.length];
	for (var x = 0; x < allEditorAppearances.length; x++) {
	    allEditorAppearances[x] = Images.getImage(ArenaObjectList.allObjects[x], false);
	}
	return allEditorAppearances;
    }

    public static BufferedImageIcon[] getAllEditorAppearancesOnLayer(final int layer, final boolean useDisable) {
	if (useDisable) {
	    final var allEditorAppearancesOnLayer = new BufferedImageIcon[ArenaObjectList.allObjects.length];
	    for (var x = 0; x < ArenaObjectList.allObjects.length; x++) {
		if (ArenaObjectList.allObjects[x].getLayer() == layer) {
		    ArenaObjectList.allObjects[x].setEnabled(true);
		} else {
		    ArenaObjectList.allObjects[x].setEnabled(false);
		}
		allEditorAppearancesOnLayer[x] = Images.getImage(ArenaObjectList.allObjects[x], false);
	    }
	    return allEditorAppearancesOnLayer;
	}
	final var tempAllEditorAppearancesOnLayer = new BufferedImageIcon[ArenaObjectList.allObjects.length];
	var objectCount = 0;
	for (var x = 0; x < ArenaObjectList.allObjects.length; x++) {
	    if (ArenaObjectList.allObjects[x].getLayer() == layer) {
		tempAllEditorAppearancesOnLayer[x] = Images.getImage(ArenaObjectList.allObjects[x], false);
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

    public static AbstractArenaObject[] getAllObjectsOnLayer(final int layer, final boolean useDisable) {
	if (useDisable) {
	    for (final AbstractArenaObject allObject : ArenaObjectList.allObjects) {
		if (allObject.getLayer() == layer) {
		    allObject.setEnabled(true);
		} else {
		    allObject.setEnabled(false);
		}
	    }
	    return ArenaObjectList.allObjects;
	}
	final var tempAllObjectsOnLayer = new AbstractArenaObject[ArenaObjectList.allObjects.length];
	var objectCount = 0;
	for (var x = 0; x < ArenaObjectList.allObjects.length; x++) {
	    if (ArenaObjectList.allObjects[x].getLayer() == layer) {
		tempAllObjectsOnLayer[x] = ArenaObjectList.allObjects[x];
	    }
	}
	for (final AbstractArenaObject element : tempAllObjectsOnLayer) {
	    if (element != null) {
		objectCount++;
	    }
	}
	final var allObjectsOnLayer = new AbstractArenaObject[objectCount];
	objectCount = 0;
	for (final AbstractArenaObject element : tempAllObjectsOnLayer) {
	    if (element != null) {
		allObjectsOnLayer[objectCount] = element;
		objectCount++;
	    }
	}
	return allObjectsOnLayer;
    }

    public static boolean[] getObjectEnabledStatuses(final int layer) {
	final var allObjectEnabledStatuses = new boolean[ArenaObjectList.allObjects.length];
	for (var x = 0; x < ArenaObjectList.allObjects.length; x++) {
	    if (ArenaObjectList.allObjects[x].getLayer() == layer) {
		allObjectEnabledStatuses[x] = true;
	    } else {
		allObjectEnabledStatuses[x] = false;
	    }
	}
	return allObjectEnabledStatuses;
    }

    public static AbstractArenaObject readArenaObjectG2(final DataIOReader reader, final GameFormat formatVersion)
	    throws IOException {
	AbstractArenaObject o = null;
	var UID = Strings.loadCommon(CommonString.SPACE);
	if (!GameFormatHelper.isValidG1(formatVersion) && !GameFormatHelper.isValidG2(formatVersion)) {
	    return null;
	}
	UID = reader.readString();
	for (final AbstractArenaObject allObject : ArenaObjectList.allObjects) {
	    try {
		final AbstractArenaObject instance = allObject.getClass().getConstructor().newInstance();
		if (!GameFormatHelper.isValidG1(formatVersion) && !GameFormatHelper.isValidG2(formatVersion)) {
		    return null;
		}
		o = instance.readArenaObjectG2(reader, UID, formatVersion);
		if (o != null) {
		    return o;
		}
	    } catch (final InstantiationException | IllegalAccessException | IllegalArgumentException
		    | InvocationTargetException | NoSuchMethodException | SecurityException e) {
		LaserTankEE.logError(e);
	    }
	}
	return null;
    }

    public static AbstractArenaObject readArenaObjectG3(final DataIOReader reader, final GameFormat formatVersion)
	    throws IOException {
	AbstractArenaObject o = null;
	var UID = Strings.loadCommon(CommonString.SPACE);
	if (!GameFormatHelper.isValidG3(formatVersion)) {
	    return null;
	}
	UID = reader.readString();
	for (final AbstractArenaObject allObject : ArenaObjectList.allObjects) {
	    try {
		final AbstractArenaObject instance = allObject.getClass().getConstructor().newInstance();
		if (!GameFormatHelper.isValidG3(formatVersion)) {
		    return null;
		}
		o = instance.readArenaObjectG3(reader, UID, formatVersion);
		if (o != null) {
		    return o;
		}
	    } catch (final InstantiationException | IllegalAccessException | IllegalArgumentException
		    | InvocationTargetException | NoSuchMethodException | SecurityException e) {
		LaserTankEE.logError(e);
	    }
	}
	return null;
    }

    public static AbstractArenaObject readArenaObjectG4(final DataIOReader reader, final GameFormat formatVersion)
	    throws IOException {
	AbstractArenaObject o = null;
	var UID = Strings.loadCommon(CommonString.SPACE);
	if (!GameFormatHelper.isValidG4(formatVersion)) {
	    return null;
	}
	UID = reader.readString();
	for (final AbstractArenaObject allObject : ArenaObjectList.allObjects) {
	    try {
		final AbstractArenaObject instance = allObject.getClass().getConstructor().newInstance();
		if (!GameFormatHelper.isValidG4(formatVersion)) {
		    return null;
		}
		o = instance.readArenaObjectG4(reader, UID, formatVersion);
		if (o != null) {
		    return o;
		}
	    } catch (final InstantiationException | IllegalAccessException | IllegalArgumentException
		    | InvocationTargetException | NoSuchMethodException | SecurityException e) {
		LaserTankEE.logError(e);
	    }
	}
	return null;
    }

    public static AbstractArenaObject readArenaObjectG5(final DataIOReader reader, final GameFormat formatVersion)
	    throws IOException {
	AbstractArenaObject o = null;
	var UID = Strings.loadCommon(CommonString.SPACE);
	if (!GameFormatHelper.isValidG5(formatVersion)) {
	    return null;
	}
	UID = reader.readString();
	for (final AbstractArenaObject allObject : ArenaObjectList.allObjects) {
	    try {
		final AbstractArenaObject instance = allObject.getClass().getConstructor().newInstance();
		if (!GameFormatHelper.isValidG5(formatVersion)) {
		    return null;
		}
		o = instance.readArenaObjectG5(reader, UID, formatVersion);
		if (o != null) {
		    return o;
		}
	    } catch (final InstantiationException | IllegalAccessException | IllegalArgumentException
		    | InvocationTargetException | NoSuchMethodException | SecurityException e) {
		LaserTankEE.logError(e);
	    }
	}
	return null;
    }

    public static AbstractArenaObject readArenaObjectG6(final DataIOReader reader, final GameFormat formatVersion)
	    throws IOException {
	AbstractArenaObject o = null;
	var UID = Strings.loadCommon(CommonString.SPACE);
	if (!GameFormatHelper.isValidG6(formatVersion)) {
	    return null;
	}
	UID = reader.readString();
	for (final AbstractArenaObject allObject : ArenaObjectList.allObjects) {
	    try {
		final AbstractArenaObject instance = allObject.getClass().getConstructor().newInstance();
		if (!GameFormatHelper.isValidG6(formatVersion)) {
		    return null;
		}
		o = instance.readArenaObjectG6(reader, UID, formatVersion);
		if (o != null) {
		    return o;
		}
	    } catch (final InstantiationException | IllegalAccessException | IllegalArgumentException
		    | InvocationTargetException | NoSuchMethodException | SecurityException e) {
		LaserTankEE.logError(e);
	    }
	}
	return null;
    }
}
