/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.current;

import java.io.IOException;
import java.util.ArrayDeque;

import com.puttysoftware.diane.fileio.DataIOReader;
import com.puttysoftware.diane.fileio.DataIOWriter;
import com.puttysoftware.diane.storage.FlagStorage;
import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.arena.Arena;
import com.puttysoftware.lasertank.arena.ArenaData;
import com.puttysoftware.lasertank.arena.HistoryStatus;
import com.puttysoftware.lasertank.arena.objects.ArenaObject;
import com.puttysoftware.lasertank.arena.objects.DeadAntiTank;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
import com.puttysoftware.lasertank.helper.DirectionHelper;
import com.puttysoftware.lasertank.helper.GameFormatHelper;
import com.puttysoftware.lasertank.helper.LayerHelper;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameAction;
import com.puttysoftware.lasertank.index.GameFormat;
import com.puttysoftware.lasertank.index.Layer;
import com.puttysoftware.lasertank.index.Material;
import com.puttysoftware.lasertank.index.RangeType;
import com.puttysoftware.lasertank.locale.ErrorString;
import com.puttysoftware.lasertank.locale.Strings;
import com.puttysoftware.lasertank.utility.ArenaObjectList;

public final class CurrentArenaData extends ArenaData {
	private class ImageUndoEngine {
		// Inner classes
		private class HistoryEntry {
			// Fields
			private final CurrentArenaStorage histImage;
			private final HistoryStatus histWhatWas;

			HistoryEntry(final CurrentArenaStorage i, final HistoryStatus hww) {
				this.histImage = i;
				this.histWhatWas = hww;
			}

			public CurrentArenaStorage getImage() {
				return this.histImage;
			}

			public HistoryStatus getWhatWas() {
				return this.histWhatWas;
			}
		}

		private class HistoryStack {
			// Fields
			private final ArrayDeque<HistoryEntry> stack;

			HistoryStack() {
				this.stack = new ArrayDeque<>();
			}

			public boolean isEmpty() {
				return this.stack.isEmpty();
			}

			public HistoryEntry pop() {
				return this.stack.removeFirst();
			}

			public void push(final CurrentArenaStorage i, final HistoryStatus hww) {
				final var newEntry = new HistoryEntry(i, hww);
				this.stack.addFirst(newEntry);
			}
		}

		// Fields
		private HistoryStack undoHistory, redoHistory;
		private HistoryStatus whatWas;
		private CurrentArenaStorage image;

		// Constructors
		public ImageUndoEngine() {
			this.undoHistory = new HistoryStack();
			this.redoHistory = new HistoryStack();
			this.image = null;
			this.whatWas = null;
		}

		public void clearRedoHistory() {
			this.redoHistory = new HistoryStack();
		}

		public void clearUndoHistory() {
			this.undoHistory = new HistoryStack();
		}

		public CurrentArenaStorage getImage() {
			return this.image;
		}

		public HistoryStatus getWhatWas() {
			return this.whatWas;
		}

		public void redo() {
			if (!this.redoHistory.isEmpty()) {
				final var entry = this.redoHistory.pop();
				this.image = entry.getImage();
				this.whatWas = entry.getWhatWas();
			} else {
				this.image = null;
				this.whatWas = null;
			}
		}

		public boolean tryRedo() {
			return !this.redoHistory.isEmpty();
		}

		public boolean tryUndo() {
			return !this.undoHistory.isEmpty();
		}

		// Public methods
		public void undo() {
			if (!this.undoHistory.isEmpty()) {
				final var entry = this.undoHistory.pop();
				this.image = entry.getImage();
				this.whatWas = entry.getWhatWas();
			} else {
				this.image = null;
				this.whatWas = null;
			}
		}

		public void updateRedoHistory(final CurrentArenaStorage newImage, final HistoryStatus newWhatWas) {
			this.redoHistory.push(newImage, newWhatWas);
		}

		public void updateUndoHistory(final CurrentArenaStorage newImage, final HistoryStatus newWhatWas) {
			this.undoHistory.push(newImage, newWhatWas);
		}
	}

	public static final CurrentArenaLock LOCK_OBJECT = new CurrentArenaLock();

	private static CurrentArenaData readDataG1(final Arena arena, final DataIOReader reader,
			final GameFormat formatVersion) throws IOException {
		int y, x, z, arenaSizeX, arenaSizeY, arenaSizeZ;
		arenaSizeX = reader.readInt();
		arenaSizeY = reader.readInt();
		arenaSizeZ = reader.readInt();
		final var lt = new CurrentArenaData();
		for (x = 0; x < arenaSizeX; x++) {
			for (y = 0; y < arenaSizeY; y++) {
				for (z = 0; z < arenaSizeZ; z++) {
					final var obj = ArenaObjectList.readArenaObjectG2(reader, formatVersion);
					lt.setCell(arena, obj, y, x, z, obj.getLayer());
				}
			}
		}
		arena.setStartColumn(0, reader.readInt());
		arena.setStartRow(0, reader.readInt());
		arena.setStartFloor(0, reader.readInt());
		final var horzWrap = reader.readBoolean();
		if (horzWrap) {
			arena.enableHorizontalWraparound();
		} else {
			arena.disableHorizontalWraparound();
		}
		final var vertWrap = reader.readBoolean();
		if (vertWrap) {
			arena.enableVerticalWraparound();
		} else {
			arena.disableVerticalWraparound();
		}
		arena.disableThirdDimensionWraparound();
		arena.setName(reader.readString());
		arena.setHint(reader.readString());
		arena.setAuthor(reader.readString());
		arena.setDifficulty(reader.readInt());
		arena.setMoveShootAllowedThisLevel(false);
		// Fill nulls
		lt.fillNulls(arena, new ArenaObject(GameObjectID.GROUND), new ArenaObject(GameObjectID.WALL), true);
		lt.fillVirtual();
		return lt;
	}

	private static CurrentArenaData readDataG2(final Arena arena, final DataIOReader reader,
			final GameFormat formatVersion) throws IOException {
		int y, x, z, arenaSizeX, arenaSizeY, arenaSizeZ;
		arenaSizeX = reader.readInt();
		arenaSizeY = reader.readInt();
		arenaSizeZ = reader.readInt();
		final var lt = new CurrentArenaData();
		lt.resize(arena, arenaSizeZ, new ArenaObject(GameObjectID.GROUND));
		for (x = 0; x < arenaSizeX; x++) {
			for (y = 0; y < arenaSizeY; y++) {
				for (z = 0; z < arenaSizeZ; z++) {
					final var obj = ArenaObjectList.readArenaObjectG2(reader, formatVersion);
					lt.setCell(arena, obj, y, x, z, obj.getLayer());
				}
			}
		}
		arena.setStartColumn(0, reader.readInt());
		arena.setStartRow(0, reader.readInt());
		arena.setStartFloor(0, reader.readInt());
		final var horzWrap = reader.readBoolean();
		if (horzWrap) {
			arena.enableHorizontalWraparound();
		} else {
			arena.disableHorizontalWraparound();
		}
		final var vertWrap = reader.readBoolean();
		if (vertWrap) {
			arena.enableVerticalWraparound();
		} else {
			arena.disableVerticalWraparound();
		}
		final var thirdWrap = reader.readBoolean();
		if (thirdWrap) {
			arena.enableThirdDimensionWraparound();
		} else {
			arena.disableThirdDimensionWraparound();
		}
		arena.setName(reader.readString());
		arena.setHint(reader.readString());
		arena.setAuthor(reader.readString());
		arena.setDifficulty(reader.readInt());
		arena.setMoveShootAllowedThisLevel(false);
		// Fill nulls
		lt.fillNulls(arena, new ArenaObject(GameObjectID.GROUND), null, false);
		lt.fillVirtual();
		return lt;
	}

