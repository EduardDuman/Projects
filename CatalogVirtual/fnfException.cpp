
/* Clasa fnfException mosteneste clasa exception.
   Exceptii de acest tip sunt aruncate cand fisierele
   STUDENTI.TXT sau PROFESORI.TXT nu pot fi deschise
 */

#include "fnfException.h"

fnfException::fnfException(std::string msg) {
    this->msg = msg;
}

const char* fnfException::what() const throw() {
    return msg.c_str();
}



