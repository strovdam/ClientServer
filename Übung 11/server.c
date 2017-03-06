#include <stdio.h>
#include <stdlib.h>
#include <sys/socket.h>
#include <sys/stat.h>
#include <netinet/in.h>
#include <string.h>
#include <unistd.h>
#include "../clientserversockets.h"

int startsGet(char buffer[]);
int endsExt(char buffer[]);
void getFileName(char buffer[], char* out);
off_t fsize(const char *filename);
//-----------------------------------------------------------------Program------------------------------------------------
int main(int argc, char** argv) {
	srand(time(NULL));
	serversocket ss;
	ss = newServerSocket();
	ss.init(&ss, "192.168.2.10", 4242);
	if(ss.start(&ss, 10) != 0)
		printf("Error\n");
	
	clientsocket cs = ss.accept(&ss);
	printf("Client connected\n");
	
	
	char bufferIn[1024];
	cs.read(&cs, bufferIn);
	
	char* msg = (char*) malloc(sizeof(char)*1000);
	
	if(startsGet(bufferIn)) {
		if(endsExt(bufferIn)) {
			char* file = (char*) malloc(sizeof(char)*1000);
			getFileName(bufferIn, file);
			printf("%s", file);
			if( access( file, F_OK ) != -1 ) {
				FILE *fp;
				char buff[fsize(file)];

				fp = fopen(file, "r");
				fread(buff, fsize(file), 1, (FILE*)fp);
		
				fclose(fp);
				
				buff[fsize(file)] = '\0';
			
				
				cs.send(&cs, buff);
			} else {
				strcpy(msg, "The file does not exist\n\0");
				cs.send(&cs, msg);
			}
		} else {
			strcpy(msg, "Please follow the pattern \"GET <filename>.ext\" (.ext not used)\n\0");
			cs.send(&cs, msg);
		}
	} else {
		strcpy(msg, "Please follow the pattern \"GET <filename>.ext\" (GET not used)\n\0");
		cs.send(&cs, msg);
	}

	ss.close(&ss);
	return 0;
}

int startsGet(char buffer[]) {
	if(buffer[0] == 'G') {
		if(buffer[1] == 'E') {
			if(buffer[2] == 'T') {
				if(buffer[3] == ' ')
					return 1;
			}
		}
	}
	return 0;
}

int endsExt(char buffer[]) {
	int i;
	int startIdx = -1;
	char end[4];
	strcpy(end, ".ext");
	for(i = 0; i < 1024; i++) {
		if(startIdx != -1) {
			int n = i - startIdx;
			if(buffer[i] != end[n]) {
				startIdx = -1;
			} else {
				if(n == 3) {
					return 1;
				}
			}
		} else if(buffer[i] == '.') {
			startIdx = i;
		}
	}
	return 0;
}

void getFileName(char buffer[], char* out) {
	int i;
	strcpy(out, "");
	
			
	int startIdx = -1;
	char end[4];
	strcpy(end, ".ext");
	for(i = 4; i < strlen(buffer); i++) {
		out[i - 4] = buffer[i];
		if(startIdx != -1) {
			int n = i - startIdx;
			if(buffer[i] != end[n]) {
				startIdx = -1;
			} else {
				if(n == 3) {
					buffer[i+1] = '\0';
					break;
				}
			}
		} else if(buffer[i] == '.') {
			startIdx = i;
		}
	}
}

off_t fsize(const char *filename) {
    struct stat st; 

    if (stat(filename, &st) == 0)
        return st.st_size;

    return -1; 
}
