void quickselect(gpointer block,gpointer tmp,int size,gint32 b,gint32 c);

TaskQueue *queue_init(int size1,int size2,int size3){
    TaskQueue f=g_new(TaskQueue,1);
    f->size1=size1;
    f->size2=size2;
    f->size3=size3;
    f->size3_=0;
    f->interval=g_new(Double,size3);
    f->interval[0]=G_MAXDOUBLE;
    f->dataSize=g_new(int,size3);
    f->data=g_new(gpointer,size3);
    return f;
}

gchar queue_insert(TaskQueue *queue,gpointer element){
    Double key=*(Double*)element;
    int t=-1;
    while(f->interval[++t]<key){}
    if(f->dataSize[t]<size2){
        memcpy(f->data[f->dataSize[t]++],element,f->size1);
    }else{
        gpointer block=f->data[t];
        gpointer tmp=(gpointer)malloc(s1);
        quickselect(block,tmp,f->size1,f->size2,f->size2/2);
        free(tmp);
        int t2;
        for(t2=f->size3_;t2>t;t2--){
            f->data[t2]=f->data[t2-1];
            f->dataSize[t2]=f->data[t2-1];
            f->interval[t2]=f->interval[t2-1];
        }
        f->dataSize[t]=f->size2/2;
        f->data[t+1]=(gpointer)malloc(f->size1*f->size2);
        f->dataSize[t+1]=
        memcpy(f->data[t+1],block+f->size2/2*f->size1,f->size2*f->size1);
        f->dataSize[t+1]=f->
        
        
    }
    
}
#define SWAP(b1,b2,tmp,size) memcpy
void quickselect(gpointer block,gpointer tmp,int size,gint32 b,gint32 c){
    gint32 r1,r2;
    while(c!=b&&c!=0){
        r1=g_random_int_range(0,b);
        memcpy(tmp,block+(r1*size),size);
        memcpy(block+(r1*size),block,size);
        r2=0;
        Double key=KEY(tmp);
        for(r1=1;r1<b;r1++){
            if(KEY(block+r1*size)<key){
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
}
