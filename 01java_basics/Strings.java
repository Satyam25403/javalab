
import java.security.Key;
import java.util.Scanner;
import java.util.regex.*;

public class Strings {
    //components of a method:access specifier,return type,method name,parameter list


    //static is a class level property such that to use it we dont need to create any instance of the class
    //only static methods can call static methods


    //if this were not declared static main method call tho this method will produce error:
    //non static method cannot be referenced from a static method

    public static int avg(int a,int b){ //parameters
        return (a+b)/2;
    }
    //if it were not to be declared as static an intance of the class is needed to access the method



    public static void main(String[] args) {
        System.out.println("total"+avg(4,4));      //arguments
        System.out.println(Math.min(4,6));      //math class libraries have many predefined methods

        //string args: are the command line arguments we pass to main method:see how to enter commandline args
        for(String a:args){
            System.out.println(a);
        }

        //creating strings: new keyword and string literal
        char c[]={'n','a','m','e'};     //as array of charaters


        //METHOD 1:using new keyword
        String name = new String("karan");    //when string is declared as object using "new"
        //it is simply placed in heap memory same as all other objects when they are created
        //and not specifically in string constant pool 
        //this is not recommended as it creates seperate objects even for same string values

        name="anuj";
        //strings are immutable in java; their data cannot be changed once created

        //- Operations like +=, replace(), toUpperCase() return new strings.

        //just their reference can be made to point to some other string while the previous string remains the same
        //and garbage collector looks what to do with it
        name+="kumar";
        String nam=new String("Anuj");




        //METHOD 2: string literal
        //in general if two variables are created 
        //even if their values are same they point to different memory locations
        String name1="Anuj";        //but when it comes to strings in java both name1 and name2 
        String name2="Anuj";        //point to same memory location in the string constant pool in heap memory
        //this method of string declaration is more optimized since references point to same string




        // ✅ 3. Comparison
        // == → compares references
        // .equals() → compares values
        // .equalsIgnoreCase() → ignores case

        //METH 1:
        if(name1==name2){    //declared using literals:since values are same they point to same string
            System.out.println("both(references) are same");
        }

        if(nam==name1){    //references to objects declared by new is not the same as that using literal
            System.out.println("both(references) are same");
        }
        else{
            System.out.println("both(references) are not same");
        }
        //when == is used, references are comapred whether the references are same or not
        //not the actual string values

        //METH 2:
        //when equals() is used, the actual values of the string are compared
        if(name1.equals(name2)){
            System.out.println("both have same values");
        }
        if(nam.equals(name1)){
            System.out.println("both have same values");
        }
        else{
            System.out.println("both have different values");
        }//since all strings have same value, outputs will be both are same
        if(name1.equalsIgnoreCase(name2)){
            System.out.println("ignoring case, they are equal");
        }
        
        //NOTE:generalization is that comparision operator == checks REFERENCES in case of objects(created with help of new operator) ex:string
        //if references point to same address the condition becomes true
        //whereas for primitive data types, it checks their VALUES ex:int,char etc




        Scanner sc=new Scanner(System.in);
        System.out.print("enter first name:");
        String fName=sc.nextLine();         //takes line input including spaces into the string
        System.out.print("enter last name:");
        String lName=sc.next();             //takes first string until a space is encountered
        System.out.println(fName+lName);
        //use nextLine method in most cases and then process accordingly


        //METHODS:all these methods are not manipulating existing strings
        //they are creating new strings with desired specifications...to make changes in the original string, do  orgstring=orgstring.method(arg)
        // string + int/char/string/escape sequence characters like /n,/t etc
        // string is not immutable...like u cant do something like s.charAt(someindex)='a new character'...we cant change contents of string, a new string will/has to be created

        //.length(); .toString(); .concat(string)
        //.toUpperCase(), .toLowerCase(); .trim() remove initial or end spaces if present in string;
        //.startsWith(); .endsWith(); .equals(string) values of string are compared, == compares references; .equalsIgnoreCase(); .compareTo(string) compares strings lexographically(by ascii values) retruns difference between ascii values of first unmatched chars
        // if first string is greater in length and second(arg) is a substring of the first, compareTo returns difference in the number of characters
        //.charAt(index); .indexOf(char); .valueOf() static method to convert int double float etc to characters; replace();
        //.contains(string/substring); .substring(start[included], .end[excluded])...where is 2nd arg is not passed, it will take right limit to end of string; .split() return an array at specified delimiter; toCharArray();
        //.isEmpty()to chech whether a string is empty or not
        //.isBlank()return true if string only has whitespaces


        // ---------------------------------------------MOST IMPORTANT SECTION-------------------------------------------------------------------------
        //converting integer to string
        String sExample= Integer.toString(12345);      //or use String.valueOf(int)
        //converting String to integer
        int n=Integer.parseInt(sExample);                 //or use Integer.valueOf(string)
        //numeric character to its integer value
        char ch = '5';
        int num = ch - '0';   // Best way           before performing addn or subn on chars they are converted to integer first
        System.out.println(num);   // 5:difference in ascii values is stored
        //Integer digit to character digit
        int num1 = 5;
        char ch1 = (char)(num1 + '0');      //0 converted to ascii number, added with 5 and resultant ascii number is converted to its char representation
        System.out.println(ch1);   // '5'



        int age=123;
        String st=String.valueOf(age);      //convert to string
        System.out.println(st+2);           //appends 2

        String line="I love java and java is a good language";   
        String arr[]=line.split(" ");       //means to split at spaces; regx=regular expression
        for(String s:arr){
            System.out.println(s);
        }



        
        // 🔍 1. StringBuilder vs StringBuffer
        // Both are mutable classes used to manipulate strings without creating new objects.
        //StringBuilder: Mutable, not-threadsafe, faster(no sync overhead), for singlethreaded apps/interview codes
        //StringBuffer: Mutable, Thread-safe(synchronized), slower, used in multi-threaded environments
        StringBuilder sb = new StringBuilder("Hello");
        sb.append(" World");
        System.out.println(sb); // Hello World

        StringBuffer sb1 = new StringBuffer("Hello");
        sb1.append(" World");
        System.out.println(sb1); // Hello World

        //some methods that both contain: StringBuilder and StringBuffer, Both classes share the same API, so these methods apply to both
        //.append(), .insert(offset/index, string), .delete(start{inclusive}, end{exclusive}), .replace(start,end, replacementstring), 
        //.reverse(), .charAt(index), .setCharAt(index, char), .deleteCharAt(index), .substring(start,end)
        //.length():current no of chars, .capacity():no of chars it can accomodate:buffer size, .ensureCapacity(mincapacity): atleast these many no. of chars
        //.toString() convert to string type from stringbuilder type and can then apply methods available in strings
        // Prefer StringBuilder for interview questions for dealing with strings that need to be changed



        // 3. Pattern Matching with Pattern and Matcher
        // Java’s regex engine is built around Pattern and Matcher.
        // ✅ Example:
        Pattern p = Pattern.compile("\\d+"); // matches digits
        Matcher m = p.matcher("abc123xyz");

        while (m.find()) {
            System.out.println(m.group()); // prints 123
        }
        // 🔹 Key Methods:
        // - matcher.find() → finds next match
        // - matcher.group() → returns matched text
        // - matcher.start() / matcher.end() → match positions
        // Use Case: Validate formats (emails, phone numbers), extract tokens, search patterns.







    }

