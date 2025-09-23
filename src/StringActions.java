import java.sql.*;
import java.util.Scanner;

public class StringActions {

    private String firstString = "";
    private String secondString = "";
    private String result;
    private String tempString;
    private Task task;

    public StringActions(Task taskTemp){
        task = taskTemp;
    }

    private void chooseString(Scanner scanner){
        if(check(scanner)) {
            tempString = "";
            while (true) {
                System.out.println("Введите номер строки:\n0.Назад");
                String ans = scanner.nextLine();
                if (ans.contains("1")) {
                    tempString = firstString;
                    break;
                } else if (ans.contains("2")) {
                    tempString = secondString;
                    break;
                } else if (ans.contains("0")) {
                    break;
                } else {
                    System.out.println("Ошибка Ввода!");
                }
            }
        }
    }


    public void stringToRegister(Scanner scanner) {
        if(check(scanner)) {
            chooseString(scanner);

            while (true) {
                System.out.println("1.В верхний\n2.В нижний\n0.Назад");
                String ans = scanner.nextLine();
                if (ans.contains("1")) {
                    tempString = tempString.toUpperCase();
                    break;
                } else if (ans.contains("2")) {
                    tempString = tempString.toLowerCase();
                    break;
                } else {
                    break;
                }
            }

            result = tempString;
            defaultAction();
        }
    }

    public void returnStrByIndex(Scanner scanner) {
        if(check(scanner)) {
            chooseString(scanner);

            int startIndex = 0;
            int endIndex = 0;

            try {
                System.out.println("Введите первый индекс");
                startIndex = scanner.nextInt();
                System.out.println("Введите второй индекс");
                endIndex = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Ошибка ввода!");
            }

            if (startIndex >= 0 && endIndex > startIndex && endIndex <= tempString.length()) {
                result = tempString.substring(startIndex, endIndex);
                defaultAction();
            } else {
                System.out.println("Неверные индексы!");
                returnStrByIndex(scanner);
            }
        }
    }

    public void findEndOfString(Scanner scanner) {
        if(check(scanner)) {
            chooseString(scanner);

            System.out.print("Введите подстроку для проверки: ");
            String substring = scanner.nextLine().trim();

            if (substring.isEmpty()) {
                System.out.println("Подстрока пуста!");
                findEndOfString(scanner);
            }

            // Опционально: проверка без учёта регистра
            boolean endsWithIgnoreCase = tempString.toLowerCase().endsWith(substring.toLowerCase());
            System.out.println("Без учёта регистра: " + endsWithIgnoreCase);

            // Дополнительно: найти позицию подстроки (если нужно "найти")
            int lastIndex = tempString.lastIndexOf(substring);
            if (lastIndex != -1 && lastIndex + substring.length() == tempString.length()) {
                result = "Подстрока " + substring + " найдена в конце (позиция: " + lastIndex + ").";
            } else if (lastIndex != -1) {
                result = "Подстрока " + substring + " найдена, но не в конце (позиция: " + lastIndex + ").";
            } else {
                result = "Подстрока " + substring + " не найдена.";
            }

            defaultAction();
        }
    }

    public void getStrings(Scanner scanner, boolean isAction) {
        if (task.tableName.isEmpty()){
            System.out.println("Вы не выбрали/создали таблицу!");

        }
        else {
            result = "";
            System.out.println("Введите первую строку");
            firstString = scanner.nextLine();
            System.out.println("Введите вторую строку");
            secondString = scanner.nextLine();

            if (isAction)
                defaultAction();
        }
    }

    public void getLenOfStrings(Scanner scanner){
        if(check(scanner)) {
            result = String.format("%d + %d = %d", firstString.length(), secondString.length(), firstString.length() + secondString.length());
            defaultAction();
        }
    }

    public void addLines(Scanner scanner){
        if(check(scanner)) {
            result = firstString + secondString;
            defaultAction();
        }
    }

    public void compare(Scanner scanner){
        if(check(scanner)) {
            if (firstString.equals(secondString)) {
                result = "Строки равны";
            } else {
                result = "Строки не равны";
            }

            defaultAction();
        }
    }

    private boolean check(Scanner scanner){
        if (task.tableName.isEmpty()){
            System.out.println("Вы не выбрали/создали таблицу!");
            return false;
        }
        if (firstString.isEmpty() || secondString.isEmpty()){
            System.out.println("Нет строк! Введите новые!");
            getStrings(scanner,false);
            return true;
        }
        return true;
    }

    private void defaultAction() {
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Java", "postgres", "root")) {
            Statement stmt = conn.createStatement();

            try {
                String sql = String.format("INSERT INTO %s (string1, string2, result) VALUES (?,  ?, ?)",(task.tableName));

                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, firstString);
                pstmt.setString(2, secondString);
                pstmt.setString(3, result);

                pstmt.executeUpdate();
                System.out.println("Данные успешно сохранены");
                getInfo(stmt);
            }catch (Exception e){
                System.out.println("Ошибка в запросе: " +e);
            }

        } catch (Exception e) {
            System.out.println("Ошибка при работе: " + e);
        }
    }

    private void getInfo(Statement stmt) throws SQLException {
        String sqlString = String.format("SELECT id, string1, string2, result FROM %s",(task.tableName));

        ResultSet data = stmt.executeQuery(sqlString);
        while (data.next()) {
            int id = data.getInt("id");
            String string1 = data.getString("string1");
            String string2 = data.getString("string2");
            String result = data.getString("result");

            if (!result.isEmpty())
                System.out.printf("id: %d, string1: %s, string2: %s, result: %s\n",
                        id, string1, string2, result);
            else
                System.out.printf("id: %d, string1: %s, string2: %s\n",
                        id, string1, string2);
        }
    }
}
