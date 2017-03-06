#include <stdio.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <string.h>
#include "../clientserversockets.h"


int main(int argc, char** argv) {
	connectorsocket cs = newConnectorSocket();
	cs.connect(&cs, "127.0.0.1", 4242);
	
	printf("Please choose your function [ 1 = random | 2 = negative random ]\n");
	int x;
	scanf("%d", &x);
	
	if(x < 1 || x > 2) {
		printf("Wrong input");
	} else {
		char msgBuffer[1024];
		msgBuffer[0] = (char)(48 + x);
		cs.send(&cs, msgBuffer);
		char buffer[1024];
		cs.read(&cs, buffer);
		printf("Received [%s] from server\n", &buffer[0]);
	}
	
	cs.close(&cs);
	
	return 0;
}

