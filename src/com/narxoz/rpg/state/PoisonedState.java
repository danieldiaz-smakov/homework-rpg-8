package com.narxoz.rpg.state;

import com.narxoz.rpg.combatant.Hero;

public class PoisonedState implements HeroState {
    private int turnsRemaining;
    private final int poisonTickDamage;

    public PoisonedState(int turnsRemaining, int poisonTickDamage) {
        this.turnsRemaining = Math.max(1, turnsRemaining);
        this.poisonTickDamage = Math.max(1, poisonTickDamage);
    }

    @Override
    public String getName() {
        return "Poisoned(" + turnsRemaining + ")";
    }

    @Override
    public int modifyOutgoingDamage(int basePower) {
        return Math.max(1, (int) Math.floor(basePower * 0.75));
    }

    @Override
    public int modifyIncomingDamage(int rawDamage) {
        return Math.max(1, (int) Math.ceil(rawDamage * 1.15));
    }

    @Override
    public void onTurnStart(Hero hero) {
        hero.takeDamage(poisonTickDamage);
        System.out.println(hero.getName() + " suffers " + poisonTickDamage + " poison damage. HP=" + hero.getHp());
    }

    @Override
    public void onTurnEnd(Hero hero) {
        turnsRemaining--;
        if (turnsRemaining <= 0 && hero.isAlive()) {
            hero.setState(new NormalState());
        }
    }

    @Override
    public boolean canAct() {
        return true;
    }
}
