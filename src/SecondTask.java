import java.sql.SQLException;
import java.util.Scanner;

public class SecondTask extends Task{

    public void start(Scanner scanner) throws SQLException {
        taskNumber = 2;

        StringActions stringActions = new StringActions(this);
        ExportDB exportDB = new ExportDB(this);
        CreateDB createDB = new CreateDB(this);
        ShowerDB showerDB = new ShowerDB(this);

        boolean loop = true;
        while (loop) {
            System.out.println("Выберите действие:");
            System.out.println("""
                    1. Вывести таблицы.
                    2. Создать таблицу\s
                    3. Ввести две строки с клавиатуры
                    4. Подсчитать размер ранее введенных строк
                    5. Объединить две строки в единое целое
                    6. Сравнить две ранее введенные строки
                    7. Сохранить все данные в Excel и вывести на экран.
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
                    stringActions.getStrings(scanner,true);
                    break;
                case "4":
                    stringActions.getLenOfStrings(scanner);
                    break;
                case "5":
                    stringActions.addLines(scanner);
                    break;
                case "6":
                    stringActions.compare(scanner);
                    break;
                case "7":
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
        System.out.println("Чтобы продолжить, нажмите Enter\n");
        scanner.nextLine();
    }
}
