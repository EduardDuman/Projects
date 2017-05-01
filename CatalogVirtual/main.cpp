#include <iostream>
#include <fstream>
#include <vector>
#include <algorithm>
#include <regex>

#include "Student.h"
#include "Profesor.h"
#include "fnfException.h"

using namespace std;

/* getListaStudenti(vector<Profesor*>) - citeste datele studentilor din fisierul
 *                                       STUDENTI.TXT si le salveaza in 
 *                                       variabila de intrare listaStudenti;
 *                                       arunca o exceptie fnfException daca
 *                                       fisierul nu poate fi deschis
 */
void getListaStudenti(vector<Student*> &listaStudenti) {
    
    string id;
    string medie;
    string nume;
    string prenume;     
    Student* studentNou;    
    
    ifstream fisierStudent;
    fisierStudent.open("STUDENTI.TXT");
    
    if(fisierStudent.is_open() == false) {
        throw (new fnfException("studenti"));
    }
    
    do {
        
        getline(fisierStudent, id);
        getline(fisierStudent, nume);
        getline(fisierStudent, prenume);
        getline(fisierStudent,  medie);        
        
        if(id.empty() == false) {
            studentNou = new Student(nume, prenume, stoi(id), stof(medie));
            listaStudenti.push_back(studentNou);
        }
    } while(id.empty() == false);
    
    fisierStudent.close();    
}

/* getListaProfesori(vector<Profesor*>) - citeste datele prfesorilor din fisierul
 *                                        PROFESORI.TXT si le salveaza in 
 *                                        variabila de intrare listaProfesori;
 *                                        arunca o exceptie fnfException daca
 *                                        fisierul nu poate fi deschis
 */
void getListaProfesori(vector<Profesor*> &listaProfesori) {
    
    string nume;
    string prenume;
    string domeniu;
    Profesor* profesorNou;    
    
    ifstream fisierProfesor;
    fisierProfesor.open("PROFESORI.TXT");
    
    if(fisierProfesor.is_open() == false) {
        throw (new fnfException("profesori"));
    }
    
    do {
        getline(fisierProfesor, nume);
        getline(fisierProfesor,prenume);
        getline(fisierProfesor, domeniu);
        
        if(nume.empty() == false) {
            profesorNou = new Profesor(nume, prenume, domeniu);
            listaProfesori.push_back(profesorNou);
        }
    } while(nume.empty() == false);
    
    fisierProfesor.close();    
}

/* listeazaDate() - citeste toate datele studentilor si profesorilor din fisierele
 *                  STUDENTI.TXT si PROFESORI.TXT si ii afiseaza, pe cate
 *                  o linie, in consola
 */
void listeazaDate() {
    
    vector<Student*> listaStudenti; 
    getListaStudenti(listaStudenti);
    
    vector<Profesor*> listaProfesori;
    getListaProfesori(listaProfesori);
    
    cout << endl << "Studenti:";
    
    if(listaStudenti.empty() == true) {
        
        cout << endl << "Nu exista studenti!"; 
    } else {
        
        for(int i = 0; i < listaStudenti.size(); i++) {
            
            cout << endl << listaStudenti[i]->toString();
        }            
    }  
    
    cout << endl;
    
    cout << endl << "Profesori:";
    
    if(listaProfesori.empty() == true) {
        
        cout << endl << "Nu exista profesori!"; 
    } else {
        
        for(int i = 0; i < listaProfesori.size(); i++) {
            
            cout << endl << listaProfesori[i]->toString();
        }            
    }   
    
    cout << endl;
    
    for(int i = 0; i < listaStudenti.size(); i++) {
        delete listaStudenti[i];
    }
    
    for(int i = 0; i < listaProfesori.size(); i++) {
        delete listaProfesori[i];
    }
}

/* cautaDateStudenti() - citeste de la tastatura variabila id si afiseaza 
 *                       studentul cu id-ul respectiv; afiseaza un mesaj 
 *                       corespunzator daca niciun student nu e gasit
 */
void cautaDateStudenti() {
    
    string id;
    int idCautat;
    bool niciunRezultat = true;
    
    vector<Student*> listaStudenti;
    getListaStudenti(listaStudenti);
    
    while(true) {
        cout << endl << "ID student: ";
        getline(cin, id);
        
        if(id.find_first_not_of("0123456789") == string::npos) {
            break;
        }
        cout << endl << "ID-ul poate contine doar cifre!";
    }
    
    idCautat = stoi(id);
    
    for(int i = 0; i < listaStudenti.size(); i++) {
        
        if(listaStudenti[i]->getId() == idCautat) {
            
            cout << endl << listaStudenti[i]->toString();
            niciunRezultat = false;
            break;
        }
    }
    
    if(niciunRezultat == true) {
        cout << endl << "Niciun student nu corespunde ID-ului introdus!";
    }
    
    cout << endl;
    
    for(int i = 0; i < listaStudenti.size(); i++) {
        delete listaStudenti[i];
    }
}

