
#include "Student.h"


Student::Student(string nume, string prenume, int id, float medie) : Persoana(nume, prenume) {
    this->id = id;
    this->medie = medie;
}

Student::~Student() {
} 

string Student::toString() {
    return (to_string(id) + " | " + Persoana::toString() + " | " + to_string(medie).substr(0,4));
}

int Student::getId() {
    return id;
}

float Student::getMedie() {
    return medie;
}


