/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.arena.fileio.DataIOPrefixHandler;
import com.puttysoftware.lasertank.arena.fileio.DataIOSuffixHandler;
import com.puttysoftware.lasertank.arena.objects.ArenaObject;
import com.puttysoftware.lasertank.fileio.DataIOReader;
import com.puttysoftware.lasertank.fileio.DataIOWriter;
import com.puttysoftware.lasertank.fileio.XDataReader;
import com.puttysoftware.lasertank.fileio.XDataWriter;
import com.puttysoftware.lasertank.fileio.utility.FileUtilities;
import com.puttysoftware.lasertank.helper.DifficultyHelper;
import com.puttysoftware.lasertank.helper.GameFormatHelper;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameAction;
import com.puttysoftware.lasertank.index.GameFormat;
import com.puttysoftware.lasertank.index.RangeType;
import com.puttysoftware.lasertank.locale.CommonString;
import com.puttysoftware.lasertank.locale.DialogString;
import com.puttysoftware.lasertank.locale.ErrorString;
import com.puttysoftware.lasertank.locale.Strings;
import com.puttysoftware.lasertank.locale.global.GlobalStrings;
import com.puttysoftware.lasertank.locale.global.UntranslatedString;
import com.puttysoftware.lasertank.settings.Settings;
import com.puttysoftware.lasertank.utility.FileExtensions;
import com.puttysoftware.lasertank.utility.IDGenerator;
import com.puttysoftware.lasertank.utility.InvalidArenaException;

class CurrentArena extends Arena {
    private static String convertDifficultyNumberToName(final int number) {
	return DifficultyHelper.getNames()[number - 1];
    }

    public static int getStartLevel() {
	return 0;
    }

    // Properties
    private CurrentArenaData arenaData;
    private CurrentArenaData clipboard;
    private CurrentArenaLevelInfo infoClipboard;
    private int levelCount;
    private int activeLevel;
    private String basePath;
    private DataIOPrefixHandler prefixHandler;
    private DataIOSuffixHandler suffixHandler;
    private String musicFilename;
    private boolean moveShootAllowed;
    private final ArrayList<CurrentArenaLevelInfo> levelInfoData;
    private ArrayList<String> levelInfoList;

    // Constructors
    public CurrentArena() throws IOException {
	this.arenaData = null;
	this.clipboard = null;
	this.levelCount = 0;
	this.activeLevel = 0;
	this.prefixHandler = null;
	this.suffixHandler = null;
	this.musicFilename = Strings.loadCommon(CommonString.NULL);
	this.moveShootAllowed = false;
	this.levelInfoData = new ArrayList<>();
	this.levelInfoList = new ArrayList<>();
	final var randomID = IDGenerator.getRandomIDString(16);
	this.basePath = System.getProperty(GlobalStrings.loadUntranslated(UntranslatedString.TEMP_DIR)) + File.separator
		+ GlobalStrings.loadUntranslated(UntranslatedString.RDNS_COMPANY_NAME) + File.separator + randomID
		+ GlobalStrings.loadUntranslated(UntranslatedString.FORMAT_FOLDER);
	final var base = new File(this.basePath);
	final var res = base.mkdirs();
	if (!res) {
	    throw new IOException(Strings.loadError(ErrorString.TEMP_DIR));
	}
    }

    @Override
    public boolean addLevel() {
	if (this.levelCount >= Arena.MAX_LEVELS) {
	    return false;
	}
	// Save old data, if any
	if (this.arenaData != null) {
	    try (var writer = this.getLevelWriter()) {
		// Save old level
		this.writeArenaLevel(writer);
	    } catch (final IOException ioe) {
		throw new InvalidArenaException(ioe);
	    }
	}
	// Create new level data
	this.levelCount++;
	this.activeLevel = this.levelCount - 1;
	this.levelInfoData.add(new CurrentArenaLevelInfo());
	this.levelInfoList.add(this.generateCurrentLevelInfo());
	this.arenaData = new CurrentArenaData();
	return true;
    }

    @Override
    public void checkForEnemies(final int floor, final int ex, final int ey, final ArenaObject e) {
	this.arenaData.checkForEnemies(this, floor, ex, ey, e);
    }

