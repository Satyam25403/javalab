//loops, control transfer statements
public class ch3 {
    public static void main(String[] args) {
        int i;


        //for loop
        for(i=0;i<100;i++){}

        //while
        while(i<10){}

        //do while
        do{}while(i<10);        //excecute atleast once

        //break:to come out of a loop
        //continue: jump to next iteration i.e. skip current iteration
        //in general normal break and for loop work on a single loop


        //labelled break and continue :to do their functionality on nested loops also
        here:               //label/name of the immediate block
        while(i<10){
            while(i<5){
                if(i==4){
                    continue here;
                }
            }
        }
        
    }
}
