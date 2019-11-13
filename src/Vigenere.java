import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Vigenere {
    private static int CHAR_LENGTH = 26;
    private static char[][] chars = new char[CHAR_LENGTH][CHAR_LENGTH];

    public static void main(String[] args) throws Exception {
        createCharsArray();
        if (args.length > 0) {
            switch (args[0]) {
                case "-p":
                    createPlainText();
                    break;
                case "-e":
                    cryptoPlainText();
                    break;
                case "-d":
                    decryptText();
                    break;
            }
        } else {
            System.out.println("Choose one of the: '-p -e -d -k' options!");
        }
    }

    private static void createPlainText() throws Exception {
        String origInput = loadTextFile("orig.txt");
        char[] origInputArray = origInput.toCharArray();
        String letterInRow;
        StringBuilder input = new StringBuilder();
        for (char c : origInputArray) {
            letterInRow = String.valueOf(c);
            if (letterInRow.matches("[a-z]")) {
                input.append(c);
            }
        }
        System.out.println(input);
        createTextFile("plain.txt", input.toString());
    }

    private static void cryptoPlainText() throws Exception {
        String input = loadTextFile("plain.txt");
        char[] inputArray = input.toCharArray();
        String key = loadTextFile("key.txt");
        char[] keyArray = key.toCharArray();
        StringBuilder output = new StringBuilder();
        for (int x = 0; x < inputArray.length; x++) {
            int inputChar = (int) inputArray[x] - 'a';
            int keyChar;
            if (x < keyArray.length) {
                keyChar = (int) keyArray[x] - 'a';
            } else {
                keyChar = (int) keyArray[x % keyArray.length] - 'a';
            }
            output.append(chars[inputChar][keyChar]);
        }
        System.out.println(output);
        createTextFile("crypto.txt", output.toString());
    }

    private static void decryptText() throws Exception {
        String output = loadTextFile("crypto.txt");
        char[] outputArray = output.toCharArray();
        String key = loadTextFile("key.txt");
        char[] keyArray = key.toCharArray();
        StringBuilder decrypted = new StringBuilder();
        for (int x = 0; x < outputArray.length; x++) {
            int keyChar = getKeyChar(outputArray[x], keyArray, x);
            for (int i = 0; i < 26; i++) {
                if (chars[keyChar][i] == outputArray[x]) {
                    decrypted.append(chars[0][i]);
                }
            }
        }
        System.out.println(decrypted);
        createTextFile("decrypt.txt", decrypted.toString());
    }

    private static int getKeyChar(char c, char[] keyArray, int x) {
        int outputChar = (int) c - 'a';
        int keyChar;
        if (x < keyArray.length) {
            keyChar = (int) keyArray[x] - 'a';
        } else {
            keyChar = (int) keyArray[x % keyArray.length] - 'a';
        }
        return keyChar;
    }

    private static void createCharsArray() {
        for (int i = 0; i < 26; i++) {
            for (int z = 0; z < 26; z++) {
                int shift = z + i;
                if (shift >= 26) {
                    shift = shift % 26;
                }
                chars[i][z] = (char) (shift + 'a');
            }
        }
    }

    private static String loadTextFile(String fileName) throws Exception {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }

    private static void createTextFile(String fileName, String input) throws Exception {
        File file = new File(fileName);
        FileWriter writer = new FileWriter(file);
        writer.write(input);
        writer.close();
    }
}
