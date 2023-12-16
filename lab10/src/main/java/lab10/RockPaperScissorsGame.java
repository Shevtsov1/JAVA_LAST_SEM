package lab10;

import java.io.*;
import java.net.*;

public class RockPaperScissorsGame {
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try {
            // Запуск сервера в отдельном потоке
            Thread serverThread = new Thread(() -> runServer());
            serverThread.start();

            // Подключение клиента
            Socket socket = new Socket("localhost", PORT);

            // Получение потоков ввода/вывода для обмена данными с сервером
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();

            // Создание объектов для чтения/записи данных
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            PrintWriter writer = new PrintWriter(outputStream, true);

            // Логика игры
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            String userInput;
            while ((userInput = consoleReader.readLine()) != null) {
                writer.println(userInput);  // Отправка выбора пользователя на сервер

                String serverResponse = reader.readLine();  // Получение ответа от сервера
                System.out.println("Server: " + serverResponse);

                if (serverResponse.equals("You win!") || serverResponse.equals("You lose!") || serverResponse.equals("It's a tie!")) {
                    break;  // Игра завершена
                }
            }

            // Закрытие ресурсов
            writer.close();
            reader.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void runServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server started. Waiting for a client...");

            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected.");
            System.out.print("Input your choice [rock, paper, scissors]: ");

            // Получение потоков ввода/вывода для обмена данными с клиентом
            InputStream inputStream = clientSocket.getInputStream();
            OutputStream outputStream = clientSocket.getOutputStream();

            // Создание объектов для чтения/записи данных
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            PrintWriter writer = new PrintWriter(outputStream, true);

            // Логика игры
            String[] choices = {"rock", "paper", "scissors"};
            while (true) {
                int randomIndex = (int) (Math.random() * choices.length);
                String serverChoice = choices[randomIndex];

                String clientChoice = reader.readLine();  // Получение выбора клиента

                if (clientChoice == null) {
                    break;  // Клиент отключился
                }

                System.out.println("Client choice: " + clientChoice);
                System.out.println("Server choice: " + serverChoice);

                String result;
                if (clientChoice.equals(serverChoice)) {
                    result = "It's a tie!";
                } else if (
                        (clientChoice.equals("rock") && serverChoice.equals("scissors")) ||
                        (clientChoice.equals("paper") && serverChoice.equals("rock")) ||
                        (clientChoice.equals("scissors") && serverChoice.equals("paper"))
                ) {
                    result = "You win!";
                } else {
                    result = "You lose!";
                }

                writer.println(result);  // Отправка результата клиенту
            }

            // Закрытие ресурсов
            writer.close();
            reader.close();
            clientSocket.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
