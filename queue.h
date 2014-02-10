#ifndef QUEUE_H
#define QUEUE_H
#include "base_type.h"
/*
 *
 * structure of element:
 *  cost: sizeof(Dollar)
 *  positionHintB: size1-sizeof(Dollar)
 */
typedef struct _TaskQueue{
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
     * always greater than 0
     */
    int size3_;
    /**
     * has length of size3+1
     * key in data[i] are less than interval[t] for all i<=t
     */
    Double *interval;
    /**
     * has length of size3+1
     * dataSize[t] is the actual number of elements in data[t]
     */
    int* dataSize;
    /**
     * array of arrays storing blocks
     * first size3_ array are valid, others should not be accessed
     */
    gpointer* data;
} TaskQueue;

/**
 * reorder block such that block[t].cost<block[c].cost iff t<c
 * equality are not handled strictly
 * it is not stable
 * each element has <code>size</code>byte
 * b is the number of elements
 */
Double quickselect(gpointer block,gpointer tmp,int size,gint32 b,gint32 c);

TaskQueue *queue_init(TaskQueue *f,int size1,int size2,int size3);

/**
 * return
 * 0 when no split happened
 * 1 when split tailing block
 * 2 when split without remove tail
 * 3 when split and remove tail
 */
gchar queue_insert(TaskQueue *queue,gconstpointer element);


gpointer queue_pull(TaskQueue *queue);

void queue_free(TaskQueue *queue);

void queue_print(TaskQueue *queue);

#endif
