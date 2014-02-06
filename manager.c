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

    f->queue=queue_init(NULL,queueSize1,queueSize2,queueSize3);
    f->table=table_init(NULL,freeJointSize,TABLE_SIZE,tableLimit);

    //the only task in the queue
    gpointer task=g_malloc(queueSize1);
    f->min=task;

    //set typeHint
    memcpy(task,&bridge->typeHint,hintSize);
    //set positionHint
    memset(task+hintSize,0,freeJointSize);

    queue_insert(f->queue,task);

    return f;
}

int manager_update(Manager *manager,gconstpointer task){
    Dollar value=GET_DOLLAR(task);
    //no need to update
    int freeJointSize=manager->bridge->totalJointSize-manager->bridge->fixedJointSize;
    int hintSize=TYPE_HINT_COST_SIZE(manager->bridge->memberSize);
    //update record
    if(value<GET_DOLLAR(manager->min)){
        memcpy(manager->min,task,hintSize+freeJointSize);
        printf("new record! %lf\n",value);
    }
    //update table
    value=table_insert(manager->table,task+hintSize,value);
    //table key exist
    if(value!=EMPTY_VALUE){
        //two cost should be the same
        g_assert(value==*(Dollar*)task);
        return 1;
    }
    //update queue
    queue_insert(manager->queue,task);
    return 0;

}

/*
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

*/
void update_task(Result *result,OptimizeTask *otask,gpointer task,gpointer tmp1, Manager *manager,int hintSize){
    Dollar value=table_peek(manager->table,tmp1+hintSize);
    if(value==EMPTY_VALUE){
        memcpy(tmp1,task,hintSize);
        int n=analyze(result,otask,manager->bridge,tmp1);
        n=optimize(tmp1, otask);
        manager_update(manager,tmp1);
        //queue_insert(manager->queue,tmp);
    }
}
void main_work(Manager *manager){
    printf("start work\n");
    int freeJointSize=manager->bridge->totalJointSize-manager->bridge->fixedJointSize;
    int hintSize=TYPE_HINT_COST_SIZE(manager->bridge->memberSize);

    //pointer to be moved arround
    gpointer queueTask=queue_pull(manager->queue);
    //original copy
    gpointer queueTask_=queueTask;

    OptimizeTask optimizeTask;
    Result* result=(Result*)malloc(sizeof(Result));
    gpointer tmp1=g_malloc(hintSize+freeJointSize);
    int count=0;
    Dollar value;
    while(GET_DOLLAR(queueTask)!=EMPTY_VALUE){
        //TODO smarter analysis to increase speed AND accuracy
        
        int t;
        int n;
        Byte *tmp2=(Byte*)(tmp1+hintSize);
        memcpy(tmp2,queueTask+hintSize,freeJointSize);
        for(t=0;t<freeJointSize;t++){

            Byte b=*tmp2;

            //right left up down
            *tmp2=b^((b^(b+1))&15);
            update_task(result,&optimizeTask,queueTask,tmp1,manager,hintSize);
            *tmp2=b^((b^(b-1))&15);
            update_task(result,&optimizeTask,queueTask,tmp1,manager,hintSize);
            *tmp2=b^((b^(b+16))&240);
            update_task(result,&optimizeTask,queueTask,tmp1,manager,hintSize);
            *tmp2=b^((b^(b-16))&240);
            update_task(result,&optimizeTask,queueTask,tmp1,manager,hintSize);

            //restore *tmp2
            *tmp2=b;
            tmp2++;
        }

        queueTask+=hintSize+freeJointSize;
    }
    free(tmp1);
    free(queueTask_);
    free(result);
    printf("end work\n");
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

