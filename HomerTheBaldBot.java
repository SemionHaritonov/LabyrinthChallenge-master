package override.example;

import override.logic.Direction;
import override.logic.GameState;
import override.logic.LabyrinthPlayer;

public class HomerTheBaldBot implements LabyrinthPlayer {
    private int mynumber;

    private int currentJ;
    private int currentI;
    private Direction prevDir;
    private Direction prevPrevDir;
    private Direction prevPrevPrevDir;
    @Override
    public void takeYourNumber(int number) {
        mynumber = number;
    }

    @Override
    public Direction step(GameState gameState) {
        findMe(gameState.getMap());
        int[][] map = gameState.getMap();
        int[][] analyzedGameState = analyzeMap(gameState.getMap(), 2);
        int upMap = -1000;
        int leftMap = -1000;
        int rightMap = -1000;
        int downMap = -1000;
        if (currentI > 0) upMap = analyzedGameState[currentI - 1][currentJ];
        if (currentI < 14) downMap = analyzedGameState[currentI + 1][currentJ];
        if (currentJ < 14) rightMap = analyzedGameState[currentI][currentJ + 1];
        if (currentJ > 0) leftMap = analyzedGameState[currentI][currentJ - 1];

        if (upMap >= downMap && upMap > leftMap && upMap > rightMap && currentI > 0) {
            prevDir = Direction.UP;
            return Direction.UP;
        }
        if (downMap > upMap && downMap >= leftMap && downMap > rightMap && currentI < 14) {
            prevDir = Direction.BOTTOM;
            return Direction.BOTTOM;
        }
        if (leftMap > upMap && leftMap > downMap && leftMap >= rightMap && currentJ > 0) {
            prevDir = Direction.LEFT;
            return Direction.LEFT;
        }
        if (rightMap >= upMap && rightMap > downMap && rightMap > leftMap && currentJ < 14) {
            prevDir = Direction.RIGHT;
            return Direction.RIGHT;
        }

        if (prevPrevPrevDir != null && prevPrevPrevDir == prevDir) {
            Direction dir = Direction.NONE;
            if (prevDir != Direction.BOTTOM && currentI > 0) dir = Direction.UP;
            if (prevDir != Direction.UP && currentI < 14) dir = Direction.BOTTOM;
            if (prevDir != Direction.LEFT && currentJ < 14) dir = Direction.RIGHT;
            if (prevDir != Direction.RIGHT && currentJ > 0) dir = Direction.LEFT;
            return dir;
        }

        int up = 0;
        if (currentI > 0) up = map[currentI - 1][currentJ];
        int left = 0;
        if (currentJ > 0) left = map[currentI][currentJ - 1];
        int right = 0;
        if (currentJ < 14) right = map[currentI][currentJ + 1];
        int down = 0;
        if (currentI < 14) down = map[currentI + 1][currentJ];

        if (up >= down && up > left && up > right && currentI > 0) {
            prevPrevPrevDir = prevPrevDir;
            prevPrevDir = prevDir;
            prevDir = Direction.UP;
            return Direction.UP;
        }
        if (down > up && down >= left && down > right && currentI < 14) {
            prevPrevPrevDir = prevPrevDir;
            prevPrevDir = prevDir;
            prevDir = Direction.BOTTOM;
            return Direction.BOTTOM;
        }
        if (left > up && left > down && left >= right && currentJ > 0) {
            prevPrevPrevDir = prevPrevDir;
            prevPrevDir = prevDir;
            prevDir = Direction.LEFT;
            return Direction.LEFT;
        }
        if (right >= up && right > down && right > left && currentJ < 14) {
            prevPrevPrevDir = prevPrevDir;
            prevPrevDir = prevDir;
            prevDir = Direction.RIGHT;
            return Direction.RIGHT;
        }



        return Direction.NONE;
    }

    @Override
    public String getTelegramNick() {
        return "@HomerTheBald";
    }

    private void findMe(int[][] map) {
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (map[i][j] == mynumber) {
                    currentJ = j;
                    currentI = i;
                }
            }
        }
    }

    private static int[][] analyzeMap(int[][] map, int weigh) {
        int[][] mergedMap = new int[15][15];
        for (int i1 = 0; i1 < 15; i1++) {
            for (int j1 = 0; j1 < 15; j1++) {
                if (map[i1][j1] > 0) {
                    int[][] subMap = new int[15][15];
                    //Лево верх
                    for (int i2 = 0; i2 <= i1; i2++) {
                        for (int j2 = 0; j2 <= j1; j2++) {
                            if (map[i1][j1] - weigh * (i1 - i2) - weigh * (j1 - j2) > 0)
                                subMap[i2][j2] = map[i1][j1] - weigh * (i1 - i2) - weigh * (j1 - j2);
                        }
                    }
                    //Право низ
                    for (int i2 = i1; i2 < 15; i2++) {
                        for (int j2 = j1; j2 < 15; j2++) {
                            if (map[i1][j1] - weigh * (i2 - i1) - weigh * (j2 - j1) > 0)
                                subMap[i2][j2] = map[i1][j1] - weigh * (i2 - i1) - weigh * (j2 - j1);
                        }
                    }
                    //Лево низ
                    for (int i2 = i1; i2 < 15; i2++) {
                        for (int j2 = 0; j2 < j1; j2++) {
                            if (map[i1][j1] - weigh * (i2 - i1) - weigh * (j1 - j2) > 0)
                                subMap[i2][j2] = map[i1][j1] - weigh * (i2 - i1) - weigh * (j1 - j2);
                        }
                    }
                    //Право верх
                    for (int i2 = 0; i2 < i1; i2++) {
                        for (int j2 = j1; j2 < 15; j2++) {
                            if (map[i1][j1] - weigh * (i1 - i2) - weigh * (j2 - j1) > 0)
                                subMap[i2][j2] = map[i1][j1] - weigh * (i1 - i2) - weigh * (j2 - j1);
                        }
                    }

                    subMap[i1][j1] = 1000 * subMap[i1][j1];

                    for (int i2 = 0; i2 < 15; i2++) {
                        for (int j2 = 0; j2 < 15; j2++) {
                            mergedMap[i2][j2] += subMap[i2][j2];
                        }
                    }
                }
            }
        }
        for (int i1 = 0; i1 < 15; i1++) {
            for (int j1 = 0; j1 < 15; j1++) {
                if (map[i1][j1] != -1 || map[i1][j1] != -2) mergedMap[i1][j1] += map[i1][j1];
            }
        }
        return mergedMap;
    }
}