	private static CurrentArenaData readDataG3(final Arena arena, final DataIOReader reader,
			final GameFormat formatVersion) throws IOException {
		int y, x, z, arenaSizeX, arenaSizeY, arenaSizeZ;
		arenaSizeX = reader.readInt();
		arenaSizeY = reader.readInt();
		arenaSizeZ = reader.readInt();
		final var lt = new CurrentArenaData();
		lt.resize(arena, arenaSizeZ, new ArenaObject(GameObjectID.GROUND));
		for (x = 0; x < arenaSizeX; x++) {
			for (y = 0; y < arenaSizeY; y++) {
				for (z = 0; z < arenaSizeZ; z++) {
					final var obj = ArenaObjectList.readArenaObjectG3(reader, formatVersion);
					lt.setCell(arena, obj, y, x, z, obj.getLayer());
				}
			}
		}
		arena.setStartColumn(0, reader.readInt());
		arena.setStartRow(0, reader.readInt());
		arena.setStartFloor(0, reader.readInt());
		final var horzWrap = reader.readBoolean();
		if (horzWrap) {
			arena.enableHorizontalWraparound();
		} else {
			arena.disableHorizontalWraparound();
		}
		final var vertWrap = reader.readBoolean();
		if (vertWrap) {
			arena.enableVerticalWraparound();
		} else {
			arena.disableVerticalWraparound();
		}
		final var thirdWrap = reader.readBoolean();
		if (thirdWrap) {
			arena.enableThirdDimensionWraparound();
		} else {
			arena.disableThirdDimensionWraparound();
		}
		arena.setName(reader.readString());
		arena.setHint(reader.readString());
		arena.setAuthor(reader.readString());
		arena.setDifficulty(reader.readInt());
		arena.setMoveShootAllowedThisLevel(false);
		// Fill nulls
		lt.fillNulls(arena, new ArenaObject(GameObjectID.GROUND), null, false);
		lt.fillVirtual();
		return lt;
	}

	private static CurrentArenaData readDataG4(final Arena arena, final DataIOReader reader,
			final GameFormat formatVersion) throws IOException {
		int y, x, z, arenaSizeX, arenaSizeY, arenaSizeZ;
		arenaSizeX = reader.readInt();
		arenaSizeY = reader.readInt();
		arenaSizeZ = reader.readInt();
		final var lt = new CurrentArenaData();
		lt.resize(arena, arenaSizeZ, new ArenaObject(GameObjectID.GROUND));
		for (x = 0; x < arenaSizeX; x++) {
			for (y = 0; y < arenaSizeY; y++) {
				for (z = 0; z < arenaSizeZ; z++) {
					final var obj = ArenaObjectList.readArenaObjectG4(reader, formatVersion);
					lt.setCell(arena, obj, y, x, z, obj.getLayer());
				}
			}
		}
		arena.setStartColumn(0, reader.readInt());
		arena.setStartRow(0, reader.readInt());
		arena.setStartFloor(0, reader.readInt());
		final var horzWrap = reader.readBoolean();
		if (horzWrap) {
			arena.enableHorizontalWraparound();
		} else {
			arena.disableHorizontalWraparound();
		}
		final var vertWrap = reader.readBoolean();
		if (vertWrap) {
			arena.enableVerticalWraparound();
		} else {
			arena.disableVerticalWraparound();
		}
		final var thirdWrap = reader.readBoolean();
		if (thirdWrap) {
			arena.enableThirdDimensionWraparound();
		} else {
			arena.disableThirdDimensionWraparound();
		}
		arena.setName(reader.readString());
		arena.setHint(reader.readString());
		arena.setAuthor(reader.readString());
		arena.setDifficulty(reader.readInt());
		arena.setMoveShootAllowedThisLevel(false);
		// Fill nulls
		lt.fillNulls(arena, new ArenaObject(GameObjectID.GROUND), null, false);
		lt.fillVirtual();
		return lt;
	}

	private static CurrentArenaData readDataG5(final Arena arena, final DataIOReader reader,
			final GameFormat formatVersion) throws IOException {
		int y, x, z, w, arenaSizeX, arenaSizeY, arenaSizeZ;
		arenaSizeX = reader.readInt();
		arenaSizeY = reader.readInt();
		arenaSizeZ = reader.readInt();
		final var lt = new CurrentArenaData();
		lt.resize(arena, arenaSizeZ, new ArenaObject(GameObjectID.GROUND));
		for (x = 0; x < arenaSizeX; x++) {
			for (y = 0; y < arenaSizeY; y++) {
				for (z = 0; z < arenaSizeZ; z++) {
					for (w = 0; w < LayerHelper.COUNT; w++) {
						lt.setCell(arena, ArenaObjectList.readArenaObjectG5(reader, formatVersion), y, x, z, w);
					}
				}
			}
		}
		arena.setStartColumn(0, reader.readInt());
		arena.setStartRow(0, reader.readInt());
		arena.setStartFloor(0, reader.readInt());
		final var horzWrap = reader.readBoolean();
		if (horzWrap) {
			arena.enableHorizontalWraparound();
		} else {
			arena.disableHorizontalWraparound();
		}
		final var vertWrap = reader.readBoolean();
		if (vertWrap) {
			arena.enableVerticalWraparound();
		} else {
			arena.disableVerticalWraparound();
		}
		final var thirdWrap = reader.readBoolean();
		if (thirdWrap) {
			arena.enableThirdDimensionWraparound();
		} else {
			arena.disableThirdDimensionWraparound();
		}
		arena.setName(reader.readString());
		arena.setHint(reader.readString());
		arena.setAuthor(reader.readString());
		arena.setDifficulty(reader.readInt());
		arena.setMoveShootAllowedThisLevel(reader.readBoolean());
		// Fill nulls
		lt.fillNulls(arena, new ArenaObject(GameObjectID.GROUND), null, false);
		lt.fillVirtual();
		return lt;
	}

	private static CurrentArenaData readDataG6(final Arena arena, final DataIOReader reader,
			final GameFormat formatVersion) throws IOException {
		int y, x, z, w, arenaSizeX, arenaSizeY, arenaSizeZ;
		arenaSizeX = reader.readInt();
		arenaSizeY = reader.readInt();
		arenaSizeZ = reader.readInt();
		final var lt = new CurrentArenaData();
		lt.resize(arena, arenaSizeZ, new ArenaObject(GameObjectID.GROUND));
		for (x = 0; x < arenaSizeX; x++) {
			for (y = 0; y < arenaSizeY; y++) {
				for (z = 0; z < arenaSizeZ; z++) {
					for (w = 0; w < LayerHelper.COUNT; w++) {
						lt.setCell(arena, ArenaObjectList.readArenaObjectG6(reader, formatVersion), y, x, z, w);
					}
				}
			}
		}
		// Fill nulls
		lt.fillNulls(arena, new ArenaObject(GameObjectID.GROUND), null, false);
		lt.fillVirtual();
		return lt;
	}

	// Properties
	private CurrentArenaStorage data;
	private CurrentArenaStorage virtualData;
	private FlagStorage dirtyData;
	private CurrentArenaStorage savedState;
	private int foundX, foundY;
	private ImageUndoEngine iue;

