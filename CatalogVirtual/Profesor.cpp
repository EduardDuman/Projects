
#include "Profesor.h"


Profesor::Profesor(string nume, string prenume, string domeniu) : Persoana(nume, prenume) {
    this->domeniu = domeniu;
}

Profesor::~Profesor() {
    domeniu.clear();
}

string Profesor::toString() {
    return (Persoana::toString() + " | " + domeniu);
}

string Profesor::getDomeniu() {
    return domeniu;
}

