package com.puttysoftware.lasertank.arena;

import java.io.IOException;

import com.puttysoftware.lasertank.arena.objects.ArenaObject;
import com.puttysoftware.lasertank.fileio.DataIOReader;
import com.puttysoftware.lasertank.fileio.DataIOWriter;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameAction;
import com.puttysoftware.lasertank.index.GameFormat;
import com.puttysoftware.lasertank.index.RangeType;

public abstract class ArenaData {
    // Constants
    final static int MIN_FLOORS = 1;
    protected final static int MAX_FLOORS = 9;
    final static int MIN_COLUMNS = 24;
    final static int MIN_ROWS = 24;
    public final static int TUNNEL_SCAN_RADIUS = MIN_ROWS;

    public static int getMaxFloors() {
	return ArenaData.MAX_FLOORS;
    }

    public static int getMinColumns() {
	return ArenaData.MIN_COLUMNS;
    }

    public static int getMinFloors() {
	return ArenaData.MIN_FLOORS;
    }

    // Static methods
    public static int getMinRows() {
	return ArenaData.MIN_ROWS;
    }

    public abstract void checkForEnemies(final Arena arena, final int floorIn, final int enemyLocXIn,
	    final int enemyLocYIn, final ArenaObject enemy);

    public abstract int checkForMagnetic(final Arena arena, final int floor, final int centerX, final int centerY,
	    final Direction dir);

    public abstract int[] circularScan(final Arena arena, final int xIn, final int yIn, final int zIn, final int r,
	    final String targetName, boolean moved);

    public abstract void circularScanRange(final Arena arena, final int xIn, final int yIn, final int zIn, final int r,
	    final RangeType rangeType, final int forceUnits);

    public abstract boolean circularScanTank(final Arena arena, final int x, final int y, final int z, final int r);

    public abstract int[] circularScanTunnel(final Arena arena, final int x, final int y, final int z, final int maxR,
	    final int tx, final int ty, final ArenaObject target, final boolean moved);

    public abstract void clearDirtyFlags(final int floor);

    public abstract void clearRedoHistory();

    public abstract void clearUndoHistory();

    public abstract void clearVirtualGrid(final Arena arena);

    public abstract void fill(final Arena arena, final ArenaObject fillWith);

    public abstract void fillNulls(final Arena arena, final ArenaObject fill1, final ArenaObject fill2,
	    final boolean was16);

    public abstract void fillSTSNulls(final ArenaObject fillWith);

    public abstract void fillVirtual();

    public abstract int[] findObject(final Arena arena, final int z, final ArenaObject target);

    public abstract int[] findPlayer(final Arena arena, final int number);

    public abstract void fullScanFindLostPair(final Arena arena, final int zIn, final ArenaObject door);

    public abstract void fullScanFreezeGround(final Arena arena);

    public abstract void fullScanKillTanks(final Arena arena);

    public abstract void fullScanPairBind(final Arena arena, final int dx, final int dy, final int zIn,
	    final ArenaObject source);

    public abstract void fullScanPairCleanup(final Arena arena, final int px, final int py, final int zIn,
	    final ArenaObject button);

    public abstract void fullScanPairClose(final Arena arena, final int zIn, final ArenaObject source);

    public abstract void fullScanPairOpen(final Arena arena, final int zIn, final ArenaObject source);

    public abstract ArenaObject getCell(final Arena arena, final int row, final int col, final int floor,
	    final int layer);

    public abstract int getColumns();

    public abstract int getFloors();

    public abstract int getRows();

    public abstract ArenaObject getVirtualCell(final Arena arena, final int row, final int col, final int floor,
	    final int layer);

    public abstract HistoryStatus getWhatWas();

    public abstract boolean isCellDirty(final Arena arena, final int row, final int col, final int floor);

    public abstract boolean linearScan(final Arena arena, final int xIn, final int yIn, final int zIn,
	    final Direction d);

    public abstract int linearScanMagnetic(final Arena arena, final int xIn, final int yIn, final int zIn,
	    final Direction d);

    public abstract void markAsDirty(final Arena arena, final int row, final int col, final int floor);

    protected final int normalizeColumn(final int column) {
	var fC = column;
	if (fC < 0) {
	    fC += this.getColumns();
	    while (fC < 0) {
		fC += this.getColumns();
	    }
	} else if (fC > this.getColumns() - 1) {
	    fC -= this.getColumns();
	    while (fC > this.getColumns() - 1) {
		fC -= this.getColumns();
	    }
	}
	return fC;
    }

    protected final int normalizeFloor(final int floor) {
	var fF = floor;
	if (fF < 0) {
	    fF += this.getFloors();
	    while (fF < 0) {
		fF += this.getFloors();
	    }
	} else if (fF > this.getFloors() - 1) {
	    fF -= this.getFloors();
	    while (fF > this.getFloors() - 1) {
		fF -= this.getFloors();
	    }
	}
	return fF;
    }

    protected final int normalizeRow(final int row) {
	var fR = row;
	if (fR < 0) {
	    fR += this.getRows();
	    while (fR < 0) {
		fR += this.getRows();
	    }
	} else if (fR > this.getRows() - 1) {
	    fR -= this.getRows();
	    while (fR > this.getRows() - 1) {
		fR -= this.getRows();
	    }
	}
	return fR;
    }

    public abstract ArenaData readData(final Arena arena, final DataIOReader reader, final GameFormat formatVersion)
	    throws IOException;

    public abstract void readSavedState(final DataIOReader reader, final GameFormat formatVersion) throws IOException;

    public abstract void redo(final Arena arena);

    public abstract void resetHistoryEngine();

    public abstract void resize(final Arena arena, final int zIn, final ArenaObject nullFill);

    public abstract void resizeSavedState(final int z, final ArenaObject nullFill);

    public abstract void restore(final Arena arena);

    public abstract void save(final Arena arena);

    public abstract void setAllDirtyFlags();

    public abstract void setCell(final Arena arena, final ArenaObject mo, final int row, final int col, final int floor,
	    final int layer);

    public abstract void setDirtyFlags(final int floor);

    public abstract void setVirtualCell(final Arena arena, final ArenaObject mo, final int row, final int col,
	    final int floor, final int layer);

    public abstract void tickTimers(final Arena arena, final int floor, final GameAction actionType);

    public abstract boolean tryRedo();

    public abstract boolean tryUndo();

    public abstract void undo(final Arena arena);

    public abstract void updateRedoHistory(final HistoryStatus whatIs);

    public abstract void updateUndoHistory(final HistoryStatus whatIs);

    public abstract void writeData(final Arena arena, final DataIOWriter writer) throws IOException;

    public abstract void writeSavedState(final DataIOWriter writer) throws IOException;
}
