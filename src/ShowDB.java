import java.sql.*;
import java.util.Scanner;

public class ShowDB {
    public static void showDB(Scanner scanner) {
        //Как работает эта строка - хз, нашел в инете
            String sql = "SELECT table_name " +
                    "FROM information_schema.tables " +
                    "WHERE table_schema='public' " +
                    "ORDER BY table_name;";

            try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Java", "postgres", "root")){
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql);

                System.out.println("Список таблиц:");
                //Выводим каждую таблицу
                while (rs.next()) {
                    System.out.println("- " + rs.getString("table_name"));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}
