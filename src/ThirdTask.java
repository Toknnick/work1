import java.sql.*;
import java.util.Scanner;

public class ThirdTask {
    public void start(Scanner scanner) throws SQLException {
        ExportDB exportDB = new ExportDB();
        boolean loop = true;
        while (loop) {
            System.out.println("Выберите действие:");
            System.out.println("""
                    1. Вывести все таблицы из MySQL/PostgreSQL.
                    2. Создать таблицу\s
                    3. Выполнение задачи базового варианта
                    4. Сохранить все данные в Excel и вывести на экран.
                    0.Назад""");
            String ans = scanner.nextLine();

            switch (ans) {
                case "1":
                    ShowDB.showDB(scanner);
                    break;
                case "2":
                    CreateDB.createTable(scanner);
                    break;
                case "3":
                    action(scanner);
                    break;
                case "4":
                    exportDB.saveAndExportTask3();
                case "0":
                    loop = false;
                    break;
                default:
                    System.out.println("Ошибка ввода!");
                    break;
            }

            wait(scanner);
        }
    }

    private void action(Scanner scanner){
        System.out.println("Введите сколько будет чисел:");
        int countOfNumber = 0;
        try {
            countOfNumber = scanner.nextInt();
            scanner.nextLine(); // Важно!
        } catch (Exception e) {
            System.out.println("Неверный формат ввода!");
            scanner.nextLine();
            return;
        }

        int[] numbers = new int[countOfNumber];
        for (int i = 0; i < countOfNumber; i++) {
            System.out.println("Введите число. Чтобы отменить ввод, введите число ноль");
            String tmStr = scanner.nextLine();
            if (tmStr.equals(".") || tmStr.equals(",")) {
                System.out.println("Нужны только целые числа!");
                i--;
                continue;
            }
            try {
                int num = Integer.parseInt(tmStr);
                if (num == 0)
                    break;
                numbers[i] = num;
            } catch (Exception e) {
                System.out.println("Неверный формат ввода!");
                i--;
            }
        }

        StringBuilder result = new StringBuilder();
        StringBuilder strNumbers = new StringBuilder();
        for (int num : numbers) {
            if (num == 0)
                break;

            strNumbers.append(num);
            if (num != numbers[numbers.length - 1])
                strNumbers.append(" ");

            if (num % 2 == 0) {
                result.append("Число ").append(num).append(" четное и целое. ");
            } else {
                result.append("Число ").append(num).append(" нечетное и целое. ");
            }
        }

        if (strNumbers.toString().isEmpty()){
            System.out.println("Вы не ввели ни одного числа...");
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Java", "postgres", "root")) {
            Statement stmt = conn.createStatement();
            firstStart(stmt);
            try {
                String sql = "INSERT INTO Task3 (numbers, result) VALUES (?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, strNumbers.toString());
                pstmt.setString(2, result.toString());
                pstmt.executeUpdate();
                System.out.println("Данные успешно сохранены");
                getInfo(stmt);
            } catch (Exception e) {
                System.out.println("Ошибка в запросе: " + e);
            }

        } catch (Exception e) {
            System.out.println("Ошибка при работе: " + e);
        }

    }

    private void getInfo(Statement stmt) throws SQLException {
        String sqlString = "SELECT id, numbers, result FROM Task3";
        ResultSet data = stmt.executeQuery(sqlString);

        while (data.next()) {
            int id = data.getInt("id");
            String numbers = data.getString("numbers");
            String result = data.getString("result");
            System.out.printf("id: %d, numbers: %s, result: %s\n", id, numbers, result);
        }
    }

    private void firstStart(Statement stmt) throws SQLException {
        String sqlString = "CREATE TABLE IF NOT EXISTS Task3 (" +
                "id SERIAL PRIMARY KEY, " +
                "numbers TEXT," +
                "result TEXT)";
        stmt.executeUpdate(sqlString);
    }

    private void wait(Scanner scanner) {
        System.out.println("\nЧтобы продолжить, нажмите Enter");
        scanner.nextLine();
    }
}
