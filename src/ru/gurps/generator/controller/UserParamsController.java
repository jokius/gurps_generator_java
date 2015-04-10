package ru.gurps.generator.controller;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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

    private UserParams userParams;

    @FXML
    public void initialize() {
        userParams = new UserParams(stCost, dxCost, iqCost, htCost, hpCost, willCost, perCost, fpCost, bsCost, moveCost,
                bg, doge, thrust, swing);

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

        userParams.setSt();
        userParams.setDx();
        userParams.setIq();
        userParams.setHt();

        userParams.setHp();
        userParams.setWill();
        userParams.setPer();
        userParams.setFp();

        userParams.setBs();
        userParams.setMove();

        userParams.setBg();
        userParams.setDoge();
        userParams.setDmg();

        textEvents();
    }

    protected void textEvents() {
        sm.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("")) return;
            if("\\D".matches(newValue)) {
                sm.setText(Integer.toString(user.sm));
                return;
            }

            int intValue = Integer.parseInt(newValue);
            if(user.sm == intValue) return;

            user.sm = intValue;
            int oldStCost = Integer.parseInt(stCost.getText());
            userParams.setSt();
            currentPoints(stCost, oldStCost);

            int oldHpCost = Integer.parseInt(hpCost.getText());
            userParams.setHp();
            currentPoints(hpCost, oldHpCost);
            user.save();
        });

        noFineManipulators.selectedProperty().addListener((observable, oldValue, newValue) -> {
            user.noFineManipulators = newValue;
            int oldStCost = Integer.parseInt(stCost.getText());
            userParams.setSt();
            currentPoints(stCost, oldStCost);

            int oldDxCost = Integer.parseInt(dxCost.getText());
            userParams.setDx();
            currentPoints(dxCost, oldDxCost);
            user.save();
        });

        st.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("")) return;
            if(newValue.matches("\\D")) {
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
            userParams.setSt();
            currentPoints(stCost, oldStCost);

            int oldHpCost = Integer.parseInt(hpCost.getText());
            userParams.setHp();
            currentPoints(hpCost, oldHpCost);

            userParams.setBg();
            userParams.setDmg();

            user.save();
        });

        dx.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("")) return;
            if("\\D".matches(newValue)) {
                dx.setText(Integer.toString(user.dx));
                return;
            }

            int intValue = Integer.parseInt(newValue);
            if(user.dx == intValue) return;
            user.dx = intValue;

            int oldDxCost = Integer.parseInt(dxCost.getText());
            userParams.setDx();
            currentPoints(dxCost, oldDxCost);

            int oldBsCost = Integer.parseInt(bsCost.getText());
            userParams.setBs();
            currentPoints(bsCost, oldBsCost);

            int oldMoveCost = Integer.parseInt(moveCost.getText());
            userParams.setMove();
            currentPoints(moveCost, oldMoveCost);

            userParams.setDoge();
            user.save();
        });

        iq.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("")) return;
            if(newValue.matches("\\D")) {
                iq.setText(Integer.toString(user.iq));
                return;
            }

            int intValue = Integer.parseInt(newValue);
            if(user.iq == intValue) return;
            user.iq = intValue;

            int oldIqCost = Integer.parseInt(iqCost.getText());
            userParams.setIq();
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
            userParams.setWill();
            currentPoints(willCost, oldWillCost);

            int oldPerCost = Integer.parseInt(perCost.getText());
            userParams.setPer();
            currentPoints(perCost, oldPerCost);
            user.save();
        });

        ht.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("")) return;
            if("\\D".matches(newValue)) {
                ht.setText(Integer.toString(user.ht));
                return;
            }

            int intValue = Integer.parseInt(newValue);
            if(user.ht == intValue) return;
            user.ht = intValue;

            int oldHtCost = Integer.parseInt(htCost.getText());
            userParams.setHt();
            currentPoints(htCost, oldHtCost);

            if(intValue > user.fp) {
                user.fp = intValue;
                fp.setText(Integer.toString(user.fp));
            }

            int oldFpCost = Integer.parseInt(fpCost.getText());
            userParams.setFp();
            currentPoints(htCost, oldFpCost);

            int oldBsCost = Integer.parseInt(bsCost.getText());
            userParams.setBs();
            currentPoints(bsCost, oldBsCost);
            user.save();
        });

        hp.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("")) return;
            if("\\D".matches(newValue)) {
                hp.setText(Integer.toString(user.hp));
                return;
            }

            int intValue = Integer.parseInt(newValue);
            if(user.hp == intValue) return;
            user.hp = intValue;

            int oldHpCost = Integer.parseInt(hpCost.getText());
            userParams.setHp();
            currentPoints(hpCost, oldHpCost);
            user.save();
        });

        will.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("")) return;
            if("\\D".matches(newValue)) {
                will.setText(Integer.toString(user.will));
                return;
            }

            int intValue = Integer.parseInt(newValue);
            if(user.will == intValue) return;
            user.will = intValue;

            int oldWillCost = Integer.parseInt(willCost.getText());
            userParams.setWill();
            currentPoints(willCost, oldWillCost);
            user.save();
        });

        per.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("")) return;
            if("\\D".matches(newValue)) {
                per.setText(Integer.toString(user.per));
                return;
            }

            int intValue = Integer.parseInt(newValue);
            if(user.per == intValue) return;
            user.per = intValue;

            int oldPerCost = Integer.parseInt(perCost.getText());
            userParams.setPer();
            currentPoints(perCost, oldPerCost);
            user.save();
        });

        fp.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("")) return;
            if("\\D".matches(newValue)) {
                fp.setText(Integer.toString(user.fp));
                return;
            }

            int intValue = Integer.parseInt(newValue);
            if(user.fp == intValue) return;
            user.per = intValue;

            int oldFpCost = Integer.parseInt(fpCost.getText());
            userParams.setFp();
            currentPoints(htCost, oldFpCost);
            user.save();
        });

        bs.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("")) return;
            if(newValue.matches("\\D[^.]")) {
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
            userParams.setBs();
            currentPoints(bsCost, oldBsCost);

            int oldMoveCost = Integer.parseInt(moveCost.getText());
            userParams.setMove();
            currentPoints(moveCost, oldMoveCost);

            userParams.setDoge();
            user.save();
        });

        move.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("")) return;
            if("\\D".matches(newValue)) {
                move.setText(Integer.toString(user.move));
                return;
            }

            int intNewValue = Integer.parseInt(newValue);
            if(user.move == intNewValue) return;

            user.move = intNewValue;
            int oldMoveCost = Integer.parseInt(moveCost.getText());
            userParams.setMove();
            currentPoints(moveCost, oldMoveCost);
            user.save();
        });
    }

    private void currentPoints(Label cost, int oldStCost) {
        user.currentPoints = Integer.toString(Integer.parseInt(user.currentPoints) + Integer.parseInt(cost.getText()) - oldStCost);
        globalCost.setText(user.currentPoints);
    }
}
