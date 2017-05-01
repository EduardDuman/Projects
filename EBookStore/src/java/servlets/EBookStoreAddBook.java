

package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 
 * @author Eduard Duman
 */
public class EBookStoreAddBook extends HttpServlet {
    
    String ip = "localhost";
    String port = "1527";
    String url = "jdbc:derby://" + ip + ":" + port
                + "/eLibrary_Database;create=true";
    String driver = "org.apache.derby.jdbc.ClientDataSource40";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String isbn = request.getParameter("isbn");
        String title = request.getParameter("title");
        String price = request.getParameter("price");
        String rating = request.getParameter("rating");
        String action = request.getParameter("action_performed");
        
        String msg;
        
        HttpSession session = request.getSession(false);
        if(session.getAttribute("message") != null) {
            session.removeAttribute("message");
        }
        
        if(action == null || action.equals("cancel")) {       
            // redirect the user toward the index page if the request did not 
            // come from the addBook page or if the "cacel" button was pressed
            response.sendRedirect("index.jsp");            
        } else {
        
            if(isbn.isEmpty()) {
                msg = "ISBN cannot be empty";
                returnToPage(request, response, msg);
                return;
            }

            try {
                if(isbnExists(isbn)) {
                    msg = "ISBN already exists in database";
                    returnToPage(request, response, msg);
                    return;
                }
            } catch(Exception ex) {
                msg = ex.getMessage();
                returnToPage(request, response, msg);
            }

            if(isbn.length() > 17) {
                msg = "ISBN cannot exceed 17 characters";
                returnToPage(request, response, msg);
                return;
            }

            if(title.isEmpty()) {
                msg = "Title cannot be empty";
                returnToPage(request, response, msg);
                return;
            }

            if(title.length() > 100) {
                msg = "Title cannot exceed 100 characters";
                returnToPage(request, response, msg);
                return;
            }

            if(price.isEmpty()) {
                msg = "Price cannot be empty";
                returnToPage(request, response, msg);
                return;
            }

            if(!(price.matches("[1-9]\\d*(.\\d\\d)?"))) {
                msg = "Price is invalid";
                returnToPage(request, response, msg);
                return;
            }

            if(rating.isEmpty()) {
                msg = "Rating cannot be empty";
                returnToPage(request, response, msg);
                return;
            }

            if(!(rating.matches("[1-4](.5)?|5"))) {
                msg = "Rating is invalid";
                returnToPage(request, response, msg);
                return;
            }

            if(request.getParameterValues("selected_author_ids") == null 
                    || request.getParameterValues("selected_author_ids").length == 0) {
                msg = "Book must have at least one author";
                returnToPage(request, response, msg);
                return;
            }


            try {
                addBook(isbn, title, price, rating, request.getParameterValues("selected_author_ids"));
                
                session.setAttribute("message", "Book was added to database");                
                response.sendRedirect("index.jsp"); 
            } catch(SQLException ex) {
                msg = "Connection to database failed";
                returnToPage(request, response, msg);
            } catch(ClassNotFoundException ex) {
                msg = "Could not find database connection driver";
                returnToPage(request, response, msg);
            }
            
            
        }
    }
    
    /**
     * Creates a return message as a request parameter and forwards the request to
     * addBook.jsp
     * 
     * @param request the request to be forwarded
     * @param response the response to be forwarded
     * @param msg the message to be returned
     * @throws ServletException
     * @throws IOException 
     */
    protected void returnToPage(HttpServletRequest request, HttpServletResponse response, String msg)
            throws ServletException, IOException {
        request.setAttribute("return_message", msg);
        request.getRequestDispatcher("addBook.jsp").forward(request, response);
    }
    
    /**
     * Checks whether the specified isbn already exists in the database
     * 
     * @param isbn the checked isbn
     * @return true if the isbn exists in the database, false otherwise
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public boolean isbnExists(String isbn) 
            throws ClassNotFoundException, SQLException {
        
        String query;
        Connection connection = null;
        Statement sqlStatement = null; 
        ResultSet result;
        
        try{
            Class.forName(driver);

            connection = DriverManager.getConnection(url);

            sqlStatement = connection.createStatement();

            query = "SELECT * FROM BOOKS "
                    + "WHERE isbn = "
                    + "'" + isbn + "'";    
            
            result = sqlStatement.executeQuery(query);
            
            return result.next();
                             
        } finally {
            try {
                if(sqlStatement != null) {
                        sqlStatement.close();
                }
                if(connection != null) {
                        connection.close();
                }
            } catch(Exception ex) {
                throw ex;
            }
        }
    } 
    
    /**
     * Add a new entry in the BOOKS table containing the specified data and one 
     * or more entries in the BOOK_AUTHOR_TABLE
     * 
     * @param isbn isbn of the new book
     * @param title title of the new book
     * @param price price of the new book
     * @param rating rating of the new book
     * @param authorIDs IDs of the authors of the new book
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public void addBook(String isbn, String title, String price, 
            String rating, String[] authorIDs) 
            throws ClassNotFoundException, SQLException{
        
        String query;
        Connection connection = null;
        Statement sqlStatement = null; 
        
        isbn = "'" + isbn + "'";
        title = "'" + title + "'";
                
        try{
            Class.forName(driver);

            connection = DriverManager.getConnection(url);

            sqlStatement = connection.createStatement();

            query = "INSERT INTO BOOKS VALUES ("
                    + isbn + ","
                    + title + ","
                    + price + ","
                    + rating
                    + ")";    
            
            sqlStatement.executeUpdate(query);
            
            for(String authorId : authorIDs) {
                query = "INSERT INTO BOOK_AUTHOR_LINK VALUES ("
                        + isbn + "," + authorId
                        + ")";
                sqlStatement.executeUpdate(query);
            }                                   
                
        } finally {
            try {
                if(sqlStatement != null) {
                        sqlStatement.close();
                }
                if(connection != null) {
                        connection.close();
                }
            } catch(Exception ex) {
                throw ex;
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