    @Override
    public int checkForMagnetic(final int floor, final int centerX, final int centerY, final Direction dir) {
	return this.arenaData.checkForMagnetic(this, floor, centerX, centerY, dir);
    }

    @Override
    public int[] circularScan(final int x, final int y, final int z, final int maxR, final String targetName,
	    final boolean moved) {
	return this.arenaData.circularScan(this, x, y, z, maxR, targetName, moved);
    }

    @Override
    public void circularScanRange(final int x, final int y, final int z, final int maxR, final RangeType rangeType,
	    final int forceUnits) {
	this.arenaData.circularScanRange(this, x, y, z, maxR, rangeType, forceUnits);
    }

    @Override
    public boolean circularScanTank(final int x, final int y, final int z, final int maxR) {
	return this.arenaData.circularScanTank(this, x, y, z, maxR);
    }

    @Override
    public int[] circularScanTunnel(final int x, final int y, final int z, final int maxR, final int tx, final int ty,
	    final ArenaObject target, final boolean moved) {
	return this.arenaData.circularScanTunnel(this, x, y, z, maxR, tx, ty, target, moved);
    }

    @Override
    public void clearDirtyFlags(final int floor) {
	this.arenaData.clearDirtyFlags(floor);
    }

    @Override
    public void clearVirtualGrid() {
	this.arenaData.clearVirtualGrid(this);
    }

    @Override
    public void copyLevel() {
	this.clipboard = this.arenaData;
	this.infoClipboard = this.levelInfoData.get(this.activeLevel);
    }

    @Override
    public void cutLevel() {
	if (this.levelCount > 1) {
	    this.clipboard = this.arenaData;
	    this.infoClipboard = this.levelInfoData.get(this.activeLevel);
	    this.removeActiveLevel();
	}
    }

    @Override
    public void disableHorizontalWraparound() {
	this.levelInfoData.get(this.activeLevel).disableHorizontalWraparound(this.levelInfoData.get(this.activeLevel));
    }

    @Override
    public void disableThirdDimensionWraparound() {
	this.levelInfoData.get(this.activeLevel)
		.disableThirdDimensionWraparound(this.levelInfoData.get(this.activeLevel));
    }

    @Override
    public void disableVerticalWraparound() {
	this.levelInfoData.get(this.activeLevel).disableVerticalWraparound(this.levelInfoData.get(this.activeLevel));
    }

    @Override
    public boolean doesLevelExist(final int level) {
	return level < this.levelCount && level >= 0;
    }

    @Override
    public boolean doesLevelExistOffset(final int level) {
	return this.activeLevel + level < this.levelCount && this.activeLevel + level >= 0;
    }

    @Override
    public boolean doesPlayerExist(final int pi) {
	return this.levelInfoData.get(this.activeLevel).doesPlayerExist(pi, this.levelInfoData.get(this.activeLevel));
    }

    @Override
    public void enableHorizontalWraparound() {
	this.levelInfoData.get(this.activeLevel).enableHorizontalWraparound(this.levelInfoData.get(this.activeLevel));
    }

    @Override
    public void enableThirdDimensionWraparound() {
	this.levelInfoData.get(this.activeLevel)
		.enableThirdDimensionWraparound(this.levelInfoData.get(this.activeLevel));
    }

    @Override
    public void enableVerticalWraparound() {
	this.levelInfoData.get(this.activeLevel).enableVerticalWraparound(this.levelInfoData.get(this.activeLevel));
    }

    @Override
    public void fillDefault() {
	final var fill = Settings.getEditorDefaultFill();
	this.arenaData.fill(this, fill);
    }

    @Override
    public int[] findObject(final int z, final ArenaObject target) {
	return this.arenaData.findObject(this, z, target);
    }

    @Override
    public int[] findPlayer(final int number) {
	return this.arenaData.findPlayer(this, number);
    }

    @Override
    public void fullScanFindLostPair(final int z, final ArenaObject source) {
	this.arenaData.fullScanFindLostPair(this, z, source);
    }

    @Override
    public void fullScanFreezeGround() {
	this.arenaData.fullScanFreezeGround(this);
    }

    @Override
    public void fullScanKillTanks() {
	this.arenaData.fullScanKillTanks(this);
    }

