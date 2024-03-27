package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.helper.DirectionHelper;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameAction;
import com.puttysoftware.lasertank.index.GameColor;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.Material;

class ArenaObjectData {
    private static final int PLASTIC_FORCE = 0;
    private static final int DEFAULT_FORCE = 1;
    private static final int METAL_FORCE = 2;

    public static boolean acceptTick(final GameObjectID objectID, final GameAction action) {
	return ArenaObjectDataLoader.loadAcceptTick(objectID, action);
    }

    public static GameObjectID attributeRenderHook(final GameObjectID objectID) {
	return ArenaObjectDataLoader.loadAttributeRender(objectID);
    }

    public static boolean canCloak(final GameObjectID objectID) {
	return ArenaObjectDataLoader.loadCloak(objectID);
    }

    public static boolean canControl(final GameObjectID objectID) {
	return ArenaObjectDataLoader.loadControl(objectID);
    }

    public static boolean canJump(final GameObjectID objectID) {
	return ArenaObjectDataLoader.loadJump(objectID);
    }

    public static boolean canLasersPassThrough(final GameObjectID objectID) {
	return ArenaObjectDataLoader.loadLaserPassthru(objectID);
    }

    public static boolean canMove(final GameObjectID objectID) {
	return ArenaObjectDataLoader.loadMovable(objectID, Direction.NONE);
    }

    public static boolean canRoll(final GameObjectID objectID) {
	return ArenaObjectDataLoader.loadRoll(objectID);
    }

    public static boolean canShoot(final GameObjectID objectID) {
	return ArenaObjectDataLoader.loadShoot(objectID);
    }

    public static int getBlockHeight(final GameObjectID objectID) {
	return ArenaObjectDataLoader.loadHeight(objectID);
    }

    public static final int getImbuedForce(final Material materialID) {
	if (materialID == Material.PLASTIC) {
	    return ArenaObjectData.PLASTIC_FORCE;
	}
	if (materialID == Material.METALLIC) {
	    return ArenaObjectData.METAL_FORCE;
	}
	return ArenaObjectData.DEFAULT_FORCE;
    }

    public static int getLastFrameNumber(final GameObjectID objectID) {
	return ArenaObjectDataLoader.loadFrame(objectID);
    }

    public static int getLayer(final GameObjectID objectID) {
	return ArenaObjectDataLoader.loadLayer(objectID);
    }

    public static Material getMaterial(final GameObjectID objectID) {
	return ArenaObjectDataLoader.loadMaterial(objectID);
    }

    public static final int getMinimumReactionForce(final Material materialID) {
	if (materialID == Material.PLASTIC) {
	    return ArenaObjectData.PLASTIC_FORCE;
	}
	if (materialID == Material.METALLIC) {
	    return ArenaObjectData.METAL_FORCE;
	}
	return ArenaObjectData.DEFAULT_FORCE;
    }

    public static GameObjectID getPairedObjectID(final GameObjectID objectID) {
	return ArenaObjectDataLoader.loadPair(objectID);
    }

    public static GameColor[] getValidColors(final GameObjectID objectID) {
	return ArenaObjectDataLoader.loadColor(objectID);
    }

    public static Direction[] getValidDirections(final GameObjectID objectID) {
	return ArenaObjectDataLoader.loadDirection(objectID);
    }

    public static int[] getValidIndexes(final GameObjectID objectID) {
	return ArenaObjectDataLoader.loadIndex(objectID);
    }

    public static boolean hasDirection(final GameObjectID objectID) {
	return ArenaObjectDataLoader.loadDirection(objectID).length > 1;
    }

    public static boolean hasFriction(final GameObjectID objectID) {
	return ArenaObjectDataLoader.loadFriction(objectID, Direction.NONE);
    }

    public static boolean hitReflectiveSide(final Direction dir) {
	Direction trigger1, trigger2;
	trigger1 = DirectionHelper.previous(dir);
	trigger2 = DirectionHelper.next(dir);
	return dir == trigger1 || dir == trigger2;
    }

    public static int initialTimerValue(final GameObjectID objectID) {
	return ArenaObjectDataLoader.loadTimer(objectID);
    }

    public static boolean isAnimated(final GameObjectID objectID) {
	return ArenaObjectDataLoader.loadFrame(objectID) > 1;
    }

    public static boolean isBox(final GameObjectID objectID) {
	return ArenaObjectDataLoader.loadBox(objectID);
    }

    public static boolean isHostile(final GameObjectID objectID) {
	return ArenaObjectDataLoader.loadHostile(objectID);
    }

    public static boolean isMovableMirror(final GameObjectID objectID, final Direction dir) {
	return ArenaObjectDataLoader.loadMovableMirror(objectID, dir);
    }

    public static boolean isPowerful(final GameObjectID objectID) {
	return ArenaObjectDataLoader.loadPowerful(objectID);
    }

    public static boolean isPushable(final GameObjectID objectID) {
	return ArenaObjectDataLoader.loadMovable(objectID, Direction.NONE);
    }

    public static boolean isReflective(final GameObjectID objectID, final Direction dir) {
	return ArenaObjectDataLoader.loadReflect(objectID, dir);
    }

    public static boolean isSolid(final GameObjectID objectID) {
	return ArenaObjectDataLoader.loadSolid(objectID, Direction.NONE);
    }

    public static boolean isStunned(final GameObjectID objectID) {
	return ArenaObjectDataLoader.loadStunned(objectID);
    }

    public static boolean isTimerActive(final GameObjectID objectID) {
	return ArenaObjectDataLoader.loadTimer(objectID) > 0;
    }

    public static boolean killsOnMove(final GameObjectID objectID, final int index) {
	return ArenaObjectDataLoader.loadLethal(objectID, index);
    }

    public static boolean movesBoxes(final GameObjectID objectID, final Direction dir) {
	return ArenaObjectDataLoader.loadMovesBoxes(objectID, dir);
    }

    public static boolean movesHostiles(final GameObjectID objectID, final Direction dir) {
	return ArenaObjectDataLoader.loadMovesHostiles(objectID, dir);
    }

    public static boolean movesMirrors(final GameObjectID objectID, final Direction dir) {
	return ArenaObjectDataLoader.loadMovesMirrors(objectID, dir);
    }

    public static boolean movesTanks(final GameObjectID objectID, final Direction dir) {
	return ArenaObjectDataLoader.loadMovesTanks(objectID, dir);
    }

    public static int navigatesToOnMove(final GameObjectID objectID) {
	return ArenaObjectDataLoader.loadNavigate(objectID);
    }

    public static boolean solvesOnMove(final GameObjectID objectID) {
	return objectID == GameObjectID.FLAG;
    }

    public static boolean usesTrigger(final GameObjectID objectID) {
	return ArenaObjectDataLoader.loadTrigger(objectID);
    }
}