	// Constructors
	public CurrentArenaData() {
		this.data = new CurrentArenaStorage(ArenaData.MIN_COLUMNS, ArenaData.MIN_ROWS, ArenaData.MIN_FLOORS,
				LayerHelper.COUNT);
		this.virtualData = new CurrentArenaStorage(ArenaData.MIN_COLUMNS, ArenaData.MIN_ROWS, ArenaData.MIN_FLOORS,
				LayerHelper.VIRTUAL_COUNT);
		this.fillVirtual();
		this.dirtyData = new FlagStorage(ArenaData.MIN_COLUMNS, ArenaData.MIN_ROWS, ArenaData.MIN_FLOORS);
		this.savedState = new CurrentArenaStorage(ArenaData.MIN_ROWS, ArenaData.MIN_COLUMNS, ArenaData.MIN_FLOORS,
				LayerHelper.COUNT);
		this.foundX = -1;
		this.foundY = -1;
		this.iue = new ImageUndoEngine();
	}

	@Override
	public void checkForEnemies(final Arena arena, final int floorIn, final int enemyLocXIn, final int enemyLocYIn,
			final ArenaObject enemy) {
		final var template = new ArenaObject(GameObjectID.ANTI_TANK);
		var enemyLocX = enemyLocXIn;
		var enemyLocY = enemyLocYIn;
		var floor = floorIn;
		if (arena.isVerticalWraparoundEnabled()) {
			enemyLocX = this.normalizeColumn(enemyLocX);
		}
		if (arena.isHorizontalWraparoundEnabled()) {
			enemyLocY = this.normalizeRow(enemyLocY);
		}
		if (arena.isThirdDimensionWraparoundEnabled()) {
			floor = this.normalizeFloor(floor);
		}
		final var scanE = this.linearScan(arena, enemyLocX, enemyLocY, floor, Direction.EAST);
		if (scanE) {
			final var at = this.getCell(arena, this.foundX, this.foundY, floor, template.getLayer());
			at.kill(this.foundX, this.foundY);
		}
		final var scanW = this.linearScan(arena, enemyLocX, enemyLocY, floor, Direction.WEST);
		if (scanW) {
			final var at = this.getCell(arena, this.foundX, this.foundY, floor, template.getLayer());
			at.kill(this.foundX, this.foundY);
		}
		final var scanS = this.linearScan(arena, enemyLocX, enemyLocY, floor, Direction.SOUTH);
		if (scanS) {
			final var at = this.getCell(arena, this.foundX, this.foundY, floor, template.getLayer());
			at.kill(this.foundX, this.foundY);
		}
		final var scanN = this.linearScan(arena, enemyLocX, enemyLocY, floor, Direction.NORTH);
		if (scanN) {
			final var at = this.getCell(arena, this.foundX, this.foundY, floor, template.getLayer());
			at.kill(this.foundX, this.foundY);
		}
	}

	@Override
	public int checkForMagnetic(final Arena arena, final int floor, final int centerX, final int centerY,
			final Direction dir) {
		if (dir == Direction.EAST) {
			return this.linearScanMagnetic(arena, centerX, centerY, floor, Direction.EAST);
		}
		if (dir == Direction.WEST) {
			return this.linearScanMagnetic(arena, centerX, centerY, floor, Direction.WEST);
		}
		if (dir == Direction.SOUTH) {
			return this.linearScanMagnetic(arena, centerX, centerY, floor, Direction.SOUTH);
		} else if (dir == Direction.NORTH) {
			return this.linearScanMagnetic(arena, centerX, centerY, floor, Direction.NORTH);
		}
		return 0;
	}

	@Override
	public int[] circularScan(final Arena arena, final int xIn, final int yIn, final int zIn, final int r,
			final String targetName, final boolean moved) {
		var xFix = xIn;
		var yFix = yIn;
		var zFix = zIn;
		if (arena.isVerticalWraparoundEnabled()) {
			xFix = this.normalizeColumn(xFix);
		}
		if (arena.isHorizontalWraparoundEnabled()) {
			yFix = this.normalizeRow(yFix);
		}
		if (arena.isThirdDimensionWraparoundEnabled()) {
			zFix = this.normalizeFloor(zFix);
		}
		int u, v, w;
		u = v = 0;
		// Perform the scan
		for (u = xFix - r; u <= xFix + r; u++) {
			for (v = yFix - r; v <= yFix + r; v++) {
				for (w = 0; w < LayerHelper.COUNT; w++) {
					try {
						final var obj = this.getCell(arena, v, u, zFix, w);
						final var savedObj = obj.getSavedObject();
						String testName;
						if (!obj.canControl() || moved) {
							testName = obj.getImageName();
						} else {
							testName = savedObj.getImageName();
						}
						if (testName.equals(targetName)) {
							return new int[] { v, u, zFix };
						}
					} catch (final ArrayIndexOutOfBoundsException aioob) {
						// Do nothing
					}
				}
			}
		}
		return null;
	}

	@Override
	public void circularScanRange(final Arena arena, final int xIn, final int yIn, final int zIn, final int r,
			final RangeType rangeType, final int forceUnits) {
		var xFix = xIn;
		var yFix = yIn;
		var zFix = zIn;
		if (arena.isVerticalWraparoundEnabled()) {
			xFix = this.normalizeColumn(xFix);
		}
		if (arena.isHorizontalWraparoundEnabled()) {
			yFix = this.normalizeRow(yFix);
		}
		if (arena.isThirdDimensionWraparoundEnabled()) {
			zFix = this.normalizeFloor(zFix);
		}
		int u, v, w;
		u = v = 0;
		// Perform the scan
		for (u = xFix - r; u <= xFix + r; u++) {
			for (v = yFix - r; v <= yFix + r; v++) {
				if (u == xFix && v == yFix) {
					continue;
				}
				for (w = 0; w < LayerHelper.COUNT; w++) {
					try {
						this.getCell(arena, u, v, zFix, w).rangeAction(xFix, yFix, zFix, u - xFix, v - yFix, rangeType,
								forceUnits);
					} catch (final ArrayIndexOutOfBoundsException aioob) {
						// Do nothing
					}
				}
			}
		}
	}

	@Override
	public boolean circularScanTank(final Arena arena, final int x, final int y, final int z, final int r) {
		final var tankLoc = LaserTankEE.getGame().getTankLocation();
		var fX = x;
		var fY = y;
		var fZ = z;
		if (arena.isVerticalWraparoundEnabled()) {
			fX = this.normalizeColumn(fX);
		}
		if (arena.isHorizontalWraparoundEnabled()) {
			fY = this.normalizeRow(fY);
		}
		if (arena.isThirdDimensionWraparoundEnabled()) {
			fZ = this.normalizeFloor(fZ);
		}
		final var tx = tankLoc[0];
		final var ty = tankLoc[1];
		final var tz = tankLoc[2];
		return fZ == tz && Math.abs(fX - tx) <= r && Math.abs(fY - ty) <= r;
	}

