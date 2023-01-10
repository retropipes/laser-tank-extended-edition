package com.puttysoftware.lasertank.datatype;

import com.puttysoftware.lasertank.helper.DirectionHelper;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameAction;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.Material;

public class GameObjectData {
    private static final int PLASTIC_MINIMUM_REACTION_FORCE = 0;
    private static final int DEFAULT_MINIMUM_REACTION_FORCE = 1;
    private static final int METAL_MINIMUM_REACTION_FORCE = 2;

    public static final int getImbuedRangeForce(final Material materialID) {
	if (materialID == Material.PLASTIC) {
	    return GameObjectData.PLASTIC_MINIMUM_REACTION_FORCE;
	}
	if (materialID == Material.METALLIC) {
	    return GameObjectData.METAL_MINIMUM_REACTION_FORCE;
	}
	return GameObjectData.DEFAULT_MINIMUM_REACTION_FORCE;
    }

    public static final int getMinimumReactionForce(final Material materialID) {
	if (materialID == Material.PLASTIC) {
	    return GameObjectData.PLASTIC_MINIMUM_REACTION_FORCE;
	}
	if (materialID == Material.METALLIC) {
	    return GameObjectData.METAL_MINIMUM_REACTION_FORCE;
	}
	return GameObjectData.DEFAULT_MINIMUM_REACTION_FORCE;
    }

    public static boolean hitReflectiveSide(final Direction dir) {
	Direction trigger1, trigger2;
	trigger1 = DirectionHelper.previous(dir);
	trigger2 = DirectionHelper.next(dir);
	return dir == trigger1 || dir == trigger2;
    }

    public static boolean acceptTick(final GameObjectID objectID, final GameAction action) {
	return DataLoader.loadAcceptTick(objectID, action);
    }

    public static boolean canMove(final GameObjectID objectID) {
	return DataLoader.loadMovable(objectID, Direction.NONE);
    }

    public static boolean canShoot(final GameObjectID objectID) {
	return DataLoader.loadShoot(objectID);
    }

    public static int getBlockHeight(final GameObjectID objectID) {
	return DataLoader.loadHeight(objectID);
    }

    public static int getFirstFrameNumber(final GameObjectID objectID) {
	final var finalFrame = DataLoader.loadFrame(objectID);
	if (finalFrame > 1) {
	    return 1;
	}
	return 0;
    }

    public static int getLastFrameNumber(final GameObjectID objectID) {
	return DataLoader.loadFrame(objectID);
    }

    public static int getLayer(final GameObjectID objectID) {
	return DataLoader.loadLayer(objectID);
    }

    public static Material getMaterial(final GameObjectID objectID) {
	return DataLoader.loadMaterial(objectID);
    }

    public static GameObjectID getTransform(final GameObjectID objectID, final Material materialID) {
	return DataLoader.loadTransform(objectID, materialID);
    }

    public static Direction[] getValidDirections(final GameObjectID objectID) {
	return DataLoader.loadDirection(objectID);
    }

    public static int[] getValidIndexes(final GameObjectID objectID) {
	return DataLoader.loadIndex(objectID);
    }

    public static int getWeight(final GameObjectID objectID) {
	return DataLoader.loadWeight(objectID);
    }

    public static boolean hasDirection(final GameObjectID objectID) {
	return DataLoader.loadDirection(objectID).length > 1;
    }

    public static boolean hasFriction(final GameObjectID objectID) {
	return DataLoader.loadFriction(objectID, Direction.NONE);
    }

    public static boolean isAnimated(final GameObjectID objectID) {
	final var finalFrame = DataLoader.loadFrame(objectID);
	if (finalFrame > 1) {
	    return true;
	}
	return false;
    }

    public static boolean isPushable(final GameObjectID objectID) {
	return DataLoader.loadMovable(objectID, Direction.NONE);
    }

    public static boolean isReflective(final GameObjectID objectID, final Direction dir) {
	return DataLoader.loadReflect(objectID, dir);
    }

    public static boolean isSolid(final GameObjectID objectID) {
	return DataLoader.loadSolid(objectID, Direction.NONE);
    }

    public static boolean killsOnMove(final GameObjectID objectID) {
	return DataLoader.loadLethal(objectID, Direction.NONE);
    }

    public static boolean rotates(final GameObjectID objectID, final Direction dir) {
	return DataLoader.loadRotate(objectID, dir);
    }

    public static boolean solvesOnMove(final GameObjectID objectID) {
	return objectID == GameObjectID.FLAG;
    }
}
