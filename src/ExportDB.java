import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.Scanner;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.sql.*;

public class ExportDB {
    public void saveAndExport() {
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Java", "postgres", "root")) {
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM Math1";
            ResultSet data = stmt.executeQuery(sql);
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Results");
            Row header = sheet.createRow(0);

            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("FirstNumber");
            header.createCell(3).setCellValue("SecondNumber");
            header.createCell(4).setCellValue("Result");
            header.createCell(5).setCellValue("Action");

            int rowNum = 1;
            while (data.next()) {
                Row row = sheet.createRow(rowNum++);
                int id = data.getInt("id");
                float num1 = data.getFloat("num1");
                float num2 = data.getFloat("num2");
                float result = data.getFloat("result");
                String action = data.getString("action");

                row.createCell(0).setCellValue(id);
                row.createCell(1).setCellValue(TryToParseToInt(num1));
                row.createCell(2).setCellValue(TryToParseToInt(num2));
                row.createCell(3).setCellValue(TryToParseToInt(result));
                row.createCell(4).setCellValue(action);

                if(action.equals("+") || action.equals("-") || action.equals("*") || action.equals("/") || action.equals("%")){
                    System.out.printf("id: %d, number: %s, result: %s, action: %s\n",
                            id, TryToParseToInt(num1), TryToParseToInt(result), action);
                }
                else{
                    System.out.printf("id: %d, firstNum: %s, secondNum: %s, result: %s, action: %s\n",
                            id, TryToParseToInt(num1), TryToParseToInt(num2), TryToParseToInt(result),action);
                }
            }

            //Сделать авторазмер таблицы
            for (int i = 0; i < 5; i++) {
                sheet.autoSizeColumn(i);
            }

            // Сохраняем Excel в файл
            try (FileOutputStream fileOut = new FileOutputStream("Results.xlsx")) {
                workbook.write(fileOut);
            }

            System.out.println("\nДанные экспортированы в Results.xlsx");
        } catch (Exception e) {
            System.out.println("Ошибка при работе: " + e);
        }
    }

    private String TryToParseToInt(float number){
        if (number == (int) number){
            return String.valueOf((int)number);
        }
        else{
            DecimalFormat df = new DecimalFormat("0.##");
            return df.format(number);
        }
    }
}
