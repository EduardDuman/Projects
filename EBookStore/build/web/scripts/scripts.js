
window.onload = MainFunction;


/** 
 * Function that will be called when the page is loaded
 */
function MainFunction() {  
    
    ClosePopupMessage();
    
    var checkBoxes = document.querySelectorAll("#bookTable input");
    
    Array.prototype.forEach.call(checkBoxes, function(checkBox) {
        checkBox.onclick = UpdateButtons;
    });           
}


/**
 * Returns the number of marked checkboxes contained in the specified parent element
 * 
 * @param {type} table the parent element of the checkboxes
 * @returns {Number}
 */
function GetNumberOfCheckedBoxes(table) {

  var checkBoxes = table.getElementsByTagName("input");
  var numberOfCheckedBoxes = 0;

  Array.prototype.forEach.call(checkBoxes, function(checkBox) {
    if(checkBox.checked) {
      numberOfCheckedBoxes++;
    }
  });

  return numberOfCheckedBoxes;
}


/**
 * Disables or enables the buttons on the index page, depending on how many
 * checkboxes are marked; disables them when no checkboxes are marked, 
 * enables them when at least one checkbox is marked
 */
function UpdateButtons() {

  var deleteB = document.getElementById("deleteButton");
  var updateB = document.getElementById("updateButton");
  var bTable = document.getElementById("bookTable");

  var numberOfCheckedBoxes = GetNumberOfCheckedBoxes(bTable);
  
  if(numberOfCheckedBoxes > 0) {
    updateB.disabled = false;
    deleteB.disabled = false;
  } else {
    updateB.disabled = true;
    deleteB.disabled = true;
  }
}


/**
 * If the request contains a message from a servlet it displays it and then
 * creates a fade transition for it
 */
function ClosePopupMessage() {
        
    var msgBox = document.getElementById("messageBox");   
    
    if(msgBox === null) {
        return;
    }
        
    setTimeout(function() {
        msgBox.style.opacity = 0;
        
        setTimeout(function() {
            msgBox.style.display = "none";                  
        }, 2000);
    }, 2000);    
} 
