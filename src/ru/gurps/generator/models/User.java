package ru.gurps.generator.models;

import ru.gurps.generator.config.Model;

public class User extends Model {
    public int id;
    public String name;
    public String currentPoints;
    public String maxPoints;
    public int st = 10;
    public int dx = 10;
    public int iq = 10;
    public int ht = 10;
    public int hp = 10;
    public int will = 10;
    public int per = 10;
    public int fp = 10;
    public double bs = 5.0;
    public int move = 5;
    public int sm = 0;
    public boolean noFineManipulators = false;
    
    public User(){
    }

    public User(String name, String maxPoints) {
        this.name = name;
        this.maxPoints = maxPoints;
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
