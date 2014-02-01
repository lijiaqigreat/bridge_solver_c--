#include "base_type.h"
#include "BridgeInfoIO.h"
#include "optimizer.h"
#include <time.h>
int test1();
int main(){
    const BridgeInfo* f=loadBridge("Eg/2014/test2.bdc");
    for(int t=0;t<MAX_TYPE;t++){
        const TypeB* type=&f->types[t];
        printf("%d %d %d\n",type->materialIndex,type->shapeIndex,type->sizeIndex);
    }
}

int test1(){
 FILE* file=fopen("optimizeTask.dat","rb");
 OptimizeTask task;
 fread(&task,sizeof(OptimizeTask),1,file);
 task.capCost=1000000;
 for(int t=0;t<task.typeSize;t++){
  printf("test[t]: %llx\n",task.typeTestMask[t]);
 }
 printf("memberSize: %d\n",task.memberSize);
 TypeHintCostB f;
 size_t time=clock();
 f=optimize(task,true);
 for(int t=0;t<1000;t++){
  f=optimize(task);
 }
 time = clock() - time;
 printf ("It took me %d clicks (%f seconds).\n",(int)time,((float)time)/CLOCKS_PER_SEC);
 size_t t2=clock();
 printf("starting!\n");
 for(int t=0;t<task.memberSize;t++){
  printf("%d ",f.member[t]);
 }
 printf("\n");
}
