package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.combatant.Monster;
import com.narxoz.rpg.state.BerserkState;
import com.narxoz.rpg.state.PoisonedState;
import java.util.ArrayList;
import java.util.List;

public class CombatFloor extends TowerFloor {
    private final String floorName;
    private final List<Monster> monsters;

    public CombatFloor(String floorName, List<Monster> monsters) {
        this.floorName = floorName;
        this.monsters = new ArrayList<>(monsters);
    }

    @Override
    protected void setup(List<Hero> party) {
        System.out.println("Setup: " + monsters.size() + " monsters emerge.");
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        System.out.println("Challenge: combat starts.");
        int totalDamageTaken = 0;

        int round = 1;
        while (anyHeroAlive(party) && anyMonsterAlive(monsters)) {
            System.out.println("Round " + round++);
            for (Hero hero : party) {
                if (!hero.isAlive() || !anyMonsterAlive(monsters)) {
                    continue;
                }
                hero.startTurn();
                if (!hero.isAlive()) {
                    continue;
                }
                if (hero.canAct()) {
                    Monster target = firstAliveMonster(monsters);
                    int damage = hero.dealDamage();
                    target.takeDamage(damage);
                    System.out.println(hero.getName() + " [" + hero.getStateName() + "] hits "
                            + target.getName() + " for " + damage + " (Monster HP=" + target.getHp() + ")");
                    if (!target.isAlive()) {
                        System.out.println(target.getName() + " is defeated.");
                    }
                } else {
                    System.out.println(hero.getName() + " loses the turn due to " + hero.getStateName());
                }
                hero.endTurn();
            }

            for (Monster monster : monsters) {
                if (!monster.isAlive() || !anyHeroAlive(party)) {
                    continue;
                }
                Hero target = firstAliveHero(party);
                int before = target.getHp();
                target.takeHit(monster.getAttackPower());
                int dealt = Math.max(0, before - target.getHp());
                totalDamageTaken += dealt;
                System.out.println(monster.getName() + " hits " + target.getName() + " for " + dealt
                        + " (Hero HP=" + target.getHp() + ")");

                if (target.isAlive() && target.getHp() <= target.getMaxHp() / 3
                        && !"Berserk".equals(target.getState().getName())) {
                    target.setState(new BerserkState());
                }
                if (target.isAlive() && dealt >= 8 && round % 2 == 0) {
                    target.setState(new PoisonedState(2, 2));
                }
            }
        }

        boolean cleared = anyHeroAlive(party) && !anyMonsterAlive(monsters);
        String summary = cleared ? "Combat won." : "Party was defeated in combat.";
        return new FloorResult(cleared, totalDamageTaken, summary);
    }

    @Override
    protected boolean shouldAwardLoot(FloorResult result) {
        return result.isCleared();
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
        System.out.println("Loot: victors recover 4 HP.");
        for (Hero hero : party) {
            if (hero.isAlive()) {
                hero.heal(4);
            }
        }
    }

    @Override
    protected void cleanup(List<Hero> party) {
        System.out.println("Cleanup: bodies are removed from the hall.");
    }

    @Override
    protected String getFloorName() {
        return floorName;
    }

    private static boolean anyHeroAlive(List<Hero> party) {
        for (Hero hero : party) {
            if (hero.isAlive()) {
                return true;
            }
        }
        return false;
    }

    private static boolean anyMonsterAlive(List<Monster> monsters) {
        for (Monster monster : monsters) {
            if (monster.isAlive()) {
                return true;
            }
        }
        return false;
    }

    private static Monster firstAliveMonster(List<Monster> monsters) {
        for (Monster monster : monsters) {
            if (monster.isAlive()) {
                return monster;
            }
        }
        return monsters.get(0);
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
