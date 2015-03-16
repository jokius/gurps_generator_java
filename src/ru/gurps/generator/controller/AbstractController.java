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

import java.util.HashMap;

import ru.gurps.generator.lib.FeatureEventHandler;

public class AbstractController extends ViewsAbstact {
    protected static User user;
    protected HashMap<String, String> params = new HashMap<>();
    protected ObservableList<Feature> advantagesData = FXCollections.observableArrayList();
    protected ObservableList<Feature> disadvantagesData = FXCollections.observableArrayList();


    protected void textEvents() {
        sm.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.equals("")) return;

                if("\\D".matches(newValue)) {
                    sm.setText(Integer.toString(user.sm));
                    return;
                }

                int intValue = Integer.parseInt(newValue);

                if(user.sm == intValue) return;

                user.sm = intValue;
                setSt();
                setHp();

                params.clear();
                params.put("sm", Integer.toString(user.sm));
                params.put("currentPoints", user.currentPoints);
                user.update(params);
            }
        });

        noFineManipulators.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                user.noFineManipulators = newValue;
                setSt();
                setDx();

                params.clear();
                params.put("noFineManipulators", Boolean.toString(user.noFineManipulators));
                params.put("currentPoints", user.currentPoints);
                user.update(params);
            }
        });

        st.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
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

                setSt();
                setHp();
                setBg();
                setDmg();

                params.clear();

                if(intValue > user.ht) {
                    user.hp = intValue;
                    params.put("hp", Integer.toString(user.hp));
                    hp.setText(Integer.toString(user.hp));
                }
                params.put("st", Integer.toString(user.st));
                params.put("currentPoints", user.currentPoints);
                user.update(params);
            }
        });

        dx.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.equals("")) return;

                if("\\D".matches(newValue)) {
                    dx.setText(Integer.toString(user.dx));
                    return;
                }

                int intValue = Integer.parseInt(newValue);
                if(user.dx == intValue) return;

                user.dx = intValue;
                setDx();
                setBs();
                setMove();
                setDoge();

                params.clear();
                params.put("dx", Integer.toString(user.dx));
                params.put("currentPoints", user.currentPoints);
                user.update(params);
            }
        });

        iq.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.equals("")) return;

                if(newValue.matches("\\D")) {
                    iq.setText(Integer.toString(user.iq));
                    return;
                }

                int intValue = Integer.parseInt(newValue);
                if(user.iq == intValue) return;

                user.iq = intValue;
                setIq();

                params.clear();

                if(intValue > user.will) {
                    user.will = intValue;
                    params.put("will", Integer.toString(user.will));
                    will.setText(Integer.toString(user.will));
                }
                if(intValue > user.per) {
                    user.per = intValue;
                    params.put("per", Integer.toString(user.per));
                    per.setText(Integer.toString(user.per));
                }

                setWill();
                setPer();
                params.put("iq", Integer.toString(user.iq));
                params.put("currentPoints", user.currentPoints);
                user.update(params);
            }
        });

        ht.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.equals("")) return;

                if("\\D".matches(newValue)) {
                    ht.setText(Integer.toString(user.ht));
                    return;
                }

                int intValue = Integer.parseInt(newValue);
                if(user.ht == intValue) return;

                user.ht = intValue;
                setHt();

                params.clear();

                if(intValue > user.fp) {
                    user.fp = intValue;
                    params.put("fp", Integer.toString(user.fp));
                    fp.setText(Integer.toString(user.fp));
                }

                setFp();
                setBs();
                params.put("ht", Integer.toString(user.ht));
                params.put("currentPoints", user.currentPoints);
                user.update(params);
            }
        });

        hp.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.equals("")) return;

                if("\\D".matches(newValue)) {
                    hp.setText(Integer.toString(user.hp));
                    return;
                }

                int intValue = Integer.parseInt(newValue);
                if(user.hp == intValue) return;

                user.hp = intValue;
                setHp();

                params.clear();
                params.put("hp", Integer.toString(user.hp));
                params.put("currentPoints", user.currentPoints);
                user.update(params);
            }
        });

        will.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.equals("")) return;

                if("\\D".matches(newValue)) {
                    will.setText(Integer.toString(user.will));
                    return;
                }

                int intValue = Integer.parseInt(newValue);
                if(user.will == intValue) return;

                user.will = intValue;
                setWill();

                params.clear();
                params.put("will", Integer.toString(user.will));
                params.put("currentPoints", user.currentPoints);
                user.update(params);
            }
        });

        per.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.equals("")) return;

                if("\\D".matches(newValue)) {
                    per.setText(Integer.toString(user.per));
                    return;
                }

                int intValue = Integer.parseInt(newValue);
                if(user.per == intValue) return;

                user.per = intValue;
                setPer();

                params.clear();
                params.put("per", Integer.toString(user.per));
                params.put("currentPoints", user.currentPoints);
                user.update(params);
            }
        });

        fp.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.equals("")) return;

                if("\\D".matches(newValue)) {
                    fp.setText(Integer.toString(user.fp));
                    return;
                }

                int intValue = Integer.parseInt(newValue);
                if(user.fp == intValue) return;

                user.per = intValue;
                setFp();

                params.clear();
                params.put("fp", Integer.toString(user.fp));
                params.put("currentPoints", user.currentPoints);
                user.update(params);
            }
        });

        bs.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.equals("")) return;

                if(newValue.matches("\\D[^.]")) {
                    bs.setText(Double.toString(user.bs));
                    return;
                }

                double dNewValue = Double.parseDouble(newValue);
                if(user.bs == dNewValue) return;

                user.bs = dNewValue;
                bs.setText(Double.toString(user.bs));
                setBs();
                params.clear();

                int intNewValue = (int) dNewValue;

                if(intNewValue > user.move) {
                    user.move = intNewValue;
                    params.put("move", Integer.toString(user.move));
                    move.setText(Integer.toString(user.move));
                }

                setMove();
                setDoge();

                params.put("bs", Double.toString(user.bs));
                params.put("currentPoints", user.currentPoints);
                user.update(params);
            }
        });

        move.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.equals("")) return;
                if("\\D".matches(newValue)) {
                    move.setText(Integer.toString(user.move));
                    return;
                }

                int intNewValue = Integer.parseInt(newValue);
                if(user.move == intNewValue) return;

                user.move = intNewValue;
                setMove();

                params.clear();
                params.put("move", Integer.toString(user.move));
                params.put("currentPoints", user.currentPoints);
                user.update(params);
            }
        });

        maxPoints.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if("\\D".matches(newValue) || newValue.equals("")) {
                    maxPoints.setText(oldValue);
                    return;
                }
                if(user.maxPoints.equals(newValue)) return;

                user.maxPoints = newValue;
                maxPoints.setText(newValue);

                params.clear();
                params.put("maxPoints", user.maxPoints);
                user.update(params);
            }
        });
    }

    protected void cellEvents() {
        initData();

        FeatureEventHandler advantagesFeatureEventHandler = new FeatureEventHandler(user, advantagesData, advantagesView,
                advantagesAddonsTableView, advantagesBottomMenu, advantagesAdd, advantagesRemove, advantagesActivate,
                advantagesAddonName, advantagesAddonNameEn, advantagesAddonLevel, advantagesAddonCost,
                advantagesFull, advantagesFinalCost, advantagesLvlLabel, advantagesLvlText, advantagesLvlComboBox, advantagesFinalCostText,
                currentPoints);

        FeatureEventHandler disadvantagesFeatureEventHandler = new FeatureEventHandler(user, disadvantagesData, disadvantagesView,
                disadvantagesAddonsTableView, disadvantagesBottomMenu, disadvantagesAdd, disadvantagesRemove, disadvantagesActivate,
                disadvantagesAddonName, disadvantagesAddonNameEn, disadvantagesAddonLevel, disadvantagesAddonCost,
                disadvantagesFull, disadvantagesFinalCost, disadvantagesLvlLabel, disadvantagesLvlText, disadvantagesLvlComboBox, disadvantagesFinalCostText,
                currentPoints);

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

    private void setAdvantages() {
        advantagesTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        advantagesTitleEn.setCellValueFactory(new PropertyValueFactory<>("titleEn"));
        advantagesType.setCellValueFactory(new PropertyValueFactory<>("type"));
        advantagesCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        advantagesDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        advantagesView.setItems(advantagesData);
    }

    private void setDisadvantages() {
        disadvantagesTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        disadvantagesTitleEn.setCellValueFactory(new PropertyValueFactory<>("titleEn"));
        disadvantagesType.setCellValueFactory(new PropertyValueFactory<>("type"));
        disadvantagesCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        disadvantagesDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        disadvantagesView.setItems(disadvantagesData);
    }

    private void initData() {
        ObservableList<Feature> advantages = new Feature().where("advantage", true);
        ObservableList<Feature> disadvantages = new Feature().where("advantage", false);
        advantagesData.removeAll();
        advantagesData.addAll(advantages);

        disadvantagesData.removeAll();
        disadvantagesData.addAll(disadvantages);
    }

    protected void setSt() {
        int cost = (user.st - 10) * 10;
        if(cost != 0) {
            if(user.noFineManipulators) cost = (int) (cost - (cost * 0.4));
            int intSm = Integer.parseInt(sm.getText());
            if(intSm > 0) cost = intSm < 8 ? (int) (cost - (cost * (0.1 * intSm))) : (int) (cost - (cost * 0.8));
        }
        int finalCost = Integer.parseInt(user.currentPoints) + cost - Integer.parseInt(stCost.getText());
        user.currentPoints = Integer.toString(finalCost);
        currentPoints.setText(Integer.toString(finalCost));
        stCost.setText(Integer.toString(cost));
    }

    protected void setDx() {
        int cost = (user.dx - 10) * 20;
        if(user.noFineManipulators && cost != 0) cost = (int) (cost - (cost * 0.4));
        int finalCost = Integer.parseInt(user.currentPoints) + cost - Integer.parseInt(dxCost.getText());
        user.currentPoints = Integer.toString(finalCost);
        currentPoints.setText(Integer.toString(finalCost));
        dxCost.setText(Integer.toString(cost));
    }

    protected void setIq() {
        int cost = (user.iq - 10) * 20;
        int finalCost = Integer.parseInt(user.currentPoints) + cost - Integer.parseInt(iqCost.getText());
        user.currentPoints = Integer.toString(finalCost);
        currentPoints.setText(Integer.toString(finalCost));
        iqCost.setText(Integer.toString(cost));
    }

    protected void setHt() {
        int cost = (user.ht - 10) * 10;
        int finalCost = Integer.parseInt(user.currentPoints) + cost - Integer.parseInt(htCost.getText());
        user.currentPoints = Integer.toString(finalCost);
        currentPoints.setText(Integer.toString(finalCost));
        htCost.setText(Integer.toString(cost));
    }

    protected void setHp() {
        int cost = (user.hp - user.st) * 2;
        if(user.sm > 0 && cost != 0)
            cost = user.sm < 8 ? (int) (cost - (cost * (0.1 * user.sm))) : (int) (cost - (cost * 0.8));
        int finalCost = Integer.parseInt(user.currentPoints) + cost - Integer.parseInt(hpCost.getText());
        user.currentPoints = Integer.toString(finalCost);
        currentPoints.setText(Integer.toString(finalCost));
        hpCost.setText(Integer.toString(cost));
    }

    protected void setWill() {
        int cost = (user.will - user.iq) * 5;
        int finalCost = Integer.parseInt(user.currentPoints) + cost - Integer.parseInt(willCost.getText());
        user.currentPoints = Integer.toString(finalCost);
        currentPoints.setText(Integer.toString(finalCost));
        willCost.setText(Integer.toString(cost));
    }

    protected void setPer() {
        int cost = (user.per - user.iq) * 5;
        int finalCost = Integer.parseInt(user.currentPoints) + cost - Integer.parseInt(perCost.getText());
        user.currentPoints = Integer.toString(finalCost);
        currentPoints.setText(Integer.toString(finalCost));
        perCost.setText(Integer.toString(cost));
    }

    protected void setFp() {
        int cost = (user.fp - user.ht) * 3;
        int finalCost = Integer.parseInt(user.currentPoints) + cost - Integer.parseInt(fpCost.getText());
        user.currentPoints = Integer.toString(finalCost);
        currentPoints.setText(Integer.toString(finalCost));
        fpCost.setText(Integer.toString(cost));
    }


    protected void setBs() {
        double defaultBs = (user.dx + user.ht) / 4;
        int cost = 0;
        int finalCost;
        double periods;
        periods = user.bs - defaultBs;

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

        finalCost = Integer.parseInt(user.currentPoints) + cost - Integer.parseInt(bsCost.getText());
        user.currentPoints = Integer.toString(finalCost);
        currentPoints.setText(Integer.toString(finalCost));
        bsCost.setText(Integer.toString(cost));
    }

    protected void setMove() {
        int defaultMove = (int) Double.parseDouble(bs.getText());
        int cost = user.move - defaultMove;
        int finalCost = Integer.parseInt(user.currentPoints) + cost - Integer.parseInt(moveCost.getText());
        user.currentPoints = Integer.toString(finalCost);
        currentPoints.setText(Integer.toString(finalCost));
        moveCost.setText(Integer.toString(cost));
    }

    protected void setBg() {
        int intBg = (user.st * user.st) / 5;
        bg.setText(Integer.toString(intBg));
    }

    protected void setDoge() {
        int intDoge = (int) (user.bs + 3);
        doge.setText(Integer.toString(intDoge));
    }

    protected void setDmg() {
        thrust.setText(Dmg.thrust(user.st));
        swing.setText(Dmg.swing(user.st));
    }
}
