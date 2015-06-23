package ru.gurps.generator.lib.export;

import javafx.collections.ObservableList;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.gurps.generator.Main;
import ru.gurps.generator.controller.UsersController;
import ru.gurps.generator.lib.UserParams;
import ru.gurps.generator.models.*;

import java.io.*;
import java.util.HashMap;

public class ExcelJokSheetFormat {

    public ExcelJokSheetFormat(File newFile) {
        String parent = "\\w*.jar";
        String jarFolder = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll(parent, "");
        try {
            InputStream file = new FileInputStream(new File(jarFolder + "views"+File.separator+"xlsx"+File.separator+"forGenerator1.0.xlsx"));
            XSSFWorkbook wb = new XSSFWorkbook(file);
            Sheet sheet = wb.getSheetAt(0);

            User user = UsersController.user;
            ObservableList<Feature> features = user.features();
            Row featureRow = null;

            for(Row row : sheet) {
                for(Cell cell : row) {
                    if(cell.getCellType() == Cell.CELL_TYPE_STRING){
                        switch(cell.getStringCellValue()){
                            case "%name%":
                                cell.setCellValue(user.name);
                                break;
                            case "%tl%":
                                cell.setCellType(0);
                                cell.setCellValue(user.tl);
                                break;
                            case "%tlCost%":
                                cell.setCellType(0);
                                cell.setCellValue(user.tlCost);
                                break;
                            case "%growth%":
                                cell.setCellType(0);
                                cell.setCellValue(user.growth);
                                break;
                            case "%weight%":
                                cell.setCellType(0);
                                cell.setCellValue(user.weight);
                                break;
                            case "%age%":
                                cell.setCellType(0);
                                cell.setCellValue(user.age);
                                break;
                            case "%noFineManipulators%":
                                cell.setCellType(0);
                                cell.setCellValue(user.noFineManipulators ? 1 : 0);
                                break;
                            case "%current%":
                                cell.setCellValue(user.currentPoints);
                                cell.setCellType(0);
                                break;
                            case "%sm%":
                                cell.setCellType(0);
                                cell.setCellValue(user.sm);
                                break;
                            case "%st%":
                                cell.setCellType(0);
                                cell.setCellValue(user.st);
                                break;
                            case "%dx%":
                                cell.setCellType(0);
                                cell.setCellValue(user.dx);
                                break;
                            case "%iq%":
                                cell.setCellType(0);
                                cell.setCellValue(user.iq);
                                break;
                            case "%ht%":
                                cell.setCellType(0);
                                cell.setCellValue(user.ht);
                                break;
                            case "%hp%":
                                cell.setCellType(0);
                                cell.setCellValue(user.hp);
                                break;
                            case "%will%":
                                cell.setCellType(0);
                                cell.setCellValue(user.will);
                                break;
                            case "%per%":
                                cell.setCellType(0);
                                cell.setCellValue(user.per);
                                break;
                            case "%fp%":
                                cell.setCellType(0);
                                cell.setCellValue(user.fp);
                                break;
                            case "%bs%":
                                cell.setCellType(0);
                                cell.setCellValue(user.bs);
                                break;
                            case "%parry%":
                                cell.setCellValue(new UserParams().getParry(user.skills()));
                                cell.setCellType(0);
                                break;
                            case "%block%":
                                cell.setCellValue(new UserParams().getBlock(user.skills()));
                                cell.setCellType(0);
                                break;
                            case "%feature%":
                                featureRow = row;
                                break;
                        }
                    }
                }
            }

            if(featureRow == null) return;
            int rowNum = featureRow.getRowNum();

            sheet.removeRow(featureRow);
            HashMap<String, Object> params = new HashMap<>();
            params.put("userId", user.id);

            for(Feature feature : features){
                featureRow = sheet.createRow(rowNum);
                Cell featureCell = featureRow.createCell(0);
                Cell costCell = featureRow.createCell(6);
                String stringAddons = "";
                params.put("featureId", feature.id);
                UserFeature userFeature = (UserFeature) new UserFeature().find_by(params);

                for(Object object : new FeatureAddon().where("userFeatureId", userFeature.id)){
                    FeatureAddon featureAddon = (FeatureAddon) object;
                    Addon addon = (Addon) new Addon().find(featureAddon.addonId);
                    if(stringAddons.equals("")) stringAddons = " (" + addon.name + ", "+Main.locale.getString("level")+": " + featureAddon.level +
                            ", "+Main.locale.getString("cost")+": " + featureAddon.cost + "%";
                    else stringAddons += "; " + addon.name + ", "+Main.locale.getString("level")+": " + featureAddon.level +
                            ", "+Main.locale.getString("cost")+": " + featureAddon.cost + "%";
                }
                if(!stringAddons.equals("")) stringAddons += ")";

                sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 5));
                featureCell.setCellValue(feature.name + stringAddons);
                costCell.setCellValue(feature.cost);
                XSSFCellStyle style = wb.createCellStyle();
                if(feature.advantage) style.setFillForegroundColor(IndexedColors.GREEN.getIndex());
                else style.setFillForegroundColor(IndexedColors.RED.getIndex());
                style.setFillPattern(CellStyle.ALIGN_CENTER);
                Font font = wb.createFont();
                font.setFontHeightInPoints((short) 12);
                font.setFontName("Times New Roman");
                style.setFont(font);
                costCell.setCellStyle(style);
                featureCell.setCellStyle(style);
                rowNum ++;
            }

            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
            for(Row r : sheet) {
                for(Cell c : r) {
                    if(c.getCellType() == Cell.CELL_TYPE_FORMULA) {
                        if(c.getCellFormula().equals("SUM(C4:C7,F4:F7,G31:G31,E9,F1)"))
                        c.setCellFormula("SUM(C4:C7,F4:F7,G31:G" + rowNum + ",E9,F1)");
                        evaluator.evaluateFormulaCell(c);
                    }
                }
            }

            FileOutputStream fileOutputStream = new FileOutputStream(newFile);
            wb.write(fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            file.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
