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
static gchar *input_path="Eg/2014/test3.bdc";
static gchar *output_path="Eg/2014/test/test3.bdc";
static gchar *log_path="LOG";
static gchar *type_path="type_data";
static gint block_size=16;
static gint queue_size=16;
static gdouble load_factor=0.75;
static gint iteration_size=50;
static GOptionEntry entries2[] =
{
    {"file", 'f',0,G_OPTION_ARG_FILENAME,&input_path,"path of input bridge", "PATH"},
    {"output",'o',0,G_OPTION_ARG_FILENAME,&output_path,"path of output bridge","PATH"},
    {"log",'d',0,G_OPTION_ARG_FILENAME,&log_path,"path of log file to write","PATH"},
    {"type-data",'t',0,G_OPTION_ARG_FILENAME,&type_path,"path of type file to reference","PATH"},
    {"block-size",'b',0,G_OPTION_ARG_INT,&block_size,"max number of tasks a worker pulls each time, N>=2","N"},
    {"queue-size",'q',0,G_OPTION_ARG_INT,&queue_size,"max number of blocks the program keeps track of, N>=2","N"},
    {"table-loadfactor",'l',0,G_OPTION_ARG_DOUBLE,&load_factor,"load factor of look up table","N"},
    {"iteration",'i',0,G_OPTION_ARG_INT,&iteration_size,"number of iterations the program tries to optimize, default and recommended value: 50, N>=1","N"},
    {NULL}
};
int test1();
int test2();
int test3();
int test4();
int test5();
int test6();
int main (int argc, char *argv[]){
    GError *error = NULL;
    GOptionContext *context;

    context = g_option_context_new ("- solver for Westpoint Bridge Design 2014");
    g_option_context_add_main_entries (context, entries2, NULL);
    if (!g_option_context_parse (context, &argc, &argv, &error))
    {
        g_print ("option parsing failed: %s\n", error->message);
        exit (1);
    }
    printf("input: %s\n",input_path);

    GIOChannel *ch=g_io_channel_new_file(log_path,"w",NULL);
    gpointer* data=g_new(gpointer,5);
    data[0]=&log_func_iochannel;
    data[1]=ch;
    data[2]=&log_func_FILE;
    data[3]=stdout;
    data[4]=NULL;
    //g_log_set_handler(G_LOG_DOMAIN,G_LOG_LEVEL_MESSAGE,&log_func_iochannel,ch);
    //g_log_set_handler(G_LOG_DOMAIN,G_LOG_LEVEL_MASK,&log_func_FILE,stdout);
    g_log_set_handler(G_LOG_DOMAIN,G_LOG_LEVEL_MASK,&log_func_multiple,data);
    test5();
    g_io_channel_unref(ch);
    free(data);
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
    const BridgeInfo* f=loadBridge(input_path,type_path);
    Result result;
    OptimizeTask task;
    if(f==NULL){
        g_critical("no bridge!!");
        return 1;
    }
    Manager *manager=manager_init(NULL,f,block_size,queue_size,load_factor);

    analyze(&result,&task,f,&f->typeHint);
    result_print(G_LOG_LEVEL_MESSAGE,&result,f,manager->min);

    int ttt=0;
    for(ttt=0;ttt<iteration_size;ttt++){
        main_work(manager);
        g_message("queue size: %d",manager->queue->size3_);
        g_message("table size: %d",manager->table->size2_);
    }

    const BridgeInfo* f2=rebaseBridge(f,manager->min);
    saveBridge(output_path,f2);


    g_message("program finished!");
    free(f);
    free(f2);

    return 0;
}
int test6(){
    const BridgeInfo* f=loadBridge(input_path,type_path);
    if(f==NULL){
        g_critical("no bridge!!");
        return 1;
    }
    int freeJointSize=f->totalJointSize-f->fixedJointSize;
    int hintSize=TYPE_HINT_COST_SIZE(f->memberSize);
    Result result;
    OptimizeTask otask;

    //the only task in the queue
    gpointer task=g_malloc(hintSize+freeJointSize);
    //set typeHint
    memcpy(task,&f->typeHint,hintSize);
    //set positionHint
    memset(task+hintSize,0,freeJointSize);

    analyze(&result,&otask,f,&f->typeHint);
    result_print(G_LOG_LEVEL_MESSAGE,&result,f,task);
    free(task);
    free(f);
}
