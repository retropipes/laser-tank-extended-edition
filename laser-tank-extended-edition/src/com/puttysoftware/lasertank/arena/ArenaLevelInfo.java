package com.puttysoftware.lasertank.arena;

import java.io.IOException;

import com.puttysoftware.lasertank.fileio.DataIOWriter;

interface ArenaLevelInfo {
    void disableHorizontalWraparound(ArenaLevelInfo currentArenaLevelInfo);

    void disableThirdDimensionWraparound(ArenaLevelInfo currentArenaLevelInfo);

    void disableVerticalWraparound(ArenaLevelInfo currentArenaLevelInfo);

    boolean doesPlayerExist(int pi, ArenaLevelInfo currentArenaLevelInfo);

    void enableHorizontalWraparound(ArenaLevelInfo currentArenaLevelInfo);

    void enableThirdDimensionWraparound(ArenaLevelInfo currentArenaLevelInfo);

    void enableVerticalWraparound(ArenaLevelInfo currentArenaLevelInfo);

    String getAuthor(ArenaLevelInfo currentArenaLevelInfo);

    int getDifficulty(ArenaLevelInfo currentArenaLevelInfo);

    String getHint(ArenaLevelInfo currentArenaLevelInfo);

    String getName(ArenaLevelInfo currentArenaLevelInfo);

    int getStartColumn(int pi, ArenaLevelInfo currentArenaLevelInfo);

    int getStartFloor(int pi, ArenaLevelInfo currentArenaLevelInfo);

    int getStartRow(int pi, ArenaLevelInfo currentArenaLevelInfo);

    boolean isHorizontalWraparoundEnabled(ArenaLevelInfo currentArenaLevelInfo);

    boolean isMoveShootAllowed(ArenaLevelInfo currentArenaLevelInfo);

    boolean isThirdDimensionWraparoundEnabled(ArenaLevelInfo currentArenaLevelInfo);

    boolean isVerticalWraparoundEnabled(ArenaLevelInfo currentArenaLevelInfo);

    void setAuthor(String newAuthor, ArenaLevelInfo currentArenaLevelInfo);

    void setDifficulty(int newDifficulty, ArenaLevelInfo currentArenaLevelInfo);

    void setHint(String newHint, ArenaLevelInfo currentArenaLevelInfo);

    void setMoveShootAllowed(boolean value, ArenaLevelInfo currentArenaLevelInfo);

    void setName(String newName, ArenaLevelInfo currentArenaLevelInfo);

    void setStartColumn(int pi, int value, ArenaLevelInfo currentArenaLevelInfo);

    void setStartFloor(int pi, int value, ArenaLevelInfo currentArenaLevelInfo);

    void setStartRow(int pi, int value, ArenaLevelInfo currentArenaLevelInfo);

    void writeLevelInfo(DataIOWriter writer, ArenaLevelInfo currentArenaLevelInfo) throws IOException;
}