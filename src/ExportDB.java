import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.*;
import java.text.DecimalFormat;

public class ExportDB {
    public void saveAndExport() throws SQLException {
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Java", "postgres", "root")) {
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM Math1";
            ResultSet data = stmt.executeQuery(sql);

            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Results");
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
                    row.createCell(4).setCellValue(action); // action — текст, стиль не нужен

                    if (action.equals("+") || action.equals("-") || action.equals("*") || action.equals("/") || action.equals("%")) {
                        System.out.printf("id: %d, number: %s, result: %s, action: %s\n",
                                id, tryToParseToInt(num1), tryToParseToInt(result), action);
                    } else {
                        System.out.printf("id: %d, firstNum: %s, secondNum: %s, result: %s, action: %s\n",
                                id, tryToParseToInt(num1), tryToParseToInt(num2), tryToParseToInt(result), action);
                    }
                }

                //Сделать авторазмер таблицы
                for (int i = 0; i < 5; i++) {
                    sheet.autoSizeColumn(i);
                }

                // Сохраняем Excel в файл
                String userHome = System.getProperty("user.home");
                String desktopPath = userHome + File.separator + "Desktop" + File.separator + "Results.xlsx";

                try (FileOutputStream fileOut = new FileOutputStream(desktopPath)) {
                    workbook.write(fileOut);
                    System.out.println("Файл сохранён на рабочем столе: " + desktopPath);
                } catch (Exception e) {
                    System.out.println("Ошибка при работе: " + e);
                }

            } catch (Exception e) {
                System.out.println("Ошибка при работе: " + e);
            }
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
