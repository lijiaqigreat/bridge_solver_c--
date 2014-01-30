#ifndef _RC4_H
#define _RC4_H

typedef struct TRC4State_t {
	unsigned char buf[256];
	int x, y;
} TRC4State;

/* rc4.c */
void setup_rc4(TRC4State *state, const char *key, int keylen);
unsigned long endecrypt_rc4(unsigned char *buf, unsigned long len, TRC4State *state);
unsigned long endecrypt_rc4(unsigned char *buf, unsigned long len);

#endif
