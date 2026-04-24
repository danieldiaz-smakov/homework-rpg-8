package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.combatant.Monster;
import com.narxoz.rpg.state.StunnedState;
import java.util.List;

public class BossFloor extends TowerFloor {
    private final String floorName;
    private Monster boss;

    public BossFloor(String floorName) {
        this.floorName = floorName;
    }

    @Override
    protected void announce() {
        System.out.println("\n=== " + floorName + " ===");
        System.out.println("The tower shakes as the final guardian appears.");
    }

    @Override
    protected void setup(List<Hero> party) {
        boss = new Monster("Wraith King", 85, 16);
        System.out.println("Setup: " + boss.getName() + " materializes with " + boss.getHp() + " HP.");
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        System.out.println("Challenge: boss battle begins.");
        int totalDamage = 0;
        int phase = 1;

        while (boss.isAlive() && anyHeroAlive(party)) {
            System.out.println("Phase " + phase++);
            for (Hero hero : party) {
                if (!hero.isAlive() || !boss.isAlive()) {
                    continue;
                }
                hero.startTurn();
                if (!hero.isAlive()) {
                    continue;
                }
                if (hero.canAct()) {
                    int damage = hero.dealDamage();
                    boss.takeDamage(damage);
                    System.out.println(hero.getName() + " strikes " + boss.getName() + " for " + damage
                            + " (Boss HP=" + boss.getHp() + ")");
                } else {
                    System.out.println(hero.getName() + " cannot attack due to " + hero.getStateName());
                }
                hero.endTurn();
            }

            if (boss.isAlive()) {
                Hero target = firstAliveHero(party);
                int before = target.getHp();
                target.takeHit(boss.getAttackPower());
                int dealt = Math.max(0, before - target.getHp());
                totalDamage += dealt;
                System.out.println(boss.getName() + " slams " + target.getName() + " for " + dealt
                        + " (Hero HP=" + target.getHp() + ")");
                if (target.isAlive() && dealt >= 9) {
                    target.setState(new StunnedState(1));
                }
            }
        }

        boolean cleared = !boss.isAlive() && anyHeroAlive(party);
        String summary = cleared ? "The boss has fallen." : "The tower claims the party.";
        return new FloorResult(cleared, totalDamage, summary);
    }

    @Override
    protected boolean shouldAwardLoot(FloorResult result) {
        return result.isCleared();
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
        System.out.println("Loot: the relic heals each survivor by 8 HP.");
        for (Hero hero : party) {
            if (hero.isAlive()) {
                hero.heal(8);
            }
        }
    }

    @Override
    protected String getFloorName() {
        return floorName;
    }

    @Override
    protected void cleanup(List<Hero> party) {
        System.out.println("Cleanup: the throne room falls silent.");
    }

    private static boolean anyHeroAlive(List<Hero> party) {
        for (Hero hero : party) {
            if (hero.isAlive()) {
                return true;
            }
        }
        return false;
    }

    private static Hero firstAliveHero(List<Hero> party) {
        for (Hero hero : party) {
            if (hero.isAlive()) {
                return hero;
            }
        }
        return party.get(0);
    }
}
