#ifndef QUEUE_H
#define QUEUE_H
#include "base_type.h"
/*
 * structure of element:
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

Double quickselect(gpointer block,gpointer tmp,int size,gint32 b,gint32 c);

TaskQueue *queue_init(TaskQueue *f,int size1,int size2,int size3);

gchar queue_insert(TaskQueue *queue,gpointer element);

gpointer queue_pull(TaskQueue *queue);

void queue_free(TaskQueue *queue);

void queue_print(TaskQueue *queue);

#endif
