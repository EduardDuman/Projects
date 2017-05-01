
/* Clasa Student */


#ifndef STUDENT_H
#define STUDENT_H


#include "Persoana.h"


class Student : public Persoana {
    int id;
    float medie;
    
public:
    Student(string, string, int, float);
    ~Student();
    
    string toString();
    int getId();
    float getMedie();
};

#endif 

