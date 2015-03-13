package ru.gurps.generator.models;

import ru.gurps.generator.config.Model;

public class User extends Model {
    public int id;
    public String name;
    public String currentPoints;
    public String maxPoints;
    public int st;
    public int dx;
    public int iq;
    public int ht;
    public int hp;
    public int will;
    public int per;
    public int fp;
    public double bs;
    public int move;
    public int sm;
    public boolean noFineManipulators;
    
    public User(){
    }

    public User(int id, String name, String currentPoints, String maxPoints, int st, int dx, int iq, int ht, int hp, int will, int per, int fp, double bs, int move, int sm, boolean noFineManipulators) {
        this.id = id;
        this.name = name;
        this.currentPoints = currentPoints;
        this.maxPoints = maxPoints;
        this.st = st;
        this.dx = dx;
        this.iq = iq;
        this.ht = ht;
        this.hp = hp;
        this.will = will;
        this.per = per;
        this.fp = fp;
        this.bs = bs;
        this.move = move;
        this.sm = sm;
        this.noFineManipulators = noFineManipulators;
    }

    public String getName() {
        return name;
    }

    public String getCurrentPoints() {
        return currentPoints;
    }

    public String getMaxPoints() {
        return maxPoints;
    }
}
