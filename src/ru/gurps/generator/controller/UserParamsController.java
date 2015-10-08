package ru.gurps.generator.controller;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import ru.gurps.generator.controller.helpers.AbstractController;
import ru.gurps.generator.lib.Dmg;
import ru.gurps.generator.lib.UserParams;

public class UserParamsController extends AbstractController {
    public TextField sm;
    public CheckBox noFineManipulators;
    public TextField st;
    public Label stCost;
    public TextField dx;
    public Label dxCost;
    public TextField iq;
    public Label iqCost;
    public TextField ht;
    public Label htCost;
    public TextField hp;
    public Label hpCost;
    public TextField will;
    public Label willCost;
    public TextField per;
    public Label perCost;
    public TextField fp;
    public Label fpCost;
    public TextField bs;
    public Label bsCost;
    public Label bg;
    public TextField move;
    public Label moveCost;
    public Label doge;
    public Label thrust;
    public Label swing;
    public TextField name;
    public TextField player;
    public TextField growth;
    public TextField weight;
    public TextField age;
    public TextField tl;
    public TextField tlCost;

    @FXML
    public void initialize() {
        sm.setText(Integer.toString(character.sm));
        noFineManipulators.setSelected(character.noFineManipulators);

        st.setText(Integer.toString(character.st));
        dx.setText(Integer.toString(character.dx));
        iq.setText(Integer.toString(character.iq));
        ht.setText(Integer.toString(character.ht));

        hp.setText(Integer.toString(character.hp));
        will.setText(Integer.toString(character.will));
        per.setText(Integer.toString(character.per));
        fp.setText(Integer.toString(character.fp));

        bs.setText(Double.toString(character.bs));
        move.setText(Integer.toString(character.move));

        name.setText(character.name);
        player.setText(character.player);
        growth.setText(Integer.toString(character.growth));
        weight.setText(Integer.toString(character.weight));
        age.setText(Integer.toString(character.age));

        tl.setText(Integer.toString(character.tl));
        tlCost.setText(Integer.toString(character.tlCost));

        stCost.setText(Integer.toString(UserParams.stCost()));
        dxCost.setText(Integer.toString(UserParams.dxCost()));
        iqCost.setText(Integer.toString(UserParams.iqCost()));
        htCost.setText(Integer.toString(UserParams.htCost()));

        hpCost.setText(Integer.toString(UserParams.hpCost()));
        willCost.setText(Integer.toString(UserParams.willCost()));
        perCost.setText(Integer.toString(UserParams.perCost()));
        fpCost.setText(Integer.toString(UserParams.fpCost()));

        bsCost.setText(Integer.toString(UserParams.bsCost()));
        moveCost.setText(Integer.toString(UserParams.moveCost()));

        bg.setText(Integer.toString(UserParams.bg()));
        doge.setText(Integer.toString(UserParams.doge()));

        thrust.setText(Dmg.thrust(character.st));
        swing.setText(Dmg.swing(character.st));

        textEvents();
    }

    protected void textEvents() {
        sm.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("")) return;
            if(!newValue.matches("\\d+")) {
                sm.setText(Integer.toString(character.sm));
                return;
            }

            int intValue = Integer.parseInt(newValue);
            if(character.sm == intValue) return;

            character.sm = intValue;
            int oldStCost = Integer.parseInt(stCost.getText());
            stCost.setText(Integer.toString(UserParams.stCost()));
            currentPoints(stCost, oldStCost);

