package bullscows;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Random random = new Random();
        Scanner scanner = new Scanner(System.in);
        int arrayLength = 0;
        int possibleOptions = 0;
        while (true) {
            System.out.println("Input the length of the secret code:");
            String inputUser1 = scanner.next();
            if (!isNumeric(inputUser1)) {
                System.out.printf("Error: %s isn't a valid number.\n", inputUser1);
                System.exit(0);
            }
            int inputUser = Integer.parseInt(inputUser1);
            if (inputUser == 0) {
                System.out.printf("Error: %s isn't a valid number.\n", inputUser);
                System.exit(0);
            }
            System.out.println("Input the number of possible symbols in the code:");
            int possibleOptions1 = scanner.nextInt();
            int errorCodes = errorOptions(inputUser, possibleOptions1);
            if (errorCodes == 1) {
                System.out.printf("Error: it's not possible to generate a code with a length of %d with %d unique symbols.", inputUser, possibleOptions1);
                System.exit(0);
            } else if (errorCodes == 2) {
                System.out.println("Error: maximum number of possible symbols in the code is 36 (0-9, a-z).");
                System.exit(0);
            } else if (errorCodes == 0) {
                arrayLength = inputUser;
                possibleOptions = possibleOptions1;
                break;
            }
        }
        outputHiddenCode(arrayLength, possibleOptions);
        int[] readySequence = generateRandomNum(arrayLength, possibleOptions, random);
        System.out.println("Okay, let's start a game!");

        int turn = 1;
        while (true) {
            System.out.println("Turn " + turn);
            int[] userInput = generateArrayUser(scanner.next());
            int bulls = 0;
            int cows = 0;
            String[] arrayBulls = new String[readySequence.length];
            for (int i = 0; i < readySequence.length; i++) {
                if (readySequence[i] == userInput[i]) {
                    arrayBulls[i] = "bull";
                    bulls++;
                }
            }
            for (int i = 0; i < readySequence.length; i++) {
                for (int j = 0; j < readySequence.length; j++) {
                    if (readySequence[i] == userInput[j] && i != j) {
                        cows++;
                    }
                }
            }
            if (bulls > 0 && cows > 0) {
                System.out.printf("Grade: %d %s and %d %s\n", bulls, bulls == 1 ? "bull" : "bulls", cows, cows == 1 ? "cow" : "cows");
            } else if (bulls > 0 && cows == 0) {
                System.out.printf("Grade: %d %s\n", bulls, bulls == 1 ? "bull" : "bulls");
            } else if (bulls == 0 && cows > 0) {
                System.out.printf("Grade: %d %s\n", cows, cows == 1 ? "cow" : "cows");
            } else {
                System.out.println("Grade: None");
            }
            if (bulls == arrayBulls.length) {
                System.out.println("Congratulations! You guessed the secret code.");
                break;
            }
            turn++;
        }
    }

    public static int[] generateArrayUser(String userInput) {
        char[] userInputChar = userInput.toCharArray();
        int[] arrayNumber = new int[userInputChar.length];
        for (int i = 0; i < userInput.length(); i++) {
            arrayNumber[i] = userInputChar[i];
        }
        return arrayNumber;
    }

    public static int[] generateRandomNum(int arrayLength, int possibleOptions, Random random) {
        int[] sequence = new int[arrayLength];
        int bound = possibleOptions - 10;
        for (int i = 0; i < sequence.length; i++) {
            while (true) {
                int digitOrLetter = 0;
                if (possibleOptions > 10) {
                    digitOrLetter = random.nextInt(2);
                }
                boolean duplicate = false;
                int temp = 0;
                if (digitOrLetter == 0) {
                    if (possibleOptions <= 10) {
                        temp = 48 + random.nextInt(possibleOptions);
                    } else {
                        temp = 48 + random.nextInt(10);
                    }
                } else {
                    temp = 97 + random.nextInt(bound);
                }
                for (int k : sequence) {
                    if (k == temp) {
                        duplicate = true;
                        break;
                    }
                }
                if (!duplicate) {
                    sequence[i] = temp;
                    break;
                }
            }
        }
        return sequence;
    }

    public static void outputHiddenCode(int arrayLength, int possibleOptions) {
        String digits = "";
        if (possibleOptions <= 10) {
            digits = String.format("0-%d", possibleOptions - 1);
        } else {
            digits = "0-9";
        }
        String letters = "";
        if (possibleOptions == 11) {
            letters = "a";
        } else if (possibleOptions > 11) {
            letters = String.format("a-%s", (char) (possibleOptions - 10 + 96));
        }
        char[] starArray = new char[arrayLength];
        Arrays.fill(starArray, '*');
        String star = String.valueOf(starArray);
        String output = "";
        if (possibleOptions <= 10) {
            output = String.format("The secret is prepared: %s (%s).", star, digits);
        } else {
            output = String.format("The secret is prepared: %s (%s, %s).", star, digits, letters);
        }
        System.out.println(output);
    }

    public static int errorOptions(int inputUser, int possibleOptions) {
        if (inputUser > possibleOptions) {
            return 1;
        }
        if (possibleOptions > 36) {
            return 2;
        }
        return 0;
    }

    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
}