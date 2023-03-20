package com.puttysoftware.lasertank.datatype;

import com.puttysoftware.lasertank.helper.DirectionHelper;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameAction;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.Material;

public class GameObjectData {
    private static final int PLASTIC_FORCE = 0;
    private static final int DEFAULT_FORCE = 1;
    private static final int METAL_FORCE = 2;

    public static final int getImbuedForce(final Material materialID) {
        if (materialID == Material.PLASTIC) {
            return GameObjectData.PLASTIC_FORCE;
        }
        if (materialID == Material.METALLIC) {
            return GameObjectData.METAL_FORCE;
        }
        return GameObjectData.DEFAULT_FORCE;
    }

    public static final int getMinimumReactionForce(final Material materialID) {
        if (materialID == Material.PLASTIC) {
            return GameObjectData.PLASTIC_FORCE;
        }
        if (materialID == Material.METALLIC) {
            return GameObjectData.METAL_FORCE;
        }
        return GameObjectData.DEFAULT_FORCE;
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

    public static GameObjectID attributeRenderHook(final GameObjectID objectID) {
        return DataLoader.loadAttributeRender(objectID);
    }

    public static boolean canCloak(final GameObjectID objectID) {
        return DataLoader.loadCloak(objectID);
    }

    public static boolean canControl(final GameObjectID objectID) {
        return DataLoader.loadControl(objectID);
    }
    
    public static boolean canMove(final GameObjectID objectID) {
        return DataLoader.loadMovable(objectID, Direction.NONE);
    }

    public static boolean canRoll(final GameObjectID objectID) {
        return DataLoader.loadRoll(objectID);
    }

    public static boolean canShoot(final GameObjectID objectID) {
        return DataLoader.loadShoot(objectID);
    }

    public static int getBlockHeight(final GameObjectID objectID) {
        return DataLoader.loadHeight(objectID);
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

    public static GameObjectID getPairedObjectID(final GameObjectID objectID) {
        return DataLoader.loadPair(objectID);
    }

    public static Direction[] getValidDirections(final GameObjectID objectID) {
        return DataLoader.loadDirection(objectID);
    }

    public static int[] getValidIndexes(final GameObjectID objectID) {
        return DataLoader.loadIndex(objectID);
    }

    public static boolean hasDirection(final GameObjectID objectID) {
        return DataLoader.loadDirection(objectID).length > 1;
    }

    public static boolean hasFriction(final GameObjectID objectID) {
        return DataLoader.loadFriction(objectID, Direction.NONE);
    }

    public static boolean isAnimated(final GameObjectID objectID) {
        return DataLoader.loadFrame(objectID) > 1;
    }

    public static boolean isBox(final GameObjectID objectID) {
        return DataLoader.loadBox(objectID);
    }

    public static boolean isHostile(final GameObjectID objectID) {
        return DataLoader.loadHostile(objectID);
    }

    public static boolean isMovableMirror(final GameObjectID objectID, final Direction dir) {
        return DataLoader.loadMovableMirror(objectID, dir);
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

    public static boolean killsOnMove(final GameObjectID objectID, final int index) {
        return DataLoader.loadLethal(objectID, index);
    }

    public static boolean movesBoxes(final GameObjectID objectID, final Direction dir) {
        return DataLoader.loadMovesBoxes(objectID, dir);
    }

    public static boolean movesHostiles(final GameObjectID objectID, final Direction dir) {
        return DataLoader.loadMovesHostiles(objectID, dir);
    }

    public static boolean movesMirrors(final GameObjectID objectID, final Direction dir) {
        return DataLoader.loadMovesMirrors(objectID, dir);
    }

    public static boolean movesTanks(final GameObjectID objectID, final Direction dir) {
        return DataLoader.loadMovesTanks(objectID, dir);
    }

    public static int navigatesToOnMove(final GameObjectID objectID) {
        return DataLoader.loadNavigate(objectID);
    }

    public static boolean solvesOnMove(final GameObjectID objectID) {
        return objectID == GameObjectID.FLAG;
    }

    public static boolean usesTrigger(final GameObjectID objectID) {
        return DataLoader.loadTrigger(objectID);
    }
}
