
#ifndef FNFEXCEPTION_H
#define FNFEXCEPTION_H

#include <string>
#include <exception>

class fnfException : public std::exception {
public:
    fnfException(std::string);    
    const char* what() const throw ();
    
private:
    std::string msg;
};

#endif 

