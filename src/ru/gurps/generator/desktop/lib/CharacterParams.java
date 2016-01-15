package ru.gurps.generator.desktop.lib;

import javafx.collections.ObservableList;
import ru.gurps.generator.desktop.Main;
import ru.gurps.generator.desktop.controller.helpers.AbstractController;
import ru.gurps.generator.desktop.models.rules.*;

public class CharacterParams extends AbstractController {
    public static int stCost() {
        int cost = (character.st - 10) * 10;
        if (cost == 0) return cost;
        if (character.noFineManipulators) cost = (int) (cost - (cost * 0.4));
        return costThroughSm(cost);
    }

    public static int dxCost() {
        int cost = (character.dx - 10) * 20;
        if (character.noFineManipulators && cost != 0) cost = (int) (cost - (cost * 0.4));
        return cost;
    }

    public static int iqCost() {
        return (character.iq - 10) * 20;
    }

    public static int htCost() {
        return (character.ht - 10) * 10;
    }

    public static int hpCost() {
        int cost = (character.hp - character.st) * 2;
        if (cost == 0) return cost;
        return costThroughSm(cost);
    }

    public static int willCost() {
        return (character.will - character.iq) * 5;
    }

    public static int perCost() {
        return (character.per - character.iq) * 5;
    }

    public static int fpCost() {
        return (character.fp - character.ht) * 3;
    }

    public static double defaultBs() {
        return (character.dx + character.ht) / 4;
    }

    public static int bsCost() {
        int cost = 0;
        double periods;
        periods = character.bs - defaultBs();

        if (periods >= 0.25) {
            do {
                periods = periods - 0.25;
                cost += 5;
            } while (periods >= 0.25);
        } else if (periods <= -0.25) {
            do {
                periods = periods + 0.25;
                cost -= 5;
            } while (periods <= -0.25);
        }
        return cost;
    }

    public static int moveCost() {
        return (int) (character.move - character.bs);
    }

    public static int bg() {
        return (character.st * character.st) / 5;
    }

    public static int doge() {
        return (int) (character.bs + 3);
    }

    public static String getParry(ObservableList<Skill> skills) {
        int resultSkill = character.dx - 4;
        for (Skill skill : skills)
            if (skill.parry && skill.level > resultSkill) resultSkill = skill.level + skill.parryBonus;
        return Integer.toString(3 + resultSkill / 2);
    }

    public static String getBlock(ObservableList<Skill> skills) {
        int shield = character.dx - 4;
        int cloak = 0;
        for (Skill skill : skills) {
            switch (skill.nameEn) {
                case "Shield":
                    shield = skill.level;
                case "Cloak":
                    cloak = skill.level;
                case "Net":
                    if (cloak == 0) cloak = skill.level - 4;
            }
        }

        if (cloak == 0) cloak = character.dx - 5;
        if (shield >= cloak) return Integer.toString(3 + shield / 2);
        else return Integer.toString(3 + cloak / 2);
    }

    public static int skillLevel(Specialization skill) {
        return skillLevelResult(skill.skillType, skill.complexity);
    }

    public static int skillLevel(Skill skill) {
        return skillLevelResult(skill.skillType, skill.complexity);
    }

    public static int skillLevelResult(int type, int complexity) {
        int parameter = 0;
        switch (type) {
            case 0:
                parameter = character.st;
                break;
            case 1:
                parameter = character.dx;
                break;
            case 2:
                parameter = character.iq;
                break;
            case 3:
                parameter = character.ht;
                break;
            case 4:
                parameter = character.hp;
                break;
            case 5:
                parameter = character.will;
                break;
            case 6:
                parameter = character.per;
                break;
            case 7:
                parameter = character.fp;
                break;
        }
        switch (complexity) {
            case 0:
                return parameter;
            case 1:
                return parameter - 1;
            case 2:
                return parameter - 2;
            case 3:
                return parameter - 3;
        }

        return 0;
    }


    public static int skillCost(Specialization skill) {
        return skillCostResult(skill.skillType, skill.complexity, skill.level);
    }

    public static int skillCost(Skill skill) {
        return skillCostResult(skill.skillType, skill.complexity, skill.level);
    }

    public static int techniqueCost(Technique technique) {
        if (technique.level == 0) return 0;
        if (technique.complexity == 1) return technique.level;
        else return technique.level + 1;
    }

