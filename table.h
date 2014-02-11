#ifndef TABLE_H
#define TABLE_H

#include "base_type.h"

#define GET_VALUE(p) (*(Dollar*)((p)+size1))

/**
 * hashtable with linear probing
 *
 * element structure:
 *  positionHintB: freeJointSize
 *  cost: sizeof(Dollar)
 */
typedef struct _CostTable{
    int size1;
    int size2;
    int size2_;
    float limit;
    gpointer data;
} CostTable;


CostTable *table_init(CostTable *f,int size1,int size2,float limit);

/**
 * return EMPTY_VALUE if no entry found
 * corresponding value otherwise
 */
Dollar table_peek(const CostTable *table,gconstpointer element);

/**
 * IMPORTANT: value is not updated if key already exist
 * return table_peek(element) before insert
 */
Dollar table_insert(CostTable *table,gconstpointer element, Dollar cost);

/**
 * size1 is size of element
 * size2 is number of entries in hashtable
 */
int table_hash(gconstpointer element,int size1,int size2);

void table_free(CostTable *table);

void table_print(GLogLevelFlags flags,CostTable *table);



#endif
