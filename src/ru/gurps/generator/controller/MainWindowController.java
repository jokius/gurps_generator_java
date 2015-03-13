package ru.gurps.generator.controller;

import javafx.fxml.FXML;

public class MainWindowController extends AbstractController {
    @FXML
    public void initialize() {
        textEvents();
        cellEvents();
        maxPoints.setText(user.maxPoints);
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

        if(user.currentPoints.equals("0")) {
            setSt();
            setDx();
            setIq();
            setHt();

            setHp();
            setWill();
            setPer();
            setFp();

            setBs();
            setMove();
        }

        setBg();
        setDoge();
        setDmg();
        
        currentPoints.setText(user.currentPoints);
    }
}
