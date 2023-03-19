package com.puttysoftware.lasertank.datatype;

import java.util.ResourceBundle;

import com.puttysoftware.lasertank.helper.DirectionHelper;
import com.puttysoftware.lasertank.helper.GameActionHelper;
import com.puttysoftware.lasertank.helper.MaterialHelper;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameAction;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.GameType;
import com.puttysoftware.lasertank.index.Material;
import com.puttysoftware.lasertank.locale.global.DataLoaderString;
import com.puttysoftware.lasertank.locale.global.GlobalStrings;

public class DataLoader {
	private static ResourceBundle load(final DataFile file) {
		return ResourceBundle.getBundle(GlobalStrings.loadDataLoader(DataLoaderString.LOAD_PATH) + file.getName());
	}

	public static boolean loadAcceptTick(final GameObjectID objID, final GameAction action) {
		final var data = DataLoader.load(DataFile.ACCEPT_TICK);
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

	public static Direction[] loadDirection(final GameObjectID objID) {
		final var fallback = new Direction[] { Direction.NONE };
		final var all = Direction.values();
		final var data = DataLoader.load(DataFile.DIRECTION);
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
		final var data = DataLoader.load(DataFile.FRAME);
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
		final var data = DataLoader.load(DataFile.FRICTION);
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
		final var subvalue = data.getString(subkey);
		if (subvalue == GlobalStrings.loadDataLoader(DataLoaderString.ANY)) {
			return true;
		} else {
			return false;
		}
	}

	public static int loadHeight(final GameObjectID objID) {
		final var data = DataLoader.load(DataFile.LAYER);
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

	public static int[] loadIndex(final GameObjectID objID) {
		final var fallback = new int[] { 0 };
		final var data = DataLoader.load(DataFile.INDEX);
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

	public static int loadLayer(final GameObjectID objID) {
		final var data = DataLoader.load(DataFile.LAYER);
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

	public static boolean loadLethal(final GameObjectID objID, final int index) {
		final var data = DataLoader.load(DataFile.LETHAL);
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
		final var subvalue = data.getString(subkey);
		if (subvalue == GlobalStrings.loadDataLoader(DataLoaderString.ANY)) {
			return true;
		} else {
			return false;
		}
	}

	public static Material loadMaterial(final GameObjectID objID) {
		final var data = DataLoader.load(DataFile.MATERIAL);
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
		final var data = DataLoader.load(DataFile.MOVABLE);
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
		final var subvalue = data.getString(subkey);
		if (subvalue == GlobalStrings.loadDataLoader(DataLoaderString.ANY)) {
			return true;
		} else {
			return false;
		}
	}

	public static int loadNavigate(final GameObjectID objID) {
		final var data = DataLoader.load(DataFile.NAVIGATE);
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

	public static boolean loadReflect(final GameObjectID objID, final Direction dir) {
		final var data = DataLoader.load(DataFile.REFLECT);
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
		final var subvalue = data.getString(subkey);
		if (subvalue == GlobalStrings.loadDataLoader(DataLoaderString.ANY)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean loadShoot(final GameObjectID objID) {
		final var data = DataLoader.load(DataFile.SHOOT);
		final var key = String.valueOf(objID);
		if (!data.containsKey(key)) {
			return false;
		}
		final var value = data.getString(key);
		if (value == GlobalStrings.loadDataLoader(DataLoaderString.ANY)) {
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
		final var subvalue = data.getString(subkey);
		if (subvalue == GlobalStrings.loadDataLoader(DataLoaderString.ANY)) {
			return true;
		} else {
			return false;
		}
	}

	public static GameType[] loadTypes(final GameObjectID objID) {
		final var fallback = new GameType[] { GameType.NONE };
		final var data = DataLoader.load(DataFile.TYPES);
		final var key = String.valueOf(objID);
		if (!data.containsKey(key)) {
			return fallback;
		}
		final var value = data.getString(key);
		if (value == GlobalStrings.loadDataLoader(DataLoaderString.ANY)) {
			return fallback;
		}
		if (!value.contains(GlobalStrings.loadDataLoader(DataLoaderString.VALUE_SEPARATOR))) {
			return new GameType[] { GameType.values()[Integer.parseInt(value)] };
		}
		final var split = value.split(GlobalStrings.loadDataLoader(DataLoaderString.VALUE_SEPARATOR));
		final var res = new GameType[split.length];
		for (var r = 0; r < res.length; r++) {
			res[r] = GameType.values()[Integer.parseInt(split[r])];
		}
		return res;
	}

	public static int loadWeight(final GameObjectID objID) {
		final var data = DataLoader.load(DataFile.WEIGHT);
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
