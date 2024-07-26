//arrays in java
public class ch4 {
    public static void main(String[] args) {
        
        int intArray[];         //array declaration
        intArray=new int[20];       //allocating memory:to do both in same line int intArray[]=new int[20]
        //uninitialized locations are set to 0 if only memory is allocated
        //if a[0]=3 and a[1]=4 and a[2]not initialized ,a[2]=0


        int marks[]={98,23,45,2,12};        //declararion with initialization
        //but here accessing beyond index 4 will give array index out of bound exception


        String names[]={"ram","harish","gopi","karan"};
        int n=names.length;

        //for iterable things like arrays for each loop is used
        for(String a:names){
            System.out.println(a);
        }

        //multiple dimensional arrays
        int a[][]=new int[3][4];
        int scores[][]={{1,2,3},{4,5,6},{7,8,9}};
    }
}
