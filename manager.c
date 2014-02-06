#include "manager.h"
#include "Result.h"
#include "analyzer.h"
#include "optimizer.h"
#include <string.h>
#include <stdlib.h>

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


CostTable *table_init(CostTable *f,int size1,int size2,float limit){
    if(f==NULL){
        f=g_new(CostTable,1);
    }
    f->size1=size1;
    f->size2=size2;
    f->size2_=0;
    f->limit=limit;
    f->data=g_malloc((size1+sizeof(Dollar))*size2);
    int t;
    for(t=0;t<size2;t++){
        (*(Dollar*)(f->data+t*(f->size1+sizeof(Dollar))+f->size1))=EMPTY_VALUE;
    }
    return f;
}

int table_hash(gconstpointer element,int size1,int size2){
    gsize f=0;
    int t=0;
    for(t=0;t<size1;t++){
        f=f*65599+GET_BYTE(element+t);
    }
    return f%size2;
}

Dollar table_peek(const CostTable *table,gconstpointer element){
    int size1=table->size1;
    int i=table_hash(element,size1,table->size2);
    gpointer data=table->data+(size1+sizeof(Dollar))*i;
    while(GET_VALUE(data)!=EMPTY_VALUE){
        if(memcmp(element,data,size1)==0){
            return GET_VALUE(data);
        }else{
            data+=size1+sizeof(Dollar);
            i++;
            if(i==table->size2){
                data=table->data;
                i=0;
            }
        }
    }
    return EMPTY_VALUE;
}

Dollar _table_insert(CostTable *table,gconstpointer element,Dollar cost);

Dollar table_insert(CostTable *table,gconstpointer element,Dollar cost){
    //expand?
    if(table->size2*table->limit<table->size2_+1){
        int size1=table->size1;
        int size2=table->size2;
        //old data
        gpointer data1=table->data;
        gpointer data1_=data1;
        //new data
        table->data=g_malloc((size1+sizeof(Dollar))*size2*2);
        table->size2*=2;
        table->size2_=0;

        //initialize data2
        int t;
        for(t=0;t<size2*2;t++){
            GET_VALUE(table->data+t*(size1+sizeof(Dollar)))=EMPTY_VALUE;
        }

        //insert all key-value pairs
        for(t=0;t<size2;t++){
            if(GET_VALUE(data1)!=EMPTY_VALUE){
                _table_insert(table,data1,GET_VALUE(data1));
            }
            data1+=size1+sizeof(Dollar);
        }
        free(data1_);
        data1_=table->data;
    }
    //get hash
    return _table_insert(table,element,cost);
}

Dollar _table_insert(CostTable *table,gconstpointer element,Dollar cost){
    int size1=table->size1;
    int size2=table->size2;
    int i=table_hash(element,size1,size2);
    gpointer data=table->data+(size1+sizeof(Dollar))*i;

    //probe
    while(*(Dollar*)(data+size1)!=EMPTY_VALUE){
        if(memcmp(element,data,size1)==0){
            //TODO key exist
            return *(Dollar*)(data+size1);
        }else{
            data+=size1+sizeof(Dollar);
            i++;
            if(i==size2){
                data=table->data;
                i=0;
            }
        }
    }
    //copy key, value
    memcpy(data,element,size1);
    memcpy(data+size1,&cost,sizeof(Dollar));
    //increase size
    table->size2_++;
    return EMPTY_VALUE;
}

void table_free(CostTable *table){
    free(table->data);
    free(table);
}

void table_print(CostTable *table){
    printf("--- table ---\n");
    printf("%4d|%4d|%4d|%1.2f\n",table->size1,table->size2,table->size2_,table->limit);
    //print all key-value pairs
    int t;
    gpointer data=table->data;
    int size1=table->size1;
    for(t=0;t<table->size2;t++){
        if(GET_VALUE(data)!=EMPTY_VALUE){
            printf("%s:%5.2f\n",print_bytes(data,table->size1),GET_VALUE(data));
        }
        data+=size1+sizeof(Dollar);
    }
    printf("--- end table ---\n");
}

