<%@ page language="java"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>

    <!-- Contains fields where the user can insert the update information-->

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        
        <title>Update books</title>
        
        <link rel="stylesheet" type="text/css" href="styles/style.css">
    </head>
    <body>
            
            <!-- Redirects the user to the index page if they did not land on this page
                by clicking the "Update" button on the index page -->
        <core:if test="${param.requested_action == null || param.requested_action != 'update'}">
            <core:redirect url="index.jsp"/>
        </core:if>
        
            <!-- Creates a connection to eLibrary_Database -->
        <sql:setDataSource var="eLibraryDB" driver="org.apache.derby.jdbc.ClientDriver40"
        url="jdbc:derby://localhost:1527/eLibrary_Database;create=true"/>
      
            <!-- Retrieves all authors from the AUTHORS table -->
        <sql:query dataSource="${eLibraryDB}" var="authors">
          SELECT * FROM AUTHORS ORDER BY ID
        </sql:query>
          
        <div id="updateMenu" class="Menu">
            
            <!-- Form containing the update information -->
          <form id="update_info" action="EBookStoreUpdateBooks" method="POST">      
              <input type="hidden" name="requested_action" value="update">
              <core:forEach var="isbn" items="${paramValues.selected_isbns}">
                  <input type="hidden" name="selected_isbns" value="${isbn}">
              </core:forEach>
            <p>
              Title
              <input type="text" id="fieldTitle" name="title" 
                     form="update_info" value="${param.title}">
            </p>
            
            <p>
              Price
              <input type="text" id="fieldPrice" name="price" 
                     form="update_info" value="${param.price}">
            </p>
            
            <p>
              Rating
              <input type="text" id="fieldRating" name="rating" 
                     form="update_info" value="${param.rating}">
            </p>            
          </form>

          <div id="fieldAuthors">
            Authors

            <div>
              <table id="authorTable">    
                  <tr>
                    <th>
                        <!-- checkbox -->
                    </th>
                    
                    <th>
                        ID
                    </th>
                    
                    <th>
                        Name
                    </th>                                        
                </tr>
                
                    <!-- Inserts a <tr> element into the table containing 
                        information about the each author -->
                  <core:forEach var="author" items="${authors.rows}">
                      
                        <!-- Checks if the request has been forwarded from a servlet;
                            if it has, marks every checkbox that was marked 
                            before the request was made -->
                      <core:set var="isChecked" value=""></core:set>
                      <core:if test="${param.selected_author_ids != null}">
                          <core:forEach var="id" items="${paramValues.selected_author_ids}"> 
                              <core:if test="${author.id == id}">
                                  <core:set var="isChecked" value="checked"></core:set>
                              </core:if>
                          </core:forEach>
                      </core:if>
                    <tr>
                        <td>
                            <input type="checkbox" ${isChecked} 
                               name="selected_author_ids" value="${author.id}" 
                               form="update_info">
                        </td>
                        
                        <td>
                            <core:out value="${author.id}"></core:out>
                        </td>
                        
                        <td>
                            <core:out value="${author.name} ${author.surname}"></core:out>
                        </td>                                                                      
                        
                    </tr>
                </core:forEach>
              </table> 
            </div>            
          </div>

          <div>
              
              <!-- Submits the information to the servlet with the value "confirm" -->
            <button id="confirmUpdate" class="MenuButton" type="submit" 
                    name="action_performed" value="confirm" form="update_info">
              Update
            </button>              
            
               <!-- Submits the information to the servlet with the value "cancel" -->
            <button id="cancelUpdate" class="MenuButton" type="submit" 
                    name="action_performed" value="cancel" form="update_info">
              Cancel
            </button>
          </div>
            
                <!-- Displays the error message received from the servlet -->
            <p id="errorMessage">   
                <core:if test="${requestScope.return_message != null}">
                    <core:out value="${requestScope.return_message}"/>
                </core:if>
            </p>
        </div>  
                  
    </body>
</html>
