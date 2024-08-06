import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcExample {
  public static void main(String[] args) {
    // Define database connection parameters
    String dbUrl = "jdbc:mysql://localhost:3306/mydatabase";
    String username = "myuser";
    String password = "mypassword";

    try {
      // Establish a connection
      Connection conn = DriverManager.getConnection(dbUrl, username, password);

      // Create a statement
      Statement stmt = conn.createStatement();

      // Insert records
      String query1 = "create table department(dept_id int,dept_name varchar(15))";
      String query2 = "insert into department values(1,'IT'),(2,'HR'),(3,'Payroll')";
      String query3 = "alter table department add constraint p_key primary key(dept_id)";
    
      stmt.executeUpdate(query1);
      stmt.executeUpdate(query2);
      stmt.executeUpdate(query3);

      // Close resources
      stmt.close();
      conn.close();
      
    } catch (SQLException e) {
      System.out.println("Error: " + e.getMessage());
    }
  }
}
