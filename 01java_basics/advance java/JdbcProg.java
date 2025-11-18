import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class JdbcProg {
    private static final String url="jdbc:mysql://localhost:3306/satyam";
    private static final String usr="root";
    private static final String pwd="Loknath@2534";
    public static void main(String[] args){
        Scanner s=new Scanner(System.in);
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("Enter student id:");
        int sid=s.nextInt();
        System.out.println("Enter department:");
        String dept=s.next();
        System.out.println("enter student name:");
        String name=s.next();
        System.out.println("enter student gender:");
        String gen=s.next();
        String query="insert into student(stud_id,dept,name,gender) values(?,?,?,?);";
        try{
            Connection con=DriverManager.getConnection(url, usr, pwd);
            PreparedStatement ps=con.prepareStatement(query);
            ps.setInt(1,sid);
            ps.setString(2,dept);
            ps.setString(3,name);
            ps.setString(4,gen);
            int rowsAffected=ps.executeUpdate();
            if(rowsAffected>0){
                System.out.println("Record inserted");
            }
            else{
                System.out.println("Failed to insert");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }

        
        
    }
    
}