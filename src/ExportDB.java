import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.Scanner;

public class ExportDB {
    private Task task;

    public ExportDB(Task taskTemp){
        task = taskTemp;
    }

    public void saveAndExportTask(Scanner scanner) throws SQLException {
        if (task.tableName.isEmpty()){
            System.out.println("Вы не выбрали/создали таблицу!");
            task.start(scanner);
        }
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Java", "postgres", "root")) {
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM " + task.tableName;
            ResultSet data = stmt.executeQuery(sql);
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("ResultsTask" + task.taskNumber);
                Row header = sheet.createRow(0);

                header.createCell(0).setCellValue(task.tableName);

                if(task.taskNumber == 1) {
                    header.createCell(1).setCellValue("ID");
                    header.createCell(2).setCellValue("FirstNumber");
                    header.createCell(3).setCellValue("SecondNumber");
                    header.createCell(4).setCellValue("Result");
                    header.createCell(5).setCellValue("Action");
                } else if (task.taskNumber == 2 || task.taskNumber == 4) {
                    header.createCell(1).setCellValue("ID");
                    header.createCell(2).setCellValue("FirstString");
                    header.createCell(3).setCellValue("SecondString");
                    header.createCell(4).setCellValue("Result");
                } else if (task.taskNumber == 3) {
                    header.createCell(1).setCellValue("ID");
                    header.createCell(2).setCellValue("Numbers");
                    header.createCell(3).setCellValue("Result");
                }

                int rowNum = 1;

                while (data.next()) {
                    Row row = sheet.createRow(rowNum++);
                    int id = data.getInt("id");
                    row.createCell(1).setCellValue(id);

                    if (task.taskNumber == 1){
                        float num1 = data.getFloat("num1");
                        float num2 = data.getFloat("num2");
                        float result = data.getFloat("result");
                        String action = data.getString("action");

                        createCellInWorkBook(2, row, num1, workbook);
                        createCellInWorkBook(3, row, num2, workbook);
                        createCellInWorkBook(4, row, result, workbook);
                        row.createCell(5).setCellValue(action);

                        if (action.equals("+") || action.equals("-") || action.equals("*") || action.equals("/") || action.equals("%")) {
                            System.out.printf("id: %d, number: %s, result: %s, action: %s\n",
                                    id, tryToParseToInt(num1), tryToParseToInt(result), action);
                        } else {
                            System.out.printf("id: %d, firstNum: %s, secondNum: %s, result: %s, action: %s\n",
                                    id, tryToParseToInt(num1), tryToParseToInt(num2), tryToParseToInt(result), action);
                        }
                    } else if (task.taskNumber == 2 || task.taskNumber == 4) {
                        String string1 = data.getString("string1");
                        String string2 = data.getString("string2");
                        String result = data.getString("result");

                        row.createCell(2).setCellValue(string1);
                        row.createCell(3).setCellValue(string2);
                        row.createCell(4).setCellValue(result);

                        if (!result.isEmpty())
                            System.out.printf("id: %d, string1: %s, string2: %s, result: %s\n",
                                    id, string1, string2, result);
                        else
                            System.out.printf("id: %d, string1: %s, string2: %s\n",
                                    id, string1, string2);
                    } else if (task.taskNumber == 3) {
                        String numbers = data.getString("numbers");
                        String result = data.getString("result");

                        row.createCell(2).setCellValue(numbers);
                        row.createCell(3).setCellValue(result);

                        System.out.printf("id: %d, numbers: %s, result: %s\n", id, numbers, result);
                    }
                }

                saveFile(workbook,sheet,"Task" + task.taskNumber);
            } catch (Exception e) {
                System.out.println("Ошибка при работе: " + e);
            }
        }
    }

    private void saveFile(Workbook workbook,Sheet sheet,String name){
        //Сделать авто размер таблицы
        for (int i = 0; i < 5; i++) {
            sheet.autoSizeColumn(i);
        }

        String desktopPath = name + ".xlsx";

        try (FileOutputStream fileOut = new FileOutputStream(desktopPath)) {
            workbook.write(fileOut);
            System.out.println("Файл сохранён на: " + desktopPath);
        } catch (Exception e) {
            System.out.println("Ошибка при работе: " + e);
        }
    }

    private void createCellInWorkBook(int idRow,Row row,float num,Workbook workbook){
        DataFormat format = workbook.createDataFormat();
        CellStyle styleTwoDecimal = workbook.createCellStyle();
        styleTwoDecimal.setDataFormat(format.getFormat("0.00"));
        Cell cell = row.createCell(idRow);

        if (num != (int) num)
            cell.setCellStyle(styleTwoDecimal);

        cell.setCellValue(num);
    }

    private String tryToParseToInt(float number){
        if (number == (int) number){
            return String.valueOf((int)number);
        }
        else{
            DecimalFormat df = new DecimalFormat("0.##");
            return df.format(number);
        }
    }
}
