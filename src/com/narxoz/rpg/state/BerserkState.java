package com.narxoz.rpg.state;

import com.narxoz.rpg.combatant.Hero;

public class BerserkState implements HeroState {
    @Override
    public String getName() {
        return "Berserk";
    }

    @Override
    public int modifyOutgoingDamage(int basePower) {
        return Math.max(1, (int) Math.ceil(basePower * 1.6));
    }

    @Override
    public int modifyIncomingDamage(int rawDamage) {
        return Math.max(1, (int) Math.ceil(rawDamage * 1.35));
    }

    @Override
    public void onTurnStart(Hero hero) {
        if (hero.getHp() > hero.getMaxHp() / 2) {
            hero.setState(new NormalState());
        }
    }

    @Override
    public void onTurnEnd(Hero hero) {
        // no-op
    }

    @Override
    public boolean canAct() {
        return true;
    }
}
