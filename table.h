#ifndef TABLE_H
#define TABLE_H

#include "base_type.h"

#define GET_VALUE(p) (*(Dollar*)((p)+size1))

typedef struct {
    int size1;
    int size2;
    int size2_;
    float limit;
    gpointer data;
} CostTable;


CostTable *table_init(CostTable *f,int size1,int size2,float limit);

Dollar table_peek(const CostTable *table,gconstpointer element);

Dollar table_insert(CostTable *table,gconstpointer element, Dollar cost);

int table_hash(gconstpointer element,int size1,int size2);

void table_free(CostTable *table);

void table_print(CostTable *table);


#endif