    @Override
    public void fullScanPairBind(final int dx, final int dy, final int z, final ArenaObject source) {
	this.arenaData.fullScanPairBind(this, dx, dy, z, source);
    }

    @Override
    public void fullScanPairCleanup(final int px, final int py, final int z, final ArenaObject source) {
	this.arenaData.fullScanPairCleanup(this, px, py, z, source);
    }

    @Override
    public void fullScanPairClose(final int z, final ArenaObject source) {
	this.arenaData.fullScanPairClose(this, z, source);
    }

    @Override
    public void fullScanPairOpen(final int z, final ArenaObject source) {
	this.arenaData.fullScanPairOpen(this, z, source);
    }

    private String generateCurrentLevelInfo() {
	final var sb = new StringBuilder();
	sb.append(Strings.loadDialog(DialogString.ARENA_LEVEL));
	sb.append(Strings.loadCommon(CommonString.SPACE));
	sb.append(this.getActiveLevelNumber() + 1);
	sb.append(Strings.loadCommon(CommonString.COLON) + Strings.loadCommon(CommonString.SPACE));
	sb.append(this.getName().trim());
	sb.append(Strings.loadCommon(CommonString.SPACE));
	sb.append(Strings.loadDialog(DialogString.ARENA_LEVEL_BY));
	sb.append(Strings.loadCommon(CommonString.SPACE));
	sb.append(this.getAuthor().trim());
	sb.append(Strings.loadCommon(CommonString.SPACE));
	sb.append(Strings.loadCommon(CommonString.OPEN_PARENTHESES));
	sb.append(CurrentArena.convertDifficultyNumberToName(this.getDifficulty()));
	sb.append(Strings.loadCommon(CommonString.CLOSE_PARENTHESES));
	return sb.toString();
    }

    @Override
    public void generateLevelInfoList() {
	final var saveLevel = this.getActiveLevelNumber();
	final var tempStorage = new ArrayList<String>();
	for (var x = 0; x < this.levelCount; x++) {
	    this.switchLevel(x);
	    tempStorage.add(this.generateCurrentLevelInfo());
	}
	this.switchLevel(saveLevel);
	this.levelInfoList = tempStorage;
    }

    @Override
    public int getActiveLevelNumber() {
	return this.activeLevel;
    }

    // Methods
    @Override
    public String getArenaTempMusicFolder() {
	return this.basePath + File.separator + GlobalStrings.loadUntranslated(UntranslatedString.MUSIC_FOLDER)
		+ File.separator;
    }

    @Override
    public String getAuthor() {
	return this.levelInfoData.get(this.activeLevel).getAuthor(this.levelInfoData.get(this.activeLevel));
    }

    @Override
    public String getBasePath() {
	return this.basePath;
    }

    @Override
    public ArenaObject getCell(final int row, final int col, final int floor, final int layer) {
	return this.arenaData.getCell(this, row, col, floor, layer);
    }

    @Override
    public int getColumns() {
	return this.arenaData.getColumns();
    }

    @Override
    public int getDifficulty() {
	return this.levelInfoData.get(this.activeLevel).getDifficulty(this.levelInfoData.get(this.activeLevel));
    }

    @Override
    public int getFloors() {
	return this.arenaData.getFloors();
    }

    @Override
    public String getHint() {
	return this.levelInfoData.get(this.activeLevel).getHint(this.levelInfoData.get(this.activeLevel));
    }

    private File getLevelFile(final int level, final int era) {
	return new File(this.basePath + File.separator + GlobalStrings.loadUntranslated(UntranslatedString.FORMAT_LEVEL)
		+ level + GlobalStrings.loadUntranslated(UntranslatedString.FORMAT_ERA) + era
		+ FileExtensions.getArenaLevelExtensionWithPeriod());
    }

    @Override
    public String[] getLevelInfoList() {
	return this.levelInfoList.toArray(new String[this.levelInfoList.size()]);
    }

    private DataIOReader getLevelReaderG5() throws IOException {
	return new XDataReader(
		this.basePath + File.separator + GlobalStrings.loadUntranslated(UntranslatedString.FORMAT_LEVEL)
			+ this.activeLevel + FileExtensions.getArenaLevelExtensionWithPeriod(),
		GlobalStrings.loadUntranslated(UntranslatedString.FORMAT_LEVEL));
    }

