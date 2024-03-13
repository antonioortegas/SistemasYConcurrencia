#include <stdio.h>
#include <stdlib.h>
#include "gestion_memoria.h"

#define MAX 1000

/* Crea la estructura utilizada para gestionar la memoria disponible.
 * Inicialmente, s�lo un nodo desde 0 a MAX
*/
void crear(T_Manejador* manejador){
    *manejador = (T_Manejador)malloc(sizeof(struct T_Nodo));
    (*manejador)->inicio=0;
    (*manejador)->fin=MAX-1;
    (*manejador)->sig=NULL;
}

/* Destruye la estructura utilizada (libera todos los nodos de la lista.
 * El par�metro manejador debe terminar apuntando a NULL
*/
void destruir(T_Manejador* manejador) {
    T_Manejador aux = NULL;
    while(*manejador!=NULL){
        aux = *manejador;
        *manejador = (*manejador)->sig;
        free(aux);
    }
}

void eliminarNodoIntermedio(T_Manejador *manejador){
    T_Manejador aux = *manejador;
    *manejador = (*manejador)->sig;
    free(aux);
}

/* Devuelve en �dir� la direcci�n de memoria �simulada� (unsigned) donde comienza
 * el trozo de memoria continua de tama�o �tam� solicitada.
 * Si la operaci�n se pudo llevar a cabo, es decir, existe un trozo con capacidad
 * suficiente, devolvera TRUE (1) en �ok�; FALSE (0) en otro caso.
 */
void obtener(T_Manejador *manejador, unsigned tam, unsigned* dir, unsigned* ok){
    *ok = 0;
    while(*manejador!=NULL){
        if(((*manejador)->fin - (*manejador)->inicio + 1) >= tam){
            //asigno memoria, true a ok, y el tamaño a tam y salgo
            *ok = 1;
            *dir = (*manejador)->inicio;
            (*manejador)->inicio += tam;
            if((*manejador)->inicio == (*manejador)->fin){
                eliminarNodoIntermedio(&manejador);
            }
            break;
        } else{
            *manejador = (*manejador)->sig;
        }
    }
}



/* Muestra el estado actual de la memoria, bloques de memoria libre */
void mostrar (T_Manejador manejador){
    while(manejador!=NULL){
        printf("Desde %d a %d: Libre\n", manejador->inicio, manejador->fin);
        manejador = manejador->sig;
    }
}


/* Devuelve el trozo de memoria continua de tama�o �tam� y que
 * comienza en �dir�.
 * Se puede suponer que se trata de un trozo obtenido previamente.
 */
void devolver(T_Manejador *manejador, unsigned tam, unsigned dir) {
    
    T_Manejador nuevo = malloc(sizeof(struct T_Nodo));
    nuevo->inicio = dir;
    nuevo->fin = dir + tam -1;
    while((*manejador)!=NULL ){
        if((*manejador)->inicio > nuevo->inicio){
            nuevo->sig = (*manejador)->sig;
            (*manejador)->sig = nuevo;
            if((*manejador)->sig != NULL){
                if((*manejador)->fin == (*manejador)->sig->inicio){
                //combinar por la derecha
                eliminarNodoIntermedio(&((*manejador)->sig));
                }
            }
            if((*manejador)->fin == (*manejador)->sig->inicio -1){
                //combinar por la izq
                eliminarNodoIntermedio(&manejador);
            }
            
            break;
        } else {
            *manejador = (*manejador)->sig;
        }
    } 
    
}

