package metrics

import adapters.UniversalMarioGame
import engine.helper.EventType

class AirTimeMetric: AbstractMetric() {
    override val name = "air_time"

    override fun getValue(level: String): String {
        val game = UniversalMarioGame()
        val results = game.runGame(
                mff.agents.astar.Agent(),
                level,
                200,
                0
        )

        var lastLandTime = 0
        var firstJumpTime = -1

        for (gameEvent in results.gameEvents) {
            when (gameEvent.eventType) {
                EventType.LAND.value -> lastLandTime = gameEvent.time
                EventType.JUMP.value -> {
                    if (firstJumpTime == -1) {
                        firstJumpTime = gameEvent.time
                    }
                }
            }
        }

        val airTime = lastLandTime - firstJumpTime
        return airTime.toString()
    }
}
