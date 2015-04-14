package ru.gurps.generator.lib;

import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import ru.gurps.generator.controller.AbstractController;
import ru.gurps.generator.models.Skill;

public class UserParams extends AbstractController {
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
    private Label block;
    private Label parry;

    public UserParams() {
    }

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

    public UserParams(Label stCost, Label dxCost, Label iqCost, Label htCost, Label hpCost, Label willCost, Label perCost, Label fpCost, Label bsCost, Label moveCost, Label bg, Label doge, Label thrust, Label swing, Label block, Label parry) {
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
        this.block = block;
        this.parry = parry;
    }

    public void setSt() {
        stCost.setText(Integer.toString(stCost()));
    }

    public static int stCost(){
        int cost = (user.st - 10) * 10;
        if(cost != 0) {
            if(user.noFineManipulators) cost = (int) (cost - (cost * 0.4));
            if(user.sm > 0) cost = user.sm < 8 ? (int) (cost - (cost * (0.1 * user.sm))) : (int) (cost - (cost * 0.8));
        }
        return cost;
    }

    public void setDx() {
        dxCost.setText(Integer.toString(dxCost()));
    }

    public static int dxCost(){
        int cost = (user.dx - 10) * 20;
        if(user.noFineManipulators && cost != 0) cost = (int) (cost - (cost * 0.4));
        return cost;
    }

    public void setIq() {
        iqCost.setText(Integer.toString(iqCost()));
    }

    public static int iqCost(){
        return (user.iq - 10) * 20;
    }

    public void setHt() {
        htCost.setText(Integer.toString(htCost()));
    }

    public static int htCost(){
        return (user.ht - 10) * 10;
    }

    public void setHp() {
        hpCost.setText(Integer.toString(hpCost()));
    }

    public static int hpCost(){
        int cost = (user.hp - user.st) * 2;
        if(user.sm > 0 && cost != 0)
            cost = user.sm < 8 ? (int) (cost - (cost * (0.1 * user.sm))) : (int) (cost - (cost * 0.8));
        return cost;
    }

    public void setWill() {
        willCost.setText(Integer.toString(willCost()));
    }

    public static int willCost(){
        return (user.will - user.iq) * 5;
    }

    public void setPer() {
        perCost.setText(Integer.toString(perCost()));
    }

    public static int perCost(){
        return (user.per - user.iq) * 5;
    }

    public void setFp() {
        fpCost.setText(Integer.toString(fpCost()));
    }

    public static int fpCost(){
        return (user.fp - user.ht) * 3;
    }

    public void setBs() {
        bsCost.setText(Integer.toString(bsCost()));
    }

    public static double defaultBs()
    {
        return (user.dx + user.ht) / 4;
    }

    public static int bsCost(){
        int cost = 0;
        double periods;
        periods = user.bs - defaultBs();

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
        return cost;
    }

    public void setMove() {
        moveCost.setText(Integer.toString(moveCost()));
    }

    public static int moveCost(){
        return (int) (user.move - user.bs);
    }

    public void setBg() {
        bg.setText(Integer.toString(bg()));
    }

    public static int bg(){
        return (user.st * user.st) / 5;
    }

    public void setDoge() {
        doge.setText(Integer.toString(doge()));
    }

    public static int doge(){
        return (int) (user.bs + 3);
    }

    public void setDmg() {
        thrust.setText(Dmg.thrust(user.st));
        swing.setText(Dmg.swing(user.st));
    }

    public String getParry(ObservableList<Skill> skills) {
        int resultSkill = user.dx - 4;
        for(Skill skill : skills)
            if(skill.parry && skill.level > resultSkill) resultSkill = skill.level + skill.parryBonus;
        return Integer.toString(3 + resultSkill / 2);
    }

    public void setParry(ObservableList<Skill> skills) {
        parry.setText(getParry(skills));
    }


    public String getBlock(ObservableList<Skill> skills) {
        int shield = user.dx - 4;
        int cloak = 0;
        for(Skill skill : skills){
            switch(skill.nameEn){
                case "Shield": shield = skill.level;
                case "Cloak": cloak = skill.level;
                case "Net": if(cloak == 0) cloak = skill.level - 4;
            }
        }

        if(cloak == 0) cloak = user.dx - 5;
        if(shield >= cloak) return Integer.toString(3 + shield / 2);
        else return Integer.toString(3 + cloak / 2);
    }

    public void setBlock(ObservableList<Skill> skills) {
        block.setText(getBlock(skills));
    }
}
