#include <stdio.h>
#include <stdlib.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <string.h>

#ifndef CLIENTSERVERSOCKETS_H   /* Include guard */
#define CLIENTSERVERSOCKETS_H


typedef struct clsk {
	int socket;
	
	void (*send)(struct clsk *this, char* text);
	void (*read)(struct clsk *this, char buffer[]);
} clientsocket;

typedef struct srsk {
	int socket;
	
	int (*init)(struct srsk *this, char* ip, int port);
	int (*start)(struct srsk *this, int max);
	clientsocket (*accept)(struct srsk *this);
	void (*close)(struct srsk *this);
} serversocket;

typedef struct sk {
	int socket;
	
	int (*connect)(struct sk *this, char* ip, int port);
	void (*read)(struct sk *this, char buffer[]);
	void (*send)(struct sk *this, char buffer[]);
	void (*close)(struct sk *this);
	
} connectorsocket;

serversocket newServerSocket();
clientsocket newClientSocket();
connectorsocket newConnectorSocket();
#endif //CLIENTSERVERSOCKETS_H
