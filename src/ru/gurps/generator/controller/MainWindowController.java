package ru.gurps.generator.controller;

import javafx.fxml.FXML;
import ru.gurps.generator.lib.TabConfigure;
import ru.gurps.generator.lib.UserParams;

public class MainWindowController extends AbstractController {
    @FXML
    public void initialize() {
        globalCost = currentPoints;

        for(int i = 1; 5 >= i; i++){
            advantagesNumbers.add(i);
            disadvantagesNumbers.add(i);
        }

        new TabConfigure(viewMenu, mainTabPanel, paramsTab, advantagesTab, disadvantagesTab, modesTab, skillsTab,
                techniquesTab, spellTab, languagesTab, culturasTab, equipmentTab);

        userParams = new UserParams(stCost, dxCost, iqCost, htCost, hpCost, willCost, perCost, fpCost, bsCost, moveCost,
                bg, doge, thrust, swing);

        textEvents();
        cellEvents();
        buttonEvents();
        SearchEvents();
        checkBoxEvents();
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
        
        currentPoints.setText(user.currentPoints);
    }
}
