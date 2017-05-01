
/* Clasa Profesor */


#ifndef PROFESOR_H
#define PROFESOR_H


#include "Persoana.h"


class Profesor : public Persoana {
    string domeniu;
    
public:
    Profesor(string, string, string);
    ~Profesor();
    
    string toString();
    string getDomeniu();
};

#endif 

