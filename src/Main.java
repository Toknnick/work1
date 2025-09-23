import java.sql.SQLException;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws SQLException {
        FirstTask firstTask = new FirstTask();
        SecondTask secondTask = new SecondTask();
        ThirdTask thirdTask = new ThirdTask();
        FourthTask fourthTask = new FourthTask();
        Scanner scanner = new Scanner(System.in);
        boolean loop = true;

        while (loop) {
            System.out.println("Выберите действие:");
            System.out.println("1.Первая часть");
            System.out.println("2.Вторая часть");
            System.out.println("3.Третья часть");
            System.out.println("4.Четвертая часть");
            System.out.println("0.Выйти");
            String ans = scanner.nextLine();
            switch (ans){
                case "1":
                    firstTask.start(scanner);
                    break;
                case "2":
                    secondTask.start(scanner);
                    break;
                case "3":
                    thirdTask.start(scanner);
                    break;
                case "4":
                    fourthTask.start(scanner);
                    break;
                case "0":
                    loop = false;
                    break;
                default:
                    System.out.println("Ошибка!");
                    break;
            }
        }
    }
}