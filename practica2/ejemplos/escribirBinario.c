/*
 * escribirBinario.c
 *
 *  Created on: 10/3/2020
 *      Author: usuario
 */

#include <stdio.h>

int main(){

	FILE* fd;

	fd = fopen("ficheroBinario.dat","wb");

	if (fd == NULL){
		perror("Error creando ficheroBinario.dat");
	}

	int i;

	for(i=0; i<10; i++){
		fwrite(&i,sizeof(int),1,fd);
	}
	fclose(fd);
	return 0;
}
