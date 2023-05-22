package metrics;

import engine.core.MarioEvent;
import engine.core.MarioGame;
import engine.core.MarioResult;
import engine.helper.EventType;
import org.jetbrains.annotations.NotNull;

public class AirTimeMetricJava extends AbstractMetric {
    @NotNull
    @Override
    public String getName() {
        return "air_time";
    }

    @NotNull
    @Override
    public String getValue(@NotNull String level) {
        MarioResult results = runBaumgartenAgent(level);

        int lastLandTime = 0;
        int firstJumpTime = -1;

        for (MarioEvent gameEvent: results.getGameEvents()) {
            if (gameEvent.getEventType() == EventType.LAND.getValue()) {
                lastLandTime = gameEvent.getTime();
            } else if (gameEvent.getEventType() == EventType.JUMP.getValue() && firstJumpTime == -1) {
                firstJumpTime = gameEvent.getTime();
            }
        }

        int airTime = lastLandTime - firstJumpTime;
        return Integer.toString(airTime);
    }

    private MarioResult runBaumgartenAgent(String level) {
        MarioGame game = new MarioGame();
        return game.runGame(
                new agents.robinBaumgarten.Agent(),
                level,
                200,
                0
        );
    }
}

