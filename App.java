import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        int choice = 0;
        
        while(choice != -1) {
            System.out.println("Option 1: make an array");
            System.out.println("Option 2: Sort existing array");
            System.out.println("to quit type -1");
            choice = scanner.nextInt();
            
            if(choice == 1) {
                System.out.print("Enter array length: ");
                int length = scanner.nextInt();
                scanner.nextLine();

                System.out.println("What would you like your array to be called?");
                String filename = scanner.nextLine();

                Integer[] array = createRandomArray(length);
                writeArrayToFile(array, filename + ".txt");
                System.out.println("Your array was saved as " + filename + ".txt");
            }
            else if(choice == 2) {
                System.out.print("Enter name of file you want to sort: ");
                scanner.nextLine();
                String fileToSort = scanner.nextLine();
                
                Integer[] array = readFileToArray(fileToSort);
                String baseName = fileToSort.replace(".txt", " ");

                Integer[] bubbleArray = array.clone();
                bubbleSort(bubbleArray);
                writeArrayToFile(bubbleArray, baseName + "bubble.txt");

                Integer[] mergeArray = array.clone();
                mergeSort(mergeArray, Integer.class);  // Pass Integer.class
                writeArrayToFile(mergeArray, baseName + "merge.txt");

                System.out.println("The arrays have been sorted and saved as: ");
                System.out.println(baseName + "bubble.txt (for bubble sort)");
                System.out.println(baseName + "merge.txt (for merge sort)");
            }
            else if (choice != -1) {
                System.out.println("Invalid option try again.");
            }
        }
        
        scanner.close();
        System.out.println("Goodbye");
    }

    public static Integer[] createRandomArray(int arrayLength) {
        Integer[] array = new Integer[arrayLength];
        Random random = new Random();

        for(int i = 0; i < arrayLength; i++) {
            array[i] = random.nextInt(101);
        }
        return array;
    }

    public static <T> void writeArrayToFile(T[] array, String filename) throws IOException {
        try(PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for(T num : array) {
                writer.println(num);
            }
        }
    }

    public static Integer[] readFileToArray(String filename) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filename));
        List<Integer> list = new ArrayList<>();
        while(scanner.hasNextInt()) {
            list.add(scanner.nextInt());
        }
        scanner.close();
        return list.toArray(new Integer[0]);
    }

    public static <T extends Comparable<T>> void bubbleSort(T[] array) {
        boolean swap = true;
        while(swap) {
            swap = false;
            for(int i = 0; i < array.length - 1; i++) {
                if(array[i].compareTo(array[i + 1]) > 0) {
                    swap = true;
                    T temp = array[i];
                    array[i] = array[i + 1];
                    array[i + 1] = temp;
                }
            }
        }
    }

    public static <T extends Comparable<T>> void mergeSort(T[] array, Class<T> clazz) {
        int arrayLength = array.length;
        if(arrayLength < 2) {
            return;
        }

        int midPoint = arrayLength / 2;
        @SuppressWarnings("unchecked")
        T[] left = (T[]) Array.newInstance(clazz, midPoint);
        @SuppressWarnings("unchecked")
        T[] right = (T[]) Array.newInstance(clazz, arrayLength - midPoint);

        System.arraycopy(array, 0, left, 0, midPoint);
        System.arraycopy(array, midPoint, right, 0, arrayLength - midPoint);

        mergeSort(left, clazz);
        mergeSort(right, clazz);
        merge(array, left, right);
    }

    private static <T extends Comparable<T>> void merge(T[] array, T[] left, T[] right) {
        int leftSize = left.length;
        int rightSize = right.length;

        int a = 0;
        int b = 0;
        int c = 0;

        while(a < leftSize && b < rightSize) {
            if(left[a].compareTo(right[b]) <= 0) {
                array[c] = left[a];
                a++;
            }
            else {
                array[c] = right[b];
                b++;
            }
            c++;
        }

        while(a < leftSize) {
            array[c] = left[a];
            a++;
            c++;
        }

        while(b < rightSize) {
            array[c] = right[b];
            b++;
            c++;
        }
    }
}