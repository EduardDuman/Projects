
<%@ page language="java"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>

    <!-- Contains fields where the user can insert information about the new book -->

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        
        <title>Add book</title>
        
        <link rel="stylesheet" type="text/css" href="styles/style.css">
    </head>
    <body>
        
            <!-- Redirects the user to the index page if they did not land on this page
                by clicking the "Add" button on the index page -->
        <core:if test="${param.requested_action == null || param.requested_action != 'add'}">
            <core:redirect url="index.jsp"/>
        </core:if>
        
            <!-- Creates a connection to eLibrary_Database -->
        <sql:setDataSource var="eLibraryDB" driver="org.apache.derby.jdbc.ClientDriver40"
        url="jdbc:derby://localhost:1527/eLibrary_Database;create=true"/>
      
            <!-- Retrieves all authors from the AUTHORS table -->
        <sql:query dataSource="${eLibraryDB}" var="authors">
          SELECT * FROM AUTHORS ORDER BY ID
        </sql:query>
          
        <div id="addMenu" class="Menu">
            
            <!-- Form containing information about the book to be inserted 
                into the database -->
          <form id="new_book_info" action="EBookStoreAddBook" method="POST">
              <input type="hidden" name="requested_action" value="add">
            <p>
              ISBN
              <input type="text" id="fieldIsbn" name="isbn" 
                     form="new_book_info" value="${param.isbn}">
            </p>
            
            <p>
              Title
              <input type="text" id="fieldTitle" name="title" 
                     form="new_book_info" value="${param.title}">
            </p>
            
            <p>
              Price
              <input type="text" id="fieldPrice" name="price" 
                     form="new_book_info" value="${param.price}">
            </p>
            
            <p>
              Rating
              <input type="text" id="fieldRating" name="rating" 
                     form="new_book_info" value="${param.rating}">
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
                               form="new_book_info">
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
            <button id="confirmAdd" class="MenuButton" type="submit" 
                    name="action_performed" value="confirm" form="new_book_info">
              Add
            </button>              
              <!-- Submits the information to the servlet with the value "cancel" -->
            <button id="cancelAdd" class="MenuButton" type="submit" 
                    name="action_performed" value="cancel" form="new_book_info">
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
