
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        System.out.println("Enter the path to the file INPUT.txt");
        Scanner scanner = new Scanner(System.in);
        String path = scanner.nextLine();

        Game game = new Game(path);
        game.run();
    }
}
