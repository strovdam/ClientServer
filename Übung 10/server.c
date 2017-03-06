#include <stdio.h>
#include <stdlib.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <string.h>
#include "../clientserversockets.h"

typedef char string[1024];

void* handleClient(void *s);
void functionOne(char* buffer);
void functionTwo(char* buffer);


int main(int argc, char** argv) {
	srand(time(NULL));
	serversocket ss;
	ss = newServerSocket();
	ss.init(&ss, "127.0.0.1", 4242);
	if(ss.start(&ss, 10) != 0)
		printf("Error\n");
	int running = 1;
	clientsocket sockets[100];
	int i = 0;
	while(running == 1 && i < 100) {
		sockets[i] = ss.accept(&ss);
		printf("Connection received\n");
		pthread_t thread;
		pthread_create(&thread, NULL, handleClient, &sockets[i]);
		i++;
	}
	
	
	return 0;
}

void* handleClient(void *s) {
	clientsocket *cs = (clientsocket *)s;
	char buffer[1024];
	(*cs).read(cs, buffer);
	
	char* toSend = (char*)malloc(sizeof(char)*1000);
	if(buffer[0] == '1') {
		functionOne(toSend);
	} else if(buffer[0] == '2') {
		functionTwo(toSend);
	}
	(*cs).send(cs, toSend);
	free(toSend);	
	printf("connection over\n");
	fflush(stdout);
}

void functionOne(char* buffer) {
	int rnd = rand();
	usleep(10000000);
	sprintf(buffer, "%d", rnd);
}

void functionTwo(char* buffer) {
	int rnd = rand();
	usleep(10000000);
	sprintf(buffer, "%d", rnd*-1);
}
