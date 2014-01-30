#include <stdlib.h>
#include <string.h>
#include "BridgeInfo.h"
#include "rc4.h"

#define BUFLEN 100000

unsigned char* readFile(const char* path){
  FILE *fp;
  size_t n;
  
  fp = fopen(path, "rb");
  if (!fp) {
    fprintf(stderr, "can't open input file\n");
    return 0;
  }
  fseek(fp, 0L, SEEK_END);
  n = ftell(fp);
  unsigned char* buf=(unsigned char*) malloc(n+1L);
  fseek(fp, 0L, SEEK_SET);
  fread(buf, 1, n, fp);
  endecrypt_rc4(buf,n);
  fclose(fp);
  buf[n] = '\0';
  return buf;
}
int writeFile(const char* path, unsigned char* buf){
  FILE *fp;
  size_t n;
  

  fp = fopen(path, "wb");
  if (!fp) {
    fprintf(stderr, "can't open output file\n");
    return 1;
  }
  n = strlen((char*)buf);
  endecrypt_rc4(buf,n);
  fwrite(buf, 1, n, fp);
  fclose(fp);
  return 0;
}

int loadBridge(BridgeInfo* b,const char* path){
  /* encrypt buffer */
  unsigned char* buf=readFile(path);
  printf("decrypted text:%s\n", buf);
  int year;
  unsigned long long conditionID;
  int jointSize;
  int memberSize;
  sscanf((const char*)buf,"%4d%10llu%2d%3d",&year,&conditionID,&jointSize,&memberSize);
  

  
  return 0;
}
int main(){
  loadBridge(0,"Eg/2014/test2.bdc");
}
