#ifndef MANAGER_H
#define MANAGER_H
#include "base_type.h"
#include "BridgeInfo.h"
/*
 * structure of data:
 *  cost: 8
 *  positionHintB: size1
 * cost is EMPTY_VALUE if the slot is empty
 */
typedef struct {
    /**
     * size of each element
     */
    int size1;
    /**
     * number of elements in each block
     */
    int size2;
    /**
     * number of blocks
     */
    int size3;
    /**
     * current number of blocks
     */
    int size3_;
    /**
     * mem has length of size3
     * key in data[i] are less than interval[t] for all i<=t
     */
    Double *interval;
    /**
     * mem has length of size3
     * dataSize[t] is the actual number of elements in data[t]
     */
    int* dataSize;
    /**
     * array of arrays storing blocks
     * first size3_ array are valid, others should not be accessed
     */
    gpointer* data;
} TaskQueue;

#define EMPTY_VALUE -1.
#define TABLE_SIZE 128
typedef struct {
    int size1;
    int size2;
    int size2_;
    float limit;
    gpointer data;
} CostTable;

typedef struct{
    TaskQueue queue;
    CostTable table;
    const BridgeInfo *bridge;
} Manager;

TaskQueue *queue_init(TaskQueue *f,int size1,int size2,int size3);
gchar queue_insert(TaskQueue *queue,gpointer element);
gpointer queue_pull(TaskQueue *queue);
void queue_free(TaskQueue *queue);

CostTable *table_init(CostTable *f,int size1,int size2,float limit);
Dollar table_peek(const CostTable *table,gconstpointer element);
Dollar table_insert(CostTable *table,gconstpointer element, Dollar cost);
int table_hash(gconstpointer element,int size1,int size2);
void table_free(CostTable *table);

Manager *manager_init(Manager *manager,const BridgeInfo *bridge,int queueSize2,int queueSize3,float tableLimit);

void manager_rebase(Manager * manager,const BridgeInfo *bridge);




#endif
