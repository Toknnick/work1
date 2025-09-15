import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Postgresql {

    public static void main(String[] args) {


        Scanner scanner = new Scanner(System.in);

        String postURL = "jdbc:postgresql://localhost:5432/Java";
        Connection con;

        try {
            con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Java", "postgres", "root");
            System.out.println("Подключение к базе данных... Успешно!");
        } catch (SQLException e) {
            System.out.println("Ошибка " + e);
        }

        //System.out.println("Введите название таблицы:");


        while (true) {
            System.out.println("Выберите действие:");
            System.out.println("1.Вывести все таблицы");
            System.out.println("2.Создать таблицу ");
            System.out.println("3.Сложение чисел, результат сохранить");
            System.out.println("4.Вычитание чисел, результат сохранить");
            System.out.println("5.Умножение чисел, результат сохранить");
            System.out.println("6.Деление чисел, результат сохранить ");
            System.out.println("7.Деление чисел по модулю (остаток), результат сохранить");
            System.out.println("8.Возведение числа в модуль, результат сохранить");
            System.out.println("9.Возведение числа в степень, результат сохранить");
            System.out.println("10.Сохранить все данные (вышеполученные результаты) в Excel и вывести на экран.");
            String ans = scanner.nextLine();
            switch (ans){
                case "1":
                    ShowDB.showDB(scanner);
                    wait(scanner);
                    break;
                case "2":
                    CreateDB.createTable(scanner);
                    break;
                case "3":
                    //MathActions.addition(scanner);
                    break;
                case "4":
                    //MathActions.subtraction(scanner);
                    break;
                case "5":
                    //MathActions.multiplication(scanner);
                    break;
                case "6":
                    //MathActions.division(scanner);
                    break;
                case "7":
                    //MathActions.divisionModule(scanner);
                    break;
                case "8":
                    //MathActions.toModule(scanner);
                    break;
                case "9":
                    //MathActions.degree(scanner);
                    break;
                case "10":
                    //ExportDB.saveAndExport(scanner);
                    break;
                default:
                    System.out.println("Ошибка ввода!");
                    break;
            }
        }
    }

    private static void wait(Scanner scanner){
        System.out.println("Чтобы продолжить, нажмите любую кнопку");
        scanner.nextLine();
    }
}

