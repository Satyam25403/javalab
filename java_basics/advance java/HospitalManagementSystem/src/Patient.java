import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
class Patient{
    private Connection connection;
    private Scanner sc;
    Patient(Connection c,Scanner s){
        connection=c;
        sc=s;
    }
    public void addPatient(){
        System.out.println("Enter name of the patient:");
        String name=sc.next();
        System.out.println("Enter patient age:");
        int age=sc.nextInt();
        System.out.println("Enter gender of the patient:");
        String gen=sc.next();

        try{
            String query="insert into patients(name,age,gender) values(?,?,?)";
            PreparedStatement ps=connection.prepareStatement(query);
            ps.setString(1,name);
            ps.setInt(2,age);
            ps.setString(3,gen);
            int affectedRows=ps.executeUpdate();
            if(affectedRows>0){
                System.out.println("patient added succesfully");
            }
            else{
                System.out.println("failed to add patient");
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public void viewPatient(){
        String que="select * from patients";
        try{
            PreparedStatement p=connection.prepareStatement(que);
            ResultSet rs=p.executeQuery();
            System.out.println("PATIENTS:");
            System.out.println("+------+--------------+-----+--------+");
            System.out.println("| Id   |   Name       | Age | Gender |");
            System.out.println("+------+--------------+-----+--------+");
            while(rs.next()){
                int id=rs.getInt("id");
                String name=rs.getString("name");
                int age=rs.getInt("age");
                String gen=rs.getString("gender");
                System.out.printf("| %-4s | %-12s | %-3s | %-6s |\n",id,name,age,gen);//count spaces between two |s and format accordingly
                System.out.println("+------+--------------+-----+--------+");
            }
            
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public boolean getPatientById(int id){
        String query="select * from patients where id =?";
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