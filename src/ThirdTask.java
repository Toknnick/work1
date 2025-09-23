import java.sql.*;
import java.util.Scanner;

public class ThirdTask extends Task{
    public void start(Scanner scanner) throws SQLException {
        taskNumber = 3;

        ExportDB exportDB = new ExportDB(this);
        CreateDB createDB = new CreateDB(this);
        ShowerDB showerDB = new ShowerDB(this);

        boolean loop = true;
        while (loop) {
            System.out.println("Выберите действие:");
            System.out.println("""
                    1. Вывести все таблицы
                    2. Создать таблицу\s
                    3. Выполнение задачи базового варианта
                    4. Сохранить все данные в Excel и вывести на экран.
                    0.Назад""");
            String ans = scanner.nextLine();

            switch (ans) {
                case "1":
                    showerDB.show(scanner);
                    break;
                case "2":
                    createDB.createTableForTask3(scanner);
                    break;
                case "3":
                    action(scanner);
                    break;
                case "4":
                    exportDB.saveAndExportTask(scanner);
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
        if (tableName.isEmpty()){
            System.out.println("Вы не выбрали/создали таблицу!");
            return;
        }

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
            try {
                String sql = String.format("INSERT INTO %s (numbers, result) VALUES (?, ?)",tableName);
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
        String sqlString = String.format("SELECT id, numbers, result FROM %s",tableName);
        ResultSet data = stmt.executeQuery(sqlString);

        while (data.next()) {
            int id = data.getInt("id");
            String numbers = data.getString("numbers");
            String result = data.getString("result");
            System.out.printf("id: %d, numbers: %s, result: %s\n", id, numbers, result);
        }
    }

    private void wait(Scanner scanner) {
        System.out.println("\nЧтобы продолжить, нажмите Enter");
        scanner.nextLine();
    }
}
