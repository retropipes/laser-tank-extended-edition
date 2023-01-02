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
	var data = DataLoader.load(DataFile.ACCEPT_TICK);
	var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return false;
	}
	var value = data.getString(key);
	if (value == "!") {
	    return true;
	} else {
	    return action == GameActionHelper.fromStringValue(value);
	}
    }

    public static Direction[] loadDirection(final GameObjectID objID) {
	var fallback = new Direction[] { Direction.NONE };
	var all = Direction.values();
	var data = DataLoader.load(DataFile.DIRECTION);
	var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return fallback;
	}
	var value = data.getString(key);
	if (value == "!") {
	    return all;
	} else {
	    if (value.contains(",")) {
		var split = value.split(",");
		Direction[] res = new Direction[split.length];
		for (int r = 0; r < res.length; r++) {
		    res[r] = DirectionHelper.fromStringValue(split[r]);
		}
		return res;
	    } else {
		return new Direction[] { DirectionHelper.fromStringValue(value) };
	    }
	}
    }

    public static int loadFrame(final GameObjectID objID) {
	var data = DataLoader.load(DataFile.FRAME);
	var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return 0;
	}
	var value = data.getString(key);
	if (value == "?") {
	    return 0;
	} else {
	    return Integer.parseInt(value);
	}
    }

    public static boolean loadFriction(final GameObjectID objID, final Direction dir) {
	var data = DataLoader.load(DataFile.FRICTION);
	var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return false;
	}
	var value = data.getString(key);
	if (value == "!") {
	    return true;
	} else if (value == "?") {
	    return false;
	} else if (value == "d") {
	    var subkey = key + "_d" + dir.ordinal();
	    if (!data.containsKey(subkey)) {
		return false;
	    }
	    var subvalue = data.getString(subkey);
	    if (subvalue == "!") {
		return true;
	    } else {
		return false;
	    }
	} else {
	    return false;
	}
    }

    public static int loadHeight(final GameObjectID objID) {
	var data = DataLoader.load(DataFile.LAYER);
	var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return 0;
	}
	var value = data.getString(key);
	if (value == "?") {
	    return 0;
	} else {
	    return Integer.parseInt(value);
	}
    }

    public static int[] loadIndex(final GameObjectID objID) {
	var fallback = new int[] { 0 };
	var data = DataLoader.load(DataFile.INDEX);
	var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return fallback;
	}
	var value = data.getString(key);
	if (value == "!") {
	    return fallback;
	} else {
	    if (value.contains(",")) {
		var split = value.split(",");
		int[] res = new int[split.length];
		for (int r = 0; r < res.length; r++) {
		    res[r] = Integer.parseInt(split[r]);
		}
		return res;
	    } else {
		return new int[] { Integer.parseInt(value) };
	    }
	}
    }

    public static int loadLayer(final GameObjectID objID) {
	var data = DataLoader.load(DataFile.LAYER);
	var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return 0;
	}
	var value = data.getString(key);
	if (value == "?") {
	    return 0;
	} else {
	    return Integer.parseInt(value);
	}
    }

    public static boolean loadLethal(final GameObjectID objID, final Direction dir) {
	var data = DataLoader.load(DataFile.LETHAL);
	var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return false;
	}
	var value = data.getString(key);
	if (value == "!") {
	    return true;
	} else if (value == "?") {
	    return false;
	} else if (value == "d") {
	    var subkey = key + "_d" + dir.ordinal();
	    if (!data.containsKey(subkey)) {
		return false;
	    }
	    var subvalue = data.getString(subkey);
	    if (subvalue == "!") {
		return true;
	    } else {
		return false;
	    }
	} else {
	    return false;
	}
    }

    public static Material loadMaterial(final GameObjectID objID) {
	var data = DataLoader.load(DataFile.MATERIAL);
	var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return Material.NONE;
	}
	var value = data.getString(key);
	if (value == "!") {
	    return Material.NONE;
	} else {
	    return MaterialHelper.fromStringValue(value);
	}
    }

    public static boolean loadMovable(final GameObjectID objID, final Direction dir) {
	var data = DataLoader.load(DataFile.MOVABLE);
	var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return false;
	}
	var value = data.getString(key);
	if (value == "!") {
	    return true;
	} else if (value == "?") {
	    return false;
	} else if (value == "d") {
	    var subkey = key + "_d" + dir.ordinal();
	    if (!data.containsKey(subkey)) {
		return false;
	    }
	    var subvalue = data.getString(subkey);
	    if (subvalue == "!") {
		return true;
	    } else {
		return false;
	    }
	} else {
	    return false;
	}
    }

    public static boolean loadReflect(final GameObjectID objID, final Direction dir) {
	var data = DataLoader.load(DataFile.REFLECT);
	var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return false;
	}
	var value = data.getString(key);
	if (value == "!") {
	    return true;
	} else if (value == "?") {
	    return false;
	} else if (value == "d") {
	    var subkey = key + "_d" + dir.ordinal();
	    if (!data.containsKey(subkey)) {
		return false;
	    }
	    var subvalue = data.getString(subkey);
	    if (subvalue == "!") {
		return true;
	    } else {
		return false;
	    }
	} else {
	    return false;
	}
    }

    public static boolean loadRotate(final GameObjectID objID, final Direction dir) {
	var data = DataLoader.load(DataFile.ROTATE);
	var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return false;
	}
	var value = data.getString(key);
	if (value == "!") {
	    return true;
	} else if (value == "?") {
	    return false;
	} else if (value == "d") {
	    var subkey = key + "_d" + dir.ordinal();
	    if (!data.containsKey(subkey)) {
		return false;
	    }
	    var subvalue = data.getString(subkey);
	    if (subvalue == "!") {
		return true;
	    } else {
		return false;
	    }
	} else {
	    return false;
	}
    }

    public static boolean loadShoot(final GameObjectID objID, final Direction dir) {
	var data = DataLoader.load(DataFile.SHOOT);
	var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return false;
	}
	var value = data.getString(key);
	if (value == "!") {
	    return true;
	} else if (value == "?") {
	    return false;
	} else if (value == "d") {
	    var subkey = key + "_d" + dir.ordinal();
	    if (!data.containsKey(subkey)) {
		return false;
	    }
	    var subvalue = data.getString(subkey);
	    if (subvalue == "!") {
		return true;
	    } else {
		return false;
	    }
	} else {
	    return false;
	}
    }

    public static boolean loadSolid(final GameObjectID objID, final Direction dir) {
	var data = DataLoader.load(DataFile.SOLID);
	var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return false;
	}
	var value = data.getString(key);
	if (value == "!") {
	    return true;
	} else if (value == "?") {
	    return false;
	} else if (value == "d") {
	    var subkey = key + "_d" + dir.ordinal();
	    if (!data.containsKey(subkey)) {
		return false;
	    }
	    var subvalue = data.getString(subkey);
	    if (subvalue == "!") {
		return true;
	    } else {
		return false;
	    }
	} else {
	    return false;
	}
    }

    public static GameObjectID loadTransform(final GameObjectID objID, final Material materialID) {
	var data = DataLoader.load(DataFile.TRANSFORM);
	var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return objID;
	}
	var value = data.getString(key);
	if (value == "?") {
	    return objID;
	} else if (value == "m") {
	    var subkey = key + "_m" + materialID.ordinal();
	    if (!data.containsKey(subkey)) {
		return objID;
	    }
	    var subvalue = data.getString(subkey);
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
	var data = DataLoader.load(DataFile.WEIGHT);
	var key = String.valueOf(objID);
	if (!data.containsKey(key)) {
	    return 0;
	}
	var value = data.getString(key);
	if (value == "?") {
	    return 0;
	} else {
	    return Integer.parseInt(value);
	}
    }
}
