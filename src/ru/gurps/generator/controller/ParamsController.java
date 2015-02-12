package ru.gurps.generator.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import ru.gurps.generator.config.Db;
import ru.gurps.generator.lib.Dmg;
import java.util.HashMap;
import java.util.prefs.Preferences;

public class ParamsController extends MainController {
//    @FXML
//    private TextField sm;
//
//    @FXML
//    private CheckBox noFineManipulators;
//
//    @FXML
//    private TextField st;
//
//    @FXML
//    private Label stCost;
//
//    @FXML
//    private TextField dx;
//
//    @FXML
//    private Label dxCost;
//
//    @FXML
//    private TextField iq;
//
//    @FXML
//    private Label iqCost;
//
//    @FXML
//    private TextField ht;
//
//    @FXML
//    private Label htCost;
//
//    @FXML
//    private TextField hp;
//
//    @FXML
//    private Label hpCost;
//
//    @FXML
//    private TextField will;
//
//    @FXML
//    private Label willCost;
//
//    @FXML
//    private TextField per;
//
//    @FXML
//    private Label perCost;
//
//    @FXML
//    private TextField fp;
//
//    @FXML
//    private Label fpCost;
//
//    @FXML
//    private TextField bs;
//
//    @FXML
//    private Label bsCost;
//
//    @FXML
//    private Label bg;
//
//    @FXML
//    private TextField move;
//
//    @FXML
//    private Label moveCost;
//
//    @FXML
//    private Label doge;
//
//    @FXML
//    private Label thrust;
//
//    @FXML
//    private Label swing;
//
//    @FXML
//    private TextField maxPoints;
//
//    @FXML
//    public Label currentPoints;

    public String points;

    private HashMap<String, String> params = new HashMap<String, String>();

    public void setPoints(String points) {
        this.points = points;
        currentPoints.setText(points);
    }

    @FXML
    private void initialize() {
        setParams();


        sm.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.equals("")){
                    return;
                }
                
                if(newValue.matches("\\D")){
                    sm.setText(Integer.toString(user.getSm()));
                    return;
                }

                int intValue = Integer.parseInt(newValue);

                if(user.getSm() == intValue){
                    return;
                }
                user.setSm(intValue);
                setSt();
                setHp();

