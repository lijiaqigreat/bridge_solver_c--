#include "manager.h"
#include "Result.h"
#include "analyzer.h"
#include "optimizer.h"
#include <string.h>
#include <stdlib.h>




Manager *manager_init(Manager *f,const BridgeInfo *bridge,int queueSize2,int queueSize3,float tableLimit){
    if(f==NULL){
        f=g_new(Manager,1);
    }
    f->bridge=bridge;
    int freeJointSize=bridge->totalJointSize-bridge->fixedJointSize;
    int hintSize=TYPE_HINT_COST_SIZE(bridge->memberSize);
    int queueSize1=hintSize+freeJointSize;

    queue_init(&f->queue,queueSize1,queueSize2,queueSize3);
    table_init(&f->table,freeJointSize,TABLE_SIZE,tableLimit);

    //the only task in the queue
    gpointer task=g_malloc(queueSize1);
    f->min=task;

    //set typeHint
    memcpy(task,&bridge->typeHint,hintSize);
    //set positionHint
    memset(task+hintSize,0,freeJointSize);

    queue_insert(&f->queue,task);
    //queue_print(&f->queue);
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
    //update each task
    for(tt=0;tt<taskSize;tt++){
        value=*(Dollar*)task;
        //no need to update
        if(value==EMPTY_VALUE){
            continue;
        }
        //update record
        if(value<*(Dollar*)(manager->min)){
            memcpy(manager->min,task,hintSize+freeJointSize);
        }
        value=table_insert(&manager->table,task+hintSize,value);
        //table key exist
        if(value!=EMPTY_VALUE){
            //two cost should be the same
            g_assert(value==*(Dollar*)task);
            continue;
        }
        
        //copy of task
        gpointer tmp=g_malloc(freeJointSize+hintSize);
        //pointer different place of the same data
        Byte *tmp2=(Byte*)(tmp+hintSize);
        memcpy(tmp,task,hintSize+freeJointSize);
        //expand task
        for(t=0;t<freeJointSize;t++){
            Byte b=*(tmp2);
            Byte b2;

            //right left up down
            b2=b^((b^(b+1))&15);
            UPDATE_TASK(value,b2,tmp,tmp2,manager,hintSize);
            b2=b^((b^(b-1))&15);
            UPDATE_TASK(value,b2,tmp,tmp2,manager,hintSize);
            b2=b^((b^(b+16))&240);
            UPDATE_TASK(value,b2,tmp,tmp2,manager,hintSize);
            b2=b^((b^(b-16))&240);
            UPDATE_TASK(value,b2,tmp,tmp2,manager,hintSize);

            //restore *tmp2
            *tmp2=b;

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

    //pointer to be moved arround
    gpointer task=queue_pull(&manager->queue);
    //original copy
    gpointer task_=task;

    OptimizeTask otask;
    Result* result=(Result*)malloc(sizeof(Result));
    int count=0;
    while(*(Dollar*)task!=EMPTY_VALUE){
        //TODO smarter analysis to increase speed AND accuracy
        int n=analyze(result,&otask,manager->bridge,task);
        n=optimize(task, &otask);
        task+=hintSize+freeJointSize;
        count++;
    }
    manager_update(manager,task_,count);
}

//TODO move to different place
char* print_bytes(gconstpointer p,int size){
    static char buf[10000];
    int t=0;
    for(t=0;t<size;t++){
        sprintf(buf+t*2,"%02X",GET_BYTE(p+t));
    }
    buf[size*2]=0;
    return buf;
}

