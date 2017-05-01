
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
public class EBookStoreDeleteBooks extends HttpServlet {
    
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
        
        String requestedAction = request.getParameter("requested_action");     
        String[] bookIDs = request.getParameterValues("selected_isbns");
        
        HttpSession session = request.getSession(false);
        if(session.getAttribute("message") != null) {
            session.removeAttribute("message");
        }
        
        if(requestedAction == null || !requestedAction.equals("delete") || 
                bookIDs == null) {         
            // redirect the user toward the index page if the request did not 
            // come from the index page 
            response.sendRedirect("index.jsp");            
        } else {                    
            
            try {
                deleteBooks(bookIDs);
                
                session.setAttribute("message", "Books were deleted from database");               
                response.sendRedirect("index.jsp"); 
                
            } catch(SQLException ex) {
                session.setAttribute("message", "Connection to database failed");               
                response.sendRedirect("index.jsp"); 
            } catch(ClassNotFoundException ex) {
                session.setAttribute("message", "Could not find database connection driver");               
                response.sendRedirect("index.jsp"); 
            }
        }
    }
    
    /**
     * Deletes one or more entries from the BOOKS table
     * 
     * @param bookIDs the IDs of the books to be deleted
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public void deleteBooks(String[] bookIDs) throws ClassNotFoundException, SQLException{
        
        String query;        
        Connection connection = null;
        Statement sqlStatement = null; 
        String idSet;
        
        idSet = "('" + bookIDs[0] + "'";
        
        for(int i = 1; i < bookIDs.length; i++) {
            idSet += ",'" + bookIDs[i] + "'";
        }
        
        idSet += ")";
                                
        try{
            Class.forName(driver);

            connection = DriverManager.getConnection(url);

            sqlStatement = connection.createStatement();
                                                
            query = "DELETE FROM BOOKS "
                    + "WHERE isbn IN " + idSet;                      
            
            sqlStatement.executeUpdate(query);    
                            
            query = "DELETE FROM BOOK_AUTHOR_LINK "
                    + "WHERE isbn IN " + idSet;                      
            
            sqlStatement.executeUpdate(query);             
                             
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
