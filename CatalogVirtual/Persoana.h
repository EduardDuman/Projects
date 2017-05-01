
/* Clasa Persoana */


#ifndef PERSOANA_H
#define PERSOANA_H

#include<string>
using namespace std;

class Persoana {    
protected:
    string nume;
    string prenume;
    
public:
    Persoana(string, string);
    ~Persoana();
    
    virtual string toString();    
    string getNume();    
    string getPrenume();
};

#endif 