/* cautaDateProfesori() - citeste de la tastatura variabila numeCautat si 
 *                        afiseaza toti profesorii al caror nume contin
 *                        variabila numeCautat; afiseaza un mesaj corespunzator
 *                        daca niciun profesor nu e gasit
 */
void cautaDateProfesori() {
    
    string numeCautat;
    string numeProfesor;
    bool niciunRezultat = true;
    
    vector<Profesor*> listaProfesori;
    getListaProfesori(listaProfesori);
    
    cout << endl << "Nume profesor: ";
    getline(cin, numeCautat);
    transform(numeCautat.begin(), numeCautat.end(), numeCautat.begin(), ::tolower);
    
    for(int i = 0; i < listaProfesori.size(); i++) {
        
        numeProfesor = listaProfesori[i]->getNume();
        transform(numeProfesor.begin(), numeProfesor.end(), numeProfesor.begin(), ::tolower);
        
        if(numeProfesor.find(numeCautat) != string::npos) {
            cout << endl << listaProfesori[i]->toString();
            niciunRezultat = false;
        }
    }
    
    if(niciunRezultat == true) {
        cout << endl << "Niciun profesor nu corespunde numelui introdus!";
    }
    
    cout << endl;
    
    for(int i = 0; i < listaProfesori.size(); i++) {
        delete listaProfesori[i];
    }
}

/* cautaDate() - afiseaza optiunile pentru cautarea datelor; se opreste cand 
 *               revenireLaMP devine true sau cand este aruncata o exceptie                                       
 */
void cautaDate() {
    
    bool revenireLaMP = false;
    string optiune;

    while (revenireLaMP == false) {

        cout << endl;
        cout << "1. Despre studenti." << endl;
        cout << "2. Despre profesori." << endl;
        cout << "3. Revenire la meniul principal." << endl;
        cout << "$";

        getline(cin, optiune);
        if(optiune.empty() == true || optiune.size() > 1) {
            cout << endl << "Optiune incorecta!" << endl;
            continue;
        }

        switch (optiune[0]) {

            case '1':
                cautaDateStudenti();
                revenireLaMP = true;
                break;

            case '2':
                cautaDateProfesori();
                revenireLaMP = true;
                break;

            case '3':
                revenireLaMP = true;
                break;

            default:
                cout << endl << "Optiune incorecta!" << endl;
        }
    }
}

/* introducereDateStudenti() - citeste de la tastatura variabilele nume,
 *                              prenume, id si medie si creeaza un nou obiect
 *                              de tip Student pe baza lor
 */
void introducereDateStudenti() {
    
    string id;
    string medie;    
    string nume;
    string prenume;
    string repeta;
    int valoareId;
    float valoareMedie;
    bool revenireLaMP = false;;
    vector<int> listaId;
    regex patternMedie ("[0-9](.[0-9][0-9]?)?|10");
    
    fstream listaStudenti;    
    listaStudenti.open("STUDENTI.TXT", ios::in);
            
    do {
        getline(listaStudenti, id);
        
        if (id.empty() == false) {
            
            listaId.push_back(stoi(id));
            
            getline(listaStudenti, id);
            getline(listaStudenti, id);
            getline(listaStudenti, id);
        }
    } while(id.empty() == false);   
    
    listaStudenti.close();
    listaStudenti.open("STUDENTI.TXT", ios::out | ios::app);
    
    while(revenireLaMP == false) {
        
        bool idExista = true;
        
        while(idExista == true) {
            
            cout << endl << "ID: ";
            getline(cin, id);
            
            if(id.empty() == true) {
                continue;
            }
            
            if(id.find_first_not_of("0123456789") != string::npos) {
                cout << "ID-ul poate contine doar cifre!";
                continue;
            }
            
            valoareId = stoi(id);
            
            idExista = false;
            for(int i = 0; i < listaId.size(); i++) {
                if(listaId[i] == valoareId) {
                    idExista = true;
                    cout << "ID-ul exista!";
                    break;
                }
            }
        } 
        
        listaId.push_back(valoareId);
        
        do {
            cout << endl << "Nume: ";
            getline(cin, nume);
        } while (nume.empty() == true);

        do {
            cout << endl << "Prenume: ";
            getline(cin, prenume);
        } while (prenume.empty() == true);
            
        do {
            cout << endl << "Media: ";
            getline(cin, medie);
            
            if(medie.empty() == true) {
                continue;
            }
            
            if(regex_match(medie, patternMedie) == true) {
                break;                
            }
            cout << endl << "Media trebuie sa fie de forma X.XX, X.X sau 10!";
        } while(true);
        
        valoareMedie = stof(medie);
        
        Student* studentNou = new Student(nume, prenume, valoareId, valoareMedie);

        listaStudenti << studentNou->getId() << endl;        
        listaStudenti << studentNou->getNume() << endl;
        listaStudenti << studentNou->getPrenume() << endl;
        listaStudenti << studentNou->getMedie() << endl;
               
        delete studentNou;
        
        cout << endl << "Student nou salvat!";
        
        while (true) {
            cout << endl << "Doriti sa introduceti un nou student (d/n)? ";
            getline(cin, repeta);

            if (repeta == "d" || repeta == "D") {
                break;
            }
            if (repeta == "n" || repeta == "N") {
                revenireLaMP = true;
                break;
            }
        }
    }
    listaStudenti.close();    
}

