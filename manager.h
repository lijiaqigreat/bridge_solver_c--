#ifndef MANAGER_H
#define MANAGER_H
#include "base_type.h"
#include "queue.h"
#include "table.h"
#include "BridgeInfo.h"

#define TABLE_SIZE 128

typedef struct{
    TaskQueue queue;
    CostTable table;
    gpointer *min;
    const BridgeInfo *bridge;
} Manager;

Manager *manager_init(Manager *manager,const BridgeInfo *bridge,int queueSize2,int queueSize3,float tableLimit);

void manager_rebase(Manager * manager,const BridgeInfo *bridge);

void main_work(Manager *manager);



#endif
