package ru.gurps.generator.controller;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
        sm.setText(Integer.toString(user.sm));
        noFineManipulators.setSelected(user.noFineManipulators);

        st.setText(Integer.toString(user.st));
        dx.setText(Integer.toString(user.dx));
        iq.setText(Integer.toString(user.iq));
        ht.setText(Integer.toString(user.ht));

        hp.setText(Integer.toString(user.hp));
        will.setText(Integer.toString(user.will));
        per.setText(Integer.toString(user.per));
        fp.setText(Integer.toString(user.fp));

        bs.setText(Double.toString(user.bs));
        move.setText(Integer.toString(user.move));

        name.setText(user.name);
        player.setText(user.player);
        growth.setText(Integer.toString(user.growth));
        weight.setText(Integer.toString(user.weight));
        age.setText(Integer.toString(user.age));

        tl.setText(Integer.toString(user.tl));
        tlCost.setText(Integer.toString(user.tlCost));

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

        thrust.setText(Dmg.thrust(user.st));
        swing.setText(Dmg.swing(user.st));

        textEvents();
    }

    protected void textEvents() {
        sm.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("")) return;
            if(!newValue.matches("\\d+")) {
                sm.setText(Integer.toString(user.sm));
                return;
            }

            int intValue = Integer.parseInt(newValue);
            if(user.sm == intValue) return;

            user.sm = intValue;
            int oldStCost = Integer.parseInt(stCost.getText());
            stCost.setText(Integer.toString(UserParams.stCost()));
            currentPoints(stCost, oldStCost);

            int oldHpCost = Integer.parseInt(hpCost.getText());
            hpCost.setText(Integer.toString(UserParams.hpCost()));
            currentPoints(hpCost, oldHpCost);
            user.save();
        });

        noFineManipulators.selectedProperty().addListener((observable, oldValue, newValue) -> {
            user.noFineManipulators = newValue;
            int oldStCost = Integer.parseInt(stCost.getText());
            stCost.setText(Integer.toString(UserParams.stCost()));
            currentPoints(stCost, oldStCost);

            int oldDxCost = Integer.parseInt(dxCost.getText());
            dxCost.setText(Integer.toString(UserParams.dxCost()));
            currentPoints(dxCost, oldDxCost);
            user.save();
        });

        st.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("")) return;
            if(!newValue.matches("\\d+")) {
                st.setText(Integer.toString(user.st));
                return;
            }

            int intValue = Integer.parseInt(newValue);
            if(user.st == intValue) return;

            user.st = intValue;
            if(intValue > user.hp) {
                user.hp = intValue;
                hp.setText(newValue);
            }

            int oldStCost = Integer.parseInt(stCost.getText());
            stCost.setText(Integer.toString(UserParams.stCost()));
            currentPoints(stCost, oldStCost);

            int oldHpCost = Integer.parseInt(hpCost.getText());
            hpCost.setText(Integer.toString(UserParams.hpCost()));
            currentPoints(hpCost, oldHpCost);

            bg.setText(Integer.toString(UserParams.bg()));
            thrust.setText(Dmg.thrust(user.st));
            swing.setText(Dmg.swing(user.st));

            user.save();
        });

        dx.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("")) return;
            if(!newValue.matches("\\d+")) {
                dx.setText(Integer.toString(user.dx));
                return;
            }

            int intValue = Integer.parseInt(newValue);
            if(user.dx == intValue) return;
            user.dx = intValue;

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
            user.save();
        });

        iq.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("")) return;
            if(!newValue.matches("\\d+")) {
                iq.setText(Integer.toString(user.iq));
                return;
            }

            int intValue = Integer.parseInt(newValue);
            if(user.iq == intValue) return;
            user.iq = intValue;

            int oldIqCost = Integer.parseInt(iqCost.getText());
            iqCost.setText(Integer.toString(UserParams.iqCost()));
            currentPoints(iqCost, oldIqCost);

            if(intValue > user.will) {
                user.will = intValue;
                will.setText(Integer.toString(user.will));
            }

            if(intValue > user.per) {
                user.per = intValue;
                per.setText(Integer.toString(user.per));
            }

            int oldWillCost = Integer.parseInt(willCost.getText());
            willCost.setText(Integer.toString(UserParams.willCost()));
            currentPoints(willCost, oldWillCost);

            int oldPerCost = Integer.parseInt(perCost.getText());
            perCost.setText(Integer.toString(UserParams.perCost()));
            currentPoints(perCost, oldPerCost);
            user.save();
        });

        ht.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("")) return;
            if(!newValue.matches("\\d+")) {
                ht.setText(Integer.toString(user.ht));
                return;
            }

            int intValue = Integer.parseInt(newValue);
            if(user.ht == intValue) return;
            user.ht = intValue;

            int oldHtCost = Integer.parseInt(htCost.getText());
            htCost.setText(Integer.toString(UserParams.htCost()));
            currentPoints(htCost, oldHtCost);

            if(intValue > user.fp) {
                user.fp = intValue;
                fp.setText(Integer.toString(user.fp));
            }

            int oldFpCost = Integer.parseInt(fpCost.getText());
            fpCost.setText(Integer.toString(UserParams.fpCost()));
            currentPoints(htCost, oldFpCost);

            int oldBsCost = Integer.parseInt(bsCost.getText());
            bsCost.setText(Integer.toString(UserParams.bsCost()));
            currentPoints(bsCost, oldBsCost);
            user.save();
        });

        hp.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("")) return;
            if(!newValue.matches("\\d+")) {
                hp.setText(Integer.toString(user.hp));
                return;
            }

            int intValue = Integer.parseInt(newValue);
            if(user.hp == intValue) return;
            user.hp = intValue;

            int oldHpCost = Integer.parseInt(hpCost.getText());
            hpCost.setText(Integer.toString(UserParams.hpCost()));
            currentPoints(hpCost, oldHpCost);
            user.save();
        });

        will.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("")) return;
            if(!newValue.matches("\\d+")) {
                will.setText(Integer.toString(user.will));
                return;
            }

            int intValue = Integer.parseInt(newValue);
            if(user.will == intValue) return;
            user.will = intValue;

            int oldWillCost = Integer.parseInt(willCost.getText());
            willCost.setText(Integer.toString(UserParams.willCost()));
            currentPoints(willCost, oldWillCost);
            user.save();
        });

        per.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("")) return;
            if(!newValue.matches("\\d+")) {
                per.setText(Integer.toString(user.per));
                return;
            }

            int intValue = Integer.parseInt(newValue);
            if(user.per == intValue) return;
            user.per = intValue;

            int oldPerCost = Integer.parseInt(perCost.getText());
            perCost.setText(Integer.toString(UserParams.perCost()));
            currentPoints(perCost, oldPerCost);
            user.save();
        });

        fp.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("")) return;
            if(!newValue.matches("\\d+")) {
                fp.setText(Integer.toString(user.fp));
                return;
            }

            int intValue = Integer.parseInt(newValue);
            if(user.fp == intValue) return;
            user.per = intValue;

            int oldFpCost = Integer.parseInt(fpCost.getText());
            fpCost.setText(Integer.toString(UserParams.fpCost()));
            currentPoints(htCost, oldFpCost);
            user.save();
        });

        bs.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("")) return;
            if(newValue.matches(".\\d+") && !newValue.matches("\\d+.\\d+") || newValue.matches("\\d+.") && !newValue.matches("\\d+.\\d+")) return;
            if(!newValue.matches("\\d+.\\d+|\\d+")) {
                bs.setText(Double.toString(user.bs));
                return;
            }

            double dNewValue = Double.parseDouble(newValue);
            if(user.bs == dNewValue) return;

            user.bs = dNewValue;
            int intNewValue = (int) dNewValue;
            if(intNewValue > user.move) {
                user.move = intNewValue;
                move.setText(Integer.toString(user.move));
            }

            int oldBsCost = Integer.parseInt(bsCost.getText());
            bsCost.setText(Integer.toString(UserParams.bsCost()));
            currentPoints(bsCost, oldBsCost);

            int oldMoveCost = Integer.parseInt(moveCost.getText());
            moveCost.setText(Integer.toString(UserParams.moveCost()));
            currentPoints(moveCost, oldMoveCost);

            doge.setText(Integer.toString(UserParams.doge()));
            user.save();
        });

        move.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("")) return;
            if(!newValue.matches("\\d+")) {
                move.setText(Integer.toString(user.move));
                return;
            }

            int intNewValue = Integer.parseInt(newValue);
            if(user.move == intNewValue) return;

            user.move = intNewValue;
            int oldMoveCost = Integer.parseInt(moveCost.getText());
            moveCost.setText(Integer.toString(UserParams.moveCost()));
            currentPoints(moveCost, oldMoveCost);
            user.save();
        });

        name.textProperty().addListener((observable, oldValue, newValue) -> {
            if(user.name.equals(newValue)) return;
            user.update_single("name", newValue);
        });

        player.textProperty().addListener((observable, oldValue, newValue) -> {
            if(user.player.equals(newValue)) return;
            user.update_single("player", newValue);
        });

        tl.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("")) return;
            if(!newValue.matches("\\d+")) {
                tl.setText(Integer.toString(user.tl));
                return;
            }

            int intNewValue = Integer.parseInt(newValue);
            if(user.tl == intNewValue) return;
            user.update_single("tl", intNewValue);
        });

        tlCost.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("")) return;
            if(newValue.equals("-")) return;
            try {
                int intNewValue = Integer.parseInt(newValue);
                if(user.tlCost == intNewValue) return;
                setCurrentPoints(Integer.parseInt(user.currentPoints) + intNewValue - user.tlCost);
                user.tlCost = intNewValue;
                user.save();
            } catch(NumberFormatException e){
                tlCost.setText(Integer.toString(user.tlCost));
            }
        });
    }

    private void currentPoints(Label cost, int oldStCost) {
        setCurrentPoints(Integer.parseInt(user.currentPoints) + Integer.parseInt(cost.getText()) - oldStCost);
    }
}