    public static int skillCostResult(int type, int complexity, int level) {
        int parameter = 0;
        switch (type) {
            case 0:
                parameter = character.st;
                break;
            case 1:
                parameter = character.dx;
                break;
            case 2:
                parameter = character.iq;
                break;
            case 3:
                parameter = character.ht;
                break;
            case 4:
                parameter = character.hp;
                break;
            case 5:
                parameter = character.will;
                break;
            case 6:
                parameter = character.per;
                break;
            case 7:
                parameter = character.fp;
                break;
        }

        int i = 0;
        int cost = 0;
        switch (complexity) {
            case 0:
                if (level <= parameter) return 1;
                else if (level <= parameter + 1) return 2;
                else if (level <= parameter + 2) return 4;
                else if (level <= parameter + 3) return 8;
                else {
                    i = level;
                    cost = 8;
                    break;
                }

            case 1:
                if (level <= parameter - 1) return 1;
                else if (level <= parameter) return 2;
                else if (level <= parameter + 1) return 4;
                else if (level <= parameter + 2) return 8;
                else if (level <= parameter + 3) return 12;
                else {
                    i = level;
                    cost = 12;
                    break;
                }

            case 2:
                if (level <= parameter - 2) return 1;
                else if (level <= parameter - 1) return 2;
                else if (level <= parameter) return 4;
                else if (level <= parameter + 1) return 8;
                else if (level <= parameter + 2) return 12;
                else if (level <= parameter + 3) return 16;
                else {
                    i = level;
                    cost = 16;
                    break;
                }
            case 3:
                if (level <= parameter - 3) return 1;
                else if (level <= parameter - 2) return 2;
                else if (level <= parameter - 1) return 4;
                else if (level <= parameter) return 8;
                else if (level <= parameter + 1) return 12;
                else if (level <= parameter + 2) return 16;
                else if (level <= parameter + 3) return 20;
                else {
                    i = level;
                    cost = 20;
                    break;
                }
        }
        while (i > parameter + 3) {
            cost += 4;
            i--;
        }
        return cost;
    }

    public static int spellLevelResult(int complexity) {
        switch (complexity) {
            case 0:
                return character.iq;
            case 1:
                return character.iq - 1;
            case 2:
                return character.iq - 2;
            case 3:
                return character.iq - 3;
        }

        return 0;
    }


    public static int spellCost(Spell spell) {
        if (spell.complexity == 2) {
            if (spell.level <= character.iq - 2) return 1;
            else if (spell.level == character.iq - 1) return 2;
            else if (spell.level == character.iq) return 4;
            else if (spell.level == character.iq + 1) return 8;
            else if (spell.level == character.iq + 2) return 12;
            else if (spell.level == character.iq + 3) return 16;
            else {
                int i = spell.level;
                int cost = 16;
                while (i > character.iq + 3) {
                    cost += 4;
                    i--;
                }
                return cost;
            }
        } else {
            if (spell.level <= character.iq - 3) return 1;
            else if (spell.level == character.iq - 2) return 2;
            else if (spell.level == character.iq - 1) return 4;
            else if (spell.level == character.iq) return 8;
            else if (spell.level == character.iq + 1) return 12;
            else if (spell.level == character.iq + 2) return 16;
            else if (spell.level == character.iq + 3) return 20;
            else {
                int i = spell.level;
                int cost = 20;
                while (i == character.iq + 3) {
                    cost += 4;
                    i--;
                }
                return cost;
            }
        }
    }

    public static String featureFullNameRu(Feature feature) {
        ObservableList<Modifier> modifiers = feature.modifiers(character.id);
        if (modifiers.size() == 0) return feature.name;

        String fullName = feature.name + " (";
        for (Modifier modifier : modifiers) {
            fullName += modifier.name + ", " + modifier.cost + "%, " + Main.locale.getString("level_colon") +
                    modifier.level + "; ";
        }

        fullName = fullName.substring(0, fullName.length() - 2) + ")";
        return fullName;
    }

    private static int costThroughSm(int cost){
        if (character.sm == 0) return cost;
        if ((character.sm > 0 && character.sm < 8) || (character.sm < 0 && character.sm > -8))
            cost = (int) (cost - (cost * (0.1 * character.sm)));
        else if(character.sm < 0 && character.sm <= -8)
            cost = (int) (cost - (cost * -0.8));
        else cost = (int) (cost - (cost * 0.8));
        return cost;
    }
}
