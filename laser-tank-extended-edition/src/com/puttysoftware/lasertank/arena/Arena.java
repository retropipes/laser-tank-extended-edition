/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena;

import java.io.File;
import java.io.IOException;

import com.puttysoftware.lasertank.arena.abc.ArenaObject;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameAction;
import com.puttysoftware.lasertank.index.RangeType;
import com.puttysoftware.lasertank.locale.global.GlobalStrings;
import com.puttysoftware.lasertank.locale.global.UntranslatedString;

public abstract class Arena {
    // Constants
    private static final int MIN_LEVELS = 1;
    protected static final int MAX_LEVELS = Integer.MAX_VALUE;
    protected static final int ERA_COUNT = 5;

    // Static methods
    public static String getArenaTempFolder() {
        return System.getProperty(GlobalStrings.loadUntranslated(UntranslatedString.TEMP_DIR)) + File.separator
                + GlobalStrings.loadUntranslated(UntranslatedString.PROGRAM_NAME);
    }

    public static int getMaxFloors() {
        return ArenaData.getMaxFloors();
    }

    public static int getMaxLevels() {
        return Arena.MAX_LEVELS;
    }

    public static int getMinColumns() {
        return ArenaData.getMinColumns();
    }

    public static int getMinFloors() {
        return ArenaData.getMinFloors();
    }

    public static int getMinLevels() {
        return Arena.MIN_LEVELS;
    }

    public static int getMinRows() {
        return ArenaData.getMinRows();
    }

    public static int getStartLevel() {
        return 0;
    }

    // Constructors
    public Arena() {
        // Do nothing
    }

    public abstract boolean addLevel();

    public abstract void checkForEnemies(final int floor, final int ex, final int ey, final ArenaObject e);

    public abstract int checkForMagnetic(int floor, int centerX, int centerY, Direction dir);

    public abstract int[] circularScan(final int x, final int y, final int z, final int maxR, final String targetName,
            final boolean moved);

    public abstract void circularScanRange(final int x, final int y, final int z, final int maxR,
            final RangeType rangeType, final int forceUnits);

    public abstract boolean circularScanTank(final int x, final int y, final int z, final int maxR);

    public abstract int[] circularScanTunnel(final int x, final int y, final int z, final int maxR, final int tx,
            final int ty, final ArenaObject target, final boolean moved);

    public abstract void clearDirtyFlags(int floor);

    public abstract void clearVirtualGrid();

    public abstract void copyLevel();

    public abstract void cutLevel();

    public abstract void disableHorizontalWraparound();

    public abstract void disableThirdDimensionWraparound();

    public abstract void disableVerticalWraparound();

    public abstract boolean doesLevelExist(int level);

    public abstract boolean doesLevelExistOffset(int level);

    public abstract boolean doesPlayerExist(final int pi);

    public abstract void enableHorizontalWraparound();

    public abstract void enableThirdDimensionWraparound();

    public abstract void enableVerticalWraparound();

    public abstract void fillDefault();

    public abstract int[] findObject(int z, ArenaObject target);

    public abstract int[] findPlayer(final int number);

    public abstract void fullScanPairClose(int z, ArenaObject source);

    public abstract void fullScanPairOpen(int z, ArenaObject source);

    public abstract void fullScanPairBind(int dx, int dy, int z, ArenaObject source);

    public abstract void fullScanPairCleanup(int px, int py, int z, ArenaObject button);

    public abstract void fullScanFindLostPair(int z, ArenaObject door);

    public abstract void fullScanFreezeGround();

    public abstract void fullScanKillTanks();

    public abstract void generateLevelInfoList();

    public abstract int getActiveEraNumber();

    public abstract int getActiveLevelNumber();

    // Methods
    public abstract String getArenaTempMusicFolder();

    public abstract String getAuthor();

    public abstract String getBasePath();

    public abstract ArenaObject getCell(final int row, final int col, final int floor, final int layer);

    public abstract int getColumns();

    public abstract int getDifficulty();

    public abstract int getFloors();

    public abstract String getHint();

    public abstract String[] getLevelInfoList();

    public abstract int getLevels();

    public abstract String getMusicFilename();

    public abstract String getName();

    public abstract int getRows();

    public abstract int getStartColumn(final int pi);

    public abstract int getStartFloor(final int pi);

    public abstract int getStartRow(final int pi);

    public abstract ArenaObject getVirtualCell(final int row, final int col, final int floor, final int layer);

