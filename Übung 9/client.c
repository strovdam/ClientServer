#include <stdio.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <string.h>
#include "../clientserversockets.h"




//-------------------------------------------------------------------Program----------------------------------------------------
int main(int argc, char** argv) {
	connectorsocket cs = newConnectorSocket();
	cs.connect(&cs, "127.0.0.1", 4242);
	char buffer[1024];
	cs.read(&cs, buffer);
	printf("%s\n", &buffer[0]);
	
	return 0;
}
