import java.sql.*;
import java.util.Scanner;
import java.util.Set;

public class CreateDB {
    private Task task;

    public CreateDB(Task taskTemp){
        task = taskTemp;
    }

    public void createTableForTask1(Scanner scanner) {
        System.out.println("Введите название таблицы:");
        String tempStr = scanner.nextLine();
        task.tableName = normalizeTableName(tempStr);

        //Запрос для создания таблицы
        String sqlString = "CREATE TABLE IF NOT EXISTS " +  task.tableName + " (" +
                "id SERIAL PRIMARY KEY, " +
                "num1 FLOAT NOT NULL, " +
                "num2 FLOAT NOT NULL, " +
                "result FLOAT," +
                "action TEXT);";

        create(sqlString,scanner);
    }

    public void createTableForTask2And4(Scanner scanner) {
        System.out.println("Введите название таблицы:");
        String tempStr = scanner.nextLine();
        task.tableName = normalizeTableName(tempStr);

        //Запрос для создания таблицы
        String sqlString = "CREATE TABLE IF NOT EXISTS " +  task.tableName + " (" +
                "id SERIAL PRIMARY KEY, " +
                "string1 TEXT, " +
                "string2 TEXT," +
                "result TEXT);";

        create(sqlString,scanner);
    }

    public void createTableForTask3(Scanner scanner) {
        System.out.println("Введите название таблицы:");
        String tempStr = scanner.nextLine();
        task.tableName = normalizeTableName(tempStr);

        //Запрос для создания таблицы
        String sqlString = "CREATE TABLE IF NOT EXISTS " +  task.tableName + " (" +
                "id SERIAL PRIMARY KEY, " +
                "numbers TEXT, " +
                "result TEXT);";

        create(sqlString,scanner);
    }

    private void create(String sqlString,Scanner scanner){
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Java", "postgres", "root")) {
            String[] tables  = getTables(conn);
            for (String table : tables) {
                if (task.tableName.equals(table)) {
                    task.tableName = "";
                    System.out.println("Такая таблица уже существует.");
                    while (true) {
                        System.out.println("Желаете создать другую? 0 - нет, 1 - да");
                        String ans = scanner.nextLine();

                        if (ans.contains("1")) {
                            if (task.taskNumber == 1) {
                                createTableForTask1(scanner);
                            } else if (task.taskNumber == 2 || task.taskNumber == 4) {
                                createTableForTask2And4(scanner);
                            } else {
                                createTableForTask3(scanner);
                            }
                        } else if (ans.contains("0")){
                            task.start(scanner);
                        }
                        else{
                            System.out.println("Ошибка ввода!");
                        }
                    }
                }
            }

            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sqlString);
            System.out.println("Таблица '" +  task.tableName + "' создана.");
            task.start(scanner);
        } catch (Exception e) {
            System.out.println("Ошибка при работе: " + e);
        }
    }

    private static String normalizeTableName(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "mytable"; // Дефолтное имя, если ввод пустой
        }

        String normalized = input.toLowerCase();
        // Шаг 2: Замена пробелов и спецсимволов на _
        normalized = normalized.replaceAll("\\s+", "_"); // Пробелы на _
        normalized = normalized.replaceAll("[^a-z0-9_]", "");

        if (normalized.isEmpty()) {
            return "mytable";
        }
        if (normalized.matches("^\\d.*")) {
            normalized = "_" + normalized;
        }
        if (normalized.length() > 63) {
            normalized = normalized.substring(0, 63);
        }

        //зарезервированные слова
        String[] reservedWords = {"select", "insert", "update", "delete", "table", "create"}; // Примеры
        for (String word : reservedWords) {
            if (normalized.equals(word)) {
                return "mytable_" + System.currentTimeMillis();
            }
        }

        return normalized;
    }

    private String[] getTables(Connection conn) throws SQLException {
        //Как работает эта строка - хз, нашел в инете
        String sql = "SELECT table_name " +
                "FROM information_schema.tables " +
                "WHERE table_schema='public' " +
                "ORDER BY table_name;";

        String[] names = new String[100];
        int counter = 0;
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            String name = rs.getString("table_name");
            names[counter] = name;
            counter++;
        }

        return names;
    }
}
