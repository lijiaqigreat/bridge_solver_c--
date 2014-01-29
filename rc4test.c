#include <string.h>
#include <stdio.h>

typedef struct TRC4State_t {
	int x, y;
	unsigned char buf[256];
} TRC4State;

void setup_rc4(TRC4State *state, char *key, int keylen)
{
    unsigned tmp;
    int x, y;

	// use only first 256 characters of key 
    if (keylen > 256) 
		keylen = 256;

    // make RC4 perm and shuffle 
    for (x = 0; x < 256; x++)
        state->buf[x] = x;

    for (x = y = 0; x < 256; x++) {
        y = (y + state->buf[x] + key[x % keylen]) & 255;
        tmp = state->buf[x]; 
		state->buf[x] = state->buf[y]; 
		state->buf[y] = tmp;
    }
    state->x = x;
    state->y = y;

	// DEBUG: Temporary while Steve fixes bug.
	state->y = 0;
}

unsigned long endecrypt_rc4(unsigned char *buf, unsigned long len, TRC4State *state)
{
   int x, y; 
   unsigned char *s, tmp;
   unsigned long n;

   n = len;
   x = state->x;
   y = state->y;
   s = state->buf;
   while (len--) {
      x = (x + 1) & 255;
      y = (y + s[x]) & 255;
      tmp = s[x]; s[x] = s[y]; s[y] = tmp;
      tmp = (s[x] + s[y]) & 255;
      *buf++ ^= s[tmp];
   }
   state->x = x;
   state->y = y;
   return n;
}

int main(void)
{
	TRC4State state;
	unsigned char buf[100000];
	static char key[] = "QuenchHollow";
	FILE *f;
	size_t n;

	/* setup RC4 for use */
	setup_rc4(&state, key, strlen(key));
	f = fopen("Eg/2014/MyDesign.bdc", "rb");
	if (!f) {
		fprintf(stderr, "can't open input file\n");
		return 1;
	}
	n = fread(buf, 1, sizeof buf, f);
	fclose(f);
	buf[n] = '\0';

	/* encrypt buffer */
	if (endecrypt_rc4(buf, n, &state) != n) {
		printf("endecrypt error\n");
		return -1;
	}
	printf("decrypted text:%s\n", buf);
	return 0;
}