    public abstract HistoryStatus getWhatWas();

    public abstract boolean insertLevelFromClipboard();

    public abstract boolean isCellDirty(final int row, final int col, final int floor);

    public abstract boolean isCutBlocked();

    public abstract boolean isHorizontalWraparoundEnabled();

    public abstract boolean isMoveShootAllowed();

    public abstract boolean isMoveShootAllowedGlobally();

    public abstract boolean isMoveShootAllowedThisLevel();

    public abstract boolean isPasteBlocked();

    public abstract boolean isThirdDimensionWraparoundEnabled();

    public abstract boolean isVerticalWraparoundEnabled();

    public abstract void markAsDirty(final int row, final int col, final int floor);

    public abstract void pasteLevel();

    public abstract Arena readArena() throws IOException;

    public abstract void redo();

    protected abstract boolean removeActiveLevel();

    public final boolean removeLevel(final int num) {
        final var saveLevel = this.getActiveLevelNumber();
        this.switchLevel(num);
        final var success = this.removeActiveLevel();
        if (success) {
            if (saveLevel == 0) {
                // Was at first level
                this.switchLevel(0);
            } else // Was at level other than first
            if (saveLevel > num) {
                // Saved level was shifted down
                this.switchLevel(saveLevel - 1);
            } else if (saveLevel < num) {
                // Saved level was NOT shifted down
                this.switchLevel(saveLevel);
            } else {
                // Saved level was deleted
                this.switchLevel(0);
            }
        } else {
            this.switchLevel(saveLevel);
        }
        return success;
    }

    public abstract void resetHistoryEngine();

    public abstract void resize(int z, ArenaObject nullFill);

    public abstract void restore();

    public abstract void save();

    public abstract void setAuthor(String newAuthor);

    public abstract void setCell(final ArenaObject mo, final int row, final int col, final int floor,
            final int layer);

    public abstract void setData(ArenaData newData, int count);

    public abstract void setDifficulty(int newDifficulty);

    public abstract void setDirtyFlags(int floor);

    public abstract void setHint(String newHint);

    public abstract void setMoveShootAllowedGlobally(boolean value);

    public abstract void setMoveShootAllowedThisLevel(boolean value);

    public abstract void setMusicFilename(final String newMusicFilename);

    public abstract void setName(String newName);

    public abstract void setPrefixHandler(DataIOPrefixHandler xph);

    public abstract void setStartColumn(final int pi, final int newStartColumn);

    public abstract void setStartFloor(final int pi, final int newStartFloor);

    public abstract void setStartRow(final int pi, final int newStartRow);

    public abstract void setSuffixHandler(DataIOSuffixHandler xsh);

    public abstract void setVirtualCell(final ArenaObject mo, final int row, final int col, final int floor,
            final int layer);

    public abstract void switchEra(final int era);

    public abstract void switchEraOffset(final int era);

    protected abstract void switchInternal(int level, int era);

    public abstract void switchLevel(int level);

    public abstract void switchLevelOffset(int level);

    public final boolean switchToNextLevelWithDifficulty(final int[] difficulty) {
        var keepGoing = true;
        while (keepGoing) {
            final var diff = this.getDifficulty();
            for (final int element : difficulty) {
                if (diff - 1 == element) {
                    keepGoing = false;
                    return true;
                }
            }
            if (!this.doesLevelExistOffset(1)) {
                keepGoing = false;
                return false;
            }
            if (keepGoing) {
                this.switchLevelOffset(1);
            }
        }
        return false;
    }

    public final boolean switchToPreviousLevelWithDifficulty(final int[] difficulty) {
        var keepGoing = true;
        while (keepGoing) {
            final var diff = this.getDifficulty();
            for (final int element : difficulty) {
                if (diff - 1 == element) {
                    keepGoing = false;
                    return true;
                }
            }
            if (!this.doesLevelExistOffset(-1)) {
                keepGoing = false;
                return false;
            }
            if (keepGoing) {
                this.switchLevelOffset(-1);
            }
        }
        return false;
    }

    public abstract void tickTimers(final int floor, final GameAction actionType);

    public abstract boolean tryRedo();

    public abstract boolean tryUndo();

    public abstract void undo();

    public abstract void updateRedoHistory(final HistoryStatus whatIs);

    public abstract void updateUndoHistory(final HistoryStatus whatIs);

    public abstract void writeArena() throws IOException;
}