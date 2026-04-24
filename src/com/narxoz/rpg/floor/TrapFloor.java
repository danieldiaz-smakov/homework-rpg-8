package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.state.PoisonedState;
import com.narxoz.rpg.state.StunnedState;
import java.util.List;

public class TrapFloor extends TowerFloor {
    private final String floorName;
    private final int trapDamage;

    public TrapFloor(String floorName, int trapDamage) {
        this.floorName = floorName;
        this.trapDamage = trapDamage;
    }

    @Override
    protected void setup(List<Hero> party) {
        System.out.println("Setup: pressure plates and darts are detected.");
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        System.out.println("Challenge: everyone attempts to cross the corridor.");
        int totalDamage = 0;
        int index = 0;
        for (Hero hero : party) {
            if (!hero.isAlive()) {
                continue;
            }
            int before = hero.getHp();
            hero.takeHit(trapDamage);
            int dealt = Math.max(0, before - hero.getHp());
            totalDamage += dealt;
            System.out.println(hero.getName() + " takes " + dealt + " trap damage (HP=" + hero.getHp() + ")");

            if (hero.isAlive()) {
                if (index % 2 == 0) {
                    hero.setState(new StunnedState(1));
                } else {
                    hero.setState(new PoisonedState(2, 2));
                }
            }
            index++;
        }

        boolean cleared = false;
        for (Hero hero : party) {
            if (hero.isAlive()) {
                cleared = true;
                break;
            }
        }
        String summary = cleared ? "The trap corridor is bypassed." : "All heroes perished in traps.";
        return new FloorResult(cleared, totalDamage, summary);
    }

    @Override
    protected boolean shouldAwardLoot(FloorResult result) {
        return result.isCleared();
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
        System.out.println("Loot: survivors find antitoxin and heal 3 HP.");
        for (Hero hero : party) {
            if (hero.isAlive()) {
                hero.heal(3);
            }
        }
    }

    @Override
    protected String getFloorName() {
        return floorName;
    }

    @Override
    protected void cleanup(List<Hero> party) {
        System.out.println("Cleanup: trapped mechanisms reset behind the party.");
    }
}
