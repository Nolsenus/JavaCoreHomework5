import java.io.*;
import java.util.Scanner;

public class Main {

    public static final int FIELD_LENGTH = 9;
    public static final int SIZE_X = 3;

    public static void main(String[] args) {
        task1Showcase();
        task2Showcase();
    }

    public static void task1Showcase() {
        System.out.print("Enter the path to directory for backup: ");
        Scanner in = new Scanner(System.in);
        String path = in.next();
        createBackup(path);
    }

    public static void task2Showcase() {
        int[] field = new int[]{1, 2, 1, 2, 1, 2, 1, 2, 1};
        System.out.println("Starting field:");
        showField(field);
        int stateFromField = stateFromField(field);
        saveState(stateFromField);
        int stateFromFile = loadState();
        System.out.println("Field loaded from file:");
        showField(fieldFromState(stateFromFile));
    }

    public static void createBackup(String pathToDir) {
        File directory = new File(pathToDir);
        File[] files = directory.listFiles();
        if (files == null) {
            System.out.println("No files found.");
            return;
        }
        String pathToBackup = pathToDir + "/backup";
        new File(pathToBackup).mkdir();
        File copy;
        byte[] buffer = new byte[1024];
        int bytesRead;
        for (File file : files) {
            if (file.isFile()) {
                copy = new File(pathToBackup + "/" + file.getName());
                try (FileInputStream fis = new FileInputStream(file);
                     FileOutputStream fos = new FileOutputStream(copy, false);
                ) {
                    while ((bytesRead = fis.read(buffer)) > 0) {
                        fos.write(buffer, 0, bytesRead);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Backup successful.");
    }

    public static int[] fieldFromState(int state) {
        int[] field = new int[FIELD_LENGTH];
        for (int i = 0; i < FIELD_LENGTH; i++) {
            field[i] = state % 4;
            state /= 4;
        }
        return field;
    }

    public static int stateFromField(int[] field) {
        int state = 0;
        int multiplier = 1;
        for (int i = 0; i < FIELD_LENGTH; i++) {
            state += multiplier * field[i];
            multiplier *= 4;
        }
        return state;
    }

    public static void saveState(int state) {
        File save = new File("save.txt");
        try (FileWriter fw = new FileWriter(save, false)) {
            fw.write(Integer.toString(state));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int loadState() {
        File save = new File("save.txt");
        try (Scanner input = new Scanner(save)) {
            return input.nextInt();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void showField(int[] field) {
        for (int i = 0; i < FIELD_LENGTH; i++) {
            switch (field[i]) {
                case 0:
                    System.out.print(' ');
                    break;
                case 1:
                    System.out.print('X');
                    break;
                case 2:
                    System.out.print('O');
                    break;
                case 3:
                    System.out.print('R');
                    break;
                default:
                    System.out.print('?');
            }
            if (i % SIZE_X == 2) {
                System.out.println();
            }
        }
    }
}
