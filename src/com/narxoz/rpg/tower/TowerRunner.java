package com.narxoz.rpg.tower;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.floor.FloorResult;
import com.narxoz.rpg.floor.TowerFloor;
import java.util.List;

public class TowerRunner {
    public TowerRunResult run(List<Hero> party, List<TowerFloor> floors) {
        int floorsCleared = 0;

        for (TowerFloor floor : floors) {
            if (countAlive(party) == 0) {
                break;
            }

            FloorResult result = floor.explore(party);
            System.out.println("Floor result: " + result.getSummary() + " DamageTaken=" + result.getDamageTaken());
            if (result.isCleared()) {
                floorsCleared++;
            } else {
                break;
            }
        }

        int survivors = countAlive(party);
        boolean reachedTop = floorsCleared == floors.size() && survivors > 0;
        return new TowerRunResult(floorsCleared, survivors, reachedTop);
    }

    private int countAlive(List<Hero> party) {
        int alive = 0;
        for (Hero hero : party) {
            if (hero.isAlive()) {
                alive++;
            }
        }
        return alive;
    }
}
