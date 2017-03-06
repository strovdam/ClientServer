#include "clientserversockets.h"

//---------------------------------------------------------------Classes---------------------------------------------------

int serversocket_init(serversocket *this, char* address, int port) {
	(*this).socket = socket(PF_INET, SOCK_STREAM, 0);
	struct sockaddr_in serverAddr;
	serverAddr.sin_family = AF_INET;
    serverAddr.sin_port = htons(port);
    serverAddr.sin_addr.s_addr = inet_addr(address);
    int optval = 1;
	setsockopt((*this).socket, SOL_SOCKET, SO_REUSEADDR, &optval, sizeof(optval));
    memset(serverAddr.sin_zero, '\0', sizeof serverAddr.sin_zero);
    bind((*this).socket, (struct sockaddr *) &serverAddr, sizeof(serverAddr));
	return 1;
}

int serversocket_start(serversocket *this, int maxConnections) {
	int l = listen((*this).socket, maxConnections);
	if(l == 0)
		return 0;
	else
		return -1;
}

void serversocket_close(serversocket *this) {
	close((*this).socket);
}

clientsocket serversocket_accept(serversocket *this) {
	struct sockaddr_storage serverStorage;
    socklen_t addr_size = sizeof serverStorage;

    int clientSocket = accept((*this).socket, (struct sockaddr *) &serverStorage, &addr_size);

	clientsocket cs;
	cs = newClientSocket();
	cs.socket = clientSocket;
	return cs;
}

void clientsocket_send(clientsocket *this, char* value) {
	char buffer[1024];
	strcpy(buffer, value);
	send((*this).socket, buffer, strlen(value) + 1, 0);
}

void clientsocket_read(clientsocket *this, char buffer[]) {
	recv((*this).socket, buffer, 1024, 0);
}

int connectorsocket_connect(connectorsocket *this, char* address, int port) {
	(*this).socket = socket(PF_INET, SOCK_STREAM, 0);
	struct sockaddr_in serverAddr;
	serverAddr.sin_family = AF_INET;
    serverAddr.sin_port = htons(port);
    serverAddr.sin_addr.s_addr = inet_addr(address);
    memset(serverAddr.sin_zero, '\0', sizeof serverAddr.sin_zero);
    
    socklen_t addr_size = sizeof serverAddr;
	connect((*this).socket, (struct sockaddr *) &serverAddr, addr_size);
	return 1;
}

void connectorsocket_read(connectorsocket *this, char buffer[]) {
	recv((*this).socket, buffer, 1024, 0);
}

void connectorsocket_send(connectorsocket *this, char* value) {
	char buffer[1024];
	strcpy(buffer, value);
	send((*this).socket, buffer, strlen(value) + 1, 0);
}

void connectorsocket_close(connectorsocket *this) {
		close((*this).socket);
}
//---------------------------------------------------------------Constructors-----------------------------------------------
serversocket newServerSocket() {
	serversocket s;
	s.init = &serversocket_init;
	s.start = &serversocket_start;
	s.accept = &serversocket_accept;
	s.close = &serversocket_close;
	return s;
}

clientsocket newClientSocket() {
	clientsocket s;
	s.send = &clientsocket_send;
	s.read = &clientsocket_read;
	return s;
}

connectorsocket newConnectorSocket() {
	connectorsocket s;
	s.connect = &connectorsocket_connect;
	s.read = &connectorsocket_read;
	s.send = &connectorsocket_send;
	s.close = &connectorsocket_close;
	return s;
}

