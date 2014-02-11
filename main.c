#include "base_type.h"
#include "BridgeInfoIO.h"
#include "Result.h"
#include "analyzer.h"
#include "optimizer.h"
#include <math.h>
#include <glib.h>
#include "manager.h"
#include "queue.h"
#include "table.h"
#include "log.h"
typedef __uint128_t BigInt;
int test1();
int test2();
int test3();
int test4();
int test5();
int test6();
#define eprintf(...) printf(__VA_ARGS__)
int main(){
    GIOChannel *ch=g_io_channel_new_file("LOG","w",NULL);
    //g_log_set_handler(G_LOG_DOMAIN,G_LOG_LEVEL_MASK,&log_func_iochannel,ch);
    g_log_set_handler(G_LOG_DOMAIN,G_LOG_LEVEL_MASK,&log_func_FILE,stdout);
    test5();
    g_io_channel_unref(ch);
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
    table_print(G_LOG_LEVEL_MESSAGE,table);
    table_free(table);

    return 0;
}
int test5(){
    const BridgeInfo* f=loadBridge("Eg/2014/test3.bdc");
    Result result;
    OptimizeTask task;
    if(f==NULL){
        g_critical("no bridge!!");
        return 1;
    }
    Manager *manager=manager_init(NULL,f,128,16,0.5);

    analyze(&result,&task,f,&f->typeHint);
    result_print(G_LOG_LEVEL_MESSAGE,&result,f,manager->min);

    int ttt=0;
    for(ttt=0;ttt<100;ttt++){
        main_work(manager);
        g_message("queue size: %d",manager->queue->size3_);
        g_message("table size: %d",manager->table->size2_);
    }

    const BridgeInfo* f2=rebaseBridge(f,manager->min);
    saveBridge("Eg/2014/test/test3.bdc",f2);


    g_message("program finished!");
    free(f);
    free(f2);

    return 0;

}
int test6(){
    const BridgeInfo* f=loadBridge("Eg/2014/test2.bdc");
    int freeJointSize=f->totalJointSize-f->fixedJointSize;
    int hintSize=TYPE_HINT_COST_SIZE(f->memberSize);
    gpointer queueTask=g_malloc(freeJointSize+hintSize);
    //set typeHint
    memcpy(queueTask,&f->typeHint,hintSize);
    //set positionHint
    memset(queueTask+hintSize,0,freeJointSize);
    const BridgeInfo* f2=rebaseBridge(f,queueTask);
    printf("new bridge:\n%s\n",f2->buf);

    saveBridge("Eg/2014/test/test3.bdc",f2);
    return 0;
}
