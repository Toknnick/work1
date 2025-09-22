import java.sql.*;
import java.util.Scanner;

public class ShowerDB {
    private Task task;

    public ShowerDB(Task taskTemp) {
        task = taskTemp;
    }

    public void show(Scanner scanner) {
        //Как работает эта строка - хз, нашел в инете
        String sql = "SELECT table_name " +
                "FROM information_schema.tables " +
                "WHERE table_schema='public' " +
                "ORDER BY table_name;";


        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Java", "postgres", "root")) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            String[] namesTask1 = new String[100];
            String[] namesTask2And4 = new String[100];
            String[] namesTask3 = new String[100];

            int counterTask1 = 1;
            int counterTask2And4 = 1;
            int counterTask3 = 1;

            System.out.println("Список таблиц:");
            //Выводим каждую таблицу
            while (rs.next()) {
                String name = rs.getString("table_name");

                boolean hasNum1 = hasColumn(conn, name, "num1");
                boolean hasAction = hasColumn(conn, name, "action");

                if (hasNum1) {
                    // Task1: есть num1
                    namesTask1[counterTask1] = name;
                    counterTask1++;
                } else if (hasAction) {
                    namesTask3[counterTask3] = name;
                    counterTask3++;
                } else {
                    namesTask2And4[counterTask2And4] = name;
                    counterTask2And4++;
                }

            }
            boolean loop = true;

            if (task.taskNumber == 1) {
                if (counterTask1 == 1){
                    System.out.println("Таблиц нет!");
                    task.start(scanner);
                }
                for (int i = 1; i < counterTask1; i++) {
                    System.out.println((i) + ". " + namesTask1[i]);
                }
            }
            else if (task.taskNumber == 2 || task.taskNumber == 4) {
                if (counterTask2And4 == 1){
                    System.out.println("Таблиц нет!");
                    task.start(scanner);
                }
                for (int i = 1; i < counterTask2And4; i++) {
                    System.out.println((i) + ". " + namesTask2And4[i]);
                }
            }
            else {
                if (counterTask3 == 1){
                    System.out.println("Таблиц нет!");
                    task.start(scanner);
                }
                for (int i = 1; i < counterTask3; i++) {
                    System.out.println((i) + ". " + namesTask3[i]);
                }
            }

            while (loop) {
                loop = choose(scanner, namesTask1, namesTask2And4,namesTask3);
                scanner.nextLine();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private boolean choose(Scanner scanner,String[] names1,String[] names2,String[] names3){
        if (task.tableName.isEmpty()) {
            System.out.println("Выберите номер таблицы, если не хотите выбирать - напишите цифру ноль");
            try {
                int ans = scanner.nextInt();

                if (ans == 0){
                    return false;
                }

                if (task.taskNumber == 1) {
                    task.tableName = names1[ans];
                }
                else if (task.taskNumber == 2 || task.taskNumber == 4) {
                    task.tableName = names2[ans];
                }
                else {
                    task.tableName = names3[ans];
                }
                return false;
            } catch (Exception e) {
                System.out.println("Ошибка ввода!");
                return true;
            }
        }
        return false;
    }

    // Метод для проверки наличия колонки (без try-catch для нормального случая)
    private static boolean hasColumn(Connection conn, String tableName, String columnName) {
        String checkSql = "SELECT COUNT(*) FROM information_schema.columns " +
                "WHERE table_schema = 'public' AND table_name = ? AND column_name = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(checkSql)) {
            pstmt.setString(1, tableName.toLowerCase()); // PostgreSQL имена в нижнем регистре по умолчанию
            pstmt.setString(2, columnName.toLowerCase());

            try (ResultSet rsCheck = pstmt.executeQuery()) {
                if (rsCheck.next()) {
                    return rsCheck.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Ошибка проверки колонки '" + columnName + "' в '" + tableName + "': " + e.getMessage());
            // Не прерываем цикл — возвращаем false
        }
        return false;
    }
}




