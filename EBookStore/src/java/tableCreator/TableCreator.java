package tableCreator;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Eduard Duman
 */
public class TableCreator {
    
    public static void main(String[] args) {
        
        try {
            Class.forName("org.apache.derby.jdbc.ClientDataSource40");
            System.out.println("Found DBMS driver.");
        } catch(ClassNotFoundException ex) {
            System.out.println("Could not find DBMS driver.");
            return;
        }
        
        String dbIP = "localhost";
        String port = "1527";
        String dbURL = "jdbc:derby://" + dbIP + ":" + port
                + "/eLibrary_Database;create=true";
        String query;
        
        Connection connection = null;
        Statement sqlStatement = null;
        DatabaseMetaData metaData = null;
        ResultSet table = null;
        
        try {
            System.out.println("Connecting to database...");
            connection = DriverManager.getConnection(dbURL);
            System.out.println("Connected to database.");
        } catch (SQLException ex) {
            System.out.println("Connection to database failed.");  
            System.out.println(ex.getMessage());
            return;
        }
        
        try {
            metaData = connection.getMetaData();
            
            sqlStatement = connection.createStatement();
            
            table = metaData.getTables(null, null, "BOOKS", null);            
            if(table.next()) {
                System.out.println("Table BOOKS already exists.");
            } else {            
                query = "CREATE TABLE BOOKS ("
                        + "isbn VARCHAR(17) NOT NULL PRIMARY KEY, "
                        + "title VARCHAR(100) NOT NULL, "                        
                        + "price FLOAT(2), "
                        + "rating FLOAT"
                        + ")";            
                sqlStatement.executeUpdate(query);
                System.out.println("Created BOOKS table.");
                
                query = "INSERT INTO BOOKS VALUES"
                        + "('9789731046198', 'Maitreyi', 17.34, 4),"
                        + "('9738670292', 'Padurea Spanzuratilor', 14, 4.5),"
                        + "('9786061500147', 'Moara cu noroc', 9.88, 5),"
                        + "('978-0201633610', 'Design Patterns: Elements "
                        + "of Reusable Object-Oriented Software', 104, 4.5),"
                        + "('978-973-87260-7-9', 'Ion', 17.34, 4.5)";
                sqlStatement.executeUpdate(query);
                System.out.println("Populated BOOKS table.");
            }
            
            table = metaData.getTables(null, null, "AUTHORS", null);            
            if(table.next()) {
                System.out.println("Table AUTHORS already exists.");
            } else {            
            query = "CREATE TABLE AUTHORS ("
                    + "id INTEGER NOT NULL PRIMARY KEY, "
                    + "name VARCHAR(30) NOT NULL, "
                    + "surname VARCHAR(30) NOT NULL"                   
                    + ")";            
            sqlStatement.executeUpdate(query);
            System.out.println("Created AUTHORS table.");
            
            query = "INSERT INTO AUTHORS VALUES"
                        + "(10, 'Mircea', 'Eliade'),"
                        + "(23, 'Liviu', 'Rebreanu'),"
                        + "(12, 'Ioan', 'Slavici'),"
                        + "(52, 'Erich', 'Gamma'),"
                        + "(57, 'Richard', 'Helm'),"
                        + "(30, 'Ralph', 'Johnson'),"
                        + "(101, 'John', 'Vlissides')";
                sqlStatement.executeUpdate(query);
                System.out.println("Populated AUTHORS table.");
            }
            
            table = metaData.getTables(null, null, "BOOK_AUTHOR_LINK", null);            
            if(table.next()) {
                System.out.println("Table BOOK_AUTHOR_LINK already exists.");
            } else {
            query = "CREATE TABLE BOOK_AUTHOR_LINK ("
                    + "isbn VARCHAR(17) NOT NULL, "
                    + "author_id INTEGER NOT NULL, "                    
                    + "PRIMARY KEY(isbn, author_id)"
                    + ")";            
            sqlStatement.executeUpdate(query);
            System.out.println("Created BOOK_AUTHOR_LINK table.");
            
            query = "INSERT INTO BOOK_AUTHOR_LINK VALUES"
                        + "('9789731046198', 10),"
                        + "('9738670292', 23),"
                        + "('9786061500147', 12),"
                        + "('978-0201633610', 52),"
                        + "('978-0201633610', 57),"
                        + "('978-0201633610', 30),"
                        + "('978-0201633610', 101),"
                        + "('978-973-87260-7-9', 23)";
                sqlStatement.executeUpdate(query);
                System.out.println("Populated BOOK_AUTHOR_LINK table.");
            }
            
        } catch(SQLException ex) {
            System.out.println("Could not run one or more SQL statements.");
            System.out.println(ex.getMessage());            
        } finally {
            try {
                if(table != null) {
                    table.close();
                }
                if(sqlStatement != null) {
                    sqlStatement.close();
                }
                if(connection != null) {
                    connection.close();
                }
                
            } catch(SQLException ex) {
                System.out.println("An error occured while closing connections.");
            }
        }
        
        
    }
}
