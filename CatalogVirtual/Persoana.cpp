 
#include "Persoana.h"


Persoana::Persoana(string nume, string prenume) {
    this->nume = nume;
    this->prenume = prenume;
}

Persoana::~Persoana() {
    nume.clear();
    prenume.clear();
}

string Persoana::toString() {
    return (nume + " | " + prenume);
}

string Persoana::getNume() {
    return nume;
}

string Persoana::getPrenume() {
    return prenume;
}
