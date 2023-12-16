import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс ThreadGenerator представляет генератор потоков для вычисления факториала числа.
 */
public class ThreadGenerator {
    private int threadCount;
    private BigInteger number;
    private BigInteger result;
    private List<CalculatorThread> threads;

    /**
     * Конструктор класса ThreadGenerator.
     *
     * @param threadCount количество потоков для генерации
     * @param number      число, для которого генерируются потоки
     */
    public ThreadGenerator(int threadCount, BigInteger number) {
        this.threadCount = threadCount;
        this.number = number;
        this.threads = new ArrayList<>();
    }

    /**
     * Выполняет генерацию и запуск потоков.
     */
    public void execute() {
        BigInteger chunkSize = number.divide(BigInteger.valueOf(threadCount));
        BigInteger remainder = number.remainder(BigInteger.valueOf(threadCount));

        BigInteger start = BigInteger.ONE;
        BigInteger end = chunkSize;

        for (int i = 0; i < threadCount; i++) {
            if (i == threadCount - 1) {
                // Добавляем остаток к последнему потоку
                end = end.add(remainder);
            }

            CalculatorThread thread = new CalculatorThread(start, end);
            threads.add(thread);
            thread.start();

            start = end.add(BigInteger.ONE);
            end = end.add(chunkSize);
        }

        // Ждем завершения всех потоков
        for (CalculatorThread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Возвращает результат вычисления факториала, полученный от всех потоков.
     *
     * @return результат вычисления факториала
     */
    public BigInteger getResult() {
        result = BigInteger.ONE;

        for (CalculatorThread thread : threads) {
            result = result.multiply(thread.getResult());
        }

        return result;
    }
}