package org.example;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
    static char letter = 'r';

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите последовательность для кодирования: ");
        String sequence = scanner.nextLine();
        String fullSeq = addBits(sequence);
        int[][] matrix = getMatrix(fullSeq, countCheck(sequence));
        int[] checks = errorCorrection(matrix);
        printMatrix(matrix, checks);

        letter = 's';
        while (true) {
            System.out.println("""
                    Выберите действие:
                    1 - Ввести последовательность для проверки
                    2 - Выход
                    """);
            int input = Integer.parseInt(scanner.nextLine());
            if (input == 2)
                break;
            System.out.print("Введите последовательность для кодирования: ");
            String findSeq = scanner.nextLine();
            String fullFindSeq = addBits(findSeq, checks);
            int[][] matrixFind = getMatrix(fullFindSeq, countCheck(findSeq));
            int[] checksFind = errorCorrection(matrixFind);
            int error = getErrorIndex(checksFind);
            printMatrix(matrixFind, checksFind);

            String seq2 = correction(fullFindSeq, error);
            int[][] matrixFind2 = getMatrix(seq2, countCheck(findSeq));
            int[] checksFind2 = errorCorrection(matrixFind2);
            int error2 = getErrorIndex(checksFind2);
//            printMatrix(matrixFind2, checksFind2);
//            System.out.println(error2);
            if (error2 == 0)
                System.out.println("Ошибка произошла на " + error);
            else
                System.out.println("Ошибок больше, чем одна");
        }
        System.out.println("Возвращайтесь!");
    }

    private static String correction(String seq, int error) {
        char r = seq.charAt(error);
        StringBuilder s = null;
        if (r == '0') {
            s = new StringBuilder(seq);
            s.setCharAt(error, '1');
        } if (r == '1') {
            s = new StringBuilder(seq);
            s.setCharAt(error, '0');
        }
        assert s != null;
        return s.toString();
    }


    private static void printMatrix(int[][] array, int[] checks) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < array[0].length; i++) {
            str.append("\t").append(i + 1).append("\t");
        }
        str.append("\n");

        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                str.append("\t").append(array[i][j]).append("\t");
            }
            if (i != 0) {
                str.append(letter).append(i - 1).append("\t").append(checks[i - 1]);
            }
            str.append("\n");
        }
        System.out.println(str);
    }

    private static int[][] getMatrix(String seq, int numR) {
        int[][] array = new int[numR + 1][seq.length()];
        int k = 0;
        for (int i = 0; i < array[0].length; i++) {
            array[0][i] = Integer.parseInt(String.valueOf(seq.charAt(k++)));
            String str = String.format("%" + (array.length - 1) + "s", Integer.toBinaryString(i + 1)).replaceAll(" ", "0");
            str = new StringBuilder(str).reverse().toString();
            for (int j = 1; j < array.length; j++) {
                array[j][i] = Integer.parseInt(String.valueOf(str.charAt(j - 1)));
            }
        }
        return array;
    }

    private static int[] errorCorrection(int[][] array) {
        int[] checks = new int[array.length - 1];
        int k = 0;
        for (int i = 1; i < array.length; i++) {
            int ch = 0;
            for (int j = 0; j < array[0].length; j++) {
                if (array[i][j] == array[0][j] && array[0][j] == 1) {
                    ch++;
                }
            }
            checks[k++] = ch % 2;
        }
        return checks;
    }

    private static int getErrorIndex(int[] checks) {
        StringBuilder s = new StringBuilder();
        for (int i : checks)
            s.append(i);
//        System.out.println(Integer.parseInt(s.reverse().toString(), 2));
        return Integer.parseInt(s.reverse().toString(), 2);
    }



    private static String addBits(String seq) {
        int numR = countCheck(seq);
        int j = 0;
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < seq.length() + numR; i++) {
            if (IsPowerOfTwo(i + 1)) {
                s.append(0);
            } else {
                s.append(seq.charAt(j++));
            }
        }
        return s.toString();
    }

    private static int countCheck(String seq) {
        int numR = log2(seq.length());
        for (int i = seq.length(); i < seq.length() + numR; i++) {
            if (IsPowerOfTwo(i + 1)) {
                numR++;
                break;
            }
        }
        return numR;
    }

    private static String addBits(String seq, int[] checks) {
        int numR = countCheck(seq);
        int j = 0, k = 0;
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < seq.length() + numR; i++) {
            if (IsPowerOfTwo(i + 1)) {
                s.append(checks[k++]);
            } else {
                s.append(seq.charAt(j++));
            }
        }
        return s.toString();
    }

    private static int log2(int N) {
        return (int) Math.ceil(Math.log(N) / Math.log(2));
    }

    private static boolean IsPowerOfTwo(int x) {
        return (x & (x - 1)) == 0;
    }
}