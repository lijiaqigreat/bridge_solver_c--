#include "manager.h"
#include <string.h>
Double quickselect(gpointer block,gpointer tmp,int size,gint32 b,gint32 c);

TaskQueue *queue_init(int size1,int size2,int size3){
    TaskQueue* f=g_new(TaskQueue,1);
    f->size1=size1;
    f->size2=size2;
    f->size3=size3;
    f->size3_=1;
    f->interval=g_new(Double,size3+1);
    f->interval[0]=G_MAXDOUBLE;
    f->dataSize=g_new(int,size3+1);
    f->data=g_new(gpointer,size3+1);
    f->data[0]=g_malloc(size1*(size2+1));
    return f;
}

gchar queue_insert(TaskQueue *f,gpointer element){
    Double key=*(Double*) element;
    int t=-1;
    while(f->interval[++t]<key){}
    //not split?
    if(f->dataSize[t]<f->size2){
        memcpy(f->data[t]+(f->dataSize[t]++)*f->size1,element,f->size1);
        //no split
        return 0;
    }else{
        gpointer block=f->data[t];
        memcpy(block+f->size2*f->size1,element,f->size1);
        gpointer tmp=(gpointer)g_malloc(f->size1);
        Double cutoff = quickselect(block,tmp,f->size1,(gint32)f->size2+1,(gint32)((f->size2+1)/2));
        g_free(tmp);
        int t2;
        //shift data
        for(t2=f->size3_;t2>t;t2--){
            f->data[t2]=f->data[t2-1];
            f->dataSize[t2]=f->dataSize[t2-1];
            f->interval[t2]=f->interval[t2-1];
        }
        f->dataSize[t]=(f->size2+1)/2;
        f->size3_++;
        //copy splited data
        if(t!=f->size3-1){
            int newSize=f->size2/2+1;
            f->data[t+1]=(gpointer)g_malloc(f->size1*(f->size2+1));
            f->dataSize[t+1]=newSize;
            memcpy(f->data[t+1],block+((f->size2+1)/2)*f->size1,newSize*f->size1);
            f->interval[t]=cutoff;

            //remove last data
            if(f->size3_>f->size3){
                f->size3_--;
                g_free(f->data[f->size3]);
                f->interval[f->size3-1]=G_MAXDOUBLE;
                //split and remove tail
                return 3;
            }
            //split without remove tail
            return 2;
        }else{
            f->size3_--;
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
    int t;
    queue->size3_--;
    for(t=0;t<queue->size3_;t++){
        queue->data[t]=queue->data[t+1];
    }
    if(queue->size3_==0){
        queue->data[0]=g_malloc(queue->size1*(queue->size2+1));
        queue->size3_++;
    }
    return f;
}

#define SWAP(b1,b2,tmp,size) memcpy
#define KEY(p) (*(Double*)(p))
Double quickselect(gpointer block,gpointer tmp,int size,gint32 b,gint32 c){
    gint32 r1,r2;
    Double key=G_MAXDOUBLE;
    while(c!=b&&c!=0){
        r1=g_random_int_range(0,b);
        memcpy(tmp,block+(r1*size),size);
        memcpy(block+(r1*size),block,size);
        r2=0;
        key=KEY(tmp);
        for(r1=1;r1<b;r1++){
            if(KEY(block+r1*size)<key){
                memcpy(block+r2*size,block+r1*size,size);
                r2++;
                memcpy(block+r1*size,block+r2*size,size);
            }
        }
        memcpy(block+r2*size,tmp,size);
        if(r2<=c){
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
