#include <string.h>
#include <stdio.h>
#include "rc4.h"
#define KEY "QuenchHollow"
#define KEYLEN 12


void setup_rc4(TRC4State *state, const char *key, int keylen)
{
    unsigned int tmp,x,y;

	// use only first 256 characters of key 
    if (keylen > 256) 
	keylen = 256;

    // make RC4 perm and shuffle 
    for (x = 0; x < 256; x++)
        state->buf[x] = x;
    state->x=state->y=0;
 
    for (x = y = 0; x < 256; x++) {
        y = (y + state->buf[x] + key[x % keylen]) & 255;
        tmp = state->buf[x]; 
	state->buf[x] = state->buf[y]; 
	state->buf[y] = tmp;
    }
    state->x = x;
    state->y = y;
}

unsigned long endecrypt_rc4_state(unsigned char *buf, unsigned long len, TRC4State *state)
{
    int x=state->x;
    int y=state->y;
    int i;
    unsigned char *s=(unsigned char*)state;
    unsigned char tmp;
    for(i=0;i<len;i++){
        x=(x+1)&255;
        y=(y+s[x])&255;
        tmp=s[x];
        s[x]=s[y];
        s[y]=tmp;
        buf[i]^=s[(s[x]+s[y])&255];
    }
    state->x=x;
    state->y=y;
    return 0;
}

unsigned long endecrypt_rc4(unsigned char *buf, unsigned long len){
  TRC4State state;
  //printf("key size: %lu\n",strlen(KEY));
  setup_rc4(&state, KEY, KEYLEN);
  return endecrypt_rc4_state(buf, len, &state);
}



/*
int main(void)
{
	TRC4State state;
	unsigned char buf[100000];
	static char key[] = "QuenchHollow";
	FILE *f;
	size_t n;

	setup_rc4(&state, key, strlen(key));
	f = fopen("Eg/2014/test2.bdc", "rb");
	if (!f) {
		fprintf(stderr, "can't open input file\n");
		return 1;
	}
	n = fread(buf, 1, sizeof buf, f);
	fclose(f);
	buf[n] = '\0';

	endecrypt_rc4(buf, n, &state);
	printf("decrypted text:%s\n", buf);
	return 0;
}
*/