    private DataIOReader getLevelReaderG6() throws IOException {
	return new XDataReader(
		this.basePath + File.separator + GlobalStrings.loadUntranslated(UntranslatedString.FORMAT_LEVEL)
			+ this.activeLevel + FileExtensions.getArenaLevelExtensionWithPeriod(),
		GlobalStrings.loadUntranslated(UntranslatedString.FORMAT_LEVEL));
    }

    @Override
    public int getLevels() {
	return this.levelCount;
    }

    private DataIOWriter getLevelWriter() throws IOException {
	return new XDataWriter(
		this.basePath + File.separator + GlobalStrings.loadUntranslated(UntranslatedString.FORMAT_LEVEL)
			+ this.activeLevel + FileExtensions.getArenaLevelExtensionWithPeriod(),
		GlobalStrings.loadUntranslated(UntranslatedString.FORMAT_LEVEL));
    }

    @Override
    public String getMusicFilename() {
	return this.musicFilename;
    }

    @Override
    public String getName() {
	return this.levelInfoData.get(this.activeLevel).getName(this.levelInfoData.get(this.activeLevel));
    }

    @Override
    public int getRows() {
	return this.arenaData.getRows();
    }

    @Override
    public int getStartColumn(final int pi) {
	return this.levelInfoData.get(this.activeLevel).getStartColumn(pi, this.levelInfoData.get(this.activeLevel));
    }

    @Override
    public int getStartFloor(final int pi) {
	return this.levelInfoData.get(this.activeLevel).getStartFloor(pi, this.levelInfoData.get(this.activeLevel));
    }

    @Override
    public int getStartRow(final int pi) {
	return this.levelInfoData.get(this.activeLevel).getStartRow(pi, this.levelInfoData.get(this.activeLevel));
    }

    @Override
    public ArenaObject getVirtualCell(final int row, final int col, final int floor, final int layer) {
	return this.arenaData.getVirtualCell(this, row, col, floor, layer);
    }

    @Override
    public HistoryStatus getWhatWas() {
	return this.arenaData.getWhatWas();
    }

    @Override
    public boolean insertLevelFromClipboard() {
	if (this.levelCount < Arena.MAX_LEVELS) {
	    this.arenaData = this.clipboard;
	    this.levelCount++;
	    return true;
	}
	return false;
    }

    @Override
    public boolean isCellDirty(final int row, final int col, final int floor) {
	return this.arenaData.isCellDirty(this, row, col, floor);
    }

    @Override
    public boolean isCutBlocked() {
	return this.levelCount <= 1;
    }

    @Override
    public boolean isHorizontalWraparoundEnabled() {
	return this.levelInfoData.get(this.activeLevel)
		.isHorizontalWraparoundEnabled(this.levelInfoData.get(this.activeLevel));
    }

    @Override
    public boolean isMoveShootAllowed() {
	return this.isMoveShootAllowedGlobally() && this.isMoveShootAllowedThisLevel();
    }

    @Override
    public boolean isMoveShootAllowedGlobally() {
	return this.moveShootAllowed;
    }

    @Override
    public boolean isMoveShootAllowedThisLevel() {
	return this.levelInfoData.get(this.activeLevel).isMoveShootAllowed(this.levelInfoData.get(this.activeLevel));
    }

    @Override
    public boolean isPasteBlocked() {
	return this.clipboard == null;
    }

    @Override
    public boolean isThirdDimensionWraparoundEnabled() {
	return this.levelInfoData.get(this.activeLevel)
		.isThirdDimensionWraparoundEnabled(this.levelInfoData.get(this.activeLevel));
    }

    @Override
    public boolean isVerticalWraparoundEnabled() {
	return this.levelInfoData.get(this.activeLevel)
		.isVerticalWraparoundEnabled(this.levelInfoData.get(this.activeLevel));
    }

    @Override
    public void markAsDirty(final int row, final int col, final int floor) {
	this.arenaData.markAsDirty(this, row, col, floor);
    }

