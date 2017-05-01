
package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
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
public class EBookStoreUpdateBooks extends HttpServlet {
    
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
        
        String[] isbns = request.getParameterValues("selected_isbns");
        String title = request.getParameter("title");
        String price = request.getParameter("price");
        String rating = request.getParameter("rating");
        String[] authorIds = request.getParameterValues("selected_author_ids");
        String action = request.getParameter("action_performed");
        
        String msg;
        
        HttpSession session = request.getSession(false);
        if(session.getAttribute("message") != null) {
            session.removeAttribute("message");
        }
        
        if(action == null || action.equals("cancel")) {  
            // redirect the user toward the index page if the request did not 
            // come from the updateBook page or if the "cacel" button was pressed
            response.sendRedirect("index.jsp");            
        } else {
                    
            if(title.isEmpty() && price.isEmpty() && rating.isEmpty() 
                    && authorIds == null) {
                msg = "No information to update";
                returnToPage(request, response, msg);
                return;
            }
            
            if(title.length() > 100) {
                msg = "Title cannot exceed 100 characters";
                returnToPage(request, response, msg);
            }
            
            if(!price.isEmpty() && !(price.matches("[1-9]\\d*(.\\d\\d)?"))) {
                msg = "Price is invalid";
                returnToPage(request, response, msg);
            }

            if(!rating.isEmpty() && !(rating.matches("[1-4](.5)?|5"))) {
                msg = "Rating is invalid";
                returnToPage(request, response, msg);
            }

            try {
                updateBooks(isbns, title, price, rating, authorIds);
                
                session.setAttribute("message", "Books have been updated");                
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
     * updateBook.jsp
     * 
     * @param request
     * @param response
     * @param msg
     * @throws ServletException
     * @throws IOException 
     */
    protected void returnToPage(HttpServletRequest request, HttpServletResponse response, String msg)
            throws ServletException, IOException {
        request.setAttribute("return_message", msg);
        request.getRequestDispatcher("updateBooks.jsp").forward(request, response);
    }

    /**
     * Modifies one or more entries in the BOOKS table; only performs the
     * modification if the associated parameter is not empty
     * 
     * @param isbns the IDs of the books to be updated
     * @param title the new title of the books
     * @param price the new price of the books
     * @param rating the new rating of the books
     * @param authorIDs the IDs of the new authors of the book
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public void updateBooks(String[] isbns, String title, String price, 
            String rating, String[] authorIDs) 
            throws ClassNotFoundException, SQLException{
        
        String query;
        Connection connection = null;
        Statement sqlStatement = null;                
                
        String isbnSet;
        
        isbnSet = "('" + isbns[0] + "'";
        
        for(int i = 1; i < isbns.length; i++) {
            isbnSet += ",'" + isbns[i] + "'";
        }
        
        isbnSet += ")";
                
        try{
            Class.forName(driver);

            connection = DriverManager.getConnection(url);

            sqlStatement = connection.createStatement();
            
            if(!title.isEmpty()) {
                query = "UPDATE BOOKS"
                        + " SET title = " + "'" + title + "'"
                        + " WHERE isbn IN " + isbnSet;                 
                sqlStatement.executeUpdate(query);
            }
            
            if(!price.isEmpty()) {
                query = "UPDATE BOOKS"
                        + " SET price = " + price
                        + " WHERE isbn IN " + isbnSet;                 
                sqlStatement.executeUpdate(query);
            }
            
            if(!rating.isEmpty()) {
                query = "UPDATE BOOKS"
                        + " SET rating = " + rating
                        + " WHERE isbn IN " + isbnSet;                 
                sqlStatement.executeUpdate(query);
            }
            
            if(authorIDs != null && authorIDs.length > 0) {
                query = "DELETE FROM BOOK_AUTHOR_LINK"
                        + " WHERE isbn IN " + isbnSet;                 
                sqlStatement.executeUpdate(query);
                
                for(String isbn : isbns) {
                    for(String authorId : authorIDs) {
                        query = "INSERT INTO BOOK_AUTHOR_LINK VALUES ('"
                                + isbn + "'," + authorId
                                + ")";
                        sqlStatement.executeUpdate(query);
                    }  
                }
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
