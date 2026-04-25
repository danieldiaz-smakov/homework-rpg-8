package com.narxoz.rpg;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.combatant.Monster;
import com.narxoz.rpg.floor.BossFloor;
import com.narxoz.rpg.floor.CombatFloor;
import com.narxoz.rpg.floor.RestFloor;
import com.narxoz.rpg.floor.TowerFloor;
import com.narxoz.rpg.floor.TrapFloor;
import com.narxoz.rpg.state.BerserkState;
import com.narxoz.rpg.state.NormalState;
import com.narxoz.rpg.state.PoisonedState;
import com.narxoz.rpg.tower.TowerRunResult;
import com.narxoz.rpg.tower.TowerRunner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Entry point for Homework 8 — The Haunted Tower: Ascending the Floors.
 *
 * Build your heroes, floors, tower runner, and execute the climb here.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("=== State Demo: Berserk Preview ===");
        Hero previewHero = new Hero("Rogan", 30, 10, 1, new BerserkState());
        System.out.println("Preview state: " + previewHero.getStateName());
        System.out.println("Preview outgoing damage in Berserk: " + previewHero.dealDamage());
        previewHero.heal(30); // bring HP above half; Berserk should self-transition on turn start
        previewHero.startTurn();
        System.out.println("State after Berserk turn-start check: " + previewHero.getStateName());

        System.out.println("\n=== Tower Run Demo ===");
        Hero alaric = new Hero("Alaric", 70, 14, 3, new NormalState());
        Hero mira = new Hero("Mira", 58, 12, 2, new PoisonedState(2, 2));

        List<Hero> party = new ArrayList<>(Arrays.asList(alaric, mira));

        List<TowerFloor> floors = Arrays.asList(
                new CombatFloor(
                        "Floor 1: Skeleton Hall",
                        Arrays.asList(new Monster("Skeleton", 22, 8), new Monster("Ghoul", 28, 9))
                ),
                new TrapFloor("Floor 2: Needle Gallery", 9),
                new RestFloor("Floor 3: Whispering Sanctuary", 6),
                new CombatFloor(
                        "Floor 4: Cursed Barracks",
                        Arrays.asList(new Monster("Knight Shade", 32, 12), new Monster("Bone Archer", 24, 11))
                ),
                new BossFloor("Floor 5: Throne of the Wraith King")
        );

        TowerRunner runner = new TowerRunner();
        TowerRunResult result = runner.run(party, floors);

        System.out.println("\n=== Final Tower Result ===");
        System.out.println("Floors cleared: " + result.getFloorsCleared());
        System.out.println("Heroes surviving: " + result.getHeroesSurviving());
        System.out.println("Reached top: " + result.isReachedTop());
        System.out.println("\n=== Party Status ===");
        for (Hero hero : party) {
            System.out.println(hero.getName() + " HP=" + hero.getHp() + " State=" + hero.getStateName());
        }
    }
}
