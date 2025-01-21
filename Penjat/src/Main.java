import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String fileName = "words.txt";

        boolean exit = false;
        int menuOption = 0;
        do {
            menuOption = menu();
            switch (menuOption) {
                case 1:
                    String[] words = getWords(fileName);
                    char[] wordArray = generateWord(words);
                    System.out.println("La palabra ha sido elegida!!");
                    game(wordArray);
                    break;
                case 2:
                    exit = true;
                    break;
                default:
                    break;
            }
        } while (!exit);
    }

    private static void game(char[] wordArray) {
        char[] userAnswersArray = new char[wordArray.length];
        int tries = 5;
        char[] triesChar = new char[tries + 1];
        char currentTry = '_';

        Arrays.fill(userAnswersArray, '_');
        Arrays.fill(triesChar, '_');

        boolean exit = false;
        do {
            System.out.println("Este es el estado de su palabra: ");
            dibujarAhorcado(5 - tries);
            for (int i = 0; i < userAnswersArray.length; i++) {
                System.out.printf("%1s", userAnswersArray[i]);
            }
            System.out.println();
            System.out.print("Letras falladas: ");
            for (int i = 0; i < triesChar.length; i++) {
                if (triesChar[i] != '_') {
                    System.out.printf(triesChar[i] + " ");
                }
            }
            System.out.println();
            System.out.println("Intentos restantes: " + (tries + 1));

            currentTry = inputCharacterTry(triesChar, userAnswersArray);

            tries = searchLetter(wordArray, triesChar,userAnswersArray, tries, currentTry);

            exit = checkIfWon(userAnswersArray);

            exit = endGameScreen(userAnswersArray, exit, tries);
        } while (!exit);
    }

    private static char inputCharacterTry(char[] triesChar, char [] userAnswersArray) {
        Scanner sc = new Scanner(System.in);
        char currentTry = '_';
        boolean repeatedTry = false;

        do {
            repeatedTry = false;
            boolean invalidInputCaracter = false;
            do {
                try {
                    System.out.print("Introduce una letra (si introduces mas de una se usara la primera):");
                    currentTry = sc.next().toLowerCase().charAt(0);
                    invalidInputCaracter = false;
                } catch (InputMismatchException e) {
                    System.out.println("PLEASE ENTER A VALID INPUT!");
                    invalidInputCaracter = true;
                };
            } while (invalidInputCaracter);

            for (char c : triesChar) {
                if (c == currentTry) {
                    System.out.println("Ya has probado esta letra!");
                    repeatedTry = true;
                }
            }
            for (char c : userAnswersArray) {
                if (c == currentTry) {
                    System.out.println("Ya has probado esta letra!");
                    repeatedTry = true;
                }
            }
        } while (repeatedTry);

        return currentTry;
    }

    private static boolean endGameScreen(char[] userAnswersArray, boolean exit, int tries) {
        if (exit) {
            System.out.println();
            System.out.println();
            System.out.println("--------------PALABRA ENCONTRADA!!!!!--------------");
            for (int i = 0; i < userAnswersArray.length; i++) {
                System.out.printf("%1s", userAnswersArray[i]);
            }
            System.out.println();
            System.out.println();
            System.out.println();
        } else {System.out.println("------------------------------------");}
        if (tries < 0) {
            System.out.println("Has perdido!!!!!!!!!");
            dibujarAhorcado(5 - tries);
            exit = true;
        }

        return exit;
    }

    private static boolean checkIfWon(char[] userAnswersArray) {
        boolean exit = true;
        for (char c : userAnswersArray) {
            if (c == '_') {
                exit = false;
            }
        }
        return exit;
    }

    private static int searchLetter(char[] wordArray, char[] triesChar, char [] userAnswersArray, int tries, char currentTry) {
        boolean found = false;
        for (int i = 0; i < wordArray.length; i++) {
            if (currentTry == wordArray[i]) {
                System.out.println("Letra Encontrada!!!!!!");
                found = true;
                wordArray[i] = '_';
                userAnswersArray[i] = currentTry;
            }
        }

        if (!found) {
            System.out.println("La palabra NO contiene esta letra!");
            triesChar[5 - tries] = currentTry;
            tries--;
        }

        return tries;
    }

    private static int menu() {
        Scanner sc = new Scanner(System.in);
        int menuOption = 0;
        System.out.println("=======================================");
        System.out.println("  ¡Bienvenido al juego de El Penjat!");
        System.out.println("=======================================");
        System.out.println("Reglas del juego:");
        System.out.println("1. Se elegirá una palabra secreta.");
        System.out.println("2. Tendrás que adivinar la palabra letra por letra.");
        System.out.println("3. Tienes 6 intentos.");
        System.out.println("4. Si adivinas una letra que no está en la palabra, perderás un intento.");
        System.out.println("5. Si adivinas la palabra antes de quedarte sin intentos, ¡Ganas!");
        System.out.println("6. Si se te acaban los intentos, ¡Perderás!");
        System.out.println();

        boolean invalidInput = false;
        do {
            System.out.println("Elije una opcion (1/2): ");
            System.out.println("1 - JUGAR");
            System.out.println("2 - SALIR");

            try {
                menuOption = sc.nextInt();
                sc.nextLine();
                invalidInput = false;
            } catch (InputMismatchException e) {
                System.out.println("Introduzaca un valor valido!!!!");
                sc.nextLine();
                invalidInput = true;
            }

            if (menuOption != 1 && menuOption != 2 && !invalidInput) {
                System.out.println("Introduzaca una opción valido (1/2)!!");
                invalidInput = true;
            }
        } while (invalidInput);


        return menuOption;
    }

    private static String[] getWords(String fileName) {
        String[] words = {};

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String data;
            data = reader.readLine();
            words = data.split(", ");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return words;
    }

    public static char[] generateWord(String[] words) {
        Random random = new Random();
        String word = words[random.nextInt(words.length)];
        return word.toCharArray();
    }

    public static void dibujarAhorcado(int intentosFallidos) {
        System.out.println(" +---+");
        System.out.println(" |   |");

        if (intentosFallidos >= 1) {
            System.out.println(" O   |"); // Cabeza
        } else {
            System.out.println("     |");
        }

        if (intentosFallidos >= 2) {
            if (intentosFallidos == 2) {
                System.out.println(" |   |"); // Tronco
            } else if (intentosFallidos == 3) {
                System.out.println("/|   |"); // Un brazo
            } else {
                System.out.println("/|\\  |"); // Dos brazos
            }
        } else {
            System.out.println("     |");
        }

        if (intentosFallidos >= 5) {
            if (intentosFallidos == 5) {
                System.out.println("/    |"); // Una pierna
            } else {
                System.out.println("/ \\  |"); // Dos piernas
            }
        } else {
            System.out.println("     |");
        }

        System.out.println("     |");
        System.out.println("=====");
    }
}