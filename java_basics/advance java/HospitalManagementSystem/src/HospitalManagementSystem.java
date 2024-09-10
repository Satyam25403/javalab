import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class HospitalManagementSystem {
    private static final String url="jdbc:mysql://localhost:3306/hospital";
    private static final String usr="root";
    private static final String pwd="Loknath@2534";
    public static void main(String[] args){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch(Exception e){
            e.printStackTrace();
        }

        Scanner scanner=new Scanner(System.in);
        try{
            Connection con=DriverManager.getConnection(url, usr, pwd);
            Patient p=new Patient(con, scanner);
            Doctors d=new Doctors(con);
            while(true){
                System.out.println("HOSPITAL MANAGEMENT SYSTEM");
                System.out.println("1.Add patient");
                System.out.println("2.View Patient");
                System.out.println("3.View Doctor");
                System.out.println("4.Book Appointment");
                System.out.println("5.exit");
                System.out.println("Enter your choice:");
                int choice=scanner.nextInt();
                switch(choice){
                    case 1:
                        p.addPatient();
                        System.out.println();
                        break;
                    case 2:
                        p.viewPatient();
                        System.out.println();
                        break;
                    case 3:
                        d.viewDoctors();
                        System.out.println();
                        break;
                    case 4:
                        bookAppointment(p,d,con,scanner);
                        System.out.println();
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("Enter valid choice:");
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void bookAppointment(Patient p,Doctors d,Connection c,Scanner s){
        System.out.println("Enter patient id:");
        int pid=s.nextInt();
        System.out.println("Enter doctor id;");
        int did=s.nextInt();
        System.out.println("enter appointment date (YYYY/MM/DD):");
        String date=s.next();
        //if both patient as well as doctors are available on same date
        if(p.getPatientById(pid) && d.getDoctorById(did)){
            if(checkDoctorAvailability(did,date,c)){
                String appointMentQuery="insert into appointments(patint_id,doctor_id,appointment_date) values(?,?,?);";
                try{
                    PreparedStatement ps=c.prepareStatement(appointMentQuery);
                    ps.setInt(1,pid);
                    ps.setInt(2,did);
                    ps.setString(3,date);
                    int rowsAffected=ps.executeUpdate();
                    if(rowsAffected>0){
                        System.out.println("Appointment Booked");
                    }
                    else{
                        System.out.println("Failed to book appointment");
                    }
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
            else{
                System.out.println("Doctor not available on this date");
            }
        }
        else{
            System.out.println("Either the doctor or the patient doesnt exist");
        }
    }
    public static boolean checkDoctorAvailability(int did,String date,Connection c){
        String query="select count(*) from appointments where doctor_id=? and appointment_date=?";//count(*)retrives how many rows have retrived 
        try{
            PreparedStatement p=c.prepareStatement(query);
            p.setInt(1, did);
            p.setString(2, date);
            ResultSet rs=p.executeQuery();
            if(rs.next()){
                int count=rs.getInt(1);
                if(count==1){
                    return true;
                }
                else{
                    return false;
                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

}
