

<%@ page language="java"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>

    <!-- The index page of the application -->

<!DOCTYPE html>

<html>

  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    
    <title>
      eBookStore
    </title>

    <link rel="stylesheet" type="text/css" href="styles/style.css">

    <script src="scripts/scripts.js"></script>

  </head>

  <body>
        <!-- Creates a connection to eLibrary_Database -->
      <sql:setDataSource var="eLibraryDB" driver="org.apache.derby.jdbc.ClientDriver40"
        url="jdbc:derby://localhost:1527/eLibrary_Database;create=true"/>
      
        <!-- Retrieves all books from the BOOKS table -->
      <sql:query dataSource="${eLibraryDB}" var="books">
        SELECT * FROM BOOKS
      </sql:query>
      
      <div id="mainBox">
        <div id="bookBox">
            <table id="bookTable">            
                <tr>
                    <th>
                        <!-- checkbox -->
                    </th>
                    
                    <th>
                        ISBN
                    </th>
                    
                    <th>
                        Title
                    </th>
                    
                    <th>
                        Authors
                    </th>
                    
                    <th>
                        Price
                    </th>
                    
                    <th>
                        Rating
                    </th>
                </tr>
                
                    <!-- Inserts a <tr> element into the table containing 
                        information about the each book -->
                <core:forEach var="book" items="${books.rows}">
                    <tr>
                        <td>
                            <input type="checkbox" name="selected_isbns" 
                                   value="${book.isbn}" form="checked_books">
                        </td>
                        
                        <td>
                            <core:out value="${book.isbn}"></core:out>
                        </td>
                        
                        <td>
                            <core:out value="${book.title}"></core:out>
                        </td>
                        
                        <td>
                            <core:set var="book_isbn" value="'${book.isbn}'"></core:set>
                            
                                <!-- Retrieves authors from the AUTHORS table -->
                            <sql:query dataSource="${eLibraryDB}" var="authors">
                              SELECT id, name, surname FROM BOOKS 
                              JOIN BOOK_AUTHOR_LINK USING (isbn) 
                              JOIN AUTHORS ON author_id = id 
                              WHERE books.isbn = ${book_isbn}
                            </sql:query>
                              
                              <!-- Inserts each author into the author column -->
                            <core:forEach var="author" items="${authors.rows}" 
                                          varStatus="author_index">
                                <core:if test="${author_index.index != 0}">
                                    <core:out value=","></core:out>
                                </core:if>                                
                                <core:out value="${author.name} ${author.surname}"></core:out>
                            </core:forEach>                           
                        </td>
                        
                        <td>
                            <core:out value="${book.price}"></core:out>
                        </td>
                        
                        <td>
                            <core:out value="${book.rating}"></core:out>
                        </td>
                    </tr>
                </core:forEach>

            </table>
        </div>

          <!-- Button that redirects to the addBook page -->
        <button id="addButton" type="submit" form="redirect_to_add_menu" 
                formaction="addBook.jsp" name="requested_action" value="add"> 
            Add
        </button>
          
          <!-- Button that redirects to the updateBooks page -->
        <button id="updateButton" disabled="true" type="submit" form="checked_books" 
                formaction="updateBooks.jsp" name="requested_action" value="update">
            Update
        </button>

          <!-- Button that sends request to the EBookStoreDeleteBooks servlet -->
        <button id="deleteButton" disabled="true" type="submit" form="checked_books" 
                formaction="EBookStoreDeleteBooks" name="requested_action" value="delete">
            Delete
        </button>
          
          <!-- Form containing the serial numbers of the selected books -->
        <form id="checked_books" method="POST">              
        </form>
          
          <!-- Form containing no relevant data; it is only used to redirect 
            the user to the addBook page -->
        <form id="redirect_to_add_menu">              
        </form>
      </div>   
        
        <!-- Message that is displayed if one or more books are successfully 
            added, updated or deleted-->
      <core:if test="${sessionScope.message != null}">  
        <div id="messageBox">  
          <core:out value="${sessionScope.message}" />
          <!-- Parameter is removed after message is displayed -->
          <core:remove var="message" scope="session" />   
        </div>
      </core:if>

  </body>

</html>
