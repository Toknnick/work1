import java.sql.SQLException;
import java.util.Scanner;

public class FourthTask extends Task{

    public void start(Scanner scanner) throws SQLException {
        taskNumber = 4;

        StringActions stringActions = new StringActions(this);
        ExportDB exportDB = new ExportDB(this);
        CreateDB createDB = new CreateDB(this);
        ShowerDB showerDB = new ShowerDB(this);

        boolean loop = true;
        while (loop) {
            System.out.println("Выберите действие:");
            System.out.println("""
                    1. Вывести таблицы
                    2. Создать таблицу
                    3. Возвращение подстроки по индексам.
                    4. Перевод строк в верхний и нижний регистры
                    5. Поиск подстроки и определение окончания подстроки
                    6. Сохранить все данные в Excel
                    
                    0.Назад""");
            String ans = scanner.nextLine();

            switch (ans){
                case "1":
                    showerDB.show(scanner);
                    break;
                case "2":
                    createDB.createTableForTask2And4(scanner);
                    break;
                case "3":
                    stringActions.returnStrByIndex(scanner);
                    break;
                case "4":
                    stringActions.stringToRegister(scanner);
                    break;
                case "5":
                    stringActions.findEndOfString(scanner);
                    break;
                case "6":
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

    private void wait(Scanner scanner){
        System.out.println("\nЧтобы продолжить, нажмите Enter\n");
        scanner.nextLine();
    }
}
