#include "queue.h"

TaskQueue *queue_init(TaskQueue *f,int size1,int size2,int size3){
    if(f==NULL){
        f=g_new(TaskQueue,1);
    }
    f->size1=size1;
    f->size2=size2;
    f->size3=size3;
    f->size3_=1;
    f->interval=g_new(Double,size3+1);
    f->interval[0]=G_MAXDOUBLE;
    f->dataSize=g_new(int,size3+1);
    f->data=g_new(gpointer,size3+1);
    f->data[0]=g_malloc(size1*(size2+1));
    f->dataSize[0]=0;
    return f;
}

gchar queue_insert(TaskQueue *queue,gpointer element){
    Double key=*(Double*) element;
    int t=-1;
    while(queue->interval[++t]<key){}
    printf("queue insert test %5.5lf\n",*(Dollar*)element);
    //not split?
    if(queue->dataSize[t]<queue->size2){
        memcpy(queue->data[t]+(queue->dataSize[t]++)*queue->size1,element,queue->size1);
        //no split
        return 0;
    }else{
        queue_print(queue);
        printf("queue spliting\n");
        gpointer block=queue->data[t];
        memcpy(block+queue->size2*queue->size1,element,queue->size1);
        gpointer tmp=(gpointer)g_malloc(queue->size1);
        printf("queue spliting\n");
        Double cutoff = quickselect(block,tmp,queue->size1,(gint32)queue->size2+1,(gint32)((queue->size2+1)/2));
        printf("queue spliting\n");
        g_free(tmp);
        int t2;
        printf("queue spliting\n");
        //shift data
        for(t2=queue->size3_;t2>t;t2--){
            queue->data[t2]=queue->data[t2-1];
            queue->dataSize[t2]=queue->dataSize[t2-1];
            queue->interval[t2]=queue->interval[t2-1];
        }
        printf("queue spliting\n");
        queue->dataSize[t]=(queue->size2+1)/2;
        queue->size3_++;
        printf("queue spliting\n");
        //copy splited data
        if(t!=queue->size3-1){
            printf("queue spliting\n");
            int newSize=queue->size2/2+1;
            queue->data[t+1]=(gpointer)g_malloc(queue->size1*(queue->size2+1));
            queue->dataSize[t+1]=newSize;
            memcpy(queue->data[t+1],block+((queue->size2+1)/2)*queue->size1,newSize*queue->size1);
            queue->interval[t]=cutoff;

            //remove last data
            if(queue->size3_>queue->size3){
                queue->size3_--;
                g_free(queue->data[queue->size3]);
                queue->interval[queue->size3-1]=G_MAXDOUBLE;
                //split and remove tail
                return 3;
            }
            printf("queue spliting\n");
            //split without remove tail
            return 2;
        }else{
            queue->size3_--;
            //split tailing block
            return 1;
        }
    }
}
gpointer queue_pull(TaskQueue *queue){
    if(queue->size3_==0){
        return 0;
    }
    gpointer f=queue->data[0];
    *(Dollar*)(f+queue->dataSize[0]*queue->size1)=EMPTY_VALUE;
    printf("pull: %1.5lf,%1.5lf\n",*(Dollar*)f,0.);

    int t;
    queue->size3_--;
    for(t=0;t<queue->size3_;t++){
        queue->data[t]=queue->data[t+1];
        queue->dataSize[t]=queue->dataSize[t+1];
    }
    if(queue->size3_==0){
        queue->data[0]=g_malloc(queue->size1*(queue->size2+1));
        queue->size3_++;
    }
    return f;
}

Double quickselect(gpointer block,gpointer tmp,int size,gint32 b,gint32 c){
    gint32 r1,r2;
    Double key=G_MAXDOUBLE;
    while(c!=b&&c!=0){
        r1=g_random_int_range(0,b);
        memcpy(tmp,block+(r1*size),size);
        memcpy(block+(r1*size),block,size);
        r2=0;
        key=GET_DOLLAR(tmp);
        for(r1=1;r1<b;r1++){
            if(GET_DOLLAR(block+r1*size)<key){
                memcpy(block+r2*size,block+r1*size,size);
                r2++;
                memcpy(block+r1*size,block+r2*size,size);
            }
        }
        memcpy(block+r2*size,tmp,size);
        if(r2<c){
            r2++;
            block+=r2*size;
            b-=r2;
            c-=r2;
        }else{
            b=r2;
        }
    }
    return key;
}
void queue_free(TaskQueue *queue){
    int t;
    for(t=0;t<queue->size3_;t++){
        g_free(queue->data[t]);
    }
    free(queue->interval);
    free(queue->dataSize);
    free(queue->data);
    free(queue);
}
#define KEY(p) (*(Dollar*)(p))
void queue_print(TaskQueue *queue){
    printf("--- queue ---\n");
    printf("%4d|%4d|%4d|%4d\n",queue->size1,queue->size2,queue->size3,queue->size3_);
    int t;
    for(t=0;t<queue->size3_;t++){
        Double a=queue->interval[t];
        int size=queue->dataSize[t];
        
        printf("block:%3d, size:%5d, interval: %5.3lf\n",t,size,a);
        int t2;
        for(t2=0;t2<size;t2++){
            printf("%5.3lf:%s\n",KEY(queue->data[t]+queue->size1*t2),print_bytes(queue->data[t]+queue->size1*t2+sizeof(Dollar),queue->size1-sizeof(Dollar)));
        }
    }
    printf("--- end queue ---\n");
}
