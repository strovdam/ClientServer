#include <stdio.h>
#include <stdlib.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <string.h>
#include "../clientserversockets.h"

typedef char string[1024];
//-----------------------------------------------------------------Program------------------------------------------------
int main(int argc, char** argv) {
	srand(time(NULL));
	serversocket ss;
	ss = newServerSocket();
	ss.init(&ss, "127.0.0.1", 4242);
	if(ss.start(&ss, 10) == 0)
		printf("Everything ok\n");
	else
		printf("Error\n");
	
	clientsocket cs = ss.accept(&ss);
	printf("Client connected\n");
	
	
	string places[5];
	strcpy(places[0], "Brixen");
	strcpy(places[1], "Bozen");
	strcpy(places[2], "Klausen");
	strcpy(places[3], "Sterzing");
	strcpy(places[4], "Meran");
	float danger[5];
	int i;
	for(i = 0; i < 5; i++) {
		danger[i] = rand() & 100;
	}
	
	char* message = (char*)malloc(sizeof(char)*1000);
	strcpy(message, "");
	for(i = 0; i < 5; i++) {
		strcat(message, places[i]);
		strcat(message, " - ");
		char* temp = (char*)malloc(sizeof(char)*4);
		sprintf(temp, "%.2f", danger[i]); 
		strcat(message, temp);
		strcat(message, "%\n");
		free(temp);
	}
	strcat(message, "\0");
	
	
	cs.send(&cs, message);

	ss.close(&ss);
	return 0;
}
