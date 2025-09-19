import java.sql.*;
import java.text.DecimalFormat;
import java.util.Scanner;

public class MathActions {

    private float firstNumFloat;
    private float secondNumFloat;
    private float resultNumFloat;

    public void addition(Scanner scanner){
        try {
            getNumbers(scanner);
            resultNumFloat = firstNumFloat + secondNumFloat;
            defaultActionWithNumbers("+","Math1");
        } catch (Exception e) {
            System.out.println("Неверный формат ввода!");
        }
    }

    public void subtraction(Scanner scanner) {
        try {
            getNumbers(scanner);
            resultNumFloat = firstNumFloat - secondNumFloat;
            defaultActionWithNumbers("-","Math1");
        } catch (Exception e) {
            System.out.println("Неверный формат ввода!");
        }
    }

    public void multiplication(Scanner scanner) {
        try {
            getNumbers(scanner);
            resultNumFloat = firstNumFloat * secondNumFloat;
            defaultActionWithNumbers("*","Math1");
        } catch (Exception e) {
            System.out.println("Неверный формат ввода!");
        }
    }

    public void division(Scanner scanner) {
        try {
            getNumbers(scanner);
            resultNumFloat = firstNumFloat / secondNumFloat;
            defaultActionWithNumbers("/","Math1");
        } catch (Exception e) {
            System.out.println("Неверный формат ввода!");
        }
    }

    public void divisionModule(Scanner scanner) {
        try {
            getNumbers(scanner);
            resultNumFloat = firstNumFloat % secondNumFloat;
            defaultActionWithNumbers("%","Math1");
        } catch (Exception e) {
            System.out.println("Неверный формат ввода!");
        }
    }

    public void toModule(Scanner scanner) {
        try {
            System.out.println("Введите число:");
            String num1 = scanner.nextLine();
            firstNumFloat = Float.parseFloat(num1);
            resultNumFloat = Math.abs(firstNumFloat);
            defaultActionWithNumbers("|x|","Math2");
        } catch (Exception e) {
            System.out.println("Неверный формат ввода!");
        }
    }

    public void toDegree(Scanner scanner) {
        try {
            System.out.println("Введите число:");
            String num1 = scanner.nextLine();
            System.out.println("Введите степень:");
            int degree = scanner.nextInt();
            firstNumFloat = Float.parseFloat(num1);
            resultNumFloat = (float)Math.pow(firstNumFloat,degree);
            defaultActionWithNumbers("X^y","Math2");
        } catch (Exception e) {
            System.out.println("Неверный формат ввода!");
        }
    }

    private void defaultActionWithNumbers(String action,String table) {
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Java", "postgres", "root")) {
            Statement stmt = conn.createStatement();
            firstStart(stmt);
                try {
                    String sql = "";
                    if (table.equals("Math1"))
                        sql = "INSERT INTO Math1 (num1, num2, result, action) VALUES (?, ?, ?, ?)";
                    else
                        sql = "INSERT INTO Math1 (num1, result, action) VALUES (?, ?, ?)";

                    PreparedStatement pstmt = conn.prepareStatement(sql);

                    if (table.equals("Math1")) {
                        pstmt.setFloat(1, firstNumFloat);
                        pstmt.setFloat(2, secondNumFloat);
                        pstmt.setFloat(3, resultNumFloat);
                        pstmt.setString(4, action);
                    }
                    else{
                        pstmt.setFloat(1, firstNumFloat);
                        pstmt.setFloat(2, resultNumFloat);
                        pstmt.setString(3, action);
                    }

                    pstmt.executeUpdate();
                    System.out.println("Данные успешно сохранены");
                    getInfo(stmt,table);
                }catch (Exception e){
                    System.out.println("Ошибка в запросе: " +e);
                }

        } catch (Exception e) {
            System.out.println("Ошибка при работе: " + e);
        }
    }

    private void getInfo(Statement stmt,String table) throws SQLException {
        String sqlString = "";
        sqlString = "SELECT id, num1, num2, result,action, action FROM Math1";

        ResultSet data = stmt.executeQuery(sqlString);
        while (data.next()) {
            int id = data.getInt("id");
            float num1 = data.getFloat("num1");
            float num2 = 0;

            if (table.equals("Math1"))
                num2 = data.getFloat("num2");

            float result = data.getFloat("result");
            String action = data.getString("action");

            if ((table.equals("Math2") && !(action.equals("+") || action.equals("-") || action.equals("*") || action.equals("/") || action.equals("%")))){
                System.out.printf("id: %d, number: %s, result: %s, action: %s\n",
                        id, TryToParseToInt(num1), TryToParseToInt(result), action);
            }

            else if((table.equals("Math1") && !(action.equals("|x|") || action.equals("X^y")))){
                System.out.printf("id: %d, firstNum: %s, secondNum: %s, result: %s, action: %s\n",
                        id, TryToParseToInt(num1), TryToParseToInt(num2), TryToParseToInt(result),action);
            }
            //%s для строк
            //%d для int
        }

    }

    private String TryToParseToInt(float number){
        if (number == (int) number){
            return String.valueOf((int)number);
        }
        else{
            DecimalFormat df = new DecimalFormat("0.##");
            return df.format(number);
        }
    }

    private void firstStart(Statement stmt) throws SQLException {
        String sqlString = "CREATE TABLE IF NOT EXISTS Math (" +
                "id SERIAL PRIMARY KEY, " +
                "num1 FLOAT NOT NULL, " +
                "num2 FLOAT, " +
                "result FLOAT, " +
                "action TEXT" +
                ");";
            stmt.executeUpdate(sqlString);
    }

    private void getNumbers(Scanner scanner) {
        System.out.println("Введите через Enter 2 числа:");
        String num1 = scanner.nextLine();
        String num2 = scanner.nextLine();
        firstNumFloat = Float.parseFloat(num1);
        secondNumFloat = Float.parseFloat(num2);
    }
}
