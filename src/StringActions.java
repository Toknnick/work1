import java.sql.*;
import java.util.Scanner;

public class StringActions {

    private String firstString = "";
    private String secondString = "";
    private String result;
    private Task task;

    public StringActions(Task taskTemp){
        task = taskTemp;
    }

    public void getStrings(Scanner scanner,boolean isAction){
        result = "";
        System.out.println("Введите первую строку");
        firstString = scanner.nextLine();
        System.out.println("Введите вторую строку");
        secondString = scanner.nextLine();

        if(isAction)
            defaultAction();
    }

    public void getLenOfStrings(Scanner scanner){
        if (firstString.isEmpty() || secondString.isEmpty()){
            System.out.println("Нет строк! Введите новые!");
            getStrings(scanner,false);
        }
        result = String.format("%d + %d = %d",firstString.length(),secondString.length(),firstString.length() + secondString.length());
        defaultAction();
    }

    public void addLines(Scanner scanner){
        if (firstString.isEmpty() || secondString.isEmpty()){
            System.out.println("Нет строк! Введите новые!");
            getStrings(scanner,false);
        }
        result = firstString + secondString;
        defaultAction();
    }

    public void compare(Scanner scanner){
        if (firstString.isEmpty() || secondString.isEmpty()){
            System.out.println("Нет строк! Введите новые!");
            getStrings(scanner,false);
        }
        if (firstString.equals(secondString)) {
            result = "Строки равны";
        } else {
            result = "Строки не равны";
        }

        defaultAction();
    }

    private void defaultAction() {
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Java", "postgres", "root")) {
            Statement stmt = conn.createStatement();

            if (task.tableName.isEmpty()){
                System.out.println("Вы не выбрали/создали таблицу!");
                return;
            }

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
