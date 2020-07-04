package utils;

import java.util.Scanner;

public final class ScannerSingleton {

    private static volatile ScannerSingleton instance;
    private Scanner scanner;

    private ScannerSingleton(Scanner scanner) {
        this.scanner = scanner;
    }

    public static ScannerSingleton getInstance() {
        ScannerSingleton localInstance = instance;
        if (localInstance == null) {
            synchronized (ScannerSingleton.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new ScannerSingleton(new Scanner(System.in));
                }
            }
        }
        return localInstance;
    }

    public Scanner getScanner() {
        return scanner;
    }
}