    //NOTE: all references are stored in stack and the actual objects created are stored in heap
    
}



// classic problems:

// 🔁 Core String Subproblems
// 1. Reverse a string
// - Use two-pointer swap or StringBuilder.reverse()
// - Interview variant: reverse words, reverse vowels
// 2. Check palindrome
// - Compare characters from both ends
// - Case-sensitive vs case-insensitive
// 3. Count characters / frequency
// - Use HashMap<Character, Integer>
// - Variants: first non-repeating, most frequent
// 4. Remove duplicates
// - Use Set or frequency array
// - Maintain order or not
// 5. Anagram check
// - Sort and compare OR use frequency array
// - Interview variant: group anagrams
// 6. Substring search
// - Use indexOf(), contains(), or sliding window
// - Interview variant: longest repeating substring
// 7. Split and tokenize
// - Use split(), regex, or Scanner
// - Variants: split by space, comma, custom delimiter
// 8. Convert case
// - toUpperCase(), toLowerCase()
// - ASCII trick: ch + 32 or ch - 32
// 9. Remove spaces / trim
// - trim(), replaceAll(" ", "")
// - Interview variant: normalize spacing
// 10. String to char array and vice versa
// char[] arr = str.toCharArray();
// String str = new String(arr);



// 🧠 Intermediate Patterns
// 11. Sliding Window
// - Longest substring without repeating characters
// - Max frequency substring of length k
// 12. Two Pointer Technique
// - Remove vowels, reverse words, merge strings
// 13. Prefix/Suffix Matching
// - startsWith(), endsWith()
// - Interview variant: longest common prefix
// 14. StringBuilder vs StringBuffer
// - Mutability, performance, thread safety
// 15. ASCII tricks
// - Convert digit char to int: '5' - '0'
// - Convert int to char: (char)(num + '0')

// 🚀 Advanced Interview Patterns
// 16. KMP Algorithm
// - Efficient substring search
// - Avoids rechecking characters
// 17. Z-Algorithm
// - Pattern matching and preprocessing
// 18. Rabin-Karp
// - Rolling hash for substring search
// 19. Trie (Prefix Tree)
// - Fast prefix lookup
// - Used in autocomplete, dictionary problems
// 20. Regex Matching
// - Validate email, phone, pattern
// - Extract digits, words, tokens

// 🔧 Utility Methods to Build
// - isPalindrome(String s)
// - reverseWords(String s)
// - removeDuplicates(String s)
// - charFrequency(String s)
// - areAnagrams(String a, String b)
// - longestUniqueSubstring(String s)
// - reverseString(String s)
// - countVowels(String s)
// - capitalizeWords(String s)
// - longestWord(String s)
// - frequencyMap(String s)




