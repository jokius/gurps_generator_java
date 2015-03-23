package ru.gurps.generator.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import ru.gurps.generator.Main;
import ru.gurps.generator.lib.LanguagesTable;
import ru.gurps.generator.lib.UserParams;
import ru.gurps.generator.lib.ViewsAbstract;
import ru.gurps.generator.models.*;
import java.io.IOException;
import java.util.ArrayList;

import ru.gurps.generator.lib.FeatureEventHandler;

public class AbstractController extends ViewsAbstract {
    public static User user;
    protected UserParams userParams;
    protected ObservableList<Feature> advantagesData = FXCollections.observableArrayList();
    protected ObservableList<Feature> disadvantagesData = FXCollections.observableArrayList();
    protected ArrayList<Integer> advantagesNumbers = new ArrayList<>();
    protected ArrayList<Integer> disadvantagesNumbers = new ArrayList<>();


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
                int oldStCost = Integer.parseInt(stCost.getText());
                userParams.setSt();
                currentPoints(stCost, oldStCost);

                int oldHpCost = Integer.parseInt(hpCost.getText());
                userParams.setHp();
                currentPoints(hpCost, oldHpCost);
                user.save();
            }
        });

        noFineManipulators.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                user.noFineManipulators = newValue;
                int oldStCost = Integer.parseInt(stCost.getText());
                userParams.setSt();
                currentPoints(stCost, oldStCost);

                int oldDxCost = Integer.parseInt(dxCost.getText());
                userParams.setDx();
                currentPoints(dxCost, oldDxCost);
                user.save();
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

                int oldStCost = Integer.parseInt(stCost.getText());
                userParams.setSt();
                currentPoints(stCost, oldStCost);

                int oldHpCost = Integer.parseInt(hpCost.getText());
                userParams.setHp();
                currentPoints(hpCost, oldHpCost);

                userParams.setBg();
                userParams.setDmg();

                if(intValue > user.ht) {
                    user.hp = intValue;
                    hp.setText(Integer.toString(user.hp));
                }
                user.save();
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

                int oldDxCost = Integer.parseInt(dxCost.getText());
                userParams.setDx();
                currentPoints(dxCost, oldDxCost);

                int oldBsCost = Integer.parseInt(bsCost.getText());
                userParams.setBs();
                currentPoints(bsCost, oldBsCost);

                int oldMoveCost = Integer.parseInt(bsCost.getText());
                userParams.setMove();
                currentPoints(moveCost, oldMoveCost);

                userParams.setDoge();
                user.save();
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

                int oldHpCost = Integer.parseInt(hpCost.getText());
                userParams.setHp();
                currentPoints(hpCost, oldHpCost);
                user.save();
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

                int oldWillCost = Integer.parseInt(willCost.getText());
                userParams.setWill();
                currentPoints(willCost, oldWillCost);
                user.save();
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

                int oldPerCost = Integer.parseInt(perCost.getText());
                userParams.setPer();
                currentPoints(perCost, oldPerCost);
                user.save();
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

                int oldFpCost = Integer.parseInt(fpCost.getText());
                userParams.setFp();
                currentPoints(htCost, oldFpCost);
                user.save();
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

                int oldBsCost = Integer.parseInt(bsCost.getText());
                userParams.setBs();
                currentPoints(bsCost, oldBsCost);

                int intNewValue = (int) dNewValue;
                if(intNewValue > user.move) {
                    user.move = intNewValue;
                    move.setText(Integer.toString(user.move));
                }

                int oldMoveCost = Integer.parseInt(bsCost.getText());
                userParams.setMove();
                currentPoints(moveCost, oldMoveCost);

                userParams.setDoge();
                user.save();
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
                int oldMoveCost = Integer.parseInt(bsCost.getText());
                userParams.setMove();
                currentPoints(moveCost, oldMoveCost);
                user.save();
            }
        });

        maxPoints.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.equals("")) return;
                if("\\D".matches(newValue)) {
                    maxPoints.setText(Integer.toString(user.move));
                    return;
                }

                if(user.maxPoints.equals(newValue)) return;
                user.maxPoints = newValue;
                maxPoints.setText(newValue);
                user.save();
            }
        });

        advantagesSearchText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.equals("")) advantagesSearchButton.setDisable(true);
                else advantagesSearchButton.setDisable(false);
            }
        });

        disadvantagesSearchText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.equals("")) disadvantagesSearchButton.setDisable(true);
                else disadvantagesSearchButton.setDisable(false);
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

        new LanguagesTable(languagesTableView, languagesNameColumn, languageSpokenColumn, languagesWrittenColumn,
                languagesCostColumn, languagesUserColumn, languagesDbColumn, languageNameText, languageSpokenChoiceBox,
                languageWrittenChoiceBox, languageCostText, languageAddButton);
    }

    protected void buttonEvents() {
        userSheet.setOnAction(event -> {
            Stage childrenStage = new Stage();
            userSheet.setDisable(true);
            childrenStage.setOnCloseRequest(we -> userSheet.setDisable(false));
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("resources/views/userSheet.fxml"));
            UserSheetController controller = new UserSheetController(currentPoints, userSheet);
            loader.setController(controller);
            Parent childrenRoot;
            try {
                childrenRoot = loader.load();
                childrenStage.setScene(new Scene(childrenRoot, 700, 795));
                childrenStage.setTitle("GURPS Лист персонажа");
                childrenStage.show();
                childrenStage.setResizable(false);
            } catch(IOException e) {
                e.printStackTrace();
            }
        });
    }

    protected void SearchEvents(){
        advantagesSearchAll.setOnAction(event ->{
            String query = "advantage=true and UPPER(title) like UPPER('%" + advantagesSearchText.getText() + "%') or " +
                    "advantage=true and UPPER(titleEn) like UPPER('%" + advantagesSearchText.getText() + "%') or " +
                    "advantage=true and UPPER(cost) like UPPER('%" + advantagesSearchText.getText() + "%') or " +
                    "advantage=true and UPPER(description) like UPPER('%" + advantagesSearchText.getText() + "%')";
            advantagesView.setItems(new Feature().where(query));
        });

        for(String feature : new String[] {"Title", "TitleEn", "Cost", "Description"}){
            try {
                MenuItem menuItem = (MenuItem) ViewsAbstract.class.getDeclaredField("advantagesSearch" + feature).get(this);
                menuItem.setOnAction(event ->{
                    String query = "advantage=true and UPPER("+ feature + ") like UPPER('%" + advantagesSearchText.getText() + "%')";
                    advantagesView.setItems(new Feature().where(query));
                });

                menuItem = (MenuItem) ViewsAbstract.class.getDeclaredField("disadvantagesSearch" + feature).get(this);
                menuItem.setOnAction(event ->{
                    String query = "advantage=false and "+ feature + " like '%" + disadvantagesSearchText.getText() + "%'";
                    disadvantagesView.setItems(new Feature().where(query));
                });
            } catch(IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }

    protected void checkBoxEvents() {
        Integer[] numbers = {1, 2, 3, 4, 5};
        for(Integer number : numbers) {
            try {
                CheckBox checkBox = (CheckBox) ViewsAbstract.class.getDeclaredField("advantage" + number + "CheckBox").get(this);
                checkBox.setSelected(true);
                checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    String query = "advantage=true and type like ";
                    if(newValue) advantagesNumbers.add(number);
                    else advantagesNumbers.remove(number);
                    for(Integer lNumber : advantagesNumbers) {
                        if(query.equals("advantage=true and type like ")) query += "'%" + lNumber + "%'";
                        else query += " or advantage=true and type like '%" + lNumber + "%'";
                    }
                    if(query.equals("advantage=true and type like ")) query = "advantage=true and type='6'";
                    advantagesView.setItems(new Feature().where(query));
                });
            } catch(NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        for(Integer number : numbers) {
            try {
                CheckBox checkBox = (CheckBox) ViewsAbstract.class.getDeclaredField("disadvantage" + number + "CheckBox").get(this);
                checkBox.setSelected(true);
                checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    String query = "advantage=false and type like ";
                    if(newValue) disadvantagesNumbers.add(number);
                    else disadvantagesNumbers.remove(number);
                    for(Integer lNumber : disadvantagesNumbers) {
                        if(query.equals("advantage=false and type like ")) query += "'%" + lNumber + "%'";
                        else query += " or advantage=false and type like '%" + lNumber + "%'";
                    }
                    if(query.equals("advantage=false and type like ")) query = "advantage=false and type='6'";
                    disadvantagesView.setItems(new Feature().where(query));
                });
            } catch(NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void setAdvantages() {
        advantagesTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        advantagesTitleEn.setCellValueFactory(new PropertyValueFactory<>("titleEn"));
        advantagesType.setCellValueFactory(new PropertyValueFactory<>("type"));
        advantagesCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        advantagesDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        advantagesView.setPlaceholder(new Label("Преимуществ нет"));
        advantagesView.setItems(advantagesData);
    }

    private void setDisadvantages() {
        disadvantagesTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        disadvantagesTitleEn.setCellValueFactory(new PropertyValueFactory<>("titleEn"));
        disadvantagesType.setCellValueFactory(new PropertyValueFactory<>("type"));
        disadvantagesCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        disadvantagesDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        disadvantagesView.setPlaceholder(new Label("Недостатков нет"));
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

    private void currentPoints(Label cost, int oldStCost) {
        user.currentPoints = Integer.toString(Integer.parseInt(user.currentPoints) + Integer.parseInt(cost.getText()) - oldStCost);
        currentPoints.setText(user.currentPoints);
    }
}
