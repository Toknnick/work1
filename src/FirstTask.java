import java.sql.SQLException;
import java.util.Scanner;

public class FirstTask {
    public void start(Scanner scanner) throws SQLException {
        MathActions mathActions = new MathActions();
        ExportDB exportDB = new ExportDB();
        boolean loop = true;
        while (loop) {
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
            System.out.println("0.Назад");
            String ans = scanner.nextLine();
            switch (ans){
                case "1":
                    ShowDB.showDB(scanner);
                    break;
                case "2":
                    CreateDB.createTable(scanner);
                    break;
                case "3":
                    mathActions.addition(scanner);
                    break;
                case "4":
                    mathActions.subtraction(scanner);
                    break;
                case "5":
                    mathActions.multiplication(scanner);
                    break;
                case "6":
                    mathActions.division(scanner);
                    break;
                case "7":
                    mathActions.divisionModule(scanner);
                    break;
                case "8":
                    mathActions.toModule(scanner);
                    break;
                case "9":
                    mathActions.toDegree(scanner);
                    break;
                case "10":
                    exportDB.saveAndExportTask1();
                    break;
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

    private void wait(Scanner scanner){
        System.out.println("\nЧтобы продолжить, нажмите Enter\n");
        scanner.nextLine();
    }
}

