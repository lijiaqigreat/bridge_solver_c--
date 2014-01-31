#include <stdlib.h>
#include <string.h>
#include "BridgeInfo.h"
#include "rc4.h"


#define MAX_BUNDLE 8
#define MAX_FIXED_JOINT 20
#define MAX_MEMBER 64
#define BUFLEN 100000
#define DEADLOADFACTOR 1.25
#define LIVELOADFACTOR 2.3275

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

const BridgeInfo* loadBridge(const char* path){
  /* encrypt buffer */
  char* buf=(char*) readFile(path);
  printf("decrypted text:%s\n", buf);
  int t;
  int tt;
  int year;
  int jointSize;
  int memberSize;
  char code[10];
  unsigned long long conditionID;
  {
    char jss[3],mss[4];
    sscanf(buf,"%4d%10llu%2[ 0-9]%3[ 0-9]",&year,&conditionID,jss,mss);
    sscanf(jss,"%d",&jointSize);
    sscanf(mss,"%d",&memberSize);
  }
  buf+=19;
  for(t=0;t<10;t++){
    code[t]=conditionID%10;
    conditionID/=10;
  }
  BridgeInfo* f=(BridgeInfo*) malloc(sizeof(BridgeInfo));
  f->buf=buf;
  f->totalJointSize=jointSize;
  bool hiPier=code[9]>0;
  int pierPanelIndex=code[8]-1;
  int pierJointIndex=code[8]-1;
  bool pier=pierPanelIndex>=0;
  bool arch=code[7]==1;
  bool leftCable=code[7]==2||code[7]==3;
  bool rightCable=code[7]==3;
  int underClearance=10*code[5]+code[6];
  int overClearance=10*code[3]+code[4];
  int nPanels=10*code[1]+code[2];
  int loadCaseIndex=code[0]-1;
  int loadType=(loadCaseIndex&1)==0?0:1;
  int deckType=(loadCaseIndex&2)==0?0:1;
  int deckElevation;
  int archHeight;
  if(arch){
    deckElevation = (4 * (nPanels - 5) + underClearance);
    archHeight = underClearance;
  }else{
    deckElevation = (4 * (nPanels - 5));
    archHeight = -1;
  }
  int overMargin = (32 - deckElevation);
  int pierHeight = (pier ? deckElevation - underClearance : hiPier ? deckElevation : -1);
  int nPrescribedJoints = nPanels + 1;
  int archJointIndex=-1;
  int leftAnchorageJointIndex=-1;
  int rightAnchorageJointIndex=-1;
  if ((pier) && (!hiPier)){
    pierJointIndex = nPrescribedJoints;
    nPrescribedJoints += 1;
  }
  if (arch)
  {
    archJointIndex = nPrescribedJoints;
    nPrescribedJoints += 2;
  }
  int nAnchorages = 0;
  if (leftCable)
  {
    leftAnchorageJointIndex = nPrescribedJoints;
    nAnchorages += 1;
    nPrescribedJoints += 1;
  }
  if (rightCable)
  {
    //assert (leftCable);
    rightAnchorageJointIndex = nPrescribedJoints;
    nAnchorages += 1;
    nPrescribedJoints += 1;
  }
  int spanLength = (nPanels * 4.0D);
  int nLoadedJoints = (nPanels + 1);



  f->fixedJointSize=nPrescribedJoints;
  f->deckSize=nPanels;
  f->deckWeight= deckType == 0 ? 183.42825000000002 : 136.357;

  f->backWeight=loadType==0?181.0:120.0;
  f->frontWeight=loadType==0?44.0:120.0;
  f->backWeight*=DEADLOADFACTOR;
  f->frontWeight*=DEADLOADFACTOR;

  f->slenderness=(leftCable||rightCable)?1e100:300;
  {
    bool restraint[MAX_FIXED_JOINT*2];
    for(t=0;t<MAX_FIXED_JOINT*2;t++){
      restraint[t]=false;
    }
    restraint[0]=true;
    restraint[1]=true;
    restraint[nLoadedJoints*2-1]=true;
    if(pier){
      restraint[pierJointIndex*2]=true;
      restraint[pierJointIndex*2+1]=true;
      if(hiPier){
        restraint[0]=false;
      }
    }
    if(arch){
      restraint[nLoadedJoints*2-1]=false;
      restraint[0]=false;
      restraint[1]=false;
      restraint[archJointIndex*2+0]=true;
      restraint[archJointIndex*2+1]=true;
      restraint[archJointIndex*2+2]=true;
      restraint[archJointIndex*2+3]=true;
    }
    if(leftCable){
      restraint[leftAnchorageJointIndex*2]=true;
      restraint[leftAnchorageJointIndex*2+1]=true;
    }
    if(rightCable){
      restraint[rightAnchorageJointIndex*2]=true;
      restraint[rightAnchorageJointIndex*2+1]=true;
    }
    tt=0;
    for(t=0;t<MAX_FIXED_JOINT*2;t++){
      if(restraint[t]){
        f->fixedIndex[tt++]=t;
      }
    }
  }
  for(t=0;t<jointSize;t++){
    Int x,y;
    char xs[4],ys[4];
    sscanf(buf,"%3[ 0-9]%3[ 0-9]",xs,ys);
    sscanf(xs,"%d",&x);
    sscanf(ys,"%d",&y);
    buf+=6;
    f->positionHint.xy[t*2]=x;
    f->positionHint.xy[t*2+1]=y;
  }
  {
    int index;
    int ttt;
    tt=0;
    //valid conversion?
    Byte* memberi=f->typeHint.member;
    Int bundle[MAX_BUNDLE];
    for(t=0;t<MAX_BUNDLE;t++){
      bundle[t]=-1;
    }
    for(t=0;t<memberSize;t++){
      char j1s[3],j2s[3],i3s[3];
      Int j1,j2;
      int i1,i2,i3;
      sscanf(buf,"%2[ 0-9]%2[ 0-9]%1d%1d%2[ 0-9]",j1s,j2s,&i1,&i2,i3s);
      sscanf(j1s,"%d",&j1);
      sscanf(j2s,"%d",&j2);
      sscanf(i3s,"%d",&i3);
      buf+=8;
      f->memberLinks[t].j1=j1;
      f->memberLinks[t].j2=j2;
      index=i1*1000+i2*100+i3;
      for(ttt=0;ttt<tt&&bundle[ttt]!=index;ttt++){}
      if(ttt==tt){
        if(tt==MAX_BUNDLE){
          //exceed max bundle
          printf("!!!\n");
          return 0;
        }
        bundle[tt]=index;
        printf("test: %d %d\n",tt,index);
        tt++;
      }
      memberi[t]=ttt;
    }
    for(t=0;t<tt;t++){
      //TODO update f->typeHint.bundle
    }
  }
  f->baseCost=0;
  return f;
}
int main(){
  const BridgeInfo* f=loadBridge("Eg/2014/test2.bdc");
  printf("%d\n",f->typeHint.member[10]);
}