            int oldHpCost = Integer.parseInt(hpCost.getText());
            hpCost.setText(Integer.toString(UserParams.hpCost()));
            currentPoints(hpCost, oldHpCost);
            character.save();
        });

        noFineManipulators.selectedProperty().addListener((observable, oldValue, newValue) -> {
            character.noFineManipulators = newValue;
            int oldStCost = Integer.parseInt(stCost.getText());
            stCost.setText(Integer.toString(UserParams.stCost()));
            currentPoints(stCost, oldStCost);

            int oldDxCost = Integer.parseInt(dxCost.getText());
            dxCost.setText(Integer.toString(UserParams.dxCost()));
            currentPoints(dxCost, oldDxCost);
            character.save();
        });

        st.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("")) return;
            if(!newValue.matches("\\d+")) {
                st.setText(Integer.toString(character.st));
                return;
            }

            int intValue = Integer.parseInt(newValue);
            if(character.st == intValue) return;

            character.st = intValue;
            if(intValue > character.hp) {
                character.hp = intValue;
                hp.setText(newValue);
            }

            int oldStCost = Integer.parseInt(stCost.getText());
            stCost.setText(Integer.toString(UserParams.stCost()));
            currentPoints(stCost, oldStCost);

            int oldHpCost = Integer.parseInt(hpCost.getText());
            hpCost.setText(Integer.toString(UserParams.hpCost()));
            currentPoints(hpCost, oldHpCost);

            bg.setText(Integer.toString(UserParams.bg()));
            thrust.setText(Dmg.thrust(character.st));
            swing.setText(Dmg.swing(character.st));

            character.save();
        });

        dx.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("")) return;
            if(!newValue.matches("\\d+")) {
                dx.setText(Integer.toString(character.dx));
                return;
            }

            int intValue = Integer.parseInt(newValue);
            if(character.dx == intValue) return;
            character.dx = intValue;

            int oldDxCost = Integer.parseInt(dxCost.getText());
            dxCost.setText(Integer.toString(UserParams.dxCost()));
            currentPoints(dxCost, oldDxCost);

            int oldBsCost = Integer.parseInt(bsCost.getText());
            bsCost.setText(Integer.toString(UserParams.bsCost()));
            currentPoints(bsCost, oldBsCost);

            int oldMoveCost = Integer.parseInt(moveCost.getText());
            moveCost.setText(Integer.toString(UserParams.moveCost()));
            currentPoints(moveCost, oldMoveCost);

            doge.setText(Integer.toString(UserParams.doge()));
            character.save();
        });

        iq.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("")) return;
            if(!newValue.matches("\\d+")) {
                iq.setText(Integer.toString(character.iq));
                return;
            }

            int intValue = Integer.parseInt(newValue);
            if(character.iq == intValue) return;
            character.iq = intValue;

            int oldIqCost = Integer.parseInt(iqCost.getText());
            iqCost.setText(Integer.toString(UserParams.iqCost()));
            currentPoints(iqCost, oldIqCost);

            if(intValue > character.will) {
                character.will = intValue;
                will.setText(Integer.toString(character.will));
            }

            if(intValue > character.per) {
                character.per = intValue;
                per.setText(Integer.toString(character.per));
            }

            int oldWillCost = Integer.parseInt(willCost.getText());
            willCost.setText(Integer.toString(UserParams.willCost()));
            currentPoints(willCost, oldWillCost);

            int oldPerCost = Integer.parseInt(perCost.getText());
            perCost.setText(Integer.toString(UserParams.perCost()));
            currentPoints(perCost, oldPerCost);
            character.save();
        });

        ht.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("")) return;
            if(!newValue.matches("\\d+")) {
                ht.setText(Integer.toString(character.ht));
                return;
            }

            int intValue = Integer.parseInt(newValue);
            if(character.ht == intValue) return;
            character.ht = intValue;

            int oldHtCost = Integer.parseInt(htCost.getText());
            htCost.setText(Integer.toString(UserParams.htCost()));
            currentPoints(htCost, oldHtCost);

            if(intValue > character.fp) {
                character.fp = intValue;
                fp.setText(Integer.toString(character.fp));
            }

            int oldFpCost = Integer.parseInt(fpCost.getText());
            fpCost.setText(Integer.toString(UserParams.fpCost()));
            currentPoints(fpCost, oldFpCost);

            int oldBsCost = Integer.parseInt(bsCost.getText());
            bsCost.setText(Integer.toString(UserParams.bsCost()));
            currentPoints(bsCost, oldBsCost);

            int oldMoveCost = Integer.parseInt(moveCost.getText());
            moveCost.setText(Integer.toString(UserParams.moveCost()));
            currentPoints(moveCost, oldMoveCost);
        });

        hp.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("")) return;
            if(!newValue.matches("\\d+")) {
                hp.setText(Integer.toString(character.hp));
                return;
            }

            int intValue = Integer.parseInt(newValue);
            if(character.hp == intValue) return;
            character.hp = intValue;

            int oldHpCost = Integer.parseInt(hpCost.getText());
            hpCost.setText(Integer.toString(UserParams.hpCost()));
            currentPoints(hpCost, oldHpCost);
            character.save();
        });

        will.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("")) return;
            if(!newValue.matches("\\d+")) {
                will.setText(Integer.toString(character.will));
                return;
            }

            int intValue = Integer.parseInt(newValue);
            if(character.will == intValue) return;
            character.will = intValue;

            int oldWillCost = Integer.parseInt(willCost.getText());
            willCost.setText(Integer.toString(UserParams.willCost()));
            currentPoints(willCost, oldWillCost);
            character.save();
        });

        per.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("")) return;
            if(!newValue.matches("\\d+")) {
                per.setText(Integer.toString(character.per));
                return;
            }

            int intValue = Integer.parseInt(newValue);
            if(character.per == intValue) return;
            character.per = intValue;

            int oldPerCost = Integer.parseInt(perCost.getText());
            perCost.setText(Integer.toString(UserParams.perCost()));
            currentPoints(perCost, oldPerCost);
            character.save();
        });

        fp.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("")) return;
            if(!newValue.matches("\\d+")) {
                fp.setText(Integer.toString(character.fp));
                return;
            }

            int intValue = Integer.parseInt(newValue);
            if(character.fp == intValue) return;
            character.fp = intValue;

            int oldFpCost = Integer.parseInt(fpCost.getText());
            fpCost.setText(Integer.toString(UserParams.fpCost()));
            currentPoints(fpCost, oldFpCost);
            character.save();
        });

        bs.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("")) return;
            if(newValue.matches(".\\d+") && !newValue.matches("\\d+.\\d+") || newValue.matches("\\d+.") && !newValue.matches("\\d+.\\d+")) return;
            if(!newValue.matches("\\d+.\\d+|\\d+")) {
                bs.setText(Double.toString(character.bs));
                return;
            }

            double dNewValue = Double.parseDouble(newValue);
            if(character.bs == dNewValue) return;

            character.bs = dNewValue;
            int intNewValue = (int) dNewValue;
            if(intNewValue > character.move) {
                character.move = intNewValue;
                move.setText(Integer.toString(character.move));
            }

            int oldBsCost = Integer.parseInt(bsCost.getText());
            bsCost.setText(Integer.toString(UserParams.bsCost()));
            currentPoints(bsCost, oldBsCost);

            int oldMoveCost = Integer.parseInt(moveCost.getText());
            moveCost.setText(Integer.toString(UserParams.moveCost()));
            currentPoints(moveCost, oldMoveCost);

            doge.setText(Integer.toString(UserParams.doge()));
            character.save();
        });

        move.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("")) return;
            if(!newValue.matches("\\d+")) {
                move.setText(Integer.toString(character.move));
                return;
            }

            int intNewValue = Integer.parseInt(newValue);
            if(character.move == intNewValue) return;

            character.move = intNewValue;
            int oldMoveCost = Integer.parseInt(moveCost.getText());
            moveCost.setText(Integer.toString(UserParams.moveCost()));
            currentPoints(moveCost, oldMoveCost);
            character.save();
        });

        name.textProperty().addListener((observable, oldValue, newValue) -> {
            if(character.name.equals(newValue)) return;
            character.update_single("name", newValue);
        });

        player.textProperty().addListener((observable, oldValue, newValue) -> {
            if(character.player != null && character.player.equals(newValue)) return;
            character.update_single("player", newValue);
        });

        tl.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("")) return;
            if(!newValue.matches("\\d+")) {
                tl.setText(Integer.toString(character.tl));
                return;
            }

            int intNewValue = Integer.parseInt(newValue);
            if(character.tl == intNewValue) return;
            character.update_single("tl", intNewValue);
        });

        tlCost.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("") || newValue.equals("-")) return;
            try {
                int intNewValue = Integer.parseInt(newValue);
                if(character.tlCost == intNewValue) return;
                setCurrentPoints(Integer.parseInt(character.currentPoints) + intNewValue - character.tlCost);
                character.tlCost = intNewValue;
                character.save();
            } catch(NumberFormatException e){
                tlCost.setText(Integer.toString(character.tlCost));
            }
        });

        growth.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("")) return;
            if(!newValue.matches("\\d+")) {
                growth.setText(Integer.toString(character.growth));
                return;
            }

            character.update_single("growth", Integer.parseInt(newValue));
        });

        weight.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("")) return;
            if(!newValue.matches("\\d+")) {
                weight.setText(Integer.toString(character.weight));
                return;
            }

            character.update_single("weight", Integer.parseInt(newValue));
        });

        age.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("")) return;
            if(!newValue.matches("\\d+")) {
                age.setText(Integer.toString(character.age));
                return;
            }

            character.update_single("age", Integer.parseInt(newValue));
        });
    }

    private void currentPoints(Label cost, int oldStCost) {
        setCurrentPoints(Integer.parseInt(character.currentPoints) + Integer.parseInt(cost.getText()) - oldStCost);
    }
}
