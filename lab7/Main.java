import java.math.BigInteger;
import java.util.Scanner;

/*

    Класс Main является точкой входа в программу.
    Он предлагает пользователю ввести число и количество потоков,
    а затем создает объект ThreadGenerator для генерации и выполнения потоков.
    После выполнения потоков, полученный факториал выводится на экран.
*/
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("Введите число (а): ");
            BigInteger number = scanner.nextBigInteger();

            System.out.print("Введите количество потоков: ");
            int threadCount = scanner.nextInt();

            ThreadGenerator generator = new ThreadGenerator(threadCount, number);
            generator.execute();
            BigInteger result = generator.getResult();

            System.out.println("Факториал a!!: " + result);
        } finally {
            scanner.close();
        }
    }
}
