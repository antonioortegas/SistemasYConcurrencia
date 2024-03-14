/*
 * leerTexto.c
 *
 *  Created on: 10/3/2020
 *      Author: usuario
 */

#include <stdio.h>

int main(){

	FILE* fd;

	if ((fd = fopen("ficheroBinario.dat","rb"))==NULL){
		perror("Error abriendo ficheroBinario.dat");
	}

	int valor, leidos;
	while ((leidos = fread(&valor,sizeof(int),1,fd)) > 0){
		printf("%d ", valor);
	}

	fclose(fd);
	return 0;
}

