package ru.gurps.generator.lib;

import javafx.scene.control.Label;
import ru.gurps.generator.controller.UsersController;
import ru.gurps.generator.models.User;

public class UserParams {
    private User user = UsersController.user;
    private Label stCost;
    private Label dxCost;
    private Label iqCost;
    private Label htCost;
    private Label hpCost;
    private Label willCost;
    private Label perCost;
    private Label fpCost;
    private Label bsCost;
    private Label moveCost;
    private Label bg;
    private Label doge;
    private Label thrust;
    private Label swing;

    public UserParams(Label stCost, Label dxCost, Label iqCost, Label htCost, Label hpCost, Label willCost, Label perCost, Label fpCost, Label bsCost, Label moveCost, Label bg, Label doge, Label thrust, Label swing) {
        this.stCost = stCost;
        this.dxCost = dxCost;
        this.iqCost = iqCost;
        this.htCost = htCost;
        this.hpCost = hpCost;
        this.willCost = willCost;
        this.perCost = perCost;
        this.fpCost = fpCost;
        this.bsCost = bsCost;
        this.moveCost = moveCost;
        this.bg = bg;
        this.doge = doge;
        this.thrust = thrust;
        this.swing = swing;
    }

    public UserParams(Label stCost, Label dxCost, Label iqCost, Label htCost, Label hpCost, Label willCost, Label perCost, Label fpCost, Label bg, Label doge, Label thrust, Label swing) {
        this.stCost = stCost;
        this.dxCost = dxCost;
        this.iqCost = iqCost;
        this.htCost = htCost;
        this.hpCost = hpCost;
        this.willCost = willCost;
        this.perCost = perCost;
        this.fpCost = fpCost;
        this.bg = bg;
        this.doge = doge;
        this.thrust = thrust;
        this.swing = swing;
    }

    public void setSt() {
        int cost = (user.st - 10) * 10;
        if(cost != 0) {
            if(user.noFineManipulators) cost = (int) (cost - (cost * 0.4));
            if(user.sm > 0) cost = user.sm < 8 ? (int) (cost - (cost * (0.1 * user.sm))) : (int) (cost - (cost * 0.8));
        }
        stCost.setText(Integer.toString(cost));
    }

    public void setDx() {
        int cost = (user.dx - 10) * 20;
        if(user.noFineManipulators && cost != 0) cost = (int) (cost - (cost * 0.4));
        dxCost.setText(Integer.toString(cost));
    }

    public void setIq() {
        int cost = (user.iq - 10) * 20;
        iqCost.setText(Integer.toString(cost));
    }

    public void setHt() {
        int cost = (user.ht - 10) * 10;
        htCost.setText(Integer.toString(cost));
    }

    public void setHp() {
        int cost = (user.hp - user.st) * 2;
        if(user.sm > 0 && cost != 0)
            cost = user.sm < 8 ? (int) (cost - (cost * (0.1 * user.sm))) : (int) (cost - (cost * 0.8));
        hpCost.setText(Integer.toString(cost));
    }

    public void setWill() {
        int cost = (user.will - user.iq) * 5;
        willCost.setText(Integer.toString(cost));
    }

    public void setPer() {
        int cost = (user.per - user.iq) * 5;
        perCost.setText(Integer.toString(cost));
    }


    public void setFp() {
        int cost = (user.fp - user.ht) * 3;
        fpCost.setText(Integer.toString(cost));
    }

    public void setBs() {
        double defaultBs = (user.dx + user.ht) / 4;
        int cost = 0;
        double periods;
        periods = user.bs - defaultBs;

        if(periods >= 0.25) {
            do {
                periods = periods - 0.25;
                cost += 5;
            } while(periods >= 0.25);
        } else if(periods <= -0.25) {
            do {
                periods = periods + 0.25;
                cost -= 5;
            } while(periods <= -0.25);
        }

        bsCost.setText(Integer.toString(cost));
    }

    public void setMove() {
        int cost = (int) (user.move - user.bs);
        moveCost.setText(Integer.toString(cost));
    }

    public void setBg() {
        bg.setText(Integer.toString((user.st * user.st) / 5));
    }

    public void setDoge() {
        doge.setText(Integer.toString((int) (user.bs + 3)));
    }

    public void setDmg() {
        thrust.setText(Dmg.thrust(user.st));
        swing.setText(Dmg.swing(user.st));
    }
}
