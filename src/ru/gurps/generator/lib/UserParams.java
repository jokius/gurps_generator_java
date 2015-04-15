package ru.gurps.generator.lib;

import javafx.collections.ObservableList;
import ru.gurps.generator.controller.AbstractController;
import ru.gurps.generator.models.Skill;

public class UserParams extends AbstractController {
    public static int stCost(){
        int cost = (user.st - 10) * 10;
        if(cost != 0) {
            if(user.noFineManipulators) cost = (int) (cost - (cost * 0.4));
            if(user.sm > 0) cost = user.sm < 8 ? (int) (cost - (cost * (0.1 * user.sm))) : (int) (cost - (cost * 0.8));
        }
        return cost;
    }

    public static int dxCost(){
        int cost = (user.dx - 10) * 20;
        if(user.noFineManipulators && cost != 0) cost = (int) (cost - (cost * 0.4));
        return cost;
    }

    public static int iqCost(){
        return (user.iq - 10) * 20;
    }

    public static int htCost(){
        return (user.ht - 10) * 10;
    }

    public static int hpCost(){
        int cost = (user.hp - user.st) * 2;
        if(user.sm > 0 && cost != 0)
            cost = user.sm < 8 ? (int) (cost - (cost * (0.1 * user.sm))) : (int) (cost - (cost * 0.8));
        return cost;
    }

    public static int willCost(){
        return (user.will - user.iq) * 5;
    }

    public static int perCost(){
        return (user.per - user.iq) * 5;
    }

    public static int fpCost(){
        return (user.fp - user.ht) * 3;
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

    public static int moveCost(){
        return (int) (user.move - user.bs);
    }

    public static int bg(){
        return (user.st * user.st) / 5;
    }

    public static int doge(){
        return (int) (user.bs + 3);
    }

    public static String getParry(ObservableList<Skill> skills) {
        int resultSkill = user.dx - 4;
        for(Skill skill : skills)
            if(skill.parry && skill.level > resultSkill) resultSkill = skill.level + skill.parryBonus;
        return Integer.toString(3 + resultSkill / 2);
    }

    public static String getBlock(ObservableList<Skill> skills) {
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
}