    @Override
    public void pasteLevel() {
	if (this.clipboard != null) {
	    this.arenaData = this.clipboard;
	    this.levelInfoData.set(this.activeLevel, this.infoClipboard);
	    this.levelInfoList.set(this.activeLevel, this.generateCurrentLevelInfo());
	    ArenaManager.setDirty(true);
	}
    }

    @Override
    public CurrentArena readArena() throws IOException {
	final var m = new CurrentArena();
	// Attach handlers
	m.setPrefixHandler(this.prefixHandler);
	m.setSuffixHandler(this.suffixHandler);
	// Make base paths the same
	m.basePath = this.basePath;
	var formatVersion = GameFormatHelper.FORMAT_LATEST;
	// Create metafile reader
	try (DataIOReader metaReader = new XDataReader(
		m.basePath + File.separator + GlobalStrings.loadUntranslated(UntranslatedString.FORMAT_METAFILE)
			+ FileExtensions.getArenaLevelExtensionWithPeriod(),
		GlobalStrings.loadUntranslated(UntranslatedString.FORMAT_ARENA))) {
	    // Read metafile
	    formatVersion = m.readArenaMetafileVersion(metaReader);
	    if (GameFormatHelper.isValidG6(formatVersion)) {
		m.readArenaMetafileG6(metaReader, formatVersion);
	    } else if (GameFormatHelper.isValidG4(formatVersion) || GameFormatHelper.isValidG5(formatVersion)) {
		m.readArenaMetafileG4(metaReader, formatVersion);
	    } else {
		m.readArenaMetafileG3(metaReader, formatVersion);
	    }
	} catch (final IOException ioe) {
	    throw new InvalidArenaException(ioe);
	}
	if (!GameFormatHelper.isLevelListStored(formatVersion)) {
	    // Create data reader
	    try (var dataReader = m.getLevelReaderG5()) {
		// Read data
		m.readArenaLevel(dataReader, formatVersion);
	    } catch (final IOException ioe) {
		throw new InvalidArenaException(ioe);
	    }
	    // Update level info
	    m.generateLevelInfoList();
	} else {
	    // Create data reader
	    try (var dataReader = m.getLevelReaderG6()) {
		// Read data
		m.readArenaLevel(dataReader, formatVersion);
	    } catch (final IOException ioe) {
		throw new InvalidArenaException(ioe);
	    }
	}
	return m;
    }

    private void readArenaLevel(final DataIOReader reader) throws IOException {
	this.readArenaLevel(reader, GameFormatHelper.FORMAT_LATEST);
    }

    private void readArenaLevel(final DataIOReader reader, final GameFormat formatVersion) throws IOException {
	this.arenaData = (CurrentArenaData) new CurrentArenaData().readData(this, reader, formatVersion);
	this.arenaData.readSavedState(reader, formatVersion);
    }

    private void readArenaMetafileG3(final DataIOReader reader, final GameFormat formatVersion) throws IOException {
	this.levelCount = reader.readInt();
	this.musicFilename = Strings.loadCommon(CommonString.NULL);
	if (this.suffixHandler != null) {
	    this.suffixHandler.readSuffix(reader, formatVersion);
	}
    }

    private void readArenaMetafileG4(final DataIOReader reader, final GameFormat formatVersion) throws IOException {
	this.levelCount = reader.readInt();
	this.musicFilename = reader.readString();
	if (this.suffixHandler != null) {
	    this.suffixHandler.readSuffix(reader, formatVersion);
	}
    }

    private void readArenaMetafileG6(final DataIOReader reader, final GameFormat formatVersion) throws IOException {
	this.levelCount = reader.readInt();
	this.musicFilename = reader.readString();
	this.moveShootAllowed = reader.readBoolean();
	for (var l = 0; l < this.levelCount; l++) {
	    this.levelInfoData.add(CurrentArenaLevelInfo.readLevelInfo(reader));
	    this.levelInfoList.add(reader.readString());
	}
	if (this.suffixHandler != null) {
	    this.suffixHandler.readSuffix(reader, formatVersion);
	}
    }

    private GameFormat readArenaMetafileVersion(final DataIOReader reader) throws IOException {
	var formatVersion = GameFormatHelper.FORMAT_LATEST;
	if (this.prefixHandler != null) {
	    formatVersion = this.prefixHandler.readPrefix(reader);
	}
	this.moveShootAllowed = GameFormatHelper.isMoveShootAllowed(formatVersion);
	return formatVersion;
    }

