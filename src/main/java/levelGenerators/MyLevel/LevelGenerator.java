package levelGenerators.MyLevel;

import engine.core.MarioLevelGenerator;
import engine.core.MarioLevelModel;
import engine.core.MarioTimer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LevelGenerator implements MarioLevelGenerator {
    private final int GROUND_Y_LOCATION = 13;
    private final float GROUND_PROB = 0.4f;
    private final int OBSTACLES_LOCATION = 10;
    private final float OBSTACLES_PROB = 0.1f;
    private final int COLLECTIBLE_LOCATION = 3;
    private final float COLLECTIBLE_PROB = 0.05f;
    private final float ENMEY_PROB = 0.1f;
    private final int FLOOR_PADDING = 3;
    private final Random rnd = new Random();

    private int lastObstacleX = -1;
    private int lastCollectibleX = -1;

    private void placePipe(MarioLevelModel model, int x, int y, int height) {
        char pipeType = MarioLevelModel.PIPE;
        if (this.rnd.nextDouble() < 0.2) {
            pipeType = MarioLevelModel.PIPE_FLOWER;
        }
        model.setRectangle(x, y - height + 1, 2, height, pipeType);
    }

    @Override
    public String getGeneratedLevel(MarioLevelModel model, MarioTimer timer) {
        model.clearMap();

        int numGaps = 3; // number of gaps to be generated
        int numEnemies = 5; // number of enemies to be generated
        int numObstacles = 7; // number of obstacles to be generated

        int gapSize = model.getWidth() / (numGaps + 1); // calculate the size of each gap
        int enemySpacing = model.getWidth() / (numEnemies + 1); // calculate the spacing between enemies
        int obstacleSpacing = model.getWidth() / (numObstacles + 1); // calculate the spacing between obstacles

        List<Integer> gapPositions = generateRandomPositions(numGaps, gapSize);
        List<Integer> enemyPositions = generateRandomPositions(numEnemies, enemySpacing);
        List<Integer> obstaclePositions = generateRandomPositions(numObstacles, obstacleSpacing);

        // Generate ground and obstacles
        for (int x = 0; x < model.getWidth(); x++) {
            for (int y = 0; y < model.getHeight(); y++) {
                model.setBlock(x, y, MarioLevelModel.EMPTY);

                if (y > GROUND_Y_LOCATION) {
                    if (rnd.nextDouble() < GROUND_PROB) {
                        model.setBlock(x, y, MarioLevelModel.GROUND);
                    }
                } else if (y > OBSTACLES_LOCATION) {
                    if (obstaclePositions.contains(x)) {
                        int obstacleHeight = rnd.nextInt(3) + 1; // Vary obstacle height
                        model.setRectangle(x, GROUND_Y_LOCATION - obstacleHeight, 1, obstacleHeight, MarioLevelModel.PYRAMID_BLOCK);
                        if (obstacleHeight > 1 && rnd.nextDouble() < 0.3) { // Add pipes on top of the obstacle
                            placePipe(model, x, GROUND_Y_LOCATION - obstacleHeight, obstacleHeight - 1);
                        }
                    }
                } else if (y > COLLECTIBLE_LOCATION) {
                    // Generate collectibles
                }
            }
        }

        // Generate gaps
        for (int gapPosition : gapPositions) {
            model.setRectangle(gapPosition, GROUND_Y_LOCATION - 1, gapSize, 1, MarioLevelModel.EMPTY);
        }

        // Generate enemies
        for (int enemyPosition : enemyPositions) {
            model.setBlock(enemyPosition, GROUND_Y_LOCATION - 1,
                    MarioLevelModel.getEnemyCharacters()[rnd.nextInt(MarioLevelModel.getEnemyCharacters().length)]);
        }

        // Generate floor and start/end points
        model.setRectangle(0, 14, FLOOR_PADDING, 2, MarioLevelModel.GROUND);
        model.setRectangle(model.getWidth() - 1 - FLOOR_PADDING, 14, FLOOR_PADDING, 2, MarioLevelModel.GROUND);
        model.setBlock(FLOOR_PADDING / 2, 13, MarioLevelModel.MARIO_START);
        model.setBlock(model.getWidth() - 1 - FLOOR_PADDING / 2, 13, MarioLevelModel.MARIO_EXIT);

        return model.getMap();
    }

    private List<Integer> generateRandomPositions(int count, int spacing) {
        List<Integer> positions = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            int position = i * spacing + rnd.nextInt(spacing);
            positions.add(position);
        }
        return positions;
    }

    @Override
    public String getGeneratorName() {
        return "CustomLevelGenerator";
    }

}