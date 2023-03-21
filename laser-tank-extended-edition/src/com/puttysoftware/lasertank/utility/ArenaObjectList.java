/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.utility;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import com.puttysoftware.diane.asset.image.BufferedImageIcon;
import com.puttysoftware.diane.fileio.DataIOReader;
import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.arena.objects.*;
import com.puttysoftware.lasertank.asset.Images;
import com.puttysoftware.lasertank.helper.GameFormatHelper;
import com.puttysoftware.lasertank.index.GameFormat;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.locale.CommonString;
import com.puttysoftware.lasertank.locale.Strings;

public class ArenaObjectList {
	// Fields
	private static final ArenaObject[] allObjects = { new ArenaObject(GameObjectID.PLACEHOLDER), new ArenaObject(GameObjectID.PLACEHOLDER),
			new ArenaObject(GameObjectID.PLACEHOLDER), new ArenaObject(GameObjectID.GROUND), new ArenaObject(GameObjectID.TANK_MOVER), new ArenaObject(GameObjectID.ICE), new ArenaObject(GameObjectID.WATER), new ArenaObject(GameObjectID.THIN_ICE), new Bridge(),
			new ArenaObject(GameObjectID.TANK, 1), new ArenaObject(GameObjectID.FLAG), new ArenaObject(GameObjectID.WALL), new ArenaObject(GameObjectID.ANTI_TANK), new DeadAntiTank(), new ArenaObject(GameObjectID.CRYSTAL_BLOCK), new ArenaObject(GameObjectID.BRICKS),
			new ArenaObject(GameObjectID.TUNNEL), new ArenaObject(GameObjectID.MIRROR), new ArenaObject(GameObjectID.ROTARY_MIRROR), new ArenaObject(GameObjectID.BOX), new ArenaObject(GameObjectID.ANTI_TANK_MOVER), new ArenaObject(GameObjectID.ANY_MOVER),
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
			new UniversalTriggerButton(), new UniversalTriggerButtonDoor(), new ArenaObject(GameObjectID.BOX_MOVER), new JumpBox(),
			new ReverseJumpBox(), new ArenaObject(GameObjectID.MIRROR_MOVER), new HotCrystalBlock(), new IcyCrystalBlock(), new ArenaObject(GameObjectID.CRACKED),
			new ArenaObject(GameObjectID.CRUMBLING), new ArenaObject(GameObjectID.DAMAGED), new Weakened(), new Cloak(), new ArenaObject(GameObjectID.DARKNESS), new PowerBolt(),
			new RollingCrystalHorizontal(), new RollingCrystalVertical(), new KillerSkull(), new Bomb(), new Acid(),
			new StrongAcid(), new StrongerAcid(), new StrongestAcid(), new ArenaObject(GameObjectID.ACID_BRIDGE), new HotLava(),
			new HotterLava(), new HottestLava(), new ToughBricks(), new TougherBricks(), new ToughestBricks(),
			new ShadowCrystalBlock(), new MagneticRepelWall(), new HotBricks(), new IcyBricks(), new WildWall(),
			new ArenaObject(GameObjectID.BOMBABLE), new ArenaObject(GameObjectID.BREAKABLE), new ArenaObject(GameObjectID.FAKE), new ArenaObject(GameObjectID.INVISIBLE), new ArenaObject(GameObjectID.FADING), new OneWay(), new Box2(),
			new Box3(), new Box4(), new HotBox2(), new HotBox3(), new HotBox4(), new IcyBox2(), new IcyBox3(),
			new IcyBox4(), new MagneticBox2(), new MagneticBox3(), new MagneticBox4(), new MetallicBox2(),
			new MetallicBox3(), new MetallicBox4(), new PlasticBox2(), new PlasticBox3(), new PlasticBox4(),
			new BoxTeleport(), new InvisibleBoxTeleport(), new Tree(), new ArenaObject(GameObjectID.AXE), new Pit(), new Spring(),
			new SuperPit(), new SuperSpring(), new Sand(), new QuickSand(), new QuickerSand(), new QuickestSand(),
			new SandBridge() };

