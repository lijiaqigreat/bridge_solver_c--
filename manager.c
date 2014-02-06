#include "manager.h"
#include "Result.h"
#include "analyzer.h"
#include "optimizer.h"
#include <string.h>
#include <stdlib.h>




Manager *manager_init(Manager *f,const BridgeInfo *bridge,int queueSize2,int queueSize3,float tableLimit){
    printf("test1!\n");
    if(f==NULL){
        f=g_new0(Manager,1);
    }
    printf("test!\n");
    f->bridge=bridge;
    //freeJointSize
    int freeJointSize=bridge->totalJointSize-bridge->fixedJointSize;
    int hintSize=TYPE_HINT_COST_SIZE(bridge->memberSize);
    int queueSize1=hintSize+freeJointSize;

    printf("test!\n");
    queue_init(&f->queue,queueSize1,queueSize2,queueSize3);
    table_init(&f->table,freeJointSize,TABLE_SIZE,tableLimit);
    printf("test!\n");

    gpointer task=g_malloc(queueSize1);
    f->min=task;
    //set typeHint
    memcpy(task,&bridge->typeHint,hintSize);
    //set positionHint
    memset(task+hintSize,0,freeJointSize);

    queue_insert(&f->queue,task);
    queue_print(&f->queue);
    printf("test1!\n");

    return f;
}
#define UPDATE_TASK(value, b2, tmp, tmp2, manager, hintSize) \
        *tmp2=b2; \
        value=table_peek(&manager->table,tmp+hintSize); \
        if(value==EMPTY_VALUE){ \
            printf("inserting %d\n",t);\
            queue_insert(&manager->queue,tmp); \
            printf("inserted  %d\n",t);\
        }else{\
        }

int manager_update(Manager *manager,gconstpointer task,int taskSize){
    int freeJointSize=manager->bridge->totalJointSize-manager->bridge->fixedJointSize;
    int hintSize=TYPE_HINT_COST_SIZE(manager->bridge->memberSize);
    int t;
    int tt;
    int count=0;
    Dollar value;
    for(tt=0;tt<taskSize;tt++){
        value=*(Dollar*)task;
        if(value==EMPTY_VALUE){
            continue;
        }
        if(value<*(Dollar*)(manager->min)){
            memcpy(manager->min,task,hintSize+freeJointSize);
        }
        value=table_insert(&manager->table,task+hintSize,value);
        if(value!=EMPTY_VALUE){
            g_assert(value==*(Dollar*)task);
            continue;
        }
        value=*(Dollar*)task;
        printf("update adding seed!\n");
        
        gpointer tmp=g_malloc(freeJointSize+hintSize);
        Byte *tmp2=(Byte*)(tmp+hintSize);
        memcpy(tmp,task,hintSize+freeJointSize);
        for(t=0;t<freeJointSize;t++){
            printf("seed!\n");
            Byte b=*(tmp2);
            Byte b2;
            b2=b^((b^(b+1))&15);
            UPDATE_TASK(value,b2,tmp,tmp2,manager,hintSize);
            b2=b^((b^(b-1))&15);
            UPDATE_TASK(value,b2,tmp,tmp2,manager,hintSize);
            b2=b^((b^(b+16))&240);
            UPDATE_TASK(value,b2,tmp,tmp2,manager,hintSize);
            b2=b^((b^(b-16))&240);
            UPDATE_TASK(value,b2,tmp,tmp2,manager,hintSize);
            printf("seed!\n");
            *tmp2=b;
            printf("seed!\n");

            tmp2++;
        }
        task+=hintSize+freeJointSize;
    }
    return 0;
}

void main_work(Manager *manager){
    printf("start work\n");
    int freeJointSize=manager->bridge->totalJointSize-manager->bridge->fixedJointSize;
    int hintSize=TYPE_HINT_COST_SIZE(manager->bridge->memberSize);
    gpointer task=queue_pull(&manager->queue);
    gpointer task_=task;
    OptimizeTask otask;
    Result* result=(Result*)malloc(sizeof(Result));
    printf("end cost:%lf\n",*(Dollar*)(task+hintSize+freeJointSize));
    int count=0;
    printf("start work\n");
    while(*(Dollar*)task!=EMPTY_VALUE){
        printf("in work!\n");
        int n=analyze(result,&otask,manager->bridge,task);
        printf("return: %d\n",n);
        n=optimize(task, &otask);
        printf("return: %d\n",n);
        task+=hintSize+freeJointSize;
        count++;
    }
    printf("start work\n");
    manager_update(manager,task_,count);
}

char* print_bytes(gconstpointer p,int size){
    static char buf[10000];
    int t=0;
    for(t=0;t<size;t++){
        sprintf(buf+t*2,"%02X",GET_BYTE(p+t));
    }
    buf[size*2]=0;
    return buf;
}

