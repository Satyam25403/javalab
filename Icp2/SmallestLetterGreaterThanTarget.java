import java.util.Scanner;

public class SmallestLetterGreaterThanTarget {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the number of elements in the array");
        int n= sc.nextInt();
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
            System.out.println("No such letter found.");
        }
    }

    public static char findSmallestLetterGreaterThanTarget(char[] a, char tar) {
        char smallest = a[0],result='\0'; //Assume first letter to be smallest initially and  result to hold if a greater letter is found
        for (char c : a) {
            if (c > tar) {
                if (result == '\0' || c < result) {
                    //if smallest has not been initialized yet or we find a smaller character, update
                    result = c;
                }
            }
            //simultaneously keep treack of smallest letter
            if(c<smallest){
                smallest=c;
            }
        }
        return result=='\0'?smallest:result;
    }
}

