package ru.gurps.generator.lib.export;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.gurps.generator.controller.helpers.AbstractController;
import ru.gurps.generator.lib.CharacterParams;
import ru.gurps.generator.models.rules.Feature;
import ru.gurps.generator.models.rules.Skill;
import ru.gurps.generator.models.rules.Spell;

import java.io.*;

public class ExcelJokSheetFormat extends AbstractController {
    private Sheet sheet;
    XSSFWorkbook wb;
    private final static XSSFColor ADVANTAGE = new XSSFColor(new java.awt.Color(41, 173, 63));
    private final static XSSFColor DISADVANTAGE = new XSSFColor(new java.awt.Color(178, 71, 75));
    private final static XSSFColor SKILLS = new XSSFColor(new java.awt.Color(159, 200, 105));
    private final static XSSFColor SPILLS = new XSSFColor(new java.awt.Color(95, 188, 182));

    public ExcelJokSheetFormat(File newFile) {
        try {
            InputStream file = new FileInputStream(new File(jarFolder + "views" + File.separator +
                    "xlsx" + File.separator + "vFromJok.xlsx"));
            wb = new XSSFWorkbook(file);
            sheet = wb.getSheetAt(0);

            Row featureRow = null;

            for(Row row : sheet) {
                for(Cell cell : row) {
                    if(cell.getCellType() == Cell.CELL_TYPE_STRING){
                        switch(cell.getStringCellValue()){
                            case "%name%":
                                cell.setCellValue(character.name);
                                break;
                            case "%tl%":
                                cell.setCellType(0);
                                cell.setCellValue(character.tl);
                                break;
                            case "%tlCost%":
                                cell.setCellType(0);
                                cell.setCellValue(character.tlCost);
                                break;
                            case "%growth%":
                                cell.setCellType(0);
                                cell.setCellValue(character.growth);
                                break;
                            case "%weight%":
                                cell.setCellType(0);
                                cell.setCellValue(character.weight);
                                break;
                            case "%age%":
                                cell.setCellType(0);
                                cell.setCellValue(character.age);
                                break;
                            case "%noFineManipulators%":
                                cell.setCellType(0);
                                cell.setCellValue(character.noFineManipulators ? 1 : 0);
                                break;
                            case "%current%":
                                cell.setCellValue(character.currentPoints);
                                cell.setCellType(0);
                                break;
                            case "%sm%":
                                cell.setCellType(0);
                                cell.setCellValue(character.sm);
                                break;
                            case "%st%":
                                cell.setCellType(0);
                                cell.setCellValue(character.st);
                                break;
                            case "%dx%":
                                cell.setCellType(0);
                                cell.setCellValue(character.dx);
                                break;
                            case "%iq%":
                                cell.setCellType(0);
                                cell.setCellValue(character.iq);
                                break;
                            case "%ht%":
                                cell.setCellType(0);
                                cell.setCellValue(character.ht);
                                break;
                            case "%hp%":
                                cell.setCellType(0);
                                cell.setCellValue(character.hp);
                                break;
                            case "%will%":
                                cell.setCellType(0);
                                cell.setCellValue(character.will);
                                break;
                            case "%per%":
                                cell.setCellType(0);
                                cell.setCellValue(character.per);
                                break;
                            case "%fp%":
                                cell.setCellType(0);
                                cell.setCellValue(character.fp);
                                break;
                            case "%bs%":
                                cell.setCellType(0);
                                cell.setCellValue(character.bs);
                                break;
                            case "%parry%":
                                cell.setCellValue(new CharacterParams().getParry(character.skills()));
                                cell.setCellType(0);
                                break;
                            case "%block%":
                                cell.setCellValue(new CharacterParams().getBlock(character.skills()));
                                cell.setCellType(0);
                                break;
                            case "%feature%":
                                featureRow = row;
                                break;
                        }
                    }
                }
            }

            if(featureRow != null){
                int beginRowNum = featureRow.getRowNum();
                int endRowNum = setFeatures(featureRow);
                FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
                for(Row r : sheet) {
                    for(Cell c : r) {
                        if(c.getCellType() == Cell.CELL_TYPE_FORMULA) {
                            if(c.getCellFormula().equals("SUM(C4:C7,F4:F7,G31:G31,E9,F1)"))
                                c.setCellFormula("SUM(C4:C7,F4:F7" +
                                        ",G" + beginRowNum + ":G" + endRowNum +
                                        ",L" + beginRowNum + ":L" + endRowNum +
                                        ",E9,F1)");
                            evaluator.evaluateFormulaCell(c);
                        }
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

    private int setFeatures(Row featureRow) {
        Row row;
        int rowNum = featureRow.getRowNum();
        int skillRowNum = featureRow.getRowNum();
        sheet.removeRow(featureRow);

        for(Feature feature : character.features()){
            XSSFCellStyle style = wb.createCellStyle();
            Font font = wb.createFont();
            font.setFontHeightInPoints((short) 12);
            font.setFontName("Times New Roman");
            style.setFillPattern(CellStyle.ALIGN_CENTER);
            style.setFont(font);
            style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
            style.setBorderTop(XSSFCellStyle.BORDER_THIN);
            style.setBorderRight(XSSFCellStyle.BORDER_THIN);
            style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
            style.setFillPattern(CellStyle.SOLID_FOREGROUND);

            row = sheet.createRow(rowNum);
            Cell featureCell = row.createCell(0);
            Cell costCell = row.createCell(6);
            sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 5));

            featureCell.setCellValue(CharacterParams.featureFullNameRu(feature));
            costCell.setCellValue(feature.cost);

            if(feature.advantage) style.setFillForegroundColor(ADVANTAGE);
            else style.setFillForegroundColor(DISADVANTAGE);

            featureCell.setCellStyle(style);
            costCell.setCellStyle(style);
            rowNum ++;
        }

        for(Skill skill : character.skills()){
            XSSFCellStyle style = wb.createCellStyle();
            Font font = wb.createFont();
            font.setFontHeightInPoints((short) 12);
            font.setFontName("Times New Roman");
            style.setFillPattern(CellStyle.ALIGN_CENTER);
            style.setFont(font);
            style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
            style.setBorderTop(XSSFCellStyle.BORDER_THIN);
            style.setBorderRight(XSSFCellStyle.BORDER_THIN);
            style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
            style.setFillPattern(CellStyle.SOLID_FOREGROUND);

            if(skillRowNum < rowNum) row = sheet.getRow(skillRowNum);
            else row = sheet.createRow(skillRowNum);

            Cell skillCell = row.createCell(7);
            Cell complexityCell = row.createCell(9);
            Cell levelCell = row.createCell(10);
            Cell costCell = row.createCell(11);
            sheet.addMergedRegion(new CellRangeAddress(skillRowNum, skillRowNum, 7, 8));

            skillCell.setCellValue(skill.name);
            complexityCell.setCellValue(skill.getTypeAndComplexity());
            levelCell.setCellValue(skill.level);
            levelCell.setCellStyle(style);
            costCell.setCellValue(skill.cost);
            style.setFillForegroundColor(SKILLS);

            skillCell.setCellStyle(style);
            complexityCell.setCellStyle(style);
            costCell.setCellStyle(style);
            skillRowNum++;
        }

        for(Spell spell : character.spells()){
            XSSFCellStyle style = wb.createCellStyle();
            Font font = wb.createFont();
            font.setFontHeightInPoints((short) 12);
            font.setFontName("Times New Roman");
            style.setFillPattern(CellStyle.ALIGN_CENTER);
            style.setFont(font);
            style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
            style.setBorderTop(XSSFCellStyle.BORDER_THIN);
            style.setBorderRight(XSSFCellStyle.BORDER_THIN);
            style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
            style.setFillPattern(CellStyle.SOLID_FOREGROUND);

            if(skillRowNum < rowNum) row = sheet.getRow(skillRowNum);
            else row = sheet.createRow(skillRowNum);

            Cell spellCell = row.createCell(7);
            Cell complexityCell = row.createCell(9);
            Cell levelCell = row.createCell(10);
            Cell costCell = row.createCell(11);
            sheet.addMergedRegion(new CellRangeAddress(skillRowNum, skillRowNum, 7, 8));

            spellCell.setCellValue(spell.name);
            complexityCell.setCellValue(spell.getComplexity());
            levelCell.setCellValue(spell.level);
            costCell.setCellValue(spell.finalCost);
            style.setFillForegroundColor(SPILLS);

            spellCell.setCellStyle(style);
            complexityCell.setCellStyle(style);
            levelCell.setCellStyle(style);
            costCell.setCellStyle(style);
            skillRowNum++;
        }

        return skillRowNum > rowNum ? skillRowNum : rowNum;
    }
}
