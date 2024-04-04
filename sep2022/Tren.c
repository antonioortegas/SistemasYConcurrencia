/*
 * Examen Septiembre 2022 PSC - todos los grupos.
 * Implementación Tren.c
*/

#include "Tren.h"
#include <stdlib.h>
#include <stdio.h>
#include <string.h>

/// 0.25 pts 
void inicializarTren(Vagon * tren, int maximoVagones){
    for(int i = 0; i<maximoVagones; i++){
        tren[i] = NULL;
    }
}


void crearAsiento(unsigned numeroAsiento, char * nombre, Vagon *nuevoAsiento){
    *nuevoAsiento = (Vagon)malloc(sizeof(struct Asiento));
    if(nuevoAsiento != NULL){ //Robusto
        (*nuevoAsiento)->num = numeroAsiento;
        strcpy((*nuevoAsiento)->nombre, nombre);
    }
}
/// 1.75 pts 
int entraPasajero(Vagon * tren, unsigned numeroVagon, unsigned numeroAsiento, char * nombre){
    printf("sentando a %s", nombre);
    Vagon ptr = tren[numeroVagon];
    Vagon ant = NULL;
    Vagon nuevo = NULL;
    if(ptr == NULL){ // esta vacia
        crearAsiento(numeroAsiento, nombre, &nuevo);
        nuevo->sig = NULL;
        tren[numeroVagon] = nuevo;
        return 0;
    }
    while(ptr!=NULL){
        if(numeroAsiento == ptr->num){
            return -1;
        }
        if(numeroAsiento < ptr->num){
            crearAsiento(numeroAsiento, nombre, &nuevo);
            nuevo->sig = ptr;
            if(ant ==NULL){
                tren[numeroVagon] = nuevo;
            } else {
                ant->sig = nuevo;
            }
            return 0;
        }
        ant = ptr;
        ptr = ptr->sig;
    }
    crearAsiento(numeroAsiento, nombre, &nuevo);
    nuevo->sig = NULL;
    ant->sig = nuevo;
    return 0;
}

/// 0.75 pt 
void imprimeTren(Vagon * tren, unsigned maximoVagones){
    for(int i = 0; i<maximoVagones; i++){
        if(tren[i] != NULL){
            printf("Vagon %d\n", i);
            Vagon vagon = tren[i];
            while(vagon != NULL){
                printf("Asiento %d : %s \n", vagon->num, vagon->nombre);
                vagon = vagon ->sig;
            }
        }
    }
}

/// 1.5 pts 
int salePasajero(Vagon * tren, unsigned numeroVagon, unsigned numeroAsiento){

}

/// 1.75 pts 
int intercambianAsientos(Vagon * tren, unsigned numeroVagon1, unsigned numeroAsiento1,unsigned numeroVagon2, unsigned numeroAsiento2){

}

/// 1 pt 
void ultimaParada(Vagon * tren, unsigned maximoVagones){

}

/// 1.5 pts 
void almacenarRegistroPasajeros(char *filename, Vagon * tren, unsigned maximoVagones){

}

/// 1.5 pts 
void importarPasajerosVagon(char *filename, Vagon * tren,unsigned numeroVagon){

}
