#include "base_type.h"
#include "BridgeInfoIO.h"
#include "Result.h"
#include "analyzer.h"
#include "optimizer.h"
#include <math.h>
#include <glib.h>
#include "manager.h"
typedef __uint128_t BigInt;
int test1();
int test2();
int test3();
int test4();
int test5();
int main(){
    test5();
    //printf("%lf\n",-G_MAXDOUBLE);
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
    
    OptimizeTask task;

    int freeJointSize=f->totalJointSize-f->fixedJointSize;
    int hintSize=TYPE_HINT_COST_SIZE(f->memberSize);

    gpointer element=g_malloc0(hintSize+freeJointSize);
    TypeHintCostB *thc=(TypeHintCostB*)element;

    memcpy(thc,&f->typeHint,hintSize);

    int n=analyze(result,&task,f,element);
    printf("return: %d\n",n);
    n=optimize(thc, &task,TRUE);
    
    int t;
    for(t=0;t<33;t++){
        //printf("joint %3d:%4d|%4d\n",t,f->positionHint.xy[t*2],f->positionHint.xy[t*2+1]);
    }
    for(t=0;t<62;t++){
        const TypeB* type=&f->types[f->typeHint.bundle[f->typeHint.member[t]]];
        const TypeB* type2=&f->types[thc->bundle[thc->member[t]]];
        Double max=result->maxForce[t];
        Double min=-result->minForce[t];
        Double max2=type->tensionStrength;
        Double min2=getCompressionStrength(type,result->memberLength[t]);
        printf("member %3d:%2d|%2d|%10.2lf|%10.2lf|%10.2lf|%10.2lf|%2.2lf|%2.2lf|%2.3lf|%5.2lf|%s|%s\n",t,f->memberLinks[t].j1,f->memberLinks[t].j2,max,min,max2,min2,max/max2,min/min2,result->memberLength[t],type2->cost,type->name,type2->name);
    }
    for(t=0;t<10;t++){
        printf("fixed: %d\n",f->fixedIndex[t]);
    }

    free((void*)f);
    free(result);
    free(element);
    return 0;
}
#define KEY(p) (*(Double*)(p))
int test3(){
    TaskQueue *queue=queue_init(NULL,sizeof(Double),20,10);
    int t;
    Double tmp;
    for(t=0;t<1000;t++){
        tmp=g_random_double();
        queue_insert(queue,&tmp);
    }
    for(t=0;t<queue->size3_;t++){
        Double a=queue->interval[t];
        int size=queue->dataSize[t];
        int t2;
        
        printf("block %3d, size: %d, interval: %lf\n",t,size,a);
        for(t2=0;t2<size;t2++){
            printf("%lf ",KEY(queue->data[t]+queue->size1*t2));
        }
        printf("\n");
    }

    return 0;
}
int test4(){
    CostTable *table=table_init(NULL,sizeof(Double),16,0.5);
    int size=100;
    int t;
    Double *table2=(Double*)malloc(size*2*sizeof(Double));
    Double tmp1;
    Double tmp2;
    printf("size: %d,%d\n",(int)sizeof(Double),(int)sizeof(Dollar));
    for(t=0;t<size;t++){
        tmp1=g_random_double();
        tmp2=g_random_double();
        table_insert(table,&tmp1,tmp2);
        table2[2*t]=tmp1;
        table2[2*t+1]=tmp2;
        int i=table_hash(&tmp1,8,table->size2);
        printf("%1.5f,%1.5f,%1.5f,%1.5f,%d\n",tmp1,tmp2,*(Double*)(table->data+16*i),*(Double*)(table->data+16*i+8),i);
    }

    for(t=0;t<size;t++){
        tmp1=table2[2*t];
        tmp2=table_peek(table,&tmp1);
        printf("%1.5f,%1.5f,%1.5f\n",tmp1,tmp2,table2[2*t+1]);
    }
    return 0;
}
int test5(){
    const BridgeInfo* f=loadBridge("Eg/2014/test1.bdc");
    Manager *manager=manager_init(NULL,f,16,4,0.5);
    int t;
    main_work(manager);
    main_work(manager);
    printf("starting!\n");
    for(t=0;t<33;t++){
        //printf("joint %3d:%4d|%4d\n",t,f->positionHint.xy[t*2],f->positionHint.xy[t*2+1]);
    }
    TypeHintCostB *thc=(TypeHintCostB*)manager->min;
    for(t=0;t<62;t++){
        const TypeB* type=&f->types[f->typeHint.bundle[f->typeHint.member[t]]];
        const TypeB* type2=&f->types[thc->bundle[thc->member[t]]];
        printf("member %3d:%2d|%2d|%s|%s\n",t,f->memberLinks[t].j1,f->memberLinks[t].j2,type->name,type2->name);
    }
    printf("size: %d,%d\n",manager->queue.size3,manager->table.size2_);

    free((void*)f);
    printf("finished!\n");
    return 0;

}
