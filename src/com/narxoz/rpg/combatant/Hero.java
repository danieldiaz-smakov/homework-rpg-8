package com.narxoz.rpg.combatant;

import com.narxoz.rpg.state.HeroState;
import com.narxoz.rpg.state.NormalState;

/**
 * Represents a player-controlled hero participating in the tower climb.
 *
 * Students: you may extend this class as needed for your implementation.
 * You will need to add a HeroState field and related methods.
 */
public class Hero {

    private final String name;
    private int hp;
    private final int maxHp;
    private final int attackPower;
    private final int defense;
    private HeroState state;

    public Hero(String name, int hp, int attackPower, int defense) {
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.attackPower = attackPower;
        this.defense = defense;
        this.state = new NormalState();
    }

    public Hero(String name, int hp, int attackPower, int defense, HeroState initialState) {
        this(name, hp, attackPower, defense);
        setState(initialState);
    }

    public String getName()        { return name; }
    public int getHp()             { return hp; }
    public int getMaxHp()          { return maxHp; }
    public int getAttackPower()    { return attackPower; }
    public int getDefense()        { return defense; }
    public boolean isAlive()       { return hp > 0; }
    public HeroState getState()    { return state; }
    public String getStateName()   { return state.getName(); }

    /**
     * Reduces this hero's HP by the given amount, clamped to zero.
     *
     * @param amount the damage to apply; must be non-negative
     */
    public void takeDamage(int amount) {
        hp = Math.max(0, hp - amount);
    }

    /**
     * Restores this hero's HP by the given amount, clamped to maxHp.
     *
     * @param amount the HP to restore; must be non-negative
     */
    public void heal(int amount) {
        hp = Math.min(maxHp, hp + amount);
    }

    public void setState(HeroState newState) {
        if (newState == null) {
            throw new IllegalArgumentException("Hero state cannot be null");
        }
        String previous = state == null ? "None" : state.getName();
        this.state = newState;
        System.out.println(name + " state change: " + previous + " -> " + newState.getName());
    }

    public void startTurn() {
        state.onTurnStart(this);
    }

    public void endTurn() {
        state.onTurnEnd(this);
    }

    public boolean canAct() {
        return state.canAct();
    }

    public int dealDamage() {
        return Math.max(1, state.modifyOutgoingDamage(attackPower));
    }

    public int takeHit(int rawDamage) {
        int modified = Math.max(1, state.modifyIncomingDamage(rawDamage));
        int finalDamage = Math.max(1, modified - defense);
        takeDamage(finalDamage);
        return finalDamage;
    }
}