                params.clear();
                params.put("sm", Integer.toString(user.getSm()));
                params.put("current_points", user.getCurrentPoints());
                Db.update("users", user.getId(), params);
            }
        });

        noFineManipulators.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                user.setNoFineManipulators(newValue);
                setSt();
                setDx();

                params.clear();
                params.put("no_fine_manipulators", Boolean.toString(user.getNoFineManipulators()));
                params.put("current_points", user.getCurrentPoints());
                Db.update("users", user.getId(), params);
            }
        });

        st.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.equals("")){
                    return;
                }
                
                if(newValue.matches("\\D")){
                    st.setText(Integer.toString(user.getSt()));
                    return;
                }

                int intValue = Integer.parseInt(newValue);

                if(user.getSt() == intValue){
                    return;
                }

                user.setSt(intValue);
                if(intValue > user.getHp()){
                    user.setHp(intValue);
                    hp.setText(newValue);
                }

                setSt();
                setHp();
                setBg();
                setDmg();

                params.clear();

                if(intValue > user.getHt()){
                    user.setHp(intValue);
                    params.put("hp", Integer.toString(user.getHp()));
                    hp.setText(Integer.toString(user.getHp()));
                }
                params.put("st", Integer.toString(user.getSt()));
                params.put("current_points", user.getCurrentPoints());
                Db.update("users", user.getId(), params);
            }
        });

        dx.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.equals("")){
                    return;
                }
                
                if(newValue.matches("\\D")){
                    dx.setText(Integer.toString(user.getDx()));
                    return;
                }

                int intValue = Integer.parseInt(newValue);

                if(user.getDx() == intValue){
                    return;
                }
                user.setDx(intValue);
                setDx();
                setBs();
                setMove();
                setDoge();
                
                params.clear();
                params.put("dx", Integer.toString(user.getDx()));
                params.put("current_points", user.getCurrentPoints());
                Db.update("users", user.getId(), params);
            }
        });

        iq.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.equals("")){
                    return;
                }
                
                if(newValue.matches("\\D")){
                    iq.setText(Integer.toString(user.getIq()));
                    return;
                }

                int intValue = Integer.parseInt(newValue);

                if(user.getIq() == intValue){
                    return;
                }
                user.setIq(intValue);
                setIq();

                params.clear();

                if(intValue > user.getWill()){
                    user.setWill(intValue);
                    params.put("will", Integer.toString(user.getWill()));
                    will.setText(Integer.toString(user.getWill()));
                }
                if(intValue > user.getPer()){
                    user.setPer(intValue);
                    params.put("per", Integer.toString(user.getPer()));
                    per.setText(Integer.toString(user.getPer()));
                }

                setWill();
                setPer();
                params.put("iq", Integer.toString(user.getIq()));
                params.put("current_points", user.getCurrentPoints());
                Db.update("users", user.getId(), params);
            }
        });

        ht.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.equals("")){
                    return;
                }
                
                if(newValue.matches("\\D")){
                    ht.setText(Integer.toString(user.getHt()));
                    return;
                }

                int intValue = Integer.parseInt(newValue);

                if(user.getHt() == intValue){
                    return;
                }
                user.setHt(intValue);
                setHt();

                params.clear();
                
                if(intValue > user.getFp()){
                    user.setFp(intValue);
                    params.put("fp", Integer.toString(user.getFp()));
                    fp.setText(Integer.toString(user.getFp()));
                }
                
                setFp();
                setBs();
                params.put("ht", Integer.toString(user.getHt()));
                params.put("current_points", user.getCurrentPoints());
                Db.update("users", user.getId(), params);
            }
        });

        hp.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.equals("")){
                    return;
                }
                
                if(newValue.matches("\\D")){
                    hp.setText(Integer.toString(user.getHp()));
                    return;
                }

                int intValue = Integer.parseInt(newValue);

                if(user.getHp() == intValue){
                    return;
                }
                user.setHp(intValue);
                setHp();

                params.clear();
                params.put("hp", Integer.toString(user.getHp()));
                params.put("current_points", user.getCurrentPoints());
                Db.update("users", user.getId(), params);
            }
        });

        will.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.equals("")){
                    return;
                }
                
                if(newValue.matches("\\D")){
                    will.setText(Integer.toString(user.getWill()));
                    return;
                }

                int intValue = Integer.parseInt(newValue);

                if(user.getWill() == intValue){
                    return;
                }
                user.setWill(intValue);
                setWill();

                params.clear();
                params.put("will", Integer.toString(user.getWill()));
                params.put("current_points", user.getCurrentPoints());
                Db.update("users", user.getId(), params);
            }
        });

        per.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.equals("")){
                    return;
                }
                
                if(newValue.matches("\\D")){
                    per.setText(Integer.toString(user.getPer()));
                    return;
                }

                int intValue = Integer.parseInt(newValue);

                if(user.getPer() == intValue){
                    return;
                }
                user.setPer(intValue);
                setPer();

                params.clear();
                params.put("per", Integer.toString(user.getPer()));
                params.put("current_points", user.getCurrentPoints());
                Db.update("users", user.getId(), params);
            }
        });

        fp.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.equals("")){
                    return;
                }
                
                if(newValue.matches("\\D")){
                    fp.setText(Integer.toString(user.getFp()));
                    return;
                }

                int intValue = Integer.parseInt(newValue);

                if(user.getFp() == intValue){
                    return;
                }
                user.setFp(intValue);
                setFp();

                params.clear();
                params.put("fp", Integer.toString(user.getFp()));
                params.put("current_points", user.getCurrentPoints());
                Db.update("users", user.getId(), params);
            }
        });

        bs.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.equals("")){
                    return;
                }
                
                if(newValue.matches("\\D[^.]")){
                    bs.setText(Double.toString(user.getBs()));
                    return;
                }
                
                double dNewValue = Double.parseDouble(newValue);

                if(user.getBs() == dNewValue){
                    return;
                }

                user.setBs(dNewValue);
                bs.setText(Double.toString(user.getBs()));
                setBs();
                params.clear();
                
                int intNewValue = (int) dNewValue;
                
                if(intNewValue > user.getMove()){
                    user.setMove(intNewValue);
                    params.put("move", Integer.toString(user.getMove()));
                    move.setText(Integer.toString(user.getMove()));
                }
                
                setMove();
                setDoge();
                
                params.put("bs", Double.toString(user.getBs()));
                params.put("current_points", user.getCurrentPoints());
                Db.update("users", user.getId(), params);
            }
        });

        move.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.equals("")){
                    return;
                }
                
                if(newValue.matches("\\D")){
                    move.setText(Integer.toString(user.getMove()));
                    return;
                }

                int intNewValue = Integer.parseInt(newValue);
                
                if(user.getMove() == intNewValue){
                    return;
                }
                
                user.setMove(intNewValue);
                setMove();

                params.clear();
                params.put("move", Integer.toString(user.getMove()));
                params.put("current_points", user.getCurrentPoints());
                Db.update("users", user.getId(), params);
            }
        });

        maxPoints.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.matches("\\D") || newValue.equals("")){
                    maxPoints.setText(oldValue);
                    return;
                }

                if(user.getMaxPoints().equals(newValue)){
                    return;
                }
                user.setMaxPoints(newValue);
                maxPoints.setText(newValue);

                params.clear();
                params.put("max_points", user.getMaxPoints());
                Db.update("users", user.getId(), params);
            }
        });
    }
    
    
    private void setParams(){
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

    protected void setSt(){
        int cost = (user.getSt() - 10) * 10;

        if(cost != 0){
            if(user.getNoFineManipulators()){
                cost = (int) (cost - (cost * 0.4));
            }

            int intSm = Integer.parseInt(sm.getText());
            if(intSm > 0){
                if(intSm < 8){
                    cost = (int) (cost - (cost * (0.1 * intSm)));
                }
                else{
                    cost = (int) (cost - (cost * 0.8));
                }
            }
        }

        int finalCost = Integer.parseInt(user.getCurrentPoints()) + cost - Integer.parseInt(stCost.getText());
        user.setCurrentPoints(Integer.toString(finalCost));
        currentPoints.setText(Integer.toString(finalCost));
        stCost.setText(Integer.toString(cost));
    }

    private void setDx(){
        int cost = (user.getDx() - 10) * 20;

        if(user.getNoFineManipulators() && cost != 0){
            cost = (int) (cost - (cost * 0.4));
        }

        int finalCost = Integer.parseInt(user.getCurrentPoints()) + cost - Integer.parseInt(dxCost.getText());
        user.setCurrentPoints(Integer.toString(finalCost));
        currentPoints.setText(Integer.toString(finalCost));
        dxCost.setText(Integer.toString(cost));
    }

    private void setIq(){
        int cost = (user.getIq() - 10) * 20;
        int finalCost = Integer.parseInt(user.getCurrentPoints()) + cost - Integer.parseInt(iqCost.getText());
        user.setCurrentPoints(Integer.toString(finalCost));
        currentPoints.setText(Integer.toString(finalCost));
        iqCost.setText(Integer.toString(cost));
    }

    private void setHt(){
        int cost = (user.getHt() - 10) * 10;
        int finalCost = Integer.parseInt(user.getCurrentPoints()) + cost - Integer.parseInt(htCost.getText());
        user.setCurrentPoints(Integer.toString(finalCost));
        currentPoints.setText(Integer.toString(finalCost));
        htCost.setText(Integer.toString(cost));
    }

    private void setHp(){
        int cost = (user.getHp() - user.getSt()) * 2;
        
        if (user.getSm() > 0 && cost != 0) {
            if (user.getSm() < 8) {
                cost = (int) (cost - (cost * (0.1 * user.getSm())));
            } else {
                cost = (int) (cost - (cost * 0.8));
            }
        }

        int finalCost = Integer.parseInt(user.getCurrentPoints()) + cost - Integer.parseInt(hpCost.getText());
        user.setCurrentPoints(Integer.toString(finalCost));
        currentPoints.setText(Integer.toString(finalCost));
        hpCost.setText(Integer.toString(cost));
    }

    private void setWill(){
        int cost = (user.getWill() - user.getIq()) * 5;
        int finalCost = Integer.parseInt(user.getCurrentPoints()) + cost - Integer.parseInt(willCost.getText());
        user.setCurrentPoints(Integer.toString(finalCost));
        currentPoints.setText(Integer.toString(finalCost));
        willCost.setText(Integer.toString(cost));
    }

    private void setPer(){
        int cost = (user.getPer() - user.getIq()) * 5;
        int finalCost = Integer.parseInt(user.getCurrentPoints()) + cost - Integer.parseInt(perCost.getText());
        user.setCurrentPoints(Integer.toString(finalCost));
        currentPoints.setText(Integer.toString(finalCost));
        perCost.setText(Integer.toString(cost));
    }

    private void setFp(){
        int cost = (user.getFp() - user.getHt()) * 3;
        int finalCost = Integer.parseInt(user.getCurrentPoints()) + cost - Integer.parseInt(fpCost.getText());
        user.setCurrentPoints(Integer.toString(finalCost));
        currentPoints.setText(Integer.toString(finalCost));
        fpCost.setText(Integer.toString(cost));
    }


    private void setBs(){
        double defaultBs = (user.getDx() + user.getHt()) / 4;
        int cost = 0;
        int finalCost;

        double periods;
        periods = user.getBs() - defaultBs;

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
        
        finalCost = Integer.parseInt(user.getCurrentPoints()) + cost - Integer.parseInt(bsCost.getText());
        user.setCurrentPoints(Integer.toString(finalCost));
        currentPoints.setText(Integer.toString(finalCost));
        bsCost.setText(Integer.toString(cost));
    }

    private void setMove(){
        int defaultMove = (int) Double.parseDouble(bs.getText());
        int cost = user.getMove() - defaultMove;
        int finalCost = Integer.parseInt(user.getCurrentPoints()) + cost - Integer.parseInt(moveCost.getText());
        user.setCurrentPoints(Integer.toString(finalCost));
        currentPoints.setText(Integer.toString(finalCost));
        moveCost.setText(Integer.toString(cost));
    }

    private void setBg(){
        int intBg = (user.getSt() * user.getSt()) / 5;
        bg.setText(Integer.toString(intBg));
    }

    private void setDoge(){
        int intDoge = (int) user.getBs() + 3;
        doge.setText(Integer.toString(intDoge));
    }

    private void setDmg(){
        thrust.setText(Dmg.thrust(user.getSt()));
        swing.setText(Dmg.swing(user.getSt()));
    }
}
