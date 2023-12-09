import java.io.*;
import java.util.*;
public class HighScore {
    public static void main(String args[]){
        try{
            File file=new File("scores.txt");
            if(!file.exists()){
                throw new FileNotFoundException("file not found");
            }
            Scanner a=new Scanner(file);
            int highscore=Integer.MIN_VALUE;
            while(a.hasNextInt()){
                int score=a.nextInt();
                if(score>highscore){
                    highscore=score;
                }
            }
            if(highscore!=Integer.MIN_VALUE){
                System.out.println("highscore is "+ highscore);
            }
            else{
                System.out.println("highscore not found");
            }
            a.close();
        }
        catch(FileNotFoundException e){
            System.out.println(e.getMessage());
        }
    }
}
