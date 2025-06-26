package com.puttysoftware.lasertank.arena.objects;

import java.util.ResourceBundle;

import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.helper.DirectionHelper;
import com.puttysoftware.lasertank.helper.GameActionHelper;
import com.puttysoftware.lasertank.helper.GameColorHelper;
import com.puttysoftware.lasertank.helper.MaterialHelper;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameAction;
import com.puttysoftware.lasertank.index.GameColor;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.Material;
import com.puttysoftware.lasertank.locale.global.DataLoaderString;
import com.puttysoftware.lasertank.locale.global.GlobalStrings;

class ArenaObjectDataLoader {
    private static final int GameObjectID_DONT_CHANGE = -1;

    private static ResourceBundle load(final ArenaObjectDataFile file) {
	return ResourceBundle.getBundle(GlobalStrings.loadDataLoader(DataLoaderString.LOAD_PATH) + file.getName());
    }

    public static boolean loadAcceptFire(final GameObjectID objID) {
	final var data = ArenaObjectDataLoader.load(ArenaObjectDataFile.ACCEPT_FIRE);
	final var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return false;
	}
	final var value = data.getString(key);
	if (value == GlobalStrings.loadDataLoader(DataLoaderString.ANY)) {
	    return true;
	}
	if (value == GlobalStrings.loadDataLoader(DataLoaderString.NONE)
		|| value != GlobalStrings.loadDataLoader(DataLoaderString.VALUE_DIRECTION)) {
	    return false;
	}
	return data.getString(key) == GlobalStrings.loadDataLoader(DataLoaderString.ANY);
    }

    public static boolean loadAcceptIce(final GameObjectID objID) {
	final var data = ArenaObjectDataLoader.load(ArenaObjectDataFile.ACCEPT_ICE);
	final var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return false;
	}
	final var value = data.getString(key);
	if (value == GlobalStrings.loadDataLoader(DataLoaderString.ANY)) {
	    return true;
	}
	if (value == GlobalStrings.loadDataLoader(DataLoaderString.NONE)
		|| value != GlobalStrings.loadDataLoader(DataLoaderString.VALUE_DIRECTION)) {
	    return false;
	}
	return data.getString(key) == GlobalStrings.loadDataLoader(DataLoaderString.ANY);
    }

    public static boolean loadAcceptTick(final GameObjectID objID, final GameAction action) {
	final var data = ArenaObjectDataLoader.load(ArenaObjectDataFile.ACCEPT_TICK);
	final var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return false;
	}
	final var value = data.getString(key);
	if (value == GlobalStrings.loadDataLoader(DataLoaderString.ANY)) {
	    return true;
	}
	return action == GameActionHelper.fromStringValue(value);
    }

    public static GameObjectID loadAttributeRender(final GameObjectID objID) {
	final var data = ArenaObjectDataLoader.load(ArenaObjectDataFile.ATTRIBUTE_RENDER);
	final var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return objID;
	}
	final var value = data.getString(key);
	if (value == GlobalStrings.loadDataLoader(DataLoaderString.NONE)) {
	    return objID;
	}
	var index = Integer.parseInt(value);
	if (index == ArenaObjectDataLoader.GameObjectID_DONT_CHANGE) {
	    return objID;
	}
	return GameObjectID.values()[index];
    }

    public static boolean loadBox(final GameObjectID objID) {
	final var data = ArenaObjectDataLoader.load(ArenaObjectDataFile.BOX);
	final var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return false;
	}
	return data.getString(key) == GlobalStrings.loadDataLoader(DataLoaderString.ANY);
    }

    public static boolean loadCloak(final GameObjectID objID) {
	final var data = ArenaObjectDataLoader.load(ArenaObjectDataFile.CLOAK);
	final var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return false;
	}
	return data.getString(key) == GlobalStrings.loadDataLoader(DataLoaderString.ANY);
    }

    public static boolean loadControl(final GameObjectID objID) {
	final var data = ArenaObjectDataLoader.load(ArenaObjectDataFile.CONTROL);
	final var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return false;
	}
	return data.getString(key) == GlobalStrings.loadDataLoader(DataLoaderString.ANY);
    }

    public static GameColor[] loadColor(final GameObjectID objID) {
	final var fallback = new GameColor[] { GameColor.NONE };
	final var all = GameColor.values();
	final var data = ArenaObjectDataLoader.load(ArenaObjectDataFile.COLOR);
	final var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return fallback;
	}
	final var value = data.getString(key);
	if (value == GlobalStrings.loadDataLoader(DataLoaderString.ANY)) {
	    return all;
	}
	if (!value.contains(GlobalStrings.loadDataLoader(DataLoaderString.VALUE_SEPARATOR))) {
	    return new GameColor[] { GameColorHelper.fromStringValue(value) };
	}
	final var split = value.split(GlobalStrings.loadDataLoader(DataLoaderString.VALUE_SEPARATOR));
	final var res = new GameColor[split.length];
	for (var r = 0; r < res.length; r++) {
	    res[r] = GameColorHelper.fromStringValue(split[r]);
	}
	return res;
    }

    public static Direction[] loadDirection(final GameObjectID objID) {
	final var fallback = new Direction[] { Direction.NONE };
	final var all = Direction.values();
	final var data = ArenaObjectDataLoader.load(ArenaObjectDataFile.DIRECTION);
	final var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return fallback;
	}
	final var value = data.getString(key);
	if (value == GlobalStrings.loadDataLoader(DataLoaderString.ANY)) {
	    return all;
	}
	if (!value.contains(GlobalStrings.loadDataLoader(DataLoaderString.VALUE_SEPARATOR))) {
	    return new Direction[] { DirectionHelper.fromStringValue(value) };
	}
	final var split = value.split(GlobalStrings.loadDataLoader(DataLoaderString.VALUE_SEPARATOR));
	final var res = new Direction[split.length];
	for (var r = 0; r < res.length; r++) {
	    res[r] = DirectionHelper.fromStringValue(split[r]);
	}
	return res;
    }

    public static int loadFrame(final GameObjectID objID) {
	final var data = ArenaObjectDataLoader.load(ArenaObjectDataFile.FRAME);
	final var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return 0;
	}
	final var value = data.getString(key);
	if (value == GlobalStrings.loadDataLoader(DataLoaderString.NONE)) {
	    return 0;
	}
	return Integer.parseInt(value);
    }

    public static boolean loadFriction(final GameObjectID objID, final Direction dir) {
	final var data = ArenaObjectDataLoader.load(ArenaObjectDataFile.FRICTION);
	final var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return false;
	}
	final var value = data.getString(key);
	if (value == GlobalStrings.loadDataLoader(DataLoaderString.ANY)) {
	    return true;
	}
	if (value == GlobalStrings.loadDataLoader(DataLoaderString.NONE)
		|| value != GlobalStrings.loadDataLoader(DataLoaderString.VALUE_DIRECTION)) {
	    return false;
	}
	final var subkey = key + GlobalStrings.loadDataLoader(DataLoaderString.DIRECTION_SEPARATOR) + dir.ordinal();
	if (!data.containsKey(subkey)) {
	    return false;
	}
	return data.getString(subkey) == GlobalStrings.loadDataLoader(DataLoaderString.ANY);
    }

    public static int loadHeight(final GameObjectID objID) {
	final var data = ArenaObjectDataLoader.load(ArenaObjectDataFile.LAYER);
	final var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return 0;
	}
	final var value = data.getString(key);
	if (value == GlobalStrings.loadDataLoader(DataLoaderString.NONE)) {
	    return 0;
	}
	return Integer.parseInt(value);
    }

    public static boolean loadHostile(final GameObjectID objID) {
	final var data = ArenaObjectDataLoader.load(ArenaObjectDataFile.CONTROL);
	final var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return false;
	}
	return data.getString(key) == GlobalStrings.loadDataLoader(DataLoaderString.ANY);
    }

    public static int[] loadIndex(final GameObjectID objID) {
	final var fallback = new int[] { 0 };
	final var data = ArenaObjectDataLoader.load(ArenaObjectDataFile.INDEX);
	final var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return fallback;
	}
	final var value = data.getString(key);
	if (value == GlobalStrings.loadDataLoader(DataLoaderString.ANY)) {
	    return fallback;
	}
	if (!value.contains(GlobalStrings.loadDataLoader(DataLoaderString.VALUE_SEPARATOR))) {
	    return new int[] { Integer.parseInt(value) };
	}
	final var split = value.split(GlobalStrings.loadDataLoader(DataLoaderString.VALUE_SEPARATOR));
	final var res = new int[split.length];
	for (var r = 0; r < res.length; r++) {
	    res[r] = Integer.parseInt(split[r]);
	}
	return res;
    }

    public static boolean loadJump(final GameObjectID objID) {
	final var data = ArenaObjectDataLoader.load(ArenaObjectDataFile.JUMP);
	final var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return false;
	}
	return data.getString(key) == GlobalStrings.loadDataLoader(DataLoaderString.ANY);
    }

    public static boolean loadLaserPassthru(final GameObjectID objID) {
	final var data = ArenaObjectDataLoader.load(ArenaObjectDataFile.LASER_PASSTHRU);
	final var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return false;
	}
	return data.getString(key) == GlobalStrings.loadDataLoader(DataLoaderString.ANY);
    }

    public static int loadLayer(final GameObjectID objID) {
	final var data = ArenaObjectDataLoader.load(ArenaObjectDataFile.LAYER);
	final var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return 0;
	}
	final var value = data.getString(key);
	if (value == GlobalStrings.loadDataLoader(DataLoaderString.NONE)) {
	    return 0;
	}
	return Integer.parseInt(value);
    }

    public static Sound loadLaserEnterSound(final GameObjectID objID) {
	final var data = ArenaObjectDataLoader.load(ArenaObjectDataFile.LASER_ENTER_SOUND);
	final var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return Sound._NONE;
	}
	var value = data.getString(key);
	var index = Integer.parseInt(value);
	return Sound.values()[index];
    }

    public static boolean loadLethal(final GameObjectID objID, final int index) {
	final var data = ArenaObjectDataLoader.load(ArenaObjectDataFile.LETHAL);
	final var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return false;
	}
	final var value = data.getString(key);
	if (value == GlobalStrings.loadDataLoader(DataLoaderString.ANY)) {
	    return true;
	}
	if (value == GlobalStrings.loadDataLoader(DataLoaderString.NONE)
		|| value != GlobalStrings.loadDataLoader(DataLoaderString.VALUE_DIRECTION)) {
	    return false;
	}
	final var subkey = key + GlobalStrings.loadDataLoader(DataLoaderString.INDEX_SEPARATOR) + index;
	if (!data.containsKey(subkey)) {
	    return false;
	}
	return data.getString(key) == GlobalStrings.loadDataLoader(DataLoaderString.ANY);
    }

    public static Material loadMaterial(final GameObjectID objID) {
	final var data = ArenaObjectDataLoader.load(ArenaObjectDataFile.MATERIAL);
	final var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return Material.NONE;
	}
	final var value = data.getString(key);
	if (value == GlobalStrings.loadDataLoader(DataLoaderString.ANY)) {
	    return Material.NONE;
	}
	return MaterialHelper.fromStringValue(value);
    }

    public static boolean loadMovable(final GameObjectID objID, final Direction dir) {
	final var data = ArenaObjectDataLoader.load(ArenaObjectDataFile.MOVABLE);
	final var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return false;
	}
	final var value = data.getString(key);
	if (value == GlobalStrings.loadDataLoader(DataLoaderString.ANY)) {
	    return true;
	}
	if (value == GlobalStrings.loadDataLoader(DataLoaderString.NONE)
		|| value != GlobalStrings.loadDataLoader(DataLoaderString.VALUE_DIRECTION)) {
	    return false;
	}
	final var subkey = key + GlobalStrings.loadDataLoader(DataLoaderString.DIRECTION_SEPARATOR) + dir.ordinal();
	if (!data.containsKey(subkey)) {
	    return false;
	}
	return data.getString(subkey) == GlobalStrings.loadDataLoader(DataLoaderString.ANY);
    }

    public static boolean loadMovableMirror(final GameObjectID objID, final Direction dir) {
	final var data = ArenaObjectDataLoader.load(ArenaObjectDataFile.MOVABLE_MIRROR);
	final var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return false;
	}
	final var value = data.getString(key);
	if (value == GlobalStrings.loadDataLoader(DataLoaderString.ANY)) {
	    return true;
	}
	if (value == GlobalStrings.loadDataLoader(DataLoaderString.NONE)
		|| value != GlobalStrings.loadDataLoader(DataLoaderString.VALUE_DIRECTION)) {
	    return false;
	}
	final var subkey = key + GlobalStrings.loadDataLoader(DataLoaderString.DIRECTION_SEPARATOR) + dir.ordinal();
	if (!data.containsKey(subkey)) {
	    return false;
	}
	return data.getString(subkey) == GlobalStrings.loadDataLoader(DataLoaderString.ANY);
    }

    public static boolean loadMovesBoxes(final GameObjectID objID, final Direction dir) {
	final var data = ArenaObjectDataLoader.load(ArenaObjectDataFile.MOVES_BOXES);
	final var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return false;
	}
	final var value = data.getString(key);
	if (value == GlobalStrings.loadDataLoader(DataLoaderString.ANY)) {
	    return true;
	}
	if (value == GlobalStrings.loadDataLoader(DataLoaderString.NONE)
		|| value != GlobalStrings.loadDataLoader(DataLoaderString.VALUE_DIRECTION)) {
	    return false;
	}
	final var subkey = key + GlobalStrings.loadDataLoader(DataLoaderString.DIRECTION_SEPARATOR) + dir.ordinal();
	if (!data.containsKey(subkey)) {
	    return false;
	}
	return data.getString(subkey) == GlobalStrings.loadDataLoader(DataLoaderString.ANY);
    }

    public static boolean loadMovesHostiles(final GameObjectID objID, final Direction dir) {
	final var data = ArenaObjectDataLoader.load(ArenaObjectDataFile.MOVES_HOSTILES);
	final var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return false;
	}
	final var value = data.getString(key);
	if (value == GlobalStrings.loadDataLoader(DataLoaderString.ANY)) {
	    return true;
	}
	if (value == GlobalStrings.loadDataLoader(DataLoaderString.NONE)
		|| value != GlobalStrings.loadDataLoader(DataLoaderString.VALUE_DIRECTION)) {
	    return false;
	}
	final var subkey = key + GlobalStrings.loadDataLoader(DataLoaderString.DIRECTION_SEPARATOR) + dir.ordinal();
	if (!data.containsKey(subkey)) {
	    return false;
	}
	return data.getString(subkey) == GlobalStrings.loadDataLoader(DataLoaderString.ANY);
    }

    public static boolean loadMovesMirrors(final GameObjectID objID, final Direction dir) {
	final var data = ArenaObjectDataLoader.load(ArenaObjectDataFile.MOVES_MIRRORS);
	final var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return false;
	}
	final var value = data.getString(key);
	if (value == GlobalStrings.loadDataLoader(DataLoaderString.ANY)) {
	    return true;
	}
	if (value == GlobalStrings.loadDataLoader(DataLoaderString.NONE)
		|| value != GlobalStrings.loadDataLoader(DataLoaderString.VALUE_DIRECTION)) {
	    return false;
	}
	final var subkey = key + GlobalStrings.loadDataLoader(DataLoaderString.DIRECTION_SEPARATOR) + dir.ordinal();
	if (!data.containsKey(subkey)) {
	    return false;
	}
	return data.getString(subkey) == GlobalStrings.loadDataLoader(DataLoaderString.ANY);
    }

    public static boolean loadMovesTanks(final GameObjectID objID, final Direction dir) {
	final var data = ArenaObjectDataLoader.load(ArenaObjectDataFile.MOVES_TANKS);
	final var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return false;
	}
	final var value = data.getString(key);
	if (value == GlobalStrings.loadDataLoader(DataLoaderString.ANY)) {
	    return true;
	}
	if (value == GlobalStrings.loadDataLoader(DataLoaderString.NONE)
		|| value != GlobalStrings.loadDataLoader(DataLoaderString.VALUE_DIRECTION)) {
	    return false;
	}
	final var subkey = key + GlobalStrings.loadDataLoader(DataLoaderString.DIRECTION_SEPARATOR) + dir.ordinal();
	if (!data.containsKey(subkey)) {
	    return false;
	}
	return data.getString(subkey) == GlobalStrings.loadDataLoader(DataLoaderString.ANY);
    }

    public static int loadNavigate(final GameObjectID objID) {
	final var data = ArenaObjectDataLoader.load(ArenaObjectDataFile.NAVIGATE);
	final var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return 0;
	}
	final var value = data.getString(key);
	if (value == GlobalStrings.loadDataLoader(DataLoaderString.NONE)) {
	    return 0;
	}
	return Integer.parseInt(value);
    }

    public static GameObjectID loadNewIDFire(final GameObjectID objID) {
	final var data = ArenaObjectDataLoader.load(ArenaObjectDataFile.NEW_ID_FIRE);
	final var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return objID;
	}
	final var value = data.getString(key);
	if (value == GlobalStrings.loadDataLoader(DataLoaderString.NONE)) {
	    return objID;
	}
	var index = Integer.parseInt(value);
	if (index == ArenaObjectDataLoader.GameObjectID_DONT_CHANGE) {
	    return objID;
	}
	return GameObjectID.values()[index];
    }

    public static GameObjectID loadNewIDIce(final GameObjectID objID) {
	final var data = ArenaObjectDataLoader.load(ArenaObjectDataFile.NEW_ID_ICE);
	final var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return objID;
	}
	final var value = data.getString(key);
	if (value == GlobalStrings.loadDataLoader(DataLoaderString.NONE)) {
	    return objID;
	}
	var index = Integer.parseInt(value);
	if (index == ArenaObjectDataLoader.GameObjectID_DONT_CHANGE) {
	    return objID;
	}
	return GameObjectID.values()[index];
    }

    public static GameObjectID loadPair(final GameObjectID objID) {
	final var data = ArenaObjectDataLoader.load(ArenaObjectDataFile.PAIR);
	final var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return objID;
	}
	final var value = data.getString(key);
	if (value == GlobalStrings.loadDataLoader(DataLoaderString.NONE)) {
	    return objID;
	}
	var index = Integer.parseInt(value);
	if (index == ArenaObjectDataLoader.GameObjectID_DONT_CHANGE) {
	    return objID;
	}
	return GameObjectID.values()[index];
    }

    public static boolean loadPowerful(final GameObjectID objID) {
	final var data = ArenaObjectDataLoader.load(ArenaObjectDataFile.POWERFUL);
	final var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return false;
	}
	return data.getString(key) == GlobalStrings.loadDataLoader(DataLoaderString.ANY);
    }

    public static Sound loadRangeSound(final GameObjectID objID) {
	final var data = ArenaObjectDataLoader.load(ArenaObjectDataFile.RANGE_SOUND);
	final var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return Sound._NONE;
	}
	var value = data.getString(key);
	var index = Integer.parseInt(value);
	return Sound.values()[index];
    }

    public static boolean loadReactsToObjectsPushedInto(final GameObjectID objID) {
	final var data = ArenaObjectDataLoader.load(ArenaObjectDataFile.REACTS_TO_OBJECTS_PUSHED_INTO);
	final var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return false;
	}
	return data.getString(key) == GlobalStrings.loadDataLoader(DataLoaderString.ANY);
    }

    public static boolean loadReflect(final GameObjectID objID, final Direction dir) {
	final var data = ArenaObjectDataLoader.load(ArenaObjectDataFile.REFLECT);
	final var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return false;
	}
	final var value = data.getString(key);
	if (value == GlobalStrings.loadDataLoader(DataLoaderString.ANY)) {
	    return true;
	}
	if (value == GlobalStrings.loadDataLoader(DataLoaderString.NONE)
		|| value != GlobalStrings.loadDataLoader(DataLoaderString.VALUE_DIRECTION)) {
	    return false;
	}
	final var subkey = key + GlobalStrings.loadDataLoader(DataLoaderString.DIRECTION_SEPARATOR) + dir.ordinal();
	if (!data.containsKey(subkey)) {
	    return false;
	}
	return data.getString(subkey) == GlobalStrings.loadDataLoader(DataLoaderString.ANY);
    }

    public static boolean loadRemovesPushedObjects(final GameObjectID objID) {
	final var data = ArenaObjectDataLoader.load(ArenaObjectDataFile.REMOVES_PUSHED_OBJECTS);
	final var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return false;
	}
	return data.getString(key) == GlobalStrings.loadDataLoader(DataLoaderString.ANY);
    }

    public static boolean loadRoll(final GameObjectID objID) {
	final var data = ArenaObjectDataLoader.load(ArenaObjectDataFile.ROLL);
	final var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return false;
	}
	return data.getString(key) == GlobalStrings.loadDataLoader(DataLoaderString.ANY);
    }

    public static boolean loadShoot(final GameObjectID objID) {
	final var data = ArenaObjectDataLoader.load(ArenaObjectDataFile.SHOOT);
	final var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return false;
	}
	return data.getString(key) == GlobalStrings.loadDataLoader(DataLoaderString.ANY);
    }

    public static boolean loadSolid(final GameObjectID objID, final Direction dir) {
	final var data = ArenaObjectDataLoader.load(ArenaObjectDataFile.SOLID);
	final var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return false;
	}
	final var value = data.getString(key);
	if (value == GlobalStrings.loadDataLoader(DataLoaderString.ANY)) {
	    return true;
	}
	if (value == GlobalStrings.loadDataLoader(DataLoaderString.NONE)
		|| value != GlobalStrings.loadDataLoader(DataLoaderString.VALUE_DIRECTION)) {
	    return false;
	}
	final var subkey = key + GlobalStrings.loadDataLoader(DataLoaderString.DIRECTION_SEPARATOR) + dir.ordinal();
	if (!data.containsKey(subkey)) {
	    return false;
	}
	return data.getString(subkey) == GlobalStrings.loadDataLoader(DataLoaderString.ANY);
    }

    public static boolean loadStunned(final GameObjectID objID) {
	final var data = ArenaObjectDataLoader.load(ArenaObjectDataFile.STUNNED);
	final var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return false;
	}
	return data.getString(key) == GlobalStrings.loadDataLoader(DataLoaderString.ANY);
    }

    public static int loadTimer(final GameObjectID objID) {
	final var data = ArenaObjectDataLoader.load(ArenaObjectDataFile.TIMER);
	final var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return 0;
	}
	final var value = data.getString(key);
	if (value == GlobalStrings.loadDataLoader(DataLoaderString.NONE)) {
	    return 0;
	}
	return Integer.parseInt(value);
    }

    public static boolean loadTrigger(final GameObjectID objID) {
	final var data = ArenaObjectDataLoader.load(ArenaObjectDataFile.USES_TRIGGER);
	final var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return false;
	}
	return data.getString(key) == GlobalStrings.loadDataLoader(DataLoaderString.ANY);
    }

    public static GameObjectID loadWeaken(final GameObjectID objID) {
	final var data = ArenaObjectDataLoader.load(ArenaObjectDataFile.WEAKEN);
	final var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return objID;
	}
	final var value = data.getString(key);
	if (value == GlobalStrings.loadDataLoader(DataLoaderString.NONE)) {
	    return objID;
	}
	var index = Integer.parseInt(value);
	if (index == ArenaObjectDataLoader.GameObjectID_DONT_CHANGE) {
	    return objID;
	}
	return GameObjectID.values()[index];
    }

    public static int loadWeight(final GameObjectID objID) {
	final var data = ArenaObjectDataLoader.load(ArenaObjectDataFile.WEIGHT);
	final var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return 0;
	}
	final var value = data.getString(key);
	if (value == GlobalStrings.loadDataLoader(DataLoaderString.NONE)) {
	    return 0;
	}
	return Integer.parseInt(value);
    }
}