	public static void enableAllObjects() {
		for (final ArenaObject allObject : ArenaObjectList.allObjects) {
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

	public static ArenaObject[] getAllObjectsOnLayer(final int layer, final boolean useDisable) {
		if (useDisable) {
			for (final ArenaObject allObject : ArenaObjectList.allObjects) {
				if (allObject.getLayer() == layer) {
					allObject.setEnabled(true);
				} else {
					allObject.setEnabled(false);
				}
			}
			return ArenaObjectList.allObjects;
		}
		final var tempAllObjectsOnLayer = new ArenaObject[ArenaObjectList.allObjects.length];
		var objectCount = 0;
		for (var x = 0; x < ArenaObjectList.allObjects.length; x++) {
			if (ArenaObjectList.allObjects[x].getLayer() == layer) {
				tempAllObjectsOnLayer[x] = ArenaObjectList.allObjects[x];
			}
		}
		for (final ArenaObject element : tempAllObjectsOnLayer) {
			if (element != null) {
				objectCount++;
			}
		}
		final var allObjectsOnLayer = new ArenaObject[objectCount];
		objectCount = 0;
		for (final ArenaObject element : tempAllObjectsOnLayer) {
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

	public static ArenaObject readArenaObjectG2(final DataIOReader reader, final GameFormat formatVersion)
			throws IOException {
		ArenaObject o = null;
		var UID = Strings.loadCommon(CommonString.SPACE);
		if (!GameFormatHelper.isValidG1(formatVersion) && !GameFormatHelper.isValidG2(formatVersion)) {
			return null;
		}
		UID = reader.readString();
		for (final ArenaObject allObject : ArenaObjectList.allObjects) {
			try {
				final ArenaObject instance = allObject.getClass().getConstructor().newInstance();
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

	public static ArenaObject readArenaObjectG3(final DataIOReader reader, final GameFormat formatVersion)
			throws IOException {
		ArenaObject o = null;
		var UID = Strings.loadCommon(CommonString.SPACE);
		if (!GameFormatHelper.isValidG3(formatVersion)) {
			return null;
		}
		UID = reader.readString();
		for (final ArenaObject allObject : ArenaObjectList.allObjects) {
			try {
				final ArenaObject instance = allObject.getClass().getConstructor().newInstance();
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

	public static ArenaObject readArenaObjectG4(final DataIOReader reader, final GameFormat formatVersion)
			throws IOException {
		ArenaObject o = null;
		var UID = Strings.loadCommon(CommonString.SPACE);
		if (!GameFormatHelper.isValidG4(formatVersion)) {
			return null;
		}
		UID = reader.readString();
		for (final ArenaObject allObject : ArenaObjectList.allObjects) {
			try {
				final ArenaObject instance = allObject.getClass().getConstructor().newInstance();
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

	public static ArenaObject readArenaObjectG5(final DataIOReader reader, final GameFormat formatVersion)
			throws IOException {
		ArenaObject o = null;
		var UID = Strings.loadCommon(CommonString.SPACE);
		if (!GameFormatHelper.isValidG5(formatVersion)) {
			return null;
		}
		UID = reader.readString();
		for (final ArenaObject allObject : ArenaObjectList.allObjects) {
			try {
				final ArenaObject instance = allObject.getClass().getConstructor().newInstance();
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

	public static ArenaObject readArenaObjectG6(final DataIOReader reader, final GameFormat formatVersion)
			throws IOException {
		ArenaObject o = null;
		var UID = Strings.loadCommon(CommonString.SPACE);
		if (!GameFormatHelper.isValidG6(formatVersion)) {
			return null;
		}
		UID = reader.readString();
		for (final ArenaObject allObject : ArenaObjectList.allObjects) {
			try {
				final ArenaObject instance = allObject.getClass().getConstructor().newInstance();
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
