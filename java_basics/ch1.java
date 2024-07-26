//primitive data types ,basic syntax,comments
//bool=false,byte=0,short=0,int=0,long=0L,float=0.0f,double=0.0d,char="\o0000"
public class ch1{
    public static void main(String[] args) {

        System.out.println("Heloo people");
        //other than "_"  java supports "$" into variable name
        
        boolean $varib$all=true;     //size:1 bit                                        
        byte c=23;                   //size:1 byte         from -128    to   127                           
        short d=3200;                //size:         from  -32768    to 32767     
        int e=89364;                 //size:         from   -2147483648     to   2147483647   
        long f=83964218;             //size:         from    -9223372036854775808    to   9223372036854775807   
        float g=3.14f;      //f is mandatory to specify float  fractional numbers suffecient to store 6 to 7 decimal digits   
        double h=2.0004;             //size:         from    fractionalnumbers  to store 15 decimal digits     
        char i='d';                  //size:         from     single characters/letter /ascii values
        
        

        //strict rule:  ' 'used for characters ;    " "used for strings


        //TYPECONVERSIONS: widening(implicit)=smaller to larger ;
        //narrowing(explicit)=larger to smaller type   {casting}        a.k.a lossy conversion


        //for multi line comments /* are used */
    }
}


//java has 50 keywords use camelcase convention for variable and method names:: camelCase
//class names start with capital::CamelCase


