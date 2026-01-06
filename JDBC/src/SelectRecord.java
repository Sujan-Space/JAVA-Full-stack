import java.sql.*;

class SelectRecord {
    public static void main(String[] args) {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // 1. Load the driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. Establish the connection
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/myapp", "root", "Sujan@2003");
            System.out.println("Connected to MySQL Database");

            // 3. Create a statement
            stmt = con.createStatement();

            // 4. Execute the statement and get the result set
            rs = stmt.executeQuery("SELECT * FROM customer");

            // 5. Process the result set
            while (rs.next()) {
                // Fetch the values by column index (1 for id, 2 for name, 3 for age)
                int id = rs.getInt(1);
                String name = rs.getString(2);
                int age = rs.getInt(3);
                System.out.println(id + " " + name + " " + age);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            // Close the connections and statement in finally block
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}




/**
 * 3. Creating statement
 * Statement st=con.createStatement();
 * int count=st.executeUpdate("insert into customer values(1256,'anil',89000");
 * if(count>0)
 * syso("Record updated in the table ");
 * 
 * 
 * 
 * 
 * 
 * /
 */

//int count = st.executeupdate("update customer set bal=25000 where custid=114");

// mysql -u appuser -p'Sujan@2003' -h 127.0.0.1