    @Override
    public void redo() {
	this.arenaData.redo(this);
    }

    @Override
    protected boolean removeActiveLevel() {
	if (this.levelCount <= 1 || this.activeLevel < 0 || this.activeLevel > this.levelCount) {
	    return false;
	}
	this.arenaData = null;
	// Delete all files corresponding to current level
	for (var e = 0; e < Arena.ERA_COUNT; e++) {
	    final var res = this.getLevelFile(this.activeLevel, e).delete();
	    if (!res) {
		return false;
	    }
	}
	// Shift all higher-numbered levels down
	for (var x = this.activeLevel; x < this.levelCount - 1; x++) {
	    for (var e = 0; e < Arena.ERA_COUNT; e++) {
		final var sourceLocation = this.getLevelFile(x + 1, e);
		final var targetLocation = this.getLevelFile(x, e);
		try {
		    FileUtilities.moveFile(sourceLocation, targetLocation);
		} catch (final IOException ioe) {
		    throw new InvalidArenaException(ioe);
		}
	    }
	}
	this.levelCount--;
	this.levelInfoData.remove(this.activeLevel);
	this.levelInfoList.remove(this.activeLevel);
	return true;
    }

    @Override
    public void resetHistoryEngine() {
	this.arenaData.resetHistoryEngine();
    }

    @Override
    public void resize(final int z, final ArenaObject nullFill) {
	this.arenaData.resize(this, z, nullFill);
    }

    @Override
    public void restore() {
	this.arenaData.restore(this);
    }

    @Override
    public void save() {
	this.arenaData.save(this);
    }

    @Override
    public void setAuthor(final String newAuthor) {
	this.levelInfoData.get(this.activeLevel).setAuthor(newAuthor, this.levelInfoData.get(this.activeLevel));
	this.levelInfoList.set(this.activeLevel, this.generateCurrentLevelInfo());
    }

    @Override
    public void setCell(final ArenaObject mo, final int row, final int col, final int floor, final int layer) {
	this.arenaData.setCell(this, mo, row, col, floor, layer);
    }

    @Override
    public void setData(final ArenaData newData, final int count) {
	if (newData instanceof CurrentArenaData) {
	    this.arenaData = (CurrentArenaData) newData;
	    if (count >= 0) {
		this.levelCount = count;
	    }
	}
    }

    @Override
    public void setDifficulty(final int newDifficulty) {
	this.levelInfoData.get(this.activeLevel).setDifficulty(newDifficulty, this.levelInfoData.get(this.activeLevel));
	this.levelInfoList.set(this.activeLevel, this.generateCurrentLevelInfo());
    }

    @Override
    public void setDirtyFlags(final int floor) {
	this.arenaData.setDirtyFlags(floor);
    }

    @Override
    public void setHint(final String newHint) {
	this.levelInfoData.get(this.activeLevel).setHint(newHint, this.levelInfoData.get(this.activeLevel));
    }

    @Override
    public void setMoveShootAllowedGlobally(final boolean value) {
	this.moveShootAllowed = value;
    }

    @Override
    public void setMoveShootAllowedThisLevel(final boolean value) {
	this.levelInfoData.get(this.activeLevel).setMoveShootAllowed(value, this.levelInfoData.get(this.activeLevel));
    }

    @Override
    public void setMusicFilename(final String newMusicFilename) {
	this.musicFilename = newMusicFilename;
    }

    @Override
    public void setName(final String newName) {
	this.levelInfoData.get(this.activeLevel).setName(newName, this.levelInfoData.get(this.activeLevel));
	this.levelInfoList.set(this.activeLevel, this.generateCurrentLevelInfo());
    }

    @Override
    public void setPrefixHandler(final DataIOPrefixHandler xph) {
	this.prefixHandler = xph;
    }

    @Override
    public void setStartColumn(final int pi, final int newStartColumn) {
	this.levelInfoData.get(this.activeLevel).setStartColumn(pi, newStartColumn,
		this.levelInfoData.get(this.activeLevel));
    }