	@Override
	public int[] circularScanTunnel(final Arena arena, final int xIn, final int yIn, final int zIn, final int r,
			final int tx, final int ty, final ArenaObject target, final boolean moved) {
		var xFix = xIn;
		var yFix = yIn;
		var zFix = zIn;
		if (arena.isVerticalWraparoundEnabled()) {
			xFix = this.normalizeColumn(xFix);
		}
		if (arena.isHorizontalWraparoundEnabled()) {
			yFix = this.normalizeRow(yFix);
		}
		if (arena.isThirdDimensionWraparoundEnabled()) {
			zFix = this.normalizeFloor(zFix);
		}
		int u, v, w;
		w = Layer.LOWER_OBJECTS.ordinal();
		// Perform the scan
		for (u = xFix - r; u <= xFix + r; u++) {
			for (v = yFix - r; v <= yFix + r; v++) {
				if (v == tx && u == ty && moved) {
					continue;
				}
				if (v >= 0 && v < ArenaData.MIN_ROWS && u >= 0 && u < ArenaData.MIN_COLUMNS) {
					final var obj = this.getCell(arena, v, u, zFix, w);
					final var savedObj = obj.getSavedObject();
					ArenaObject test;
					if (obj.canControl()) {
						test = savedObj;
					} else {
						test = obj;
					}
					if (target.equals(test)) {
						return new int[] { v, u, zFix };
					}
				}
			}
		}
		return null;
	}

	@Override
	public void clearDirtyFlags(final int floor) {
		for (var row = 0; row < this.getRows(); row++) {
			for (var col = 0; col < this.getColumns(); col++) {
				this.dirtyData.setCell(false, col, row, floor);
			}
		}
	}

	@Override
	public void clearRedoHistory() {
		this.iue.clearRedoHistory();
	}

	@Override
	public void clearUndoHistory() {
		this.iue.clearUndoHistory();
	}

	@Override
	public void clearVirtualGrid(final Arena arena) {
		for (var row = 0; row < this.getRows(); row++) {
			for (var col = 0; col < this.getColumns(); col++) {
				for (var floor = 0; floor < this.getFloors(); floor++) {
					for (var layer = 0; layer < LayerHelper.VIRTUAL_COUNT; layer++) {
						this.setVirtualCell(arena, new ArenaObject(GameObjectID.PLACEHOLDER), row, col, floor, layer);
					}
				}
			}
		}
	}

	// Methods
	@Override
	public void fill(final Arena arena, final ArenaObject fill) {
		int y, x, z, w;
		for (x = 0; x < this.getColumns(); x++) {
			for (y = 0; y < this.getRows(); y++) {
				for (z = 0; z < this.getFloors(); z++) {
					for (w = 0; w < LayerHelper.COUNT; w++) {
						if (w == Layer.LOWER_GROUND.ordinal()) {
							this.setCell(arena, fill, y, x, z, w);
						} else {
							this.setCell(arena, new ArenaObject(GameObjectID.PLACEHOLDER), y, x, z, w);
						}
					}
				}
			}
		}
	}

