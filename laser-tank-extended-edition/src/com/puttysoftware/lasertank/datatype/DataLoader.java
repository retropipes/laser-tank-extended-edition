package com.puttysoftware.lasertank.datatype;

import java.util.ResourceBundle;

import com.puttysoftware.lasertank.helper.DirectionHelper;
import com.puttysoftware.lasertank.helper.GameActionHelper;
import com.puttysoftware.lasertank.helper.GameObjectIDHelper;
import com.puttysoftware.lasertank.helper.MaterialHelper;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameAction;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.Material;

public class DataLoader {
    private static ResourceBundle load(final DataFile file) {
	return ResourceBundle.getBundle("asset.data." + file.getName());
    }

    public static boolean loadAcceptTick(final GameObjectID objID, final GameAction action) {
	final var data = DataLoader.load(DataFile.ACCEPT_TICK);
	final var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return false;
	}
	final var value = data.getString(key);
	if (value == "!") {
	    return true;
	}
	return action == GameActionHelper.fromStringValue(value);
    }

    public static Direction[] loadDirection(final GameObjectID objID) {
	final var fallback = new Direction[] { Direction.NONE };
	final var all = Direction.values();
	final var data = DataLoader.load(DataFile.DIRECTION);
	final var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return fallback;
	}
	final var value = data.getString(key);
	if (value == "!") {
	    return all;
	}
	if (value.contains(",")) {
	    final var split = value.split(",");
	    final var res = new Direction[split.length];
	    for (var r = 0; r < res.length; r++) {
		res[r] = DirectionHelper.fromStringValue(split[r]);
	    }
	    return res;
	} else {
	    return new Direction[] { DirectionHelper.fromStringValue(value) };
	}
    }

    public static int loadFrame(final GameObjectID objID) {
	final var data = DataLoader.load(DataFile.FRAME);
	final var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return 0;
	}
	final var value = data.getString(key);
	if (value == "?") {
	    return 0;
	}
	return Integer.parseInt(value);
    }

    public static boolean loadFriction(final GameObjectID objID, final Direction dir) {
	final var data = DataLoader.load(DataFile.FRICTION);
	final var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return false;
	}
	final var value = data.getString(key);
	if (value == "!") {
	    return true;
	}
	if ((value == "?") || (value != "d")) {
	    return false;
	} else {
	    final var subkey = key + "_d" + dir.ordinal();
	    if (!data.containsKey(subkey)) {
		return false;
	    }
	    final var subvalue = data.getString(subkey);
	    if (subvalue == "!") {
		return true;
	    } else {
		return false;
	    }
	}
    }

    public static int loadHeight(final GameObjectID objID) {
	final var data = DataLoader.load(DataFile.LAYER);
	final var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return 0;
	}
	final var value = data.getString(key);
	if (value == "?") {
	    return 0;
	}
	return Integer.parseInt(value);
    }

    public static int[] loadIndex(final GameObjectID objID) {
	final var fallback = new int[] { 0 };
	final var data = DataLoader.load(DataFile.INDEX);
	final var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return fallback;
	}
	final var value = data.getString(key);
	if (value == "!") {
	    return fallback;
	}
	if (value.contains(",")) {
	    final var split = value.split(",");
	    final var res = new int[split.length];
	    for (var r = 0; r < res.length; r++) {
		res[r] = Integer.parseInt(split[r]);
	    }
	    return res;
	} else {
	    return new int[] { Integer.parseInt(value) };
	}
    }

    public static int loadLayer(final GameObjectID objID) {
	final var data = DataLoader.load(DataFile.LAYER);
	final var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return 0;
	}
	final var value = data.getString(key);
	if (value == "?") {
	    return 0;
	}
	return Integer.parseInt(value);
    }

    public static boolean loadLethal(final GameObjectID objID, final Direction dir) {
	final var data = DataLoader.load(DataFile.LETHAL);
	final var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return false;
	}
	final var value = data.getString(key);
	if (value == "!") {
	    return true;
	}
	if ((value == "?") || (value != "d")) {
	    return false;
	} else {
	    final var subkey = key + "_d" + dir.ordinal();
	    if (!data.containsKey(subkey)) {
		return false;
	    }
	    final var subvalue = data.getString(subkey);
	    if (subvalue == "!") {
		return true;
	    } else {
		return false;
	    }
	}
    }

    public static Material loadMaterial(final GameObjectID objID) {
	final var data = DataLoader.load(DataFile.MATERIAL);
	final var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return Material.NONE;
	}
	final var value = data.getString(key);
	if (value == "!") {
	    return Material.NONE;
	}
	return MaterialHelper.fromStringValue(value);
    }

    public static boolean loadMovable(final GameObjectID objID, final Direction dir) {
	final var data = DataLoader.load(DataFile.MOVABLE);
	final var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return false;
	}
	final var value = data.getString(key);
	if (value == "!") {
	    return true;
	}
	if ((value == "?") || (value != "d")) {
	    return false;
	} else {
	    final var subkey = key + "_d" + dir.ordinal();
	    if (!data.containsKey(subkey)) {
		return false;
	    }
	    final var subvalue = data.getString(subkey);
	    if (subvalue == "!") {
		return true;
	    } else {
		return false;
	    }
	}
    }

    public static boolean loadReflect(final GameObjectID objID, final Direction dir) {
	final var data = DataLoader.load(DataFile.REFLECT);
	final var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return false;
	}
	final var value = data.getString(key);
	if (value == "!") {
	    return true;
	}
	if ((value == "?") || (value != "d")) {
	    return false;
	} else {
	    final var subkey = key + "_d" + dir.ordinal();
	    if (!data.containsKey(subkey)) {
		return false;
	    }
	    final var subvalue = data.getString(subkey);
	    if (subvalue == "!") {
		return true;
	    } else {
		return false;
	    }
	}
    }

    public static boolean loadRotate(final GameObjectID objID, final Direction dir) {
	final var data = DataLoader.load(DataFile.ROTATE);
	final var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return false;
	}
	final var value = data.getString(key);
	if (value == "!") {
	    return true;
	}
	if ((value == "?") || (value != "d")) {
	    return false;
	} else {
	    final var subkey = key + "_d" + dir.ordinal();
	    if (!data.containsKey(subkey)) {
		return false;
	    }
	    final var subvalue = data.getString(subkey);
	    if (subvalue == "!") {
		return true;
	    } else {
		return false;
	    }
	}
    }

    public static boolean loadShoot(final GameObjectID objID) {
	final var data = DataLoader.load(DataFile.SHOOT);
	final var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return false;
	}
	final var value = data.getString(key);
	if (value == "!") {
	    return true;
	}
	return false;
    }

    public static boolean loadSolid(final GameObjectID objID, final Direction dir) {
	final var data = DataLoader.load(DataFile.SOLID);
	final var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return false;
	}
	final var value = data.getString(key);
	if (value == "!") {
	    return true;
	}
	if ((value == "?") || (value != "d")) {
	    return false;
	} else {
	    final var subkey = key + "_d" + dir.ordinal();
	    if (!data.containsKey(subkey)) {
		return false;
	    }
	    final var subvalue = data.getString(subkey);
	    if (subvalue == "!") {
		return true;
	    } else {
		return false;
	    }
	}
    }

    public static GameObjectID loadTransform(final GameObjectID objID, final Material materialID) {
	final var data = DataLoader.load(DataFile.TRANSFORM);
	final var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return objID;
	}
	final var value = data.getString(key);
	if (value == "?") {
	    return objID;
	}
	if (value == "m") {
	    final var subkey = key + "_m" + materialID.ordinal();
	    if (!data.containsKey(subkey)) {
		return objID;
	    }
	    final var subvalue = data.getString(subkey);
	    if (subvalue == "?") {
		return objID;
	    } else {
		return GameObjectIDHelper.fromStringValue(subvalue);
	    }
	} else {
	    return GameObjectIDHelper.fromStringValue(value);
	}
    }

    public static int loadWeight(final GameObjectID objID) {
	final var data = DataLoader.load(DataFile.WEIGHT);
	final var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return 0;
	}
	final var value = data.getString(key);
	if (value == "?") {
	    return 0;
	}
	return Integer.parseInt(value);
    }
}
