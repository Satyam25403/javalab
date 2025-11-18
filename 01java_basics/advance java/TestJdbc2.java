import com.mysql.cj.jdbc.Driver;
import java.sql.*;
class TestJdbc2
{
public static void main(String[] args) throws SQLException
{
Driver driver = new Driver();

//"jdbc:mysql://localhost:3306/satyam", "root", "Loknath@2534"

Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/satyam", "root", "Loknath@2534"); 
Statement statement = connection.createStatement();
String sqlSelectQuery ="select * from stud"; 
ResultSet resultSet = statement.executeQuery(sqlSelectQuery); 
System.out.println("SNAME\tSAGE\t");
while(resultSet.next()){
String name = resultSet.getString(1); 
Integer age =resultSet.getInt(2); 
System.out.println(name+"\t"+age);
}
connection.close();
}
}
