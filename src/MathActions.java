import java.sql.*;
import java.util.Scanner;

public class MathActions {
    public void addition(Scanner scanner) {
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Java", "postgres", "root")) {
            System.out.println("Введите через Enter 2 числа:");
            Statement stmt = conn.createStatement();
            firstStart(stmt);
            try {
                int firstNum = Integer.parseInt(scanner.nextLine());
                int secondNum = Integer.parseInt(scanner.nextLine());
                int result = firstNum + secondNum;
                try {
                    String sqlStringInsert = String.format("INSERT INTO Math (num1,num2,result) VALUES (%d,%d,%d)",firstNum,secondNum,result);
                    stmt.executeUpdate(sqlStringInsert);
                    System.out.println("Данные успешно сохранены");
                    getInfo(stmt);
                }catch (Exception e){
                    System.out.println("Ошибка! " +e);
                }
            } catch (Exception e) {
                System.out.println("Неверный формат ввода!");
            }
        } catch (Exception e) {
            System.out.println("Ошибка при работе: " + e);
        }
    }

    private void getInfo(Statement stmt) throws SQLException {
        String sqlString = "SELECT id, num1, num2, result FROM Math";
        ResultSet data = stmt.executeQuery(sqlString);
        while (data.next()) {
            int id = data.getInt("id");
            int num1 = data.getInt("num1");
            int num2 = data.getInt("num2");
            int result = data.getInt("result");
            System.out.printf("id: %d, firstNum: %d, secondNum: %d, sum: %d%n%n", id, num1, num2, result);
        }

    }

    private void firstStart(Statement stmt) throws SQLException {
        String sqlString = "CREATE TABLE IF NOT EXISTS Math ("+
                "id SERIAL PRIMARY KEY, " +
                "num1 INT NOT NULL, " +
                "num2 INT NOT NULL, " +
                "result INT" +
                ");";
            stmt.executeUpdate(sqlString);
    }
}
