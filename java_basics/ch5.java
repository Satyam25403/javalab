//methods, strings
import java.util.Scanner;
public class ch5 {
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

        //string args are the command line arguments we pass to main method:see how to enter commandline args
        for(String a:args){
            System.out.println(a);
        }
        //creating strings: new keyword and string literal
        char c[]={'n','a','m','e'};     //as array of charaters


        //METHOD 1:using new keyword
        String name=new String("karan");    //when string is declared as object using "new"
        //it is simply placed in heap memory same as all other objects when they are created
        //and not specifically in string constant pool 
        //this is not recommended as it creates seperate objects even for same string values

        name="anuj";
        //strings are immutable in java; their data cannot be changed once created
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



        //comparision:using == and equals()
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
        //not the actual string values are compared

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
        
        //NOTE:generalization is that comparision operator == checks REFERENCES in case of objects ex:string
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
        //they are creating new strings with desired specifications

        //toUpperCase();toLowerCase();trim()remove initial or end spaces if present in string;
        //startsWith();endsWith();equals();equalsIgnoreCase()
        //charAt();valueOf()to convert int double float etc to characters;replace();
        //contains();substring(start[included],end[excluded]);split() return an array;toCharArray();
        //isEmpty()to chech whether a string is empty or not
        //isBlank()return true if string only has whitespaces

        int age=123;
        String st=String.valueOf(age);      //convert to string
        System.out.println(st+2);           //appends 2

        String line="I love java and java is a good language";   
        String arr[]=line.split(" ");       //means to split at spaces; regx=regular expression
        for(String s:arr){
            System.out.println(s);
        }

    }

    //NOTE: all references are stored in stack and the actual objects created are stored in heap
    
}