	@Override
	public void fillNulls(final Arena arena, final ArenaObject fill1, final ArenaObject fill2,
			final boolean was16) {
		int y, x, z, w;
		for (x = 0; x < this.getColumns(); x++) {
			for (y = 0; y < this.getRows(); y++) {
				for (z = 0; z < this.getFloors(); z++) {
					for (w = 0; w < LayerHelper.COUNT; w++) {
						if (this.getCell(arena, y, x, z, w) == null) {
							if (w == Layer.LOWER_GROUND.ordinal()) {
								this.setCell(arena, fill1, y, x, z, w);
							} else if (w == Layer.LOWER_OBJECTS.ordinal() && was16 && (x >= 16 || y >= 16)) {
								this.setCell(arena, fill2, y, x, z, w);
							} else {
								this.setCell(arena, new ArenaObject(GameObjectID.PLACEHOLDER), y, x, z, w);
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void fillSTSNulls(final ArenaObject fill) {
		int y, x, z, w;
		for (x = 0; x < this.getColumns(); x++) {
			for (y = 0; y < this.getRows(); y++) {
				for (z = 0; z < this.getFloors(); z++) {
					for (w = 0; w < LayerHelper.COUNT; w++) {
						if (this.savedState.getCell(y, x, z, w) == null) {
							if (w == Layer.LOWER_GROUND.ordinal()) {
								this.savedState.setCell(fill, y, x, z, w);
							} else {
								this.savedState.setCell(new ArenaObject(GameObjectID.PLACEHOLDER), y, x, z, w);
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void fillVirtual() {
		int y, x, z, w;
		for (x = 0; x < this.getColumns(); x++) {
			for (y = 0; y < this.getRows(); y++) {
				for (z = 0; z < this.getFloors(); z++) {
					for (w = 0; w < LayerHelper.VIRTUAL_COUNT; w++) {
						this.virtualData.setCell(new ArenaObject(GameObjectID.PLACEHOLDER), y, x, z, w);
					}
				}
			}
		}
	}

	@Override
	public int[] findObject(final Arena arena, final int z, final ArenaObject target) {
		// Perform the scan
		for (var x = 0; x < ArenaData.MIN_COLUMNS; x++) {
			for (var y = 0; y < ArenaData.MIN_ROWS; y++) {
				for (var w = 0; w < LayerHelper.COUNT; w++) {
					try {
						final var obj = this.getCell(arena, x, y, z, w);
						if (target.equals(obj)) {
							return new int[] { x, y };
						}
					} catch (final ArrayIndexOutOfBoundsException aioob) {
						// Do nothing
					}
				}
			}
		}
		return null;
	}

	@Override
	public int[] findPlayer(final Arena arena, final int number) {
		final var t = new ArenaObject(GameObjectID.TANK, number);
		int y, x, z;
		for (x = 0; x < this.getColumns(); x++) {
			for (y = 0; y < this.getRows(); y++) {
				for (z = 0; z < this.getFloors(); z++) {
					final var mo = this.getCell(arena, y, x, z, t.getLayer());
					if (mo != null && t.equals(mo)) {
						return new int[] { y, x, z };
					}
				}
			}
		}
		return null;
	}

	@Override
	public void fullScanPairClose(final Arena arena, final int zIn, final ArenaObject source) {
		// Perform the scan
		var zFix = zIn;
		if (arena.isThirdDimensionWraparoundEnabled()) {
			zFix = this.normalizeFloor(zFix);
		}
		var flag = !source.isPairTriggered();
		for (var x = 0; x < ArenaData.MIN_COLUMNS; x++) {
			if (flag) {
				break;
			}
			for (var y = 0; y < ArenaData.MIN_ROWS; y++) {
				if (flag) {
					break;
				}
				final var obj = this.getCell(arena, y, x, zFix, source.getLayer());
				if (obj.getPairedWith() != null && obj.usesTrigger()
						&& (source.hasSamePair(obj) && !obj.isPairTriggered())) {
					flag = true;
				}
			}
		}
		if (flag) {
			// Scan said OK to proceed
			final var dx = source.getPairX();
			final var dy = source.getPairY();
			if (!this.getCell(arena, dx, dy, zFix, source.getLayer()).getClass()
					.equals(source.getPairedWith().getClass())) {
				this.setCell(arena, source.getPairedWith(), dx, dy, zFix, source.getLayer());
				Sounds.play(Sound.DOOR_CLOSES);
			}
		}
	}

	@Override
	public void fullScanPairOpen(final Arena arena, final int zIn, final ArenaObject source) {
		// Perform the scan
		var zFix = zIn;
		if (arena.isThirdDimensionWraparoundEnabled()) {
			zFix = this.normalizeFloor(zFix);
		}
		var flag = true;
		for (var x = 0; x < ArenaData.MIN_COLUMNS; x++) {
			if (!flag) {
				break;
			}
			for (var y = 0; y < ArenaData.MIN_ROWS; y++) {
				if (!flag) {
					break;
				}
				final var obj = this.getCell(arena, y, x, zFix, source.getLayer());
				if (obj.getPairedWith() != null && obj.usesTrigger()
						&& (source.hasSamePair(obj) && !obj.isPairTriggered())) {
					flag = false;
				}
			}
		}
		if (flag) {
			// Scan said OK to proceed
			final var dx = source.getPairX();
			final var dy = source.getPairY();
			if (!(this.getCell(arena, dx, dy, zFix, source.getLayer()).getID() == GameObjectID.GROUND)) {
				this.setCell(arena, new ArenaObject(GameObjectID.GROUND), dx, dy, zFix, source.getLayer());
				Sounds.play(Sound.DOOR_OPENS);
			}
		}
	}

	@Override
	public void fullScanPairBind(final Arena arena, final int dx, final int dy, final int zIn,
			final ArenaObject source) {
		// Perform the scan
		var z = zIn;
		if (arena.isThirdDimensionWraparoundEnabled()) {
			z = this.normalizeFloor(z);
		}
		for (var x = 0; x < ArenaData.MIN_COLUMNS; x++) {
			for (var y = 0; y < ArenaData.MIN_ROWS; y++) {
				final var obj = this.getCell(arena, x, y, z, source.getLayer());
				if (obj.getPairedWith() != null && obj.usesTrigger()
						&& source.getClass().equals(obj.getPairedWith().getClass())) {
					obj.setPairX(dx);
					obj.setPairY(dy);
					obj.setPairTriggered(false);
				}
			}
		}
		for (var x = 0; x < ArenaData.MIN_COLUMNS; x++) {
			for (var y = 0; y < ArenaData.MIN_ROWS; y++) {
				final var obj = this.getCell(arena, x, y, z, source.getLayer());
				if ((obj instanceof final ArenaObject door) && source.getClass().equals(door.getClass())) {
					this.setCell(arena, new ArenaObject(GameObjectID.GROUND), x, y, z, source.getLayer());
				}
			}
		}
	}

	@Override
	public void fullScanPairCleanup(final Arena arena, final int px, final int py, final int zIn,
			final ArenaObject source) {
		// Perform the scan
		var zFix = zIn;
		if (arena.isThirdDimensionWraparoundEnabled()) {
			zFix = this.normalizeFloor(zFix);
		}
		for (var x = 0; x < ArenaData.MIN_COLUMNS; x++) {
			for (var y = 0; y < ArenaData.MIN_ROWS; y++) {
				if (x == px && y == py) {
					continue;
				}
				final var obj = this.getCell(arena, x, y, zFix, source.getLayer());
				if (obj.getPairedWith() != null && obj.usesTrigger() && obj.hasSamePair(source)) {
					this.setCell(arena, new ArenaObject(GameObjectID.GROUND), x, y, zFix, source.getLayer());
				}
			}
		}
	}

	@Override
	public void fullScanFindLostPair(final Arena arena, final int zIn, final ArenaObject source) {
		// Perform the scan
		var zFix = zIn;
		if (arena.isThirdDimensionWraparoundEnabled()) {
			zFix = this.normalizeFloor(zFix);
		}
		for (var x = 0; x < ArenaData.MIN_COLUMNS; x++) {
			for (var y = 0; y < ArenaData.MIN_ROWS; y++) {
				final var obj = this.getCell(arena, x, y, zFix, source.getLayer());
				if (obj.getPairedWith() != null && obj.usesTrigger() && obj.isPairedWith(source)) {
					obj.setPairTriggered(true);
					return;
				}
			}
		}
	}

	@Override
	public void fullScanFreezeGround(final Arena arena) {
		// Perform the scan
		for (var x = 0; x < ArenaData.MIN_COLUMNS; x++) {
			for (var y = 0; y < ArenaData.MIN_ROWS; y++) {
				for (var z = 0; z < this.getFloors(); z++) {
					final var obj = this.getCell(arena, y, x, z, Layer.LOWER_GROUND.ordinal());
					if (obj.getID() != GameObjectID.GROUND) {
						// Freeze the ground
						LaserTankEE.getGame().morph(obj.changesToOnExposure(Material.ICE), y, x,
								z, Layer.LOWER_GROUND.ordinal());
					}
				}
			}
		}
	}

	@Override
	public void fullScanKillTanks(final Arena arena) {
		// Perform the scan
		for (var x = 0; x < ArenaData.MIN_COLUMNS; x++) {
			for (var y = 0; y < ArenaData.MIN_ROWS; y++) {
				for (var z = 0; z < this.getFloors(); z++) {
					final var obj = this.getCell(arena, y, x, z, Layer.LOWER_OBJECTS.ordinal());
					if (obj.getID() == GameObjectID.ANTI_TANK) {
						// Kill the tank
						final var gm = LaserTankEE.getGame();
						final var dat = new DeadAntiTank();
						dat.setSavedObject(obj.getSavedObject());
						dat.setDirection(obj.getDirection());
						gm.morph(dat, y, x, z, Layer.LOWER_OBJECTS.ordinal());
					}
				}
			}
		}
	}

	@Override
	public ArenaObject getCell(final Arena arena, final int row, final int col, final int floor,
			final int layer) {
		var fR = row;
		var fC = col;
		var fF = floor;
		if (arena.isVerticalWraparoundEnabled()) {
			fC = this.normalizeColumn(fC);
		}
		if (arena.isHorizontalWraparoundEnabled()) {
			fR = this.normalizeRow(fR);
		}
		if (arena.isThirdDimensionWraparoundEnabled()) {
			fF = this.normalizeFloor(fF);
		}
		return this.data.getArenaDataCell(fC, fR, fF, layer);
	}

	@Override
	public int getColumns() {
		return this.data.getShape()[0];
	}

	@Override
	public int getFloors() {
		return this.data.getShape()[2];
	}

	@Override
	public int getRows() {
		return this.data.getShape()[1];
	}

	@Override
	public ArenaObject getVirtualCell(final Arena arena, final int row, final int col, final int floor,
			final int layer) {
		var fR = row;
		var fC = col;
		var fF = floor;
		if (arena.isVerticalWraparoundEnabled()) {
			fC = this.normalizeColumn(fC);
		}
		if (arena.isHorizontalWraparoundEnabled()) {
			fR = this.normalizeRow(fR);
		}
		if (arena.isThirdDimensionWraparoundEnabled()) {
			fF = this.normalizeFloor(fF);
		}
		return this.virtualData.getArenaDataCell(fC, fR, fF, layer);
	}

	@Override
	public HistoryStatus getWhatWas() {
		return this.iue.getWhatWas();
	}

	@Override
	public boolean isCellDirty(final Arena arena, final int row, final int col, final int floor) {
		var fR = row;
		var fC = col;
		var fF = floor;
		if (arena.isVerticalWraparoundEnabled()) {
			fC = this.normalizeColumn(fC);
		}
		if (arena.isHorizontalWraparoundEnabled()) {
			fR = this.normalizeRow(fR);
		}
		if (arena.isThirdDimensionWraparoundEnabled()) {
			fF = this.normalizeFloor(fF);
		}
		return this.dirtyData.getCell(fC, fR, fF);
	}

	@Override
	public boolean linearScan(final Arena arena, final int xIn, final int yIn, final int zIn, final Direction d) {
		// Perform the scan
		var xFix = xIn;
		var yFix = yIn;
		var zFix = zIn;
		if (arena.isVerticalWraparoundEnabled()) {
			xFix = this.normalizeColumn(xFix);
		}
		if (arena.isHorizontalWraparoundEnabled()) {
			yFix = this.normalizeRow(yFix);
		}
		if (arena.isThirdDimensionWraparoundEnabled()) {
			zFix = this.normalizeFloor(zFix);
		}
		int u, w;
		if (d == Direction.NORTH) {
			final ArenaObject tank = LaserTankEE.getGame().getTank();
			if (tank.getSavedObject().isSolid()) {
				return false;
			}
			for (u = yFix - 1; u >= 0; u--) {
				for (w = 0; w < LayerHelper.COUNT; w++) {
					try {
						final var obj = this.getCell(arena, xFix, u, zFix, w);
						if (obj.isHostile()) {
							final var invert = DirectionHelper.invert(obj.getDirection());
							if (d == invert) {
								this.foundX = xFix;
								this.foundY = u;
								return true;
							}
						}
						if (obj.isSolid()) {
							return false;
						}
					} catch (final ArrayIndexOutOfBoundsException aioobe) {
						return false;
					}
				}
			}
			return false;
		}
		if (d == Direction.SOUTH) {
			final ArenaObject tank = LaserTankEE.getGame().getTank();
			if (tank.getSavedObject().isSolid()) {
				return false;
			}
			for (u = yFix + 1; u < 24; u++) {
				for (w = 0; w < LayerHelper.COUNT; w++) {
					try {
						final var obj = this.getCell(arena, xFix, u, zFix, w);
						if (obj.isHostile()) {
							final var invert = DirectionHelper.invert(obj.getDirection());
							if (d == invert) {
								this.foundX = xFix;
								this.foundY = u;
								return true;
							}
						}
						if (obj.isSolid()) {
							return false;
						}
					} catch (final ArrayIndexOutOfBoundsException aioobe) {
						return false;
					}
				}
			}
		} else if (d == Direction.WEST) {
			final ArenaObject tank = LaserTankEE.getGame().getTank();
			if (tank.getSavedObject().isSolid()) {
				return false;
			}
			for (u = xFix - 1; u >= 0; u--) {
				for (w = 0; w < LayerHelper.COUNT; w++) {
					try {
						final var obj = this.getCell(arena, u, yFix, zFix, w);
						if (obj.isHostile()) {
							final var invert = DirectionHelper.invert(obj.getDirection());
							if (d == invert) {
								this.foundX = u;
								this.foundY = yFix;
								return true;
							}
						}
						if (obj.isSolid()) {
							return false;
						}
					} catch (final ArrayIndexOutOfBoundsException aioobe) {
						return false;
					}
				}
			}
		} else if (d == Direction.EAST) {
			final ArenaObject tank = LaserTankEE.getGame().getTank();
			if (tank.getSavedObject().isSolid()) {
				return false;
			}
			for (u = xFix + 1; u < 24; u++) {
				for (w = 0; w < LayerHelper.COUNT; w++) {
					try {
						final var obj = this.getCell(arena, u, yFix, zFix, w);
						if (obj.isHostile()) {
							final var invert = DirectionHelper.invert(obj.getDirection());
							if (d == invert) {
								this.foundX = u;
								this.foundY = yFix;
								return true;
							}
						}
						if (obj.isSolid()) {
							return false;
						}
					} catch (final ArrayIndexOutOfBoundsException aioobe) {
						return false;
					}
				}
			}
		}
		return false;
	}

	@Override
	public int linearScanMagnetic(final Arena arena, final int xIn, final int yIn, final int zIn, final Direction d) {
		// Perform the scan
		var xFix = xIn;
		var yFix = yIn;
		var zFix = zIn;
		if (arena.isVerticalWraparoundEnabled()) {
			xFix = this.normalizeColumn(xFix);
		}
		if (arena.isHorizontalWraparoundEnabled()) {
			yFix = this.normalizeRow(yFix);
		}
		if (arena.isThirdDimensionWraparoundEnabled()) {
			zFix = this.normalizeFloor(zFix);
		}
		int u, w;
		if (d == Direction.NORTH) {
			for (u = yFix - 1; u >= 0; u--) {
				for (w = 0; w < LayerHelper.COUNT; w++) {
					try {
						final var obj = this.getCell(arena, xFix, u, zFix, w);
						if (obj.getMaterial() == Material.MAGNETIC) {
							return yFix - u - 1;
						}
						if (obj.isSolid()) {
							return 0;
						}
					} catch (final ArrayIndexOutOfBoundsException aioobe) {
						return 0;
					}
				}
			}
			return 0;
		}
		if (d == Direction.SOUTH) {
			for (u = yFix + 1; u < 24; u++) {
				for (w = 0; w < LayerHelper.COUNT; w++) {
					try {
						final var obj = this.getCell(arena, xFix, u, zFix, w);
						if (obj.getMaterial() == Material.MAGNETIC) {
							return u - yFix - 1;
						}
						if (obj.isSolid()) {
							return 0;
						}
					} catch (final ArrayIndexOutOfBoundsException aioobe) {
						return 0;
					}
				}
			}
		} else if (d == Direction.WEST) {
			for (u = xFix - 1; u >= 0; u--) {
				for (w = 0; w < LayerHelper.COUNT; w++) {
					try {
						final var obj = this.getCell(arena, u, yFix, zFix, w);
						if (obj.getMaterial() == Material.MAGNETIC) {
							return xFix - u - 1;
						}
						if (obj.isSolid()) {
							return 0;
						}
					} catch (final ArrayIndexOutOfBoundsException aioobe) {
						return 0;
					}
				}
			}
		} else if (d == Direction.EAST) {
			for (u = xFix + 1; u < 24; u++) {
				for (w = 0; w < LayerHelper.COUNT; w++) {
					try {
						final var obj = this.getCell(arena, u, yFix, zFix, w);
						if (obj.getMaterial() == Material.MAGNETIC) {
							return u - xFix - 1;
						}
						if (obj.isSolid()) {
							return 0;
						}
					} catch (final ArrayIndexOutOfBoundsException aioobe) {
						return 0;
					}
				}
			}
		}
		return 0;
	}

	@Override
	public void markAsDirty(final Arena arena, final int row, final int col, final int floor) {
		var fR = row;
		var fC = col;
		var fF = floor;
		if (arena.isVerticalWraparoundEnabled()) {
			fC = this.normalizeColumn(fC);
		}
		if (arena.isHorizontalWraparoundEnabled()) {
			fR = this.normalizeRow(fR);
		}
		if (arena.isThirdDimensionWraparoundEnabled()) {
			fF = this.normalizeFloor(fF);
		}
		this.dirtyData.setCell(true, fC, fR, fF);
	}

	@Override
	public ArenaData readData(final Arena arena, final DataIOReader reader, final GameFormat formatVersion)
			throws IOException {
		if (GameFormatHelper.isValidG1(formatVersion)) {
			return CurrentArenaData.readDataG1(arena, reader, formatVersion);
		}
		if (GameFormatHelper.isValidG2(formatVersion)) {
			return CurrentArenaData.readDataG2(arena, reader, formatVersion);
		}
		if (GameFormatHelper.isValidG3(formatVersion)) {
			return CurrentArenaData.readDataG3(arena, reader, formatVersion);
		} else if (GameFormatHelper.isValidG4(formatVersion)) {
			return CurrentArenaData.readDataG4(arena, reader, formatVersion);
		} else if (GameFormatHelper.isValidG5(formatVersion)) {
			return CurrentArenaData.readDataG5(arena, reader, formatVersion);
		} else if (GameFormatHelper.isValidG6(formatVersion)) {
			return CurrentArenaData.readDataG6(arena, reader, formatVersion);
		} else {
			throw new IOException(Strings.loadError(ErrorString.UNKNOWN_ARENA_FORMAT));
		}
	}

	@Override
	public void readSavedState(final DataIOReader reader, final GameFormat formatVersion) throws IOException {
		if (GameFormatHelper.isValidG1(formatVersion) || GameFormatHelper.isValidG2(formatVersion)) {
			this.readSavedStateG2(reader, formatVersion);
		} else if (GameFormatHelper.isValidG3(formatVersion)) {
			this.readSavedStateG3(reader, formatVersion);
		} else if (GameFormatHelper.isValidG4(formatVersion)) {
			this.readSavedStateG4(reader, formatVersion);
		} else if (GameFormatHelper.isValidG5(formatVersion)) {
			this.readSavedStateG5(reader, formatVersion);
		} else if (GameFormatHelper.isValidG6(formatVersion)) {
			this.readSavedStateG6(reader, formatVersion);
		} else {
			throw new IOException(Strings.loadError(ErrorString.UNKNOWN_ARENA_FORMAT));
		}
	}

	private void readSavedStateG2(final DataIOReader reader, final GameFormat formatVersion) throws IOException {
		int y, x, z, saveSizeX, saveSizeY, saveSizeZ;
		saveSizeX = reader.readInt();
		saveSizeY = reader.readInt();
		saveSizeZ = reader.readInt();
		this.savedState = new CurrentArenaStorage(saveSizeY, saveSizeX, saveSizeZ, LayerHelper.COUNT);
		for (x = 0; x < saveSizeX; x++) {
			for (y = 0; y < saveSizeY; y++) {
				for (z = 0; z < saveSizeZ; z++) {
					this.savedState.setCell(ArenaObjectList.readArenaObjectG2(reader, formatVersion), y, x, z,
							Layer.LOWER_GROUND.ordinal());
				}
			}
		}
		if (saveSizeX != ArenaData.MIN_COLUMNS || saveSizeY != ArenaData.MIN_ROWS) {
			this.resizeSavedState(saveSizeZ, new ArenaObject(GameObjectID.GROUND));
		}
	}

	private void readSavedStateG3(final DataIOReader reader, final GameFormat formatVersion) throws IOException {
		int y, x, z, saveSizeX, saveSizeY, saveSizeZ;
		saveSizeX = reader.readInt();
		saveSizeY = reader.readInt();
		saveSizeZ = reader.readInt();
		this.savedState = new CurrentArenaStorage(saveSizeY, saveSizeX, saveSizeZ, LayerHelper.COUNT);
		for (x = 0; x < saveSizeX; x++) {
			for (y = 0; y < saveSizeY; y++) {
				for (z = 0; z < saveSizeZ; z++) {
					this.savedState.setCell(ArenaObjectList.readArenaObjectG3(reader, formatVersion), y, x, z,
							Layer.LOWER_GROUND.ordinal());
				}
			}
		}
		if (saveSizeX != ArenaData.MIN_COLUMNS || saveSizeY != ArenaData.MIN_ROWS) {
			this.resizeSavedState(saveSizeZ, new ArenaObject(GameObjectID.GROUND));
		}
	}

	private void readSavedStateG4(final DataIOReader reader, final GameFormat formatVersion) throws IOException {
		int y, x, z, saveSizeX, saveSizeY, saveSizeZ;
		saveSizeX = reader.readInt();
		saveSizeY = reader.readInt();
		saveSizeZ = reader.readInt();
		this.savedState = new CurrentArenaStorage(saveSizeY, saveSizeX, saveSizeZ, LayerHelper.COUNT);
		for (x = 0; x < saveSizeX; x++) {
			for (y = 0; y < saveSizeY; y++) {
				for (z = 0; z < saveSizeZ; z++) {
					this.savedState.setCell(ArenaObjectList.readArenaObjectG4(reader, formatVersion), y, x, z,
							Layer.LOWER_GROUND.ordinal());
				}
			}
		}
		if (saveSizeX != ArenaData.MIN_COLUMNS || saveSizeY != ArenaData.MIN_ROWS) {
			this.resizeSavedState(saveSizeZ, new ArenaObject(GameObjectID.GROUND));
		}
	}

	private void readSavedStateG5(final DataIOReader reader, final GameFormat formatVersion) throws IOException {
		int y, x, z, w, saveSizeX, saveSizeY, saveSizeZ;
		saveSizeX = reader.readInt();
		saveSizeY = reader.readInt();
		saveSizeZ = reader.readInt();
		this.savedState = new CurrentArenaStorage(saveSizeY, saveSizeX, saveSizeZ, LayerHelper.COUNT);
		for (x = 0; x < saveSizeX; x++) {
			for (y = 0; y < saveSizeY; y++) {
				for (z = 0; z < saveSizeZ; z++) {
					for (w = 0; w < LayerHelper.COUNT; w++) {
						this.savedState.setCell(ArenaObjectList.readArenaObjectG5(reader, formatVersion), y, x, z, w);
					}
				}
			}
		}
		if (saveSizeX != ArenaData.MIN_COLUMNS || saveSizeY != ArenaData.MIN_ROWS) {
			this.resizeSavedState(saveSizeZ, new ArenaObject(GameObjectID.GROUND));
		}
	}

	private void readSavedStateG6(final DataIOReader reader, final GameFormat formatVersion) throws IOException {
		int y, x, z, w, saveSizeX, saveSizeY, saveSizeZ;
		saveSizeX = reader.readInt();
		saveSizeY = reader.readInt();
		saveSizeZ = reader.readInt();
		this.savedState = new CurrentArenaStorage(saveSizeY, saveSizeX, saveSizeZ, LayerHelper.COUNT);
		for (x = 0; x < saveSizeX; x++) {
			for (y = 0; y < saveSizeY; y++) {
				for (z = 0; z < saveSizeZ; z++) {
					for (w = 0; w < LayerHelper.COUNT; w++) {
						this.savedState.setCell(ArenaObjectList.readArenaObjectG6(reader, formatVersion), y, x, z, w);
					}
				}
			}
		}
		if (saveSizeX != ArenaData.MIN_COLUMNS || saveSizeY != ArenaData.MIN_ROWS) {
			this.resizeSavedState(saveSizeZ, new ArenaObject(GameObjectID.GROUND));
		}
	}

	@Override
	public void redo(final Arena arena) {
		this.iue.redo();
		this.data = this.iue.getImage();
		this.setAllDirtyFlags();
		this.clearVirtualGrid(arena);
	}

	@Override
	public void resetHistoryEngine() {
		this.iue = new ImageUndoEngine();
	}

	@Override
	public void resize(final Arena arena, final int zIn, final ArenaObject nullFill) {
		final var x = ArenaData.MIN_ROWS;
		final var y = ArenaData.MIN_COLUMNS;
		var z = zIn;
		if (arena.isThirdDimensionWraparoundEnabled()) {
			z = this.normalizeFloor(z);
		}
		// Allocate temporary storage array
		final var tempStorage = new CurrentArenaStorage(y, x, z, LayerHelper.COUNT);
		// Copy existing maze into temporary array
		int u, v, w, t;
		for (u = 0; u < y; u++) {
			for (v = 0; v < x; v++) {
				for (w = 0; w < z; w++) {
					for (t = 0; t < LayerHelper.COUNT; t++) {
						try {
							tempStorage.setCell(this.getCell(arena, v, u, w, t), u, v, w, t);
						} catch (final ArrayIndexOutOfBoundsException aioob) {
							// Do nothing
						}
					}
				}
			}
		}
		// Set the current data to the temporary array
		this.data = tempStorage;
		this.virtualData = new CurrentArenaStorage(x, y, z, LayerHelper.VIRTUAL_COUNT);
		this.dirtyData = new FlagStorage(x, y, z);
		// Fill any blanks
		this.fillNulls(arena, nullFill, null, false);
		// Fix saved tower state
		this.resizeSavedState(z, nullFill);
	}

	@Override
	public void resizeSavedState(final int z, final ArenaObject nullFill) {
		final var x = ArenaData.MIN_ROWS;
		final var y = ArenaData.MIN_COLUMNS;
		// Allocate temporary storage array
		final var tempStorage = new CurrentArenaStorage(y, x, z, LayerHelper.COUNT);
		// Copy existing maze into temporary array
		int u, v, w, t;
		for (u = 0; u < y; u++) {
			for (v = 0; v < x; v++) {
				for (w = 0; w < z; w++) {
					for (t = 0; t < LayerHelper.COUNT; t++) {
						try {
							tempStorage.setCell(this.savedState.getCell(v, u, w, t), u, v, w, t);
						} catch (final ArrayIndexOutOfBoundsException aioob) {
							// Do nothing
						}
					}
				}
			}
		}
		// Set the current data to the temporary array
		this.savedState = tempStorage;
		// Fill any blanks
		this.fillSTSNulls(nullFill);
	}

	@Override
	public void restore(final Arena arena) {
		int y, x, z, w;
		for (x = 0; x < this.getColumns(); x++) {
			for (y = 0; y < this.getRows(); y++) {
				for (z = 0; z < this.getFloors(); z++) {
					for (w = 0; w < LayerHelper.COUNT; w++) {
						this.setCell(arena, (ArenaObject) this.savedState.getCell(x, y, z, w), y, x, z, w);
					}
				}
			}
		}
	}

	@Override
	public void save(final Arena arena) {
		int y, x, z, w;
		for (x = 0; x < this.getColumns(); x++) {
			for (y = 0; y < this.getRows(); y++) {
				for (z = 0; z < this.getFloors(); z++) {
					for (w = 0; w < LayerHelper.COUNT; w++) {
						this.savedState.setCell(this.getCell(arena, y, x, z, w), x, y, z, w);
					}
				}
			}
		}
	}

	@Override
	public void setAllDirtyFlags() {
		for (var floor = 0; floor < this.getFloors(); floor++) {
			this.setDirtyFlags(floor);
		}
	}

	@Override
	public void setCell(final Arena arena, final ArenaObject mo, final int row, final int col, final int floor,
			final int layer) {
		var fR = row;
		var fC = col;
		var fF = floor;
		if (arena.isVerticalWraparoundEnabled()) {
			fC = this.normalizeColumn(fC);
		}
		if (arena.isHorizontalWraparoundEnabled()) {
			fR = this.normalizeRow(fR);
		}
		if (arena.isThirdDimensionWraparoundEnabled()) {
			fF = this.normalizeFloor(fF);
		}
		this.data.setArenaDataCell(mo, fC, fR, fF, layer);
		this.dirtyData.setCell(true, fC, fR, fF);
	}

	@Override
	public void setDirtyFlags(final int floor) {
		for (var row = 0; row < this.getRows(); row++) {
			for (var col = 0; col < this.getColumns(); col++) {
				this.dirtyData.setCell(true, col, row, floor);
			}
		}
	}

	@Override
	public void setVirtualCell(final Arena arena, final ArenaObject mo, final int row, final int col,
			final int floor, final int layer) {
		var fR = row;
		var fC = col;
		var fF = floor;
		if (arena.isVerticalWraparoundEnabled()) {
			fC = this.normalizeColumn(fC);
		}
		if (arena.isHorizontalWraparoundEnabled()) {
			fR = this.normalizeRow(fR);
		}
		if (arena.isThirdDimensionWraparoundEnabled()) {
			fF = this.normalizeFloor(fF);
		}
		this.virtualData.setArenaDataCell(mo, fC, fR, fF, layer);
		this.dirtyData.setCell(true, fC, fR, fF);
	}

	@Override
	public void tickTimers(final Arena arena, final int floor, final GameAction actionType) {
		var floorFix = floor;
		if (arena.isThirdDimensionWraparoundEnabled()) {
			floorFix = this.normalizeFloor(floorFix);
		}
		int x, y, z, w;
		// Tick all ArenaObject timers
		ArenaObject.checkTunnels();
		for (z = Direction.NORTH.ordinal(); z <= Direction.NORTHWEST.ordinal(); z += 2) {
			for (x = 0; x < this.getColumns(); x++) {
				for (y = 0; y < this.getRows(); y++) {
					for (w = 0; w < LayerHelper.COUNT; w++) {
						final var mo = this.getCell(arena, y, x, floorFix, w);
						if (mo != null && z == Direction.NORTH.ordinal()) {
							// Handle objects waiting for a tunnel to open
							if (mo instanceof final ArenaObject gmo) {
								final var saved = gmo.getSavedObject();
								if (saved instanceof ArenaObject) {
									final var color = saved.getColor();
									if (gmo.waitingOnTunnel() && !ArenaObject.tunnelsFull(color)) {
										gmo.setWaitingOnTunnel(false);
										saved.pushIntoAction(gmo, y, x, floorFix);
									}
									if (ArenaObject.tunnelsFull(color)) {
										gmo.setWaitingOnTunnel(true);
									}
								}
							}
							mo.tickTimer(y, x, actionType);
						}
					}
				}
			}
		}
	}

	@Override
	public boolean tryRedo() {
		return this.iue.tryRedo();
	}

	@Override
	public boolean tryUndo() {
		return this.iue.tryUndo();
	}

	@Override
	public void undo(final Arena arena) {
		this.iue.undo();
		this.data = this.iue.getImage();
		this.setAllDirtyFlags();
		this.clearVirtualGrid(arena);
	}

	@Override
	public void updateRedoHistory(final HistoryStatus whatWas) {
		this.iue.updateRedoHistory(this.data, whatWas);
	}

	@Override
	public void updateUndoHistory(final HistoryStatus whatWas) {
		this.iue.updateUndoHistory(this.data, whatWas);
	}

	@Override
	public void writeData(final Arena arena, final DataIOWriter writer) throws IOException {
		int y, x, z, w;
		writer.writeInt(this.getColumns());
		writer.writeInt(this.getRows());
		writer.writeInt(this.getFloors());
		for (x = 0; x < this.getColumns(); x++) {
			for (y = 0; y < this.getRows(); y++) {
				for (z = 0; z < this.getFloors(); z++) {
					for (w = 0; w < LayerHelper.COUNT; w++) {
						this.getCell(arena, y, x, z, w).writeArenaObject(writer);
					}
				}
			}
		}
	}

	@Override
	public void writeSavedState(final DataIOWriter writer) throws IOException {
		int y, x, z, w;
		writer.writeInt(this.getColumns());
		writer.writeInt(this.getRows());
		writer.writeInt(this.getFloors());
		for (x = 0; x < this.getColumns(); x++) {
			for (y = 0; y < this.getRows(); y++) {
				for (z = 0; z < this.getFloors(); z++) {
					for (w = 0; w < LayerHelper.COUNT; w++) {
						((ArenaObject) this.savedState.getCell(y, x, z, w)).writeArenaObject(writer);
					}
				}
			}
		}
	}
}
