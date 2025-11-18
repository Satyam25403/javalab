import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

class Doctors{
    private Connection connection;
    Doctors(Connection c){
        connection=c;
    }
    
    public void viewDoctors(){
        String que="select * from doctors";
        try{
            PreparedStatement p=connection.prepareStatement(que);
            ResultSet rs=p.executeQuery();
            System.out.println("Doctors:");
            System.out.println("+--------+--------------+---------------------+");
            System.out.println("| Id     |   Name       |Specialization       |");
            System.out.println("+--------+--------------+---------------------+");
            while(rs.next()){
                int id=rs.getInt("id");
                String name=rs.getString("name");
                String spec=rs.getString("specialization");
                System.out.printf("| %-6s | %-14s | %-14s |\n",id,name,spec);//count spaces between two |s and format accordingly
                System.out.println("+--------+--------------+---------------------+");
            }
            
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public boolean getDoctorById(int id){
        String query="select * from doctors where id =?";
        try{
            PreparedStatement p=connection.prepareStatement(query);
            p.setInt(1,id);
            ResultSet r=p.executeQuery();
            if(r.next()){
                return true;
            }
            else{
                return false;
            }
        }catch(SQLException e){
            e.printStackTrace();
            
        }
        return false;
    }

}
