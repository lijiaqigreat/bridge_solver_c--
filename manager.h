#ifndef MANAGER_H
#define MANAGER_H
#include "base_type.h"

#define TABLE_SIZE 128

/**
 * Serve as central unit of the whole program
 */
typedef struct _Manager{
    struct _TaskQueue *queue;
    struct _CostTable *table;
    gpointer min;
    const struct _BridgeInfo *bridge;
} Manager;

Manager *manager_init(Manager *manager,const struct _BridgeInfo *bridge,int queueSize2,int queueSize3,float tableLimit);

//TODO yet to be implemented
void manager_rebase(Manager * manager,const struct _BridgeInfo *bridge);

/**
 * TODO make queue only store task before expanding to save memory
 * TODO need to decide return value
 * update taskSize tasks
 * each of mem size manager->queue->size1
 * 
 * individual task is not processed if GET_DOLLAR(task)==EMPTY_VALUE
 */
int manager_update(Manager *manager,gconstpointer task);

void update_task(struct _Result *result,OptimizeTask *otask,gpointer task,gpointer tmp1, Manager *manager,int hintSize);

/**
 * main thread function
 */
void main_work(Manager *manager);



#endif
