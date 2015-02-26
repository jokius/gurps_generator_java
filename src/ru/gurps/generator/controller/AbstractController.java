package ru.gurps.generator.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import ru.gurps.generator.lib.Dmg;
import ru.gurps.generator.lib.ViewsAbstact;
import ru.gurps.generator.models.*;
import java.sql.ResultSet;
import java.util.HashMap;
import ru.gurps.generator.lib.FeatureEventHandler;

public class AbstractController extends ViewsAbstact {
    protected static User user;
    protected HashMap<String, String> params = new HashMap<>();
    protected ObservableList<Feature> advantagesData = FXCollections.observableArrayList();
    protected ObservableList<Feature> disadvantagesData = FXCollections.observableArrayList();


    protected void textEvents(){
        sm.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.equals("")) return;

                if("\\D".matches(newValue)){
                    sm.setText(Integer.toString(user.getSm()));
                    return;
                }

                int intValue = Integer.parseInt(newValue);

                if(user.getSm() == intValue) return;

                user.setSm(intValue);
                setSt();
                setHp();

                params.clear();
                params.put("sm", Integer.toString(user.getSm()));
                params.put("current_points", user.getCurrentPoints());
                new User().update(user.getId(), params);
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
                new User().update(user.getId(), params);
            }
        });

        st.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.equals("")) return;

                if(newValue.matches("\\D")){
                    st.setText(Integer.toString(user.getSt()));
                    return;
                }

                int intValue = Integer.parseInt(newValue);
                if(user.getSt() == intValue) return;
                
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
                new User().update(user.getId(), params);
            }
        });

        dx.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.equals("")) return;

                if("\\D".matches(newValue)){
                    dx.setText(Integer.toString(user.getDx()));
                    return;
                }

                int intValue = Integer.parseInt(newValue);
                if(user.getDx() == intValue) return;
                
                user.setDx(intValue);
                setDx();
                setBs();
                setMove();
                setDoge();

                params.clear();
                params.put("dx", Integer.toString(user.getDx()));
                params.put("current_points", user.getCurrentPoints());
                new User().update(user.getId(), params);
            }
        });

        iq.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.equals("")) return;

                if(newValue.matches("\\D")){
                    iq.setText(Integer.toString(user.getIq()));
                    return;
                }

                int intValue = Integer.parseInt(newValue);
                if(user.getIq() == intValue) return;
                
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
                new User().update(user.getId(), params);
            }
        });

        ht.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.equals("")) return;

                if("\\D".matches(newValue)){
                    ht.setText(Integer.toString(user.getHt()));
                    return;
                }

                int intValue = Integer.parseInt(newValue);
                if(user.getHt() == intValue) return;
                
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
                new User().update(user.getId(), params);
            }
        });

        hp.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.equals("")) return;

                if("\\D".matches(newValue)){
                    hp.setText(Integer.toString(user.getHp()));
                    return;
                }

                int intValue = Integer.parseInt(newValue);
                if(user.getHp() == intValue) return;
                
                user.setHp(intValue);
                setHp();

                params.clear();
                params.put("hp", Integer.toString(user.getHp()));
                params.put("current_points", user.getCurrentPoints());
                new User().update(user.getId(), params);
            }
        });

        will.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.equals("")) return;

                if("\\D".matches(newValue)){
                    will.setText(Integer.toString(user.getWill()));
                    return;
                }

                int intValue = Integer.parseInt(newValue);
                if(user.getWill() == intValue) return;
                
                user.setWill(intValue);
                setWill();

                params.clear();
                params.put("will", Integer.toString(user.getWill()));
                params.put("current_points", user.getCurrentPoints());
                new User().update(user.getId(), params);
            }
        });

        per.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.equals("")) return;

                if("\\D".matches(newValue)){
                    per.setText(Integer.toString(user.getPer()));
                    return;
                }

                int intValue = Integer.parseInt(newValue);
                if(user.getPer() == intValue) return;
                
                user.setPer(intValue);
                setPer();

                params.clear();
                params.put("per", Integer.toString(user.getPer()));
                params.put("current_points", user.getCurrentPoints());
                new User().update(user.getId(), params);
            }
        });

        fp.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.equals("")) return;

                if("\\D".matches(newValue)){
                    fp.setText(Integer.toString(user.getFp()));
                    return;
                }

                int intValue = Integer.parseInt(newValue);
                if(user.getFp() == intValue) return;
                
                user.setFp(intValue);
                setFp();

                params.clear();
                params.put("fp", Integer.toString(user.getFp()));
                params.put("current_points", user.getCurrentPoints());
                new User().update(user.getId(), params);
            }
        });

        bs.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.equals("")) return;

                if(newValue.matches("\\D[^.]")){
                    bs.setText(Double.toString(user.getBs()));
                    return;
                }

                double dNewValue = Double.parseDouble(newValue);
                if(user.getBs() == dNewValue) return;

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
                new User().update(user.getId(), params);
            }
        });

        move.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.equals("")) return;
                if("\\D".matches(newValue)){
                    move.setText(Integer.toString(user.getMove()));
                    return;
                }

                int intNewValue = Integer.parseInt(newValue);
                if(user.getMove() == intNewValue) return;

                user.setMove(intNewValue);
                setMove();

                params.clear();
                params.put("move", Integer.toString(user.getMove()));
                params.put("current_points", user.getCurrentPoints());
                new User().update(user.getId(), params);
            }
        });

        maxPoints.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if("\\D".matches(newValue) || newValue.equals("")){
                    maxPoints.setText(oldValue);
                    return;
                }
                if(user.getMaxPoints().equals(newValue)) return;
                
                user.setMaxPoints(newValue);
                maxPoints.setText(newValue);

                params.clear();
                params.put("max_points", user.getMaxPoints());
                new User().update(user.getId(), params);
            }
        });
    }

    protected void cellEvents(){
        initData();

        FeatureEventHandler advantagesFeatureEventHandler = new FeatureEventHandler(user, advantagesData, advantagesView,
                advantagesAddonsTableView, advantagesBottomMenu, advantagesAdd, advantagesRemove, advantagesActivate,
                advantagesAddonName, advantagesAddonNameEn, advantagesAddonLevel, advantagesAddonCost,
                advantagesFull, advantagesFinalCost, advantagesLvlLabel, advantagesLvlText, advantagesLvlSlider, advantagesFinalCostText);

        FeatureEventHandler disadvantagesFeatureEventHandler = new FeatureEventHandler(user, disadvantagesData, disadvantagesView,
                disadvantagesAddonsTableView, disadvantagesBottomMenu, disadvantagesAdd, disadvantagesRemove, disadvantagesActivate,
                disadvantagesAddonName, disadvantagesAddonNameEn, disadvantagesAddonLevel, disadvantagesAddonCost,
                disadvantagesFull, disadvantagesFinalCost, disadvantagesLvlLabel, disadvantagesLvlText, disadvantagesLvlSlider, disadvantagesFinalCostText);

        advantagesView.setRowFactory(tv -> {
            TableRow<Feature> row = new TableRow<>();
            row.addEventFilter(MouseEvent.MOUSE_CLICKED, advantagesFeatureEventHandler);
            return row;
        });

        disadvantagesView.setRowFactory(tv -> {
            TableRow<Feature> row = new TableRow<>();
            row.addEventFilter(MouseEvent.MOUSE_CLICKED, disadvantagesFeatureEventHandler);
            return row;
        });
        
        setAdvantages();
        setDisadvantages();
    }

    private void setAdvantages(){
        advantagesTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        advantagesTitleEn.setCellValueFactory(new PropertyValueFactory<>("titleEn"));
        advantagesType.setCellValueFactory(new PropertyValueFactory<>("type"));
        advantagesCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        advantagesDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        advantagesView.setItems(advantagesData);
    }

    private void setDisadvantages(){
        disadvantagesTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        disadvantagesTitleEn.setCellValueFactory(new PropertyValueFactory<>("titleEn"));
        disadvantagesType.setCellValueFactory(new PropertyValueFactory<>("type"));
        disadvantagesCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        disadvantagesDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        disadvantagesView.setItems(disadvantagesData);
    }

    private void initData() {
        try {
            ResultSet advantages = new Feature().find_by("advantage", "true");
            ResultSet disadvantages = new Feature().find_by("advantage", "false");
            advantagesData.removeAll();
            while (advantages.next()) {
                advantagesData.add(new Feature(
                        advantages.getInt("id"),
                        advantages.getBoolean("advantage"),
                        advantages.getString("title"),
                        advantages.getString("title_en"),
                        advantages.getString("type"),
                        advantages.getInt("cost"),
                        advantages.getString("description"),
                        1,
                        advantages.getInt("max_level"),
                        advantages.getBoolean("psi"),
                        advantages.getBoolean("cybernetic"),
                        false
                ));
            }

            disadvantagesData.removeAll();
            while (disadvantages.next()) {
                disadvantagesData.add(new Feature(
                        disadvantages.getInt("id"),
                        disadvantages.getBoolean("advantage"),
                        disadvantages.getString("title"),
                        disadvantages.getString("title_en"),
                        disadvantages.getString("type"),
                        disadvantages.getInt("cost"),
                        disadvantages.getString("description"),
                        1,
                        disadvantages.getInt("max_level"),
                        disadvantages.getBoolean("psi"),
                        disadvantages.getBoolean("cybernetic"),
                        false
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void setSt(){
        int cost = (user.getSt() - 10) * 10;
        if(cost != 0){
            if(user.getNoFineManipulators()) cost = (int) (cost - (cost * 0.4));
            int intSm = Integer.parseInt(sm.getText());
            if(intSm > 0) cost = intSm < 8 ? (int) (cost - (cost * (0.1 * intSm))) : (int) (cost - (cost * 0.8));
        }
        int finalCost = Integer.parseInt(user.getCurrentPoints()) + cost - Integer.parseInt(stCost.getText());
        user.setCurrentPoints(Integer.toString(finalCost));
        currentPoints.setText(Integer.toString(finalCost));
        stCost.setText(Integer.toString(cost));
    }

    protected void setDx(){
        int cost = (user.getDx() - 10) * 20;
        if(user.getNoFineManipulators() && cost != 0) cost = (int) (cost - (cost * 0.4));
        int finalCost = Integer.parseInt(user.getCurrentPoints()) + cost - Integer.parseInt(dxCost.getText());
        user.setCurrentPoints(Integer.toString(finalCost));
        currentPoints.setText(Integer.toString(finalCost));
        dxCost.setText(Integer.toString(cost));
    }

    protected void setIq(){
        int cost = (user.getIq() - 10) * 20;
        int finalCost = Integer.parseInt(user.getCurrentPoints()) + cost - Integer.parseInt(iqCost.getText());
        user.setCurrentPoints(Integer.toString(finalCost));
        currentPoints.setText(Integer.toString(finalCost));
        iqCost.setText(Integer.toString(cost));
    }

    protected void setHt(){
        int cost = (user.getHt() - 10) * 10;
        int finalCost = Integer.parseInt(user.getCurrentPoints()) + cost - Integer.parseInt(htCost.getText());
        user.setCurrentPoints(Integer.toString(finalCost));
        currentPoints.setText(Integer.toString(finalCost));
        htCost.setText(Integer.toString(cost));
    }

    protected void setHp(){
        int cost = (user.getHp() - user.getSt()) * 2;
        if (user.getSm() > 0 && cost != 0) cost = user.getSm() < 8 ? (int) (cost - (cost * (0.1 * user.getSm()))) : (int) (cost - (cost * 0.8));
        int finalCost = Integer.parseInt(user.getCurrentPoints()) + cost - Integer.parseInt(hpCost.getText());
        user.setCurrentPoints(Integer.toString(finalCost));
        currentPoints.setText(Integer.toString(finalCost));
        hpCost.setText(Integer.toString(cost));
    }

    protected void setWill(){
        int cost = (user.getWill() - user.getIq()) * 5;
        int finalCost = Integer.parseInt(user.getCurrentPoints()) + cost - Integer.parseInt(willCost.getText());
        user.setCurrentPoints(Integer.toString(finalCost));
        currentPoints.setText(Integer.toString(finalCost));
        willCost.setText(Integer.toString(cost));
    }

    protected void setPer(){
        int cost = (user.getPer() - user.getIq()) * 5;
        int finalCost = Integer.parseInt(user.getCurrentPoints()) + cost - Integer.parseInt(perCost.getText());
        user.setCurrentPoints(Integer.toString(finalCost));
        currentPoints.setText(Integer.toString(finalCost));
        perCost.setText(Integer.toString(cost));
    }

    protected void setFp(){
        int cost = (user.getFp() - user.getHt()) * 3;
        int finalCost = Integer.parseInt(user.getCurrentPoints()) + cost - Integer.parseInt(fpCost.getText());
        user.setCurrentPoints(Integer.toString(finalCost));
        currentPoints.setText(Integer.toString(finalCost));
        fpCost.setText(Integer.toString(cost));
    }


    protected void setBs(){
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

    protected void setMove(){
        int defaultMove = (int) Double.parseDouble(bs.getText());
        int cost = user.getMove() - defaultMove;
        int finalCost = Integer.parseInt(user.getCurrentPoints()) + cost - Integer.parseInt(moveCost.getText());
        user.setCurrentPoints(Integer.toString(finalCost));
        currentPoints.setText(Integer.toString(finalCost));
        moveCost.setText(Integer.toString(cost));
    }

    protected void setBg(){
        int intBg = (user.getSt() * user.getSt()) / 5;
        bg.setText(Integer.toString(intBg));
    }

    protected void setDoge(){
        int intDoge = (int) user.getBs() + 3;
        doge.setText(Integer.toString(intDoge));
    }

    protected void setDmg(){
        thrust.setText(Dmg.thrust(user.getSt()));
        swing.setText(Dmg.swing(user.getSt()));
    }
}
