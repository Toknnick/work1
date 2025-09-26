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
        String tempstr2 = normalizeTableName(tempStr);
        if (tempstr2.equals("mytable")) {
            System.out.println("Ошибка ввода!");
            //createTableForTask1(scanner);
            return;
        } else {

            task.tableName = tempstr2;

            //Запрос для создания таблицы
            String sqlString = "CREATE TABLE IF NOT EXISTS " + task.tableName + " (" +
                    "id SERIAL PRIMARY KEY, " +
                    "num1 FLOAT, " +
                    "num2 FLOAT, " +
                    "result FLOAT," +
                    "action TEXT);";

            create(sqlString, scanner);
        }
    }

    public void createTableForTask2And4(Scanner scanner) {
        System.out.println("Введите название таблицы:");
        String tempStr = scanner.nextLine();
        String tempstr2 = normalizeTableName(tempStr);
        if (tempstr2.equals("mytable")){
            System.out.println("Ошибка ввода!");
            createTableForTask2And4(scanner);
            return;
        }

        task.tableName = tempstr2;

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
        String tempstr2 = normalizeTableName(tempStr);
        if (tempstr2.equals("mytable")){
            System.out.println("Ошибка ввода!");
            createTableForTask3(scanner);
            return;
        }

        task.tableName = tempstr2;

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
                                break;
                            } else if (task.taskNumber == 2 || task.taskNumber == 4) {
                                createTableForTask2And4(scanner);
                                break;
                            } else {
                                createTableForTask3(scanner);
                                break;
                            }
                        } else if (ans.contains("0")){
                            break;
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
        normalized = normalized.replaceAll("[^a-z0-9_а-я]", "");
        //1normalized = normalized.replaceAll("[^а-я0-9_]", "");

        if (normalized.isEmpty()) {
            return "mytable";
        }
        if (normalized.matches("^\\d.*")) {
            return "mytable";
        }
        if (normalized.length() > 63) {
            normalized = normalized.substring(0, 63);
        }

        //зарезервированные слова
        String[] reservedWords = {"select", "insert", "update", "delete", "table", "create"}; // Примеры
        for (String word : reservedWords) {
            if (normalized.equals(word)) {
                return "mytable";
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