Manager *manager_init(Manager *f,const BridgeInfo *bridge,int queueSize2,int queueSize3,float tableLimit){
    printf("test1!\n");
    if(f==NULL){
        f=g_new0(Manager,1);
    }
    printf("test!\n");
    f->bridge=bridge;
    //freeJointSize
    int freeJointSize=bridge->totalJointSize-bridge->fixedJointSize;
    int hintSize=TYPE_HINT_COST_SIZE(bridge->memberSize);
    int queueSize1=hintSize+freeJointSize;

    printf("test!\n");
    queue_init(&f->queue,queueSize1,queueSize2,queueSize3);
    table_init(&f->table,freeJointSize,TABLE_SIZE,tableLimit);
    printf("test!\n");

    gpointer task=g_malloc(queueSize1);
    f->min=task;
    //set typeHint
    memcpy(task,&bridge->typeHint,hintSize);
    //set positionHint
    memset(task+hintSize,0,freeJointSize);

    queue_insert(&f->queue,task);
    queue_print(&f->queue);
    printf("test1!\n");

    return f;
}
#define UPDATE_TASK(value, b2, tmp, tmp2, manager, hintSize) \
        *tmp2=b2; \
        value=table_peek(&manager->table,tmp+hintSize); \
        if(value==EMPTY_VALUE){ \
            printf("inserting %d\n",t);\
            queue_insert(&manager->queue,tmp); \
            printf("inserted  %d\n",t);\
        }else{\
        }

int manager_update(Manager *manager,gconstpointer task,int taskSize){
    int freeJointSize=manager->bridge->totalJointSize-manager->bridge->fixedJointSize;
    int hintSize=TYPE_HINT_COST_SIZE(manager->bridge->memberSize);
    int t;
    int tt;
    int count=0;
    Dollar value;
    for(tt=0;tt<taskSize;tt++){
        value=*(Dollar*)task;
        if(value==EMPTY_VALUE){
            continue;
        }
        if(value<*(Dollar*)(manager->min)){
            memcpy(manager->min,task,hintSize+freeJointSize);
        }
        value=table_insert(&manager->table,task+hintSize,value);
        if(value!=EMPTY_VALUE){
            g_assert(value==*(Dollar*)task);
            continue;
        }
        value=*(Dollar*)task;
        printf("update adding seed!\n");
        
        gpointer tmp=g_malloc(freeJointSize+hintSize);
        Byte *tmp2=(Byte*)(tmp+hintSize);
        memcpy(tmp,task,hintSize+freeJointSize);
        for(t=0;t<freeJointSize;t++){
            printf("seed!\n");
            Byte b=*(tmp2);
            Byte b2;
            b2=b^((b^(b+1))&15);
            UPDATE_TASK(value,b2,tmp,tmp2,manager,hintSize);
            b2=b^((b^(b-1))&15);
            UPDATE_TASK(value,b2,tmp,tmp2,manager,hintSize);
            b2=b^((b^(b+16))&240);
            UPDATE_TASK(value,b2,tmp,tmp2,manager,hintSize);
            b2=b^((b^(b-16))&240);
            UPDATE_TASK(value,b2,tmp,tmp2,manager,hintSize);
            printf("seed!\n");
            *tmp2=b;
            printf("seed!\n");

            tmp2++;
        }
        task+=hintSize+freeJointSize;
    }
    return 0;
}

void main_work(Manager *manager){
    printf("start work\n");
    int freeJointSize=manager->bridge->totalJointSize-manager->bridge->fixedJointSize;
    int hintSize=TYPE_HINT_COST_SIZE(manager->bridge->memberSize);
    gpointer task=queue_pull(&manager->queue);
    gpointer task_=task;
    OptimizeTask otask;
    Result* result=(Result*)malloc(sizeof(Result));
    printf("end cost:%lf\n",*(Dollar*)(task+hintSize+freeJointSize));
    int count=0;
    printf("start work\n");
    while(*(Dollar*)task!=EMPTY_VALUE){
        printf("in work!\n");
        int n=analyze(result,&otask,manager->bridge,task);
        printf("return: %d\n",n);
        n=optimize(task, &otask,TRUE);
        printf("return: %d\n",n);
        task+=hintSize+freeJointSize;
        count++;
    }
    printf("start work\n");
    manager_update(manager,task_,count);
}

char* print_bytes(gconstpointer p,int size){
    static char buf[10000];
    int t=0;
    for(t=0;t<size;t++){
        sprintf(buf+t*2,"%02X",GET_BYTE(p+t));
    }
    buf[size*2]=0;
    return buf;
}

