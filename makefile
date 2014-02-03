CC=gcc
CFLAGS=-c -Wall

all: bridge_solver
bridge_solver: main.o rc4.o analyzer.o optimizer.o BridgeInfoIO.o Result.o type.o
	$(CC) *.o -o bridge_solver -lm
main.o: main.c
	$(CC) $(CFLAGS) main.c -o main.o
rc4.o: rc4.c
	$(CC) $(CFLAGS) rc4.c -o rc4.o
analyzer.o: analyzer.c
	$(CC) $(CFLAGS) analyzer.c -o analyzer.o
optimizer.o: optimizer.c
	$(CC) $(CFLAGS) optimizer.c -o optimizer.o
BridgeInfoIO.o: BridgeInfoIO.c
	$(CC) $(CFLAGS) BridgeInfoIO.c -o BridgeInfoIO.o
Result.o: Result.c
	$(CC) $(CFLAGS) Result.c -o Result.c
type.o: type.c
	$(CC) $(CFLAGS) type.c -o type.o

