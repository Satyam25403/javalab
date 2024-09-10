import java.util.Scanner;

public class SmallestLetterGreaterThanTarget {
    public static void main(String[] args){
        int n;
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the number of elements in the array");
        n = sc.nextInt();
        char[] a = new char[n];
        System.out.println("Enter the target letter");
        char tar = sc.next().charAt(0);
        System.out.println("Enter array:");
        for(int i = 0; i < n; i++){
            a[i] = sc.next().charAt(0);
        }
        
        char result = findSmallestLetterGreaterThanTarget(a, tar);

        if (result != '\0') {
            System.out.println("The smallest letter greater than " + tar + " is " + result);
        } else {
            System.out.println("No letter greater than " + tar + " found.");
        }
    }

    public static char findSmallestLetterGreaterThanTarget(char[] a, char tar) {
        char smallest = '\0'; // Initialize with null character
        for (char c : a) {
            if (c > tar) {
                if (smallest == '\0' || c < smallest) {
                    smallest = c;
                }
            }
        }
        return smallest;
    }
}

