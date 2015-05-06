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

    public static int skillLevel(Skill skill){
        int parameter = 0;
        switch(skill.type){
            case 0:
                parameter = user.st;
                break;
            case 1:
                parameter = user.dx;
                break;
            case 2:
                parameter = user.iq;
                break;
            case 3:
                parameter = user.ht;
                break;
            case 4:
                parameter = user.hp;
                break;
            case 5:
                parameter = user.will;
                break;
            case 6:
                parameter = user.per;
                break;
            case 7:
                parameter = user.fp;
                break;
        }
        switch(skill.complexity){
            case 0: return parameter;
            case 1: return parameter - 1;
            case 2: return parameter - 2;
            case 3: return parameter - 3;
        }

        return 0;
    }

    public static int skillCost(Skill skill){
        int parameter = 0;
        switch(skill.type){
            case 0:
                parameter = user.st;
                break;
            case 1:
                parameter = user.dx;
                break;
            case 2:
                parameter = user.iq;
                break;
            case 3:
                parameter = user.ht;
                break;
            case 4:
                parameter = user.hp;
                break;
            case 5:
                parameter = user.will;
                break;
            case 6:
                parameter = user.per;
                break;
            case 7:
                parameter = user.fp;
                break;
        }

        int i = 0;
        int cost = 0;
        switch(skill.complexity) {
            case 0:
                if(skill.level <= parameter) return 1;
                else if(skill.level <= parameter + 1) return 2;
                else if(skill.level <= parameter + 2) return 4;
                else if(skill.level <= parameter + 3) return 8;
                else {
                    i = skill.level;
                    cost = 8;
                    break;
                }

            case 1:
                if(skill.level <= parameter - 1) return 1;
                else if(skill.level <= parameter) return 2;
                else if(skill.level <= parameter + 1) return 4;
                else if(skill.level <= parameter + 2) return 8;
                else if(skill.level <= parameter + 3) return 12;
                else {
                    i = skill.level;
                    cost = 12;
                    break;
                }

            case 2:
                if(skill.level <= parameter - 2) return 1;
                else if(skill.level <= parameter - 1) return 2;
                else if(skill.level <= parameter) return 4;
                else if(skill.level <= parameter + 1) return 8;
                else if(skill.level <= parameter + 2) return 12;
                else if(skill.level <= parameter + 3) return 16;
                else {
                    i = skill.level;
                    cost = 16;
                    break;
                }
            case 3:
                if(skill.level <= parameter - 3) return 1;
                else if(skill.level <= parameter - 2) return 2;
                else if(skill.level <= parameter - 1) return 4;
                else if(skill.level <= parameter) return 8;
                else if(skill.level <= parameter + 1) return 12;
                else if(skill.level <= parameter + 2) return 16;
                else if(skill.level <= parameter + 3) return 20;
                else {
                    i = skill.level;
                    cost = 20;
                    break;
                }
        }
        while(i > parameter + 3) {
            cost += 4;
            i--;
        }
        return cost;
    }
}
