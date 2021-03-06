CC=gcc
CFLAGS=-c -g -pg -Wall `pkg-config --cflags glib-2.0`
LIBS=-L/usr/lib -lm -lglib-2.0

all: bridge_solver
bridge_solver: main.o rc4.o analyzer.o optimizer.o BridgeInfoIO.o Result.o type.o manager.o table.o queue.o log.o
	$(CC) -g -pg *.o -o bridge_solver $(LIBS) 
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
	$(CC) $(CFLAGS) Result.c -o Result.o
type.o: type.c
	$(CC) $(CFLAGS) type.c -o type.o
manager.o: manager.c
	$(CC) $(CFLAGS) manager.c -o manager.o
table.o: table.c
	$(CC) $(CFLAGS) table.c -o table.o
queue.o: queue.c
	$(CC) $(CFLAGS) queue.c -o queue.o
log.o: log.c
	$(CC) $(CFLAGS) log.c -o log.o
	

clean:
	rm *.o