    @Override
    public void setStartFloor(final int pi, final int newStartFloor) {
	this.levelInfoData.get(this.activeLevel).setStartFloor(pi, newStartFloor,
		this.levelInfoData.get(this.activeLevel));
    }

    @Override
    public void setStartRow(final int pi, final int newStartRow) {
	this.levelInfoData.get(this.activeLevel).setStartRow(pi, newStartRow, this.levelInfoData.get(this.activeLevel));
    }

    @Override
    public void setSuffixHandler(final DataIOSuffixHandler xsh) {
	this.suffixHandler = xsh;
    }

    @Override
    public void setVirtualCell(final ArenaObject mo, final int row, final int col, final int floor, final int layer) {
	this.arenaData.setVirtualCell(this, mo, row, col, floor, layer);
    }

    @Override
    protected void switchInternal(final int level) {
	if (level < 0 || level >= this.levelCount) {
	    System.err.println(
		    Strings.subst(Strings.loadError(ErrorString.SWITCH_TO_INVALID_LEVEL), Integer.toString(level)));
	    return;
	}
	if (this.activeLevel != level || this.arenaData == null) {
	    if (this.arenaData != null) {
		try (var writer = this.getLevelWriter()) {
		    // Save old level
		    this.writeArenaLevel(writer);
		} catch (final IOException ioe) {
		    throw new InvalidArenaException(ioe);
		}
	    }
	    this.activeLevel = level;
	    try (var reader = this.getLevelReaderG6()) {
		// Load new level
		this.readArenaLevel(reader);
	    } catch (final IOException ioe) {
		LaserTankEE.logError(ioe);
	    }
	}
    }

    @Override
    public void switchLevel(final int level) {
	this.switchInternal(level);
    }

    @Override
    public void switchLevelOffset(final int level) {
	this.switchInternal(this.activeLevel + level);
    }

    @Override
    public void tickTimers(final int floor, final GameAction actionType) {
	this.arenaData.tickTimers(this, floor, actionType);
    }

    @Override
    public boolean tryRedo() {
	return this.arenaData.tryRedo();
    }

    @Override
    public boolean tryUndo() {
	return this.arenaData.tryUndo();
    }

    @Override
    public void undo() {
	this.arenaData.undo(this);
    }

    @Override
    public void updateRedoHistory(final HistoryStatus whatIs) {
	this.arenaData.updateRedoHistory(whatIs);
    }

    @Override
    public void updateUndoHistory(final HistoryStatus whatIs) {
	this.arenaData.updateUndoHistory(whatIs);
    }

    @Override
    public void writeArena() throws IOException {
	// Create metafile writer
	try (DataIOWriter metaWriter = new XDataWriter(
		this.basePath + File.separator + GlobalStrings.loadUntranslated(UntranslatedString.FORMAT_METAFILE)
			+ FileExtensions.getArenaLevelExtensionWithPeriod(),
		GlobalStrings.loadUntranslated(UntranslatedString.FORMAT_ARENA))) {
	    // Write metafile
	    this.writeArenaMetafile(metaWriter);
	} catch (final IOException ioe) {
	    throw new InvalidArenaException(ioe);
	}
	// Create data writer
	try (var dataWriter = this.getLevelWriter()) {
	    // Write data
	    this.writeArenaLevel(dataWriter);
	} catch (final IOException ioe) {
	    throw new InvalidArenaException(ioe);
	}
    }

    private void writeArenaLevel(final DataIOWriter writer) throws IOException {
	// Write the level
	this.arenaData.writeData(this, writer);
	this.arenaData.writeSavedState(writer);
    }

    private void writeArenaMetafile(final DataIOWriter writer) throws IOException {
	if (this.prefixHandler != null) {
	    this.prefixHandler.writePrefix(writer);
	}
	writer.writeInt(this.levelCount);
	writer.writeString(this.musicFilename);
	writer.writeBoolean(this.moveShootAllowed);
	for (var l = 0; l < this.levelCount; l++) {
	    this.levelInfoData.get(l).writeLevelInfo(writer, this.levelInfoData.get(l));
	    writer.writeString(this.levelInfoList.get(l));
	}
	if (this.suffixHandler != null) {
	    this.suffixHandler.writeSuffix(writer);
	}
    }
}