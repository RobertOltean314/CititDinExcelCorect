package org.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.*;

public class Main {

    public static void main(String[] args) {
        try {
            FileInputStream file = new FileInputStream(new File("CititDinFisier.xlsx"));
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheetAt(0);
            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();

            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Cell cellA = row.getCell(0);
                Cell cellB = row.getCell(1);
                Cell cellC = row.createCell(2);
                String concatValue = getCellValueAsString(cellA) + getCellValueAsString(cellB);
                System.out.println(concatValue);
                cellC.setCellValue(concatValue);
            }

            FileOutputStream outFile = new FileOutputStream(new File("Output.xlsx"));
            workbook.write(outFile);
            outFile.close();
            file.close();
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());
            case STRING:
                return cell.getStringCellValue();
            case FORMULA:
                FormulaEvaluator formulaEvaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
                CellValue cellValue = formulaEvaluator.evaluate(cell);
                if (cellValue.getCellType() == CellType.NUMERIC) {
                    return String.valueOf((int) cellValue.getNumberValue());
                } else if (cellValue.getCellType() == CellType.STRING) {
                    return cellValue.getStringValue();
                }
            default:
                return "";
        }
    }
}