package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.state.NormalState;
import java.util.List;

public class RestFloor extends TowerFloor {
    private final String floorName;
    private final int healAmount;

    public RestFloor(String floorName, int healAmount) {
        this.floorName = floorName;
        this.healAmount = healAmount;
    }

    @Override
    protected void announce() {
        System.out.println("\n--- " + floorName + " ---");
        System.out.println("A rare safe room in the haunted tower.");
    }

    @Override
    protected void setup(List<Hero> party) {
        System.out.println("Setup: the campfire is lit.");
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        System.out.println("Challenge: there is no combat, only recovery.");
        for (Hero hero : party) {
            if (hero.isAlive()) {
                hero.heal(healAmount);
                hero.setState(new NormalState());
                System.out.println(hero.getName() + " recovers " + healAmount + " HP and calms down.");
            }
        }
        return new FloorResult(true, 0, "The party rested successfully.");
    }

    @Override
    protected boolean shouldAwardLoot(FloorResult result) {
        return false;
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
        System.out.println("No loot should be awarded on this floor.");
    }

    @Override
    protected void cleanup(List<Hero> party) {
        System.out.println("Cleanup: ashes of the campfire are scattered.");
    }

    @Override
    protected String getFloorName() {
        return floorName;
    }
}
