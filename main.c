#include "base_type.h"
#include "BridgeInfoIO.h"
#include "Result.h"
#include "analyzer.h"
#include "optimizer.h"
#include <time.h>
#include <stdlib.h>
#include <math.h>
#include <glib.h>
typedef __uint128_t BigInt;
int test1();
int test2();
int main(){
    /*
    GError *error=NULL;
    GIOChannel *ch = g_io_channel_new_file("Eg/2014/test2.bdc","r",NULL);
    printf("encoding: %s",g_io_channel_get_encoding(ch));
    g_io_channel_set_encoding(ch,NULL,NULL);
    printf("encoding: %s",g_io_channel_get_encoding(ch));
    gchar *buf=NULL;
    gsize size;
    g_io_channel_read_to_end(ch,&buf,&size,&error);
    printf("size: %d\n",(int)size);
    printf("message: %d,%d,%s\n",error->code,G_FILE_ERROR_FAILED,error->message);
    printf(buf);
    
    g_io_channel_unref(ch);
    free(buf);
    */
    test2();
    return 0;
}

/*
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
*/
int test2(){
    const BridgeInfo* f=loadBridge("Eg/2014/test2.bdc");
    Result* result=(Result*)malloc(sizeof(Result));
    PositionHintB position;
    
    TypeHintCostB thc;
    OptimizeTask task;
    memset(&position, 0, sizeof(PositionHintB));
    memset(&thc, 0, sizeof(TypeHintCostB));

    int n=analyze(result,&task,f,&position,&f->typeHint);
    printf("return: %d\n",n);
    n=optimize(&thc, &task,TRUE);
    
    int t;
    for(t=0;t<33;t++){
        //printf("joint %3d:%4d|%4d\n",t,f->positionHint.xy[t*2],f->positionHint.xy[t*2+1]);
    }
    for(t=0;t<62;t++){
        const TypeB* type=&f->types[f->typeHint.bundle[f->typeHint.member[t]]];
        const TypeB* type2=&f->types[thc.bundle[thc.member[t]]];
        Double max=result->maxForce[t];
        Double min=-result->minForce[t];
        Double max2=type->tensionStrength;
        Double min2=getCompressionStrength(type,result->memberLength[t]);
        printf("member %3d:%2d|%2d|%10.2lf|%10.2lf|%10.2lf|%10.2lf|%2.2lf|%2.2lf|%2.3lf|%s|%s\n",t,f->memberLinks[t].j1,f->memberLinks[t].j2,max,min,max2,min2,max/max2,min/min2,result->memberLength[t],type->name,type2->name);
    }
    for(t=0;t<10;t++){
        printf("fixed: %d\n",f->fixedIndex[t]);
    }

    free((void*)f);
    return 0;
}
