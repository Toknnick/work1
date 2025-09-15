import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Scanner;

public class CreateDB {
    public static void createTable(Scanner scanner) {
        System.out.println("Введите название таблицы:");
        String tableName = scanner.nextLine();

        //Запрос для создания таблицы
        String sqlString = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                "id SERIAL PRIMARY KEY, " +
                "num1 INT NOT NULL, " +
                "num2 INT NOT NULL, " +
                "result INT" +
                ");";

        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Java", "postgres", "root")) {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sqlString);
            System.out.println("Таблица '" + tableName + "' создана.");
        } catch (Exception e) {
            System.out.println("Ошибка при работе: " + e);
        }
    }
}