/* introducereDateProfesori() - citeste de la tastatura variabilele nume,
 *                              prenume si domeniu si creeaza un nou obiect
 *                              de tip Profesor pe baza lor
 */
void introducereDateProfesori() {

    bool revenireLaMP = false;
    string repeta;
    string nume;
    string prenume;
    string domeniu;
    
    ofstream listaProfesori;
    listaProfesori.open("PROFESORI.TXT", ios::app); 
    
    cout << endl << "Date profesor nou:";

    while (revenireLaMP == false) {

        do {
            cout << endl << "Nume: ";
            getline(cin, nume);
        } while (nume.empty() == true);

        do {
            cout << endl << "Prenume: ";
            getline(cin, prenume);
        } while (prenume.empty() == true);

        do {
            cout << endl << "Domeniu: ";
            getline(cin, domeniu);
        } while (domeniu.empty() == true);

        Profesor* profesorNou = new Profesor(nume, prenume, domeniu);
        
        listaProfesori << profesorNou->getNume() << endl;
        listaProfesori << profesorNou->getPrenume() << endl;
        listaProfesori << profesorNou->getDomeniu() << endl;
        
        delete profesorNou;
        
        cout << endl << "Profesor now salvat!";

        while (true) {
            cout << endl << "Doriti sa introduceti un nou profesor (d/n)? ";
            getline(cin, repeta);

            if (repeta == "d" || repeta == "D") {
                break;
            }
            if (repeta == "n" || repeta == "N") {
                revenireLaMP = true;
                break;
            }
        }
    }
    
    listaProfesori.close();
}

/* introducereDate() - afiseaza optiunile pentru introducerea datelor;
 *                     se opreste cand revenireLaMP devine true sau cand
 *                     este aruncata o exceptie                     
 */
void introducereDate() {

    bool revenireLaMP = false;
    string optiune;

    while (revenireLaMP == false) {

        cout << endl;
        cout << "1. Despre studenti." << endl;
        cout << "2. Despre profesori." << endl;
        cout << "3. Revenire la meniul principal." << endl;
        cout << "$";

        getline(cin, optiune);
        if(optiune.empty() == true || optiune.size() > 1) {
            cout << endl << "Optiune incorecta!" << endl;
            continue;
        }

        switch (optiune[0]) {

            case '1':
                introducereDateStudenti();
                revenireLaMP = true;
                break;

            case '2':
                introducereDateProfesori();
                revenireLaMP = true;
                break;

            case '3':
                revenireLaMP = true;
                break;

            default:
                cout << endl << "Optiune incorecta!" << endl;
        }
    }
}

/* deschideMP() - afiseaza in consola optiunile din meniu si
 *                apeleaza functia corespunzatoare optiunii alese;
 *                se opreste cand iesireAplicatie devine true sau cand este
 *                aruncata o exceptie                     
 */
void deschideMP() {

    bool iesireAplicatie = false;
    string optiune;

    while (iesireAplicatie == false) {

        cout << endl;
        cout << "1. Introducere date." << endl;
        cout << "2. Cautare date." << endl;
        cout << "3. Listare date." << endl;
        cout << "0. Iesire Aplicatie." << endl;
        cout << "$";

        getline(cin, optiune);
        if(optiune.empty() == true || optiune.size() > 1) {
            cout << endl << "Optiune incorecta!" << endl;
            continue;
        }

        switch (optiune[0]) {

            case '1':                
                introducereDate();                
                break;

            case '2':
                cautaDate();
                break;

            case '3':
                listeazaDate();                
                break;

            case '0':
                iesireAplicatie = true;
                break;

            default:
                cout << endl << "Optiune incorecta!" << endl;
        }
    }
}

/* Functia main */
int main() {

    try {
        deschideMP();
    }     
    catch(fnfException* e) {
        
        string continutExceptie = e->what();
        
        if(continutExceptie == "studenti") {
            cout << "Lipseste fisierul STUDENTI.TXT";
        }
        if(continutExceptie == "profesori") {
            cout << "Lipseste fisierul PROFESORI.TXT";
        }
    }
    catch(exception &e) {
        cout << "Exceptie: " << e.what();
    }
            
    return 0;
}