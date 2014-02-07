#ifndef MANAGER_H
#define MANAGER_H
#include "base_type.h"
#include "queue.h"
#include "table.h"
#include "BridgeInfo.h"
#include "Result.h"

#define TABLE_SIZE 128

/**
 * Serve as central unit of the whole program
 */
typedef struct{
    TaskQueue *queue;
    CostTable *table;
    gpointer min;
    const BridgeInfo *bridge;
} Manager;

Manager *manager_init(Manager *manager,const BridgeInfo *bridge,int queueSize2,int queueSize3,float tableLimit);

//TODO yet to be implemented
void manager_rebase(Manager * manager,const BridgeInfo *bridge);

/**
 * TODO make queue only store task before expanding to save memory
 * TODO need to decide return value
 * update taskSize tasks
 * each of mem size manager->queue->size1
 * 
 * individual task is not processed if GET_DOLLAR(task)==EMPTY_VALUE
 */
int manager_update(Manager *manager,gconstpointer task);

void update_task(Result *result,OptimizeTask *otask,gpointer task,gpointer tmp1, Manager *manager,int hintSize);

/**
 * main thread function
 */
void main_work(Manager *manager);



#endif
