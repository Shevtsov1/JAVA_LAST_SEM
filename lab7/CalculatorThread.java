import java.math.BigInteger;

/**
 * Класс CalculatorThread представляет поток для вычисления факториала чисел в заданном диапазоне.
 */
public class CalculatorThread extends Thread {
    private BigInteger start;
    private BigInteger end;
    private BigInteger result;

    /**
     * Конструктор класса CalculatorThread.
     *
     * @param start начальное значение для вычисления факториала
     * @param end   конечное значение для вычисления факториала
     */
    public CalculatorThread(BigInteger start, BigInteger end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public void run() {
        result = calculateFactorial();
    }

    /**
     * Вычисляет факториал чисел от start до end (включительно).
     *
     * @return результат вычисления факториала
     */
    private BigInteger calculateFactorial() {
        BigInteger factorial = BigInteger.ONE;

        for (BigInteger i = start; i.compareTo(end) <= 0; i = i.add(BigInteger.valueOf(2))) {
            factorial = factorial.multiply(i);
        }

        return factorial;
    }

    /**
     * Возвращает результат вычисления факториала.
     *
     * @return результат вычисления факториала
     */
    public BigInteger getResult() {
        return result;
    }
}