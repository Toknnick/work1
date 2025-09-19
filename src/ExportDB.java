import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.*;
import java.text.DecimalFormat;

public class ExportDB {
    public void saveAndExportTask1() throws SQLException {
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Java", "postgres", "root")) {
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM Math";
            ResultSet data = stmt.executeQuery(sql);

            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("ResultsTask1");
                Row header = sheet.createRow(0);

                header.createCell(0).setCellValue("ID");
                header.createCell(1).setCellValue("FirstNumber");
                header.createCell(2).setCellValue("SecondNumber");
                header.createCell(3).setCellValue("Result");
                header.createCell(4).setCellValue("Action");


                int rowNum = 1;

                while (data.next()) {
                    Row row = sheet.createRow(rowNum++);
                    int id = data.getInt("id");
                    float num1 = data.getFloat("num1");
                    float num2 = data.getFloat("num2");
                    float result = data.getFloat("result");
                    String action = data.getString("action");

                    row.createCell(0).setCellValue(id); // id — целое число, стиль не нужен
                    createCellInWorkBook(1,row,num1,workbook);
                    createCellInWorkBook(2,row,num2,workbook);
                    createCellInWorkBook(3,row,result,workbook);
                    row.createCell(4).setCellValue(action);

                    if (action.equals("+") || action.equals("-") || action.equals("*") || action.equals("/") || action.equals("%")) {
                        System.out.printf("id: %d, number: %s, result: %s, action: %s\n",
                                id, tryToParseToInt(num1), tryToParseToInt(result), action);
                    } else {
                        System.out.printf("id: %d, firstNum: %s, secondNum: %s, result: %s, action: %s\n",
                                id, tryToParseToInt(num1), tryToParseToInt(num2), tryToParseToInt(result), action);
                    }
                }

                saveFile(workbook,sheet,"Task3");
            } catch (Exception e) {
                System.out.println("Ошибка при работе: " + e);
            }
        }
    }

    public void saveAndExportTask2And4(String table) throws SQLException {
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Java", "postgres", "root")) {
            Statement stmt = conn.createStatement();
            String sql = String.format("SELECT * FROM %s",table);
            ResultSet data = stmt.executeQuery(sql);

            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("ResultsTask2");
                Row header = sheet.createRow(0);

                header.createCell(0).setCellValue("ID");
                header.createCell(1).setCellValue("FirstString");
                header.createCell(2).setCellValue("SecondString");
                header.createCell(3).setCellValue("Result");

                int rowNum = 1;

                while (data.next()) {
                    Row row = sheet.createRow(rowNum++);
                    int id = data.getInt("id");
                    String string1 = data.getString("string1");
                    String string2 = data.getString("string2");
                    String result = data.getString("result");

                    row.createCell(0).setCellValue(id);
                    row.createCell(1).setCellValue(string1);
                    row.createCell(2).setCellValue(string2);
                    row.createCell(3).setCellValue(result);

                    if (!result.isEmpty())
                        System.out.printf("id: %d, string1: %s, string2: %s, result: %s\n",
                                id, string1, string2, result);
                    else
                        System.out.printf("id: %d, string1: %s, string2: %s\n",
                                id, string1, string2);
                }

                saveFile(workbook,sheet,table);
            } catch (Exception e) {
                System.out.println("Ошибка при работе: " + e);
            }
        }
    }

    public void saveAndExportTask3() throws SQLException {
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Java", "postgres", "root")) {
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM Task3";
            ResultSet data = stmt.executeQuery(sql);

            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("ResultsTask1");
                Row header = sheet.createRow(0);

                header.createCell(0).setCellValue("ID");
                header.createCell(1).setCellValue("Numbers");
                header.createCell(2).setCellValue("Result");
                int rowNum = 1;

                while (data.next()) {
                    Row row = sheet.createRow(rowNum++);
                    int id = data.getInt("id");
                    String numbers = data.getString("numbers");
                    String result = data.getString("result");

                    row.createCell(0).setCellValue(id);
                    row.createCell(1).setCellValue(numbers);
                    row.createCell(2).setCellValue(result);

                    System.out.printf("id: %d, numbers: %s, result: %s\n", id, numbers, result);
                }

                saveFile(workbook,sheet,"Task2");
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

        // Сохраняем Excel в файл
        String userHome = System.getProperty("user.home");
        String desktopPath = userHome + File.separator + "Desktop" + File.separator + name + ".xlsx";

        try (FileOutputStream fileOut = new FileOutputStream(desktopPath)) {
            workbook.write(fileOut);
            System.out.println("Файл сохранён на рабочем столе: " + desktopPath);
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
