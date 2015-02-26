package ru.gurps.generator.controller;

import javafx.fxml.FXML;

public class MainWindowController extends AbstractController {
    @FXML
    public void initialize() {
        textEvents();
        cellEvents();
        maxPoints.setText(user.getMaxPoints());
        sm.setText(Integer.toString(user.getSm()));
        noFineManipulators.setSelected(user.getNoFineManipulators());

        st.setText(Integer.toString(user.getSt()));
        dx.setText(Integer.toString(user.getDx()));
        iq.setText(Integer.toString(user.getIq()));
        ht.setText(Integer.toString(user.getHt()));

        hp.setText(Integer.toString(user.getHp()));
        will.setText(Integer.toString(user.getWill()));
        per.setText(Integer.toString(user.getPer()));
        fp.setText(Integer.toString(user.getFp()));
        
        bs.setText(Double.toString(user.getBs()));
        move.setText(Integer.toString(user.getMove()));

        if(user.getCurrentPoints().equals("0")) {
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
        
        currentPoints.setText(user.getCurrentPoints());
    }
}
